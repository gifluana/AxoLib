package com.lunazstudios.axolib.api.model;

import com.lunazstudios.axolib.api.animatable.AxoAnimatable;
import net.minecraft.resources.Identifier;

public class DefaultedAxoBlockModel<T extends AxoAnimatable> extends DefaultedAxoModel<T> {
    public DefaultedAxoBlockModel(Identifier baseResource) {
        super(baseResource, "block");
    }
}
