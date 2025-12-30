package net.collectively.geode.mc.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderTickCounter;

/**
 * Collection of client rendering utils.
 */
@Environment(EnvType.CLIENT)
public interface RenderHelper {
    /**
     * Gets the tick delta of the application for that render tick.
     *
     * @see RenderTickCounter#getTickProgress(boolean)
     */
    static float getTickDelta() {
        MinecraftClient client = MinecraftClient.getInstance();
        RenderTickCounter counter = client.getRenderTickCounter();
        return counter.getTickProgress(true);
    }
}
