package dev.rosenoire.character_engine.foundation.index;

import dev.rosenoire.character_engine.foundation.item.PlayerStackChangeCallback;
import dev.rosenoire.character_engine.foundation.item.StackChangeAwareItem;

public interface FoundationCallbacks {
    static void subscribe() {
        PlayerStackChangeCallback.EVENT.register((player, previousStack, itemStack, hand) -> {
            if (itemStack.getItem() instanceof StackChangeAwareItem stackChangeAwareItem) {
                stackChangeAwareItem.onSelected(player, hand, previousStack, itemStack);
            }

            if (previousStack != null && previousStack.getItem() instanceof StackChangeAwareItem stackChangeAwareItem) {
                stackChangeAwareItem.onDeselected(player, hand, previousStack, itemStack);
            }
        });
    }
}
