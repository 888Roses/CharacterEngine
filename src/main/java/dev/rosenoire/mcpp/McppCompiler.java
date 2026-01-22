package dev.rosenoire.mcpp;

import net.collectively.geode.mc.util.IdentifierHelper;
import net.minecraft.util.Identifier;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class McppCompiler {
    public static void compileFunction(Path function, Path outDirectory) throws IOException {
        Identifier functionIdentifier = IdentifierHelper.parsePath(function, "mcpp/", false);

        if (functionIdentifier == null) {
            System.out.printf("Function identifier is null for function: '%s'!%n", function);
            return;
        }

        List<String> functionContent = Files.readAllLines(function);

        // If this function doesn't use MCPP, we can simply copy the file without doing anything special to it.
        if (!Utils.validateMcppFile(functionContent)) {
            Path outPath = outDirectory.resolve(functionIdentifier.getPath() + ".mcfunction");
            System.out.printf("File '%s' was detected as not using MCPP. Copying at: '%s'...%n", function, outPath);
            if (!Files.exists(outPath.getParent())) Files.createDirectory(outPath.getParent());
            Files.copy(function, outPath);
            return;
        }

        replaceImplicitMethodCalls(functionIdentifier, functionContent);

        // Writing every method files.
        for (McppMethod mcppMethod : extractMethodDefinitions(functionIdentifier, functionContent)) {
            String methodContent = String.join("\n", mcppMethod.content()).strip();
            writeFile(mcppMethod.methodIdentifier(), outDirectory, methodContent);
        }

        // Writing the main function file.
        List<String> cleanedFunctionFileContent = removeMethodsFromFunctionContent(functionContent);
        String cleanedJoinedFunctionFileContent = String.join("\n", cleanedFunctionFileContent);
        // Copyright claim
        cleanedJoinedFunctionFileContent = cleanedJoinedFunctionFileContent
                .replace("# enable mcpp", "# Compiled by CharacterEngine 2026")
                .replace("#enable mcpp", "# Compiled by CharacterEngine 2026");
        writeFile(functionIdentifier, outDirectory, cleanedJoinedFunctionFileContent);
    }

    private static void writeFile(Identifier identifier, Path outDirectory, String content) throws IOException {
        Path path = outDirectory.resolve(identifier.getPath() + ".mcfunction");
        if (!Files.exists(path.getParent())) Files.createDirectory(path.getParent());
        Files.writeString(path, content);
    }

    /// Replaces implicit `method my_method` instructions with explicit function calls to generate method files.
    /// For example, the base MCPP code:
    /// ```mcfunction
     /// method my_method
     /// ```
    /// Will be turned into:
    /// ```mcfunction
     /// function example_character:this_function__my_method
     /// ```
    /// This method changes the lines directly, meaning that nothing is returned and the provided lines list is modified
    /// directly. This also means that the provided [List] should be modifiable.
    /// @param functionContent Address of the function file containing the method calls.
    /// @param functionIdentifier The lines of that function file.
    private static void replaceImplicitMethodCalls(Identifier functionIdentifier, List<String> functionContent) {
        // TODO: More robust system required
        for (int i = 0; i < functionContent.size(); i++) {
            String line = functionContent.get(i);

            // Replacing method calls at the start of a line.
            if (line.startsWith("method ")) {
                String address = line.replaceFirst("method ", "").strip();
                address = Utils.getMethodIdentifier(functionIdentifier, address).withPrefixedPath("compiled/").toString();

                line = "function %s".formatted(address);
                functionContent.set(i, line);
                continue;
            }

            // Replacing method calls at the end of execute instructions.
            int methodIndex = line.indexOf("run method ");
            if (methodIndex > -1) {
                String subLine = line.substring(methodIndex + "run method ".length());
                String address = subLine.split(" ")[0];
                address = Utils.getMethodIdentifier(functionIdentifier, address).withPrefixedPath("compiled/").toString();

                line = line.substring(0, methodIndex) + "run function " + address;
                functionContent.set(i, line);
            }
        }
    }

    /// Gets the content of a function file after removing every function from it.
    /// @param functionContent The base function file content.
    /// @return The cleaned function file content.
    public static List<String> removeMethodsFromFunctionContent(List<String> functionContent) {
        List<String> cleaned = new ArrayList<>();

        boolean inMethod = false;
        boolean wasInMethod = false;
        for (String line : functionContent) {
            String strippedLine = line.strip();

            if (!inMethod && strippedLine.startsWith("def") && strippedLine.endsWith(":")) {
                inMethod = true;
                continue;
            }

            if (inMethod) {
                if (line.startsWith(" ")) {
                    continue;
                }

                inMethod = false;
                wasInMethod = true;
            }

            if (wasInMethod) {
                wasInMethod = false;

                if (line.isBlank()) {
                    continue;
                }
            }

            cleaned.add(line);
        }

        return cleaned;
    }

    /// Extracts every method from the function lines.
    /// @param functionIdentifier The identifier of the function file used to determine the full name of the method(s).
    /// @param functionContent The content of the function file.
    /// @return An [ArrayList] containing every extracted method.
    public static List<McppMethod> extractMethodDefinitions(Identifier functionIdentifier, List<String> functionContent) {
        List<McppMethod> methods = new ArrayList<>();
        McppMethod.Builder builder = new McppMethod.Builder(functionIdentifier);
        boolean inMethod = false;

        for (String line : functionContent) {
            String strippedLine = line.strip();

            if (!inMethod && strippedLine.startsWith("def") && strippedLine.endsWith(":")) {
                strippedLine = strippedLine.substring("def ".length(), strippedLine.length() - 1);
                builder.name = strippedLine;
                inMethod = true;
                continue;
            }

            if (inMethod) {
                if (line.startsWith(" ")) {
                    builder.content.add(strippedLine);
                    continue;
                }

                methods.add(builder.build());
                builder.clear();
                inMethod = false;
            }
        }

        if (inMethod) {
            methods.add(builder.build());
        }

        return methods;
    }
}
