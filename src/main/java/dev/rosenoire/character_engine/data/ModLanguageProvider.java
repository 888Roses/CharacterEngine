package dev.rosenoire.character_engine.data;

import dev.rosenoire.character_engine.common.index.ModItemIndex;
import dev.rosenoire.character_engine.common.index.ModSounds;
import net.collectively.geode.datagen.LanguageProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class ModLanguageProvider extends LanguageProvider {
    public ModLanguageProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    protected void generate() {
        item(ModItemIndex.BLASTER, "Blaster");
        sound(ModSounds.BLASTER_COCK, "Blaster Drawn");
        sound(ModSounds.BLASTER_SHOOT, "Blaster Shot");
    }
}
