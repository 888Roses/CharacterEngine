package dev.rosenoire.character_engine.common.index;

import dev.rosenoire.character_engine.common.item.BlasterItem;
import dev.rosenoire.character_engine.foundation.item.PlayerStackChangeCallback;

public interface ModCommonCallbackIndex {
    static void subscribeCallbacks() {
        PlayerStackChangeCallback.EVENT.register(BlasterItem::onHandStackChanged);
    }
}
