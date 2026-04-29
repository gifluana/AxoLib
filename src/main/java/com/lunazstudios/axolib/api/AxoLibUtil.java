package com.lunazstudios.axolib.api;

import com.lunazstudios.axolib.api.animatable.AxoAnimatable;
import com.lunazstudios.axolib.api.animatable.AxoAnimatableInstanceCache;
import com.lunazstudios.axolib.api.animatable.AxoAnimationController;
import com.lunazstudios.axolib.api.animatable.AxoResolvedAnimation;
import com.lunazstudios.axolib.cubic.AxoLoadedModel;
import com.lunazstudios.axolib.cubic.data.animation.AxoAnimation;
import net.minecraft.resources.Identifier;

public class AxoLibUtil {
    private AxoLibUtil() {
    }

    public static AxoAnimatableInstanceCache createInstanceCache(AxoAnimatable animatable) {
        return new AxoAnimatableInstanceCache(animatable);
    }

    public static <T extends AxoAnimatable> AxoAnimationController<T> controller(T animatable, String animation) {
        return controller(animatable, "main", 0, animation);
    }

    public static <T extends AxoAnimatable> AxoAnimationController<T> controller(T animatable, String name, String animation) {
        return controller(animatable, name, 0, animation);
    }

    public static <T extends AxoAnimatable> AxoAnimationController<T> controller(T animatable, String name, int transitionTicks, String animation) {
        return new AxoAnimationController<>(animatable, name, transitionTicks, animation);
    }

    public static Identifier id(String namespace, String path) {
        return Identifier.fromNamespaceAndPath(namespace, path);
    }

    public static AxoResolvedAnimation resolveAnimation(AxoAnimatable animatable,
                                                        AxoLoadedModel loaded,
                                                        String fallbackAnimation,
                                                        long gameTime,
                                                        float partialTick) {
        AxoAnimatableInstanceCache cache = animatable.getAnimatableInstanceCache();
        float renderTick = cache.renderTick(gameTime, partialTick);
        String selectedAnimation = fallbackAnimation == null || fallbackAnimation.isBlank() ? "idle" : fallbackAnimation;

        cache.initialize();
        for (AxoAnimationController<?> controller : cache.controllers()) {
            @SuppressWarnings({"rawtypes", "unchecked"})
            String animation = ((AxoAnimationController) controller).tick(renderTick, partialTick);

            if (animation != null && !animation.isBlank()) {
                selectedAnimation = animation;
                break;
            }
        }

        var triggered = cache.latestTriggeredAnimation();
        if (triggered.isPresent() && loaded != null && loaded.animations.has(triggered.get().name())) {
            String triggeredName = triggered.get().name();
            float triggeredTick = cache.triggeredTick(triggeredName, gameTime, partialTick);
            AxoAnimation animation = loaded.animations.get(triggeredName);
            int durationTicks = animation.durationTicks();

            if (durationTicks <= 0 || triggeredTick < durationTicks) {
                return new AxoResolvedAnimation(triggeredName, triggeredTick);
            }

            cache.stopAnimation(triggeredName);
        }

        return new AxoResolvedAnimation(selectedAnimation, renderTick);
    }
}
