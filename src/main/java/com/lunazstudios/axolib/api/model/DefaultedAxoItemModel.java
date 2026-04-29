package com.lunazstudios.axolib.api.model;

import com.lunazstudios.axolib.api.animatable.AxoAnimatable;
import net.minecraft.resources.Identifier;

public class DefaultedAxoItemModel<T extends AxoAnimatable> extends DefaultedAxoModel<T> {
    public DefaultedAxoItemModel(Identifier baseResource) {
        super(baseResource, "item");
    }
}
