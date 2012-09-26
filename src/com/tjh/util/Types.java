package com.tjh.util;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Utility methods for converting strings to any of the native types
 */
public class Types {
    private static final Map<Class<?>, Class<?>> WRAPPERS =
            Maps.<Class<?>, Class<?>>asMap(int.class, Integer.class,
                    char.class, Character.class,
                    long.class, Long.class,
                    float.class, Float.class,
                    double.class, Double.class,
                    boolean.class, Boolean.class,
                    byte.class, Byte.class,
                    short.class, Short.class);

    public static Object[] convert(final String[] strings, final Class<?>[] classes) {
        final Object[] result = new Object[classes.length];
        int i = 0;
        for (final Class<?> aClass : classes) {
            if (aClass.isArray()) {
                final List<Object> arrayArgs = new ArrayList<Object>();
                int j = i;
                while (j < strings.length) {
                    arrayArgs.add(translate(strings[j++], aClass.getComponentType()));
                }
                result[i] =
                        arrayArgs.toArray((Object[]) Array.newInstance(aClass.getComponentType(), arrayArgs.size()));
            } else {
                result[i] = translate(strings[i], aClass);
            }
            i++;
        }

        return result;
    }

    public static <T> T translate(final String string, final Class<? extends T> aClass) {
        final T result;
        if (String.class.equals(aClass)) {
            result = (T) string;
        } else {
            try {
                result = (T) getTranslatingMethod(aClass).invoke(null, string);
            } catch (IllegalAccessException e) { throw new RuntimeException(e); } catch (InvocationTargetException e) {
                throw new RuntimeException(e.getCause());
            }
        }
        return result;
    }

    private static Method getTranslatingMethod(Class<?> aClass) {
        if (aClass.isPrimitive()) {
            aClass = WRAPPERS.get(aClass);
        }
        try {
            final Method result;
            if (Character.class.equals(aClass)) {
                result = Types.class.getDeclaredMethod("getChar", String.class);
            } else {
                result = aClass.getMethod("valueOf", String.class);
            }
            return result;
        } catch (NoSuchMethodException e) { throw new RuntimeException(e); }
    }

    protected static char getChar(final String string) { return string.toCharArray()[0]; }
}
