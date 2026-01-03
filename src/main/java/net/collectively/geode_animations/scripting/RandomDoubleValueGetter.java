package net.collectively.geode_animations.scripting;

import net.minecraft.util.math.random.Random;

public record RandomDoubleValueGetter(AbstractValueGetter<Double> min, AbstractValueGetter<Double> max) implements AbstractValueGetter<Double> {

    @Override
    public Double get(Random random) {
        double min = this.min.get(random);
        double max = this.max.get(random);
        return min >= max ? min : random.nextDouble() * (max - min) + min;
    }
}
