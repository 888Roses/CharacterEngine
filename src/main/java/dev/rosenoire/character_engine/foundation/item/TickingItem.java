package dev.rosenoire.character_engine.foundation.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;

public interface TickingItem {
    void tickInHand(LivingEntity livingEntity, ItemStack itemStack, Hand hand);
}
