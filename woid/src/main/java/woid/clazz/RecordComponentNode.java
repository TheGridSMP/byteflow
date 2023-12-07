package woid.clazz;

import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.RecordComponentVisitor;
import org.objectweb.asm.TypePath;
import woid.annotation.AnnotationNode;
import woid.annotation.TypeAnnotationNode;

public class RecordComponentNode extends RecordComponentVisitor {

    /**
     * The record component name.
     */
    private final String name;

    /**
     * The record component descriptor (see {@link org.objectweb.asm.Type}).
     */
    private final String descriptor;

    /**
     * The record component signature. May be {@literal null}.
     */
    private final String signature;

    /**
     * The runtime visible annotations of this record component.
     */
    private final List<AnnotationNode> annotations = new ArrayList<>();

    /**
     * The runtime visible type annotations of this record component.
     */
    private final List<TypeAnnotationNode> typeAnnotations = new ArrayList<>();

    /**
     * The non-standard attributes of this record component. * May be {@literal null}.
     */
    private final List<Attribute> attrs = new ArrayList<>();

    /**
     * Constructs a new {@link RecordComponentNode}. <i>Subclasses must not use this constructor</i>.
     * Instead, they must use the {@link #RecordComponentNode(int, String, String, String)} version.
     *
     * @param name       the record component name.
     * @param descriptor the record component descriptor (see {@link org.objectweb.asm.Type}).
     * @param signature  the record component signature.
     * @throws IllegalStateException If a subclass calls this constructor.
     */
    public RecordComponentNode(String name, String descriptor, String signature) {
        this(Opcodes.ASM9, name, descriptor, signature);

        if (this.getClass() != RecordComponentNode.class)
            throw new IllegalStateException();
    }

    /**
     * Constructs a new {@link RecordComponentNode}.
     *
     * @param api        the ASM API version implemented by this visitor. Must be one of {@link Opcodes#ASM8}
     *                   or {@link Opcodes#ASM9}.
     * @param name       the record component name.
     * @param descriptor the record component descriptor (see {@link org.objectweb.asm.Type}).
     * @param signature  the record component signature.
     */
    public RecordComponentNode(int api, String name, String descriptor, String signature) {
        super(api);

        this.name = name;
        this.descriptor = descriptor;
        this.signature = signature;
    }

    // -----------------------------------------------------------------------------------------------
    // Implementation of the FieldVisitor abstract class
    // -----------------------------------------------------------------------------------------------

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
     * Makes the given class visitor visit this record component.
     *
     * @param visitor a class visitor.
     */
    public void accept(ClassVisitor visitor) {
        this.accept(visitor.visitRecordComponent(this.name, this.descriptor, this.signature));
    }

    public void accept(RecordComponentVisitor visitor) {
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
