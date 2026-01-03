package net.collectively.geode_animations.scripting;

import net.minecraft.util.math.random.Random;

public interface AbstractValueGetter<T> {
    T get(Random random);
}
