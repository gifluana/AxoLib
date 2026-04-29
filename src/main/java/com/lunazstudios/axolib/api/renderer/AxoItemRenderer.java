package com.lunazstudios.axolib.api.renderer;

import com.lunazstudios.axolib.api.animatable.AxoItem;
import com.lunazstudios.axolib.api.model.AxoModel;
import com.lunazstudios.axolib.api.model.DefaultedAxoItemModel;
import net.minecraft.resources.Identifier;

public class AxoItemRenderer<T extends AxoItem> extends AxoRenderer<T> {
    public AxoItemRenderer(String namespace, String name) {
        super(new DefaultedAxoItemModel<>(Identifier.fromNamespaceAndPath(namespace, name)));
    }

    public AxoItemRenderer(Identifier modelResource, Identifier textureResource) {
        super(modelResource, textureResource);
    }

    public AxoItemRenderer(AxoModel<T> model) {
        super(model);
    }

    public static Identifier itemModel(String namespace, String name) {
        return Identifier.fromNamespaceAndPath(namespace, "axolib/models/item/" + name + ".axo.json");
    }

    public static Identifier itemTexture(String namespace, String name) {
        return Identifier.fromNamespaceAndPath(namespace, "textures/item/" + name + ".png");
    }
}
