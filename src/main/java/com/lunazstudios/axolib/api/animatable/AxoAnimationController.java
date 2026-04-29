package com.lunazstudios.axolib.api.animatable;

import java.util.Objects;
import java.util.function.Function;

public class AxoAnimationController<T extends AxoAnimatable> {
    private final T animatable;
    private final String name;
    private final int transitionTicks;
    private final Function<AxoAnimationState<T>, String> animationSelector;
    private String currentAnimation;

    public AxoAnimationController(T animatable, String name, int transitionTicks, String defaultAnimation) {
        this(animatable, name, transitionTicks, state -> defaultAnimation);
    }

    public AxoAnimationController(T animatable, String name, int transitionTicks, Function<AxoAnimationState<T>, String> animationSelector) {
        this.animatable = Objects.requireNonNull(animatable, "animatable");
        this.name = Objects.requireNonNull(name, "name");
        this.transitionTicks = transitionTicks;
        this.animationSelector = Objects.requireNonNull(animationSelector, "animationSelector");
    }

    public String name() {
        return name;
    }

    public int transitionTicks() {
        return transitionTicks;
    }

    public String currentAnimation() {
        return currentAnimation;
    }

    public String tick(float ageInTicks, float partialTick) {
        currentAnimation = animationSelector.apply(new AxoAnimationState<>(animatable, ageInTicks, partialTick, this));
        return currentAnimation;
    }

    public void setAnimation(String animation) {
        this.currentAnimation = animation;
    }
}
