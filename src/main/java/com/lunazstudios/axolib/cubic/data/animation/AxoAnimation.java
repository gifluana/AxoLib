package com.lunazstudios.axolib.cubic.data.animation;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.lunazstudios.axolib.math.molang.MolangParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AxoAnimation {
    public final String id;

    public double durationSec;
    public boolean loop = true;

    public final Map<String, AxoAnimationPart> parts = new HashMap<>();
    public final List<AxoAnimationEvent> events = new ArrayList<>();

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

        if (obj.has("events")) {
            for (JsonElement el : obj.getAsJsonArray("events")) {
                if (!el.isJsonObject()) continue;
                JsonObject ev = el.getAsJsonObject();
                if (!ev.has("time") || !ev.has("type")) continue;
                float tick = ev.get("time").getAsFloat() * 20f;
                switch (ev.get("type").getAsString()) {
                    case "sound" -> {
                        if (ev.has("sound"))
                            events.add(new AxoAnimationEvent.Sound(tick, ev.get("sound").getAsString()));
                    }
                    case "particle" -> {
                        if (ev.has("particle")) {
                            String bone = ev.has("bone") ? ev.get("bone").getAsString() : null;
                            float ox = 0, oy = 0, oz = 0;
                            if (ev.has("offset")) {
                                JsonArray off = ev.getAsJsonArray("offset");
                                ox = off.get(0).getAsFloat();
                                oy = off.get(1).getAsFloat();
                                oz = off.get(2).getAsFloat();
                            }
                            events.add(new AxoAnimationEvent.Particle(tick, ev.get("particle").getAsString(), bone, ox, oy, oz));
                        }
                    }
                    case "custom" -> {
                        if (ev.has("event")) {
                            String data = ev.has("data") ? ev.get("data").getAsString() : "";
                            events.add(new AxoAnimationEvent.Custom(tick, ev.get("event").getAsString(), data));
                        }
                    }
                }
            }
        }
    }
}
