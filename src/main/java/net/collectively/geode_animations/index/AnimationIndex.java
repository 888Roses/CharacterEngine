package net.collectively.geode_animations.index;

import net.collectively.geode_animations.GeodeAnimations;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public interface AnimationIndex {
    List<Identifier> ANIMATIONS = new ArrayList<>();

    static boolean isRegistered(String name) {
        return ANIMATIONS.stream().anyMatch(animation -> animation.getPath().equals(name));
    }

    static Identifier register(String animationId) {
        return register(GeodeAnimations.id(animationId));
    }

    static Identifier register(Identifier animationId) {
        ANIMATIONS.add(animationId);
        return animationId;
    }
}
