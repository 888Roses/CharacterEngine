package net.collectively.geode_animations.index;

import com.zigythebird.playeranimcore.animation.Animation;
import net.collectively.geode_animations.GeodeAnimations;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public interface AnimationIndex {
    List<Identifier> ANIMATIONS = new ArrayList<>();

    static boolean isRegistered(@Nullable Animation animation) {
        if (animation == null) {
            return false;
        }

        return isRegistered(animation.getNameOrId());
    }

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
