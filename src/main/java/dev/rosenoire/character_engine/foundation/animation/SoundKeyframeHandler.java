package dev.rosenoire.character_engine.foundation.animation;

import com.zigythebird.playeranim.animation.PlayerAnimationController;
import com.zigythebird.playeranimcore.animation.Animation;
import com.zigythebird.playeranimcore.animation.AnimationController;
import com.zigythebird.playeranimcore.animation.AnimationData;
import com.zigythebird.playeranimcore.animation.keyframe.event.CustomKeyFrameEvents;
import com.zigythebird.playeranimcore.animation.keyframe.event.data.SoundKeyframeData;
import com.zigythebird.playeranimcore.event.EventResult;
import dev.rosenoire.character_engine.common.CharacterEngine;
import net.collectively.geode.core.types.double3;
import net.minecraft.entity.PlayerLikeEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

import java.util.Optional;

/// # Sounds
/// While PAL (Player Animation Library) has its own sound keyframe handler, it is quite limited, so this library brings a more advanced one. In Blockbench, adding a sound keyframe is as simple as:
/// 1. Clicking on the button named `Animate Effects` in between `Bring Up All Animators` and the flag icon.
/// 2. In the Timeline, under "Effects", clicking on the plus icon next to the `Sound` channel.
/// 3. Go in the Keyframe window and edit the fields there. ***
/// ## Fields
/// #### Effect
/// Contains information about the played sound (sound identifier, volume, pitch). Those information are formatted as follows:
///
/// `identifier`<br/> Represents the identifiers of the sound(s) to play. Providing multiple sounds is as simple as separating them with commas ','. When providing multiple sounds, a random one will be picked in the array. For example:
/// ```
/// minecraft:item.brush.brushing.generic, minecraft:item.brush.brushing.sand
/// ```
///
/// `volume`<br/> Represents the volume of the sound played. See the section on numeric value format below for more information on the syntax. For example:
/// ```
/// volume = 0.5
/// ```
///
/// `pitch`<br/> Represents the pitch of the sound played. See the section on numeric value format below for more information on the syntax. For example:
/// ```
/// pitch = {0.9, 1.1}
/// ```
/// <br/>
///
/// You may now combine those different keys by separating them with semicolon ';' characters. For example:
/// ```
/// minecraft:item.brush.brushing.generic, minecraft:item.brush.brushing.sand; volume = 0.5; pitch = {0.9, 1.1};
/// ```
/// #### Locator
/// Unused, work in progress.
///
/// ***
/// ### Syntax of Numeric Fields
/// Numeric fields are fields that require a number value. Those can be, for example, the `volume` and the `pitch` in this keyframe's **Effects** field. Their syntax is quite easy:
///
/// **Constant**<br/> A value that does not change. It can be any number. For example:
/// ```
/// my_field = 1234;
/// ```
///
/// **List**<br/> An array of numbers to pick from. A random number will be chosen in this array. Each entry is separated using comma ',' characters. In order for the field to know that its value is a `list`, the value must start with a `[`. For example:
/// ```
/// my_field = [1, 2, 3, 4];
/// ```
///
/// Note: It is not required to end with a `]` but looks clearer and nicer that way. For example, this does the exact same as the above syntax:
/// ```
/// my_field = [1, 2, 3, 4;
/// ```
///
/// **Random**<br/> A random value between two bounds. The value will be chosen between the first provided number (min) and the second provided number (max). For example:
/// ```
/// my_field = {1, 4};
/// ```
/// Special Cases:
///   - If more than `2` numbers are provided, only the first two numbers will be used to determine the bound.
///   - If less than `2` numbers are provided, the default value of that field will be returned instead, ignoring this instruction.
public class SoundKeyframeHandler implements CustomKeyFrameEvents.CustomKeyFrameHandler<SoundKeyframeData> {
    @Override
    public EventResult handle(
            float animationTime,
            AnimationController controller,
            SoundKeyframeData keyframe,
            AnimationData animationData
    ) {
        // Sadly, only PlayerAnimationControllers have access to their world via their PlayerLikeEntity
        // avatar, so we can only play sounds in the world using them.
        if (!(controller instanceof PlayerAnimationController playerController)) {
            return EventResult.PASS;
        }

        // region --------- PLAYER VARIABLES ---------
        PlayerLikeEntity playerLike = playerController.getAvatar();
        World world = playerLike.getEntityWorld();
        Random random = playerLike.getRandom();
        // endregion

        // region -------- DEBUGGING PURPOSES --------
        Animation currentAnimation = playerController.getCurrentAnimationInstance();
        String currentAnimationNameOrId = currentAnimation == null ? "NULL" : currentAnimation.getNameOrId();
        // endregion

        String[] segments = keyframe.getSound().replace(" ", "").split(";");

        // region ------------ IDENTIFIER ------------
        String packedSoundIdentifiers = segments[0];
        Identifier soundEventIdentifier;

        if (packedSoundIdentifiers.contains(",")) {
            String[] allSoundIdentifiers = packedSoundIdentifiers.split(",");
            int randomSoundIdentifierIndex = MathHelper.nextBetween(random, 0, allSoundIdentifiers.length - 1);
            soundEventIdentifier = Identifier.tryParse(allSoundIdentifiers[randomSoundIdentifierIndex]);

            if (soundEventIdentifier == null) {
                CharacterEngine.LOGGER.error("Could not parse identifier '{}' ('{}') for animation '{}'!", allSoundIdentifiers[randomSoundIdentifierIndex], packedSoundIdentifiers, currentAnimationNameOrId);
                return EventResult.PASS;
            }
        } else {
            soundEventIdentifier = Identifier.tryParse(packedSoundIdentifiers);

            if (soundEventIdentifier == null) {
                CharacterEngine.LOGGER.error("Could not parse identifier '{}' for animation '{}'!", packedSoundIdentifiers, currentAnimationNameOrId);
                return EventResult.PASS;
            }
        }
        // endregion

        // region ---------- REGISTRY ENTRY ----------
        // NOTE: Currently only used to check whether the sound is registered or not. Might remove later.
        // It will be used to play sounds on the server side though, so good thing to keep it for now.
        Optional<RegistryEntry.Reference<SoundEvent>> registryEntry = Registries.SOUND_EVENT.getEntry(soundEventIdentifier);

        if (registryEntry.isEmpty()) {
            CharacterEngine.LOGGER.error("SoundEvent registryEntry reference for identifier '{}' could not be found (Animation '{}')!", soundEventIdentifier, currentAnimationNameOrId);
            return EventResult.PASS;
        }
        // endregion

        // region ---------- VOLUME & PITCH ----------
        float volume = 1;
        float pitch = 1;

        if (segments.length > 1) {
            for (String segment : segments) {
                if (segment.startsWith("volume")) {
                    volume = parseFloatValue(random, segment, "volume", volume);
                }

                if (segment.startsWith("pitch")) {
                    pitch = parseFloatValue(random, segment, "pitch", pitch);
                }
            }
        }
        // endregion

        // region ------------ PLAY SOUND ------------
        // TODO: Fix server side.
        //       Currently, using server side `playSound` methods results in undesirable sounds playing
        //       at seemingly random times (I.E. when clicking on a different item in the inventory of
        //       the player, etc.).
        //       NOTE: It *might* be due to how I made the `PlayerStackChangeCallback`.

        if (world.isClient()) {
            SoundEvent soundEvent = SoundEvent.of(soundEventIdentifier);
            // TODO: Would be interesting to be able to add an offset to the position in the sound field
            //       similarly to how it's done for the volume & pitch, instead of it being locked.
            double3 position = new double3(playerLike.getEntityPos());
            world.playSoundClient(
                    position.x(), position.y(), position.z(),
                    // TODO: Add a way to choose the sound category akin to how you can change the pitch
                    //       and volume. Same goes for the "useDistance" field.
                    soundEvent, SoundCategory.PLAYERS, volume, pitch, false
            );
        }
        // endregion

        return EventResult.SUCCESS;
    }

    private float parseFloatValue(Random random, String segment, String name, float fallback) {
        String stripped = segment.substring((name + "=").length());

        // region -------- RANDOM LIST ---------
        // Returns a random value in a list of provided float values, where entries are split using commas ','.
        if (stripped.startsWith("[")) {
            stripped = stripped.replace("[", "").replace("]", "");

            String[] valuesAsStrings = stripped.split(",");

            float[] possibleValues = new float[valuesAsStrings.length];
            for (int i = 0; i < valuesAsStrings.length; i++) {
                possibleValues[i] = Float.parseFloat(valuesAsStrings[i]);
            }

            return possibleValues[MathHelper.nextBetween(random, 0, possibleValues.length - 1)];
        }
        // endregion

        // region ------- RANDOM NUMBER --------
        // Returns a random value between two given floating point numbers separated by a comma ','.
        // Special Cases:
        // - Providing more than two values will still only use the values at indices 0 and 1 to generate the number.
        // - Providing less than two values will return the fallback.
        if (stripped.startsWith("{")) {
            stripped = stripped.replace("{", "").replace("}", "");

            String[] valuesAsStrings = stripped.split(",");

            if (valuesAsStrings.length >= 2) {
                float min = Float.parseFloat(valuesAsStrings[0]);
                float max = Float.parseFloat(valuesAsStrings[1]);
                return MathHelper.nextFloat(random, min, max);
            }

            return fallback;
        }
        // endregion

        return Float.parseFloat(stripped);
    }
}