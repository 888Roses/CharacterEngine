package net.collectively.geode;

import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main API class.
 */
public class Geode {
    public static String HOOKED_MOD_ID;
    public static final Logger LOGGER = LoggerFactory.getLogger("geode");

    public static Identifier id(String identifier) {
        return Identifier.of(HOOKED_MOD_ID, identifier);
    }

    public static void setHookedMod(String hookedModId) {
        HOOKED_MOD_ID = hookedModId;
    }
}
