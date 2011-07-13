package com.tjh.util;

abstract class AbstractMutable<T extends Comparable<? super T>> implements Mutable<T> {

    public Mutable<T> nextMutable() {
        next();
        return this;
    }

    public Mutable<T> previousMutable() {
        previous();
        return this;
    }

    abstract public T getValue();

    public int compareTo(final T t) { return getValue().compareTo(t); }
}
