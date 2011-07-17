package com.tjh.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class PopulatingMap<K, V> extends DefaultBlockMap<K, V> {
    public PopulatingMap(final Class<? extends V> type) {
        this(new Block2<DefaultBlockMap<K, V>, Object, V>() {
            @Override
            public V invoke(DefaultBlockMap<K, V> map, Object key) {
                try {
                    return type.newInstance();
                }
                catch (InstantiationException e) { throw new RuntimeException(e); }
                catch (IllegalAccessException e) { throw new RuntimeException(e); }
            }
        });
    }

    public PopulatingMap(final Constructor<? extends V> constructor) {
        this(new Block2<DefaultBlockMap<K, V>, Object, V>() {
            @Override
            public V invoke(DefaultBlockMap<K, V> map, Object key) {
                try {
                    return constructor.newInstance();
                }
                catch (InstantiationException e) { throw new RuntimeException(e); }
                catch (IllegalAccessException e) { throw new RuntimeException(e); }
                catch (InvocationTargetException e) { throw new RuntimeException(e); }
            }
        });
    }

    public PopulatingMap(final Block2<DefaultBlockMap<K, V>, Object, V> block){
        super(new Block2<DefaultBlockMap<K, V>, Object, V>() {
            @Override
            public V invoke(DefaultBlockMap<K, V> map, Object o) {
                V result = block.invoke(map, o);
                map.put((K) o, result);
                return result;
            }
        });
    }
}
