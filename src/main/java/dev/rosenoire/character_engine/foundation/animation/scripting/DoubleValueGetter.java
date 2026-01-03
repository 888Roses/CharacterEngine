package dev.rosenoire.character_engine.foundation.animation.scripting;

import net.minecraft.util.math.random.Random;

public record DoubleValueGetter(Double value) implements AbstractValueGetter<Double> {
    @Override
    public Double get(Random random) {
        return value;
    }
}
