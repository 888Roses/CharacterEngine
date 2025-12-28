package dev.rosenoire.geode.core;

import dev.rosenoire.geode.core.types.double2;
import dev.rosenoire.geode.core.types.double3;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Position;
import org.joml.Quaternionf;

@SuppressWarnings({"unused", "SpellCheckingInspection"})
public interface math {
    static double min(double value1, double value2) {
        return Math.min(value1, value2);
    }

    static double max(double value1, double value2) {
        return Math.max(value1, value2);
    }

    static double clamp(double value, double min, double max) {
        return min(max, max(value, min));
    }

    static double clamp01(double value) {
        return clamp(value, 0, 1);
    }

    static long ceil(double value) {
        return (long) Math.ceil(value);
    }

    static float min(float value1, float value2) {
        return Math.min(value1, value2);
    }

    static float max(float value1, float value2) {
        return Math.max(value1, value2);
    }

    static float clamp(float value, float min, float max) {
        return min(max, max(value, min));
    }

    static float clamp01(float value) {
        return clamp(value, 0, 1);
    }

    static int ceil(float value) {
        return (int) Math.ceil(value);
    }

    static int min(int value1, int value2) {
        return Math.min(value1, value2);
    }

    static int max(int value1, int value2) {
        return Math.max(value1, value2);
    }

    static int clamp(int value, int min, int max) {
        return min(max, max(value, min));
    }

    static int clamp01(int value) {
        return clamp(value, 0, 1);
    }

    static long min(long value1, long value2) {
        return Math.min(value1, value2);
    }

    static long max(long value1, long value2) {
        return Math.max(value1, value2);
    }

    static long clamp(long value, long min, long max) {
        return min(max, max(value, min));
    }

    static long clamp01(long value) {
        return clamp(value, 0, 1);
    }

    static long round(double value) {
        return Math.round(value);
    }

    static int round(float value) {
        return Math.round(value);
    }

    static double abs(double value) {return Math.abs(value);}
    static float abs(float value) {return Math.abs(value);}
    static int abs(int value) {return Math.abs(value);}
    static long abs(long value) {return Math.abs(value);}

    static Quaternionf euler(double3 rot) {
        return new Quaternionf().rotateXYZ((float) rot.x(), (float) rot.y(), (float) rot.z());
    }

    static Quaternionf euler() {
        return euler(new double3(0));
    }

    // https://stackoverflow.com/questions/11513344/how-to-implement-the-fast-inverse-square-root-in-java
    static float invSqrt(float x) {
        float xhalf = 0.5f * x;
        int i = Float.floatToIntBits(x);
        i = 0x5f3759df - (i >> 1);
        x = Float.intBitsToFloat(i);
        x *= (1.5f - xhalf * x * x);
        return x;
    }

    // https://stackoverflow.com/questions/11513344/how-to-implement-the-fast-inverse-square-root-in-java
    static double invSqrt(double x) {
        double xhalf = 0.5d * x;
        long i = Double.doubleToLongBits(x);
        i = 0x5fe6ec85e7de30daL - (i >> 1);
        x = Double.longBitsToDouble(i);
        x *= (1.5d - xhalf * x * x);
        return x;
    }

    static double sqrt(double a) {
        return Math.sqrt(a);
    }

    static double rad2deg(double rad) {
        return Math.toDegrees(rad);
    }

    static double deg2rad(double deg) {
        return Math.toRadians(deg);
    }

    static double atan2(double y, double x) {
        return MathHelper.atan2(y, x);
    }

    static double2 vec2deg(double3 v) {
        var rad = vec2rad(v);
        return new double2(rad2deg(rad.x()), rad2deg(rad.y()));
    }

    static double2 vec2rad(double3 v) {
        return new double2(atan2(v.y(), sqrt(v.squaredHorMag())), atan2(v.x(), v.z()));
    }

    static double lerp(double delta, double a, double b) {
        return a + delta * (b - a);
    }

    static float lerp(float delta, float a, float b) {
        return a + delta * (b - a);
    }

    static double2 lerp(double delta, double2 a, double2 b) {
        return new double2(lerp(delta, a.x(), b.x()), lerp(delta, a.y(), b.y()));
    }

    static double3 lerp(double delta, double3 a, double3 b) {
        return new double3(lerp(delta, a.x(), b.x()), lerp(delta, a.y(), b.y()), lerp(delta, a.z(), b.z()));
    }

    static double3 lerp(double delta, Position a, Position b) {
        return new double3(lerp(delta, a.getX(), b.getX()), lerp(delta, a.getY(), b.getY()), lerp(delta, a.getZ(), b.getZ()));
    }

    /**
     * Returns the trigonometric sine of an angle.  Special cases:
     * <ul><li>If the argument is NaN or an infinity, then the
     * result is NaN.
     * <li>If the argument is zero, then the result is a zero with the
     * same sign as the argument.</ul>
     *
     * <p>The computed result must be within 1 ulp of the exact result.
     * Results must be semi-monotonic.
     *
     * @param a an angle, in radians.
     * @return the sine of the argument.
     */
    static double sin(double a) {
        return StrictMath.sin(a); // default impl. delegates to StrictMath
    }

    /**
     * Returns the trigonometric cosine of an angle. Special cases:
     * <ul><li>If the argument is NaN or an infinity, then the
     * result is NaN.
     * <li>If the argument is zero, then the result is {@code 1.0}.
     * </ul>
     *
     * <p>The computed result must be within 1 ulp of the exact result.
     * Results must be semi-monotonic.
     *
     * @param a an angle, in radians.
     * @return the cosine of the argument.
     */
    static double cos(double a) {
        return StrictMath.cos(a); // default impl. delegates to StrictMath
    }

    /**
     * Returns the trigonometric tangent of an angle.  Special cases:
     * <ul><li>If the argument is NaN or an infinity, then the result
     * is NaN.
     * <li>If the argument is zero, then the result is a zero with the
     * same sign as the argument.</ul>
     *
     * <p>The computed result must be within 1 ulp of the exact result.
     * Results must be semi-monotonic.
     *
     * @param a an angle, in radians.
     * @return the tangent of the argument.
     */
    static double tan(double a) {
        return StrictMath.tan(a); // default impl. delegates to StrictMath
    }

    /**
     * Returns the arc sine of a value; the returned angle is in the
     * range -<i>pi</i>/2 through <i>pi</i>/2.  Special cases:
     * <ul><li>If the argument is NaN or its absolute value is greater
     * than 1, then the result is NaN.
     * <li>If the argument is zero, then the result is a zero with the
     * same sign as the argument.</ul>
     *
     * <p>The computed result must be within 1 ulp of the exact result.
     * Results must be semi-monotonic.
     *
     * @param a the value whose arc sine is to be returned.
     * @return the arc sine of the argument.
     */
    static double asin(double a) {
        return StrictMath.asin(a); // default impl. delegates to StrictMath
    }

    /**
     * Returns the arc cosine of a value; the returned angle is in the
     * range 0.0 through <i>pi</i>.  Special case:
     * <ul><li>If the argument is NaN or its absolute value is greater
     * than 1, then the result is NaN.
     * <li>If the argument is {@code 1.0}, the result is positive zero.
     * </ul>
     *
     * <p>The computed result must be within 1 ulp of the exact result.
     * Results must be semi-monotonic.
     *
     * @param a the value whose arc cosine is to be returned.
     * @return the arc cosine of the argument.
     */
    static double acos(double a) {
        return StrictMath.acos(a); // default impl. delegates to StrictMath
    }

    /**
     * Returns the arc tangent of a value; the returned angle is in the
     * range -<i>pi</i>/2 through <i>pi</i>/2.  Special cases:
     * <ul><li>If the argument is NaN, then the result is NaN.
     * <li>If the argument is zero, then the result is a zero with the
     * same sign as the argument.
     * <li>If the argument is {@linkplain Double#isInfinite infinite},
     * then the result is the closest value to <i>pi</i>/2 with the
     * same sign as the input.
     * </ul>
     *
     * <p>The computed result must be within 1 ulp of the exact result.
     * Results must be semi-monotonic.
     *
     * @param a the value whose arc tangent is to be returned.
     * @return the arc tangent of the argument.
     */
    static double atan(double a) {
        return StrictMath.atan(a); // default impl. delegates to StrictMath
    }
}
