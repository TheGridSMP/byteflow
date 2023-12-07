package woid.field;

import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.TypePath;
import woid.annotation.AnnotationNode;
import woid.annotation.TypeAnnotationNode;

public class FieldNode extends FieldVisitor {

    /**
     * The field's access flags (see {@link org.objectweb.asm.Opcodes}). This field also indicates if
     * the field is synthetic and/or deprecated.
     */
    private final int access;

    /**
     * The field's name.
     */
    private final String name;

    /**
     * The field's descriptor (see {@link org.objectweb.asm.Type}).
     */
    private final String desc;

    /**
     * The field's signature. May be {@literal null}.
     */
    private final String signature;

    /**
     * The field's initial value. This field, which may be {@literal null} if the field does not have
     * an initial value, must be an {@link Integer}, a {@link Float}, a {@link Long}, a {@link Double}
     * or a {@link String}.
     */
    private final Object value;

    /**
     * The runtime visible annotations of this field.
     */
    private final List<AnnotationNode> annotations = new ArrayList<>();

    /**
     * The runtime visible type annotations of this field.
     */
    private final List<TypeAnnotationNode> typeAnnotations = new ArrayList<>();

    /**
     * The non-standard attributes of this field.
     */
    private final List<Attribute> attrs = new ArrayList<>();

    /**
     * Constructs a new {@link FieldNode}. <i>Subclasses must not use this constructor</i>. Instead,
     * they must use the {@link #FieldNode(int, int, String, String, String, Object)} version.
     *
     * @param access     the field's access flags (see {@link org.objectweb.asm.Opcodes}). This parameter
     *                   also indicates if the field is synthetic and/or deprecated.
     * @param name       the field's name.
     * @param descriptor the field's descriptor (see {@link org.objectweb.asm.Type}).
     * @param signature  the field's signature.
     * @param value      the field's initial value. This parameter, which may be {@literal null} if the
     *                   field does not have an initial value, must be an {@link Integer}, a {@link Float}, a {@link
     *                   Long}, a {@link Double} or a {@link String}.
     * @throws IllegalStateException If a subclass calls this constructor.
     */
    public FieldNode(int access, String name, String descriptor, String signature, Object value) {
        this(Opcodes.ASM9, access, name, descriptor, signature, value);

        if (this.getClass() != FieldNode.class)
            throw new IllegalStateException();
    }

    /**
     * Constructs a new {@link FieldNode}.
     *
     * @param api        the ASM API version implemented by this visitor. Must be one of the {@code
     *                   ASM}<i>x</i> values in {@link Opcodes}.
     * @param access     the field's access flags (see {@link org.objectweb.asm.Opcodes}). This parameter
     *                   also indicates if the field is synthetic and/or deprecated.
     * @param name       the field's name.
     * @param descriptor the field's descriptor (see {@link org.objectweb.asm.Type}).
     * @param signature  the field's signature.
     * @param value      the field's initial value. This parameter, which may be {@literal null} if the
     *                   field does not have an initial value, must be an {@link Integer}, a {@link Float}, a {@link
     *                   Long}, a {@link Double} or a {@link String}.
     */
    public FieldNode(int api, int access, String name, String descriptor, String signature, Object value) {
        super(api);

        this.access = access;
        this.name = name;
        this.desc = descriptor;
        this.signature = signature;
        this.value = value;
    }

    @Override
    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        AnnotationNode annotation = new AnnotationNode(descriptor, visible);
        this.annotations.add(annotation);

        return annotation;
    }

    @Override
    public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
        TypeAnnotationNode typeAnnotation = new TypeAnnotationNode(typeRef, typePath, descriptor, visible);
        this.typeAnnotations.add(typeAnnotation);

        return typeAnnotation;
    }

    @Override
    public void visitAttribute(Attribute attribute) {
        this.attrs.add(attribute);
    }

    @Override
    public void visitEnd() { }

    /**
     * Makes the given class visitor visit this field.
     *
     * @param visitor a class visitor.
     */
    public void accept(ClassVisitor visitor) {
        this.accept(visitor.visitField(this.access, this.name, this.desc, this.signature, this.value));
    }

    public void accept(FieldVisitor visitor) {
        if (visitor == null)
            return;

        // Visit the annotations.
        for (AnnotationNode annotation : this.annotations) {
            annotation.accept(visitor);
        }

        for (TypeAnnotationNode typeAnnotation : this.typeAnnotations) {
            typeAnnotation.accept(visitor);
        }

        // Visit the non-standard attributes.
        for (Attribute attr : this.attrs) {
            visitor.visitAttribute(attr);
        }

        visitor.visitEnd();
    }
}
