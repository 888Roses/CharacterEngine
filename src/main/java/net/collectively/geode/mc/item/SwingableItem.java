package net.collectively.geode.mc.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;

// TODO: Rename to reflect the attack and not swinging of the item.
/// `SwingableItem` is an interface allowing its implementations to be aware of the player swinging the item. It has only one
/// method, `onSwing` that is called both on the **client side** and on the **server side** when any `PlayerEntity` swings
/// with that item. In the case of a player swinging the item, this method is called regardless of what the player is looking
/// at (a block, an entity, a fluid, nothing at all).
///
/// @apiNote This interface will only do something if an `Item` implements it. Once implemented, it will work out of the box
/// without needing anything else.
public interface SwingableItem {
    // TODO: Redo documentation.

    /// Called when any `PlayerEntity` swings the item, regardless of what the player is looking at (block, entity, fluid,
    /// nothing at all).
    ///
    /// @param playerEntity Reference to the {@link PlayerEntity} swinging that item.
    /// @param itemStack    Reference to the {@link ItemStack} being swung.
    void onSwing(PlayerEntity playerEntity, ItemStack itemStack);
}
