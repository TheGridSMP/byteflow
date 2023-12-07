package dev.drtheo.byteflow.mixin.method;

import dev.drtheo.byteflow.annotation.injection.Inject;
import dev.drtheo.byteflow.annotation.injection.InjectAt;
import dev.drtheo.byteflow.mixin.MixinClass;
import dev.drtheo.byteflow.mixin.method.base.TargetedMixinMethod;
import org.objectweb.asm.Type;
import woid.insn.base.AbstractInsnNode;
import woid.insn.list.InsnList;
import woid.method.MethodNode;
import woid.util.Remap;
import woid.util.Util;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class InjectMixinMethod extends TargetedMixinMethod<Inject> {

    public InjectMixinMethod(MixinClass mixin, MethodNode method, Method runtime, Inject annotation) {
        super(mixin, method, annotation, () -> {
            if (annotation.at().value() == InjectAt.RETURN) {
                return mixin.getTarget().getMethod(annotation.method(),
                        Util.removeFromDesc(method.getDesc(), Type.getInternalName(runtime.getReturnType()))
                );
            }

            return mixin.getTarget().getMethod(
                    annotation.method(), method.getDesc()
            );
        });
    }

    @Override
    public void patch() {
        Remap.method(this.getTargetMethod(), this.getSourceClass(), this.getMethod());

        InsnList add = this.getMethod().getInstructions();
        Remap.noReturn(add);

        switch (this.getAnnotation().at().value()) {
            case HEAD -> this.getTargetMethod().getInstructions().insert(add);
            case TAIL -> this.getTargetMethod().getInstructions().add(add);
            case RETURN -> {
                List<AbstractInsnNode> returns = new ArrayList<>();

                for (AbstractInsnNode node : this.getTargetMethod().getInstructions()) {
                    if (Util.isReturn(node.getOpcode())) {
                        returns.add(node);
                    }
                }

                for (AbstractInsnNode ret : returns) {
                    this.getTargetMethod().getInstructions().insertBefore(ret, add);
                }
            }
        }
    }
}
