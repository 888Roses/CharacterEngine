package dev.rosenoire.mcpp;

import net.minecraft.util.Identifier;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public record McppMethod(Identifier parent, String name, List<String> content) {
    public Identifier methodIdentifier() {
        return Utils.getMethodIdentifier(parent(), name());
    }

    public Path methodPath(Path function) {
        String functionFileName = function.toString().split("\\.")[0];
        String methodPathString = functionFileName + "__" + name() + ".mcfunction";
        return Path.of(methodPathString);
    }

    public static class Builder {
        public final List<String> content = new ArrayList<>();
        public final Identifier parent;
        public String name;

        public Builder(Identifier parent) {
            this.parent = parent;
        }

        public void clear() {
            content.clear();
            name = "";
        }

        public McppMethod build() {
            return new McppMethod(parent, name, new ArrayList<>(content));
        }
    }
}