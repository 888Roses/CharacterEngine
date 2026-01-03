package net.collectively.geode_animations.scripting;

import net.collectively.geode.core.types.double3;

import net.minecraft.util.math.random.Random;

public record Double3ValueGetter(double3 value) implements AbstractValueGetter<double3> {
    @Override
    public double3 get(Random random) {
        return value;
    }
}
