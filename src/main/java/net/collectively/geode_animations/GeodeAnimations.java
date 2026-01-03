package net.collectively.geode_animations;

import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/// # Geode Animations
/// Geode animations is an addon for [Player Animation Library](https://github.com/ZigyTheBird/PlayerAnimationLibrary) serving as a high level implementation to make animating players even more of a piece of cake. Please note that it requires the **Geode** library in order to work.
/// ***
/// # Additions
/// ## Keyframe Handler Implementations
/// **Geode Animations** brings a couple of [Custom Keyframe Handlers](https://github.com/ZigyTheBird/PlayerAnimationLibrary/blob/aa844bbe5d8e1f11fdbba86e920affb06b25c60c/core/src/main/java/com/zigythebird/playeranimcore/animation/keyframe/event/CustomKeyFrameEvents.java#L94) to the table, so that
/// users don't have to implement their own.
public class GeodeAnimations {
    public static String HOOKED_MOD_ID;
    public static final Logger LOGGER = LoggerFactory.getLogger("geode");

    public static Identifier id(String identifier) {
        return Identifier.of(HOOKED_MOD_ID, identifier);
    }

    public static void setHookedMod(String hookedModId) {
        HOOKED_MOD_ID = hookedModId;
    }
}
