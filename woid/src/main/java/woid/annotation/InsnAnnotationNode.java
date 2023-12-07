package woid.annotation;

import org.objectweb.asm.*;

public class InsnAnnotationNode extends TypeAnnotationNode {

    public InsnAnnotationNode(int typeRef, TypePath typePath, String descriptor, boolean visible) {
        super(typeRef, typePath, descriptor, visible);
    }

    public InsnAnnotationNode(int api, int typeRef, TypePath typePath, String descriptor, boolean visible) {
        super(api, typeRef, typePath, descriptor, visible);
    }

    public InsnAnnotationNode(TypeAnnotationNode other) {
        super(other);
    }

    @Override
    public void accept(ClassVisitor visitor) {
        throw new UnsupportedOperationException("InsnAnnotationNode doesn't support classes!");
    }

    @Override
    public void accept(RecordComponentVisitor visitor) {
        throw new UnsupportedOperationException("InsnAnnotationNode doesn't support records!");
    }

    @Override
    public void accept(MethodVisitor visitor) {
        this.accept(visitor.visitInsnAnnotation(this.getTypeRef(), this.getTypePath(), this.getDesc(), this.isVisible()));
    }

    @Override
    public void accept(FieldVisitor visitor) {
        throw new UnsupportedOperationException("InsnAnnotationNode doesn't support fields!");
    }
}
