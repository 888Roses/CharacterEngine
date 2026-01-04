package net.collectively.geode.mc.internal.networking;

import net.collectively.geode.mc.item.SwingableItem;
import net.collectively.geode.Geode;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;

public record SwingableItemC2SPayload(boolean isOffHand) implements CustomPayload {
    private static final Identifier IDENTIFIER = Geode.internalId("swingable_item_c2s");
    public static final Id<SwingableItemC2SPayload> ID = new Id<>(IDENTIFIER);
    public static final PacketCodec<PacketByteBuf, SwingableItemC2SPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.BOOLEAN,
            SwingableItemC2SPayload::isOffHand,
            SwingableItemC2SPayload::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static void receiveOnServer(SwingableItemC2SPayload payload, ServerPlayNetworking.Context context) {
        ItemStack itemStack = context.player().getStackInHand(payload.isOffHand() ? Hand.OFF_HAND : Hand.MAIN_HAND);
        if (itemStack.getItem() instanceof SwingableItem swingableItem) {
            swingableItem.onSwing(context.player(), itemStack);
        }
    }
}
