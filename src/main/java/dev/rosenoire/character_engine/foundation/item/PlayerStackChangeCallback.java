package dev.rosenoire.character_engine.foundation.item;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import org.jetbrains.annotations.Nullable;

public interface PlayerStackChangeCallback {
    Event<PlayerStackChangeCallback> EVENT = EventFactory.createArrayBacked(
            PlayerStackChangeCallback.class,
            (listeners) -> (PlayerEntity player, ItemStack previousStack, ItemStack itemStack, Hand hand) -> {
                for (PlayerStackChangeCallback listener : listeners) {
                    listener.onChanged(player, previousStack, itemStack, hand);
                }
            }
    );

    void onChanged(PlayerEntity player, @Nullable ItemStack previousStack, ItemStack itemStack, Hand hand);
}
