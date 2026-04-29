package com.lunazstudios.axolib.utils;

public class MathUtils {
    public static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }

    public static float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(max, value));
    }

    public static float toRad(float deg) {
        return deg * ((float) Math.PI / 180f);
    }

    public static float toDeg(float rad) {
        return rad * (180f / (float) Math.PI);
    }
}
