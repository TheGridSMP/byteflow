package woid.annotation;

import org.objectweb.asm.*;
import woid.insn.LabelNode;

import java.util.Arrays;
import java.util.List;

public class LocalVariableAnnotationNode extends TypeAnnotationNode {

    /**
     * The fist instructions corresponding to the continuous ranges that make the scope of this local
     * variable (inclusive). Must not be {@literal null}.
     */
    private final List<LabelNode> start;

    /**
     * The last instructions corresponding to the continuous ranges that make the scope of this local
     * variable (exclusive). This list must have the same size as the 'start' list. Must not be
     * {@literal null}.
     */
    private final List<LabelNode> end;

    /**
     * The local variable's index in each range. This list must have the same size as the 'start'
     * list. Must not be {@literal null}.
     */
    private final int[] index;

    /**
     * Constructs a new {@link LocalVariableAnnotationNode}. <i>Subclasses must not use this
     * constructor</i>. Instead, they must use the {@link #LocalVariableAnnotationNode(int, int, TypePath,
     * LabelNode[], LabelNode[], int[], String, boolean)} version.
     *
     * @param typeRef    a reference to the annotated type. See {@link org.objectweb.asm.TypeReference}.
     * @param typePath   the path to the annotated type argument, wildcard bound, array element type, or
     *                   static inner type within 'typeRef'. May be {@literal null} if the annotation targets
     *                   'typeRef' as a whole.
     * @param start      the fist instructions corresponding to the continuous ranges that make the scope
     *                   of this local variable (inclusive).
     * @param end        the last instructions corresponding to the continuous ranges that make the scope of
     *                   this local variable (exclusive). This array must have the same size as the 'start' array.
     * @param index      the local variable's index in each range. This array must have the same size as
     *                   the 'start' array.
     * @param descriptor the class descriptor of the annotation class.
     */
    public LocalVariableAnnotationNode(int typeRef, TypePath typePath, LabelNode[] start, LabelNode[] end, int[] index, String descriptor, boolean visible) {
        this(Opcodes.ASM9, typeRef, typePath, start, end, index, descriptor, visible);
    }

    /**
     * Constructs a new {@link LocalVariableAnnotationNode}.
     *
     * @param api        the ASM API version implemented by this visitor. Must be one of the {@code
     *                   ASM}<i>x</i> values in {@link Opcodes}.
     * @param typeRef    a reference to the annotated type. See {@link org.objectweb.asm.TypeReference}.
     * @param start      the fist instructions corresponding to the continuous ranges that make the scope
     *                   of this local variable (inclusive).
     * @param end        the last instructions corresponding to the continuous ranges that make the scope of
     *                   this local variable (exclusive). This array must have the same size as the 'start' array.
     * @param index      the local variable's index in each range. This array must have the same size as
     *                   the 'start' array.
     * @param typePath   the path to the annotated type argument, wildcard bound, array element type, or
     *                   static inner type within 'typeRef'. May be {@literal null} if the annotation targets
     *                   'typeRef' as a whole.
     * @param descriptor the class descriptor of the annotation class.
     */
    public LocalVariableAnnotationNode(int api, int typeRef, TypePath typePath, LabelNode[] start, LabelNode[] end, int[] index, String descriptor, boolean visible) {
        super(api, typeRef, typePath, descriptor, visible);

        this.start = Arrays.asList(start);
        this.end = Arrays.asList(end);
        this.index = index;
    }

    @Override
    public void accept(ClassVisitor visitor) {
        throw new UnsupportedOperationException("LocalVariableAnnotationNode doesn't support classes!");
    }

    @Override
    public void accept(RecordComponentVisitor visitor) {
        throw new UnsupportedOperationException("LocalVariableAnnotationNode doesn't support records!");
    }

    @Override
    public void accept(FieldVisitor visitor) {
        throw new UnsupportedOperationException("LocalVariableAnnotationNode doesn't support fields!");
    }

    /**
     * Makes the given visitor visit this type annotation.
     *
     * @param visitor the visitor that must visit this annotation.
     */
    @Override
    public void accept(MethodVisitor visitor) {
        Label[] startLabels = new Label[this.start.size()];
        Label[] endLabels = new Label[this.end.size()];

        for (int i = 0; i < startLabels.length; ++i) {
            startLabels[i] = this.start.get(i).getLabel();
            endLabels[i] = this.end.get(i).getLabel();
        }

        this.accept(visitor.visitLocalVariableAnnotation(this.getTypeRef(), this.getTypePath(), startLabels, endLabels, this.index, this.getDesc(), this.isVisible()));
    }
}
