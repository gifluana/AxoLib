package com.lunazstudios.axolib.api.animatable;

import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class AxoAnimationController<T extends AxoAnimatable> {
    private final T animatable;
    private final String name;
    private final int transitionTicks;
    private final Function<AxoAnimationState<T>, String> animationSelector;
    private String currentAnimation;
    private @Nullable AxoAnimationEventHandler eventHandler;
    private float lastEventTick = -1f;
    private final List<AxoActiveAnimation> additionalAnimations = new ArrayList<>();

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

    public void setEventHandler(@Nullable AxoAnimationEventHandler handler) {
        this.eventHandler = handler;
    }

    public @Nullable AxoAnimationEventHandler getEventHandler() {
        return eventHandler;
    }

    /** Returns the last recorded event tick and updates it to {@code currentTick}. */
    public float getAndUpdateEventTick(float currentTick) {
        float prev = lastEventTick;
        lastEventTick = currentTick;
        return prev;
    }

    /**
     * Adds an animation layer on top of the primary animation.
     * If an entry with the same id already exists its weight is updated.
     */
    public void playAdditional(String id, float weight) {
        for (int i = 0; i < additionalAnimations.size(); i++) {
            if (additionalAnimations.get(i).id().equals(id)) {
                additionalAnimations.set(i, new AxoActiveAnimation(id, weight));
                return;
            }
        }
        additionalAnimations.add(new AxoActiveAnimation(id, weight));
    }

    /** Removes a previously added animation layer. */
    public void stopAdditional(String id) {
        additionalAnimations.removeIf(a -> a.id().equals(id));
    }

    /** Removes all additional animation layers. */
    public void stopAllAdditional() {
        additionalAnimations.clear();
    }

    public List<AxoActiveAnimation> getAdditionalAnimations() {
        return Collections.unmodifiableList(additionalAnimations);
    }
}
