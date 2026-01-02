package dev.rosenoire.character_engine.foundation.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

public interface AttackItem {
    /// Returns: whether to execute the base method or not.
    boolean onSwing(LivingEntity livingEntity, ItemStack itemStack);
}
