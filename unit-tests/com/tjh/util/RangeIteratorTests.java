package com.tjh.util;

import java.util.Iterator;
import java.util.NoSuchElementException;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import com.tjh.util.Range;
import org.junit.Before;
import org.junit.Test;

public class RangeIteratorTests {
    private Iterator<Integer> iterator;

    @Test
    public void hasNextReturnsTrueWhenAppropriate(){
        assertThat(iterator.hasNext(), is(true));
    }

    @Test
    public void hasNextReturnsFalseWhenAppropriate(){
        iterator.next();
        iterator.next();
        assertThat(iterator.hasNext(), is(false));
    }

    @Test
    public void nextReturnsCorrectValue(){
        assertThat(iterator.next(), equalTo(1));
        assertThat(iterator.next(), equalTo(2));
    }

    @Test(expected = NoSuchElementException.class)
    public void nextThrowsNSEWhenDone(){
        iterator.next();
        iterator.next();
        iterator.next();
    }

    @Before
    public void before(){
        iterator = new Range<Integer>(1,2).iterator();
    }
}
