package com.lunazstudios.axolib.api.animatable;

/**
 * An additional animation layer played on top of the primary animation.
 *
 * @param id     Animation name as defined in the {@code .axo.json} file.
 * @param weight Blend weight in [0, 1]. 1.0 fully overrides the primary animation for
 *               any bone this animation touches; values below 1.0 lerp between the
 *               primary pose and this animation's pose.
 */
public record AxoActiveAnimation(String id, float weight) {}
