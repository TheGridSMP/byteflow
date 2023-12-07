package woid.module.base;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ModuleVisitor;
import org.objectweb.asm.Opcodes;
import woid.module.ModuleExportNode;
import woid.module.ModuleOpenNode;
import woid.module.ModuleProvideNode;
import woid.module.ModuleRequireNode;

public class ModuleNode extends ModuleVisitor {

    /**
     * The fully qualified name (using dots) of this module.
     */
    private final String name;

    /**
     * The module's access flags, among {@code ACC_OPEN}, {@code ACC_SYNTHETIC} and {@code
     * ACC_MANDATED}.
     */
    private final int access;

    /**
     * The version of this module. May be {@literal null}.
     */
    private final String version;

    /**
     * The internal name of the main class of this module (see {@link
     * org.objectweb.asm.Type#getInternalName()}). May be {@literal null}.
     */
    private String mainClass;

    /**
     * The internal name of the packages declared by this module (see {@link
     * org.objectweb.asm.Type#getInternalName()}). May be {@literal null}.
     */
    private final List<String> packages = new ArrayList<>(5);

    /**
     * The dependencies of this module. May be {@literal null}.
     */
    private final List<ModuleRequireNode> requires;

    /**
     * The packages exported by this module. May be {@literal null}.
     */
    private final List<ModuleExportNode> exports;

    /**
     * The packages opened by this module. May be {@literal null}.
     */
    private final List<ModuleOpenNode> opens;

    /**
     * The internal names of the services used by this module (see {@link
     * org.objectweb.asm.Type#getInternalName()}). May be {@literal null}.
     */
    private final List<String> uses;

    /**
     * The services provided by this module. May be {@literal null}.
     */
    private final List<ModuleProvideNode> provides;

    /**
     * Constructs a {@link ModuleNode}. <i>Subclasses must not use this constructor</i>. Instead, they
     * must use the {@link #ModuleNode(int, String, int, String, List, List, List, List, List)} version.
     *
     * @param name    the fully qualified name (using dots) of the module.
     * @param access  the module access flags, among {@code ACC_OPEN}, {@code ACC_SYNTHETIC} and {@code
     *                ACC_MANDATED}.
     * @param version the module version, or {@literal null}.
     * @throws IllegalStateException If a subclass calls this constructor.
     */
    public ModuleNode(String name, int access, String version) {
        super(Opcodes.ASM9);

        if (this.getClass() != ModuleNode.class)
            throw new IllegalStateException();

        this.name = name;
        this.access = access;
        this.version = version;
        this.requires = new ArrayList<>(5);
        this.exports = new ArrayList<>(5);
        this.opens = new ArrayList<>(5);
        this.uses = new ArrayList<>(5);
        this.provides = new ArrayList<>(5);
    }

    // TODO(forax): why is there no 'mainClass' and 'packages' parameters in this constructor?

    /**
     * Constructs a {@link ModuleNode}.
     *
     * @param api      the ASM API version implemented by this visitor. Must be one of {@link
     *                 Opcodes#ASM6}, {@link Opcodes#ASM7}, {@link Opcodes#ASM8} or {@link Opcodes#ASM9}.
     * @param name     the fully qualified name (using dots) of the module.
     * @param access   the module access flags, among {@code ACC_OPEN}, {@code ACC_SYNTHETIC} and {@code
     *                 ACC_MANDATED}.
     * @param version  the module version, or {@literal null}.
     * @param requires The dependencies of this module. May be {@literal null}.
     * @param exports  The packages exported by this module. May be {@literal null}.
     * @param opens    The packages opened by this module. May be {@literal null}.
     * @param uses     The internal names of the services used by this module (see {@link
     *                 org.objectweb.asm.Type#getInternalName()}). May be {@literal null}.
     * @param provides The services provided by this module. May be {@literal null}.
     */
    public ModuleNode(int api, String name, int access, String version, List<ModuleRequireNode> requires, List<ModuleExportNode> exports, List<ModuleOpenNode> opens, List<String> uses, List<ModuleProvideNode> provides) {
        super(api);

        this.name = name;
        this.access = access;
        this.version = version;
        this.requires = requires;
        this.exports = exports;
        this.opens = opens;
        this.uses = uses;
        this.provides = provides;
    }

    @Override
    public void visitMainClass(String mainClass) {
        this.mainClass = mainClass;
    }

    @Override
    public void visitPackage(String packaze) {
        this.packages.add(packaze);
    }

    @Override
    public void visitRequire(String module, int access, String version) {
        this.requires.add(new ModuleRequireNode(module, access, version));
    }

    @Override
    public void visitExport(String packaze, int access, String... modules) {
        this.exports.add(new ModuleExportNode(packaze, access, Arrays.asList(modules)));
    }

    @Override
    public void visitOpen(String packaze, int access, String... modules) {
        this.opens.add(new ModuleOpenNode(packaze, access, Arrays.asList(modules)));
    }

    @Override
    public void visitUse(String service) {
        this.uses.add(service);
    }

    @Override
    public void visitProvide(String service, String... providers) {
        this.provides.add(new ModuleProvideNode(service, Arrays.asList(providers)));
    }

    @Override
    public void visitEnd() { }

    /**
     * Makes the given class visitor visit this module.
     *
     * @param visitor a class visitor.
     */
    public void accept(ClassVisitor visitor) {
        this.accept(visitor.visitModule(this.name, this.access, this.version));
    }

    public void accept(ModuleVisitor visitor) {
        if (visitor == null)
            return;

        if (this.mainClass != null) {
            visitor.visitMainClass(this.mainClass);
        }

        for (String pkg : this.packages) {
            visitor.visitPackage(pkg);
        }

        for (ModuleRequireNode require : this.requires) {
            require.accept(visitor);
        }

        for (ModuleExportNode export : this.exports) {
            export.accept(visitor);
        }

        for (ModuleOpenNode open : this.opens) {
            open.accept(visitor);
        }

        for (String use : this.uses) {
            visitor.visitUse(use);
        }

        for (ModuleProvideNode provide : this.provides) {
            provide.accept(visitor);
        }
    }
}
