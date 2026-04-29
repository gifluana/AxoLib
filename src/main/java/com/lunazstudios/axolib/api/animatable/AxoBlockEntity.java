package com.lunazstudios.axolib.api.animatable;

import net.minecraft.world.level.block.entity.BlockEntity;

public interface AxoBlockEntity extends AxoAnimatable {
    default void triggerAxoAnimation(String animation) {
        if (!(this instanceof BlockEntity blockEntity) || blockEntity.getLevel() == null) return;

        getAnimatableInstanceCache().triggerAnimation(animation, blockEntity.getLevel().getGameTime());
        blockEntity.setChanged();
    }

    default float getAxoRenderTick(float partialTick) {
        if (!(this instanceof BlockEntity blockEntity) || blockEntity.getLevel() == null) return partialTick;

        return getAnimatableInstanceCache().renderTick(blockEntity.getLevel().getGameTime(), partialTick);
    }
}
