package dev.rosenoire.character_engine.foundation.payload;

import dev.rosenoire.character_engine.common.CharacterEngine;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record AnimationParticleKeyframeS2C(

) implements CustomPayload {
    private static final Identifier IDENTIFIER = CharacterEngine.id("animation_particle_keyframe_s2c");
    public static final Id<AnimationParticleKeyframeS2C> ID = new Id<>(IDENTIFIER);

    // public static final PacketCodec<AnimationParticleKeyframeS2C, PacketByteBuf> CODEC = PacketCodec.tuple(
//
    // );

    @Override
    public Id<? extends CustomPayload> getId() {
        return null;
    }
}
