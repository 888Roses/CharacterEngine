package dev.rosenoire.mcpp;

import net.minecraft.util.Identifier;

import java.util.List;

public interface Utils {
    String MCPP_KEYWORD = "enable mcpp";

    /**
     * Checks if the given lines start with a non-empty line containing the MCPP enabling keyword {@link #MCPP_KEYWORD}.
     * @param lines A list of {@code String} representing the content of a function file.
     * @return True if the file is valid, false otherwise.
     */
    static boolean validateMcppFile(List<String> lines) {
        for (String line : lines) {
            line = line.strip();

            if (!line.startsWith("#")) {
                return false;
            }

            if (line.substring(1).stripIndent().startsWith(MCPP_KEYWORD)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Creates a new {@link Identifier} representing a method function file.
     * @param parent The function file defining this method.
     * @param name The name of the defined method.
     * @return The created {@link Identifier}.
     */
    static Identifier getMethodIdentifier(Identifier parent, String name) {
        return parent.withSuffixedPath("__" + name);
    }
}
