package dev.rosenoire.character_engine.client.particle;

import net.minecraft.client.particle.*;
import net.minecraft.client.render.Camera;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;

public class MuzzleFlashParticle extends BillboardParticle {
    MuzzleFlashParticle(ClientWorld clientWorld, double d, double e, double f, Sprite sprite) {
        super(clientWorld, d, e, f, sprite);
        this.maxAge = 1;
    }

    public BillboardParticle.RenderType getRenderType() {
        return RenderType.PARTICLE_ATLAS_OPAQUE;
    }

    public void render(BillboardParticleSubmittable submittable, Camera camera, float tickProgress) {
        super.render(submittable, camera, tickProgress);
    }

    public float getSize(float tickProgress) {
        return 0.25f;
    }

    public static class Factory implements ParticleFactory<SimpleParticleType> {
        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        public Particle createParticle(
                SimpleParticleType particleEffect,
                ClientWorld clientWorld,
                double x, double y, double z,
                double vx, double vy, double vz,
                Random random
        ) {
            return new MuzzleFlashParticle(clientWorld, x, y, z, this.spriteProvider.getSprite(random));
        }
    }
}
