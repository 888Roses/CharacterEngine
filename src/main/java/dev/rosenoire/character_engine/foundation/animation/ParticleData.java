package dev.rosenoire.character_engine.foundation.animation;

import dev.rosenoire.character_engine.foundation.animation.scripting.AbstractValueGetter;
import net.collectively.geode.core.types.double3;

public record ParticleData(
        AbstractValueGetter<double3> position,
        AbstractValueGetter<double3> velocity,
        AbstractValueGetter<double3> offset,
        AbstractValueGetter<Integer> count
) {
}
