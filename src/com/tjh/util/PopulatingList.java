package com.tjh.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class PopulatingList<T> implements List<T> {
    private final List<T> delegate = new LinkedList<T>();
    private final Block2<PopulatingList<T>, Integer, T> populator;

    public PopulatingList(){
        this(new Block2<PopulatingList<T>, Integer, T>() {
            @Override
            public T invoke(PopulatingList<T> ts, Integer integer) { return null; }
        });
    }
    public PopulatingList(final Class<? extends T> typeClass) {
        this(new Block2<PopulatingList<T>, Integer, T>() {
            @Override
            public T invoke(PopulatingList<T> list, Integer integer) {
                try {
                    return typeClass.newInstance();
                }
                catch (InstantiationException e) { throw new RuntimeException(e); }
                catch (IllegalAccessException e) { throw new RuntimeException(e); }
            }
        });
    }

    public PopulatingList(final Block2<PopulatingList<T>, Integer, T> block){this.populator = block; }

    public T get(int i) {
        fill(i);
        return delegate.get(i);
    }

    @Override
    public T set(int i, T t) {
        fill(i);
        return delegate.set(i, t);
    }

    private void fill(int i) {for(int j = size(); j <= i; j++) add(populator.invoke(this, j));}

    @Override
    public int size() { return delegate.size(); }

    @Override
    public boolean isEmpty() { return delegate.isEmpty(); }

    @Override
    public boolean contains(Object o) { return delegate.contains(o); }

    @Override
    public Iterator<T> iterator() { return delegate.iterator(); }

    @Override
    public Object[] toArray() { return delegate.toArray(); }

    @SuppressWarnings({"SuspiciousToArrayCall"})
    @Override
    public <T> T[] toArray(T[] ts) { return delegate.toArray(ts); }

    @Override
    public boolean add(T t) { return delegate.add(t); }

    @Override
    public boolean remove(Object o) { return delegate.remove(o); }

    @Override
    public boolean containsAll(Collection<?> objects) { return delegate.containsAll(objects); }

    @Override
    public boolean addAll(Collection<? extends T> ts) { return delegate.addAll(ts); }

    @Override
    public boolean addAll(int i, Collection<? extends T> ts) { return delegate.addAll(i, ts); }

    @Override
    public boolean removeAll(Collection<?> objects) { return delegate.removeAll(objects); }

    @Override
    public boolean retainAll(Collection<?> objects) { return delegate.retainAll(objects); }

    @Override
    public void clear() { delegate.clear(); }

    @Override
    public void add(int i, T t) { delegate.add(i, t); }

    @Override
    public T remove(int i) { return delegate.remove(i); }

    @Override
    public int indexOf(Object o) { return delegate.indexOf(o); }

    @Override
    public int lastIndexOf(Object o) { return delegate.lastIndexOf(o); }

    @Override
    public ListIterator<T> listIterator() { return delegate.listIterator(); }

    @Override
    public ListIterator<T> listIterator(int i) { return delegate.listIterator(i); }

    @Override
    public List<T> subList(int i, int j) { return delegate.subList(i, j); }
}
