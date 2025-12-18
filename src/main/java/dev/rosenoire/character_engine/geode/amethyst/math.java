package dev.rosenoire.character_engine.geode.amethyst;

import dev.rosenoire.character_engine.geode.amethyst.types.double2;
import dev.rosenoire.character_engine.geode.amethyst.types.double3;
import net.minecraft.util.math.Position;

@SuppressWarnings("unused")
public interface math {
    // region Lerp

    // region double

    static double lerp(double delta, double a, double b) {
        return a + delta * (b - a);
    }

    static float lerp(float delta, float a, float b) {
        return a + delta * (b - a);
    }

    // endregion

    // region double2

    static double2 lerp(double delta, double2 a, double2 b) {
        return new double2(lerp(delta, a.x(), b.x()), lerp(delta, a.y(), b.y()));
    }

    // endregion

    // region double3

    static double3 lerp(double delta, double3 a, double3 b) {
        return new double3(lerp(delta, a.x(), b.x()), lerp(delta, a.y(), b.y()), lerp(delta, a.z(), b.z()));
    }

    static double3 lerp(double delta, Position a, Position b) {
        return new double3(lerp(delta, a.getX(), b.getX()), lerp(delta, a.getY(), b.getY()), lerp(delta, a.getZ(), b.getZ()));
    }

    // endregion

    // endregion
}
