package woid.method.specific;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import woid.insn.base.AbstractInsnNode;
import woid.insn.LabelNode;

import java.util.Map;

/**
 * A node that represents a stack map frame. These nodes are pseudo instruction nodes in order to be
 * inserted in an instruction list. In fact these nodes must(*) be inserted <i>just before</i> any
 * instruction node <b>i</b> that follows an unconditional branch instruction such as GOTO or
 * THROW, that is the target of a jump instruction, or that starts an exception handler block. The
 * stack map frame types must describe the values of the local variables and of the operand stack
 * elements <i>just before</i> <b>i</b> is executed. <br>
 * <br>
 * (*) this is mandatory only for classes whose version is greater than or equal to {@link
 * Opcodes#V1_6}.
 */
public class FrameNode extends AbstractInsnNode {

    /**
     * The type of this frame. Must be {@link Opcodes#F_NEW} for expanded frames, or {@link
     * Opcodes#F_FULL}, {@link Opcodes#F_APPEND}, {@link Opcodes#F_CHOP}, {@link Opcodes#F_SAME} or
     * {@link Opcodes#F_APPEND}, {@link Opcodes#F_SAME1} for compressed frames.
     */
    private int type;

    /**
     * The types of the local variables of this stack map frame. Elements of this list can be Integer,
     * String or LabelNode objects (for primitive, reference and uninitialized types respectively -
     * see {@link MethodVisitor}).
     */
    private Object[] local;

    /**
     * The types of the operand stack elements of this stack map frame. Elements of this list can be
     * Integer, String or LabelNode objects (for primitive, reference and uninitialized types
     * respectively - see {@link MethodVisitor}).
     */
    private Object[] stack;

    private FrameNode() {
        super(-1);
    }

    /**
     * Constructs a new {@link FrameNode}.
     *
     * @param type     the type of this frame. Must be {@link Opcodes#F_NEW} for expanded frames, or
     *                 {@link Opcodes#F_FULL}, {@link Opcodes#F_APPEND}, {@link Opcodes#F_CHOP}, {@link
     *                 Opcodes#F_SAME} or {@link Opcodes#F_APPEND}, {@link Opcodes#F_SAME1} for compressed frames.
     * @param numLocal number of local variables of this stack map frame. Long and double values count
     *                 for one variable.
     * @param local    the types of the local variables of this stack map frame. Elements of this list
     *                 can be Integer, String or LabelNode objects (for primitive, reference and uninitialized
     *                 types respectively - see {@link MethodVisitor}). Long and double values are represented by
     *                 a single element.
     * @param numStack number of operand stack elements of this stack map frame. Long and double
     *                 values count for one stack element.
     * @param stack    the types of the operand stack elements of this stack map frame. Elements of this
     *                 list can be Integer, String or LabelNode objects (for primitive, reference and
     *                 uninitialized types respectively - see {@link MethodVisitor}). Long and double values are
     *                 represented by a single element.
     */
    public FrameNode(int type, int numLocal, Object[] local, int numStack, Object[] stack) {
        super(-1);
        this.type = type;

        switch (type) {
            case Opcodes.F_NEW, Opcodes.F_FULL -> {
                this.local = local;
                this.stack = stack;
            }

            case Opcodes.F_APPEND -> this.local = local;
            case Opcodes.F_SAME1 -> this.stack = stack;
            case Opcodes.F_CHOP -> this.local = new Object[numLocal];

            case Opcodes.F_SAME -> {}
            default -> throw new IllegalArgumentException();
        }
    }

    @Override
    public int getType() {
        return FRAME;
    }

    @Override
    public void accept(MethodVisitor methodVisitor) {
        switch (this.type) {
            case Opcodes.F_NEW, Opcodes.F_FULL -> methodVisitor.visitFrame(this.type, this.local.length, unwrap(this.local), this.stack.length, unwrap(this.stack));
            case Opcodes.F_APPEND -> methodVisitor.visitFrame(this.type, this.local.length, unwrap(this.local), 0, null);
            case Opcodes.F_CHOP -> methodVisitor.visitFrame(this.type, this.local.length, null, 0, null);
            case Opcodes.F_SAME -> methodVisitor.visitFrame(this.type, 0, null, 0, null);
            case Opcodes.F_SAME1 -> methodVisitor.visitFrame(this.type, 0, null, 1, unwrap(this.stack));

            default -> throw new IllegalArgumentException();
        }
    }

    @Override
    public AbstractInsnNode clone(Map<LabelNode, LabelNode> clonedLabels) {
        FrameNode clone = new FrameNode();
        clone.type = type;

        if (this.local != null) {
            clone.local = new Object[this.local.length];

            for (int i = 0; i < this.local.length; i++) {
                Object local = this.local[i];

                if (local instanceof LabelNode) {
                    local = clonedLabels.get(local);
                }

                clone.local[i] = local;
            }
        }

        if (this.stack != null) {
            clone.stack = new Object[this.stack.length];

            for (int i = 0; i < this.stack.length; i++) {
                Object local = this.stack[i];

                if (local instanceof LabelNode) {
                    local = clonedLabels.get(local);
                }

                clone.stack[i] = local;
            }
        }

        return clone;
    }

    private static Object[] unwrap(Object[] array) {
        if (array == null)
            return null;

        Object[] copy = new Object[array.length];

        for (int i = 0; i < array.length; i++) {
            Object o = array[i];

            if (o instanceof LabelNode node) {
                o = node.getLabel();
            }

            copy[i] = o;
        }

        return copy;
    }
}
