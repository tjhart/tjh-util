package com.tjh.util;

import static com.tjh.util.Types.convert;

import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

public class Methods {
    public static Object invoke(final Object target, final Method targetMethod, final String[] methodArgs)
            throws InvocationTargetException, IllegalAccessException {
        final Class<?>[] types = targetMethod.getParameterTypes();
        return targetMethod.invoke(target, convert(methodArgs, types));
    }

    public static boolean couldInvokeFor(final Method method, final String[] args) {
        final int numOfArgs = method.getParameterTypes().length;
        return method.isVarArgs() ? args.length >= numOfArgs - 1 : args.length == numOfArgs;
    }
}
