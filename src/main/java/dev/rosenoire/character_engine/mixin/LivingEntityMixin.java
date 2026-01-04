package dev.rosenoire.character_engine.mixin;

import net.collectively.geode.mc.internal.networking.SwingableItemC2SPayload;
import net.collectively.geode.mc.item.SwingableItem;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Inject(at=@At("HEAD"),method = "swingHand(Lnet/minecraft/util/Hand;Z)V", cancellable = true)
    private void characterEngine$swingHand(Hand hand, boolean fromServerPlayer, CallbackInfo ci) {
        LivingEntity livingEntity = (LivingEntity) (Object) this;
        ItemStack itemStack = livingEntity.getStackInHand(hand);

        if (itemStack.getItem() instanceof SwingableItem swingableItem) {
            ClientPlayNetworking.send(new SwingableItemC2SPayload(hand == Hand.OFF_HAND));
            if (!swingableItem.onSwing(livingEntity, itemStack)) {
                ci.cancel();
            }
        }
    }
}