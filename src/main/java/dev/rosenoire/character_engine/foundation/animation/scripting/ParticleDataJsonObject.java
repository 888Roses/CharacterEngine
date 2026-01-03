package dev.rosenoire.character_engine.foundation.animation.scripting;

import com.google.gson.*;
import dev.rosenoire.character_engine.foundation.animation.ParticleData;
import net.collectively.geode.core.types.double3;

import java.util.Random;

public class ParticleDataJsonObject extends ExtendedJsonObject<ParticleData> {
    public static final Gson GSON = createAdapter(ParticleData.class, ParticleDataJsonObject::new);

    public ParticleDataJsonObject(JsonObject jsonObject) {
        super(jsonObject);
    }

    @Override
    public ParticleData read() {
        return new ParticleData(
                double3Provider("position", double3.zero),
                double3Provider("velocity", double3.zero),
                double3Provider("offset", double3.zero),
                integerProvider("count", 1)
        );
    }
}