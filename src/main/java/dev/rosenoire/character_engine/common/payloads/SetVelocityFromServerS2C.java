package dev.rosenoire.character_engine.common.payloads;

import dev.rosenoire.character_engine.common.CharacterEngine;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

public record SetVelocityFromServerS2C(Vec3d velocity, int id) implements CustomPayload {
    private static final Identifier IDENTIFIER = CharacterEngine.id("set_velocity_from_server_s2c");
    public static final Id<SetVelocityFromServerS2C> ID = new Id<>(IDENTIFIER);

    public static final PacketCodec<PacketByteBuf,SetVelocityFromServerS2C> CODEC = PacketCodec.tuple(
            Vec3d.PACKET_CODEC, SetVelocityFromServerS2C::velocity,
            PacketCodecs.INTEGER, SetVelocityFromServerS2C::id,
            SetVelocityFromServerS2C::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
