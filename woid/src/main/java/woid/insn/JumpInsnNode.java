package woid.insn;

import java.util.Map;

import org.objectweb.asm.MethodVisitor;
import woid.insn.base.AbstractInsnNode;

public class JumpInsnNode extends AbstractInsnNode {

    /**
     * The operand of this instruction. This operand is a label that designates the instruction to
     * which this instruction may jump.
     */
    public LabelNode label;

    /**
     * Constructs a new {@link JumpInsnNode}.
     *
     * @param opcode the opcode of the type instruction to be constructed. This opcode must be IFEQ,
     *               IFNE, IFLT, IFGE, IFGT, IFLE, IF_ICMPEQ, IF_ICMPNE, IF_ICMPLT, IF_ICMPGE, IF_ICMPGT,
     *               IF_ICMPLE, IF_ACMPEQ, IF_ACMPNE, GOTO, JSR, IFNULL or IFNONNULL.
     * @param label  the operand of the instruction to be constructed. This operand is a label that
     *               designates the instruction to which the jump instruction may jump.
     */
    public JumpInsnNode(final int opcode, final LabelNode label) {
        super(opcode);

        this.label = label;
    }

    /**
     * Sets the opcode of this instruction.
     *
     * @param opcode the new instruction opcode. This opcode must be IFEQ, IFNE, IFLT, IFGE, IFGT,
     *               IFLE, IF_ICMPEQ, IF_ICMPNE, IF_ICMPLT, IF_ICMPGE, IF_ICMPGT, IF_ICMPLE, IF_ACMPEQ,
     *               IF_ACMPNE, GOTO, JSR, IFNULL or IFNONNULL.
     */
    public void setOpcode(final int opcode) {
        this.opcode = opcode;
    }

    @Override
    public int getType() {
        return JUMP_INSN;
    }

    @Override
    public void accept(MethodVisitor visitor) {
        visitor.visitJumpInsn(this.opcode, this.label.getLabel());
        this.acceptAnnotations(visitor);
    }

    @Override
    public AbstractInsnNode clone(final Map<LabelNode, LabelNode> clonedLabels) {
        return new JumpInsnNode(this.opcode, JumpInsnNode.clone(this.label, clonedLabels)).cloneAnnotations(this);
    }
}
