package com.tjh.util;

public interface Mutable<T extends Comparable<? super T>> extends Comparable<T> {
    T next();

    T previous();

    Mutable<T> nextMutable();

    Mutable<T> previousMutable();

    T getValue();
}
