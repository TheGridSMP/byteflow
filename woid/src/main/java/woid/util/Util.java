package woid.util;

import org.objectweb.asm.Opcodes;

import java.util.Arrays;

public class Util {

    public static String addToDesc(String desc, String clazz) {
        if (clazz.length() > 1 && !clazz.startsWith("L")) {
            clazz = "L" + clazz + ";";
        }

        return "(" + clazz + desc.substring(1);
    }

    public static String removeFromDesc(String desc, String other) {
        if (other.length() > 1 && !other.startsWith("L")) {
            other = "L" + other + ";";
        }

        return desc.replaceFirst(other, "");
    }

    public static boolean isReturn(int opcode) {
        return opcode == Opcodes.RETURN || opcode == Opcodes.ARETURN || opcode == Opcodes.IRETURN
                || opcode == Opcodes.LRETURN || opcode == Opcodes.FRETURN || opcode == Opcodes.DRETURN;
    }

    public static Object tryBoxList(Object object) {
        Object[] boxed = Util.boxArray(object);
        return boxed != null ? Arrays.asList(boxed) : object;
    }

    public static Object[] boxArray(Object object) {
        if (object instanceof byte[] bytes) {
            return Util.toObject(bytes);
        }

        if (object instanceof boolean[] bools) {
            return Util.toObject(bools);
        }

        if (object instanceof short[] shorts) {
            return Util.toObject(shorts);
        }

        if (object instanceof char[] chars) {
            return Util.toObject(chars);
        }

        if (object instanceof int[] ints) {
            return Util.toObject(ints);
        }

        if (object instanceof long[] longs) {
            return Util.toObject(longs);
        }

        if (object instanceof float[] floats) {
            return Util.toObject(floats);
        }

        if (object instanceof double[] doubles) {
            return Util.toObject(doubles);
        }

        return null;
    }

    public static Boolean[] toObject(boolean[] array) {
        if (array == null) {
            return null;
        }

        if (array.length == 0) {
            return new Boolean[0];
        }

        Boolean[] result = new Boolean[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = array[i];
        }

        return result;
    }

    public static Byte[] toObject(byte[] array) {
        if (array == null) {
            return null;
        }

        if (array.length == 0) {
            return new Byte[0];
        }

        Byte[] result = new Byte[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = array[i];
        }

        return result;
    }

    public static Character[] toObject(char[] array) {
        if (array == null) {
            return null;
        }

        if (array.length == 0) {
            return new Character[0];
        }

        Character[] result = new Character[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = array[i];
        }

        return result;
    }

    public static Double[] toObject(double[] array) {
        if (array == null) {
            return null;
        }

        if (array.length == 0) {
            return new Double[0];
        }

        Double[] result = new Double[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = array[i];
        }

        return result;
    }

    public static Float[] toObject(float[] array) {
        if (array == null) {
            return null;
        }

        if (array.length == 0) {
            return new Float[0];
        }

        Float[] result = new Float[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = array[i];
        }

        return result;
    }

    public static Integer[] toObject(int[] array) {
        if (array == null) {
            return null;
        }

        if (array.length == 0) {
            return new Integer[0];
        }

        Integer[] result = new Integer[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = array[i];
        }

        return result;
    }

    public static Long[] toObject(long[] array) {
        if (array == null) {
            return null;
        }

        if (array.length == 0) {
            return new Long[0];
        }

        Long[] result = new Long[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = array[i];
        }

        return result;
    }

    public static Short[] toObject(short[] array) {
        if (array == null) {
            return null;
        }

        if (array.length == 0) {
            return new Short[0];
        }

        Short[] result = new Short[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = array[i];
        }

        return result;
    }
}
