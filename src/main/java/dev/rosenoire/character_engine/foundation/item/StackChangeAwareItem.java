package dev.rosenoire.character_engine.foundation.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;

/**
 * Item detecting when it is selected and unselected.
 */
public interface StackChangeAwareItem {
    default void onSelected(PlayerEntity player, Hand hand, ItemStack previousItemStack, ItemStack currentItemStack) {}

    default void onDeselected(PlayerEntity player, Hand hand, ItemStack itemStack, ItemStack newItemStack) {}
}
