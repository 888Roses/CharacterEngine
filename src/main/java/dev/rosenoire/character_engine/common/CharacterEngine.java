package dev.rosenoire.character_engine.common;

import dev.rosenoire.character_engine.common.index.*;
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
        ModSounds.initialize();
        ModAnimationIndex.initialize();
        ModItemIndex.initialize();
        ModCommonPayloadIndex.registerAll();
        ModCommonCallbackIndex.subscribeCallbacks();
    }
}
