package woid.specific;

import org.objectweb.asm.MethodVisitor;

public class ParameterNode {

    /**
     * The parameter's name.
     */
    private final String name;

    /**
     * The parameter's access flags (see {@link org.objectweb.asm.Opcodes}). Valid values are {@code
     * ACC_FINAL}, {@code ACC_SYNTHETIC} and {@code ACC_MANDATED}.
     */
    private final int access;

    /**
     * Constructs a new {@link ParameterNode}.
     *
     * @param access The parameter's access flags. Valid values are {@code ACC_FINAL}, {@code
     *               ACC_SYNTHETIC} or/and {@code ACC_MANDATED} (see {@link org.objectweb.asm.Opcodes}).
     * @param name   the parameter's name.
     */
    public ParameterNode(String name, int access) {
        this.name = name;
        this.access = access;
    }

    /**
     * Makes the given visitor visit this parameter declaration.
     *
     * @param visitor a method visitor.
     */
    public void accept(MethodVisitor visitor) {
        visitor.visitParameter(this.name, this.access);
    }
}
