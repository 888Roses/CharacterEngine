package dev.rosenoire.character_engine.foundation.player;

import dev.rosenoire.character_engine.cca.ModEntityComponentIndex;
import dev.rosenoire.character_engine.cca.components.PlayerActionsComponent;
import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.Nullable;

public interface PlayerActions {
    static void enqueue(@Nullable PlayerEntity player, long delay, Runnable... actions) {
        if (player == null || actions.length == 0) {
            return;
        }

        PlayerActionsComponent component = ModEntityComponentIndex.ACTIONS.get(player);
        component.enqueue(delay, actions);
    }
}
