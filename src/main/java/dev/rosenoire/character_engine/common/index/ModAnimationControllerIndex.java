package dev.rosenoire.character_engine.common.index;

import com.zigythebird.playeranim.animation.PlayerAnimationController;
import com.zigythebird.playeranimcore.animation.layered.modifier.AdjustmentModifier;
import com.zigythebird.playeranimcore.animation.layered.modifier.MirrorModifier;
import com.zigythebird.playeranimcore.api.firstPerson.FirstPersonConfiguration;
import com.zigythebird.playeranimcore.api.firstPerson.FirstPersonMode;
import com.zigythebird.playeranimcore.enums.PlayState;
import com.zigythebird.playeranimcore.math.Vec3f;
import dev.rosenoire.character_engine.common.item.PistolItem;
import net.collectively.geode_animations.handlers.ParticleKeyframeHandler;
import net.collectively.geode_animations.handlers.SoundKeyframeHandler;
import net.collectively.geode.core.math;
import net.collectively.geode_animations.index.AnimationControllerIndex;
import net.minecraft.entity.PlayerLikeEntity;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;

import java.util.Optional;

public class ModAnimationControllerIndex extends AnimationControllerIndex {
    public static final Identifier BLASTER = register(
            "blaster",
            1000,
            playerLike -> createStandardPlayerAnimationController(playerLike, false)
    );
    public static final Identifier BLASTER_MIRRORED = register(
            "blaster_mirrored",
            1000,
            playerLike -> createStandardPlayerAnimationController(playerLike, true)
    );

    public static void initialize() {
        registerAll();
    }

    private static PlayerAnimationController createStandardPlayerAnimationController(PlayerLikeEntity playerLike, boolean isMirrored) {
        PlayerAnimationController controller = new PlayerAnimationController(playerLike, (i, j, k) -> PlayState.CONTINUE);

        controller.setSoundKeyframeHandler(new SoundKeyframeHandler());
        controller.setParticleKeyframeHandler(new ParticleKeyframeHandler());

        controller.setFirstPersonMode(FirstPersonMode.THIRD_PERSON_MODEL);
        controller.setFirstPersonConfiguration(new FirstPersonConfiguration(true, true, true, true, true));

        controller.addModifierLast(new AdjustmentModifier(partName -> rotateWithPitch(partName, controller, isMirrored)));

        if (isMirrored) {
            controller.addModifierLast(new MirrorModifier());
        }

        return controller;
    }

    @SuppressWarnings("SameParameterValue")
    private static Optional<AdjustmentModifier.PartModifier> rotateWithPitch(String partName, PlayerAnimationController controller, boolean isMirrored) {
        PlayerLikeEntity playerLikeEntity = controller.getAvatar();

        if (playerLikeEntity == null) {
            return Optional.empty();
        }

        String targetPartName = isMirrored && playerLikeEntity.getMainArm() == Arm.RIGHT ? "left_arm" : "right_arm";

        if (!partName.equals(targetPartName)) {
            return Optional.empty();
        }

        if (!PistolItem.hasPistolInHand(playerLikeEntity, isMirrored ? Hand.OFF_HAND : Hand.MAIN_HAND)) {
            return Optional.empty();
        }

        float pitch = math.deg2rad(playerLikeEntity.getPitch());
        return Optional.of(new AdjustmentModifier.PartModifier(
                new Vec3f(pitch, 0, 0),
                new Vec3f(0, 0, 0)
        ));
    }
}
