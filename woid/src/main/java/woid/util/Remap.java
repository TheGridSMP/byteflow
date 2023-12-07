package woid.util;

import org.objectweb.asm.Opcodes;
import woid.clazz.ClassNode;
import woid.insn.base.AbstractInsnNode;
import woid.insn.list.InsnList;
import woid.method.MethodNode;

public class Remap {

    public static void method(MethodNode target, ClassNode sourceClass, MethodNode sourceMethod) {
        System.out.println("Method " + target.getName() + " static? " + ((target.getAccess() & Opcodes.ACC_STATIC) != 0));
        if ((target.getAccess() & Opcodes.ACC_STATIC) == 0) {
            target.getLocalVariables().get(0).setDesc(
                    "L" + sourceClass.getName() + ";"
            );
        }
    }

    public static void noReturn(MethodNode target) {
        Remap.noReturn(target.getInstructions());
    }

    public static void noReturn(InsnList list) {
        AbstractInsnNode previous = null;

        for (AbstractInsnNode node : list) {
            if (previous != null && previous.getOpcode() == Opcodes.ACONST_NULL
                    && node.getOpcode() == Opcodes.ATHROW) {
                list.remove(previous);
                list.remove(node);

                previous = null;
                continue;
            }

            previous = node;
        }
    }
}
