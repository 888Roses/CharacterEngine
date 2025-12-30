package net.collectively.geode.mc.util;

import net.collectively.geode.core.types.double3;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * Collection of {@link Entity} related utilities.
 */
@SuppressWarnings("unused")
public interface EntityHelper {
    /**
     * Gets and returns the position of the given {@link Entity} last tick.
     *
     * @param entity The {@link  Entity} we want to get the last pos of.
     * @return A new {@link Vec3d} containing the {@link Entity#lastX}, {@link Entity#lastY} and {@link Entity#lastZ} of the given {@link Entity}.
     */
    static double3 getLastPos(Entity entity) {
        return new double3(entity.lastX, entity.lastY, entity.lastZ);
    }

    /**
     * Gets and returns the position of the eyes of the given {@link Entity} last tic k.
     *
     * @param entity The {@link  Entity} we want to get the last eye pos of.
     * @return A new {@link Vec3d} containing the {@link Entity#lastX}, {@link Entity#lastY} and {@link Entity#lastZ} of the given {@link Entity}, added the entity's {@link Entity#getStandingEyeHeight()}.
     */
    static double3 getLastEyePos(Entity entity) {
        return getLastPos(entity).add(0, entity.getStandingEyeHeight(), 0);
    }

    /**
     * Gets and returns the position of the entity, with the Y component being the middle of the entity's hitbox.
     *
     * @param entity The {@link  Entity} we want to get the center pos of.
     */
    static double3 getCenterPos(Entity entity) {
        return new double3(entity.getEntityPos()).add(0, entity.getStandingEyeHeight() / 2d, 0);
    }

    static List<Entity> getEntitiesAround(Entity entity, float radius) {
        return  getEntitiesAround(entity, radius, ent -> true);
    }

    static List<Entity> getEntitiesAround(Entity entity, float radius, Predicate<Entity> check) {
        return  getEntitiesAround(entity, entity.getEntityWorld(), new double3(entity.getEntityPos()), radius, ent -> true);
    }

    static List<Entity> getEntitiesAround(@Nullable Entity entity, World world, double3 point, float radius) {
        return getEntitiesAround(entity, world, point, radius, ent -> true);
    }

    static List<Entity> getEntitiesAround(@Nullable Entity entity, World world, double3 point, float radius, Predicate<Entity> check) {
        final var buffer = new ArrayList<Entity>();
        final var box = Box.of(point.toVec3d(), radius, radius, radius);
        final var squaredRadius = radius * radius;
        for (var detected : world.getEntitiesByClass(Entity.class, box, ent -> ent != entity && check.test(ent))) {
            if (detected.squaredDistanceTo(point.toVec3d()) > squaredRadius) {
                continue;
            }

            buffer.add(detected);
        }

        return buffer;
    }
}
