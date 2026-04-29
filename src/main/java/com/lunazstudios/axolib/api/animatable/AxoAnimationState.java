package com.lunazstudios.axolib.api.animatable;

public record AxoAnimationState<T extends AxoAnimatable>(
    T animatable,
    float ageInTicks,
    float partialTick,
    AxoAnimationController<T> controller
) {
}
