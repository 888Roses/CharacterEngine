package dev.rosenoire.character_engine.foundation.animation.scripting;

import net.minecraft.util.math.random.Random;

public record RandomIntegerValueGetter(AbstractValueGetter<Integer> min, AbstractValueGetter<Integer> max) implements AbstractValueGetter<Integer> {
    @Override
    public Integer get(Random random) {
        int min = this.min.get(random);
        int max = this.max.get(random);
        return random.nextInt(max - min + 1) + min;
    }
}
