package dev.rosenoire.character_engine.foundation.animation.scripting;

import net.minecraft.util.math.random.Random;

public interface AbstractValueGetter<T> {
    T get(Random random);
}
