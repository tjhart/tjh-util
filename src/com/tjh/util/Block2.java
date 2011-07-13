package com.tjh.util;

public interface Block2<T,U,V> {
    V invoke(T t, U u);
}
