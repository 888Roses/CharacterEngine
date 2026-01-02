package dev.rosenoire.character_engine.cca.components;

import dev.rosenoire.character_engine.cca.ModEntityComponentIndex;
import net.collectively.geode.cca.components.impl.PlayerEntityComponent;
import net.collectively.geode.core.math;
import net.collectively.geode.core.types.double3;
import net.collectively.geode.mc.util.EntityHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import org.ladysnake.cca.api.v3.component.Component;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.tick.CommonTickingComponent;

public class PlayerFovComponent extends PlayerEntityComponent implements CommonTickingComponent {
    // region Essentials
    public PlayerFovComponent(PlayerEntity player) {
        super(player);
    }

    @Override
    public ComponentKey<? extends Component> getKey() {
        return ModEntityComponentIndex.PLAYER_FOV;
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

    public double targetFov;

    private double previousFov;
    private double currentFov;

    private double getFovIncrease() {
        double3 velocity = new double3(player.getEntityPos()).sub(EntityHelper.getLastPos(player));
        double speed = velocity.squaredHorMag();
        return math.clamp(speed / 9f, 0, 1.25f) + 1;
    }

    @Override
    public void tick() {
        previousFov = currentFov;
        targetFov = getFovIncrease();
        currentFov = math.lerp(0.25f, currentFov, targetFov);
    }

    public double getSmoothFov(float deltaTime) {
        return math.lerp(deltaTime, previousFov, currentFov);
    }
}
