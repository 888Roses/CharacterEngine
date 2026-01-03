package dev.rosenoire.character_engine.foundation.animation.scripting;

import net.minecraft.util.math.random.Random;

public record IntegerValueGetter(Integer value) implements AbstractValueGetter<Integer> {
    @Override
    public Integer get(Random random) {
        return value;
    }
}
