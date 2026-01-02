package dev.rosenoire.character_engine.client;

import dev.rosenoire.character_engine.client.animation.ModAnimationControllerIndex;
import dev.rosenoire.character_engine.client.index.ModClientParticleIndex;
import dev.rosenoire.character_engine.client.index.ModClientPayloadIndex;
import net.fabricmc.api.ClientModInitializer;

public class CharacterEngineClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ModAnimationControllerIndex.initialize();
        ModClientPayloadIndex.registerAll();
        ModClientParticleIndex.registerFactories();
    }
}
