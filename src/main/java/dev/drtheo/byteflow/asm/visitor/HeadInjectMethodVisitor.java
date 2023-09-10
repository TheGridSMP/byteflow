package dev.drtheo.byteflow.asm.visitor;

import dev.drtheo.byteflow.asm.Instructions;
import org.objectweb.asm.MethodVisitor;

public class HeadInjectMethodVisitor extends InjectMethodVisitor {

    public HeadInjectMethodVisitor(Instructions instructions, MethodVisitor visitor) {
        super(instructions, visitor);
    }

    @Override
    public void visitCode() {
        super.visitCode();
        this.cache.apply(this);
    }
}
