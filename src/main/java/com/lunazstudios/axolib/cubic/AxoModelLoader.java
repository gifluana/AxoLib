package com.lunazstudios.axolib.cubic;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.lunazstudios.axolib.cubic.data.animation.AxoAnimations;
import com.lunazstudios.axolib.cubic.data.model.AxoModel;
import com.lunazstudios.axolib.math.molang.MolangParser;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class AxoModelLoader {
    private static final Gson GSON = new Gson();

    public static AxoLoadedModel load(Path file) throws Exception {
        try (var reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            return parse(GSON.fromJson(reader, JsonObject.class));
        }
    }

    public static AxoLoadedModel load(byte[] data) throws Exception {
        String json = new String(data, StandardCharsets.UTF_8);
        return parse(GSON.fromJson(json, JsonObject.class));
    }

    public static AxoLoadedModel load(InputStream in) throws Exception {
        return parse(GSON.fromJson(new InputStreamReader(in, StandardCharsets.UTF_8), JsonObject.class));
    }

    private static AxoLoadedModel parse(JsonObject root) throws Exception {
        MolangParser parser = new MolangParser();
        MolangHelper.registerVars(parser);

        AxoModel model = new AxoModel();
        if (root.has("model")) {
            model.fromJson(root.getAsJsonObject("model"));
        }

        AxoAnimations animations = new AxoAnimations();
        if (root.has("animations")) {
            animations.fromJson(root.getAsJsonObject("animations"), parser);
        }

        return new AxoLoadedModel(model, animations, parser);
    }
}
