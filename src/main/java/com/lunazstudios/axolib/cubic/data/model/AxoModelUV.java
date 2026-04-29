package com.lunazstudios.axolib.cubic.data.model;

import com.google.gson.JsonArray;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

public class AxoModelUV {
    public float x1, y1, x2, y2;
    public float rotation;

    public static AxoModelUV fromJson(JsonArray arr) {
        AxoModelUV uv = new AxoModelUV();
        uv.x1 = arr.get(0).getAsFloat();
        uv.y1 = arr.get(1).getAsFloat();
        uv.x2 = arr.get(2).getAsFloat();
        uv.y2 = arr.get(3).getAsFloat();
        if (arr.size() >= 5) uv.rotation = arr.get(4).getAsFloat();
        return uv;
    }

    public void getPoints(Vector2f p1, Vector2f p2, Vector2f p3, Vector2f p4) {
        p1.set(x1, y1);
        p2.set(x2, y1);
        p3.set(x2, y2);
        p4.set(x1, y2);

        if (rotation != 0) {
            List<Vector2f> pts = new ArrayList<>(List.of(p1, p2, p3, p4));
            int steps = (int) (rotation / 90f);
            for (int i = 0; i < steps; i++) {
                pts.add(0, pts.remove(pts.size() - 1));
            }
            p1.set(pts.get(0)); p2.set(pts.get(1)); p3.set(pts.get(2)); p4.set(pts.get(3));
        }
    }

    public AxoModelUV copy() {
        AxoModelUV u = new AxoModelUV();
        u.x1 = x1; u.y1 = y1; u.x2 = x2; u.y2 = y2; u.rotation = rotation;
        return u;
    }
}
