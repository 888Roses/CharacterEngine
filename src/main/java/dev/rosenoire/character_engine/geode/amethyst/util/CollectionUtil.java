package dev.rosenoire.character_engine.geode.amethyst.util;

@SuppressWarnings("unused")
public interface CollectionUtil {
    static <T> boolean isOutsideOfBounds(T[] array, int index) {
        return index < 0 || index >= array.length;
    }
}
