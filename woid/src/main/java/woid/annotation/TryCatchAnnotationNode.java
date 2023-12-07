package woid.annotation;

import org.objectweb.asm.*;

public class TryCatchAnnotationNode extends TypeAnnotationNode {


    public TryCatchAnnotationNode(int typeRef, TypePath typePath, String descriptor, boolean visible) {
        super(typeRef, typePath, descriptor, visible);
    }

    public TryCatchAnnotationNode(int api, int typeRef, TypePath typePath, String descriptor, boolean visible) {
        super(api, typeRef, typePath, descriptor, visible);
    }

    public TryCatchAnnotationNode(TypeAnnotationNode other) {
        super(other);
    }

    @Override
    public void accept(ClassVisitor visitor) {
        throw new UnsupportedOperationException("ParameterAnnotationNode doesn't support classes!");
    }

    @Override
    public void accept(RecordComponentVisitor visitor) {
        throw new UnsupportedOperationException("ParameterAnnotationNode doesn't support records!");
    }

    @Override
    public void accept(MethodVisitor visitor) {
        this.accept(visitor.visitTryCatchAnnotation(this.getTypeRef(), this.getTypePath(), this.getDesc(), this.isVisible()));
    }

    @Override
    public void accept(FieldVisitor visitor) {
        throw new UnsupportedOperationException("ParameterAnnotationNode doesn't support fields!");
    }
}
