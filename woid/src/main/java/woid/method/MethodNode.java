package woid.method;

import org.objectweb.asm.*;
import woid.insn.base.AbstractInsnNode;
import woid.insn.list.InsnList;
import woid.method.specific.FrameNode;
import woid.method.specific.LocalVariableNode;
import woid.method.specific.TryCatchBlockNode;
import woid.specific.ParameterNode;
import woid.annotation.*;
import woid.insn.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MethodNode extends MethodVisitor {

    /**
     * The method's access flags (see {@link Opcodes}). This field also indicates if the method is
     * synthetic and/or deprecated.
     */
    private int access;

    /**
     * The method's name.
     */
    private String name;

    /**
     * The method's descriptor (see {@link Type}).
     */
    private String desc;

    /**
     * The method's signature. May be {@literal null}.
     */
    private final String signature;

    /**
     * The internal names of the method's exception classes (see {@link Type#getInternalName()}).
     */
    private final List<String> exceptions = new ArrayList<>();

    /**
     * The method parameter info (access flags and name).
     */
    private final List<ParameterNode> parameters = new ArrayList<>(5);

    /**
     * The runtime annotations of this method.
     */
    private final List<AnnotationNode> annotations = new ArrayList<>();

    /**
     * The runtime type annotations of this method.
     */
    private final List<TypeAnnotationNode> typeAnnotations = new ArrayList<>();

    /**
     * The non-standard attributes of this method. May be {@literal null}.
     */
    private final List<Attribute> attrs = new ArrayList<>();

    /**
     * The default value of this annotation interface method. This field must be a {@link Byte},
     * {@link Boolean}, {@link Character}, {@link Short}, {@link Integer}, {@link Long}, {@link
     * Float}, {@link Double}, {@link String} or {@link Type}, or an two elements String array (for
     * enumeration values), a {@link AnnotationNode}, or a {@link List} of values of one of the
     * preceding types. May be {@literal null}.
     */
    private Object annotationDefault;

    /**
     * The runtime visible parameter annotations of this method. These lists are lists of {@link
     * AnnotationNode} objects. May be {@literal null}.
     */
    private final List<ParameterAnnotationNode> parameterAnnotations = new ArrayList<>();

    /**
     * The instructions of this method.
     */
    private final InsnList instructions = new InsnList();

    /**
     * The try catch blocks of this method.
     */
    private final List<TryCatchBlockNode> tryCatchBlocks = new ArrayList<>();

    /**
     * The maximum stack size of this method.
     */
    private int maxStack;

    /**
     * The maximum number of local variables of this method.
     */
    private int maxLocals;

    /**
     * The local variables of this method. May be {@literal null}
     */
    private final List<LocalVariableNode> localVariables = new ArrayList<>(5);

    /**
     * The local variable annotations of this method.
     */
    public final List<LocalVariableAnnotationNode> localVarAnnotations = new ArrayList<>();

    /**
     * Whether the accept method has been called on this object.
     */
    private boolean visited;

    /**
     * Constructs a new {@link MethodNode}. <i>Subclasses must not use this constructor</i>. Instead,
     * they must use the {@link #MethodNode(int, int, String, String, String, String[])} version.
     *
     * @param access     the method's access flags (see {@link Opcodes}). This parameter also indicates if
     *                   the method is synthetic and/or deprecated.
     * @param name       the method's name.
     * @param descriptor the method's descriptor (see {@link Type}).
     * @param signature  the method's signature. May be {@literal null}.
     * @param exceptions the internal names of the method's exception classes (see {@link
     *                   Type#getInternalName()}). May be {@literal null}.
     * @throws IllegalStateException If a subclass calls this constructor.
     */
    public MethodNode(int access, String name, String descriptor, String signature, String[] exceptions) {
        this(Opcodes.ASM9, access, name, descriptor, signature, exceptions);

        if (this.getClass() != MethodNode.class)
            throw new IllegalStateException();
    }

    /**
     * Constructs a new {@link MethodNode}.
     *
     * @param api        the ASM API version implemented by this visitor. Must be one of the {@code
     *                   ASM}<i>x</i> values in {@link Opcodes}.
     * @param access     the method's access flags (see {@link Opcodes}). This parameter also indicates if
     *                   the method is synthetic and/or deprecated.
     * @param name       the method's name.
     * @param descriptor the method's descriptor (see {@link Type}).
     * @param signature  the method's signature. May be {@literal null}.
     * @param exceptions the internal names of the method's exception classes (see {@link
     *                   Type#getInternalName()}). May be {@literal null}.
     */
    public MethodNode(int api, int access, String name, String descriptor, String signature, String[] exceptions) {
        super(api);

        this.access = access;
        this.name = name;
        this.desc = descriptor;
        this.signature = signature;

        if (exceptions != null) {
            Collections.addAll(this.exceptions, exceptions);
        }
    }

    @Override
    public void visitParameter(String name, int access) {
        this.parameters.add(new ParameterNode(name, access));
    }

    @Override
    public AnnotationVisitor visitAnnotationDefault() {
        return new AnnotationNode(
                new ArrayList<>(0) {
                    @Override
                    public boolean add(Object o) {
                        MethodNode.this.annotationDefault = o;
                        return super.add(o);
                    }
                }, false);
    }

    @Override
    public AnnotationVisitor visitAnnotation(String descriptor, final boolean visible) {
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
    public void visitAnnotableParameterCount(int parameterCount, boolean visible) { }

    @Override
    public AnnotationVisitor visitParameterAnnotation(int parameter, String descriptor, boolean visible) {
        ParameterAnnotationNode annotation = new ParameterAnnotationNode(parameter, descriptor, visible);
        this.parameterAnnotations.add(annotation);

        return annotation;
    }

    @Override
    public void visitAttribute(Attribute attribute) {
        this.attrs.add(attribute);
    }

    @Override
    public void visitCode() { }

    @Override
    public void visitFrame(int type, int numLocal, Object[] local, int numStack, Object[] stack) {
        this.instructions.add(new FrameNode(type, numLocal, local == null ? null : this.getLabelNodes(local), numStack, stack == null ? null : this.getLabelNodes(stack)));
    }

    @Override
    public void visitInsn(int opcode) {
        this.instructions.add(new InsnNode(opcode));
    }

    @Override
    public void visitIntInsn(int opcode, int operand) {
        this.instructions.add(new IntInsnNode(opcode, operand));
    }

    @Override
    public void visitVarInsn(int opcode, int varIndex) {
        this.instructions.add(new VarInsnNode(opcode, varIndex));
    }

    @Override
    public void visitTypeInsn(int opcode, String type) {
        this.instructions.add(new TypeInsnNode(opcode, type));
    }

    @Override
    public void visitFieldInsn(int opcode, String owner, String name, String descriptor) {
        this.instructions.add(new FieldInsnNode(opcode, owner, name, descriptor));
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
        this.instructions.add(new MethodInsnNode(opcode & ~Opcodes.SOURCE_MASK, owner, name, descriptor, isInterface));
    }

    @Override
    public void visitInvokeDynamicInsn(String name, String descriptor, Handle bootstrapMethodHandle, Object... bootstrapMethodArguments) {
        this.instructions.add(new InvokeDynamicInsnNode(name, descriptor, bootstrapMethodHandle, bootstrapMethodArguments));
    }

    @Override
    public void visitJumpInsn(int opcode, Label label) {
        this.instructions.add(new JumpInsnNode(opcode, getLabelNode(label)));
    }

    @Override
    public void visitLabel(Label label) {
        this.instructions.add(getLabelNode(label));
    }

    @Override
    public void visitLdcInsn(Object value) {
        this.instructions.add(new LdcInsnNode(value));
    }

    @Override
    public void visitIincInsn(int varIndex, int increment) {
        this.instructions.add(new IincInsnNode(varIndex, increment));
    }

    @Override
    public void visitTableSwitchInsn(int min, int max, Label dflt, Label... labels) {
        this.instructions.add(new TableSwitchInsnNode(min, max, this.getLabelNode(dflt), this.getLabelNodes(labels)));
    }

    @Override
    public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels) {
        this.instructions.add(new LookupSwitchInsnNode(this.getLabelNode(dflt), keys, this.getLabelNodes(labels)));
    }

    @Override
    public void visitMultiANewArrayInsn(String descriptor, int numDimensions) {
        this.instructions.add(new MultiANewArrayInsnNode(descriptor, numDimensions));
    }

    @Override
    public AnnotationVisitor visitInsnAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
        // Find the last real instruction, i.e. the instruction targeted by this annotation.
        AbstractInsnNode current = this.instructions.getLast();

        while (current.getOpcode() == -1) {
            current = current.getPrevious();
        }

        // Add the annotation to this instruction.
        InsnAnnotationNode typeAnnotation = new InsnAnnotationNode(typeRef, typePath, descriptor, visible);
        current.getAnnotations().add(typeAnnotation);

        return typeAnnotation;
    }

    @Override
    public void visitTryCatchBlock(Label start, Label end, Label handler, String type) {
        TryCatchBlockNode tryCatchBlock = new TryCatchBlockNode(this.getLabelNode(start), this.getLabelNode(end), this.getLabelNode(handler), type);
        this.tryCatchBlocks.add(tryCatchBlock);
    }

    @Override
    public AnnotationVisitor visitTryCatchAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
        TryCatchBlockNode tryCatchBlock = this.tryCatchBlocks.get((typeRef & 0x00FFFF00) >> 8);
        TryCatchAnnotationNode annotation = new TryCatchAnnotationNode(typeRef, typePath, descriptor, visible);

        tryCatchBlock.getAnnotations().add(annotation);
        return annotation;
    }

    @Override
    public void visitLocalVariable(String name, String descriptor, String signature, Label start, Label end, int index) {
        this.localVariables.add(new LocalVariableNode(name, descriptor, signature, this.getLabelNode(start), this.getLabelNode(end), index));
    }

    @Override
    public AnnotationVisitor visitLocalVariableAnnotation(int typeRef, TypePath typePath, Label[] start, Label[] end, int[] index, String descriptor, boolean visible) {
        LocalVariableAnnotationNode localVariableAnnotation = new LocalVariableAnnotationNode(typeRef, typePath, this.getLabelNodes(start), this.getLabelNodes(end), index, descriptor, visible);
        this.localVarAnnotations.add(localVariableAnnotation);

        return localVariableAnnotation;
    }

    @Override
    public void visitLineNumber(final int line, final Label start) {
        this.instructions.add(new LineNumberNode(line, getLabelNode(start)));
    }

    @Override
    public void visitMaxs(final int maxStack, final int maxLocals) {
        this.maxStack = maxStack;
        this.maxLocals = maxLocals;
    }

    @Override
    public void visitEnd() { }

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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public InsnList getInstructions() {
        return instructions;
    }

    public List<LocalVariableNode> getLocalVariables() {
        return localVariables;
    }

    /**
     * Returns the LabelNode corresponding to the given Label. Creates a new LabelNode if necessary.
     * The default implementation of this method uses the {@link Label#info} field to store
     * associations between labels and label nodes.
     *
     * @param label a Label.
     * @return the LabelNode corresponding to label.
     */
    protected LabelNode getLabelNode(Label label) {
        if (!(label.info instanceof LabelNode)) {
            label.info = new LabelNode();
        }

        return (LabelNode) label.info;
    }

    private LabelNode[] getLabelNodes(Label[] labels) {
        LabelNode[] labelNodes = new LabelNode[labels.length];

        for (int i = 0; i < labels.length; ++i) {
            labelNodes[i] = this.getLabelNode(labels[i]);
        }

        return labelNodes;
    }

    private Object[] getLabelNodes(Object[] objects) {
        Object[] labelNodes = new Object[objects.length];

        for (int i = 0; i < objects.length; ++i) {
            Object o = objects[i];
            if (o instanceof Label label) {
                o = this.getLabelNode(label);
            }

            labelNodes[i] = o;
        }

        return labelNodes;
    }

    /**
     * Makes the given class visitor visit this method.
     *
     * @param visitor a class visitor.
     */
    public void accept(ClassVisitor visitor) {
        String[] exceptions = this.exceptions.isEmpty() ? null : this.exceptions.toArray(new String[0]);
        this.accept(visitor.visitMethod(this.access, this.name, this.desc, this.signature, exceptions));
    }

    /**
     * Makes the given method visitor visit this method.
     *
     * @param visitor a method visitor.
     */
    public void accept(MethodVisitor visitor) {
        if (visitor == null)
            return;

        // Visit the parameters.
        for (ParameterNode parameter : this.parameters) {
            parameter.accept(visitor);
        }

        // Visit the annotations.
        if (this.annotationDefault != null) {
            AnnotationVisitor annotationVisitor = visitor.visitAnnotationDefault();
            AnnotationNode.accept(annotationVisitor, null, this.annotationDefault);

            if (annotationVisitor != null) {
                annotationVisitor.visitEnd();
            }
        }

        for (AnnotationNode annotation : this.annotations) {
            annotation.accept(visitor);
        }

        for (TypeAnnotationNode typeAnnotation : this.typeAnnotations) {
            typeAnnotation.accept(visitor);
        }

        int visible = 0;
        int invisible = 0;

        for (ParameterAnnotationNode annotation : this.parameterAnnotations) {
            if (annotation.isVisible()) {
                visible++;
            } else {
                invisible++;
            }
        }

        if (visible > 0) {
            visitor.visitAnnotableParameterCount(visible, true);
        }

        if (invisible > 0) {
            visitor.visitAnnotableParameterCount(invisible, false);
        }

        for (ParameterAnnotationNode annotation : this.parameterAnnotations) {
            annotation.accept(visitor);
        }

        // Visit the non-standard attributes.
        if (this.visited) {
            this.instructions.resetLabels();
        }

        for (Attribute attr : attrs) {
            visitor.visitAttribute(attr);
        }

        // Visit the code.
        if (!this.instructions.isEmpty()) {
            visitor.visitCode();

            // Visits the try catch blocks.
            for (int i = 0, n = this.tryCatchBlocks.size(); i < n; ++i) {
                this.tryCatchBlocks.get(i).updateIndex(i);
                this.tryCatchBlocks.get(i).accept(visitor);
            }

            // Visit the instructions.
            this.instructions.accept(visitor);

            // Visits the local variables.
            for (LocalVariableNode localVariable : this.localVariables) {
                localVariable.accept(visitor);
            }

            // Visits the local variable annotations.
            for (LocalVariableAnnotationNode localVarAnnotation : this.localVarAnnotations) {
                localVarAnnotation.accept(visitor);
            }

            visitor.visitMaxs(this.maxStack, this.maxLocals);
            this.visited = true;
        }

        visitor.visitEnd();
    }
}
