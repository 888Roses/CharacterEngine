package dev.rosenoire.character_engine.foundation.animation;

import com.google.gson.Strictness;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.zigythebird.playeranim.animation.PlayerAnimationController;
import com.zigythebird.playeranimcore.animation.Animation;
import com.zigythebird.playeranimcore.animation.AnimationController;
import com.zigythebird.playeranimcore.animation.AnimationData;
import com.zigythebird.playeranimcore.animation.keyframe.event.CustomKeyFrameEvents;
import com.zigythebird.playeranimcore.animation.keyframe.event.data.ParticleKeyframeData;
import com.zigythebird.playeranimcore.animation.layered.modifier.AbstractModifier;
import com.zigythebird.playeranimcore.animation.layered.modifier.MirrorModifier;
import com.zigythebird.playeranimcore.bones.AdvancedPlayerAnimBone;
import com.zigythebird.playeranimcore.bones.PlayerAnimBone;
import com.zigythebird.playeranimcore.event.EventResult;
import com.zigythebird.playeranimcore.math.Vec3f;
import dev.rosenoire.character_engine.common.CharacterEngine;
import dev.rosenoire.character_engine.foundation.animation.scripting.ParticleDataJsonObject;
import net.collectively.geode.core.math;
import net.collectively.geode.core.types.double3;
import net.collectively.geode.mc.util.WorldUtil;
import net.minecraft.entity.PlayerLikeEntity;
import net.minecraft.particle.ParticleType;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

import java.io.StringReader;
import java.util.Optional;

public class ParticleKeyframeHandler implements CustomKeyFrameEvents.CustomKeyFrameHandler<ParticleKeyframeData> {
    @SuppressWarnings("UnstableApiUsage")
    @Override
    public EventResult handle(float animationTime, AnimationController controller, ParticleKeyframeData keyframe, AnimationData data) {
        // Sadly, only PlayerAnimationControllers have access to their world via their PlayerLikeEntity
        // avatar, so we can only play particles in the world using them.
        if (!(controller instanceof PlayerAnimationController playerController)) {
            return EventResult.PASS;
        }

        // region --------- PLAYER VARIABLES ---------
        PlayerLikeEntity playerLike = playerController.getAvatar();
        World world = playerLike.getEntityWorld();
        Random random = playerLike.getRandom();
        // endregion

        // region -------- DEBUGGING PURPOSES --------
        Animation currentAnimation = playerController.getCurrentAnimationInstance();
        String currentAnimationNameOrId = currentAnimation == null ? "NULL" : currentAnimation.getNameOrId();
        // endregion

        // region -------------- SCRIPT --------------
        String jsonText = keyframe.script();
        // Removes the last character from the script. Explanation: Since the script field is supposed to
        // be used for MoLang scripting, if no ';' is added at the end of the field by the user, Blockbench
        // automatically adds one. However, a JSON text with a semicolon at the end of it is not a valid
        // JSON input, and for that reason, it will crash the JSON formatter. Thus, we want to detect if
        // there is a semicolon at the end of the field and remove it if present.
        if (jsonText.endsWith(";")) jsonText = jsonText.substring(0, jsonText.length() - 1);

        StringReader stringReader = new StringReader(jsonText);
        JsonReader jsonReader = ParticleDataJsonObject.GSON.newJsonReader(stringReader);
        // Setting the strictness of the JSON reader makes it so the game won't crash if the JSON input is
        // malformed. We especially don't want the application to crash in that case, since it could simply
        // be a typo from the user, and is very annoying when debugging animations.
        jsonReader.setStrictness(Strictness.LENIENT);
        ParticleData particleData = ParticleDataJsonObject.GSON.fromJson(jsonReader, TypeToken.get(ParticleData.class));
        // endregion

        // region --------- PARTICLE POSITION --------
        double3 particlePosition = particleData.position().get(random);
        String locatorBoneName = keyframe.getLocator();
        AdvancedPlayerAnimBone locatorBone = playerController.getBone(locatorBoneName);

        if (locatorBone == null) {
            CharacterEngine.LOGGER.error("No bone with name '{}' could be found in animation '{}'!", locatorBoneName, currentAnimationNameOrId);
            return EventResult.PASS;
        }

        PlayerAnimBone locatorBoneTransform = playerController.get3DTransform(locatorBone);
        // Oh my god, thank you so much for allowing access to the modifiers of the controller, this
        // would have been so annoying...
        for (AbstractModifier abstractModifier : playerController.getModifiers()) {
            // TODO: Not the optimal way of doing it, but it should do the job for now.
            if (abstractModifier instanceof MirrorModifier) {
                locatorBoneTransform = abstractModifier.get3DTransform(locatorBone);
                // Symmetry on the X axis.
                particlePosition = particlePosition.mul(-1, 1, 1);
            }
        }

        // Rotates the particle position offset to always be in the direction of the player.
        double3 playerLikeLookRotation = new double3(-playerLike.getPitch(), playerLike.getYaw(), 0).modify(math::deg2rad);
        particlePosition = math.rotateVector(particlePosition, playerLikeLookRotation);
        // Combine all to create the base world position of the particles.
        Vec3f locatorBonePosition = locatorBoneTransform.getPositionVector();
        double3 effectiveParticlePosition = new
                double3(locatorBonePosition.x(), locatorBonePosition.y(), locatorBonePosition.x())
                .add(particlePosition)
                .add(playerLike.getEyePos())
                // Makes sure that the particle position is not affected by the velocity of the player
                // like entity. It does that by spawning it where the player like entity will be on the
                // next tick unless they change velocity suddenly (which is almost guaranteed not to happen).
                .add(new double3(playerLike.getRotationVector()).sub(playerLike.getVelocity()).mul(playerLike.getVelocity().length()));
        // endregion

        // region --------- PARTICLE SPAWNING --------
        int particleCount = particleData.count().get(random);
        for (int i = 0; i < particleCount; i++) {
            // region ----- PARTICLE IDENTIFIER -----
            Identifier particleIdentifier;
            String packedParticleIdentifiers = keyframe.getEffect().replace(" ", "");

            if (packedParticleIdentifiers.contains(",")) {
                String[] allParticleIdentifiers = packedParticleIdentifiers.split(",");
                int randomParticleIdentifierIndex = MathHelper.nextBetween(random, 0, allParticleIdentifiers.length - 1);
                particleIdentifier = Identifier.tryParse(allParticleIdentifiers[randomParticleIdentifierIndex]);

                if (particleIdentifier == null) {
                    CharacterEngine.LOGGER.error("Could not parse identifier '{}' ('{}') for animation '{}'!", allParticleIdentifiers[randomParticleIdentifierIndex], packedParticleIdentifiers, currentAnimationNameOrId);
                    continue;
                }
            } else {
                particleIdentifier = Identifier.tryParse(packedParticleIdentifiers);

                if (particleIdentifier == null) {
                    CharacterEngine.LOGGER.error("Could not parse identifier '{}' for animation '{}'!", packedParticleIdentifiers, currentAnimationNameOrId);
                    continue;
                }
            }
            // endregion

            // region -------- REGISTRY ENTRY -------
            Optional<RegistryEntry.Reference<ParticleType<?>>> registryEntry = Registries.PARTICLE_TYPE.getEntry(particleIdentifier);

            if (registryEntry.isEmpty()) {
                CharacterEngine.LOGGER.error("ParticleType registryEntry reference for identifier '{}' could not be found!", particleIdentifier);
                continue;
            }
            // endregion

            // region ---------- SPAWNING -----------
            if (registryEntry.get().value() instanceof SimpleParticleType simpleParticleType) {
                double3 offset = particleData.offset().get(random);
                double3 position = effectiveParticlePosition.add(offset);
                double3 velocity = particleData.velocity().get(random);

                WorldUtil.addParticleClient(
                        world,
                        simpleParticleType,
                        position,
                        velocity
                );

                // CharacterEngine.LOGGER.info(
                //         "Spawned '{}' ({}) at {} with base position {} and offset {} with a velocity of {}.",
                //         particleIdentifier, registryEntry.get().value().getClass().getName(),
                //         position.toPrettyString(), effectiveParticlePosition.toPrettyString(), offset.toPrettyString(),
                //         velocity.toPrettyString()
                // );

                continue;
            }
            // endregion

            CharacterEngine.LOGGER.warn("{} is not a supported particle type! Only SimpleParticleType is currently supported (Animation '{}' particle identifier '{}').", registryEntry.get().value().getClass(), currentAnimationNameOrId, particleIdentifier);
        }
        // endregion

        return EventResult.SUCCESS;
    }
}
