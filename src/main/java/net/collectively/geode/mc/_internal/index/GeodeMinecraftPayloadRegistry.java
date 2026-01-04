package net.collectively.geode.mc._internal.index;

import net.collectively.geode.mc._internal.payload.SwingableItemAttackC2S;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public interface GeodeMinecraftPayloadRegistry {
    static void registerAll() {
        PayloadTypeRegistry.playC2S().register(SwingableItemAttackC2S.ID, SwingableItemAttackC2S.CODEC);

        ServerPlayNetworking.registerGlobalReceiver(SwingableItemAttackC2S.ID, SwingableItemAttackC2S::receiveOnServer);
    }
}
