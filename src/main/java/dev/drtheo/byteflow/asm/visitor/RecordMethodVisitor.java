package dev.drtheo.byteflow.asm.visitor;

import dev.drtheo.byteflow.asm.Instructions;
import org.objectweb.asm.*;

public class RecordMethodVisitor extends MethodVisitor {

    private final Instructions instructions = new Instructions();
    
    public RecordMethodVisitor() {
        super(Opcodes.V16);
    }

    @Override
    public void visitInsn(int opcode) {
        this.instructions.add(visitor -> visitor.visitInsn(opcode));
    }

    @Override
    public void visitIntInsn(int opcode, int operand) {
        this.instructions.add(visitor -> visitor.visitIntInsn(opcode, operand));
    }

    @Override
    public void visitVarInsn(int opcode, int var) {
        this.instructions.add(visitor -> visitor.visitVarInsn(opcode, var));
    }

    @Override
    public void visitTypeInsn(int opcode, String type) {
        this.instructions.add(visitor -> visitor.visitTypeInsn(opcode, type));
    }

    @Override
    public void visitFieldInsn(int opcode, String owner, String name, String descriptor) {
        this.instructions.add(visitor -> visitor.visitFieldInsn(opcode, owner, name, descriptor));
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
        this.instructions.add(visitor -> visitor.visitMethodInsn(opcode, owner, name, descriptor, isInterface));
    }

    @Override
    public void visitInvokeDynamicInsn(String name, String descriptor, Handle bootstrapMethodHandle, Object... bootstrapMethodArguments) {
        this.instructions.add(visitor -> visitor.visitInvokeDynamicInsn(name, descriptor, bootstrapMethodHandle, bootstrapMethodArguments));
    }

    @Override
    public void visitJumpInsn(int opcode, Label label) {
        this.instructions.add(visitor -> visitor.visitJumpInsn(opcode, label));
    }

    @Override
    public void visitLabel(Label label) {
        this.instructions.add(visitor -> visitor.visitLabel(label));
    }

    @Override
    public void visitLdcInsn(Object value) {
        this.instructions.add(visitor -> visitor.visitLdcInsn(value));
    }

    @Override
    public void visitIincInsn(int var, int increment) {
        this.instructions.add(visitor -> visitor.visitIincInsn(var, increment));
    }

    @Override
    public void visitTableSwitchInsn(int min, int max, Label dflt, Label... labels) {
        this.instructions.add(visitor -> visitor.visitTableSwitchInsn(min, max, dflt, labels));
    }

    @Override
    public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels) {
        this.instructions.add(visitor -> visitor.visitLookupSwitchInsn(dflt, keys, labels));
    }

    @Override
    public void visitMultiANewArrayInsn(String descriptor, int numDimensions) {
        this.instructions.add(visitor -> visitor.visitMultiANewArrayInsn(descriptor, numDimensions));
    }

    /*@Override
    public AnnotationVisitor visitInsnAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
    
    }*/

    @Override
    public void visitTryCatchBlock(Label start, Label end, Label handler, String type) {
        this.instructions.add(visitor -> visitor.visitTryCatchBlock(start, end, handler, type));
    }

    /*@Override
    public AnnotationVisitor visitTryCatchAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
    
    }*/

    @Override
    public void visitLocalVariable(String name, String descriptor, String signature, Label start, Label end, int index) {
        this.instructions.add(visitor -> visitor.visitLocalVariable(name, descriptor, signature, start, end, index));
    }

    /*@Override
    public AnnotationVisitor visitLocalVariableAnnotation(int typeRef, TypePath typePath, Label[] start, Label[] end, int[] index, String descriptor, boolean visible) {
        
    }*/

    @Override
    public void visitLineNumber(int line, Label start) {
        this.instructions.add(visitor -> visitor.visitLineNumber(line, start));
    }

    public Instructions getInstructions() {
        return instructions;
    }
}
