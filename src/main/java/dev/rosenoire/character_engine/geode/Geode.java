package dev.rosenoire.character_engine.geode;

import net.minecraft.util.Identifier;

/**
 * Main API class.
 */
public class Geode {
    public static String HOOKED_MOD_ID;

    public static Identifier id(String identifier) {
        return Identifier.of(HOOKED_MOD_ID, identifier);
    }

    public static void setHookedMod(String hookedModId) {
        HOOKED_MOD_ID = hookedModId;
    }
}
