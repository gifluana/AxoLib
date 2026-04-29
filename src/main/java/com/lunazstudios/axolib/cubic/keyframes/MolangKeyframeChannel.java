package com.lunazstudios.axolib.cubic.keyframes;

import java.util.ArrayList;
import java.util.List;

public class MolangKeyframeChannel {
    private final List<MolangKeyframe> keyframes = new ArrayList<>();

    public boolean isEmpty() {
        return keyframes.isEmpty();
    }

    public void add(MolangKeyframe kf) {
        keyframes.add(kf);
        keyframes.sort((a, b) -> Float.compare(a.tick, b.tick));
    }

    public double evaluate(float tick, double defaultValue) {
        if (keyframes.isEmpty()) return defaultValue;

        int size = keyframes.size();
        MolangKeyframe first = keyframes.get(0);
        MolangKeyframe last  = keyframes.get(size - 1);

        if (size == 1 || tick <= first.tick) return first.value.get();
        if (tick >= last.tick)               return last.value.get();

        int lo = 0, hi = size - 1;
        while (lo + 1 < hi) {
            int mid = (lo + hi) / 2;
            if (keyframes.get(mid).tick <= tick) lo = mid; else hi = mid;
        }

        MolangKeyframe a = keyframes.get(lo);
        MolangKeyframe b = keyframes.get(hi);

        double va = a.value.get();
        double vb = b.value.get();

        float duration = b.tick - a.tick;
        if (duration <= 0) return vb;
        float t = (tick - a.tick) / duration;
        String interpolation = normalizeInterpolation(b.interpolation);

        return switch (interpolation) {
            case "constant", "step" -> va;
            case "catmullrom", "catmull_rom", "hermite", "smooth" -> {
                double vPre  = lo > 0        ? keyframes.get(lo - 1).value.get() : va;
                double vPost = hi < size - 1 ? keyframes.get(hi + 1).value.get() : vb;
                yield catmullRom(vPre, va, vb, vPost, t);
            }
            case "bezier" -> bezier(va, vb, a.rx, a.ry, b.lx, b.ly, duration, t);
            case "easeinsine", "sine_in" -> ease(va, vb, t, easeInSine(t));
            case "easeoutsine", "sine_out" -> ease(va, vb, t, easeOutSine(t));
            case "easeinoutsine", "sine_inout" -> ease(va, vb, t, easeInOutSine(t));
            case "easeinquad", "quad_in" -> ease(va, vb, t, t * t);
            case "easeoutquad", "quad_out" -> ease(va, vb, t, 1 - (1 - t) * (1 - t));
            case "easeinoutquad", "quad_inout" -> ease(va, vb, t, t < 0.5 ? 2 * t * t : 1 - Math.pow(-2 * t + 2, 2) / 2);
            case "easeincubic", "cubic_in" -> ease(va, vb, t, t * t * t);
            case "easeoutcubic", "cubic_out" -> ease(va, vb, t, 1 - Math.pow(1 - t, 3));
            case "easeinoutcubic", "cubic_inout" -> ease(va, vb, t, t < 0.5 ? 4 * t * t * t : 1 - Math.pow(-2 * t + 2, 3) / 2);
            case "easeinquart", "quart_in" -> ease(va, vb, t, t * t * t * t);
            case "easeoutquart", "quart_out" -> ease(va, vb, t, 1 - Math.pow(1 - t, 4));
            case "easeinoutquart", "quart_inout" -> ease(va, vb, t, t < 0.5 ? 8 * t * t * t * t : 1 - Math.pow(-2 * t + 2, 4) / 2);
            case "easeinquint", "quint_in" -> ease(va, vb, t, t * t * t * t * t);
            case "easeoutquint", "quint_out" -> ease(va, vb, t, 1 - Math.pow(1 - t, 5));
            case "easeinoutquint", "quint_inout" -> ease(va, vb, t, t < 0.5 ? 16 * t * t * t * t * t : 1 - Math.pow(-2 * t + 2, 5) / 2);
            case "easeinexpo", "exp_in", "expo_in" -> ease(va, vb, t, t == 0 ? 0 : Math.pow(2, 10 * t - 10));
            case "easeoutexpo", "exp_out", "expo_out" -> ease(va, vb, t, t == 1 ? 1 : 1 - Math.pow(2, -10 * t));
            case "easeinoutexpo", "exp_inout", "expo_inout" -> ease(va, vb, t, easeInOutExpo(t));
            case "easeincirc", "circle_in", "circ_in" -> ease(va, vb, t, 1 - Math.sqrt(1 - t * t));
            case "easeoutcirc", "circle_out", "circ_out" -> ease(va, vb, t, Math.sqrt(1 - Math.pow(t - 1, 2)));
            case "easeinoutcirc", "circle_inout", "circ_inout" -> ease(va, vb, t, t < 0.5 ? (1 - Math.sqrt(1 - Math.pow(2 * t, 2))) / 2 : (Math.sqrt(1 - Math.pow(-2 * t + 2, 2)) + 1) / 2);
            case "easeinback", "back_in" -> ease(va, vb, t, easeInBack(t));
            case "easeoutback", "back_out" -> ease(va, vb, t, easeOutBack(t));
            case "easeinoutback", "back_inout" -> ease(va, vb, t, easeInOutBack(t));
            case "easeinelastic" -> ease(va, vb, t, easeOutElastic(t));
            case "easeoutelastic" -> ease(va, vb, t, easeInElastic(t));
            case "easeinoutelastic", "elastic_inout" -> ease(va, vb, t, easeInOutElastic(t));
            case "elastic_in" -> ease(va, vb, t, easeInElastic(t));
            case "elastic_out" -> ease(va, vb, t, easeOutElastic(t));
            case "easeinbounce" -> ease(va, vb, t, easeOutBounce(t));
            case "easeoutbounce" -> ease(va, vb, t, easeInBounce(t));
            case "easeinoutbounce", "bounce_inout" -> ease(va, vb, t, easeInOutBounce(t));
            case "bounce_in" -> ease(va, vb, t, easeInBounce(t));
            case "bounce_out" -> ease(va, vb, t, easeOutBounce(t));
            default -> va + (vb - va) * t;
        };
    }

    private static String normalizeInterpolation(String interpolation) {
        return interpolation == null ? "linear" : interpolation.replace("-", "").toLowerCase();
    }

    private static double ease(double va, double vb, double t, double eased) {
        return va + (vb - va) * eased;
    }

    private static double easeInSine(double t) {
        return 1 - Math.cos((t * Math.PI) / 2);
    }

    private static double easeOutSine(double t) {
        return Math.sin((t * Math.PI) / 2);
    }

    private static double easeInOutSine(double t) {
        return -(Math.cos(Math.PI * t) - 1) / 2;
    }

    private static double easeInOutExpo(double t) {
        if (t == 0 || t == 1) return t;
        return t < 0.5 ? Math.pow(2, 20 * t - 10) / 2 : (2 - Math.pow(2, -20 * t + 10)) / 2;
    }

    private static double easeInBack(double t) {
        double c1 = 1.70158;
        double c3 = c1 + 1;
        return c3 * t * t * t - c1 * t * t;
    }

    private static double easeOutBack(double t) {
        double c1 = 1.70158;
        double c3 = c1 + 1;
        return 1 + c3 * Math.pow(t - 1, 3) + c1 * Math.pow(t - 1, 2);
    }

    private static double easeInOutBack(double t) {
        double c1 = 1.70158;
        double c2 = c1 * 1.525;
        return t < 0.5
            ? (Math.pow(2 * t, 2) * ((c2 + 1) * 2 * t - c2)) / 2
            : (Math.pow(2 * t - 2, 2) * ((c2 + 1) * (t * 2 - 2) + c2) + 2) / 2;
    }

    private static double easeInElastic(double t) {
        if (t == 0 || t == 1) return t;
        double c4 = (2 * Math.PI) / 3;
        return -Math.pow(2, 10 * t - 10) * Math.sin((t * 10 - 10.75) * c4);
    }

    private static double easeOutElastic(double t) {
        if (t == 0 || t == 1) return t;
        double c4 = (2 * Math.PI) / 3;
        return Math.pow(2, -10 * t) * Math.sin((t * 10 - 0.75) * c4) + 1;
    }

    private static double easeInOutElastic(double t) {
        if (t == 0 || t == 1) return t;
        double c5 = (2 * Math.PI) / 4.5;
        return t < 0.5
            ? -(Math.pow(2, 20 * t - 10) * Math.sin((20 * t - 11.125) * c5)) / 2
            : (Math.pow(2, -20 * t + 10) * Math.sin((20 * t - 11.125) * c5)) / 2 + 1;
    }

    private static double easeInBounce(double t) {
        return 1 - easeOutBounce(1 - t);
    }

    private static double easeOutBounce(double t) {
        double n1 = 7.5625;
        double d1 = 2.75;

        if (t < 1 / d1) return n1 * t * t;
        if (t < 2 / d1) return n1 * (t -= 1.5 / d1) * t + 0.75;
        if (t < 2.5 / d1) return n1 * (t -= 2.25 / d1) * t + 0.9375;

        return n1 * (t -= 2.625 / d1) * t + 0.984375;
    }

    private static double easeInOutBounce(double t) {
        return t < 0.5
            ? (1 - easeOutBounce(1 - 2 * t)) / 2
            : (1 + easeOutBounce(2 * t - 1)) / 2;
    }

    private static double catmullRom(double p0, double p1, double p2, double p3, double t) {
        double t2 = t * t, t3 = t2 * t;
        return 0.5 * ((2 * p1)
            + (-p0 + p2) * t
            + (2 * p0 - 5 * p1 + 4 * p2 - p3) * t2
            + (-p0 + 3 * p1 - 3 * p2 + p3) * t3);
    }

    private static double bezier(double va, double vb, float rxt, float ryt, float lxt, float lyt, float dur, float t) {
        double cx1 = rxt / dur, cy1 = va + ryt;
        double cx2 = 1.0 - lxt / dur, cy2 = vb - lyt;
        double u = 1 - t;
        return u * u * u * va
             + 3 * u * u * t * cy1
             + 3 * u * t * t * cy2
             + t * t * t * vb;
    }
}
