package dev.rosenoire.character_engine.common.item;

import com.zigythebird.playeranim.animation.PlayerAnimationController;
import com.zigythebird.playeranim.api.PlayerAnimationAccess;
import com.zigythebird.playeranimcore.animation.Animation;
import dev.rosenoire.character_engine.cca.components.CameraShakeComponent;
import dev.rosenoire.character_engine.client.animation.ModAnimationControllerIndex;
import dev.rosenoire.character_engine.common.index.ModAnimationIndex;
import dev.rosenoire.character_engine.common.index.ModItemIndex;
import dev.rosenoire.character_engine.foundation.index.AnimationIndex;
import dev.rosenoire.character_engine.foundation.item.AttackItem;
import dev.rosenoire.character_engine.foundation.item.TickingItem;
import dev.rosenoire.character_engine.foundation.player.CameraShake;
import dev.rosenoire.character_engine.foundation.player.PlayerActions;
import net.collectively.geode.core.math;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.PlayerLikeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

@SuppressWarnings({"SameParameterValue", "unused"})
public class BlasterItem extends Item implements TickingItem, AttackItem {
    public BlasterItem(Settings settings) {
        super(settings);
    }

    public static void onHandStackChanged(PlayerEntity player, ItemStack previousStack, ItemStack itemStack, Hand hand) {
        if (ensurePlayerHoldingStack(player, hand)) {
            PlayerAnimationController controller = playAnimationInHand(
                    player,
                    hand,
                    ModAnimationIndex.BLASTER__RIGHT_ARM_DRAW,
                    ModAnimationIndex.BLASTER__LEFT_ARM_DRAW
            );

            if (controller != null) {
                Animation currentAnimation = controller.getCurrentAnimationInstance();
                if (currentAnimation != null) {
                    enqueueIdleAnimationAfterTime(player, hand, 0.45f);
                }
            }
        }
    }

    private static void enqueueIdleAnimationAfterTime(PlayerEntity player, Hand hand, float currentAnimationLength) {
        PlayerActions.enqueue(
                player,
                math.ceil(currentAnimationLength * 20f),
                () -> {
                    if (ensurePlayerHoldingStack(player, hand)) {
                        PlayerAnimationController controller = (PlayerAnimationController) PlayerAnimationAccess.getPlayerAnimationLayer(
                                player,
                                getAnimationControllerId(player, hand)
                        );

                        // This should not work. This in fact should have no reason to work. Why does this work? Idfk.
                        if (controller != null && (!controller.isActive() || controller.getAnimationTime() > 100f)) {
                            playAnimationInHand(
                                    player,
                                    hand,
                                    ModAnimationIndex.BLASTER__RIGHT_ARM_IDLE,
                                    ModAnimationIndex.BLASTER__LEFT_ARM_IDLE
                            );
                        }
                    }
                }
        );
    }

    private static boolean ensurePlayerHoldingStack(PlayerLikeEntity player, Hand hand) {
        ItemStack stackInOffHand = player.getStackInHand(Hand.OFF_HAND);

        boolean isWeaponInMainHand = player.getStackInHand(Hand.MAIN_HAND).getItem() instanceof BlasterItem;
        boolean isWeaponInOffHand = player.getStackInHand(Hand.OFF_HAND).getItem() instanceof BlasterItem;

        if (!isWeaponInMainHand) {
            PlayerAnimationController controller = (PlayerAnimationController) PlayerAnimationAccess.getPlayerAnimationLayer(
                    player,
                    getAnimationControllerId(player, Hand.MAIN_HAND)
            );

            if (controller != null) {
                Animation currentAnimation = controller.getCurrentAnimationInstance();

                if (currentAnimation != null) {
                    String name = currentAnimation.getNameOrId();

                    if (AnimationIndex.isRegistered(name)) {
                        controller.stop();
                    }
                }
            }
        }

        if (!isWeaponInOffHand) {
            PlayerAnimationController controller = (PlayerAnimationController) PlayerAnimationAccess.getPlayerAnimationLayer(
                    player,
                    getAnimationControllerId(player, Hand.OFF_HAND)
            );

            if (controller != null) {
                Animation currentAnimation = controller.getCurrentAnimationInstance();

                if (currentAnimation != null) {
                    String name = currentAnimation.getNameOrId();

                    if (AnimationIndex.isRegistered(name)) {
                        controller.stop();
                    }
                }
            }
        }

        return hand == Hand.MAIN_HAND ? isWeaponInMainHand : isWeaponInOffHand;
    }

    private static PlayerAnimationController playAnimationInHand(PlayerLikeEntity player, Hand hand, Identifier rightAnim, Identifier leftAnim) {
        Identifier animationId = (hand == Hand.MAIN_HAND) == (player.getMainArm() == Arm.RIGHT) ? rightAnim : leftAnim;
        return playAnimationSafe(player, getAnimationControllerId(player, hand), animationId);
    }

    private static Identifier getAnimationControllerId(PlayerLikeEntity player, Hand hand) {
        return (hand == Hand.MAIN_HAND) == (player.getMainArm() == Arm.RIGHT)
                ? ModAnimationControllerIndex.BLASTER__RIGHT_HAND
                : ModAnimationControllerIndex.BLASTER__LEFT_HAND;
    }

    private static PlayerAnimationController playAnimationSafe(PlayerLikeEntity player, Identifier controllerId, Identifier animationId) {
        PlayerAnimationController controller = (PlayerAnimationController) PlayerAnimationAccess.getPlayerAnimationLayer(
                player,
                controllerId
        );

        if (controller != null) {
            controller.triggerAnimation(animationId);
        }

        return controller;
    }

    @Override
    public void tickInHand(LivingEntity livingEntity, ItemStack itemStack, Hand hand) {
        livingEntity.bodyYaw = livingEntity.headYaw;
    }

    // TODO: Bad kitten!
    private static int handSwitcher = 0;

    @Override
    public boolean onSwing(LivingEntity livingEntity, ItemStack itemStack) {
        if (livingEntity instanceof PlayerLikeEntity playerLikeEntity) {
            boolean hasGunInOffHand = playerLikeEntity.getOffHandStack().isOf(ModItemIndex.BLASTER);
            Hand hand = !hasGunInOffHand ? Hand.MAIN_HAND : handSwitcher % 4 == 0 ? Hand.MAIN_HAND : Hand.OFF_HAND;

            playAnimationInHand(
                    playerLikeEntity,
                    hand,
                    ModAnimationIndex.BLASTER__RIGHT_ARM_FIRE,
                    ModAnimationIndex.BLASTER__LEFT_ARM_FIRE
            );

            if (!hasGunInOffHand && handSwitcher % 4 != 0) handSwitcher += 1;
            handSwitcher += hasGunInOffHand ? 1 : 4;

            playerLikeEntity.setPitch(playerLikeEntity.getPitch() - 1);
            playerLikeEntity.setYaw(playerLikeEntity.getYaw() + MathHelper.nextFloat(playerLikeEntity.getRandom(), -0.4f, 0.4f));

            if (playerLikeEntity instanceof PlayerEntity player) {
                enqueueIdleAnimationAfterTime(player, hand, 1f);

                CameraShake.shake(
                        player,
                        2,
                        0.005,
                        CameraShakeComponent.EasingFunction.LINEAR
                );
            }
        }

        return false;
    }
}
