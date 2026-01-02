package dev.rosenoire.character_engine.client.animation;

import com.zigythebird.playeranim.animation.PlayerAnimationController;
import com.zigythebird.playeranim.api.PlayerAnimationFactory;
import com.zigythebird.playeranimcore.animation.layered.modifier.AdjustmentModifier;
import com.zigythebird.playeranimcore.api.firstPerson.FirstPersonConfiguration;
import com.zigythebird.playeranimcore.api.firstPerson.FirstPersonMode;
import com.zigythebird.playeranimcore.enums.PlayState;
import com.zigythebird.playeranimcore.math.Vec3f;
import dev.rosenoire.character_engine.common.CharacterEngine;
import dev.rosenoire.character_engine.foundation.animation.StandardParticleKeyframeHandler;
import dev.rosenoire.character_engine.foundation.animation.StandardSoundKeyframeHandler;
import net.collectively.geode.core.math;
import net.minecraft.entity.PlayerLikeEntity;
import net.minecraft.util.Identifier;

import java.util.Optional;

public interface ModAnimationControllerIndex {
    Identifier BLASTER__RIGHT_HAND = CharacterEngine.id("blaster__right_hand");
    Identifier BLASTER__LEFT_HAND = CharacterEngine.id("blaster__left_hand");

    static void initialize() {
        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(
                BLASTER__RIGHT_HAND,
                1000,
                player -> {
                    PlayerAnimationController controller = new PlayerAnimationController(
                            player,
                            (i, j, k) -> PlayState.CONTINUE
                    );

                    controller.setSoundKeyframeHandler(new StandardSoundKeyframeHandler());
                    controller.setParticleKeyframeHandler(new StandardParticleKeyframeHandler());
                    controller.registerPlayerAnimBone("muzzle_flash");
                    controller.setFirstPersonMode(FirstPersonMode.THIRD_PERSON_MODEL);
                    controller.setFirstPersonConfiguration(new FirstPersonConfiguration(
                            true,
                            true,
                            true,
                            true,
                            true
                    ));

                    controller.addModifierLast(new AdjustmentModifier(partName -> createPitchModifier(
                            partName,
                            controller,
                            "right_arm"
                    )));

                    return controller;
                }
        );

        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(
                BLASTER__LEFT_HAND,
                1000,
                player -> {
                    PlayerAnimationController controller = new PlayerAnimationController(
                            player,
                            (i, j, k) -> PlayState.CONTINUE
                    );

                    controller.setSoundKeyframeHandler(new StandardSoundKeyframeHandler());
                    controller.setParticleKeyframeHandler(new StandardParticleKeyframeHandler());
                    controller.setFirstPersonMode(FirstPersonMode.THIRD_PERSON_MODEL);
                    controller.setFirstPersonConfiguration(new FirstPersonConfiguration(
                            true,
                            true,
                            true,
                            true,
                            true
                    ));

                    controller.addModifierLast(new AdjustmentModifier(partName -> createPitchModifier(
                            partName,
                            controller,
                            "left_arm"
                    )));

                    return controller;
                }
        );
    }

    private static Optional<AdjustmentModifier.PartModifier> createPitchModifier(
            String partName,
            PlayerAnimationController controller,
            String targetPartName
    ) {
        if (!partName.equals(targetPartName)) {
            return Optional.empty();
        }

        PlayerLikeEntity playerLikeEntity = controller.getAvatar();

        if (playerLikeEntity == null) {
            return Optional.empty();
        }

        float rotation = playerLikeEntity.getPitch() + 0;
        return Optional.of(new AdjustmentModifier.PartModifier(
                new Vec3f(math.deg2rad(rotation), 0, 0),
                new Vec3f(0, 0, 0)
        ));
    }

}
