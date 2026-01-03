package net.collectively.geode_animations.loaders;

import com.google.gson.Strictness;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import net.collectively.geode_animations.GeodeAnimations;
import net.collectively.geode_animations.controllers.AnimationQueueData;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceReloader;
import net.minecraft.util.Identifier;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class AnimationQueueResourceReloader implements ResourceReloader {
    public static final Identifier KEY = Identifier.of(GeodeAnimations.ID, "animation_queues");
    public static final Map<Identifier, AnimationQueueData> QUEUES = new HashMap<>();

    @Override
    public CompletableFuture<Void> reload(Store store, Executor prepareExecutor, Synchronizer reloadSynchronizer, Executor applyExecutor) {
        ResourceManager manager = store.getResourceManager();
        return CompletableFuture.runAsync(() -> onResourceManagerReload(manager), applyExecutor)
                .thenCompose(reloadSynchronizer::whenPrepared);
    }

    /**
     * Internal use only!
     */
    public void onResourceManagerReload(ResourceManager manager) {
        QUEUES.clear();

        Map<Identifier, Resource> resources = manager.findResources(
                "animation_queues",
                resourceLocation -> resourceLocation.getPath().endsWith(".json")
        );

        for (Map.Entry<Identifier, Resource> resource : resources.entrySet()) {
            Identifier identifier = resource.getKey()
                    .withPath(original -> original.substring("animation_queues/".length()))
                    .withPath(original -> original.substring(0, original.length() - ".json".length()));
            try (InputStream stream = resource.getValue().getInputStream()) {
                AnimationQueueData queueData = loadAnimationQueues(stream);
                QUEUES.put(identifier, queueData);
                GeodeAnimations.LOGGER.info("Registered animation queue '{}'!", identifier);
            } catch (Exception exception) {
                GeodeAnimations.LOGGER.error("Geode Animations failed to load animation queue '{}' because: ", identifier, exception);
            }
        }
    }

    private static AnimationQueueData loadAnimationQueues(InputStream resource) throws IOException {
        try (Reader reader = new InputStreamReader(resource)) {
            JsonReader jsonReader = AnimationQueueData.GSON.newJsonReader(reader);
            jsonReader.setStrictness(Strictness.LENIENT);
            return AnimationQueueData.GSON.fromJson(jsonReader, TypeToken.get(AnimationQueueData.class));
        }
    }
}
