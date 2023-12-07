package woid.insn.base;

import java.util.*;

import org.objectweb.asm.MethodVisitor;
import woid.annotation.InsnAnnotationNode;
import woid.insn.*;
import woid.insn.list.InsnList;
import woid.insn.list.LinkedElement;
import woid.method.specific.FrameNode;

public abstract class AbstractInsnNode extends LinkedElement<AbstractInsnNode> {

    /**
     * The type of {@link InsnNode} instructions.
     */
    public static final int INSN = 0;

    /**
     * The type of {@link IntInsnNode} instructions.
     */
    public static final int INT_INSN = 1;

    /**
     * The type of {@link VarInsnNode} instructions.
     */
    public static final int VAR_INSN = 2;

    /**
     * The type of {@link TypeInsnNode} instructions.
     */
    public static final int TYPE_INSN = 3;

    /**
     * The type of {@link FieldInsnNode} instructions.
     */
    public static final int FIELD_INSN = 4;

    /**
     * The type of {@link MethodInsnNode} instructions.
     */
    public static final int METHOD_INSN = 5;

    /**
     * The type of {@link InvokeDynamicInsnNode} instructions.
     */
    public static final int INVOKE_DYNAMIC_INSN = 6;

    /**
     * The type of {@link JumpInsnNode} instructions.
     */
    public static final int JUMP_INSN = 7;

    /**
     * The type of {@link LabelNode} "instructions".
     */
    public static final int LABEL = 8;

    /**
     * The type of {@link LdcInsnNode} instructions.
     */
    public static final int LDC_INSN = 9;

    /**
     * The type of {@link IincInsnNode} instructions.
     */
    public static final int IINC_INSN = 10;

    /**
     * The type of {@link TableSwitchInsnNode} instructions.
     */
    public static final int TABLESWITCH_INSN = 11;

    /**
     * The type of {@link LookupSwitchInsnNode} instructions.
     */
    public static final int LOOKUPSWITCH_INSN = 12;

    /**
     * The type of {@link MultiANewArrayInsnNode} instructions.
     */
    public static final int MULTIANEWARRAY_INSN = 13;

    /**
     * The type of {@link FrameNode} "instructions".
     */
    public static final int FRAME = 14;

    /**
     * The type of {@link LineNumberNode} "instructions".
     */
    public static final int LINE = 15;

    /**
     * The opcode of this instruction, or -1 if this is not a JVM instruction (e.g. a label or a line
     * number).
     */
    protected int opcode;

    /**
     * The runtime visible type annotations of this instruction. This field is only used for real
     * instructions (i.e. not for labels, frames, or line number nodes). This list is a list of {@link
     * InsnAnnotationNode} objects.
     */
    private final Set<InsnAnnotationNode> annotations = new HashSet<>();

    /**
     * Constructs a new {@link AbstractInsnNode}.
     *
     * @param opcode the opcode of the instruction to be constructed.
     */
    protected AbstractInsnNode(int opcode) {
        this.opcode = opcode;
    }

    /**
     * Returns the opcode of this instruction.
     *
     * @return the opcode of this instruction, or -1 if this is not a JVM instruction (e.g. a label or
     * a line number).
     */
    public int getOpcode() {
        return opcode;
    }

    public Set<InsnAnnotationNode> getAnnotations() {
        return annotations;
    }

    /**
     * Returns the type of this instruction.
     *
     * @return the type of this instruction, i.e. one the constants defined in this class.
     */
    public abstract int getType();

    /**
     * Makes the given method visitor visit this instruction.
     *
     * @param methodVisitor a method visitor.
     */
    public abstract void accept(MethodVisitor methodVisitor);

    /**
     * Makes the given visitor visit the annotations of this instruction.
     *
     * @param methodVisitor a method visitor.
     */
    protected void acceptAnnotations(MethodVisitor methodVisitor) {
        for (InsnAnnotationNode annotation : this.annotations) {
            annotation.accept(methodVisitor);
        }
    }

    /**
     * Returns a copy of this instruction.
     *
     * @param clonedLabels a map from LabelNodes to cloned LabelNodes.
     * @return a copy of this instruction. The returned instruction does not belong to any {@link
     * InsnList}.
     */
    public abstract AbstractInsnNode clone(Map<LabelNode, LabelNode> clonedLabels);

    /**
     * Returns the clone of the given label.
     *
     * @param label        a label.
     * @param clonedLabels a map from LabelNodes to cloned LabelNodes.
     * @return the clone of the given label.
     */
    protected static LabelNode clone(LabelNode label, Map<LabelNode, LabelNode> clonedLabels) {
        return clonedLabels.get(label);
    }

    /**
     * Returns the clones of the given labels.
     *
     * @param labels       a list of labels.
     * @param clonedLabels a map from LabelNodes to cloned LabelNodes.
     * @return the clones of the given labels.
     */
    protected static LabelNode[] clone(List<LabelNode> labels, Map<LabelNode, LabelNode> clonedLabels) {
        LabelNode[] clones = new LabelNode[labels.size()];

        for (int i = 0; i < labels.size(); i++) {
            clones[i] = clonedLabels.get(labels.get(i));
        }

        return clones;
    }

    /**
     * Returns the clones of the given labels.
     *
     * @param labels       a list of labels.
     * @param clonedLabels a map from LabelNodes to cloned LabelNodes.
     * @return the clones of the given labels.
     */
    protected static LabelNode[] clone(LabelNode[] labels, Map<LabelNode, LabelNode> clonedLabels) {
        LabelNode[] clones = new LabelNode[labels.length];

        for (int i = 0; i < labels.length; i++) {
            clones[i] = clonedLabels.get(labels[i]);
        }

        return clones;
    }

    /**
     * Clones the annotations of the given instruction into this instruction.
     *
     * @param other the source instruction.
     * @return this instruction.
     */
    protected AbstractInsnNode cloneAnnotations(AbstractInsnNode other) {
        this.annotations.clear();

        for (InsnAnnotationNode source : other.annotations) {
            InsnAnnotationNode clone = new InsnAnnotationNode(source);

            source.accept(clone);
            this.annotations.add(clone);
        }

        return this;
    }
}
