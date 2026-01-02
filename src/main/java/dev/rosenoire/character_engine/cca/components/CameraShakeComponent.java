package dev.rosenoire.character_engine.cca.components;

import dev.rosenoire.character_engine.cca.ModEntityComponentIndex;
import net.collectively.geode.cca.components.impl.PlayerEntityComponent;
import net.collectively.geode.core.math;
import net.collectively.geode.core.types.double2;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import org.ladysnake.cca.api.v3.component.Component;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.tick.CommonTickingComponent;

import java.util.function.UnaryOperator;

public class CameraShakeComponent extends PlayerEntityComponent implements CommonTickingComponent {
    // region Essentials
    public CameraShakeComponent(PlayerEntity player) {
        super(player);
    }

    @Override
    public ComponentKey<? extends Component> getKey() {
        return ModEntityComponentIndex.CAMERA_SHAKE;
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

    private double intensity;
    private long startDuration;
    private long duration;
    private EasingFunction intensityFunction;

    private double2 currentShakeVector;

    public void shake(double intensity, long duration, EasingFunction intensityFunction) {
        this.intensity = intensity;
        startDuration = duration;
        this.duration = duration;
        this.intensityFunction = intensityFunction;
    }

    @Override
    public void tick() {
        if (duration > 0) {
            if (intensityFunction == null) {
                intensityFunction = EasingFunction.LINEAR;
            }

            double progress = duration / (double) startDuration;
            double currentIntensity = intensityFunction.ease(progress, intensity * progress);

            Random random = player.getRandom();

            double randomAngle = math.deg2rad(random.nextFloat() * 360f);
            double sin = math.sin(randomAngle);
            double cos = math.cos(randomAngle);
            currentShakeVector = new double2(sin, cos).mul(currentIntensity);

            duration--;
        }
    }

    public boolean isShaking() {
        return duration > 0;
    }

    public double2 getCurrentShakeVector() {
        return currentShakeVector;
    }

    @FunctionalInterface
    public interface EasingFunction {
        EasingFunction LINEAR = (progress, currentValue) -> currentValue;
        EasingFunction SQUARED = (progress, currentValue) -> math.clamp(currentValue * currentValue, 0, currentValue);

        double ease(double progress, double currentValue);
    }
}
