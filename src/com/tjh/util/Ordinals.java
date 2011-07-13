package com.tjh.util;

public enum Ordinals {
    FIRST,
    SECOND,
    THIRD,
    FOURTH,
    FIFTH,
    SIXTH,
    SEVENTH,
    EIGHTH,
    NINTH,
    TENTH;

    public static Ordinals valueOf(int i) { return values()[i-1]; }

    @Override
    public String toString() {
        return name().substring(0,1) + name().substring(1).toLowerCase();
    }
}
