package woid.method.specific;

import org.objectweb.asm.MethodVisitor;
import woid.insn.LabelNode;

public class LocalVariableNode {

    /**
     * The name of a local variable.
     */
    private String name;

    /**
     * The type descriptor of this local variable.
     */
    private String desc;

    /**
     * The signature of this local variable. May be {@literal null}.
     */
    private final String signature;

    /**
     * The first instruction corresponding to the scope of this local variable (inclusive).
     */
    private final LabelNode start;

    /**
     * The last instruction corresponding to the scope of this local variable (exclusive).
     */
    private final LabelNode end;

    /**
     * The local variable's index.
     */
    private final int index;

    /**
     * Constructs a new {@link LocalVariableNode}.
     *
     * @param name       the name of a local variable.
     * @param descriptor the type descriptor of this local variable.
     * @param signature  the signature of this local variable. May be {@literal null}.
     * @param start      the first instruction corresponding to the scope of this local variable
     *                   (inclusive).
     * @param end        the last instruction corresponding to the scope of this local variable (exclusive).
     * @param index      the local variable's index.
     */
    public LocalVariableNode(String name, String descriptor, String signature, LabelNode start, LabelNode end, int index) {
        this.name = name;
        this.desc = descriptor;
        this.signature = signature;
        this.start = start;
        this.end = end;
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    /**
     * Makes the given visitor visit this local variable declaration.
     *
     * @param visitor a method visitor.
     */
    public void accept(MethodVisitor visitor) {
        visitor.visitLocalVariable(this.name, this.desc, this.signature, this.start.getLabel(), this.end.getLabel(), this.index);
    }
}
