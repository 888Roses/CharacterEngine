package dev.rosenoire.character_engine.client.animation;

import com.zigythebird.playeranim.animation.PlayerAnimationController;
import com.zigythebird.playeranim.api.PlayerAnimationFactory;
import com.zigythebird.playeranimcore.api.firstPerson.FirstPersonConfiguration;
import com.zigythebird.playeranimcore.api.firstPerson.FirstPersonMode;
import com.zigythebird.playeranimcore.enums.PlayState;
import dev.rosenoire.character_engine.common.CharacterEngine;
import net.minecraft.util.Identifier;

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

                    controller.setFirstPersonMode(FirstPersonMode.THIRD_PERSON_MODEL);
                    controller.setFirstPersonConfiguration(new FirstPersonConfiguration(
                            true,
                            false,
                            true,
                            false,
                            true
                    ));

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

                    controller.setFirstPersonMode(FirstPersonMode.THIRD_PERSON_MODEL);
                    controller.setFirstPersonConfiguration(new FirstPersonConfiguration(
                            false,
                            true,
                            false,
                            true,
                            true
                    ));

                    return controller;
                }
        );
    }
}
