package dev.rosenoire.character_engine.cca;

import dev.rosenoire.character_engine.cca.components.*;
import dev.rosenoire.character_engine.common.CharacterEngine;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentInitializer;

public class ModEntityComponentIndex implements EntityComponentInitializer {
    public static final ComponentKey<PlayerItemManagerComponent> ITEM_MANAGER = ComponentRegistry.getOrCreate(
            CharacterEngine.id("item_manager"), PlayerItemManagerComponent.class
    );

    public static final ComponentKey<PlayerActionsComponent> ACTIONS = ComponentRegistry.getOrCreate(
            CharacterEngine.id("player_actions"), PlayerActionsComponent.class
    );

    public static final ComponentKey<TickingItemComponent> TICKING_ITEM = ComponentRegistry.getOrCreate(
            CharacterEngine.id("ticking_items"), TickingItemComponent.class
    );

    public static final ComponentKey<PlayerFovComponent> PLAYER_FOV = ComponentRegistry.getOrCreate(
            CharacterEngine.id("player_fov"), PlayerFovComponent.class
    );

    public static final ComponentKey<CameraShakeComponent> CAMERA_SHAKE = ComponentRegistry.getOrCreate(
            CharacterEngine.id("camera_shake"), CameraShakeComponent.class
    );

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry entityComponentFactoryRegistry) {
        entityComponentFactoryRegistry.registerFor(PlayerEntity.class, ITEM_MANAGER, PlayerItemManagerComponent::new);
        entityComponentFactoryRegistry.registerFor(PlayerEntity.class, ACTIONS, PlayerActionsComponent::new);
        entityComponentFactoryRegistry.registerFor(PlayerEntity.class, PLAYER_FOV, PlayerFovComponent::new);
        entityComponentFactoryRegistry.registerFor(LivingEntity.class, TICKING_ITEM, TickingItemComponent::new);
        entityComponentFactoryRegistry.registerFor(PlayerEntity.class, CAMERA_SHAKE, CameraShakeComponent::new);
    }
}
