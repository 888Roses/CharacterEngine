package dev.rosenoire.geode.cca.components.impl;

import dev.rosenoire.geode.cca.components.EnhancedComponent;
import net.minecraft.entity.LivingEntity;

/**
 * {@link EnhancedComponent} holding a {@link LivingEntity} target type.
 */
public abstract class LivingEntityComponent implements EnhancedComponent<LivingEntity> {
    protected final LivingEntity livingEntity;

    public LivingEntityComponent(LivingEntity livingEntity) {
        this.livingEntity = livingEntity;
    }

    @Override
    public LivingEntity getEntity() {
        return this.livingEntity;
    }
}
