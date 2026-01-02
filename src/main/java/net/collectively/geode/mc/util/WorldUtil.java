package net.collectively.geode.mc.util;

import net.collectively.geode.core.types.double3;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.*;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public interface WorldUtil {
    static void addParticleClient(
            World world,
            ParticleEffect effect,
            double3 position,
            double3 velocity,
            int count
    ) {
        for (int i = 0; i < count; i++) {
            world.addParticleClient(
                    effect,
                    position.x(), position.y(), position.z(),
                    velocity.x(), velocity.y(), velocity.z()
            );
        }
    }

    static void addParticleClient(
            World world,
            ParticleEffect effect,
            double3 position,
            double3 velocity
    ) {
        addParticleClient(world, effect, position, velocity, 1);
    }

    static void createExplosion(Entity entity, float power, World.ExplosionSourceType type) {
        createExplosion(entity.getEntityWorld(), entity, new double3(entity.getEntityPos()), power, type);
    }

    static void createExplosion(Entity entity, double3 pos, float power, World.ExplosionSourceType type) {
        createExplosion(entity.getEntityWorld(), entity, pos, power, type);
    }

    static void createExplosion(World world, @Nullable Entity entity, double3 pos, float power, World.ExplosionSourceType type) {
        world.createExplosion(entity, pos.x(), pos.y(), pos.z(), power, type);
    }

    static HitResult raycast(Entity sourceEntity, double distance) {
        var sqrDst = distance * distance;
        var eyePos = sourceEntity.getEyePos();

        var endPos = sourceEntity.getEyePos().add(sourceEntity.getRotationVector().multiply(distance));
        var blockHit = sourceEntity.getEntityWorld().raycast(new RaycastContext(
                sourceEntity.getEyePos(),
                endPos,
                RaycastContext.ShapeType.COLLIDER,
                RaycastContext.FluidHandling.NONE,
                sourceEntity
        ));

        var blockHitDst = blockHit.getPos().squaredDistanceTo(eyePos);
        if (blockHit.getType() != HitResult.Type.MISS) {
            sqrDst = blockHitDst;
            distance = Math.sqrt(blockHitDst);
        }

        var fwd = sourceEntity.getRotationVector();
        endPos = eyePos.add(fwd.multiply(distance));
        var hitBox = sourceEntity.getBoundingBox().stretch(fwd.multiply(distance)).expand(1.0F, 1.0F, 1.0F);
        var entityHit = ProjectileUtil.raycast(sourceEntity, eyePos, endPos, hitBox, EntityPredicates.CAN_HIT, sqrDst);

        if (entityHit == null || entityHit.getType() == HitResult.Type.MISS || entityHit.getPos().squaredDistanceTo(eyePos) > blockHitDst) {
            return blockHit;
        }

        return entityHit;
    }
}
