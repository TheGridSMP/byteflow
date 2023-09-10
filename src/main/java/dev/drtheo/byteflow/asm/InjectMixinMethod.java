package dev.drtheo.byteflow.asm;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
import org.spongepowered.asm.mixin.injection.Inject;

public class InjectMixinMethod {

    private final String mask;
    private final InjectAt at;
    private final MethodNode self;

    public InjectMixinMethod(String mask, InjectAt at, MethodNode self) {
        this.mask = mask;
        this.at = at;
        this.self = self;
    }

    public InjectMixinMethod(InjectAt at, MethodNode self) {
        this(null, at, self);
    }

    public void apply(ClassNode clazz, MethodNode node) {
        this.self.name = this.mask + "$" + this.self.name;
        clazz.methods.add(this.self);

        InsnList list = new InsnList();
        int opcode = Opcodes.INVOKESTATIC;
        if ((this.self.access & Opcodes.ACC_STATIC) == 0) {
            list.add(new VarInsnNode(Opcodes.ALOAD, 0));
            opcode = Opcodes.INVOKEVIRTUAL;
        }

        list.add(new MethodInsnNode(opcode, clazz.name, this.self.name, this.self.desc));

        switch (this.at) {
            case HEAD -> node.instructions.insert(list);
            case TAIL -> node.instructions.add(list);
            case INVOKE -> System.err.println("INVOKE is not implemented yet!");
        }
    }
}
