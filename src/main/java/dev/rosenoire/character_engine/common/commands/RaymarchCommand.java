package dev.rosenoire.character_engine.common.commands;

import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.collectively.geode.core.types.double3;
import net.collectively.geode.mc.util.WorldUtil;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.argument.Vec3ArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.CommandOutput;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public interface RaymarchCommand {
    String START_POS = "start_position";
    String STEP_COUNT = "step_count";
    String STEP_SIZE = "step_size";
    String RADIUS = "radius";
    String RADIUS_INCREMENT = "radius_increment";

    static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, commandRegistryAccess, registrationEnvironment) -> {
            dispatcher.register(CommandManager
                    .literal("execute").then(CommandManager
                            .literal("raymarch").then(CommandManager
                                    .argument(START_POS, Vec3ArgumentType.vec3()).then(CommandManager
                                            .argument(STEP_COUNT, IntegerArgumentType.integer(1)).then(CommandManager
                                                    .argument(STEP_SIZE, DoubleArgumentType.doubleArg()).then(CommandManager
                                                            .argument(RADIUS, DoubleArgumentType.doubleArg()).then(CommandManager
                                                                    .argument(RADIUS_INCREMENT, DoubleArgumentType.doubleArg())
                                                                    // "get" should return 1 if it hit an entity and 0 otherwise.
                                                                    .then(CommandManager
                                                                            .literal("get")
                                                                            .executes(RaymarchCommand::getHasHit)
                                                                    )
                                                                    // Similar to "execute" instruction's "run", selects any hit entity and run a new command on it.
                                                                    .then(CommandManager
                                                                            .literal("run")
                                                                            .redirect(dispatcher.getRoot(), RaymarchCommand::getHitEntity)
                                                                    )
                                                            )
                                                    )
                                            )
                                    )
                            )
                    )
            );
        });
    }

    private static Entity raymarchToClosestEntity(CommandContext<ServerCommandSource> ctx) {
        ServerCommandSource source = ctx.getSource();
        Entity sourceEntity = source.getEntity();

        if (sourceEntity == null) {
            return null;
        }

        double3 startPosition = new double3(Vec3ArgumentType.getVec3(ctx, START_POS));
        int stepCount = IntegerArgumentType.getInteger(ctx, STEP_COUNT);
        double stepSize = DoubleArgumentType.getDouble(ctx, STEP_SIZE);
        double radius = DoubleArgumentType.getDouble(ctx, RADIUS);
        double radiusIncrement = DoubleArgumentType.getDouble(ctx, RADIUS_INCREMENT);

        return WorldUtil.raymarchToClosest(
                source.getWorld(),
                sourceEntity,
                startPosition,
                stepCount,
                stepSize,
                radius,
                radiusIncrement
        );
    }

    static int getHasHit(CommandContext<ServerCommandSource> ctx) {
        return raymarchToClosestEntity(ctx) == null ? 0 : 1;
    }

    private static ServerCommandSource getHitEntity(CommandContext<ServerCommandSource> ctx) {
        ServerCommandSource source = ctx.getSource();
        Entity entity = raymarchToClosestEntity(ctx);

        if (entity == null) {
            source.sendError(Text.literal("No entity found"));
            return source.withEntity(null);
        }

        return source.withEntity(entity);
    }
}
