package com.lunazstudios.axolib.cubic;

import com.lunazstudios.axolib.cubic.data.animation.AxoAnimation;
import com.lunazstudios.axolib.cubic.data.animation.AxoAnimationPart;
import com.lunazstudios.axolib.cubic.data.model.AxoModel;
import com.lunazstudios.axolib.cubic.data.model.AxoModelGroup;

public class AxoModelAnimator {

    public static void animate(AxoModel model, AxoAnimation animation, float tick, float blend) {
        for (AxoModelGroup g : model.rootGroups) {
            animateGroup(g, animation, tick, blend);
        }
    }

    private static void animateGroup(AxoModelGroup group, AxoAnimation animation, float tick, float blend) {
        AxoAnimationPart part = animation.parts.get(group.id);

        if (part != null) {
            AxoTransform initial = group.initial;
            AxoTransform current = group.current;
            double px = part.tx.evaluate(tick, 0);
            double py = part.ty.evaluate(tick, 0);
            double pz = part.tz.evaluate(tick, 0);
            current.translate.x = lerp(current.translate.x, (float) px + initial.translate.x, blend);
            current.translate.y = lerp(current.translate.y, (float) py + initial.translate.y, blend);
            current.translate.z = lerp(current.translate.z, (float) pz + initial.translate.z, blend);
            double svx = part.sx.evaluate(tick, 1) - 1;
            double svy = part.sy.evaluate(tick, 1) - 1;
            double svz = part.sz.evaluate(tick, 1) - 1;
            current.scale.x = lerp(current.scale.x, (float) svx + initial.scale.x, blend);
            current.scale.y = lerp(current.scale.y, (float) svy + initial.scale.y, blend);
            current.scale.z = lerp(current.scale.z, (float) svz + initial.scale.z, blend);
            double rvx = -part.rx.evaluate(tick, 0);
            double rvy = -part.ry.evaluate(tick, 0);
            double rvz =  part.rz.evaluate(tick, 0);
            current.rotate.x = lerpYaw(current.rotate.x, (float) rvx + initial.rotate.x, blend);
            current.rotate.y = lerpYaw(current.rotate.y, (float) rvy + initial.rotate.y, blend);
            current.rotate.z = lerpYaw(current.rotate.z, (float) rvz + initial.rotate.z, blend);
        }

        for (AxoModelGroup child : group.children) {
            animateGroup(child, animation, tick, blend);
        }
    }

    private static float lerp(float a, float b, float t) {
        return a + (b - a) * t;
    }

    private static float lerpYaw(float a, float b, float t) {
        float delta = ((b - a) % 360 + 540) % 360 - 180;
        return a + delta * t;
    }
}
