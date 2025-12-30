package dev.rosenoire.character_engine.common.item;

import com.zigythebird.playeranim.animation.PlayerAnimationController;
import com.zigythebird.playeranim.animation.keyframe.event.builtin.AutoPlayingSoundKeyframeHandler;
import com.zigythebird.playeranim.api.PlayerAnimationAccess;
import com.zigythebird.playeranimcore.animation.Animation;
import dev.rosenoire.character_engine.client.animation.ModAnimationControllerIndex;
import dev.rosenoire.character_engine.common.index.ModAnimationIndex;
import dev.rosenoire.character_engine.foundation.index.AnimationIndex;
import dev.rosenoire.character_engine.foundation.player.PlayerActions;
import net.collectively.geode.core.math;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;

@SuppressWarnings({"SameParameterValue", "unused"})
public class BlasterItem extends Item {
    public BlasterItem(Settings settings) {
        super(settings);
    }

    public static void onHandStackChanged(PlayerEntity player, ItemStack previousStack, ItemStack itemStack, Hand hand) {
        if (ensurePlayerHoldingStack(player, hand)) {
            PlayerAnimationController controller = playAnimation(
                    player,
                    hand,
                    ModAnimationIndex.BLASTER__RIGHT_ARM_DRAW,
                    ModAnimationIndex.BLASTER__LEFT_ARM_DRAW
            );

            if (controller != null) {
                Animation currentAnimation = controller.getCurrentAnimationInstance();
                if (currentAnimation != null) {
                    PlayerActions.enqueue(
                            player,
                            math.round(0.46f * 20f),
                            () -> {
                                if (ensurePlayerHoldingStack(player, hand)) {
                                    playAnimation(
                                            player,
                                            hand,
                                            ModAnimationIndex.BLASTER__RIGHT_ARM_IDLE,
                                            ModAnimationIndex.BLASTER__LEFT_ARM_IDLE
                                    );
                                }
                            }
                    );
                }
            }
        }
    }

    private static boolean ensurePlayerHoldingStack(PlayerEntity player, Hand hand) {
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

    private static PlayerAnimationController playAnimation(PlayerEntity player, Hand hand, Identifier rightAnim, Identifier leftAnim) {
        Identifier animationId = (hand == Hand.MAIN_HAND) == (player.getMainArm() == Arm.RIGHT) ? rightAnim : leftAnim;
        return playAnimationSafe(player, getAnimationControllerId(player, hand), animationId);
    }

    private static Identifier getAnimationControllerId(PlayerEntity player, Hand hand) {
        return (hand == Hand.MAIN_HAND) == (player.getMainArm() == Arm.RIGHT)
                ? ModAnimationControllerIndex.BLASTER__RIGHT_HAND
                : ModAnimationControllerIndex.BLASTER__LEFT_HAND;
    }

    private static PlayerAnimationController playAnimationSafe(PlayerEntity player, Identifier controllerId, Identifier animationId) {
        PlayerAnimationController controller = (PlayerAnimationController) PlayerAnimationAccess.getPlayerAnimationLayer(
                player,
                controllerId
        );

        if (controller != null) {
            controller.triggerAnimation(animationId);
        }

        return controller;
    }
}
