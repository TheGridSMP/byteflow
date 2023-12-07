package dev.drtheo.byteflow.mixin.method;

import dev.drtheo.byteflow.annotation.Shadow;
import dev.drtheo.byteflow.mixin.MixinClass;
import dev.drtheo.byteflow.mixin.method.base.MixinMethod;
import woid.method.MethodNode;

public class ShadowMixinMethod extends MixinMethod<Shadow> {

    public ShadowMixinMethod(MixinClass mixin, MethodNode method, Shadow annotation) {
        super(mixin, method, annotation);
    }

    @Override
    public void patch() {
        // ignore
    }
}
