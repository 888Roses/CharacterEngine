package net.collectively.geode.mc.internal.index;

import net.collectively.geode.mc.internal.networking.SwingableItemC2SPayload;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public interface GeodeNetworkingIndex {
    static void registerAll() {
        PayloadTypeRegistry.playC2S().register(SwingableItemC2SPayload.ID, SwingableItemC2SPayload.CODEC);
        ServerPlayNetworking.registerGlobalReceiver(SwingableItemC2SPayload.ID, SwingableItemC2SPayload::receiveOnServer);
    }
}
