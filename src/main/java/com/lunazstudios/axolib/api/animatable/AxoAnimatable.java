package com.lunazstudios.axolib.api.animatable;

public interface AxoAnimatable {
    default void registerControllers(AxoAnimationControllerRegistrar controllers) {
    }

    AxoAnimatableInstanceCache getAnimatableInstanceCache();
}
