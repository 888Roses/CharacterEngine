package dev.rosenoire.character_engine.common.commands;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.rosenoire.character_engine.common.payloads.SetVelocityFromServerS2C;
import net.collectively.geode.core.math;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.Vec3ArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;

public interface SetCommand {
    String TARGET = "target";
    String VELOCITY = "velocity";
    String COPIED = "copied";

    static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, commandRegistryAccess, registrationEnvironment) -> {
            dispatcher.register(CommandManager
                    .literal("set").then(CommandManager
                            .argument(TARGET, EntityArgumentType.entity())
                            .then(CommandManager
                                    .literal("velocity")
                                    .then(CommandManager
                                            .argument(VELOCITY, Vec3ArgumentType.vec3())
                                            .executes(SetCommand::setVelocityToValue)
                                    )
                                    .then(CommandManager
                                            .literal("to").then(CommandManager
                                                    .argument(COPIED, EntityArgumentType.entity())
                                                    .executes(SetCommand::setVelocityToTarget)
                                            )
                                    )
                            )
                    )
            );
        });
    }

    static int setVelocityToValue(CommandContext<ServerCommandSource> ctx) {
        Entity targetEntity = getTargetEntity(ctx);
        if (targetEntity == null) return 0;

        Vec3d velocity = Vec3ArgumentType.getVec3(ctx, VELOCITY);
        targetEntity.setVelocity(velocity);
        // if (targetEntity instanceof PlayerEntity player) player.sendMessage(Text.literal("(Constant) Velocity: " + velocity).formatted(Formatting.YELLOW), false);

        updateVelocityFromServer(
                ctx,
                targetEntity,
                serverPlayer -> serverPlayer.distanceTo(targetEntity) < 100,
                velocity
        );

        return 1;
    }

    static int setVelocityToTarget(CommandContext<ServerCommandSource> ctx) {
        Entity targetEntity = getTargetEntity(ctx);
        if (targetEntity == null) return 0;

        Entity copiedEntity = getEntityArgument(ctx, COPIED);
        if (copiedEntity == null) return 0;

        Vec3d velocity = copiedEntity.getVelocity();
        targetEntity.setVelocity(velocity);
        // if (targetEntity instanceof PlayerEntity player) player.sendMessage(Text.literal("Velocity: " + velocity), false);

        updateVelocityFromServer(
                ctx,
                targetEntity,
                serverPlayer ->
                        math.min(
                                serverPlayer.distanceTo(targetEntity),
                                serverPlayer.distanceTo(copiedEntity)
                        ) < 100,
                velocity
        );

        return 1;
    }

    private static void updateVelocityFromServer(CommandContext<ServerCommandSource> ctx, Entity targetEntity, Predicate<ServerPlayerEntity> validate, Vec3d velocity) {
        ServerWorld serverWorld = ctx.getSource().getWorld();
        List<ServerPlayerEntity> playersInRange = serverWorld.getPlayers(validate);

        SetVelocityFromServerS2C packet = new SetVelocityFromServerS2C(velocity, targetEntity.getId());
        for (ServerPlayerEntity serverPlayerEntity : playersInRange) ServerPlayNetworking.send(serverPlayerEntity, packet);
    }

    private static @Nullable Entity getTargetEntity(CommandContext<ServerCommandSource> ctx) {
        return getEntityArgument(ctx, TARGET);
    }

    private static @Nullable Entity getEntityArgument(CommandContext<ServerCommandSource> ctx, String name) {
        try {
            return EntityArgumentType.getEntity(ctx, name);
        } catch (CommandSyntaxException syntaxException) {
            ctx.getSource().sendError(Text.literal(syntaxException.getMessage()));
            return null;
        }
    }
}
