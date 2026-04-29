package com.lunazstudios.axolib.api.animatable;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public class AxoAnimatableInstanceCache {
    private final AxoAnimatable owner;
    private final Map<String, AxoAnimationController<?>> controllers = new LinkedHashMap<>();
    private final Map<String, Long> triggeredAnimations = new HashMap<>();
    private boolean initialized;
    private long renderStartedAt = Long.MIN_VALUE;

    public AxoAnimatableInstanceCache(AxoAnimatable owner) {
        this.owner = owner;
    }

    public void initialize() {
        if (initialized) return;

        AxoAnimationControllerRegistrar registrar = new AxoAnimationControllerRegistrar();
        owner.registerControllers(registrar);
        for (AxoAnimationController<?> controller : registrar.controllers()) {
            controllers.put(controller.name(), controller);
        }
        initialized = true;
    }

    public Collection<AxoAnimationController<?>> controllers() {
        initialize();
        return Collections.unmodifiableCollection(controllers.values());
    }

    public AxoAnimationController<?> getController(String name) {
        initialize();
        return controllers.get(name);
    }

    public String firstAnimation() {
        initialize();
        return controllers.values().stream()
            .map(AxoAnimationController::currentAnimation)
            .filter(animation -> animation != null && !animation.isBlank())
            .findFirst()
            .orElse("idle");
    }

    public float renderTick(long gameTime, float partialTick) {
        if (renderStartedAt == Long.MIN_VALUE) {
            renderStartedAt = gameTime;
        }

        return Math.max(0, gameTime - renderStartedAt + partialTick);
    }

    public void triggerAnimation(String animation, long gameTime) {
        if (animation == null || animation.isBlank()) return;
        triggeredAnimations.put(animation, gameTime);
    }

    public void stopAnimation(String animation) {
        triggeredAnimations.remove(animation);
    }

    public Optional<TriggeredAnimation> latestTriggeredAnimation() {
        return triggeredAnimations.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(entry -> new TriggeredAnimation(entry.getKey(), entry.getValue()));
    }

    public float triggeredTick(String animation, long gameTime, float partialTick) {
        Long startedAt = triggeredAnimations.get(animation);
        if (startedAt == null) return 0;
        return Math.max(0, gameTime - startedAt + partialTick);
    }

    public record TriggeredAnimation(String name, long startedAt) {
    }
}
