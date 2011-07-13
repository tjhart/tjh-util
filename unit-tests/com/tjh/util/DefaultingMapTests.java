package com.tjh.util;

import com.tjh.util.DefaultMap;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class DefaultingMapTests {
    private static final String DEFAULT_VALUE = "foo";
    private DefaultMap<String, String> map;

    @Test
    public void defaultConstructorWorks() {
        assertThat(new DefaultMap<String, String>("fred").get(DEFAULT_VALUE), equalTo("fred"));
    }

    @Test
    public void prefersRealValues() {
        Map<String, Integer> map = new DefaultMap<String, Integer>(9);
        map.put("three", 3);
        assertThat(map.get("three"), equalTo(3));
    }

    @Test
    public void returnsNullCorrectly() {
        Map<Integer, String> map = new DefaultMap<Integer, String>("absent");
        map.put(0, null);
        assertThat(map.get(0), nullValue());
    }

    @Test
    public void handlesNullKeys() {
        map.put(null, "woohoo!");
        assertThat(map.get(null), equalTo("woohoo!"));
    }

    @Test
    public void containsValueIdentifiesDefault() {
        assertThat(map.containsValue(DEFAULT_VALUE), is(true));
    }

    @Test
    public void containsValueIdentifiesRealValue() {
        Map<String, Number> map = new DefaultMap<String, Number>(3.3f);
        map.put("2.2", 2.2d);
        assertThat(map.containsValue(2.2d), is(true));
    }

    @Test
    public void containsValueReturnsFalseCorrectly() {
        assertThat(new DefaultMap<String, Number>(8l).containsValue(1), is(false));
    }

    @Before
    public void before() {
        map = new DefaultMap<String, String>(DEFAULT_VALUE);
    }
}
