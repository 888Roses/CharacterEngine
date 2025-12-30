package dev.rosenoire.character_engine.common;

import net.collectively.geode.Geode;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;

public class CharacterEngine implements ModInitializer {
    public static final String MOD_ID = "character_engine";

    public static Identifier id(String identifier) {
        return Identifier.of(MOD_ID, identifier);
    }

    @Override
    public void onInitialize() {
        Geode.setHookedMod(MOD_ID);
    }
}
