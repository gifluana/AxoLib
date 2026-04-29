package com.lunazstudios.axolib.api.model;

import com.lunazstudios.axolib.api.animatable.AxoAnimatable;
import net.minecraft.resources.Identifier;

public class SimpleAxoModel<T extends AxoAnimatable> extends AxoModel<T> {
    private final Identifier modelResource;
    private final Identifier textureResource;

    public SimpleAxoModel(Identifier modelResource, Identifier textureResource) {
        this.modelResource = modelResource;
        this.textureResource = textureResource;
    }

    @Override
    public Identifier getModelResource(T animatable) {
        return modelResource;
    }

    @Override
    public Identifier getTextureResource(T animatable) {
        return textureResource;
    }
}
