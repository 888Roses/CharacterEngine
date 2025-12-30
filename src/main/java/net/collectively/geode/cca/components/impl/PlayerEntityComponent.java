package net.collectively.geode.cca.components.impl;

import net.collectively.geode.cca.components.EnhancedComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

/**
 * {@link EnhancedComponent} holding a {@link PlayerEntity} target type.
 */
public abstract class PlayerEntityComponent implements EnhancedComponent<PlayerEntity> {
    protected final PlayerEntity player;
    protected final World world;

    public PlayerEntityComponent(PlayerEntity player) {
        this.player = player;
        this.world = this.player.getEntityWorld();
    }

    @Override
    public PlayerEntity getEntity() {
        return this.player;
    }

    public World getEntityWorld() {
        return this.world;
    }
}
