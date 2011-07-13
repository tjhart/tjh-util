package com.tjh.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class DefaultMap<K, V> implements Map<K, V> {
    final Map<K, V> delegate;
    final V defaultValue;

    public DefaultMap(final Map<K, V> delegate, final V defaultValue){
        this.delegate = delegate;
        this.defaultValue = defaultValue;
    }

    public DefaultMap(final V defaultValue) { this(new HashMap<K, V>(), defaultValue); }

    public boolean containsValue(final Object value) {
        boolean result = false;
        if (defaultValue != null) {
            result = defaultValue.equals(value);
        }

        return result || delegate.containsValue(value);
    }

    public V get(final Object key) { return containsKey(key) ? delegate.get(key) : defaultValue; }

    public int size() { return delegate.size(); }

    public boolean isEmpty() { return delegate.isEmpty(); }

    public boolean containsKey(final Object key) { return delegate.containsKey(key); }

    public V put(final K key, final V value) { return delegate.put(key, value); }

    public V remove(final Object key) { return delegate.remove(key); }

    public void putAll(Map<? extends K, ? extends V> m) { delegate.putAll(m); }

    public void clear() { delegate.clear(); }

    public Set<K> keySet() { return delegate.keySet(); }

    public Collection<V> values() { return new ValueCollection(); }

    public Set<Entry<K, V>> entrySet() { return delegate.entrySet(); }

    class ValueCollection implements Collection<V> {
        private final Collection<V> delegateCollection = delegate.values();

        public boolean contains(final Object o) {
            return (defaultValue != null && defaultValue.equals(o)) || delegateCollection.contains(o);
        }

        public boolean containsAll(final Collection<?> c) { return combineCollections().containsAll(c); }

        //Object
        @SuppressWarnings({"EqualsWhichDoesntCheckParameterClass"})
        @Override
        public boolean equals(final Object obj) { return combineCollections().equals(obj); }

        @Override
        public int hashCode() { return combineCollections().hashCode(); }

        @Override
        public String toString() { return combineCollections().toString(); }

        private Collection<V> combineCollections() {
            final Collection<V> result = new ArrayList<V>(delegateCollection);
            if (defaultValue != null) {
                result.add(defaultValue);
            }
            return result;
        }

        @SuppressWarnings({"SuspiciousToArrayCall"})
        public <T> T[] toArray(final T[] a) { return combineCollections().toArray(a); }

        public int size() { return delegateCollection.size() + (defaultValue == null ? 0 : 1); }

        public boolean isEmpty() { return defaultValue == null && delegateCollection.isEmpty(); }

        public Iterator<V> iterator() { return new ValueCollectionIterator(); }

        public Object[] toArray() { return toArray(new Object[size()]); }

        public boolean add(final V v) { return delegateCollection.add(v); }

        public boolean remove(final Object o) { return delegateCollection.remove(o); }

        public boolean addAll(final Collection<? extends V> c) { return delegateCollection.addAll(c); }

        public boolean removeAll(final Collection<?> c) { return delegateCollection.removeAll(c); }

        public boolean retainAll(final Collection<?> c) { return delegateCollection.retainAll(c); }

        public void clear() { delegateCollection.clear(); }

        private class ValueCollectionIterator implements Iterator<V> {
            private boolean iteratedDefault = defaultValue == null;
            private final Iterator<V> delegateIterator = delegateCollection.iterator();

            public V next() {
                final V result;
                if (!delegateIterator.hasNext() && !iteratedDefault) {
                    result = defaultValue;
                    iteratedDefault = true;
                } else {
                    result = delegateIterator.next();
                }

                return result;
            }

            public boolean hasNext() { return !iteratedDefault || delegateIterator.hasNext(); }

            public void remove() { delegateIterator.remove(); }
        }
    }
}
