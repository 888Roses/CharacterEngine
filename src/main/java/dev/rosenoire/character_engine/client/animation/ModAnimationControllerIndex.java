package dev.rosenoire.character_engine.client.animation;

import com.zigythebird.playeranim.animation.PlayerAnimationController;
import com.zigythebird.playeranim.api.PlayerAnimationFactory;
import com.zigythebird.playeranimcore.animation.layered.modifier.AdjustmentModifier;
import com.zigythebird.playeranimcore.animation.layered.modifier.MirrorModifier;
import com.zigythebird.playeranimcore.api.firstPerson.FirstPersonConfiguration;
import com.zigythebird.playeranimcore.api.firstPerson.FirstPersonMode;
import com.zigythebird.playeranimcore.enums.PlayState;
import com.zigythebird.playeranimcore.math.Vec3f;
import dev.rosenoire.character_engine.common.CharacterEngine;
import dev.rosenoire.character_engine.common.index.ModItemIndex;
import dev.rosenoire.character_engine.foundation.animation.ParticleKeyframeHandler;
import dev.rosenoire.character_engine.foundation.animation.StandardSoundKeyframeHandler;
import net.collectively.geode.core.math;
import net.minecraft.entity.PlayerLikeEntity;
import net.minecraft.util.Arm;
import net.minecraft.util.Identifier;

import java.util.Optional;

public interface ModAnimationControllerIndex {
    Identifier BLASTER = CharacterEngine.id("blaster__right_hand");
    Identifier BLASTER_MIRRORED = CharacterEngine.id("blaster__left_hand");

    private static PlayerAnimationController createStandardPlayerAnimationController(PlayerLikeEntity playerLike, boolean isMirrored) {
        PlayerAnimationController controller = new PlayerAnimationController(playerLike, (i, j, k) -> PlayState.CONTINUE);

        controller.setSoundKeyframeHandler(new StandardSoundKeyframeHandler());
        controller.setParticleKeyframeHandler(new ParticleKeyframeHandler());

        controller.setFirstPersonMode(FirstPersonMode.THIRD_PERSON_MODEL);
        controller.setFirstPersonConfiguration(new FirstPersonConfiguration(true, true, true, true, true));

        controller.addModifierLast(new AdjustmentModifier(partName -> rotateWithPitch(partName, controller)));

        if (isMirrored) {
            controller.addModifierLast(new MirrorModifier());
        }

        return controller;
    }

    @SuppressWarnings("SameParameterValue")
    private static Optional<AdjustmentModifier.PartModifier> rotateWithPitch(String partName, PlayerAnimationController controller) {
        if (!partName.equals("right_arm") && !partName.equals("left_arm")) {
            return Optional.empty();
        }

        PlayerLikeEntity playerLikeEntity = controller.getAvatar();

        if (playerLikeEntity == null) {
            return Optional.empty();
        }

        float pitch = math.deg2rad(playerLikeEntity.getPitch());
        boolean hasGunInOtherHand = playerLikeEntity.getOffHandStack().isOf(ModItemIndex.BLASTER);
        boolean isLeftHand = partName.equals("left_arm");
        boolean shouldBeDivided = hasGunInOtherHand && ((isLeftHand && playerLikeEntity.getMainArm() == Arm.RIGHT) || (!isLeftHand && playerLikeEntity.getMainArm() == Arm.LEFT));

        return Optional.of(new AdjustmentModifier.PartModifier(
                new Vec3f(pitch * (shouldBeDivided ? 0.5f : 1), 0, 0),
                new Vec3f(0, 0, 0)
        ));
    }

    static void initialize() {
        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(
                BLASTER, 1000,
                playerLike -> createStandardPlayerAnimationController(playerLike, false)
        );

        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(
                BLASTER_MIRRORED, 1000,
                playerLike -> createStandardPlayerAnimationController(playerLike, true)
        );
    }
}
