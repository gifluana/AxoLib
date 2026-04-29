package com.lunazstudios.axolib.cubic.keyframes;

import com.lunazstudios.axolib.math.molang.expressions.MolangExpression;

public class MolangKeyframe {
    public float tick;
    public MolangExpression value;
    public String interpolation = "linear";

    public float lx = 5f, ly = 0f;
    public float rx = 5f, ry = 0f;

    public MolangKeyframe(float tick, MolangExpression value) {
        this.tick  = tick;
        this.value = value;
    }
}
