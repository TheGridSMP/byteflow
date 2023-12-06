package dev.drtheo.byteflow.meta;

import dev.drtheo.byteflow.annotation.injection.Inject;
import javassist.CtMethod;
import javassist.NotFoundException;
import javassist.bytecode.BadBytecode;
import javassist.bytecode.CodeIterator;
import javassist.bytecode.MethodInfo;

public class InjectMixinMethod extends MixinMethod<Inject> {

    private final CtMethod targetMethod;

    public InjectMixinMethod(MixinClass mixin, CtMethod method, Inject annotation) throws NotFoundException {
        super(mixin, method, annotation);

        MethodInfo info = method.getMethodInfo();
        this.targetMethod = mixin.getTarget().getMethod(info.getName(), info.getDescriptor());
    }

    @Override
    public void patch() throws BadBytecode {
        //String source = Util.shadowMethod(this.getTargetClass(), this.getMethod(), this.getId()) + "($$);";

        byte[] code = this.getMethod().getMethodInfo().getCodeAttribute().getCode();
        CodeIterator iterator = this.getTargetMethod().getMethodInfo().getCodeAttribute().iterator();

        switch (this.getAnnotation().at().value()) {
            case HEAD -> iterator.insert(code);
            case TAIL -> iterator.append(code);
        }
    }

    public CtMethod getTargetMethod() {
        return this.targetMethod;
    }
}
