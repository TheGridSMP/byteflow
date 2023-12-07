package woid.insn;

import java.util.Map;

import org.objectweb.asm.MethodVisitor;
import woid.insn.base.AbstractInsnNode;

public class TypeInsnNode extends AbstractInsnNode {

    /**
     * The operand of this instruction. Despite its name (due to historical reasons), this operand is
     * an internal name (see {@link org.objectweb.asm.Type#getInternalName()}).
     */
    private final String desc;

    /**
     * Constructs a new {@link TypeInsnNode}.
     *
     * @param opcode the opcode of the type instruction to be constructed. This opcode must be NEW,
     *               ANEWARRAY, CHECKCAST or INSTANCEOF.
     * @param type   the operand of the instruction to be constructed. This operand is an internal name
     *               (see {@link org.objectweb.asm.Type#getInternalName()}).
     */
    public TypeInsnNode(int opcode, String type) {
        super(opcode);

        this.desc = type;
    }

    /**
     * Sets the opcode of this instruction.
     *
     * @param opcode the new instruction opcode. This opcode must be NEW, ANEWARRAY, CHECKCAST or
     *               INSTANCEOF.
     */
    public void setOpcode(int opcode) {
        this.opcode = opcode;
    }

    @Override
    public int getType() {
        return TYPE_INSN;
    }

    @Override
    public void accept(MethodVisitor visitor) {
        visitor.visitTypeInsn(this.opcode, this.desc);
        this.acceptAnnotations(visitor);
    }

    @Override
    public AbstractInsnNode clone(Map<LabelNode, LabelNode> clonedLabels) {
        return new TypeInsnNode(this.opcode, this.desc).cloneAnnotations(this);
    }
}
