package net.collectively.geode.mc.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

/// `SwingableItem` is an interface allowing its implementations to be aware of the player swinging the item. It has only one
/// method, `onSwing` that is called both on the **client side** and on the **server side** when any `LivingEntity` swings
/// with that item. In the case of a player swinging the item, this method is called regardless of what the player is looking
/// at (a block, an entity, a fluid, nothing at all).
///
/// @apiNote This interface will only do something if an `Item` implements it. Once implemented, it will work out of the box
/// without needing anything else.
public interface SwingableItem {
    /// Called when any `LivingEntity` swings the item, regardless of what the player is looking at (block, entity, fluid,
    /// nothing at all).
    ///
    /// @param livingEntity Reference to the {@link LivingEntity} swinging that item.
    /// @param itemStack    Reference to the {@link ItemStack} being swung.
    /// @return Whether to execute the base swinging method or not. This base method executes the base Minecraft entity
    /// swinging (Animation, etc.). <br />
    /// @apiNote The returned {@code boolean} is only relevant on the **client side**. It will be entirely ignored on the
    /// server side.
    boolean onSwing(LivingEntity livingEntity, ItemStack itemStack);
}
