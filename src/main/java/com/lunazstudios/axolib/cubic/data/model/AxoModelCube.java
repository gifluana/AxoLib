package com.lunazstudios.axolib.cubic.data.model;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class AxoModelCube {
    public final Vector3f from   = new Vector3f();
    public final Vector3f size   = new Vector3f();
    public final Vector3f pivot  = new Vector3f();
    public final Vector3f rotate = new Vector3f();
    public float inflate;

    public AxoModelUV front, back, right, left, top, bottom;

    public final List<AxoModelQuad> quads = new ArrayList<>();

    public void fromJson(JsonObject obj) {
        if (obj.has("from"))   readVec3(from,   obj.getAsJsonArray("from"));
        if (obj.has("size"))   readVec3(size,   obj.getAsJsonArray("size"));
        if (obj.has("origin")) readVec3(pivot,  obj.getAsJsonArray("origin"));
        if (obj.has("rotate")) readVec3(rotate, obj.getAsJsonArray("rotate"));
        if (obj.has("offset")) inflate = obj.get("offset").getAsFloat();

        if (obj.has("uvs")) {
            JsonObject uvs = obj.getAsJsonObject("uvs");
            if (uvs.has("front"))  front  = AxoModelUV.fromJson(uvs.getAsJsonArray("front"));
            if (uvs.has("back"))   back   = AxoModelUV.fromJson(uvs.getAsJsonArray("back"));
            if (uvs.has("right"))  right  = AxoModelUV.fromJson(uvs.getAsJsonArray("right"));
            if (uvs.has("left"))   left   = AxoModelUV.fromJson(uvs.getAsJsonArray("left"));
            if (uvs.has("top"))    top    = AxoModelUV.fromJson(uvs.getAsJsonArray("top"));
            if (uvs.has("bottom")) bottom = AxoModelUV.fromJson(uvs.getAsJsonArray("bottom"));
        }
    }

    public void generateQuads(int textureW, int textureH) {
        float tw = 1f / textureW;
        float th = 1f / textureH;

        float minX = (from.x - inflate) / 16f;
        float minY = (from.y - inflate) / 16f;
        float minZ = (from.z - inflate) / 16f;
        float maxX = (from.x + size.x + inflate) / 16f;
        float maxY = (from.y + size.y + inflate) / 16f;
        float maxZ = (from.z + size.z + inflate) / 16f;

        quads.clear();

        Vector2f p1 = new Vector2f(), p2 = new Vector2f(), p3 = new Vector2f(), p4 = new Vector2f();
        if (front != null) {
            front.getPoints(p1, p2, p3, p4);
            quads.add(new AxoModelQuad()
                .vertex(maxX, minY, minZ, p4.x * tw, p4.y * th)
                .vertex(minX, minY, minZ, p3.x * tw, p3.y * th)
                .vertex(minX, maxY, minZ, p2.x * tw, p2.y * th)
                .vertex(maxX, maxY, minZ, p1.x * tw, p1.y * th)
                .normal(0, 0, -1));
        }
        if (right != null) {
            right.getPoints(p1, p2, p3, p4);
            quads.add(new AxoModelQuad()
                .vertex(maxX, minY, maxZ, p4.x * tw, p4.y * th)
                .vertex(maxX, minY, minZ, p3.x * tw, p3.y * th)
                .vertex(maxX, maxY, minZ, p2.x * tw, p2.y * th)
                .vertex(maxX, maxY, maxZ, p1.x * tw, p1.y * th)
                .normal(1, 0, 0));
        }
        if (back != null) {
            back.getPoints(p1, p2, p3, p4);
            quads.add(new AxoModelQuad()
                .vertex(minX, minY, maxZ, p4.x * tw, p4.y * th)
                .vertex(maxX, minY, maxZ, p3.x * tw, p3.y * th)
                .vertex(maxX, maxY, maxZ, p2.x * tw, p2.y * th)
                .vertex(minX, maxY, maxZ, p1.x * tw, p1.y * th)
                .normal(0, 0, 1));
        }
        if (left != null) {
            left.getPoints(p1, p2, p3, p4);
            quads.add(new AxoModelQuad()
                .vertex(minX, minY, minZ, p4.x * tw, p4.y * th)
                .vertex(minX, minY, maxZ, p3.x * tw, p3.y * th)
                .vertex(minX, maxY, maxZ, p2.x * tw, p2.y * th)
                .vertex(minX, maxY, minZ, p1.x * tw, p1.y * th)
                .normal(-1, 0, 0));
        }
        if (top != null) {
            top.getPoints(p1, p2, p3, p4);
            quads.add(new AxoModelQuad()
                .vertex(maxX, maxY, minZ, p2.x * tw, p2.y * th)
                .vertex(minX, maxY, minZ, p1.x * tw, p1.y * th)
                .vertex(minX, maxY, maxZ, p4.x * tw, p4.y * th)
                .vertex(maxX, maxY, maxZ, p3.x * tw, p3.y * th)
                .normal(0, 1, 0));
        }
        if (bottom != null) {
            bottom.getPoints(p1, p2, p3, p4);
            quads.add(new AxoModelQuad()
                .vertex(minX, minY, minZ, p4.x * tw, p4.y * th)
                .vertex(maxX, minY, minZ, p3.x * tw, p3.y * th)
                .vertex(maxX, minY, maxZ, p2.x * tw, p2.y * th)
                .vertex(minX, minY, maxZ, p1.x * tw, p1.y * th)
                .normal(0, -1, 0));
        }
    }

    public AxoModelCube copy() {
        AxoModelCube c = new AxoModelCube();
        c.from.set(from); c.size.set(size); c.pivot.set(pivot); c.rotate.set(rotate);
        c.inflate = inflate;
        if (front  != null) c.front  = front.copy();
        if (back   != null) c.back   = back.copy();
        if (right  != null) c.right  = right.copy();
        if (left   != null) c.left   = left.copy();
        if (top    != null) c.top    = top.copy();
        if (bottom != null) c.bottom = bottom.copy();
        for (AxoModelQuad q : quads) c.quads.add(q.copy());
        return c;
    }

    private static void readVec3(Vector3f v, JsonArray arr) {
        v.set(arr.get(0).getAsFloat(), arr.get(1).getAsFloat(), arr.get(2).getAsFloat());
    }
}
