package com.tjh.util;

public interface ThrowingBlock2<T, U, V, W extends Throwable> {
    public V invoke(T t, U u) throws W;
}
