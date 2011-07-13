package com.tjh.util;

public class MutableLong extends AbstractMutable<Long> {
    private Long value;

    public MutableLong(final Long value) { this.value = value; }

    public Long next() { return ++value; }

    public Long previous() { return --value; }

    public Long getValue() { return value; }
}
