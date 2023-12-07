package woid.clazz;

import java.lang.reflect.Method;
import java.util.*;

import org.objectweb.asm.*;
import woid.field.FieldNode;
import woid.method.MethodNode;
import woid.annotation.AnnotationNode;
import woid.annotation.TypeAnnotationNode;
import woid.module.base.ModuleNode;

public class ClassNode extends ClassVisitor {

    /**
     * The class version. The minor version is stored in the 16 most significant bits, and the major
     * version in the 16 least significant bits.
     */
    private int version;

    /**
     * The class's access flags (see {@link org.objectweb.asm.Opcodes}). This field also indicates if
     * the class is deprecated {@link Opcodes#ACC_DEPRECATED} or a record {@link Opcodes#ACC_RECORD}.
     */
    private int access;

    /**
     * The internal name of this class (see {@link org.objectweb.asm.Type#getInternalName()}).
     */
    private String name;

    /**
     * The signature of this class. May be {@literal null}.
     */
    private String signature;

    /**
     * The internal of name of the super class (see {@link org.objectweb.asm.Type#getInternalName()}).
     * For interfaces, the super class is {@link Object}. May be {@literal null}, but only for the
     * {@link Object} class.
     */
    private String superName;

    /**
     * The internal names of the interfaces directly implemented by this class (see {@link
     * org.objectweb.asm.Type#getInternalName()}).
     */
    private final Set<String> interfaces = new HashSet<>();

    /**
     * The name of the source file from which this class was compiled. May be {@literal null}.
     */
    private String sourceFile;

    /**
     * The correspondence between source and compiled elements of this class. May be {@literal null}.
     */
    private String sourceDebug;

    /**
     * The module stored in this class. May be {@literal null}.
     */
    private ModuleNode module;

    /**
     * The internal name of the enclosing class of this class (see {@link
     * org.objectweb.asm.Type#getInternalName()}). Must be {@literal null} if this class is not a
     * local or anonymous class.
     */
    private String outerClass;

    /**
     * The name of the method that contains the class, or {@literal null} if the class has no
     * enclosing class, or is not enclosed in a method or constructor of its enclosing class (e.g. if
     * it is enclosed in an instance initializer, static initializer, instance variable initializer,
     * or class variable initializer).
     */
    private String outerMethod;

    /**
     * The descriptor of the method that contains the class, or {@literal null} if the class has no
     * enclosing class, or is not enclosed in a method or constructor of its enclosing class (e.g. if
     * it is enclosed in an instance initializer, static initializer, instance variable initializer,
     * or class variable initializer).
     */
    private String outerMethodDesc;

    /**
     * The runtime annotations of this class.
     */
    private final List<AnnotationNode> annotations = new ArrayList<>();

    /**
     * The runtime type annotations of this class.
     */
    private final List<TypeAnnotationNode> typeAnnotations = new ArrayList<>();

    /**
     * The non-standard attributes of this class.
     */
    private final List<Attribute> attrs = new ArrayList<>();

    /**
     * The inner classes of this class.
     */
    private final List<InnerClassNode> innerClasses = new ArrayList<>();

    /**
     * The internal name of the nest host class of this class (see {@link
     * org.objectweb.asm.Type#getInternalName()}). May be {@literal null}.
     */
    private String nestHostClass;

    /**
     * The internal names of the nest members of this class (see {@link
     * org.objectweb.asm.Type#getInternalName()}).
     */
    private final List<String> nestMembers = new ArrayList<>();

    /**
     * The internal names of the permitted subclasses of this class (see {@link
     * org.objectweb.asm.Type#getInternalName()}).
     */
    private final List<String> permittedSubclasses = new ArrayList<>();

    /**
     * The record components of this class. May be {@literal null}.
     */
    private final List<RecordComponentNode> recordComponents = new ArrayList<>();

    /**
     * The fields of this class.
     */
    private final List<FieldNode> fields = new ArrayList<>();

    /**
     * The methods of this class.
     */
    private final List<MethodNode> methods = new ArrayList<>();

    /**
     * Constructs a new {@link ClassNode}. <i>Subclasses must not use this constructor</i>. Instead,
     * they must use the {@link #ClassNode(int)} version.
     *
     * @throws IllegalStateException If a subclass calls this constructor.
     */
    public ClassNode() {
        this(Opcodes.ASM9);

        if (this.getClass() != ClassNode.class)
            throw new IllegalStateException();
    }

    /**
     * Constructs a new {@link ClassNode}.
     *
     * @param api the ASM API version implemented by this visitor. Must be one of the {@code
     *            ASM}<i>x</i> values in {@link Opcodes}.
     */
    public ClassNode(int api) {
        super(api);
    }

    public int getAccess() {
        return access;
    }

    public void setAccess(int access) {
        this.access = access;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSuperName() {
        return superName;
    }

    public void setSuperName(String superName) {
        this.superName = superName;
    }

    public Collection<AnnotationNode> getAnnotations() {
        return annotations;
    }

    public AnnotationNode getAnnotation(String desc) {
        for (AnnotationNode node : this.annotations) {
            if (node.getDesc().equals(desc))
                return node;
        }

        return null;
    }

    public Collection<MethodNode> getMethods() {
        return methods;
    }

    public MethodNode getMethod(String name, String desc) {
        for (MethodNode node : this.getMethods()) {
            if (node.getName().equals(name) && node.getDesc().equals(desc))
                return node;
        }

        System.out.println("Couldn't find method with name '" + name + "' and desc '" + desc + "'");
        return null;
    }

    public MethodNode getMethod(MethodNode other) {
        return this.getMethod(other.getName(), other.getDesc());
    }

    public MethodNode getMethod(Method method) {
        return this.getMethod(method.getName(), Type.getMethodDescriptor(method));
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        this.version = version;
        this.access = access;
        this.name = name;

        this.signature = signature;
        this.superName = superName;

        Collections.addAll(this.interfaces, interfaces);
    }

    @Override
    public void visitSource(String file, String debug) {
        this.sourceFile = file;
        this.sourceDebug = debug;
    }

    @Override
    public ModuleVisitor visitModule(String name, int access, String version) {
        this.module = new ModuleNode(name, access, version);
        return this.module;
    }

    @Override
    public void visitNestHost(String nestHost) {
        this.nestHostClass = nestHost;
    }

    @Override
    public void visitOuterClass(String owner, String name, String descriptor) {
        this.outerClass = owner;
        this.outerMethod = name;
        this.outerMethodDesc = descriptor;
    }

    @Override
    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        AnnotationNode annotation = new AnnotationNode(descriptor, visible);
        this.annotations.add(annotation);

        return annotation;
    }

    @Override
    public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
        TypeAnnotationNode typeAnnotation = new TypeAnnotationNode(typeRef, typePath, descriptor, visible);
        this.typeAnnotations.add(typeAnnotation);

        return typeAnnotation;
    }

    @Override
    public void visitAttribute(Attribute attribute) {
        this.attrs.add(attribute);
    }

    @Override
    public void visitNestMember(String nestMember) {
        this.nestMembers.add(nestMember);
    }

    @Override
    public void visitPermittedSubclass(String permittedSubclass) {
        this.permittedSubclasses.add(permittedSubclass);
    }

    @Override
    public void visitInnerClass(String name, String outerName, String innerName, int access) {
        this.innerClasses.add(new InnerClassNode(name, outerName, innerName, access));
    }

    @Override
    public RecordComponentVisitor visitRecordComponent(String name, String descriptor, String signature) {
        RecordComponentNode recordComponent = new RecordComponentNode(name, descriptor, signature);

        this.recordComponents.add(recordComponent);
        return recordComponent;
    }

    @Override
    public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
        FieldNode field = new FieldNode(access, name, descriptor, signature, value);

        this.fields.add(field);
        return field;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodNode method = new MethodNode(access, name, descriptor, signature, exceptions);

        this.methods.add(method);
        return method;
    }

    @Override
    public void visitEnd() { }

    /**
     * Makes the given class visitor visit this class.
     *
     * @param visitor a class visitor.
     */
    public void accept(ClassVisitor visitor) {
        // Visit the header.
        String[] interfaces = this.interfaces.toArray(new String[0]);
        visitor.visit(this.version, this.access, this.name, this.signature, this.superName, interfaces);

        // Visit the source.
        if (this.sourceFile != null || this.sourceDebug != null) {
            visitor.visitSource(sourceFile, sourceDebug);
        }

        // Visit the module.
        if (this.module != null) {
            this.module.accept(visitor);
        }

        // Visit the nest host class.
        if (this.nestHostClass != null) {
            visitor.visitNestHost(this.nestHostClass);
        }

        // Visit the outer class.
        if (this.outerClass != null) {
            visitor.visitOuterClass(this.outerClass, this.outerMethod, this.outerMethodDesc);
        }

        // Visit the annotations.
        for (AnnotationNode annotation : this.annotations) {
            annotation.accept(visitor);
        }

        for (TypeAnnotationNode typeAnnotation : this.typeAnnotations) {
            typeAnnotation.accept(visitor);
        }

        // Visit the non-standard attributes.
        for (Attribute attr : this.attrs) {
            visitor.visitAttribute(attr);
        }

        // Visit the nest members.
        for (String nestMember : this.nestMembers) {
            visitor.visitNestMember(nestMember);
        }

        // Visit the permitted subclasses.
        for (String permittedSubclass : this.permittedSubclasses) {
            visitor.visitPermittedSubclass(permittedSubclass);
        }

        // Visit the inner classes.
        for (InnerClassNode innerClass : this.innerClasses) {
            innerClass.accept(visitor);
        }

        // Visit the record components.
        for (RecordComponentNode recordComponent : this.recordComponents) {
            recordComponent.accept(visitor);
        }

        // Visit the fields.
        for (FieldNode field : this.fields) {
            field.accept(visitor);
        }

        // Visit the methods.
        for (MethodNode method : this.methods) {
            method.accept(visitor);
        }

        visitor.visitEnd();
    }
}
