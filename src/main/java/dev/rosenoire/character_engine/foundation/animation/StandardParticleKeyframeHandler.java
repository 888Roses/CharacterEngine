package dev.rosenoire.character_engine.foundation.animation;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.zigythebird.playeranim.animation.PlayerAnimationController;
import com.zigythebird.playeranimcore.animation.AnimationController;
import com.zigythebird.playeranimcore.animation.AnimationData;
import com.zigythebird.playeranimcore.animation.keyframe.event.CustomKeyFrameEvents;
import com.zigythebird.playeranimcore.animation.keyframe.event.data.ParticleKeyframeData;
import com.zigythebird.playeranimcore.animation.layered.modifier.AbstractModifier;
import com.zigythebird.playeranimcore.animation.layered.modifier.MirrorModifier;
import com.zigythebird.playeranimcore.bones.AdvancedPlayerAnimBone;
import com.zigythebird.playeranimcore.event.EventResult;
import com.zigythebird.playeranimcore.math.Vec3f;
import dev.rosenoire.character_engine.common.CharacterEngine;
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
import java.lang.reflect.Type;
import java.util.Optional;

/**
 * <h1>Particle Keyframe Handler</h1>
 * Standard handler for particle keyframes in animations.<br/><br/>
 *
 * <h2>Format:</h2>
 * <h4>Effect</h4>
 * {@code Identifier} referencing the particle effect to play (Example: {@code example_mod:example_particle})<br/><br/>
 *
 * <h4>Locator</h4>
 * The name of the bone parent of this particle.<br/><br/>
 *
 * <h4>Script</h4>
 * Contains information about the spawned particle in JSON format. The valid keys are:
 * <h5>position</h5> The position offset of that particle. This is represented by a three-dimensional {@code double} array.
 * <h5>velocity</h5> The velocity of that particle when spawned. This is represented by a three-dimensional {@code double} array.
 * <h5>random_spread</h5> Random offset applied to the position of each spawned particle. This is represented by a three-dimensional {@code double} array.
 * <h5>count</h5> How many particles are spawned at once. This is represented by an {@code int}.
 * <br/>
 *
 * <h3>Example:</h3>
 * <pre>
 *     {@code
 *     {
 *         "position": [0.75, 0.36, 0.15], // Sets the base position of the spawned particles to this value.
 *         "velocity": [4, 0, 0], // Sets the velocity of the spawned particles to 4.
 *         "spread": [1, 1, 1], // Adds a random offset between -1 and +1 on every axes for each particle spawned.
 *         "count": 4 // Sets the count of particles spawned by this keyframe to 4.
 *     }
 *     }
 * </pre>
 */
public class StandardParticleKeyframeHandler implements CustomKeyFrameEvents.CustomKeyFrameHandler<ParticleKeyframeData> {
    public static class ParticleDataLoader implements JsonDeserializer<ParticleData> {
        public static final Gson GSON = new GsonBuilder()
                .registerTypeAdapter(ParticleData.class, new ParticleDataLoader())
                .disableHtmlEscaping()
                .create();

        private static double3 getDouble3(JsonObject obj, String name, double3 fallback) {
            double3 value = new double3(0);
            if (obj.has(name)) {
                JsonArray array = obj.getAsJsonArray(name);

                int arraySize = array.size();
                if (arraySize == 3) {
                    value = new double3(
                            array.get(0).getAsDouble(),
                            array.get(1).getAsDouble(),
                            array.get(2).getAsDouble()
                    );
                } else {
                    CharacterEngine.LOGGER.error("Invalid particle data '{}'! Expected 3 doubles, found array of size {}.", name, arraySize);
                }
            }

            return value;
        }

        private static int getInt(JsonObject obj, String name, int fallback) {
            if (obj.has(name)) {
                return obj.get(name).getAsInt();
            }

            return fallback;
        }

        @Override
        public ParticleData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject particleObject = json.getAsJsonObject();

            double3 position = getDouble3(particleObject, "position", double3.zero);
            double3 velocity = getDouble3(particleObject, "velocity", double3.zero);
            double3 spread = getDouble3(particleObject, "spread", double3.zero);
            int count = math.max(1, getInt(particleObject, "count", 1));

            return new ParticleData(position, velocity, spread, count);
        }
    }

    public record ParticleData(double3 position, double3 velocity, double3 spread, int count) {
    }

    @Override
    public EventResult handle(
            float animationTick,
            AnimationController animationController,
            ParticleKeyframeData particleKeyframeData,
            AnimationData animationData
    ) {
        if (animationController instanceof PlayerAnimationController playerController) {
            PlayerLikeEntity playerLike = playerController.getAvatar();
            World world = playerLike.getEntityWorld();

            String particleName = particleKeyframeData.getEffect();
            Identifier particleIdentifier = Identifier.tryParse(particleName);

            if (particleIdentifier == null) {
                CharacterEngine.LOGGER.error("Could not parse Identifier '{}'!", particleName);
                return EventResult.PASS;
            }

            Optional<RegistryEntry.Reference<ParticleType<?>>> key = Registries.PARTICLE_TYPE.getEntry(particleIdentifier);

            if (key.isEmpty()) {
                CharacterEngine.LOGGER.error("ParticleType RegistryEntry Reference for Identifier '{}' is null!", particleIdentifier);
                return EventResult.PASS;
            }

            ParticleType<?> particleType = key.get().value();
            if (particleType instanceof SimpleParticleType simpleParticleType) {
                // Transform JSON text into valid output.
                String jsonText = particleKeyframeData.script();
                jsonText = jsonText.replace("\\n", "").replace("\\\"", "\"");
                // By default, for some reason, the script channel always ends with a semicolon ';', so we need to substring it out.
                jsonText = jsonText.substring(0, jsonText.length() - 1);
                CharacterEngine.LOGGER.info("Modified Script: {}", jsonText);

                // Read and deserialize the JSON into particle data.
                StringReader reader = new StringReader(jsonText);
                JsonReader jsonReader = ParticleDataLoader.GSON.newJsonReader(reader);
                jsonReader.setStrictness(Strictness.LENIENT);
                ParticleData particleData = ParticleDataLoader.GSON.fromJson(jsonReader, TypeToken.get(ParticleData.class));

                double3 locatorPosition = particleData.position();
                Random random = playerLike.getRandom();

                String locatorElementName = particleKeyframeData.getLocator();
                AdvancedPlayerAnimBone locatorBone = animationController.getBone(locatorElementName);

                if (locatorBone == null) {
                    CharacterEngine.LOGGER.error("Could not find any bone named '{}' for particle locator: '{}'!", locatorElementName, particleKeyframeData.getLocator());
                    return EventResult.PASS;
                }

                var locatorElementTransform = animationController.get3DTransformRaw(locatorBone);
                for (AbstractModifier abstractModifier : animationController.getModifiers()) {
                    if (abstractModifier instanceof MirrorModifier) {
                        locatorElementTransform = abstractModifier.get3DTransform(locatorBone);
                        locatorPosition = locatorPosition.mul(-1, 1, 1);
                    }
                }

                Vec3f locatorElementPosition = locatorElementTransform.getPositionVector();
                locatorPosition = math.rotateVector(locatorPosition,
                        new double3(-playerLike.getPitch(), playerLike.getYaw(), 0).modify(math::deg2rad)
                );

                double3 particlePosition = new double3(locatorElementPosition.x(), locatorElementPosition.y(), locatorElementPosition.x())
                        .add(locatorPosition)
                        .add(playerLike.getEyePos());

                for (var i = 0; i < particleData.count(); i++) {
                    WorldUtil.addParticleClient(
                            world,
                            simpleParticleType,
                            particlePosition.add(
                                    MathHelper.nextDouble(random, -particleData.spread().x(), particleData.spread().x()),
                                    MathHelper.nextDouble(random, -particleData.spread().y(), particleData.spread().y()),
                                    MathHelper.nextDouble(random, -particleData.spread().z(), particleData.spread().z())
                            ),
                            double3.zero
                    );
                }

                return EventResult.SUCCESS;
            }

            CharacterEngine.LOGGER.warn("{} is not a SimpleParticleType! Only SimpleParticleType are currently supported.", particleType.getClass());
            return EventResult.PASS;
        }

        return EventResult.PASS;
    }
}
