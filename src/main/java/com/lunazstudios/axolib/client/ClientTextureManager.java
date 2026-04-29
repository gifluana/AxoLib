package com.lunazstudios.axolib.client;

import com.lunazstudios.axolib.AxoLib;
import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.Identifier;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ClientTextureManager {
    private static final ClientTextureManager INSTANCE = new ClientTextureManager();

    public static ClientTextureManager getInstance() {
        return INSTANCE;
    }

    private final Map<String, Identifier> cache = new HashMap<>();

    private ClientTextureManager() {
    }

    public Identifier register(String modelId, String hash, byte[] pngBytes) {
        String key = modelId + "/" + hash;
        if (cache.containsKey(key)) return cache.get(key);

        String textureId = "dynamic/" + modelId.toLowerCase().replace(' ', '_') + "_" + hash.substring(0, 8);
        Identifier location = Identifier.fromNamespaceAndPath(AxoLib.MODID, textureId);
        Minecraft mc = Minecraft.getInstance();
        mc.execute(() -> {
            try {
                NativeImage img = NativeImage.read(new ByteArrayInputStream(pngBytes));
                mc.getTextureManager().register(location, new DynamicTexture(() -> "AxoLib/" + modelId, img));
                cache.put(key, location);
            } catch (IOException e) {
                AxoLib.LOGGER.error("[AxoLib] Failed to load texture for {}: {}", modelId, e.getMessage());
            }
        });
        cache.put(key, location);
        return location;
    }

    public Identifier get(String modelId, String hash) {
        return cache.get(modelId + "/" + hash);
    }

    public boolean has(String modelId, String hash) {
        return cache.containsKey(modelId + "/" + hash);
    }

    public void unregister(String modelId, String hash) {
        String key = modelId + "/" + hash;
        Identifier loc = cache.remove(key);
        if (loc != null) {
            Minecraft.getInstance().execute(() -> Minecraft.getInstance().getTextureManager().release(loc));
        }
    }

    public void clear() {
        for (Identifier loc : cache.values()) {
            Minecraft.getInstance().getTextureManager().release(loc);
        }
        cache.clear();
    }
}
