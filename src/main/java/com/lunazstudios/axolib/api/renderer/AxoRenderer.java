package com.lunazstudios.axolib.api.renderer;

import com.lunazstudios.axolib.api.animatable.AxoAnimatable;
import com.lunazstudios.axolib.api.animatable.AxoAnimationController;
import com.lunazstudios.axolib.api.model.AxoModel;
import com.lunazstudios.axolib.api.model.SimpleAxoModel;
import com.lunazstudios.axolib.cubic.AxoLoadedModel;
import com.lunazstudios.axolib.render.AxoModelRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.LivingEntity;
import org.jspecify.annotations.Nullable;

public class AxoRenderer<T extends AxoAnimatable> {
    protected final AxoModel<T> model;

    public AxoRenderer(Identifier modelResource, Identifier textureResource) {
        this(new SimpleAxoModel<>(modelResource, textureResource));
    }

    public AxoRenderer(AxoModel<T> model) {
        this.model = model;
    }

    public AxoModel<T> getAxoModel() {
        return model;
    }

    public void render(T animatable,
                       float ageInTicks,
                       float partialTick,
                       @Nullable LivingEntity entity,
                       PoseStack poseStack,
                       MultiBufferSource buffers,
                       int packedLight,
                       int packedOverlay) {
        AxoLoadedModel loaded = model.getLoadedModel(animatable);
        if (loaded == null) return;

        String animation = resolveAnimation(animatable, ageInTicks, partialTick);
        Identifier texture = getTextureResource(animatable);
        AxoModelRenderer.render(
            loaded,
            animation,
            ageInTicks,
            partialTick,
            entity,
            texture,
            0f,
            buffers,
            poseStack,
            packedLight,
            packedOverlay
        );
    }

    protected Identifier getModelResource(T animatable) {
        return model.getModelResource(animatable);
    }

    protected Identifier getTextureResource(T animatable) {
        return model.getTextureResource(animatable);
    }

    protected String resolveAnimation(T animatable, float ageInTicks, float partialTick) {
        animatable.getAnimatableInstanceCache().initialize();
        String first = null;
        for (AxoAnimationController<?> controller : animatable.getAnimatableInstanceCache().controllers()) {
            @SuppressWarnings("unchecked")
            AxoAnimationController<T> typed = (AxoAnimationController<T>) controller;
            String animation = typed.tick(ageInTicks, partialTick);
            if (first == null && animation != null && !animation.isBlank()) {
                first = animation;
            }
        }
        return first != null ? first : animatable.getAnimatableInstanceCache().firstAnimation();
    }
}
