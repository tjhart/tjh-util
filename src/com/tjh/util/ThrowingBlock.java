package com.tjh.util;

public interface ThrowingBlock<T, U, V extends Throwable> {
    public U invoke(T t) throws V;
}