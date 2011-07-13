package com.tjh.util;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

public class MethodReplacingInvocationHandler<T> implements InvocationHandler {
    private Map<Method, ? extends ThrowingBlock2<T, Object[], Object, ? extends Throwable>> replacements;
    private Object target;

    public MethodReplacingInvocationHandler(final T target,
                                            final Map<Method, ? extends ThrowingBlock2<T, Object[], Object, ? extends Throwable>>
                                                    replacements) {
        this.target = target;
        this.replacements = replacements;
    }

    public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
        final ThrowingBlock2<T, Object[], Object, ? extends Throwable> replacement = replacements.get(method);
        //noinspection unchecked
        return replacement == null ? method.invoke(target, args) : replacement.invoke((T) proxy, args);
    }
}
