package dev.rosenoire.character_engine.common.index;

import net.collectively.geode.mc.index.SoundIndex;
import net.minecraft.sound.SoundEvent;

import static net.collectively.geode.mc.index.SoundIndex.*;

public interface ModSounds extends SoundIndex {
    SoundEvent BLASTER_COCK_1 = register("item.blaster.cock");

    static void initialize() {}
}
