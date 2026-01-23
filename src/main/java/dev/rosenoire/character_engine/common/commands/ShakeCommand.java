package dev.rosenoire.character_engine.common.commands;

import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.LongArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import dev.rosenoire.character_engine.common.CharacterEngine;
import net.collectively.geode.cardinal_components_api._internal.GeodeEntityComponentIndex;
import net.collectively.geode.cardinal_components_api.player.PlayerCameraShakeComponent;
import net.collectively.geode.cardinal_components_api.player.Shake;
import net.collectively.geode.cardinal_components_api.player.ShakeSettings;
import net.collectively.geode.core.StandardEasingFunction;
import net.collectively.geode.core.math;
import net.collectively.geode.mc.util.GeodeText;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.CommandSource;
import net.minecraft.command.suggestion.SuggestionProviders;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import java.util.Arrays;

public interface ShakeCommand {
    String CNG_CAMERA_SHAKE_INTENSITY = "intensity";
    String CNG_CAMERA_SHAKE_DURATION = "duration";
    String CNG_CAMERA_SHAKE_EASING_FUNCTION = "easing_function";

    SuggestionProvider<CommandSource> EASING_FUNCTION_PROVIDER = SuggestionProviders.register(
            CharacterEngine.id("easing_function"),
            (context, builder) ->
                    CommandSource.suggestMatching(Arrays.stream(StandardEasingFunction.values()).map(StandardEasingFunction::getName), builder)
    );

    static void register() {
        CommandRegistrationCallback.EVENT.register((commandDispatcher, commandRegistryAccess, registrationEnvironment) -> commandDispatcher.register(CommandManager
                        .literal("shake")
                        .then(CommandManager.literal("stop").executes(ShakeCommand::stopCameraShake))
                        .then(CommandManager.literal("add").then(CommandManager
                                        .argument(CNG_CAMERA_SHAKE_INTENSITY, DoubleArgumentType.doubleArg(0)).then(CommandManager
                                                .argument(CNG_CAMERA_SHAKE_DURATION, LongArgumentType.longArg(1))
                                                .executes(ctx -> {
                                                    double intensity = DoubleArgumentType.getDouble(ctx, CNG_CAMERA_SHAKE_INTENSITY);
                                                    long duration = LongArgumentType.getLong(ctx, CNG_CAMERA_SHAKE_DURATION);
                                                    return cameraShake(ctx, intensity, duration, "linear");
                                                }).then(CommandManager
                                                        .argument(CNG_CAMERA_SHAKE_EASING_FUNCTION, StringArgumentType.word())
                                                        .suggests(SuggestionProviders.cast(EASING_FUNCTION_PROVIDER))
                                                        .executes(ctx -> {
                                                            double intensity = DoubleArgumentType.getDouble(ctx, CNG_CAMERA_SHAKE_INTENSITY);
                                                            long duration = LongArgumentType.getLong(ctx, CNG_CAMERA_SHAKE_DURATION);
                                                            String easingFunctionName = StringArgumentType.getString(ctx, CNG_CAMERA_SHAKE_EASING_FUNCTION);
                                                            return cameraShake(ctx, intensity, duration, easingFunctionName);
                                                        })
                                                )
                                        )
                                )
                        )
                        .then(CommandManager.literal("get")
                                .then(CommandManager.literal("intensity").then(CommandManager.argument("scale", DoubleArgumentType.doubleArg(1)).executes(ShakeCommand::getIntensity)))
                                .then(CommandManager.literal("duration").executes(ShakeCommand::getDuration))
                                .then(CommandManager.literal("is_shaking").executes(ShakeCommand::getIsShaking))
                                .then(CommandManager.literal("shake_vector")
                                        .then(CommandManager.literal("x").then(CommandManager.argument("scale", DoubleArgumentType.doubleArg(1)).executes(ShakeCommand::getShakeVectorX)))
                                        .then(CommandManager.literal("y").then(CommandManager.argument("scale", DoubleArgumentType.doubleArg(1)).executes(ShakeCommand::getShakeVectorY)))
                                )
                        )
                )
        );
    }

    static int getShakeVectorX(CommandContext<ServerCommandSource> ctx) {
        ServerCommandSource source = ctx.getSource();
        if (source == null) return 0;

        if (source.getEntity() instanceof PlayerEntity player) {
            PlayerCameraShakeComponent cameraShakeComponent = player.getComponent(GeodeEntityComponentIndex.PLAYER_CAMERA_SHAKE);
            double scale = DoubleArgumentType.getDouble(ctx, "scale");
            int shakeVector = (int) math.round(cameraShakeComponent.getCurrentShakeVector().x() * scale);

            if (CharacterEngine.isVerbose) {
                source.sendMessage(new GeodeText()
                        .literal("Camera shake shake vector x for player ")
                        .literal("\"" + player.getStringifiedName() + "\"").withColor(0x55ff55)
                        .literal(" is equal to ")
                        .literal(shakeVector).withColor(0xff55ff)
                        .build()
                );
            }

            return shakeVector;
        }

        source.sendError(Text.literal("Cannot get shake of non-player or null entities!"));
        return 0;
    }

    static int getShakeVectorY(CommandContext<ServerCommandSource> ctx) {
        ServerCommandSource source = ctx.getSource();
        if (source == null) return 0;

        if (source.getEntity() instanceof PlayerEntity player) {
            PlayerCameraShakeComponent cameraShakeComponent = player.getComponent(GeodeEntityComponentIndex.PLAYER_CAMERA_SHAKE);
            double scale = DoubleArgumentType.getDouble(ctx, "scale");
            int shakeVector = (int) math.round(cameraShakeComponent.getCurrentShakeVector().y() * scale);

            if (CharacterEngine.isVerbose) {
                source.sendMessage(new GeodeText()
                        .literal("Camera shake shake vector y for player ")
                        .literal("\"" + player.getStringifiedName() + "\"").withColor(0x55ff55)
                        .literal(" is equal to ")
                        .literal(shakeVector).withColor(0xff55ff)
                        .build()
                );
            }

            return shakeVector;
        }

        source.sendError(Text.literal("Cannot get shake of non-player or null entities!"));
        return 0;
    }

    static int getIntensity(CommandContext<ServerCommandSource> ctx) {
        ServerCommandSource source = ctx.getSource();
        if (source == null) return 0;

        if (source.getEntity() instanceof PlayerEntity player) {
            PlayerCameraShakeComponent cameraShakeComponent = player.getComponent(GeodeEntityComponentIndex.PLAYER_CAMERA_SHAKE);
            ShakeSettings settings = cameraShakeComponent.getCurrentShake();
            double scale = DoubleArgumentType.getDouble(ctx, "scale");
            int intensity = settings == null ? 0 : (int) math.round(settings.intensity() * scale);

            if (CharacterEngine.isVerbose) {
                source.sendMessage(new GeodeText()
                        .literal("Camera shake intensity for player ")
                        .literal("\"" + player.getStringifiedName() + "\"").withColor(0x55ff55)
                        .literal(" is equal to ")
                        .literal(intensity).withColor(0xff55ff)
                        .build()
                );
            }

            return intensity;
        }

        source.sendError(Text.literal("Cannot get shake of non-player or null entities!"));
        return 0;
    }

    static int getDuration(CommandContext<ServerCommandSource> ctx) {
        ServerCommandSource source = ctx.getSource();
        if (source == null) return 0;

        if (source.getEntity() instanceof PlayerEntity player) {
            PlayerCameraShakeComponent cameraShakeComponent = player.getComponent(GeodeEntityComponentIndex.PLAYER_CAMERA_SHAKE);
            ShakeSettings settings = cameraShakeComponent.getCurrentShake();
            int duration = 0;
            if (settings != null) {
                duration = (int) (cameraShakeComponent.getCurrentShakeStartTime() + settings.duration() - source.getWorld().getTime());
            }

            if (CharacterEngine.isVerbose) {
                source.sendMessage(new GeodeText()
                        .literal("Camera shake duration for player ")
                        .literal("\"" + player.getStringifiedName() + "\"").withColor(0x55ff55)
                        .literal(" is equal to ")
                        .literal(duration).withColor(0xff55ff)
                        .build()
                );
            }

            return duration;
        }

        source.sendError(Text.literal("Cannot get shake of non-player or null entities!"));
        return 0;
    }

    static int getIsShaking(CommandContext<ServerCommandSource> ctx) {
        ServerCommandSource source = ctx.getSource();
        if (source == null) return 0;

        if (source.getEntity() instanceof PlayerEntity player) {
            PlayerCameraShakeComponent cameraShakeComponent = player.getComponent(GeodeEntityComponentIndex.PLAYER_CAMERA_SHAKE);
            int isShaking = cameraShakeComponent.isShaking() ? 1 : 0;

            if (CharacterEngine.isVerbose) {
                source.sendMessage(new GeodeText()
                        .literal("Player ")
                        .literal("\"" + player.getStringifiedName() + "\"").withColor(0x55ff55)
                        .literal(" is shaking is equal to ")
                        .literal(isShaking).withColor(0x5555ff)
                        .build()
                );
            }

            return isShaking;
        }

        source.sendError(Text.literal("Cannot get shake of non-player or null entities!"));
        return 0;
    }

    static int stopCameraShake(CommandContext<ServerCommandSource> ctx) {
        ServerCommandSource source = ctx.getSource();
        if (source == null) {
            return 0;
        }

        if (source.getEntity() instanceof PlayerEntity player) {
            PlayerCameraShakeComponent cameraShakeComponent = player.getComponent(GeodeEntityComponentIndex.PLAYER_CAMERA_SHAKE);

            if (cameraShakeComponent.isShaking()) {
                cameraShakeComponent.shake(new ShakeSettings(0, 0, StandardEasingFunction.LINEAR));

                if (CharacterEngine.isVerbose) {
                    source.sendMessage(new GeodeText()
                            .literal("Stopped camera shake for player ")
                            .literal("\"" + player.getStringifiedName() + "\"").withColor(0x55ff55)
                            .build()
                    );
                }

                return 1;
            }

            return 0;
        }

        source.sendError(Text.literal("Cannot stop shake for non-player or null entities!"));
        return 0;
    }

    static int cameraShake(CommandContext<ServerCommandSource> ctx, double intensity, long duration, String easingFunctionName) {
        ServerCommandSource source = ctx.getSource();
        if (source == null) {
            return 0;
        }

        if (source.getEntity() instanceof PlayerEntity player) {
            StandardEasingFunction easingFunction = StandardEasingFunction.getFromName(easingFunctionName, null);

            if (easingFunction == null) {
                easingFunction = StandardEasingFunction.LINEAR;

                if (CharacterEngine.isVerbose) {
                    source.sendMessage(Text.literal("\"" + easingFunctionName + "\" is not a recognized easing function!").withColor(0xffa755));
                }

                easingFunctionName = "linear";
            }

            ShakeSettings settings = new ShakeSettings(intensity, duration, easingFunction);
            Shake.shake(player, settings);

            if (CharacterEngine.isVerbose) {
                source.sendMessage(new GeodeText()
                        .literal("Shook player ")
                        .literal("\"" + player.getStringifiedName() + "\"").withColor(0x55ff55)
                        .literal(" with intensity ")
                        .literal(math.round(intensity * 100d) / 100d).withColor(0xff55ff)
                        .literal(" duration ")
                        .literal(duration).withColor(0xff55ff)
                        .literal(" and easing function ")
                        .literal("\"" + easingFunctionName + "\"").withColor(0x55ff55)
                        .build()
                );
            }

            return 1;
        }

        source.sendError(Text.literal("Cannot shake non-player or null entities!"));
        return 0;
    }
}
