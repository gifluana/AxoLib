package com.lunazstudios.axolib.utils.interps;

public class Lerps {
    public static float lerp(float a, float b, float t) {
        return a + (b - a) * t;
    }

    public static double lerp(double a, double b, double t) {
        return a + (b - a) * t;
    }

    public static double lerpYaw(double a, double b, double t) {
        double delta = ((b - a) % 360 + 540) % 360 - 180;
        return a + delta * t;
    }

    public static float lerpYaw(float a, float b, float t) {
        return (float) lerpYaw((double) a, b, t);
    }
}
