package com.lunazstudios.axolib.cubic.data.model;

import org.joml.Vector2f;
import org.joml.Vector3f;

public class AxoModelVertex {
    public final Vector3f pos = new Vector3f();
    public final Vector2f uv  = new Vector2f();

    public AxoModelVertex copy() {
        AxoModelVertex v = new AxoModelVertex();
        v.pos.set(this.pos);
        v.uv.set(this.uv);
        return v;
    }
}
