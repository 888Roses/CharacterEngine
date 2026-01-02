package dev.rosenoire.character_engine.common.index;

import dev.rosenoire.character_engine.foundation.index.AnimationIndex;
import static dev.rosenoire.character_engine.foundation.index.AnimationIndex.*;
import net.minecraft.util.Identifier;

public interface ModAnimationIndex extends AnimationIndex {
    Identifier BLASTER_IDLE = register("blaster.right_arm.idle");
    Identifier BLASTER_DRAW = register("blaster.right_arm.draw");
    Identifier BLASTER_FIRE = register("blaster.right_arm.fire");
    Identifier BLASTER__LEFT_ARM_IDLE = register("blaster.left_arm.idle");
    Identifier BLASTER__LEFT_ARM_DRAW = register("blaster.left_arm.draw");
    Identifier BLASTER__LEFT_ARM_FIRE = register("blaster.left_arm.fire");

    static void initialize() {}
}