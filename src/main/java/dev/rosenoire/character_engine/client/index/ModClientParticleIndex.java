package dev.rosenoire.character_engine.client.index;

import dev.rosenoire.character_engine.client.particle.MuzzleFlashParticle;
import dev.rosenoire.character_engine.common.index.ModParticleIndex;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;

public interface ModClientParticleIndex {
    static void registerFactories() {
        ParticleFactoryRegistry registry = ParticleFactoryRegistry.getInstance();
        registry.register(ModParticleIndex.MUZZLE_FLASH_PINK, MuzzleFlashParticle.Factory::new);
        registry.register(ModParticleIndex.MUZZLE_FLASH_BLUE, MuzzleFlashParticle.Factory::new);
        registry.register(ModParticleIndex.MUZZLE_FLASH_GREEN, MuzzleFlashParticle.Factory::new);
        registry.register(ModParticleIndex.MUZZLE_FLASH_YELLOW, MuzzleFlashParticle.Factory::new);
    }
}
