package com.lunazstudios.axolib.api.model;

import com.lunazstudios.axolib.api.animatable.AxoAnimatable;
import net.minecraft.resources.Identifier;

public class DefaultedAxoModel<T extends AxoAnimatable> extends AxoModel<T> {
    private final Identifier baseResource;
    private final String assetType;

    public DefaultedAxoModel(Identifier baseResource) {
        this(baseResource, "entity");
    }

    public DefaultedAxoModel(Identifier baseResource, String assetType) {
        this.baseResource = baseResource;
        this.assetType = assetType;
    }

    @Override
    public Identifier getModelResource(T animatable) {
        return Identifier.fromNamespaceAndPath(
            baseResource.getNamespace(),
            "axolib/models/" + assetType + "/" + baseResource.getPath() + ".axo.json"
        );
    }

    @Override
    public Identifier getTextureResource(T animatable) {
        return Identifier.fromNamespaceAndPath(
            baseResource.getNamespace(),
            "textures/" + assetType + "/" + baseResource.getPath() + ".png"
        );
    }
}
