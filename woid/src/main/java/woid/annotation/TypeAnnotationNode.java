package woid.annotation;

import org.objectweb.asm.*;

/**
 * A node that represents a type annotation.
 *
 * @author Eric Bruneton
 */
public class TypeAnnotationNode extends AnnotationNode {

    private int typeRef;
    private final TypePath typePath;

    /**
     * Constructs a new {@link AnnotationNode}. <i>Subclasses must not use this constructor</i>.
     * Instead, they must use the {@link #TypeAnnotationNode(int, int, TypePath, String, boolean)} version.
     *
     * @param typeRef    a reference to the annotated type. See {@link org.objectweb.asm.TypeReference}.
     * @param typePath   the path to the annotated type argument, wildcard bound, array element type, or
     *                   static inner type within 'typeRef'. May be {@literal null} if the annotation targets
     *                   'typeRef' as a whole.
     * @param descriptor the class descriptor of the annotation class.
     * @throws IllegalStateException If a subclass calls this constructor.
     */
    public TypeAnnotationNode(int typeRef, TypePath typePath, String descriptor, boolean visible) {
        this(Opcodes.ASM9, typeRef, typePath, descriptor, visible);
    }

    /**
     * Constructs a new {@link AnnotationNode}.
     *
     * @param api        the ASM API version implemented by this visitor. Must be one of the {@code
     *                   ASM}<i>x</i> values in {@link Opcodes}.
     * @param typeRef    a reference to the annotated type. See {@link org.objectweb.asm.TypeReference}.
     * @param typePath   the path to the annotated type argument, wildcard bound, array element type, or
     *                   static inner type within 'typeRef'. May be {@literal null} if the annotation targets
     *                   'typeRef' as a whole.
     * @param descriptor the class descriptor of the annotation class.
     */
    public TypeAnnotationNode(int api, int typeRef, TypePath typePath, String descriptor, boolean visible) {
        super(api, descriptor, visible);

        this.typeRef = typeRef;
        this.typePath = typePath;
    }

    public TypeAnnotationNode(TypeAnnotationNode other) {
        this(other.getApi(), other.getTypeRef(), other.getTypePath(), other.getDesc(), other.isVisible());
    }

    /**
     * A reference to the annotated type. See {@link org.objectweb.asm.TypeReference}.
     */
    public int getTypeRef() {
        return typeRef;
    }

    public void setTypeRef(int typeRef) {
        this.typeRef = typeRef;
    }

    /**
     * The path to the annotated type argument, wildcard bound, array element type, or static outer
     * type within the referenced type. May be {@literal null} if the annotation targets 'typeRef' as
     * a whole.
     */
    public TypePath getTypePath() {
        return typePath;
    }

    @Override
    public void accept(ClassVisitor visitor) {
        this.accept(visitor.visitTypeAnnotation(this.typeRef, this.typePath, this.getDesc(), this.isVisible()));
    }

    @Override
    public void accept(RecordComponentVisitor visitor) {
        this.accept(visitor.visitTypeAnnotation(this.typeRef, this.typePath, this.getDesc(), this.isVisible()));
    }

    @Override
    public void accept(MethodVisitor visitor) {
        this.accept(visitor.visitTypeAnnotation(this.typeRef, this.typePath, this.getDesc(), this.isVisible()));
    }

    @Override
    public void accept(FieldVisitor visitor) {
        this.accept(visitor.visitTypeAnnotation(this.typeRef, this.typePath, this.getDesc(), this.isVisible()));
    }
}
