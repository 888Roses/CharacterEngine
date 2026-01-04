package dev.rosenoire.character_engine.mixin;

import net.collectively.geode.mc.item.SwingableItem;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin {
    @Inject(at=@At("TAIL"),method = "swingHand")
    private void characterEngine$swingHand(Hand hand, CallbackInfo ci) {
        ClientPlayerEntity livingEntity = (ClientPlayerEntity) (Object) this;
        ItemStack itemStack = livingEntity.getStackInHand(hand);

        if (itemStack.getItem() instanceof SwingableItem swingableItem) {
            swingableItem.onSwing(livingEntity, itemStack, hand);
        }
    }
}