package dev.rosenoire.character_engine.foundation.animation.scripting;

import net.minecraft.util.math.random.Random;

public record FloatValueGetter(Float value) implements AbstractValueGetter<Float> {
    @Override
    public Float get(Random random) {
        return value;
    }
}
