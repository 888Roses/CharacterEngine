package net.collectively.geode.mc.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

public interface SwingableItem {
    /// Returns: whether to execute the base method or not.
    boolean onSwing(LivingEntity livingEntity, ItemStack itemStack);
}
