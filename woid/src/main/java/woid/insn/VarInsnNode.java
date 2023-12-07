package woid.insn;

import java.util.Map;

import org.objectweb.asm.MethodVisitor;
import woid.insn.base.AbstractInsnNode;

public class VarInsnNode extends AbstractInsnNode {

    /**
     * The operand of this instruction. This operand is the index of a local variable.
     */
    private final int var;

    /**
     * Constructs a new {@link VarInsnNode}.
     *
     * @param opcode   the opcode of the local variable instruction to be constructed. This opcode must
     *                 be ILOAD, LLOAD, FLOAD, DLOAD, ALOAD, ISTORE, LSTORE, FSTORE, DSTORE, ASTORE or RET.
     * @param varIndex the operand of the instruction to be constructed. This operand is the index of
     *                 a local variable.
     */
    public VarInsnNode(int opcode, int varIndex) {
        super(opcode);

        this.var = varIndex;
    }

    /**
     * Sets the opcode of this instruction.
     *
     * @param opcode the new instruction opcode. This opcode must be ILOAD, LLOAD, FLOAD, DLOAD,
     *               ALOAD, ISTORE, LSTORE, FSTORE, DSTORE, ASTORE or RET.
     */
    public void setOpcode(int opcode) {
        this.opcode = opcode;
    }

    @Override
    public int getType() {
        return VAR_INSN;
    }

    @Override
    public void accept(MethodVisitor visitor) {
        visitor.visitVarInsn(this.opcode, this.var);
        this.acceptAnnotations(visitor);
    }

    @Override
    public AbstractInsnNode clone(Map<LabelNode, LabelNode> clonedLabels) {
        return new VarInsnNode(this.opcode, this.var).cloneAnnotations(this);
    }
}
