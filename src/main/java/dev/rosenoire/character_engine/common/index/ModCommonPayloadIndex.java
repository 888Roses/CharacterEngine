package dev.rosenoire.character_engine.common.index;

import dev.rosenoire.character_engine.common.payloads.PlayAnimationQueueOnClientS2C;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;

public interface ModCommonPayloadIndex {
    static void registerAll() {
        PayloadTypeRegistry.playS2C().register(PlayAnimationQueueOnClientS2C.ID, PlayAnimationQueueOnClientS2C.CODEC);
    }
}
