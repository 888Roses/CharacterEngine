package dev.rosenoire.character_engine.common.index;

import net.collectively.geode.mc.index.SoundIndex;
import net.minecraft.sound.SoundEvent;

import static net.collectively.geode.mc.index.SoundIndex.*;

public interface ModSounds extends SoundIndex {
    SoundEvent BLASTER_COCK = register("item.blaster.cock");
    SoundEvent BLASTER_SHOOT = register("item.blaster.shoot");

    static void initialize() {}
}
