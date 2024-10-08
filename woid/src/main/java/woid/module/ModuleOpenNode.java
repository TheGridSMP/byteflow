package woid.module;

import java.util.List;

import org.objectweb.asm.ModuleVisitor;

public class ModuleOpenNode {

    /**
     * The internal name of the opened package (see {@link org.objectweb.asm.Type#getInternalName()}).
     */
    private final String packaze;

    /**
     * The access flag of the opened package, valid values are among {@code ACC_SYNTHETIC} and {@code
     * ACC_MANDATED}.
     */
    private final int access;

    /**
     * The fully qualified names (using dots) of the modules that can use deep reflection to the
     * classes of the open package, or {@literal null}.
     */
    private final List<String> modules;

    /**
     * Constructs a new {@link ModuleOpenNode}.
     *
     * @param packaze the internal name of the opened package (see {@link
     *                org.objectweb.asm.Type#getInternalName()}).
     * @param access  the access flag of the opened package, valid values are among {@code
     *                ACC_SYNTHETIC} and {@code ACC_MANDATED}.
     * @param modules the fully qualified names (using dots) of the modules that can use deep
     *                reflection to the classes of the open package, or {@literal null}.
     */
    public ModuleOpenNode(String packaze, int access, List<String> modules) {
        this.packaze = packaze;
        this.access = access;
        this.modules = modules;
    }

    /**
     * Makes the given module visitor visit this opened package.
     *
     * @param visitor a module visitor.
     */
    public void accept(ModuleVisitor visitor) {
        visitor.visitOpen(this.packaze, this.access, this.modules == null ? null : this.modules.toArray(new String[0]));
    }
}
