package com.lunazstudios.axolib.cubic.data.model;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AxoModel {
    public int textureWidth  = 64;
    public int textureHeight = 64;

    public final List<AxoModelGroup> rootGroups = new ArrayList<>();

    private final Map<String, AxoModelGroup> groupsById = new HashMap<>();
    private final List<AxoModelGroup> orderedGroups = new ArrayList<>();

    public void initialize() {
        groupsById.clear();
        orderedGroups.clear();
        int index = 0;
        for (AxoModelGroup g : rootGroups) {
            index = fillGroups(g, null, index);
        }
    }

    private int fillGroups(AxoModelGroup g, AxoModelGroup parent, int index) {
        g.parent = parent;
        g.owner  = this;
        g.index  = index++;
        groupsById.put(g.id, g);
        orderedGroups.add(g);
        for (AxoModelGroup child : g.children) {
            index = fillGroups(child, g, index);
        }
        return index;
    }

    public AxoModelGroup getGroup(String id) {
        return groupsById.get(id);
    }

    public Collection<String> getAllGroupIds() {
        return groupsById.keySet();
    }

    public List<AxoModelGroup> getOrderedGroups() {
        return orderedGroups;
    }

    public void resetPose() {
        for (AxoModelGroup g : orderedGroups) g.resetPose();
    }

    public void fromJson(JsonObject modelObj) {
        if (modelObj.has("texture")) {
            var tex = modelObj.getAsJsonArray("texture");
            textureWidth  = tex.get(0).getAsInt();
            textureHeight = tex.get(1).getAsInt();
        }

        if (!modelObj.has("groups")) return;

        JsonObject groupsJson = modelObj.getAsJsonObject("groups");
        Map<String, AxoModelGroup> flat = new HashMap<>();
        Map<String, String> parentMap  = new HashMap<>();

        for (var entry : groupsJson.entrySet()) {
            String id = entry.getKey();
            JsonObject gObj = entry.getValue().getAsJsonObject();
            AxoModelGroup g = new AxoModelGroup(id);
            g.fromJson(gObj);
            g.generateQuads(textureWidth, textureHeight);
            flat.put(id, g);
            if (gObj.has("parent")) {
                parentMap.put(id, gObj.get("parent").getAsString());
            }
        }
        for (var entry : flat.entrySet()) {
            String id  = entry.getKey();
            String pid = parentMap.get(id);
            AxoModelGroup g = entry.getValue();
            if (pid == null) {
                rootGroups.add(g);
            } else {
                AxoModelGroup parentGroup = flat.get(pid);
                if (parentGroup != null) parentGroup.children.add(g);
                else rootGroups.add(g);
            }
        }

        initialize();
    }

    public AxoModel copy() {
        AxoModel m = new AxoModel();
        m.textureWidth  = textureWidth;
        m.textureHeight = textureHeight;
        for (AxoModelGroup g : rootGroups) m.rootGroups.add(g.copy(m, null));
        m.initialize();
        return m;
    }
}
