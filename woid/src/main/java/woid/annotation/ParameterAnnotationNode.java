package woid.annotation;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.RecordComponentVisitor;

import java.util.List;

public class ParameterAnnotationNode extends AnnotationNode {

    private final int parameter;

    public ParameterAnnotationNode(int parameter, String descriptor, boolean visible) {
        super(descriptor, visible);

        this.parameter = parameter;
    }

    public ParameterAnnotationNode(int api, int parameter, String descriptor, boolean visible) {
        super(api, descriptor, visible);

        this.parameter = parameter;
    }

    public ParameterAnnotationNode(int parameter, List<Object> values, boolean visible) {
        super(values, visible);

        this.parameter = parameter;
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
        this.accept(visitor.visitParameterAnnotation(this.parameter, this.getDesc(), this.isVisible()));
    }

    @Override
    public void accept(FieldVisitor visitor) {
        throw new UnsupportedOperationException("ParameterAnnotationNode doesn't support fields!");
    }
}
