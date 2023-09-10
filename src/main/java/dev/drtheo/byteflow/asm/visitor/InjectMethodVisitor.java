package dev.drtheo.byteflow.asm.visitor;

import dev.drtheo.byteflow.asm.Instructions;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public abstract class InjectMethodVisitor extends MethodVisitor {

    protected final Instructions cache;

    public InjectMethodVisitor(Instructions instructions, MethodVisitor visitor) {
        super(Opcodes.V16, visitor);

        this.cache = instructions;
    }
}
