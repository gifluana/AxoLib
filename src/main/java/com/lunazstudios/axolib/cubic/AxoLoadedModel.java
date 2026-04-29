package com.lunazstudios.axolib.cubic;

import com.lunazstudios.axolib.cubic.data.animation.AxoAnimations;
import com.lunazstudios.axolib.cubic.data.model.AxoModel;
import com.lunazstudios.axolib.math.molang.MolangParser;

public class AxoLoadedModel {
    public final AxoModel model;
    public final AxoAnimations animations;
    public final MolangParser parser;

    public AxoLoadedModel(AxoModel model, AxoAnimations animations, MolangParser parser) {
        this.model      = model;
        this.animations = animations;
        this.parser     = parser;
    }
}
