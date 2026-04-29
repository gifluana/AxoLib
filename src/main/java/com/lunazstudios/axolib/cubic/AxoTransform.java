package com.lunazstudios.axolib.cubic;

import org.joml.Vector3f;

public class AxoTransform {
    private static final Vector3f DEFAULT_SCALE = new Vector3f(1f, 1f, 1f);

    public final Vector3f translate = new Vector3f();
    public final Vector3f rotate    = new Vector3f();
    public final Vector3f scale     = new Vector3f(DEFAULT_SCALE);
    public final Vector3f pivot     = new Vector3f();

    public void copyFrom(AxoTransform other) {
        translate.set(other.translate);
        rotate.set(other.rotate);
        scale.set(other.scale);
        pivot.set(other.pivot);
    }

    public void reset(AxoTransform initial) {
        translate.set(initial.translate);
        rotate.set(initial.rotate);
        scale.set(initial.scale);
        pivot.set(initial.pivot);
    }

    public void lerp(AxoTransform target, float t) {
        translate.lerp(target.translate, t);
        rotate.lerp(target.rotate, t);
        scale.lerp(target.scale, t);
        pivot.lerp(target.pivot, t);
    }

    public AxoTransform copy() {
        AxoTransform t = new AxoTransform();
        t.copyFrom(this);
        return t;
    }
}
