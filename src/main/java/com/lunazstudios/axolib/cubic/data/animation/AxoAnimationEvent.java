package com.lunazstudios.axolib.cubic.data.animation;

import org.jspecify.annotations.Nullable;

/**
 * A timed event embedded in an animation timeline.
 * Use pattern matching to handle each type:
 * <pre>{@code
 * handler.handle(event -> switch (event) {
 *     case AxoAnimationEvent.Sound s  -> level.playSound(..., s.sound(), ...);
 *     case AxoAnimationEvent.Particle p -> level.addParticle(...);
 *     case AxoAnimationEvent.Custom c  -> myDispatch(c.event(), c.data());
 * });
 * }</pre>
 */
public sealed interface AxoAnimationEvent
        permits AxoAnimationEvent.Sound, AxoAnimationEvent.Particle, AxoAnimationEvent.Custom {

    /** Time in ticks at which this event fires. */
    float timeTicks();

    /** Plays a sound at the entity / block position. */
    record Sound(float timeTicks, String sound) implements AxoAnimationEvent {}

    /** Spawns a particle, optionally offset from a named bone. */
    record Particle(
            float timeTicks,
            String particle,
            @Nullable String bone,
            float offsetX,
            float offsetY,
            float offsetZ
    ) implements AxoAnimationEvent {}

    /** Fires an arbitrary named event with an optional data string. */
    record Custom(float timeTicks, String event, String data) implements AxoAnimationEvent {}
}
