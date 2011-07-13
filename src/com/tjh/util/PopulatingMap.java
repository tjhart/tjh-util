package com.tjh.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class PopulatingMap<K, V> extends DefaultBlockMap<K, V> {
    public PopulatingMap(final Constructor<? extends V> constructor) {
        super(new Block2<DefaultBlockMap<K, V>, Object, V>() {
            @Override
            public V invoke(DefaultBlockMap<K, V> map, Object key) {
                try {
                    V result = constructor.newInstance();
                    map.put((K)key, result);
                    return result;
                } catch (InstantiationException e) {
                    throw new RuntimeException(e);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                } catch (InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
