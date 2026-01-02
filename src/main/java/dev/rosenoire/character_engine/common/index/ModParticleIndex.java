package dev.rosenoire.character_engine.common.index;

import dev.rosenoire.character_engine.common.CharacterEngine;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public interface ModParticleIndex {
    SimpleParticleType MUZZLE_FLASH = register("muzzle_flash");

    static void initialize() {
    }

    private static SimpleParticleType register(String identifier) {
        SimpleParticleType particle = FabricParticleTypes.simple();
        Registry.register(Registries.PARTICLE_TYPE, CharacterEngine.id(identifier), particle);
        return particle;
    }
}
