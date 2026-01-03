package dev.rosenoire.character_engine.common.index;

import com.zigythebird.playeranimcore.animation.Animation;
import dev.rosenoire.character_engine.common.CharacterEngine;
import net.collectively.geode_animations.index.AnimationIndex;

import static net.collectively.geode_animations.index.AnimationIndex.*;

import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

public interface ModAnimationIndex extends AnimationIndex {
    Identifier BLASTER_IDLE = register("blaster.idle");
    Identifier BLASTER_DRAW = register("blaster.draw");
    Identifier BLASTER_FIRE = register("blaster.fire");

    Identifier[] BLASTER_ANIMATIONS = new Identifier[]{
            BLASTER_IDLE,
            BLASTER_DRAW,
            BLASTER_FIRE
    };

    static boolean isBlasterAnimation(@Nullable Animation animation) {
        return animation != null && Arrays.stream(BLASTER_ANIMATIONS)
                .anyMatch(identifier -> identifier.getPath().equals(animation.getNameOrId()));
    }

    static void initialize() {
    }
}