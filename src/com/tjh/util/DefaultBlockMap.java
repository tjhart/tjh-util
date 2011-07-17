package com.tjh.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DefaultBlockMap<K, V> implements Map<K, V>{
    private final Block2<DefaultBlockMap<K, V>, Object, V> defaultBlock;
    private final Map<K, V> delegate = new HashMap<K, V>();

    public DefaultBlockMap(Block2<DefaultBlockMap<K, V>, Object, V> block2){ this.defaultBlock = block2; }

    public V get(Object o){ return delegate.containsKey(o) ? delegate.get(o) : defaultBlock.invoke(this, o); }

    public V put(K k, V v){ return delegate.put(k, v); }

    public int size(){ return delegate.size(); }

    public boolean isEmpty(){ return delegate.isEmpty(); }

    public boolean containsKey(Object o){ return delegate.containsKey(o); }

    public boolean containsValue(Object o){ return delegate.containsValue(o); }

    public V remove(Object o){ return delegate.remove(o); }

    public void putAll(Map<? extends K, ? extends V> map){ delegate.putAll(map); }

    public void clear(){ delegate.clear(); }

    public Set<K> keySet(){ return delegate.keySet(); }

    public Collection<V> values(){ return delegate.values(); }

    public Set<Entry<K, V>> entrySet(){ return delegate.entrySet(); }
}
