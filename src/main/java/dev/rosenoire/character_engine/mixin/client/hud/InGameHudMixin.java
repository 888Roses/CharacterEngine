package dev.rosenoire.character_engine.mixin.client.hud;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {
    /// Disables the rendering of the hotbar when in survival or adventure mode.
    @Inject(at = @At("HEAD"), method = "renderHotbar", cancellable = true)
    private void characterEngine$DisableHotbarRendering(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        var client = MinecraftClient.getInstance();
        var player = client.player;

        if (player == null || player.isCreative() || player.isSpectator()) {
            return;
        }

        ci.cancel();
    }
}
