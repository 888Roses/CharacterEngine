package net.collectively.geode.mc._internal.payload;

import net.collectively.geode.Geode;
import net.collectively.geode.mc.item.SwingableItem;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public record SwingableItemAttackC2S() implements CustomPayload {
    private static final Identifier IDENTIFIER = Geode.internalId("swingable_item_attack_c2s");
    public static final Id<SwingableItemAttackC2S> ID = new Id<>(IDENTIFIER);

    public static final PacketCodec<PacketByteBuf, SwingableItemAttackC2S> CODEC = PacketCodec.of(
            (value, buf) -> {
            },
            buf -> new SwingableItemAttackC2S()
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    public void receiveOnServer(ServerPlayNetworking.Context context) {
        ServerPlayerEntity serverPlayerEntity = context.player();
        ItemStack itemStack = serverPlayerEntity.getMainHandStack();

        if (itemStack.getItem() instanceof SwingableItem swingableItem) {
            swingableItem.onSwing(serverPlayerEntity, itemStack);
        }
    }
}
