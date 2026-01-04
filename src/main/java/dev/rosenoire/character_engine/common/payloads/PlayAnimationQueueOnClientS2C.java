package dev.rosenoire.character_engine.common.payloads;

import dev.rosenoire.character_engine.common.CharacterEngine;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

import java.util.UUID;

public record PlayAnimationQueueOnClientS2C(UUID animationAvatarUUID, Identifier animationControllerIdentifier, Identifier animationQueueIdentifier) implements CustomPayload {
    private static final Identifier IDENTIFIER = CharacterEngine.id("test_s2c");
    public static final Id<PlayAnimationQueueOnClientS2C> ID = new Id<>(IDENTIFIER);

    public static final PacketCodec<PacketByteBuf, PlayAnimationQueueOnClientS2C> CODEC = PacketCodec.tuple(
            PacketCodecs.STRING, payload -> payload.animationAvatarUUID().toString(),
            Identifier.PACKET_CODEC, PlayAnimationQueueOnClientS2C::animationControllerIdentifier,
            Identifier.PACKET_CODEC, PlayAnimationQueueOnClientS2C::animationQueueIdentifier,
            (uuidString, animationControllerIdentifier, animationQueueIdentifier) ->
                    new PlayAnimationQueueOnClientS2C(
                            UUID.fromString(uuidString),
                            animationControllerIdentifier,
                            animationQueueIdentifier
                    )
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
