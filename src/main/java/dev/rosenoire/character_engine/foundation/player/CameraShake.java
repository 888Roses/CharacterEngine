package dev.rosenoire.character_engine.foundation.player;

import dev.rosenoire.character_engine.cca.ModEntityComponentIndex;
import dev.rosenoire.character_engine.cca.components.CameraShakeComponent;
import net.minecraft.entity.player.PlayerEntity;

public interface CameraShake {
    static void shake(
            PlayerEntity player,
            long duration,
            double intensity,
            CameraShakeComponent.EasingFunction easingFunction
    ) {
        player.getComponent(ModEntityComponentIndex.CAMERA_SHAKE).shake(
                intensity,
                duration,
                easingFunction
        );
    }

    static void shake(
            PlayerEntity player,
            long duration,
            double intensity
    ) {
        shake(
                player,
                duration,
                intensity,
                CameraShakeComponent.EasingFunction.LINEAR
        );
    }
}