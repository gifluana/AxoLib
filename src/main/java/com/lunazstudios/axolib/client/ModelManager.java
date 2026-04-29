package com.lunazstudios.axolib.client;

import com.lunazstudios.axolib.AxoLib;
import com.lunazstudios.axolib.cubic.AxoLoadedModel;
import com.lunazstudios.axolib.cubic.AxoModelLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.Resource;

import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ModelManager {
    private static final ModelManager INSTANCE = new ModelManager();

    public static ModelManager getInstance() {
        return INSTANCE;
    }

    private final Map<String, AxoLoadedModel> models = new HashMap<>();
    private final Map<String, String> hashes = new HashMap<>();
    private final Map<String, Identifier> textures = new HashMap<>();
    private final Map<Identifier, AxoLoadedModel> resourceModels = new HashMap<>();

    private ModelManager() {
    }

    public void registerModel(String modelId, String hash, byte[] jsonBytes) {
        try {
            AxoLoadedModel loaded = AxoModelLoader.load(jsonBytes);
            models.put(modelId, loaded);
            hashes.put(modelId, hash);
        } catch (Exception e) {
            AxoLib.LOGGER.error("[AxoLib] Failed to parse model '{}': {}", modelId, e.getMessage());
        }
    }

    public AxoLoadedModel getOrLoadModel(Identifier modelResource) {
        AxoLoadedModel loaded = resourceModels.get(modelResource);
        if (loaded != null) return loaded;

        try {
            Resource resource = Minecraft.getInstance()
                .getResourceManager()
                .getResource(modelResource)
                .orElseThrow(() -> new IllegalArgumentException("Missing Axo model resource: " + modelResource));

            try (InputStream in = resource.open()) {
                loaded = AxoModelLoader.load(in);
                resourceModels.put(modelResource, loaded);
                return loaded;
            }
        } catch (Exception e) {
            AxoLib.LOGGER.error("[AxoLib] Failed to load model resource '{}': {}", modelResource, e.getMessage());
            return null;
        }
    }

    public void registerModelResource(Identifier modelResource, AxoLoadedModel model) {
        resourceModels.put(modelResource, model);
    }

    public void registerTexture(String modelId, String textureHash, byte[] pngBytes) {
        Identifier loc = ClientTextureManager.getInstance().register(modelId, textureHash, pngBytes);
        textures.put(modelId, loc);
    }

    public AxoLoadedModel getModel(String modelId) {
        return models.get(modelId);
    }

    public Identifier getTexture(String modelId) {
        return textures.get(modelId);
    }

    public String getHash(String modelId) {
        return hashes.get(modelId);
    }

    public boolean hasModel(String modelId) {
        return models.containsKey(modelId);
    }

    public boolean hasTexture(String modelId) {
        return textures.containsKey(modelId);
    }

    public boolean isReady(String modelId) {
        return hasModel(modelId) && hasTexture(modelId);
    }

    public Collection<String> getAllModelIds() {
        return Collections.unmodifiableSet(models.keySet());
    }

    public void clear() {
        models.clear();
        hashes.clear();
        textures.clear();
        resourceModels.clear();
    }
}
