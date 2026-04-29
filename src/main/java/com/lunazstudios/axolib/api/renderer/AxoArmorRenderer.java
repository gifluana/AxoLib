package com.lunazstudios.axolib.api.renderer;

import com.lunazstudios.axolib.api.animatable.AxoArmor;
import com.lunazstudios.axolib.api.model.AxoModel;
import com.lunazstudios.axolib.api.model.DefaultedAxoArmorModel;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;

/**
 * Renders an {@link AxoArmor} item as an animated AxoLib model on a living entity.
 *
 * <p>Typical usage inside a {@code RenderLayer}:
 * <pre>{@code
 * renderer.renderArmorPiece(armorItem, entity, ageInTicks, partialTick, poseStack, buffers, light);
 * }</pre>
 *
 * <p>The {@code poseStack} should already be positioned at the relevant body part when
 * called from a humanoid armor layer.
 */
public class AxoArmorRenderer<T extends AxoArmor> extends AxoRenderer<T> {

    public AxoArmorRenderer(Identifier modelResource, Identifier textureResource) {
        super(modelResource, textureResource);
    }

    public AxoArmorRenderer(AxoModel<T> model) {
        super(model);
    }

    /**
     * Convenience constructor that resolves paths from the armor name convention:
     * {@code assets/<namespace>/axolib/models/armor/<name>.axo.json}
     */
    public AxoArmorRenderer(String namespace, String name) {
        this(new DefaultedAxoArmorModel<>(Identifier.fromNamespaceAndPath(namespace, name)));
    }

    /**
     * Renders the armor piece for {@code armorItem} as worn by {@code wearer}.
     *
     * @param armorItem    the armor item instance (must implement {@link AxoArmor})
     * @param wearer       the entity wearing the armor
     * @param slot         the equipment slot being rendered (for multi-piece armor)
     * @param ageInTicks   render tick obtained via {@link AxoArmor#getAxoRenderTick}
     * @param partialTick  partial tick for smooth interpolation
     * @param poseStack    pose stack, already positioned at the desired body part
     * @param buffers      multi buffer source
     * @param packedLight  packed light value
     */
    public void renderArmorPiece(T armorItem,
                                 LivingEntity wearer,
                                 EquipmentSlot slot,
                                 float ageInTicks,
                                 float partialTick,
                                 PoseStack poseStack,
                                 MultiBufferSource buffers,
                                 int packedLight) {
        render(armorItem, ageInTicks, partialTick, wearer, poseStack, buffers, packedLight,
               net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY);
    }
}
