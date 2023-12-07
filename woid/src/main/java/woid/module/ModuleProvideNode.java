package woid.module;

import java.util.List;

import org.objectweb.asm.ModuleVisitor;

public class ModuleProvideNode {

    /**
     * The internal name of the service (see {@link org.objectweb.asm.Type#getInternalName()}).
     */
    private final String service;

    /**
     * The internal names of the implementations of the service (there is at least one provider). See
     * {@link org.objectweb.asm.Type#getInternalName()}.
     */
    private final List<String> providers;

    /**
     * Constructs a new {@link ModuleProvideNode}.
     *
     * @param service   the internal name of the service.
     * @param providers the internal names of the implementations of the service (there is at least
     *                  one provider). See {@link org.objectweb.asm.Type#getInternalName()}.
     */
    public ModuleProvideNode(String service, List<String> providers) {
        this.service = service;
        this.providers = providers;
    }

    /**
     * Makes the given module visitor visit this require declaration.
     *
     * @param visitor a module visitor.
     */
    public void accept(ModuleVisitor visitor) {
        visitor.visitProvide(this.service, this.providers.toArray(new String[0]));
    }
}
