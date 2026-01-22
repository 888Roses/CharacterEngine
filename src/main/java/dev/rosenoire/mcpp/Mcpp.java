package dev.rosenoire.mcpp;

import net.collectively.geode.core.util.FileHelper;
import net.collectively.geode.mc.util.IdentifierHelper;
import net.minecraft.util.Identifier;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Mcpp {
    public static final Path ROOT_DATA = Path.of("C:/dev/java/CharacterEngine/src/main/resources/data/");

    public static void main(String[] params) throws IOException {
        compileDatapack("example_character");
    }

    public static void compileDatapack(String datapackName) throws IOException {
        Path datapackRoot = ROOT_DATA.resolve(datapackName);
        Path mcppDirectory = datapackRoot.resolve("mcpp");
        Path compiledDirectory = datapackRoot.resolve("function");

        // Reset the compiled directory to make sure nothing is left in it.
        FileHelper.clearDirectory(compiledDirectory);
        Files.createDirectory(compiledDirectory);

        List<Path> functions = FileHelper.listFiles(mcppDirectory, x -> {
            String fileNameAsString = x.getFileName().toString();
            return fileNameAsString.contains(".") && fileNameAsString.split("\\.")[1].equals("mcfunction");
        });

        for (Path function : functions) {
            Identifier identifier = IdentifierHelper.parsePath(function, "mcpp/", false);
            McppCompiler.compileFunction(function, compiledDirectory);
            System.out.printf("- %s%n", identifier);
        }
    }
}