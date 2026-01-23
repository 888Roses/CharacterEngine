package dev.rosenoire.character_engine.common.index;

import dev.rosenoire.character_engine.common.commands.RaymarchCommand;
import dev.rosenoire.character_engine.common.commands.ShakeCommand;

public interface ModCommands {
    static void initialize() {
        ShakeCommand.register();
        RaymarchCommand.register();
    }
}
