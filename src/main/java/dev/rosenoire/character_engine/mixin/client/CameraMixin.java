package dev.rosenoire.character_engine.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.rosenoire.character_engine.cca.ModEntityComponentIndex;
import dev.rosenoire.character_engine.cca.components.CameraShakeComponent;
import net.collectively.geode.core.math;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.Camera;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Camera.class)
public abstract class CameraMixin {
    @Shadow
    private float yaw;

    @Shadow
    private float pitch;

    @ModifyReturnValue(method = "getRotation", at = @At("RETURN"))
    private Quaternionf update$characterEngine(Quaternionf original) {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;

        if (player == null) {
            return original;
        }

        CameraShakeComponent component = player.getComponent(ModEntityComponentIndex.CAMERA_SHAKE);

        if (component.isShaking()) {
            float correctedPitch = (float) component.getCurrentShakeVector().x();
            float correctedYaw = (float) component.getCurrentShakeVector().y();
            return original.rotateX(correctedPitch).rotateY(correctedYaw);
        }

        return original;
    }
}
