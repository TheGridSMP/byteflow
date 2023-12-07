package dev.drtheo.byteflow.mixin.method;

import dev.drtheo.byteflow.annotation.Overwrite;
import dev.drtheo.byteflow.mixin.MixinClass;
import dev.drtheo.byteflow.mixin.method.base.TargetedMixinMethod;
import woid.insn.list.InsnList;
import woid.method.MethodNode;
import woid.util.Remap;

public class OverwriteMixinMethod extends TargetedMixinMethod<Overwrite> {

    public OverwriteMixinMethod(MixinClass mixin, MethodNode method, Overwrite annotation) {
        super(mixin, method, annotation, () -> mixin.getTarget().getMethod(method));
    }

    @Override
    public void patch() {
        Remap.method(this.getTargetMethod(), this.getSourceClass(), this.getMethod());

        InsnList add = this.getMethod().getInstructions();
        Remap.noReturn(add);

        this.getTargetMethod().getInstructions().overwrite(add);
    }
}
