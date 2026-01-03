package dev.rosenoire.character_engine.client;

import dev.rosenoire.character_engine.common.index.ModAnimationControllerIndex;
import dev.rosenoire.character_engine.client.index.ModClientParticleIndex;
import dev.rosenoire.character_engine.client.index.ModClientPayloadIndex;
import net.fabricmc.api.ClientModInitializer;

public class CharacterEngineClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ModClientPayloadIndex.registerAll();
        ModClientParticleIndex.registerFactories();
    }
}
