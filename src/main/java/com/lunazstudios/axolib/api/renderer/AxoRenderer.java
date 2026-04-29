package com.lunazstudios.axolib.api.renderer;

import com.lunazstudios.axolib.api.animatable.AxoActiveAnimation;
import com.lunazstudios.axolib.api.animatable.AxoAnimatable;
import com.lunazstudios.axolib.api.animatable.AxoAnimationController;
import com.lunazstudios.axolib.api.model.AxoModel;
import com.lunazstudios.axolib.api.model.SimpleAxoModel;
import com.lunazstudios.axolib.cubic.AxoLoadedModel;
import com.lunazstudios.axolib.cubic.AxoModelAnimator;
import com.lunazstudios.axolib.cubic.data.animation.AxoAnimation;
import com.lunazstudios.axolib.render.AxoModelRenderer;

import java.util.ArrayList;
import java.util.List;
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
        dispatchAnimationEvents(animatable, loaded, animation, ageInTicks);
        List<AxoActiveAnimation> additional = collectAdditionalAnimations(animatable);

        Identifier texture = getTextureResource(animatable);
        AxoModelRenderer.render(
            loaded,
            animation,
            additional,
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

    protected void dispatchAnimationEvents(T animatable, AxoLoadedModel loaded,
                                           @Nullable String animName, float ageInTicks) {
        if (animName == null) return;
        AxoAnimation anim = loaded.animations.get(animName);
        if (anim == null || anim.events.isEmpty()) return;
        for (AxoAnimationController<?> controller : animatable.getAnimatableInstanceCache().controllers()) {
            if (controller.getEventHandler() == null) continue;
            float prev = controller.getAndUpdateEventTick(ageInTicks);
            if (prev >= 0) {
                AxoModelAnimator.dispatchEvents(anim, prev, ageInTicks, controller.getEventHandler());
            }
        }
    }

    protected List<AxoActiveAnimation> collectAdditionalAnimations(T animatable) {
        List<AxoActiveAnimation> result = new ArrayList<>();
        for (AxoAnimationController<?> controller : animatable.getAnimatableInstanceCache().controllers()) {
            result.addAll(controller.getAdditionalAnimations());
        }
        return result;
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
