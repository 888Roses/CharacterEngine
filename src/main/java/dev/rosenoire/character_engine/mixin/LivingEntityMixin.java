package dev.rosenoire.character_engine.mixin;

import dev.rosenoire.character_engine.foundation.item.SwingableItem;
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
            if (!swingableItem.onSwing(livingEntity, itemStack)) {
                ci.cancel();
            }
        }
    }
}