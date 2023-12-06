package dev.drtheo.byteflow.meta;

import dev.drtheo.byteflow.annotation.Shadow;
import javassist.CtMethod;

public class ShadowMixinMethod extends MixinMethod<Shadow> {

    public ShadowMixinMethod(MixinClass mixin, CtMethod method, Shadow annotation) {
        super(mixin, method, annotation);
    }

    @Override
    public void patch() {
        // ignore
    }
}
