package woid.module;

import org.objectweb.asm.ModuleVisitor;

public class ModuleRequireNode {

    /**
     * The fully qualified name (using dots) of the dependence.
     */
    public String module;

    /**
     * The access flag of the dependence among {@code ACC_TRANSITIVE}, {@code ACC_STATIC_PHASE},
     * {@code ACC_SYNTHETIC} and {@code ACC_MANDATED}.
     */
    public int access;

    /**
     * The module version at compile time, or {@literal null}.
     */
    public String version;

    /**
     * Constructs a new {@link ModuleRequireNode}.
     *
     * @param module  the fully qualified name (using dots) of the dependence.
     * @param access  the access flag of the dependence among {@code ACC_TRANSITIVE}, {@code
     *                ACC_STATIC_PHASE}, {@code ACC_SYNTHETIC} and {@code ACC_MANDATED}.
     * @param version the module version at compile time, or {@literal null}.
     */
    public ModuleRequireNode(String module, int access, String version) {
        this.module = module;
        this.access = access;
        this.version = version;
    }

    /**
     * Makes the given module visitor visit this require directive.
     *
     * @param visitor a module visitor.
     */
    public void accept(ModuleVisitor visitor) {
        visitor.visitRequire(this.module, this.access, this.version);
    }
}
