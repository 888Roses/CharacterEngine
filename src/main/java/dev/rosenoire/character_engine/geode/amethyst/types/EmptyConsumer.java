package dev.rosenoire.character_engine.geode.amethyst.types;

/**
 * Event similar to a {@link java.util.function.Consumer} except it doesn't take any argument. See it as a simple event.
 */
@FunctionalInterface
public interface EmptyConsumer {
    /**
     * Call the consumer.
     */
    void consume();
}