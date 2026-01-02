package dev.rosenoire.character_engine.foundation.animation;

import com.zigythebird.playeranim.animation.PlayerAnimationController;
import com.zigythebird.playeranimcore.animation.AnimationController;
import com.zigythebird.playeranimcore.animation.AnimationData;
import com.zigythebird.playeranimcore.animation.keyframe.event.CustomKeyFrameEvents;
import com.zigythebird.playeranimcore.animation.keyframe.event.data.ParticleKeyframeData;
import com.zigythebird.playeranimcore.event.EventResult;
import com.zigythebird.playeranimcore.math.Vec3f;
import dev.rosenoire.character_engine.common.CharacterEngine;
import net.collectively.geode.core.math;
import net.collectively.geode.core.types.double3;
import net.collectively.geode.mc.util.EntityHelper;
import net.collectively.geode.mc.util.WorldUtil;
import net.minecraft.entity.PlayerLikeEntity;
import net.minecraft.particle.ParticleType;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Optional;

public class StandardParticleKeyframeHandler implements CustomKeyFrameEvents.CustomKeyFrameHandler<ParticleKeyframeData> {
    // TODO: remake bruh.
    @Override
    public EventResult handle(
            float animationTick,
            AnimationController animationController,
            ParticleKeyframeData particleKeyframeData,
            AnimationData animationData
    ) {
        if (animationController instanceof PlayerAnimationController playerController) {
            String particleName = particleKeyframeData.getEffect();
            Identifier particleIdentifier = Identifier.tryParse(particleName);

            if (particleIdentifier == null) {
                CharacterEngine.LOGGER.error("The particle type identifier for '{}' is null!", particleName);
                return EventResult.PASS;
            }

            Optional<RegistryEntry.Reference<ParticleType<?>>> key = Registries.PARTICLE_TYPE.getEntry(particleIdentifier);

            if (key.isEmpty()) {
                CharacterEngine.LOGGER.error("The particle type key is empty!");
                return EventResult.PASS;
            }

            ParticleType<?> particleType = key.get().value();
            if (particleType instanceof SimpleParticleType simpleParticleType) {
                PlayerLikeEntity playerLike = playerController.getAvatar();
                World world = playerLike.getEntityWorld();

                double3 position = new double3(-0.2, 0.01, 0.75);
                position = math.rotateVector(position, new double3(-playerLike.getPitch(), playerLike.getYaw(), 0).modify(math::deg2rad));
                Vec3f parentPos = animationController.getBone(particleKeyframeData.getLocator()).getPositionVector();
                double3 pos = new double3(parentPos.x(), parentPos.y(), parentPos.x()).add(position).add(playerLike.getEyePos());

                WorldUtil.addParticleClient(world, simpleParticleType, pos, double3.zero);
                CharacterEngine.LOGGER.info("Animation spawned particle at {} ([{}, {}, {}] + {} = {}).", position.toPrettyString(), parentPos.x(), parentPos.y(), parentPos.z(), position.toPrettyString(), pos.toPrettyString());

                return EventResult.SUCCESS;
            }

            CharacterEngine.LOGGER.error("This particle type is not a simple particle type! Only SimpleParticleTypes are supported: {}", particleType);
            return EventResult.PASS;
        }

        return EventResult.PASS;
    }
}
