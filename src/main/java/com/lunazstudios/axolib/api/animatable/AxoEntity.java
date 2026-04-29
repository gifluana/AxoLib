package com.lunazstudios.axolib.api.animatable;

import net.minecraft.world.entity.Entity;

public interface AxoEntity extends AxoAnimatable {
    default void triggerAxoAnimation(String animation) {
        if (!(this instanceof Entity entity) || entity.level() == null) return;

        getAnimatableInstanceCache().triggerAnimation(animation, entity.level().getGameTime());
    }

    default float getAxoRenderTick(float partialTick) {
        if (!(this instanceof Entity entity) || entity.level() == null) return partialTick;

        return getAnimatableInstanceCache().renderTick(entity.level().getGameTime(), partialTick);
    }
}
