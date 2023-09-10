package dev.drtheo.byteflow.asm.visitor;

import dev.drtheo.byteflow.asm.Instructions;
import org.objectweb.asm.*;

public class OverwriteMethodVisitor extends MethodVisitor {

    private final Instructions instructions;
    private final MethodVisitor visitor;

    public OverwriteMethodVisitor(Instructions instructions, MethodVisitor visitor) {
        super(Opcodes.V16);

        this.instructions = instructions;
        this.visitor = visitor;
    }

    @Override
    public void visitCode() {
        this.visitor.visitCode();
        this.instructions.apply(visitor);
    }

    @Override
    public void visitEnd() {
        this.visitor.visitEnd();
    }

    @Override
    public void visitAttribute(Attribute attribute) {
        this.visitor.visitAttribute(attribute);
    }

    @Override
    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        return this.visitor.visitAnnotation(descriptor, visible);
    }

    @Override
    public void visitParameter(String name, int access) {
        this.visitor.visitParameter(name, access);
    }

    @Override
    public AnnotationVisitor visitAnnotationDefault() {
        return this.visitor.visitAnnotationDefault();
    }
}
