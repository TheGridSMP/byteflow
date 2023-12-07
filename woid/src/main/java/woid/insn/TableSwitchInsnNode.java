package woid.insn;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import woid.insn.base.AbstractInsnNode;

public class TableSwitchInsnNode extends AbstractInsnNode {

    /**
     * The minimum key value.
     */
    private final int min;

    /**
     * The maximum key value.
     */
    private final int max;

    /**
     * Beginning of the default handler block.
     */
    private final LabelNode dflt;

    /**
     * Beginnings of the handler blocks. This list is a list of {@link LabelNode} objects.
     */
    private final List<LabelNode> labels;

    /**
     * Constructs a new {@link TableSwitchInsnNode}.
     *
     * @param min    the minimum key value.
     * @param max    the maximum key value.
     * @param dflt   beginning of the default handler block.
     * @param labels beginnings of the handler blocks. {@code labels[i]} is the beginning of the
     *               handler block for the {@code min + i} key.
     */
    public TableSwitchInsnNode(int min, int max, LabelNode dflt, LabelNode... labels) {
        super(Opcodes.TABLESWITCH);

        this.min = min;
        this.max = max;
        this.dflt = dflt;
        this.labels = Arrays.asList(labels);
    }

    @Override
    public int getType() {
        return TABLESWITCH_INSN;
    }

    @Override
    public void accept(MethodVisitor visitor) {
        Label[] labelsArray = new Label[this.labels.size()];
        for (int i = 0, n = labelsArray.length; i < n; ++i) {
            labelsArray[i] = this.labels.get(i).getLabel();
        }

        visitor.visitTableSwitchInsn(this.min, this.max, this.dflt.getLabel(), labelsArray);
        this.acceptAnnotations(visitor);
    }

    @Override
    public AbstractInsnNode clone(Map<LabelNode, LabelNode> clonedLabels) {
        return new TableSwitchInsnNode(this.min, this.max, clone(this.dflt, clonedLabels), clone(this.labels, clonedLabels))
                .cloneAnnotations(this);
    }
}
