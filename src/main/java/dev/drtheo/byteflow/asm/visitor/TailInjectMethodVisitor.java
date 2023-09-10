package dev.drtheo.byteflow.asm.visitor;

import dev.drtheo.byteflow.asm.Instructions;
import org.objectweb.asm.MethodVisitor;

public class TailInjectMethodVisitor extends InjectMethodVisitor {

    public TailInjectMethodVisitor(Instructions instructions, MethodVisitor visitor) {
        super(instructions, visitor);
    }

    @Override
    public void visitCode() {
        super.visitCode();
    }

    @Override
    public void visitEnd() {
        this.cache.apply(this);
        super.visitEnd();
    }
}