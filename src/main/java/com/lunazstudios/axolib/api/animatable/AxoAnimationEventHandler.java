package com.lunazstudios.axolib.api.animatable;

import com.lunazstudios.axolib.cubic.data.animation.AxoAnimationEvent;

@FunctionalInterface
public interface AxoAnimationEventHandler {
    void handle(AxoAnimationEvent event);
}
