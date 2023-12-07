package woid.insn;

import java.util.Map;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import woid.insn.base.AbstractInsnNode;

public class MethodInsnNode extends AbstractInsnNode {

    /**
     * The internal name of the method's owner class (see {@link
     * org.objectweb.asm.Type#getInternalName()}).
     *
     * <p>For methods of arrays, e.g., {@code clone()}, the array type descriptor.
     */
    private final String owner;

    /**
     * The method's name.
     */
    private final String name;

    /**
     * The method's descriptor (see {@link org.objectweb.asm.Type}).
     */
    private final String desc;

    /**
     * Whether the method's owner class if an interface.
     */
    private final boolean itf;

    /**
     * Constructs a new {@link MethodInsnNode}.
     *
     * @param opcode     the opcode of the type instruction to be constructed. This opcode must be
     *                   INVOKEVIRTUAL, INVOKESPECIAL, INVOKESTATIC or INVOKEINTERFACE.
     * @param owner      the internal name of the method's owner class (see {@link
     *                   org.objectweb.asm.Type#getInternalName()}).
     * @param name       the method's name.
     * @param descriptor the method's descriptor (see {@link org.objectweb.asm.Type}).
     */
    public MethodInsnNode(int opcode, String owner, String name, String descriptor) {
        this(opcode, owner, name, descriptor, opcode == Opcodes.INVOKEINTERFACE);
    }

    /**
     * Constructs a new {@link MethodInsnNode}.
     *
     * @param opcode      the opcode of the type instruction to be constructed. This opcode must be
     *                    INVOKEVIRTUAL, INVOKESPECIAL, INVOKESTATIC or INVOKEINTERFACE.
     * @param owner       the internal name of the method's owner class (see {@link
     *                    org.objectweb.asm.Type#getInternalName()}).
     * @param name        the method's name.
     * @param descriptor  the method's descriptor (see {@link org.objectweb.asm.Type}).
     * @param isInterface if the method's owner class is an interface.
     */
    public MethodInsnNode(int opcode, String owner, String name, String descriptor, boolean isInterface) {
        super(opcode);

        this.owner = owner;
        this.name = name;
        this.desc = descriptor;
        this.itf = isInterface;
    }

    /**
     * Sets the opcode of this instruction.
     *
     * @param opcode the new instruction opcode. This opcode must be INVOKEVIRTUAL, INVOKESPECIAL,
     *               INVOKESTATIC or INVOKEINTERFACE.
     */
    public void setOpcode(final int opcode) {
        this.opcode = opcode;
    }

    @Override
    public int getType() {
        return METHOD_INSN;
    }

    @Override
    public void accept(MethodVisitor visitor) {
        visitor.visitMethodInsn(this.opcode, this.owner, this.name, this.desc, this.itf);
        this.acceptAnnotations(visitor);
    }

    @Override
    public AbstractInsnNode clone(Map<LabelNode, LabelNode> clonedLabels) {
        return new MethodInsnNode(this.opcode, this.owner, this.name, this.desc, this.itf).cloneAnnotations(this);
    }
}
