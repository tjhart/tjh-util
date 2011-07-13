package com.tjh.util;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class DefaultBlockMapTests {

    private DefaultBlockMap<String, Integer> map;
    private boolean invoked;

    @Test
    public void executesBlockWhenKeyNotFound() {
        assertThat(map.get("fred"), equalTo(3));
        assertThat(invoked, is(true));
    }

    @Test
    public void sizeInitializesToZero() { assertThat(map.size(), equalTo(0)); }

    @Test
    public void putAndGetBehaveAsExpected() {
        map.put("two", 2);
        assertThat(map.get("two"), equalTo(2));
    }

    @Before
    public void before() {
        invoked = false;

        map = new DefaultBlockMap<String, Integer>(
                new Block2<DefaultBlockMap<String, Integer>, Object, Integer>() {


                    @Override
                    public Integer invoke(DefaultBlockMap<String, Integer> map, Object o) {
                        invoked = true;
                        return 3;
                    }
                });

    }
}
