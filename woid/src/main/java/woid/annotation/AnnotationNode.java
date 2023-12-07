package woid.annotation;

import org.objectweb.asm.*;
import woid.util.Util;

import java.util.ArrayList;
import java.util.List;

public class AnnotationNode extends AnnotationVisitor {

    private final String desc;

    /**
     * The name value pairs of this annotation. Each name value pair is stored as two consecutive
     * elements in the list. The name is a {@link String}, and the value may be a {@link Byte}, {@link
     * Boolean}, {@link Character}, {@link Short}, {@link Integer}, {@link Long}, {@link Float},
     * {@link Double}, {@link String} or {@link org.objectweb.asm.Type}, or a two elements String
     * array (for enumeration values), an {@link AnnotationNode}, or a {@link List} of values of one
     * of the preceding types. The list may be {@literal null} if there is no name value pair.
     */
    private final List<Object> values;

    private final boolean visible;

    /**
     * Constructs a new {@link AnnotationNode}. <i>Subclasses must not use this constructor</i>.
     * Instead, they must use the {@link #AnnotationNode(int, String, boolean)} version.
     *
     * @param descriptor the class descriptor of the annotation class.
     * @throws IllegalStateException If a subclass calls this constructor.
     */
    public AnnotationNode(String descriptor, boolean visible) {
        this(Opcodes.ASM9, descriptor, visible);

        if (!this.getClass().getPackageName().startsWith("woid"))
            throw new IllegalStateException();
    }

    /**
     * Constructs a new {@link AnnotationNode}.
     *
     * @param api        the ASM API version implemented by this visitor. Must be one of the {@code
     *                   ASM}<i>x</i> values in {@link Opcodes}.
     * @param descriptor the class descriptor of the annotation class.
     */
    public AnnotationNode(int api, String descriptor, boolean visible) {
        super(api);

        this.desc = descriptor;
        this.values = new ArrayList<>(this.desc != null ? 2 : 1);
        this.visible = visible;
    }

    /**
     * Constructs a new {@link AnnotationNode} to visit an array value.
     *
     * @param values where the visited values must be stored.
     */
    public AnnotationNode(List<Object> values, boolean visible) {
        super(Opcodes.ASM9);

        this.desc = null;
        this.values = values;
        this.visible = visible;
    }

    /**
     * The class descriptor of the annotation class.
     */
    public String getDesc() {
        return desc;
    }

    public int getApi() {
        return this.api;
    }

    public boolean isVisible() {
        return visible;
    }

    @Override
    public void visit(String name, Object value) {
        if (this.desc != null) {
            this.values.add(name);
        }

        this.values.add(Util.tryBoxList(value));
    }

    @Override
    public void visitEnum(String name, String descriptor, String value) {
        if (this.desc != null) {
            this.values.add(name);
        }

        this.values.add(new String[] { descriptor, value });
    }

    @Override
    public AnnotationVisitor visitAnnotation(String name, String descriptor) {
        if (this.desc != null) {
            this.values.add(name);
        }

        AnnotationNode annotation = new AnnotationNode(descriptor, this.visible);
        this.values.add(annotation);

        return annotation;
    }

    @Override
    public AnnotationVisitor visitArray(String name) {
        if (this.desc != null) {
            this.values.add(name);
        }

        List<Object> array = new ArrayList<>();
        this.values.add(array);

        return new AnnotationNode(array, this.visible);
    }

    @Override
    public void visitEnd() { }

    public void accept(ClassVisitor visitor) {
        this.accept(visitor.visitAnnotation(this.desc, this.visible));
    }

    public void accept(RecordComponentVisitor visitor) {
        this.accept(visitor.visitAnnotation(this.desc, this.visible));
    }

    public void accept(MethodVisitor visitor) {
        this.accept(visitor.visitAnnotation(this.desc, this.visible));
    }

    public void accept(FieldVisitor visitor) {
        this.accept(visitor.visitAnnotation(this.desc, this.visible));
    }

    /**
     * Makes the given visitor visit this annotation.
     *
     * @param visitor an annotation visitor. Maybe {@literal null}.
     */
    public void accept(AnnotationVisitor visitor) {
        if (visitor == null)
            return;

        for (int i = 0; i < this.values.size(); i += 2) {
            String name = (String) this.values.get(i);
            Object value = this.values.get(i + 1);

            AnnotationNode.accept(visitor, name, value);
        }

        visitor.visitEnd();
    }

    /**
     * Makes the given visitor visit a given annotation value.
     *
     * @param visitor an annotation visitor. Maybe {@literal null}.
     * @param name              the value name.
     * @param value             the actual value.
     */
    public static void accept(AnnotationVisitor visitor, String name, Object value) {
        if (visitor == null)
            return;

        if (value instanceof String[] typeValue) {
            visitor.visitEnum(name, typeValue[0], typeValue[1]);
            return;
        }

        if (value instanceof AnnotationNode node) {
            node.accept(visitor.visitAnnotation(name, node.getDesc()));
            return;
        }

        if (value instanceof List<?> list) {
            AnnotationVisitor arrayVisitor = visitor.visitArray(name);

            if (arrayVisitor == null)
                return;

            for (Object object : list) {
                AnnotationNode.accept(arrayVisitor, null, object);
            }

            arrayVisitor.visitEnd();
            return;
        }

        visitor.visit(name, value);
    }
}
