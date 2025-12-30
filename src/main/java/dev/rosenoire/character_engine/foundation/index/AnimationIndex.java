package dev.rosenoire.character_engine.foundation.index;

import dev.rosenoire.character_engine.common.CharacterEngine;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public interface AnimationIndex {
    List<Identifier> ANIMATIONS = new ArrayList<>();

    static boolean isRegistered(String name) {
        return ANIMATIONS.stream().anyMatch(x -> x.getPath().equals(name));
    }

    static Identifier register(String animationId) {
        return register(CharacterEngine.id(animationId));
    }

    static Identifier register(Identifier animationId) {
        ANIMATIONS.add(animationId);
        return animationId;
    }
}
