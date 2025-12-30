package dev.rosenoire.character_engine.data;

import dev.rosenoire.character_engine.common.index.ModItemIndex;
import net.collectively.geode.datagen.ModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;

public class ModModelProvider extends ModelProvider {
    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    protected void generateBlocks() {
    }

    @Override
    protected void generateItems() {
        itemDefinition(ModItemIndex.BLASTER);
    }
}
