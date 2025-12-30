package dev.rosenoire.character_engine.cca.components;

import dev.rosenoire.character_engine.cca.ModEntityComponentIndex;
import dev.rosenoire.character_engine.foundation.item.TickingItem;
import net.collectively.geode.cca.components.impl.LivingEntityComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.Hand;
import org.ladysnake.cca.api.v3.component.Component;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.tick.CommonTickingComponent;

public class TickingItemComponent extends LivingEntityComponent implements CommonTickingComponent {
    // region Essentials
    public TickingItemComponent(LivingEntity livingEntity) {
        super(livingEntity);
    }

    @Override
    public ComponentKey<? extends Component> getKey() {
        return ModEntityComponentIndex.TICKING_ITEM;
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

    @Override
    public void tick() {
        for (Hand hand : Hand.values()) {
            ItemStack itemStack = livingEntity.getStackInHand(hand);

            if (itemStack.getItem() instanceof TickingItem tickingItem) {
                tickingItem.tickInHand(livingEntity, itemStack, hand);
            }
        }
    }
}
