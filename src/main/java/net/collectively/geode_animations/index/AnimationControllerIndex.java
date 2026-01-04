package net.collectively.geode_animations.index;

import com.zigythebird.playeranim.api.PlayerAnimationFactory;
import net.collectively.geode_animations.GeodeAnimations;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public abstract class AnimationControllerIndex {
    public record InitializationData(int priority, @NotNull PlayerAnimationFactory factory) {}
    private static final Map<Identifier, InitializationData> TO_INITIALIZE = new HashMap<>();

    protected static Identifier register(Identifier identifier, int priority, @NotNull PlayerAnimationFactory factory) {
        TO_INITIALIZE.put(identifier, new InitializationData(priority, factory));
        return identifier;
    }

    protected static Identifier register(String identifier, int priority, @NotNull PlayerAnimationFactory factory) {
        return register(GeodeAnimations.id(identifier), priority, factory);
    }

    protected static void registerAll() {
        for(Map.Entry<Identifier, InitializationData> entry : TO_INITIALIZE.entrySet()) {
            PlayerAnimationFactory controller = entry.getValue().factory();
            PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(entry.getKey(), entry.getValue().priority(), controller);
        }
    }
}
