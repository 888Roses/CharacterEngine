package dev.rosenoire.character_engine.client.payloads;

import dev.rosenoire.character_engine.common.payloads.SetVelocityFromServerS2C;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public interface SetVelocityFromServerS2CReceiver {
    static void handle(SetVelocityFromServerS2C packet, ClientPlayNetworking.Context context) {
        ClientPlayerEntity player = context.player();
        World world = player.getEntityWorld();
        Entity targetEntity = world.getEntityById(packet.id());
        if (targetEntity != null) targetEntity.setVelocity(packet.velocity());
    }
}
