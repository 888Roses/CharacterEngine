package dev.rosenoire.mcpp;

import java.io.IOException;
import java.nio.file.Path;

public class Mcpp {
    public static final String DIRECTORY = "C:/dev/java/CharacterEngine/src/main/resources/data/example_character/function/compiled/";
    public static final String FILE = "C:/dev/java/CharacterEngine/src/main/resources/data/example_character/function/test_function.mcfunction";

    public static void main(String[] params) throws IOException {
        Path function = Path.of(FILE);
        Path outDirectory = Path.of(DIRECTORY);
        McppCompiler.compileFunction(function, outDirectory);
    }
}