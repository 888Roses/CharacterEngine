package dev.rosenoire.character_engine.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.rosenoire.character_engine.cca.ModEntityComponentIndex;
import dev.rosenoire.character_engine.cca.components.PlayerFovComponent;
import net.collectively.geode.core.math;
import net.collectively.geode.core.types.double3;
import net.collectively.geode.mc.util.EntityHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @ModifyReturnValue(
            at = @At("RETURN"),
            method = "getFov"
    )
    private float getFov$CharacterEngine(float original, Camera camera, float tickProgress, boolean changingFov) {
        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerEntity player = client.player;

        if (player == null) {
            return original;
        }

        PlayerFovComponent component = ModEntityComponentIndex.PLAYER_FOV.get(player);
        return original * (float) component.getSmoothFov(tickProgress);
    }
}
