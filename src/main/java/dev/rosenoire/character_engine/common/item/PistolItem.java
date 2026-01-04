package dev.rosenoire.character_engine.common.item;

import com.zigythebird.playeranim.animation.PlayerAnimationController;
import com.zigythebird.playeranimcore.api.firstPerson.FirstPersonConfiguration;
import dev.rosenoire.character_engine.common.CharacterEngine;
import dev.rosenoire.character_engine.common.animation.AnimationAccess;
import dev.rosenoire.character_engine.client.index.ModAnimationControllerIndex;
import dev.rosenoire.character_engine.common.index.ModAnimationIndex;
import net.collectively.geode.mc.item.SwingableItem;
import dev.rosenoire.character_engine.foundation.item.StackChangeAwareItem;
import dev.rosenoire.character_engine.foundation.item.TickingItem;
import net.collectively.geode_animations.controllers.AnimationQueueData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.PlayerLikeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;

public class PistolItem extends Item implements StackChangeAwareItem, SwingableItem, TickingItem {
    public PistolItem(Settings settings) {
        super(settings);
    }

    // region ----============---- Selection ----=============----

    @Override
    public void onSelected(PlayerEntity player, Hand hand, ItemStack previousItemStack, ItemStack currentItemStack) {
        // Stopping any animation if the player isn't holding a pistol and is playing a pistol animation.
        ensurePistolInHandForPistolAnimations(player);
    }

    @Override
    public void onDeselected(PlayerEntity player, Hand hand, ItemStack itemStack, ItemStack newItemStack) {
        // Stopping any animation if the player isn't holding a pistol and is playing a pistol animation.
        ensurePistolInHandForPistolAnimations(player);
    }

    // endregion

    // region ----=============---- Swinging ----=============----

    // true to execute base false otherwise
    @Override
    public boolean onSwing(LivingEntity livingEntity, ItemStack itemStack, Hand hand) {
        if (!(livingEntity instanceof PlayerLikeEntity playerLike)) {
            return true;
        }

        ensurePistolInHandForPistolAnimations(playerLike);

        boolean canShootOffHand = hasPistolInHand(playerLike, Hand.OFF_HAND);
        Hand handThatShoots = !canShootOffHand || livingEntity.age % 2 == 1 ? Hand.MAIN_HAND : Hand.OFF_HAND;

        if (!AnimationQueueData.triggerAnimationServerSafe(
                playerLike,
                getControllerId(playerLike, handThatShoots),
                CharacterEngine.id("blaster.fire")
        )) {
            return true;
        }

        playerLike.setPitch(playerLike.getPitch() - 1);
        float randomYaw = MathHelper.nextFloat(playerLike.getRandom(), -0.4f, 0.4f);
        playerLike.setYaw(playerLike.getYaw() + randomYaw);

        return false;
    }

    // endregion

    // region ----==============---- Ticking ----=============----

    @Override
    public void tickInHand(LivingEntity livingEntity, ItemStack itemStack, Hand hand) {
        livingEntity.bodyYaw = livingEntity.headYaw;
        livingEntity.lastBodyYaw = livingEntity.lastHeadYaw;

        if (livingEntity instanceof PlayerLikeEntity playerLike) {
            PlayerAnimationController controller = getController(playerLike, hand);

            if (controller == null) {
                return;
            }

            if (!controller.isActive()) {
                // No need for server safe animation triggering in this one.
                AnimationQueueData.triggerAnimation(getController(playerLike, hand), CharacterEngine.id("blaster.draw"));

                boolean isRightHanded = playerLike.getMainArm() == Arm.RIGHT;
                Hand mainHand = isRightHanded ? Hand.MAIN_HAND : Hand.OFF_HAND;
                Hand offHand = isRightHanded ? Hand.OFF_HAND : Hand.MAIN_HAND;

                controller.setFirstPersonConfiguration(new FirstPersonConfiguration(
                        hasPistolInHand(playerLike, mainHand),
                        hasPistolInHand(playerLike, offHand),
                        hasPistolInHand(playerLike, mainHand),
                        hasPistolInHand(playerLike, offHand),
                        true
                ));
            }
        }
    }

    // endregion

    // region ----===========---- Item Checks ----============----

    public static boolean isPistol(ItemStack itemStack) {
        return itemStack.getItem() instanceof PistolItem;
    }

    public static boolean hasPistolInHand(PlayerLikeEntity playerLike, Hand hand) {
        return isPistol(playerLike.getStackInHand(hand));
    }

    // endregion

    // region ----==========---- Animation Help ----==========----

    /**
     * Makes sure that the {@code playerLike} entity is holding this item in order to play pistol animations. In other words,
     * stop any pistol animation from playing if the player is not holding a pistol.
     *
     * @param playerLike The entity.
     * @see #hasPistolInHand(PlayerLikeEntity, Hand)
     */
    private static void ensurePistolInHandForPistolAnimations(PlayerLikeEntity playerLike) {
        // Stops the animation if it's a pistol animation and the player isn't holding a pistol in that hand.
        for (Hand eachHand : Hand.values()) {
            boolean hasPistolInHand = hasPistolInHand(playerLike, eachHand);

            if (!hasPistolInHand) {
                PlayerAnimationController controller = getController(playerLike, eachHand);

                if (controller != null) {
                    if (ModAnimationIndex.isBlasterAnimation(controller.getCurrentAnimationInstance())) {
                        controller.stop();
                    }
                }
            }
        }
    }

    static @Nullable Identifier getControllerId(PlayerLikeEntity playerLike, Hand hand) {
        return AnimationAccess.getControllerIdForHand(
                playerLike,
                hand,
                ModAnimationControllerIndex.BLASTER,
                ModAnimationControllerIndex.BLASTER_MIRRORED
        );
    }

    static @Nullable PlayerAnimationController getController(PlayerLikeEntity playerLike, Hand hand) {
        return AnimationAccess.getControllerForHand(
                playerLike,
                hand,
                ModAnimationControllerIndex.BLASTER,
                ModAnimationControllerIndex.BLASTER_MIRRORED
        );
    }

    // endregion
}