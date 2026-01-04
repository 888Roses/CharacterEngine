package dev.rosenoire.character_engine.client.index;

import dev.rosenoire.character_engine.client.payloads.PlayAnimationQueueOnClientS2CReceiver;
import dev.rosenoire.character_engine.common.payloads.PlayAnimationQueueOnClientS2C;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public interface ModClientPayloadIndex {
    static void registerAll() {
        ClientPlayNetworking.registerGlobalReceiver(PlayAnimationQueueOnClientS2C.ID, PlayAnimationQueueOnClientS2CReceiver::handle);
    }
}
