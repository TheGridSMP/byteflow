package dev.drtheo.byteflow.mixin.method.base;

import dev.drtheo.byteflow.mixin.MixinClass;
import woid.method.MethodNode;

import java.util.function.Supplier;

public abstract class TargetedMixinMethod<T> extends MixinMethod<T> {

    private final MethodNode targetMethod;

    public TargetedMixinMethod(MixinClass mixin, MethodNode method, T annotation, Supplier<MethodNode> supplier) {
        super(mixin, method, annotation);

        this.targetMethod = supplier.get();
    }

    public MethodNode getTargetMethod() {
        return this.targetMethod;
    }
}
