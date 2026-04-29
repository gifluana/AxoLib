package com.lunazstudios.axolib.cubic.data.animation;

import com.google.gson.JsonObject;
import com.lunazstudios.axolib.math.molang.MolangParser;

import java.util.HashMap;
import java.util.Map;

public class AxoAnimation {
    public final String id;

    public double durationSec;
    public boolean loop = true;

    public final Map<String, AxoAnimationPart> parts = new HashMap<>();

    public AxoAnimation(String id) {
        this.id = id;
    }

    public int durationTicks() {
        return (int) Math.floor(durationSec * 20);
    }

    public void fromJson(JsonObject obj, MolangParser parser) {
        if (obj.has("duration")) durationSec = obj.get("duration").getAsDouble();
        if (obj.has("animation_length")) durationSec = obj.get("animation_length").getAsDouble();
        if (obj.has("loop"))     loop = obj.get("loop").getAsBoolean();

        if (obj.has("groups")) {
            JsonObject groups = obj.getAsJsonObject("groups");
            for (var entry : groups.entrySet()) {
                AxoAnimationPart part = new AxoAnimationPart();
                part.fromJson(entry.getValue().getAsJsonObject(), parser);
                parts.put(entry.getKey(), part);
            }
        }

        if (obj.has("bones")) {
            JsonObject bones = obj.getAsJsonObject("bones");
            for (var entry : bones.entrySet()) {
                AxoAnimationPart part = new AxoAnimationPart();
                part.fromJson(entry.getValue().getAsJsonObject(), parser);
                parts.put(entry.getKey(), part);
            }
        }
    }
}
