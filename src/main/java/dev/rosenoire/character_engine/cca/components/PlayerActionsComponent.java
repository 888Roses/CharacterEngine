package dev.rosenoire.character_engine.cca.components;

import dev.rosenoire.character_engine.cca.ModEntityComponentIndex;
import net.collectively.geode.cca.components.impl.PlayerEntityComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import org.ladysnake.cca.api.v3.component.Component;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.tick.CommonTickingComponent;

import java.util.*;

public class PlayerActionsComponent extends PlayerEntityComponent implements CommonTickingComponent {
    //region Essentials
    public PlayerActionsComponent(PlayerEntity player) {
        super(player);
    }

    @Override
    public ComponentKey<? extends Component> getKey() {
        return ModEntityComponentIndex.ACTIONS;
    }
    // endregion

    // region Save & Load
    @Override
    public void readData(ReadView readView) {

    }

    @Override
    public void writeData(WriteView writeView) {

    }
    // endregion

    // TODO: Sync?
    private final Map<Long, List<Runnable>> queue = new HashMap<>();

    public void enqueue(long delay, Runnable... actions) {
        long time = getEntityWorld().getTime() + delay;

        if (!queue.containsKey(time)) {
            queue.put(time, new ArrayList<>());
        }

        List<Runnable> queued = queue.get(time);
        if (actions.length == 1) queued.add(actions[0]);
        else queued.addAll(Arrays.asList(actions));
    }

    @Override
    public void tick() {
        long time = getEntityWorld().getTime();
        if (queue.containsKey(time)) {
            List<Runnable> queued = queue.get(time);
            for (Runnable action : queued) {
                action.run();
            }

            queued.clear();
        }
    }
}