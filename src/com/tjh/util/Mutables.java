package com.tjh.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public class Mutables {
    private static final Map<Class<? extends Comparable>, Class<? extends Mutable>> mutableMap =
            Maps.<Class<? extends Comparable>, Class<? extends Mutable>>asMap(
                    Integer.class, MutableInteger.class,
                    Long.class, MutableLong.class);

    public static <T extends Comparable<? super T>> Mutable<T> forValue(final T value) {
        try {
            final Class<? extends Comparable> comparableClass = value.getClass();

            //noinspection unchecked
            return ((Constructor<? extends Mutable>) ((Class<? extends Mutable>) mutableMap
                    .get(comparableClass))
                    .getDeclaredConstructor(comparableClass))
                    .newInstance(value);
        }
        catch (NoSuchMethodException e) { throw new RuntimeException(e); }
        catch (InvocationTargetException e) { throw new RuntimeException(e); }
        catch (IllegalAccessException e) { throw new RuntimeException(e); }
        catch (InstantiationException e) { throw new RuntimeException(e); }
    }
}
