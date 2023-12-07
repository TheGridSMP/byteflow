package woid.clazz;

import org.objectweb.asm.ClassVisitor;

/**
 * A node that represents an inner class. This inner class is not necessarily a member of the {@link
 * ClassNode} containing this object. More precisely, every class or interface which is referenced
 * by a {@link ClassNode} and which is not a package member must be represented with an {@link
 * InnerClassNode}. The {@link ClassNode} must reference its nested class or interface members, and
 * its enclosing class, if any. See the JVMS 4.7.6 section for more details.
 */
public class InnerClassNode {

    /**
     * The internal name of an inner class (see {@link org.objectweb.asm.Type#getInternalName()}).
     */
    private final String name;

    /**
     * The internal name of the class to which the inner class belongs (see {@link
     * org.objectweb.asm.Type#getInternalName()}). May be {@literal null}.
     */
    private final String outerName;

    /**
     * The (simple) name of the inner class inside its enclosing class. Must be {@literal null} if the
     * inner class is not the member of a class or interface (e.g. for local or anonymous classes).
     */
    private final String innerName;

    /**
     * The access flags of the inner class as originally declared in the source code from which the
     * class was compiled.
     */
    private final int access;

    /**
     * Constructs a new {@link InnerClassNode} for an inner class C.
     *
     * @param name      the internal name of C (see {@link org.objectweb.asm.Type#getInternalName()}).
     * @param outerName the internal name of the class or interface C is a member of (see {@link
     *                  org.objectweb.asm.Type#getInternalName()}). Must be {@literal null} if C is not the member
     *                  of a class or interface (e.g. for local or anonymous classes).
     * @param innerName the (simple) name of C. Must be {@literal null} for anonymous inner classes.
     * @param access    the access flags of C originally declared in the source code from which this
     *                  class was compiled.
     */
    public InnerClassNode(String name, String outerName, String innerName, int access) {
        this.name = name;
        this.outerName = outerName;
        this.innerName = innerName;
        this.access = access;
    }

    /**
     * Makes the given class visitor visit this inner class.
     *
     * @param visitor a class visitor.
     */
    public void accept(ClassVisitor visitor) {
        visitor.visitInnerClass(this.name, this.outerName, this.innerName, this.access);
    }
}
