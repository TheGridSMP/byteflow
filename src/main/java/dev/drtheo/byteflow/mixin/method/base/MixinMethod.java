package dev.drtheo.byteflow.mixin.method.base;

import dev.drtheo.byteflow.mixin.MixinClass;
import dev.drtheo.byteflow.woid.Clazz;
import woid.method.MethodNode;

import java.lang.reflect.Method;

public abstract class MixinMethod<T> {

    private final MixinClass mixin;
    private final MethodNode method;
    private final T annotation;

    public MixinMethod(MixinClass mixin, MethodNode method, T annotation) {
        this.mixin = mixin;
        this.method = method;
        this.annotation = annotation;
    }

    public abstract void patch() throws Exception;

    public String getId() {
        return this.mixin.getId();
    }

    public Clazz getSourceClass() {
        return this.mixin.getSource();
    }

    public Clazz getTargetClass() {
        return this.mixin.getTarget();
    }

    public MixinClass getMixin() {
        return this.mixin;
    }

    public MethodNode getMethod() {
        return this.method;
    }

    public T getAnnotation() {
        return this.annotation;
    }
}
