package com.lunazstudios.axolib.api.renderer;

import com.lunazstudios.axolib.api.animatable.AxoEntity;
import com.lunazstudios.axolib.api.model.AxoModel;
import com.lunazstudios.axolib.api.model.DefaultedAxoEntityModel;
import net.minecraft.resources.Identifier;

public class AxoEntityRenderer<T extends AxoEntity> extends AxoRenderer<T> {
    public AxoEntityRenderer(String namespace, String name) {
        super(new DefaultedAxoEntityModel<>(Identifier.fromNamespaceAndPath(namespace, name)));
    }

    public AxoEntityRenderer(Identifier modelResource, Identifier textureResource) {
        super(modelResource, textureResource);
    }

    public AxoEntityRenderer(AxoModel<T> model) {
        super(model);
    }

    public static Identifier entityModel(String namespace, String name) {
        return Identifier.fromNamespaceAndPath(namespace, "axolib/models/entity/" + name + ".axo.json");
    }

    public static Identifier entityTexture(String namespace, String name) {
        return Identifier.fromNamespaceAndPath(namespace, "textures/entity/" + name + ".png");
    }
}
