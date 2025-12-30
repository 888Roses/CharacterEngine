package dev.rosenoire.character_engine.data;

import dev.rosenoire.character_engine.common.index.ModSounds;
import net.collectively.geode.datagen.SoundProvider;
import net.minecraft.data.DataOutput;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class ModSoundProvider extends SoundProvider {
    public ModSoundProvider(DataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void generate() {
        create(ModSounds.BLASTER_COCK_1)
                .sound("item/blaster/cock/cock_1")
                .subtitle()
                .build();
    }
}
