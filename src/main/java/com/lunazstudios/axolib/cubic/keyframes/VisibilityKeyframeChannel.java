package com.lunazstudios.axolib.cubic.keyframes;

import java.util.ArrayList;
import java.util.List;

public class VisibilityKeyframeChannel {
    private record Entry(float tick, boolean visible) {}
    private final List<Entry> keyframes = new ArrayList<>();

    public boolean isEmpty() {
        return keyframes.isEmpty();
    }

    public void add(float tick, boolean visible) {
        keyframes.add(new Entry(tick, visible));
        keyframes.sort((a, b) -> Float.compare(a.tick(), b.tick()));
    }

    /** Step-based: returns the value of the last keyframe at or before {@code tick}. */
    public boolean evaluate(float tick) {
        if (keyframes.isEmpty()) return true;
        boolean result = keyframes.get(0).visible();
        for (Entry e : keyframes) {
            if (e.tick() <= tick) result = e.visible();
            else break;
        }
        return result;
    }
}
