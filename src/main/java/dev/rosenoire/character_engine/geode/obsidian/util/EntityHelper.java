package dev.rosenoire.character_engine.geode.obsidian.util;

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
    static Vec3d getLastPos(Entity entity) {
        return new Vec3d(entity.lastX, entity.lastY, entity.lastZ);
    }

    /**
     * Gets and returns the position of the eyes of the given {@link Entity} last tic k.
     *
     * @param entity The {@link  Entity} we want to get the last eye pos of.
     * @return A new {@link Vec3d} containing the {@link Entity#lastX}, {@link Entity#lastY} and {@link Entity#lastZ} of the given {@link Entity}, added the entity's {@link Entity#getStandingEyeHeight()}.
     */
    static Vec3d getLastEyePos(Entity entity) {
        return getLastPos(entity).add(0, entity.getStandingEyeHeight(), 0);
    }

    /**
     * Gets and returns the position of the entity, with the Y component being the middle of the entity's hitbox.
     *
     * @param entity The {@link  Entity} we want to get the center pos of.
     */
    static Vec3d getCenterPos(Entity entity) {
        return entity.getEntityPos().add(0, entity.getStandingEyeHeight() / 2d, 0);
    }

    static List<Entity> getEntitiesAround(@Nullable Entity entity, World world, Vec3d point, float radius) {
        return getEntitiesAround(entity, world, point, radius, ent -> true);
    }

    static List<Entity> getEntitiesAround(@Nullable Entity entity, World world, Vec3d point, float radius, Predicate<Entity> check) {
        final var buffer = new ArrayList<Entity>();
        final var box = Box.of(point, radius, radius, radius);
        final var squaredRadius = radius * radius;
        for (var detected : world.getEntitiesByClass(Entity.class, box, ent -> ent != entity && check.test(ent))) {
            if (detected.squaredDistanceTo(point) > squaredRadius) {
                continue;
            }

            buffer.add(detected);
        }

        return buffer;
    }
}
