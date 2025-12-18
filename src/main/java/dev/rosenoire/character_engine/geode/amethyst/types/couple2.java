package dev.rosenoire.character_engine.geode.amethyst.types;

public record couple2<T>(T a, T b) {
    public T get(boolean binary) {
        return binary ? a : b;
    }
}
