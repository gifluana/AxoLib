package com.lunazstudios.axolib.cubic.data.model;

import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class AxoModelQuad {
    public final List<AxoModelVertex> vertices = new ArrayList<>(4);
    public final Vector3f normal = new Vector3f();

    public AxoModelQuad vertex(float x, float y, float z, float u, float v) {
        AxoModelVertex vert = new AxoModelVertex();
        vert.pos.set(x, y, z);
        vert.uv.set(u, v);
        this.vertices.add(vert);
        return this;
    }

    public AxoModelQuad normal(float x, float y, float z) {
        this.normal.set(x, y, z);
        return this;
    }

    public AxoModelQuad copy() {
        AxoModelQuad q = new AxoModelQuad();
        q.normal.set(this.normal);
        for (AxoModelVertex v : this.vertices) q.vertices.add(v.copy());
        return q;
    }
}
