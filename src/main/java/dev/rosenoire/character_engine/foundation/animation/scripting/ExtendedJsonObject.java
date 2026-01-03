package dev.rosenoire.character_engine.foundation.animation.scripting;

import com.google.gson.*;
import dev.rosenoire.character_engine.common.CharacterEngine;
import net.collectively.geode.core.types.double3;
import org.jspecify.annotations.NonNull;

@SuppressWarnings({"SameParameterValue", "unused"})
public abstract class ExtendedJsonObject<T> {
    private final JsonObject jsonObject;

    public ExtendedJsonObject(JsonObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public static <T> Gson createAdapter(Class<T> clazz, ExtendedJsonObjectFactory<T> extendedJsonObjectFactory) {
        return new GsonBuilder()
                .registerTypeAdapter(clazz, readFromExtendedJsonObjectFactory(extendedJsonObjectFactory))
                .disableHtmlEscaping()
                .create();
    }

    private static <T> @NonNull JsonDeserializer<T> readFromExtendedJsonObjectFactory(ExtendedJsonObjectFactory<T> extendedJsonObjectFactory) {
        return (jsonElement, typeOfT, context) -> extendedJsonObjectFactory
                .createFrom(jsonElement.getAsJsonObject())
                .read();
    }

    public abstract T read();

    private <U> AbstractValueGetter<U> abstractProvider(String name, AbstractValueGetter<U> fallback, ProviderMethod<U> method) {
        if (!jsonObject.has(name)) {
            return fallback;
        }

        return method.getProvider(jsonObject.get(name), fallback);
    }


    protected final AbstractValueGetter<double3> double3Provider(String name, AbstractValueGetter<double3> fallback) {
        return this.abstractProvider(name, fallback, this::parseDouble3Provider);
    }

    protected final AbstractValueGetter<double3> double3Provider(String name, double3 fallback) {
        return double3Provider(name, new Double3ValueGetter(fallback));
    }

    private AbstractValueGetter<double3> parseDouble3Provider(JsonElement element, AbstractValueGetter<double3> fallback) {
        if (element.isJsonArray()) {
            JsonArray array = element.getAsJsonArray();

            if (array.size() != 3) {
                CharacterEngine.LOGGER.warn("Malformed JSON! Double3 value is expected, represented by an array of size 3. Array of size {} detected.", array.size());
                return fallback;
            }

            return new Double3ValueGetter(new double3(
                    array.get(0).getAsDouble(),
                    array.get(1).getAsDouble(),
                    array.get(2).getAsDouble()
            ));
        }

        if (element.isJsonObject()) {
            JsonObject jsonObject = element.getAsJsonObject();

            if (!jsonObject.has("min") || !jsonObject.has("max")) {
                return fallback;
            }

            AbstractValueGetter<double3> min = parseDouble3Provider(jsonObject.get("min"), new Double3ValueGetter(double3.zero));
            AbstractValueGetter<double3> max = parseDouble3Provider(jsonObject.get("max"), new Double3ValueGetter(double3.one));
            return new RandomDouble3ValueGetter(min, max);
        }

        return fallback;
    }


    protected final AbstractValueGetter<Double> doubleProvider(String name, AbstractValueGetter<Double> fallback) {
        return this.abstractProvider(name, fallback, this::parseDoubleProvider);
    }

    protected final AbstractValueGetter<Double> doubleProvider(String name, double fallback) {
        return doubleProvider(name, new DoubleValueGetter(fallback));
    }

    private AbstractValueGetter<Double> parseDoubleProvider(JsonElement element, AbstractValueGetter<Double> fallback) {
        if (element.isJsonPrimitive()) {
            return new DoubleValueGetter(element.getAsDouble());
        }

        if (element.isJsonObject()) {
            JsonObject jsonObject = element.getAsJsonObject();

            if (!jsonObject.has("min") || !jsonObject.has("max")) {
                return fallback;
            }

            AbstractValueGetter<Double> min = parseDoubleProvider(jsonObject.get("min"), new DoubleValueGetter(0d));
            AbstractValueGetter<Double> max = parseDoubleProvider(jsonObject.get("max"), new DoubleValueGetter(1d));
            return new RandomDoubleValueGetter(min, max);
        }

        return fallback;
    }


    protected final AbstractValueGetter<Float> floatProvider(String name, AbstractValueGetter<Float> fallback) {
        return this.abstractProvider(name, fallback, this::parseFloatProvider);
    }

    protected final AbstractValueGetter<Float> integerProvider(String name, float fallback) {
        return floatProvider(name, new FloatValueGetter(fallback));
    }

    private AbstractValueGetter<Float> parseFloatProvider(JsonElement element, AbstractValueGetter<Float> fallback) {
        if (element.isJsonPrimitive()) {
            return new FloatValueGetter(element.getAsFloat());
        }

        if (element.isJsonObject()) {
            JsonObject jsonObject = element.getAsJsonObject();

            if (!jsonObject.has("min") || !jsonObject.has("max")) {
                return fallback;
            }

            AbstractValueGetter<Float> min = parseFloatProvider(jsonObject.get("min"), new FloatValueGetter(0f));
            AbstractValueGetter<Float> max = parseFloatProvider(jsonObject.get("max"), new FloatValueGetter(1f));
            return new RandomFloatValueGetter(min, max);
        }

        return fallback;
    }


    protected final AbstractValueGetter<Integer> integerProvider(String name, AbstractValueGetter<Integer> fallback) {
        return this.abstractProvider(name, fallback, this::parseIntegerProvider);
    }

    protected final AbstractValueGetter<Integer> integerProvider(String name, int fallback) {
        return integerProvider(name, new IntegerValueGetter(fallback));
    }

    private AbstractValueGetter<Integer> parseIntegerProvider(JsonElement element, AbstractValueGetter<Integer> fallback) {
        if (element.isJsonPrimitive()) {
            return new IntegerValueGetter(element.getAsInt());
        }

        if (element.isJsonObject()) {
            JsonObject jsonObject = element.getAsJsonObject();

            if (!jsonObject.has("min") || !jsonObject.has("max")) {
                return fallback;
            }

            AbstractValueGetter<Integer> min = parseIntegerProvider(jsonObject.get("min"), new IntegerValueGetter(0));
            AbstractValueGetter<Integer> max = parseIntegerProvider(jsonObject.get("max"), new IntegerValueGetter(1));
            return new RandomIntegerValueGetter(min, max);
        }

        return fallback;
    }


    public interface ExtendedJsonObjectFactory<T> {
        ExtendedJsonObject<T> createFrom(JsonObject jsonObject);
    }
}