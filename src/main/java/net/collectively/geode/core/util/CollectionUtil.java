package net.collectively.geode.core.util;

@SuppressWarnings("unused")
public interface CollectionUtil {
    static <T> boolean isOutsideOfBounds(T[] array, int index) {
        return index < 0 || index >= array.length;
    }
}
