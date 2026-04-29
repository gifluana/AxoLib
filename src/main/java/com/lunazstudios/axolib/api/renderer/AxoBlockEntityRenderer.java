package com.lunazstudios.axolib.api.renderer;

import com.lunazstudios.axolib.api.AxoLibUtil;
import com.lunazstudios.axolib.api.animatable.AxoBlockEntity;
import com.lunazstudios.axolib.api.animatable.AxoResolvedAnimation;
import com.lunazstudios.axolib.api.model.AxoModel;
import com.lunazstudios.axolib.api.model.DefaultedAxoBlockModel;
import com.lunazstudios.axolib.api.model.SimpleAxoModel;
import com.lunazstudios.axolib.client.ModelManager;
import com.lunazstudios.axolib.cubic.AxoLoadedModel;
import com.lunazstudios.axolib.render.AxoModelRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;

public class AxoBlockEntityRenderer<T extends BlockEntity & AxoBlockEntity> implements BlockEntityRenderer<T, AxoBlockEntityRenderer.State> {
    protected final AxoModel<T> model;
    protected final String fallbackAnimation;

    public AxoBlockEntityRenderer(BlockEntityRendererProvider.Context context, String namespace, String name) {
        this(context, namespace, name, "idle");
    }

    public AxoBlockEntityRenderer(BlockEntityRendererProvider.Context context, String namespace, String name, String fallbackAnimation) {
        this(context, new DefaultedAxoBlockModel<>(Identifier.fromNamespaceAndPath(namespace, name)), fallbackAnimation);
    }

    public AxoBlockEntityRenderer(BlockEntityRendererProvider.Context context, Identifier modelResource, Identifier textureResource) {
        this(context, modelResource, textureResource, "idle");
    }

    public AxoBlockEntityRenderer(BlockEntityRendererProvider.Context context, Identifier modelResource, Identifier textureResource, String fallbackAnimation) {
        this(context, new SimpleAxoModel<>(modelResource, textureResource), fallbackAnimation);
    }

    public AxoBlockEntityRenderer(BlockEntityRendererProvider.Context context, AxoModel<T> model) {
        this(context, model, "idle");
    }

    public AxoBlockEntityRenderer(BlockEntityRendererProvider.Context context, AxoModel<T> model, String fallbackAnimation) {
        this.model = model;
        this.fallbackAnimation = fallbackAnimation;
    }

    @Override
    public State createRenderState() {
        return new State();
    }

    @Override
    public void extractRenderState(T blockEntity, State state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.CrumblingOverlay breakProgress) {
        BlockEntityRenderState.extractBase(blockEntity, state, breakProgress);

        AxoLoadedModel loaded = model.getLoadedModel(blockEntity);
        long gameTime = blockEntity.getLevel() == null ? 0 : blockEntity.getLevel().getGameTime();
        AxoResolvedAnimation animation = AxoLibUtil.resolveAnimation(blockEntity, loaded, fallbackAnimation, gameTime, partialTicks);

        state.modelResource = getModelResource(blockEntity);
        state.textureResource = getTextureResource(blockEntity);
        state.animation = animation.name();
        state.animationTick = animation.tick();
        state.partialTick = partialTicks;
    }

    @Override
    public void submit(State state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        if (state.modelResource == null || state.textureResource == null) return;

        AxoLoadedModel loaded = ModelManager.getInstance().getOrLoadModel(state.modelResource);
        if (loaded == null) return;

        poseStack.pushPose();
        AxoModelRenderer.submitBlock(
            loaded,
            state.animation,
            state.animationTick,
            state.partialTick,
            state.textureResource,
            submitNodeCollector,
            poseStack,
            state.lightCoords
        );
        poseStack.popPose();
    }

    protected Identifier getModelResource(T animatable) {
        return model.getModelResource(animatable);
    }

    protected Identifier getTextureResource(T animatable) {
        return model.getTextureResource(animatable);
    }

    public static Identifier blockModel(String namespace, String name) {
        return Identifier.fromNamespaceAndPath(namespace, "axolib/models/block/" + name + ".axo.json");
    }

    public static Identifier blockTexture(String namespace, String name) {
        return Identifier.fromNamespaceAndPath(namespace, "textures/block/" + name + ".png");
    }

    public static class State extends BlockEntityRenderState {
        public Identifier modelResource;
        public Identifier textureResource;
        public String animation;
        public float animationTick;
        public float partialTick;
    }
}
