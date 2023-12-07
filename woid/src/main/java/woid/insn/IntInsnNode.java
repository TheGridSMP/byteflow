package woid.insn;

import java.util.Map;

import org.objectweb.asm.MethodVisitor;
import woid.insn.base.AbstractInsnNode;

public class IntInsnNode extends AbstractInsnNode {

    /**
     * The operand of this instruction.
     */
    private final int operand;

    /**
     * Constructs a new {@link IntInsnNode}.
     *
     * @param opcode  the opcode of the instruction to be constructed. This opcode must be BIPUSH,
     *                SIPUSH or NEWARRAY.
     * @param operand the operand of the instruction to be constructed.
     */
    public IntInsnNode(int opcode, int operand) {
        super(opcode);

        this.operand = operand;
    }

    /**
     * Sets the opcode of this instruction.
     *
     * @param opcode the new instruction opcode. This opcode must be BIPUSH, SIPUSH or NEWARRAY.
     */
    public void setOpcode(int opcode) {
        this.opcode = opcode;
    }

    @Override
    public int getType() {
        return INT_INSN;
    }

    @Override
    public void accept(MethodVisitor visitor) {
        visitor.visitIntInsn(this.opcode, this.operand);
        this.acceptAnnotations(visitor);
    }

    @Override
    public AbstractInsnNode clone(Map<LabelNode, LabelNode> clonedLabels) {
        return new IntInsnNode(this.opcode, this.operand).cloneAnnotations(this);
    }
}
