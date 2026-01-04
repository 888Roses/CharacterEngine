package dev.rosenoire.character_engine.client.payloads;

import dev.rosenoire.character_engine.common.CharacterEngine;
import dev.rosenoire.character_engine.common.payloads.PlayAnimationQueueOnClientS2C;
import net.collectively.geode_animations.controllers.AnimationQueueData;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.PlayerLikeEntity;
import net.minecraft.entity.player.PlayerEntity;

public interface PlayAnimationQueueOnClientS2CReceiver {
    static void handle(PlayAnimationQueueOnClientS2C packet, ClientPlayNetworking.Context context) {
        ClientPlayerEntity clientPlayer = context.player();
        ClientWorld world = (ClientWorld) clientPlayer.getEntityWorld();

        if (clientPlayer.getUuid() == packet.animationAvatarUUID()) {
            return;
        }

        if (world.getEntity(packet.animationAvatarUUID()) instanceof PlayerLikeEntity animationAvatar) {
            if (animationAvatar instanceof PlayerEntity animationAvatarPlayer && animationAvatarPlayer.isMainPlayer()) {
                return;
            }

            CharacterEngine.LOGGER.info(
                    "PlayAnimationQueueOnClientS2CReceiver | {} > Avatar '{}' controller '{}' playing animation queue '{}' from server",
                    clientPlayer.getName().getString(),
                    animationAvatar.getName().getString(),
                    packet.animationControllerIdentifier(),
                    packet.animationQueueIdentifier()
            );

            AnimationQueueData.triggerAnimationServerSafe(
                    animationAvatar,
                    packet.animationControllerIdentifier(),
                    packet.animationQueueIdentifier()
            );
        }
    }
}
