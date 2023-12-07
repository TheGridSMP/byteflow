package dev.drtheo.byteflow.mixin.method;

import dev.drtheo.byteflow.mixin.MixinClass;
import dev.drtheo.byteflow.mixin.method.base.MixinMethod;
import woid.method.MethodNode;
import woid.util.Remap;

public class CloneMixinMethod extends MixinMethod<Void> {

    public CloneMixinMethod(MixinClass mixin, MethodNode method, Void annotation) {
        super(mixin, method, annotation);
    }

    @Override
    public void patch() {
        Remap.method(this.getMethod(), this.getTargetClass(), this.getMethod());
        this.getTargetClass().getMethods().add(this.getMethod());
    }
}
