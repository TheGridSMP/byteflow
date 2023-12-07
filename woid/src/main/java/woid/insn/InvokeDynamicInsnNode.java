package woid.insn;

import java.util.Map;

import org.objectweb.asm.Handle;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import woid.insn.base.AbstractInsnNode;

public class InvokeDynamicInsnNode extends AbstractInsnNode {

    /**
     * The method's name.
     */
    private final String name;

    /**
     * The method's descriptor (see {@link org.objectweb.asm.Type}).
     */
    private final String desc;

    /**
     * The bootstrap method.
     */
    private final Handle handle;

    /**
     * The bootstrap method constant arguments.
     */
    private final Object[] args;

    /**
     * Constructs a new {@link InvokeDynamicInsnNode}.
     *
     * @param name                     the method's name.
     * @param descriptor               the method's descriptor (see {@link org.objectweb.asm.Type}).
     * @param handle    the bootstrap method.
     * @param args the bootstrap method constant arguments. Each argument must be
     *                                 an {@link Integer}, {@link Float}, {@link Long}, {@link Double}, {@link String}, {@link
     *                                 org.objectweb.asm.Type} or {@link Handle} value. This method is allowed to modify the
     *                                 content of the array so a caller should expect that this array may change.
     */
    public InvokeDynamicInsnNode(String name, String descriptor, Handle handle, Object... args) {
        super(Opcodes.INVOKEDYNAMIC);

        this.name = name;
        this.desc = descriptor;
        this.handle = handle;
        this.args = args;
    }

    @Override
    public int getType() {
        return INVOKE_DYNAMIC_INSN;
    }

    @Override
    public void accept(MethodVisitor visitor) {
        visitor.visitInvokeDynamicInsn(this.name, this.desc, this.handle, this.args);
        this.acceptAnnotations(visitor);
    }

    @Override
    public AbstractInsnNode clone(Map<LabelNode, LabelNode> clonedLabels) {
        return new InvokeDynamicInsnNode(this.name, this.desc, this.handle, this.args).cloneAnnotations(this);
    }
}
