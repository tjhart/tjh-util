package com.tjh.util;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.InvocationHandler;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

public final class NullObjectFactory {
    private static final InvocationHandler nullInvocationHandler = new NullInvocationHandler();
    private static final Map<Class, Object> nullObjectCache = new HashMap<Class, Object>();

    @SuppressWarnings({"unchecked"})
    public static <T> T nullObject(final Class<T> someClass) {
        T result = null;
        if(nullObjectCache.keySet().contains(someClass)){
            result = (T)nullObjectCache.get(someClass);
        }
        else if(!Modifier.isFinal(someClass.getModifiers())){
            result = (T) Enhancer.create(someClass, new Class[]{NullObject.class}, nullInvocationHandler);
            nullObjectCache.put(someClass, result);
        }
        return result;
    }

    private static final class NullInvocationHandler implements InvocationHandler {
        private static final Map<Class, Object> primitiveMap = Maps.<Class, Object>asMap(
            byte.class, (byte)0,
            short.class, (short)0,
            int.class, 0,
            long.class, 0L,
            float.class, 0.0f,
            double.class, 0.0d,
            boolean.class, false,
            char.class, (char)0);

        public Object invoke(final Object target, final Method method, final Object[] arguments) throws Throwable {
            Object result;
            if ("equals".equals(method.getName())) {
                result = handleEquals(arguments);
            }
            else if("toString".equals(method.getName())){
                result = handleToString(arguments);
            }
            else {
                Class<?> type = method.getReturnType();
                if (type.isPrimitive()) {
                    result = primitiveMap.get(type);
                }
                else{
                    result = nullObject(type);
                }
            }

            return result;
        }

        private String handleToString(final Object[] arguments) {
            String result = null;
            if(arguments.length == 0){
                result = "NullObject";
            }

            return result;
        }

        private boolean handleEquals(final Object[] arguments) {
            boolean result = false;
            if (arguments.length == 1) {
                result = arguments[0] == null || arguments[0] instanceof NullObject;
            }
            return result;
        }
    }

    protected interface NullObject {
    }
}
