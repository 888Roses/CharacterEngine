package dev.rosenoire.geode.networking;
import dev.rosenoire.geode.core.types.*;
import net.minecraft.network.RegistryByteBuf;

public interface RegistryByteBufHelper {
    static double3 readDouble3(RegistryByteBuf buf) {
        return new double3(buf.readDouble(), buf.readDouble(), buf.readDouble());
    }

    static void writeDouble3(RegistryByteBuf buf, double3 v) {
        buf.writeDouble(v.x());
        buf.writeDouble(v.y());
        buf.writeDouble(v.z());
    }
}
