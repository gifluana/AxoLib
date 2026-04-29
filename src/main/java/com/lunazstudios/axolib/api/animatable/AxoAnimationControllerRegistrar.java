package com.lunazstudios.axolib.api.animatable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AxoAnimationControllerRegistrar {
    private final List<AxoAnimationController<?>> controllers = new ArrayList<>();

    public void add(AxoAnimationController<?> controller) {
        controllers.add(controller);
    }

    public List<AxoAnimationController<?>> controllers() {
        return Collections.unmodifiableList(controllers);
    }
}
