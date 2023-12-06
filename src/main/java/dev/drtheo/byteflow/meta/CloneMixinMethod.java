package dev.drtheo.byteflow.meta;

import javassist.CannotCompileException;
import javassist.CtMethod;

public class CloneMixinMethod extends MixinMethod<Void> {

    public CloneMixinMethod(MixinClass mixin, CtMethod method, Void annotation) {
        super(mixin, method, annotation);
    }

    @Override
    public void patch() throws CannotCompileException {
        this.getTargetClass().addMethod(this.getMethod());
    }
}
