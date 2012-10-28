package com.tjh.util;

import java.lang.reflect.InvocationTargetException;

public class Enums {
    public static <T extends Enum<T>> T next(T t) {
        try {
            Class enumClass = t.getClass();
            T[] values = (T[]) enumClass.getDeclaredMethod("values").invoke(enumClass);
            int nextPos = t.ordinal() + 1;
            return nextPos >= values.length ? null : values[nextPos];
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
