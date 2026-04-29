package com.lunazstudios.axolib.cubic.data.model;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.lunazstudios.axolib.cubic.AxoTransform;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class AxoModelGroup {
    public final String id;
    public AxoModel owner;
    public AxoModelGroup parent;
    public int index = -1;
    public boolean visible = true;

    public final List<AxoModelGroup> children = new ArrayList<>();
    public final List<AxoModelCube> cubes     = new ArrayList<>();

    public final AxoTransform initial = new AxoTransform();
    public final AxoTransform current = new AxoTransform();

    public AxoModelGroup(String id) {
        this.id = id;
    }

    public void resetPose() {
        current.copyFrom(initial);
    }

    public void fromJson(JsonObject obj) {
        if (obj.has("origin")) readVec3(initial.pivot, obj.getAsJsonArray("origin"));
        if (obj.has("rotate")) readVec3(initial.rotate, obj.getAsJsonArray("rotate"));
        initial.translate.set(initial.pivot);

        if (obj.has("cubes")) {
            for (var elem : obj.getAsJsonArray("cubes")) {
                AxoModelCube cube = new AxoModelCube();
                cube.fromJson(elem.getAsJsonObject());
                cubes.add(cube);
            }
        }

        resetPose();
    }

    public void generateQuads(int tw, int th) {
        for (AxoModelCube cube : cubes) cube.generateQuads(tw, th);
    }

    public AxoModelGroup copy(AxoModel newOwner, AxoModelGroup newParent) {
        AxoModelGroup g = new AxoModelGroup(id);
        g.owner = newOwner;
        g.parent = newParent;
        g.index = index;
        g.visible = visible;
        g.initial.copyFrom(initial);
        g.current.copyFrom(current);
        for (AxoModelCube c : cubes) g.cubes.add(c.copy());
        for (AxoModelGroup child : children) g.children.add(child.copy(newOwner, g));
        return g;
    }

    private static void readVec3(Vector3f v, JsonArray arr) {
        v.set(arr.get(0).getAsFloat(), arr.get(1).getAsFloat(), arr.get(2).getAsFloat());
    }
}
