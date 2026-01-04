package net.collectively.geode.mc.internal;

import net.collectively.geode.mc.internal.index.GeodeNetworkingIndex;

public class GeodeMcModule {
    public static void initialize() {
        GeodeNetworkingIndex.registerAll();
    }
}