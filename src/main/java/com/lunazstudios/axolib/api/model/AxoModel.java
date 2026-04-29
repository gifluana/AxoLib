package com.lunazstudios.axolib.api.model;

import com.lunazstudios.axolib.api.animatable.AxoAnimatable;
import com.lunazstudios.axolib.client.ModelManager;
import com.lunazstudios.axolib.cubic.AxoLoadedModel;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

public abstract class AxoModel<T extends AxoAnimatable> {
    public abstract Identifier getModelResource(T animatable);

    public abstract Identifier getTextureResource(T animatable);

    public @Nullable Identifier getAnimationResource(T animatable) {
        return getModelResource(animatable);
    }

    public AxoLoadedModel getLoadedModel(T animatable) {
        return ModelManager.getInstance().getOrLoadModel(getModelResource(animatable));
    }
}
