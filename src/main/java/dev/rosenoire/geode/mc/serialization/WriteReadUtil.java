package dev.rosenoire.geode.mc.serialization;

import dev.rosenoire.geode.core.types.double3;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;

public interface WriteReadUtil {
    static void writeDouble3(WriteView view, String name, double3 value) {
        view.put(name, double3.CODEC, value);
    }

    static double3 readDouble3(ReadView view, String name, double3 defaultValue) {
        return view.read(name, double3.CODEC).orElse(defaultValue);
    }
}
