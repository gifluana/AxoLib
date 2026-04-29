package com.lunazstudios.axolib.render;

import com.lunazstudios.axolib.api.animatable.AxoActiveAnimation;
import com.lunazstudios.axolib.cubic.AxoLoadedModel;
import com.lunazstudios.axolib.cubic.AxoModelAnimator;
import com.lunazstudios.axolib.cubic.AxoTransform;
import com.lunazstudios.axolib.cubic.MolangHelper;
import com.lunazstudios.axolib.cubic.data.animation.AxoAnimation;
import com.lunazstudios.axolib.cubic.data.model.AxoModel;

import java.util.List;
import com.lunazstudios.axolib.cubic.data.model.AxoModelCube;
import com.lunazstudios.axolib.cubic.data.model.AxoModelGroup;
import com.lunazstudios.axolib.cubic.data.model.AxoModelQuad;
import com.lunazstudios.axolib.cubic.data.model.AxoModelVertex;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.LivingEntity;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class AxoModelRenderer {
    private AxoModelRenderer() {
    }

    public static void render(AxoLoadedModel loaded,
                              String animName,
                              float animTick,
                              float partialTick,
                              LivingEntity entity,
                              Identifier texture,
                              float bodyYaw,
                              MultiBufferSource buffers,
                              PoseStack poseStack,
                              int packedLight,
                              int packedOverlay) {
        render(loaded, animName, List.of(), animTick, partialTick, entity, texture, bodyYaw,
               buffers, poseStack, packedLight, packedOverlay);
    }

    public static void render(AxoLoadedModel loaded,
                              String animName,
                              List<AxoActiveAnimation> additional,
                              float animTick,
                              float partialTick,
                              LivingEntity entity,
                              Identifier texture,
                              float bodyYaw,
                              MultiBufferSource buffers,
                              PoseStack poseStack,
                              int packedLight,
                              int packedOverlay) {
        AxoModel preparedModel = prepareModel(loaded, animName, additional, animTick, partialTick, entity);
        VertexConsumer consumer = buffers.getBuffer(RenderTypes.entityCutout(texture));

        poseStack.pushPose();
        if (bodyYaw != 0) {
            poseStack.mulPose(Axis.YP.rotationDegrees(-bodyYaw));
        }

        renderPreparedModel(preparedModel, poseStack, consumer, packedLight, packedOverlay);

        poseStack.popPose();
    }

    public static void submit(AxoLoadedModel loaded,
                              String animName,
                              float animTick,
                              float partialTick,
                              Identifier texture,
                              SubmitNodeCollector collector,
                              PoseStack poseStack,
                              int packedLight) {
        AxoModel preparedModel = prepareModel(loaded, animName, List.of(), animTick, partialTick, null);
        collector.submitCustomGeometry(
            poseStack,
            RenderTypes.entityCutout(texture),
            (pose, consumer) -> {
                PoseStack stack = new PoseStack();
                stack.last().set(pose);
                renderPreparedModel(preparedModel, stack, consumer, packedLight, OverlayTexture.NO_OVERLAY);
            }
        );
    }

    public static void submitBlock(AxoLoadedModel loaded,
                                   String animName,
                                   float animTick,
                                   float partialTick,
                                   Identifier texture,
                                   SubmitNodeCollector collector,
                                   PoseStack poseStack,
                                   int packedLight) {
        poseStack.pushPose();
        poseStack.translate(0.5f, 0f, 0.5f);
        submit(loaded, animName, animTick, partialTick, texture, collector, poseStack, packedLight);
        poseStack.popPose();
    }

    public static void renderToBuffer(AxoLoadedModel loaded,
                                      String animName,
                                      float animTick,
                                      float partialTick,
                                      LivingEntity entity,
                                      VertexConsumer consumer,
                                      PoseStack poseStack,
                                      int packedLight,
                                      int packedOverlay) {
        AxoModel preparedModel = prepareModel(loaded, animName, List.of(), animTick, partialTick, entity);
        renderPreparedModel(preparedModel, poseStack, consumer, packedLight, packedOverlay);
    }

    private static AxoModel prepareModel(AxoLoadedModel loaded,
                                         String animName,
                                         List<AxoActiveAnimation> additional,
                                         float animTick,
                                         float partialTick,
                                         LivingEntity entity) {
        AxoModel model = loaded.model.copy();
        model.resetPose();
        if (entity != null) {
            MolangHelper.setVariables(loaded.parser, entity, animTick, partialTick);
        }
        if (animName != null && loaded.animations.has(animName)) {
            AxoAnimation anim = loaded.animations.get(animName);
            AxoModelAnimator.animate(model, anim, loopedTick(anim, animTick), 1f);
        }
        for (AxoActiveAnimation layer : additional) {
            if (loaded.animations.has(layer.id())) {
                AxoAnimation anim = loaded.animations.get(layer.id());
                AxoModelAnimator.animate(model, anim, loopedTick(anim, animTick), layer.weight());
            }
        }
        return model;
    }

    private static float loopedTick(AxoAnimation anim, float animTick) {
        int len = anim.durationTicks();
        return (len > 0 && anim.loop) ? (animTick % len) : Math.min(animTick, len);
    }

    private static void renderPreparedModel(AxoModel model,
                                            PoseStack poseStack,
                                            VertexConsumer consumer,
                                            int packedLight,
                                            int packedOverlay) {
        for (AxoModelGroup group : model.rootGroups) {
            renderGroup(group, poseStack, consumer, packedLight, packedOverlay);
        }
    }

    private static void renderGroup(AxoModelGroup group, PoseStack poseStack,
                                    VertexConsumer consumer, int light, int overlay) {
        if (!group.visible) return;

        poseStack.pushPose();
        applyTransform(poseStack, group.current);

        for (AxoModelCube cube : group.cubes) {
            renderCube(cube, poseStack, consumer, light, overlay);
        }

        for (AxoModelGroup child : group.children) {
            renderGroup(child, poseStack, consumer, light, overlay);
        }

        poseStack.popPose();
    }

    private static void applyTransform(PoseStack poseStack, AxoTransform t) {
        poseStack.translate(
                -(t.translate.x - t.pivot.x) / 16f,
                (t.translate.y - t.pivot.y) / 16f,
                (t.translate.z - t.pivot.z) / 16f
        );
        float px = t.pivot.x / 16f;
        float py = t.pivot.y / 16f;
        float pz = t.pivot.z / 16f;
        poseStack.translate(px, py, pz);
        if (t.rotate.z != 0) poseStack.mulPose(Axis.ZP.rotationDegrees(t.rotate.z));
        if (t.rotate.y != 0) poseStack.mulPose(Axis.YP.rotationDegrees(t.rotate.y));
        if (t.rotate.x != 0) poseStack.mulPose(Axis.XP.rotationDegrees(t.rotate.x));
        poseStack.scale(t.scale.x, t.scale.y, t.scale.z);
        poseStack.translate(-px, -py, -pz);
    }

    private static void renderCube(AxoModelCube cube, PoseStack poseStack,
                                   VertexConsumer consumer, int light, int overlay) {
        if (cube.quads.isEmpty()) return;
        boolean hasCubeTransform = cube.rotate.x != 0 || cube.rotate.y != 0 || cube.rotate.z != 0;

        if (hasCubeTransform) {
            poseStack.pushPose();
            float px = cube.pivot.x / 16f;
            float py = cube.pivot.y / 16f;
            float pz = cube.pivot.z / 16f;
            poseStack.translate(px, py, pz);
            if (cube.rotate.z != 0) poseStack.mulPose(Axis.ZP.rotationDegrees(cube.rotate.z));
            if (cube.rotate.y != 0) poseStack.mulPose(Axis.YP.rotationDegrees(cube.rotate.y));
            if (cube.rotate.x != 0) poseStack.mulPose(Axis.XP.rotationDegrees(cube.rotate.x));
            poseStack.translate(-px, -py, -pz);
        }

        Matrix4f pose = poseStack.last().pose();
        Matrix3f normalMat = poseStack.last().normal();

        for (AxoModelQuad quad : cube.quads) {
            Vector3f n = quad.normal;
            float nx = normalMat.m00() * n.x + normalMat.m10() * n.y + normalMat.m20() * n.z;
            float ny = normalMat.m01() * n.x + normalMat.m11() * n.y + normalMat.m21() * n.z;
            float nz = normalMat.m02() * n.x + normalMat.m12() * n.y + normalMat.m22() * n.z;
            for (AxoModelVertex v : quad.vertices) {
                consumer.addVertex(pose, v.pos.x, v.pos.y, v.pos.z)
                        .setColor(1f, 1f, 1f, 1f)
                        .setUv(v.uv.x, v.uv.y)
                        .setOverlay(overlay)
                        .setLight(light)
                        .setNormal(nx, ny, nz);
            }
        }

        if (hasCubeTransform) poseStack.popPose();
    }
}
