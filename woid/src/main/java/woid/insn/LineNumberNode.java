package woid.insn;

import java.util.Map;

import org.objectweb.asm.MethodVisitor;
import woid.insn.base.AbstractInsnNode;

public class LineNumberNode extends AbstractInsnNode {

    /**
     * A line number. This number refers to the source file from which the class was compiled.
     */
    private final int line;

    /**
     * The first instruction corresponding to this line number.
     */
    private final LabelNode start;

    /**
     * Constructs a new {@link LineNumberNode}.
     *
     * @param line  a line number. This number refers to the source file from which the class was
     *              compiled.
     * @param start the first instruction corresponding to this line number.
     */
    public LineNumberNode(int line, LabelNode start) {
        super(-1);

        this.line = line;
        this.start = start;
    }

    @Override
    public int getType() {
        return LINE;
    }

    @Override
    public void accept(MethodVisitor visitor) {
        visitor.visitLineNumber(this.line, this.start.getLabel());
    }

    @Override
    public AbstractInsnNode clone(Map<LabelNode, LabelNode> clonedLabels) {
        return new LineNumberNode(this.line, LineNumberNode.clone(this.start, clonedLabels));
    }
}
