package com.lunazstudios.axolib.api.model;

import com.lunazstudios.axolib.api.animatable.AxoAnimatable;
import net.minecraft.resources.Identifier;

public class DefaultedAxoEntityModel<T extends AxoAnimatable> extends DefaultedAxoModel<T> {
    public DefaultedAxoEntityModel(Identifier baseResource) {
        super(baseResource, "entity");
    }
}
