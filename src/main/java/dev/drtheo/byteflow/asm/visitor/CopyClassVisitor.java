package dev.drtheo.byteflow.asm.visitor;

import org.objectweb.asm.*;
import org.spongepowered.asm.mixin.Shadow;

public class CopyClassVisitor extends ClassVisitor {

    private static final String SHADOW = Type.getDescriptor(Shadow.class);

    public CopyClassVisitor(ClassVisitor visitor) {
        super(Opcodes.V16, visitor);
    }

    @Override
    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        if (SHADOW.equals(descriptor)) {

        }
        //TODO: make the FieldVisitors and MethodVisitors ignore @Shadow elements
        return super.visitAnnotation(descriptor, visible);
    }

    @Override
    public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
        return super.visitTypeAnnotation(typeRef, typePath, descriptor, visible);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        return super.visitMethod(access, name, descriptor, signature, exceptions);
    }
}
