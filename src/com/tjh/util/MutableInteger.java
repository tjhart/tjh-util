package com.tjh.util;

public class MutableInteger extends AbstractMutable<Integer> {
    private Integer value;

    public MutableInteger(Integer value) { this.value = value; }

    public Integer next() { return ++value; }

    public Integer previous() { return --value; }

    public Integer getValue() { return value; }
}
