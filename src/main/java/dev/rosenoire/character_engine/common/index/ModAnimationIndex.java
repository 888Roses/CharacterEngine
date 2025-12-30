package dev.rosenoire.character_engine.common.index;

import dev.rosenoire.character_engine.foundation.index.AnimationIndex;
import static dev.rosenoire.character_engine.foundation.index.AnimationIndex.*;
import net.minecraft.util.Identifier;

public interface ModAnimationIndex extends AnimationIndex {
    Identifier BLASTER__RIGHT_ARM_IDLE = register("blaster.right_arm.idle");
    Identifier BLASTER__RIGHT_ARM_DRAW = register("blaster.right_arm.draw");
    Identifier BLASTER__LEFT_ARM_IDLE = register("blaster.left_arm.idle");
    Identifier BLASTER__LEFT_ARM_DRAW = register("blaster.left_arm.draw");

    static void initialize() {}
}