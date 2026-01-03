package net.collectively.geode_animations.controllers;

import com.google.gson.*;
import com.zigythebird.playeranim.animation.PlayerAnimResources;
import com.zigythebird.playeranimcore.animation.Animation;
import com.zigythebird.playeranimcore.animation.AnimationController;
import com.zigythebird.playeranimcore.animation.RawAnimation;
import dev.rosenoire.character_engine.common.CharacterEngine;
import net.collectively.geode_animations.GeodeAnimations;
import net.collectively.geode_animations.loaders.AnimationQueueResourceReloader;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.util.Locale;
import java.util.function.Function;

/// # Animation Queue
/// Animation queues are `.json` files acting as queues of instructions to represent an action involving multiple animations
/// in a row. In simpler terms, they offer a way of playing multiple animations in a row while specifying information about
/// said animations.
///
/// ```
/// {
///   "instructions": [
///     {
///       "id": "character_engine:blaster.draw",
///       "count": 4
///     },
///     {
///       "id": "character_engine:blaster.idle",
///       "loop": "hold"
///     },
///     {
///       "type": "wait",
///       "delay": 20
///     }
///   ]
/// }
/// ```
/// <sup>Very simple example of an **animation queue**.</sup>
///
/// ## Fields
///
/// #### `instructions`
/// The root animation JSON array containing the entire queue. If not included, this animation queue will not be loaded. For
/// example:
/// ```
/// {
///   "instructions": [
///   ]
/// }
/// ```
///
/// #### `type`
/// What kind of instruction this is. It accepts two values: `wait` and `animation`.
/// - `wait`: Waits a given amount of ticks before proceeding to the next instruction. Requires another key, `delay`,
/// specifying how many ticks should be waited out.
/// - `animation`: Plays the animation specified by the `id` key and modified by the different additional keys (see below).
///
/// For example:
/// ```
/// {
///   "instructions": [
///     {
///       // Waits 20 ticks before executing the next instruction.
///       "type": "wait",
///       "delay": 20
///     }
///   ]
/// }
/// ```
/// ```
/// {
///   "instructions": [
///     {
///       "type": "animation",
///       // Plays the animation with that id.
///       "id": "geode_animation:my_awesome_animation"
///     }
///   ]
/// }
/// ```
/// By default, all instructions are set to `animation`, so you may only specify the type when you want to do something other
/// than play an animation.
public record AnimationQueueData(Instruction[] instructions) {
    public static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(
                    AnimationQueueData.class,
                    (JsonDeserializer<AnimationQueueData>) (json, typeOfT, context)
                            -> createQueue(json.getAsJsonObject())
            )
            .disableHtmlEscaping()
            .create();

    public static @Nullable AnimationQueueData getQueue(Identifier identifier) {
        return AnimationQueueResourceReloader.QUEUES.getOrDefault(identifier, null);
    }

    public static boolean triggerAnimation(AnimationController animationController, Identifier identifier) {
        AnimationQueueData queueData = getQueue(identifier);

        if (queueData == null) {
            return false;
        }

        RawAnimation rawAnimation = queueData.build();
        animationController.triggerAnimation(rawAnimation);
        return true;
    }

    /// Creates a new {@link AnimationQueueData} using the provided {@link JsonObject}.
    public static AnimationQueueData createQueue(JsonObject jsonObject) {
        if (!jsonObject.has("instructions")) {
            throw new JsonSyntaxException("Invalid animation queue! Animation queue without 'instructions' member detected!");
        }

        JsonArray array = jsonObject.getAsJsonArray("instructions");
        Instruction[] queue = new Instruction[array.size()];

        for (int i = 0; i < array.size(); i++) {
            JsonObject instructionObject = array.get(i).getAsJsonObject();
            InstructionType instructionType = InstructionType.ANIMATION;

            if (instructionObject.has("type")) {
                instructionType = InstructionType.valueOf(instructionObject.get("type").getAsString());
            }

            queue[i] = instructionType.createInstruction(instructionObject);
        }

        return new AnimationQueueData(queue);
    }

    public RawAnimation build() {
        RawAnimation result = RawAnimation.begin();

        for (Instruction instruction : instructions) {
            result = instruction.execute(result);
        }

        return result;
    }

    /// Waits `delay` amount of ticks before proceeding to the next instruction in the queue.
    ///
    /// #### `delay`
    /// The number of ticks to wait before executing the next instruction in the queue. Does not do anything if the `type`
    /// isn't set to `wait`.
    public record WaitInstruction(int delay) implements Instruction {
        @Override
        public RawAnimation execute(RawAnimation rawAnimation) {
            return rawAnimation.thenWait(delay);
        }

        public static WaitInstruction create(JsonObject jsonObject) {
            if (!jsonObject.has("delay")) {
                throw new JsonSyntaxException("Cannot have instruction of type 'wait' with no 'delay' field!");
            }

            return new WaitInstruction(jsonObject.get("delay").getAsInt());
        }
    }

    /// Plays the animation with the `identifier` for `count` times in a row, using the given `loop` mode. By default, all
    /// instructions are set to `animation`, so you may only specify the type when you want to do something other than play
    /// an animation.
    ///
    /// #### `identifier`
    /// The identifier of the animation to play. Reminder: the identifier of an animation **does not correlate with the name
    /// of its file**. Instead, it is defined within the file itself. For example:
    /// ```
    /// {
    ///   "instructions": [
    ///     {
    ///       // Plays "my_awesome_animation".
    ///       "id": "geode_animations:my_awesome_animation"
    ///     }
    ///   ]
    /// }
    /// ```
    ///
    /// #### `loop`
    /// Determines how the animation should loop. Possible values:
    /// - `default`: Lets the animation decide by itself, depending on what the loop type is set to in the JSON file.
    /// - `play`: Plays the animation once and then stops.
    /// - `hold`: Plays the animation and holds on the last frame of it. This is especially useful for static pose
    /// animations.
    /// - `loop`: Plays the animation over and over again until a different instruction is played. Keep in mind that an
    /// instruction of type `animation` with a `loop` type set to `loop` will never proceed to the next instructions on its
    /// own.
    ///
    /// For example:
    /// ```
    /// {
    ///   "instructions": [
    ///     {
    ///       // Plays "my_awesome_animation" and then keeps the last frame's pose.
    ///       "id": "geode_animations:my_awesome_animation",
    ///       "loop": "hold"
    ///     }
    ///   ]
    /// }
    /// ```
    /// ```
    /// {
    ///   "instructions": [
    ///     {
    ///       // Plays "my_awesome_animation" forever.
    ///       "id": "geode_animations:my_awesome_animation",
    ///       "loop": "loop"
    ///     },
    ///     // The "sad_and_alone_animation" animation will never be played unless instructed via code or script.
    ///     {
    ///       "id": "geode_animations:sad_and_alone_animation"
    ///     }
    ///   ]
    /// }
    /// ```
    ///
    /// #### `count`
    /// How many times should the animation be played in a row before proceeding to the next instruction in the queue.
    public record AnimationInstruction(Identifier identifier, LoopType loop, int count) implements Instruction {
        @Override
        public RawAnimation execute(RawAnimation rawAnimation) {
            if (!PlayerAnimResources.hasAnimation(identifier)) {
                GeodeAnimations.LOGGER.error("Could not find animation with name '{}'!", identifier);
                return rawAnimation;
            }

            Animation animation = PlayerAnimResources.getAnimation(identifier);
            Animation.LoopType loopType = loop.getLoopType();

            for (int i = 0; i < count; i++) {
                rawAnimation.then(animation, loopType);
            }

            return rawAnimation;
        }

        public static AnimationInstruction create(JsonObject jsonObject) {
            if (!jsonObject.has("id")) {
                throw new JsonSyntaxException("Cannot have instruction of type 'animation' with no 'id' field!");
            }

            LoopType loopType = LoopType.DEFAULT;

            if (jsonObject.has("loop")) {
                loopType = LoopType.valueOf(jsonObject.get("loop").getAsString().toUpperCase(Locale.ROOT));
            }

            int count = 1;

            if (jsonObject.has("count")) {
                count = jsonObject.get("count").getAsInt();
            }

            return new AnimationInstruction(Identifier.tryParse(jsonObject.get("id").getAsString()), loopType, count);
        }

        /**
         * Determines how the animation should loop.
         */
        public enum LoopType {
            /**
             * Lets the animation decide by itself, depending on what the loop type is set to in the JSON file.
             */
            DEFAULT(Animation.LoopType.DEFAULT),
            /**
             * Plays the animation once and then stops.
             */
            PLAY(Animation.LoopType.PLAY_ONCE),
            /**
             * Plays the animation and holds on the last frame of it. This is especially useful for static pose animations.
             */
            HOLD(Animation.LoopType.HOLD_ON_LAST_FRAME),
            /**
             * Plays the animation over and over again until a different instruction is played. Keep in mind that an
             * instruction of type {@code animation} with a {@code loop} type set to {@code loop} will never proceed to the
             * next instructions on its own.
             */
            LOOP(Animation.LoopType.LOOP);

            private final Animation.LoopType loopType;

            LoopType(Animation.LoopType loopType) {
                this.loopType = loopType;
            }

            public Animation.LoopType getLoopType() {
                return loopType;
            }
        }
    }

    public enum InstructionType {
        WAIT("wait", WaitInstruction::create),
        ANIMATION("animation", AnimationInstruction::create),
        ;

        private final String name;
        private final Function<JsonObject, Instruction> factory;

        InstructionType(String name, Function<JsonObject, Instruction> factory) {
            this.name = name;
            this.factory = factory;
        }

        public String getName() {
            return name;
        }

        public Instruction createInstruction(JsonObject jsonObject) {
            return factory.apply(jsonObject);
        }
    }

    public interface Instruction {
        RawAnimation execute(RawAnimation rawAnimation);
    }
}