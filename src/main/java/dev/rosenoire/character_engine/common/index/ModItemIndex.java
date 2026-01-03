package dev.rosenoire.character_engine.common.index;

import dev.rosenoire.character_engine.common.item.PistolItem;
import net.collectively.geode.mc.index.ItemIndex;
import net.minecraft.item.Item;
import net.minecraft.util.Rarity;

import static net.collectively.geode.mc.index.ItemIndex.*;

public interface ModItemIndex extends ItemIndex {
    Item BLASTER = register("blaster", PistolItem::new, new Item.Settings().maxCount(1).rarity(Rarity.UNCOMMON));

    static void initialize() {
    }
}
