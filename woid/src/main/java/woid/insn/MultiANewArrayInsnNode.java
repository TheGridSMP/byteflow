package woid.insn;

import java.util.Map;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import woid.insn.base.AbstractInsnNode;

public class MultiANewArrayInsnNode extends AbstractInsnNode {

    /**
     * An array type descriptor (see {@link org.objectweb.asm.Type}).
     */
    private final String desc;

    /**
     * Number of dimensions of the array to allocate.
     */
    private final int dims;

    /**
     * Constructs a new {@link MultiANewArrayInsnNode}.
     *
     * @param descriptor    an array type descriptor (see {@link org.objectweb.asm.Type}).
     * @param numDimensions the number of dimensions of the array to allocate.
     */
    public MultiANewArrayInsnNode(String descriptor, int numDimensions) {
        super(Opcodes.MULTIANEWARRAY);

        this.desc = descriptor;
        this.dims = numDimensions;
    }

    @Override
    public int getType() {
        return MULTIANEWARRAY_INSN;
    }

    @Override
    public void accept(MethodVisitor visitor) {
        visitor.visitMultiANewArrayInsn(this.desc, this.dims);
        this.acceptAnnotations(visitor);
    }

    @Override
    public AbstractInsnNode clone(Map<LabelNode, LabelNode> clonedLabels) {
        return new MultiANewArrayInsnNode(this.desc, this.dims).cloneAnnotations(this);
    }
}
