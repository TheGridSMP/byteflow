package woid.method.specific;

import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.MethodVisitor;
import woid.annotation.TryCatchAnnotationNode;
import woid.insn.LabelNode;

public class TryCatchBlockNode {

    /**
     * The beginning of the exception handler's scope (inclusive).
     */
    private final LabelNode start;

    /**
     * The end of the exception handler's scope (exclusive).
     */
    private final LabelNode end;

    /**
     * The beginning of the exception handler's code.
     */
    private final LabelNode handler;

    /**
     * The internal name of the type of exceptions handled by the handler. May be {@literal null} to
     * catch any exceptions (for "finally" blocks).
     */
    private final String type;

    /**
     * The runtime type annotations on the exception handler type.
     */
    private final List<TryCatchAnnotationNode> annotations = new ArrayList<>();

    /**
     * Constructs a new {@link TryCatchBlockNode}.
     *
     * @param start   the beginning of the exception handler's scope (inclusive).
     * @param end     the end of the exception handler's scope (exclusive).
     * @param handler the beginning of the exception handler's code.
     * @param type    the internal name of the type of exceptions handled by the handler (see {@link
     *                org.objectweb.asm.Type#getInternalName()}), or {@literal null} to catch any exceptions (for
     *                "finally" blocks).
     */
    public TryCatchBlockNode(LabelNode start, LabelNode end, LabelNode handler, String type) {
        this.start = start;
        this.end = end;
        this.handler = handler;
        this.type = type;
    }

    public List<TryCatchAnnotationNode> getAnnotations() {
        return annotations;
    }

    /**
     * Updates the index of this try catch block in the method's list of try catch block nodes. This
     * index maybe stored in the 'target' field of the type annotations of this block.
     *
     * @param index the new index of this try catch block in the method's list of try catch block
     *              nodes.
     */
    public void updateIndex(int index) {
        int newTypeRef = 0x42000000 | (index << 8);

        for (TryCatchAnnotationNode annotation : this.annotations) {
            annotation.setTypeRef(newTypeRef);
        }
    }

    /**
     * Makes the given visitor visit this try catch block.
     *
     * @param visitor a method visitor.
     */
    public void accept(MethodVisitor visitor) {
        visitor.visitTryCatchBlock(this.start.getLabel(), this.end.getLabel(), this.handler == null ? null : this.handler.getLabel(), this.type);

        for (TryCatchAnnotationNode annotation : this.annotations) {
            annotation.accept(visitor);
        }
    }
}
