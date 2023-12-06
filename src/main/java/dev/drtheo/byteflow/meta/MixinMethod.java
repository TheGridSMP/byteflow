package dev.drtheo.byteflow.meta;

import javassist.CtClass;
import javassist.CtMethod;
import woid.MethodNode;

public abstract class MixinMethod<T> implements Patchable {

    private final MixinClass mixin;
    private final CtMethod method;
    private final T annotation;

    public MixinMethod(MixinClass mixin, CtMethod method, T annotation) {
        this.mixin = mixin;
        this.method = method;
        this.annotation = annotation;
    }

    public String getId() {
        return this.mixin.getId();
    }

    public CtClass getSourceClass() {
        return this.mixin.getSource();
    }

    public CtClass getTargetClass() {
        return this.mixin.getTarget();
    }

    public MixinClass getMixin() {
        return this.mixin;
    }

    public CtMethod getMethod() {
        return this.method;
    }

    public T getAnnotation() {
        return this.annotation;
    }
}
