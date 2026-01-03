package net.collectively.geode_animations.scripting;

import com.google.gson.JsonElement;

@FunctionalInterface
public interface ProviderMethod<U> {
    AbstractValueGetter<U> getProvider(JsonElement element, AbstractValueGetter<U> fallback);
}
