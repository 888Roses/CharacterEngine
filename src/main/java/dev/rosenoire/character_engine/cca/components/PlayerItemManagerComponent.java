package dev.rosenoire.character_engine.cca.components;

import dev.rosenoire.character_engine.cca.ModEntityComponentIndex;
import dev.rosenoire.character_engine.foundation.item.PlayerStackChangeCallback;
import net.collectively.geode.cca.components.impl.PlayerEntityComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.Hand;
import org.ladysnake.cca.api.v3.component.Component;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.tick.CommonTickingComponent;

import java.util.HashMap;
import java.util.Map;

public class PlayerItemManagerComponent extends PlayerEntityComponent implements CommonTickingComponent {
    // region Essentials
    public PlayerItemManagerComponent(PlayerEntity player) {
        super(player);
    }

    @Override
    public ComponentKey<? extends Component> getKey() {
        return ModEntityComponentIndex.ITEM_MANAGER;
    }
    // endregion

    // region Save & Load
    @Override
    public void readData(ReadView readView) {
    }

    @Override
    public void writeData(WriteView writeView) {
    }
    // endregion

    public final Map<Hand, ItemStack> previousItemStacks = new HashMap<>();

    @Override
    public void tick() {
        for (Hand hand : Hand.values()) {
            ItemStack currentStack = player.getStackInHand(hand);

            if (previousItemStacks.containsKey(hand)) {
                ItemStack previousStack = previousItemStacks.get(hand);

                if (previousStack != currentStack) {
                    PlayerStackChangeCallback.EVENT.invoker().onChanged(player, previousStack, currentStack, hand);
                }
            }

            previousItemStacks.put(hand, currentStack);
        }
    }
}
