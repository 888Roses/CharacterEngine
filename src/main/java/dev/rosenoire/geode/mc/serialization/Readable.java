package dev.rosenoire.geode.mc.serialization;

import net.minecraft.storage.ReadView;

public interface Readable {
    void read(ReadView view);
}
