package woid.module;

import org.objectweb.asm.ModuleVisitor;

import java.util.List;

public class ModuleExportNode {

    /**
     * The internal name of the exported package (see {@link
     * org.objectweb.asm.Type#getInternalName()}).
     */
    private final String packaze;

    /**
     * The access flags (see {@link org.objectweb.asm.Opcodes}). Valid values are {@code
     * ACC_SYNTHETIC} and {@code ACC_MANDATED}.
     */
    private final int access;

    /**
     * The list of modules that can access this exported package, specified with fully qualified names
     * (using dots). May be {@literal null}.
     */
    private final List<String> modules;

    /**
     * Constructs a new {@link ModuleExportNode}.
     *
     * @param packaze the internal name of the exported package (see {@link
     *                org.objectweb.asm.Type#getInternalName()}).
     * @param access  the package access flags, one or more of {@code ACC_SYNTHETIC} and {@code
     *                ACC_MANDATED}.
     * @param modules a list of modules that can access this exported package, specified with fully
     *                qualified names (using dots).
     */
    public ModuleExportNode(String packaze, int access, List<String> modules) {
        this.packaze = packaze;
        this.access = access;
        this.modules = modules;
    }

    /**
     * Makes the given module visitor visit this export declaration.
     *
     * @param moduleVisitor a module visitor.
     */
    public void accept(final ModuleVisitor moduleVisitor) {
        moduleVisitor.visitExport(
                packaze, access, modules == null ? null : modules.toArray(new String[0]));
    }
}
