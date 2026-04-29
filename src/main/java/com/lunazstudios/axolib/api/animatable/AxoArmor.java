package com.lunazstudios.axolib.api.animatable;

/**
 * Implement this interface on an {@link net.minecraft.world.item.ArmorItem} subclass
 * to make its worn model animated via AxoLib.
 *
 * <p>Wire up the renderer inside a render layer:
 * <pre>{@code
 * public class MyArmorLayer extends RenderLayer<LivingEntity, HumanoidModel<LivingEntity>> {
 *     private final AxoArmorRenderer<MyArmorItem> renderer =
 *             new AxoArmorRenderer<>(new DefaultedAxoArmorModel<>(
 *                     Identifier.fromNamespaceAndPath("mymod", "my_armor")));
 *
 *     @Override
 *     public void render(PoseStack pose, MultiBufferSource buffers, int light,
 *                        LivingEntity entity, ..., float partialTick) {
 *         ItemStack stack = entity.getItemBySlot(EquipmentSlot.CHEST);
 *         if (stack.getItem() instanceof MyArmorItem item) {
 *             float age = item.getAxoRenderTick(entity, partialTick);
 *             renderer.renderArmorPiece(item, entity, age, partialTick, pose, buffers, light);
 *         }
 *     }
 * }
 * }</pre>
 */
public interface AxoArmor extends AxoAnimatable {

    /**
     * Returns the render tick for this armor piece, derived from the wearing entity's
     * game time. Call this inside your render layer to obtain {@code ageInTicks}.
     */
    default float getAxoRenderTick(net.minecraft.world.entity.LivingEntity wearer, float partialTick) {
        if (wearer.level() == null) return partialTick;
        return getAnimatableInstanceCache().renderTick(wearer.level().getGameTime(), partialTick);
    }

    /** Triggers a named animation on this armor piece from a server-side context. */
    default void triggerAxoAnimation(String animation, net.minecraft.world.entity.LivingEntity wearer) {
        if (wearer.level() == null) return;
        getAnimatableInstanceCache().triggerAnimation(animation, wearer.level().getGameTime());
    }
}
