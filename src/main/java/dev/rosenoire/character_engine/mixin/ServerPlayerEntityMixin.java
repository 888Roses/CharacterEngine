package dev.rosenoire.character_engine.mixin;

import net.collectively.geode.mc.item.SwingableItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin {
    @Inject(at=@At("TAIL"),method = "swingHand")
    private void characterEngine$swingHand(Hand hand, CallbackInfo ci) {
        ServerPlayerEntity livingEntity = (ServerPlayerEntity) (Object) this;
        ItemStack itemStack = livingEntity.getStackInHand(hand);

        if (itemStack.getItem() instanceof SwingableItem swingableItem) {
            swingableItem.onSwing(livingEntity, itemStack, hand);
        }
    }
}