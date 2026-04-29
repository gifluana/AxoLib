package com.lunazstudios.axolib.server;

import com.lunazstudios.axolib.AxoLib;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.util.Collections;
import java.util.HashMap;
import java.util.HexFormat;
import java.util.Map;

public class ModelRepository {
    private static final ModelRepository INSTANCE = new ModelRepository();
    private static final String MODEL_SUFFIX = ".axo.json";

    public static ModelRepository getInstance() {
        return INSTANCE;
    }

    public record ModelEntry(String id, String modelHash, byte[] modelData, String textureHash, byte[] textureData) {
    }

    private final Map<String, ModelEntry> entries = new HashMap<>();
    private Path folder;

    private ModelRepository() {
    }

    public void setFolder(Path folder) {
        this.folder = folder;
    }

    public String reload() throws IOException {
        if (folder == null) throw new IllegalStateException("Model folder not configured");
        if (!Files.exists(folder)) Files.createDirectories(folder);

        entries.clear();
        int count = 0;

        try (var stream = Files.list(folder)) {
            var jsonFiles = stream
                .filter(p -> p.getFileName().toString().endsWith(MODEL_SUFFIX))
                .toList();

            for (Path jsonFile : jsonFiles) {
                String fileName = jsonFile.getFileName().toString();
                String stem = fileName.substring(0, fileName.length() - MODEL_SUFFIX.length());
                Path pngFile = folder.resolve(stem + ".png");

                if (!Files.exists(pngFile)) {
                    AxoLib.LOGGER.warn("[AxoLib] No texture found for model '{}', skipping.", stem);
                    continue;
                }

                byte[] modelData = Files.readAllBytes(jsonFile);
                byte[] textureData = Files.readAllBytes(pngFile);
                String modelHash = sha256(modelData);
                String textureHash = sha256(textureData);

                entries.put(stem, new ModelEntry(stem, modelHash, modelData, textureHash, textureData));
                count++;
            }
        }

        return "Loaded " + count + " model(s) from " + folder;
    }

    public Map<String, String> getManifest() {
        Map<String, String> manifest = new HashMap<>();
        for (var entry : entries.values()) {
            manifest.put(entry.id(), entry.modelHash() + ":" + entry.textureHash());
        }
        return Collections.unmodifiableMap(manifest);
    }

    public ModelEntry get(String id) {
        return entries.get(id);
    }

    public Map<String, ModelEntry> getAll() {
        return Collections.unmodifiableMap(entries);
    }

    public boolean has(String id) {
        return entries.containsKey(id);
    }

    private static String sha256(byte[] data) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            return HexFormat.of().formatHex(md.digest(data));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
