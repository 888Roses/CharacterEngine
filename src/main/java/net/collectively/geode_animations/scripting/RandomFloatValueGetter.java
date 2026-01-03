package net.collectively.geode_animations.scripting;

import net.minecraft.util.math.random.Random;

public record RandomFloatValueGetter(AbstractValueGetter<Float> min, AbstractValueGetter<Float> max) implements AbstractValueGetter<Float> {
    @Override
    public Float get(Random random) {
        float min = this.min.get(random);
        float max = this.max.get(random);
        return min >= max ? min : random.nextFloat() * (max - min) + min;
    }
}
