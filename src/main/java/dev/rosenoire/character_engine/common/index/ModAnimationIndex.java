package dev.rosenoire.character_engine.common.index;

import net.collectively.geode_animations.index.AnimationIndex;
import static net.collectively.geode_animations.index.AnimationIndex.*;
import net.minecraft.util.Identifier;

public interface ModAnimationIndex extends AnimationIndex {
    Identifier BLASTER_IDLE = register("blaster.idle");
    Identifier BLASTER_DRAW = register("blaster.draw");
    Identifier BLASTER_FIRE = register("blaster.fire");

    static void initialize() {}
}