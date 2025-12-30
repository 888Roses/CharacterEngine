package dev.rosenoire.character_engine.foundation.animation;

import com.zigythebird.playeranim.animation.PlayerAnimationController;
import com.zigythebird.playeranim.util.ClientUtil;
import com.zigythebird.playeranimcore.animation.AnimationController;
import com.zigythebird.playeranimcore.animation.AnimationData;
import com.zigythebird.playeranimcore.animation.keyframe.event.CustomKeyFrameEvents;
import com.zigythebird.playeranimcore.animation.keyframe.event.data.SoundKeyframeData;
import com.zigythebird.playeranimcore.event.EventResult;
import net.collectively.geode.core.math;
import net.collectively.geode.mc.util.GeodeText;
import net.minecraft.entity.PlayerLikeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.Optional;

public class StandardSoundKeyframeHandler implements CustomKeyFrameEvents.CustomKeyFrameHandler<SoundKeyframeData> {
    @Override
    public EventResult handle(
            float animationTick,
            AnimationController controller,
            SoundKeyframeData keyFrameData,
            AnimationData animationData
    ) {
        if (controller instanceof PlayerAnimationController playerController) {
            PlayerLikeEntity playerLike = playerController.getAvatar();
            Vec3d position = playerLike.getEntityPos();

            String[] segments = keyFrameData.getSound().split("\\|");
            Identifier soundEventIdentifier = Identifier.tryParse(segments[0]);

            if (soundEventIdentifier == null) {
                if (playerLike instanceof PlayerEntity player) {
                    player.sendMessage(Text.literal("Literal sound name '" + segments[0] + "' could not be parsed!").formatted(Formatting.RED), false);
                }

                return EventResult.PASS;
            }

            Optional<RegistryEntry.Reference<SoundEvent>> soundRegistryKey = Registries.SOUND_EVENT.getEntry(soundEventIdentifier);

            if (soundRegistryKey.isEmpty()) {
                if (playerLike instanceof PlayerEntity player) {
                    player.sendMessage(Text.literal("Could not find a registered sound with the identifier '" + soundEventIdentifier + "'!").formatted(Formatting.RED), false);
                }

                return EventResult.PASS;
            }

            float volume = segments.length > 1 ? Float.parseFloat(segments[1]) : 1;
            float pitch = segments.length > 2 ? Float.parseFloat(segments[2]) : 1;

            pitch = math.clamp01(pitch + MathHelper.nextFloat(playerLike.getRandom(), -0.1f, 0.1f));

            boolean isClientSide = playerLike.getEntityWorld().isClient();
            if (isClientSide) {
                SoundEvent soundEvent = SoundEvent.of(soundEventIdentifier);
                ClientUtil.getLevel().playSoundClient(position.x, position.y, position.z, soundEvent, SoundCategory.PLAYERS, volume, pitch, true);
            }

            // TODO: Fix server side sounds to not play when having a screen open.
            // if (playerLike instanceof ServerPlayerEntity serverPlayerEntity) {
            //     playerLike.getEntityWorld().playSound(null, position.x, position.y, position.z, soundRegistryKey.get(), SoundCategory.PLAYERS, volume, pitch);
            // }

            // if (playerLike instanceof PlayerEntity player) {
            //     player.sendMessage(
            //             new GeodeText()
            //                     .literal(isClientSide ? "Client " : "Server ").withColor(isClientSide ? 0xebc38a : 0x8ab7eb)
            //                     .literal("Animation ")
            //                     .literal("'" + controller.getCurrentAnimationInstance().getNameOrId() + "'", Formatting.GREEN)
            //                     .literal(" playing sound ")
            //                     .literal("'" + soundEventIdentifier + "'").withColor(0x8867c5)
            //                     .build(),
            //             false
            //     );
            // }

            return EventResult.SUCCESS;
        }

        return EventResult.PASS;
    }
}
