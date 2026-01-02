package dev.rosenoire.character_engine.common.index;

import dev.rosenoire.character_engine.foundation.index.AnimationIndex;
import static dev.rosenoire.character_engine.foundation.index.AnimationIndex.*;
import net.minecraft.util.Identifier;

public interface ModAnimationIndex extends AnimationIndex {
    Identifier BLASTER_IDLE = register("blaster.idle");
    Identifier BLASTER_DRAW = register("blaster.draw");
    Identifier BLASTER_FIRE = register("blaster.fire");

    static void initialize() {}
}