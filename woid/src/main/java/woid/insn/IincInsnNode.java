package woid.insn;

import java.util.Map;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import woid.insn.base.AbstractInsnNode;

public class IincInsnNode extends AbstractInsnNode {

    /**
     * Index of the local variable to be incremented.
     */
    private final int var;

    /**
     * Amount to increment the local variable by.
     */
    private final int incr;

    /**
     * Constructs a new {@link IincInsnNode}.
     *
     * @param varIndex index of the local variable to be incremented.
     * @param incr     increment amount to increment the local variable by.
     */
    public IincInsnNode(final int varIndex, final int incr) {
        super(Opcodes.IINC);

        this.var = varIndex;
        this.incr = incr;
    }

    @Override
    public int getType() {
        return IINC_INSN;
    }

    @Override
    public void accept(MethodVisitor visitor) {
        visitor.visitIincInsn(this.var, this.incr);
        this.acceptAnnotations(visitor);
    }

    @Override
    public AbstractInsnNode clone(Map<LabelNode, LabelNode> clonedLabels) {
        return new IincInsnNode(this.var, this.incr).cloneAnnotations(this);
    }
}
