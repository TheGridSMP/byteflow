package woid.insn;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import woid.insn.base.AbstractInsnNode;

import java.util.Map;

public class LookupSwitchInsnNode extends AbstractInsnNode {

    /**
     * Beginning of the default handler block.
     */
    private final LabelNode dflt;

    /**
     * The values of the keys.
     */
    private int[] keys;

    /**
     * Beginnings of the handler blocks.
     */
    private final LabelNode[] labels;

    /**
     * Constructs a new {@link LookupSwitchInsnNode}.
     *
     * @param dflt   beginning of the default handler block.
     * @param keys   the values of the keys.
     * @param labels beginnings of the handler blocks. {@code labels[i]} is the beginning of the
     *               handler block for the {@code keys[i]} key.
     */
    public LookupSwitchInsnNode(LabelNode dflt, int[] keys, LabelNode[] labels) {
        super(Opcodes.LOOKUPSWITCH);

        this.dflt = dflt;
        this.keys = keys;
        this.labels = labels;
    }

    @Override
    public int getType() {
        return LOOKUPSWITCH_INSN;
    }

    @Override
    public void accept(MethodVisitor visitor) {
        int[] keysArray = new int[this.keys.length];
        for (int i = 0, n = keysArray.length; i < n; ++i) {
            keysArray[i] = this.keys[i];
        }

        Label[] labelsArray = new Label[this.labels.length];
        for (int i = 0, n = labelsArray.length; i < n; ++i) {
            labelsArray[i] = this.labels[i].getLabel();
        }

        visitor.visitLookupSwitchInsn(dflt.getLabel(), keysArray, labelsArray);
        this.acceptAnnotations(visitor);
    }

    @Override
    public AbstractInsnNode clone(Map<LabelNode, LabelNode> clonedLabels) {
        LookupSwitchInsnNode clone = new LookupSwitchInsnNode(LookupSwitchInsnNode.clone(this.dflt, clonedLabels),
                null, LookupSwitchInsnNode.clone(this.labels, clonedLabels)
        );

        clone.keys = keys;
        return clone.cloneAnnotations(this);
    }
}
