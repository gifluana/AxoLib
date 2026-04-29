package com.lunazstudios.axolib.cubic.data.animation;

import com.google.gson.JsonObject;
import com.lunazstudios.axolib.math.molang.MolangParser;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class AxoAnimations {
    private final Map<String, AxoAnimation> map = new LinkedHashMap<>();

    public void fromJson(JsonObject animationsObj, MolangParser parser) {
        for (var entry : animationsObj.entrySet()) {
            AxoAnimation anim = new AxoAnimation(entry.getKey());
            anim.fromJson(entry.getValue().getAsJsonObject(), parser);
            map.put(entry.getKey(), anim);
        }
    }

    public AxoAnimation get(String id) {
        return map.get(id);
    }

    public Collection<AxoAnimation> getAll() {
        return Collections.unmodifiableCollection(map.values());
    }

    public boolean has(String id) {
        return map.containsKey(id);
    }

    public AxoAnimation getFirst() {
        return map.isEmpty() ? null : map.values().iterator().next();
    }
}
