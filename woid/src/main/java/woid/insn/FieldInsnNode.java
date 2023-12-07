package woid.insn;

import java.util.Map;

import org.objectweb.asm.MethodVisitor;
import woid.insn.base.AbstractInsnNode;

public class FieldInsnNode extends AbstractInsnNode {

    /**
     * The internal name of the field's owner class (see {@link
     * org.objectweb.asm.Type#getInternalName()}).
     */
    private final String owner;

    /**
     * The field's name.
     */
    private final String name;

    /**
     * The field's descriptor (see {@link org.objectweb.asm.Type}).
     */
    private final String desc;

    /**
     * Constructs a new {@link FieldInsnNode}.
     *
     * @param opcode     the opcode of the type instruction to be constructed. This opcode must be
     *                   GETSTATIC, PUTSTATIC, GETFIELD or PUTFIELD.
     * @param owner      the internal name of the field's owner class (see {@link
     *                   org.objectweb.asm.Type#getInternalName()}).
     * @param name       the field's name.
     * @param descriptor the field's descriptor (see {@link org.objectweb.asm.Type}).
     */
    public FieldInsnNode(int opcode, String owner, String name, String descriptor) {
        super(opcode);

        this.owner = owner;
        this.name = name;
        this.desc = descriptor;
    }

    public String getOwner() {
        return owner;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    /**
     * Sets the opcode of this instruction.
     *
     * @param opcode the new instruction opcode. This opcode must be GETSTATIC, PUTSTATIC, GETFIELD or
     *               PUTFIELD.
     */
    public void setOpcode(int opcode) {
        this.opcode = opcode;
    }

    @Override
    public int getType() {
        return FIELD_INSN;
    }

    @Override
    public void accept(MethodVisitor methodVisitor) {
        methodVisitor.visitFieldInsn(this.opcode, this.owner, this.name, this.desc);
        this.acceptAnnotations(methodVisitor);
    }

    @Override
    public AbstractInsnNode clone(Map<LabelNode, LabelNode> clonedLabels) {
        return new FieldInsnNode(this.opcode, this.owner, this.name, this.desc).cloneAnnotations(this);
    }
}
