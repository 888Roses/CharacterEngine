package dev.rosenoire.character_engine.foundation.animation.scripting;

import net.collectively.geode.core.types.double3;

import net.minecraft.util.math.random.Random;

public record RandomDouble3ValueGetter(AbstractValueGetter<double3> min, AbstractValueGetter<double3> max) implements AbstractValueGetter<double3> {
    @Override
    public double3 get(Random random) {
        double3 min = this.min.get(random);
        double3 max = this.max.get(random);

        return new double3(
                min.x() >= max.x() ? min.x() : random.nextDouble() * (max.x() - min.x()) + min.x(),
                min.y() >= max.y() ? min.y() : random.nextDouble() * (max.y() - min.y()) + min.y(),
                min.z() >= max.z() ? min.z() : random.nextDouble() * (max.z() - min.z()) + min.z()
        );
    }
}
