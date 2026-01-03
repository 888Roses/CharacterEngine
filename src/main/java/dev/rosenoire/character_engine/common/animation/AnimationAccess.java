package dev.rosenoire.character_engine.common.animation;

import com.zigythebird.playeranim.animation.PlayerAnimationController;
import com.zigythebird.playeranim.api.PlayerAnimationAccess;
import com.zigythebird.playeranimcore.animation.Animation;
import net.minecraft.entity.PlayerLikeEntity;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public interface AnimationAccess {
    static @Nullable PlayerAnimationController getControllerForHand(
            PlayerLikeEntity playerLike, Hand hand,
            Identifier mainHand, Identifier offHand
    ) {
        Identifier controllerId = (hand == Hand.MAIN_HAND) == (playerLike.getMainArm() == Arm.RIGHT) ? mainHand : offHand;
        return (PlayerAnimationController) PlayerAnimationAccess.getPlayerAnimationLayer(playerLike, controllerId);
    }
}
