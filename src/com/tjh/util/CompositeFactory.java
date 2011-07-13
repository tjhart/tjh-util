package com.tjh.util;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CompositeFactory {
    public static <T> T createComposite(final Class<T> declaredInterface, final Object... objects) {
        //noinspection unchecked
        return (T) Proxy.newProxyInstance(objects[0].getClass().getClassLoader(), new Class<?>[]{declaredInterface},
                new CompositeInvocationHandler(objects));
    }
}


class CompositeInvocationHandler implements InvocationHandler {
    private final Object[] delegates;
    final Map<Method, Object> cache = new HashMap<Method, Object>();

    public CompositeInvocationHandler(final Object... delegates) { this.delegates = delegates; }

    public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
        return method.invoke(objectFor(method), args);
    }

    Object objectFor(final Method method) throws NoSuchMethodException {
        if (!cache.keySet().contains(method)) {
            cache.put(method, findObjectFor(method));
        }

        return cache.get(method);
    }

    private Object findObjectFor(final Method method) throws NoSuchMethodException {
        final Object result = Lists.find(delegates, new Block<Object, Boolean>(){
            public Boolean invoke(final Object delegate) { return Classes.implementsMethod(delegate.getClass(), method); }
        });

        if (result == null) {
            throw new NoSuchMethodException(
                    "The method '" + method + "' was not found on any of the delegates:" + Arrays.asList(delegates));
        }
        
        return result;
    }
}