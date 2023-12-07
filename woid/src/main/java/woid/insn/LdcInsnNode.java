package woid.insn;

import java.util.Map;

import org.objectweb.asm.ConstantDynamic;
import org.objectweb.asm.Handle;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import woid.insn.base.AbstractInsnNode;

public class LdcInsnNode extends AbstractInsnNode {

    /**
     * The constant to be loaded on the stack. This field must be a non null {@link Integer}, a {@link
     * Float}, a {@link Long}, a {@link Double}, a {@link String}, a {@link Type} of OBJECT or ARRAY
     * sort for {@code .class} constants, for classes whose version is 49, a {@link Type} of METHOD
     * sort for MethodType, a {@link Handle} for MethodHandle constants, for classes whose version is
     * 51 or a {@link ConstantDynamic} for a constant dynamic for classes whose version is 55.
     */
    private final Object cst;

    /**
     * Constructs a new {@link LdcInsnNode}.
     *
     * @param value the constant to be loaded on the stack. This parameter mist be a non null {@link
     *              Integer}, a {@link Float}, a {@link Long}, a {@link Double}, a {@link String}, a {@link
     *              Type} of OBJECT or ARRAY sort for {@code .class} constants, for classes whose version is
     *              49, a {@link Type} of METHOD sort for MethodType, a {@link Handle} for MethodHandle
     *              constants, for classes whose version is 51 or a {@link ConstantDynamic} for a constant
     *              dynamic for classes whose version is 55.
     */
    public LdcInsnNode(final Object value) {
        super(Opcodes.LDC);

        this.cst = value;
    }

    @Override
    public int getType() {
        return LDC_INSN;
    }

    @Override
    public void accept(MethodVisitor visitor) {
        visitor.visitLdcInsn(this.cst);
        this.acceptAnnotations(visitor);
    }

    @Override
    public AbstractInsnNode clone(Map<LabelNode, LabelNode> clonedLabels) {
        return new LdcInsnNode(this.cst).cloneAnnotations(this);
    }
}
