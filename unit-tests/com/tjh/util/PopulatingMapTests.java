package com.tjh.util;


import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class PopulatingMapTests {

    private PopulatingMap<String, Map> map;

    @Test
    public void accessingNonExistentKeysReturnsInstance(){
        assertThat(map.get("fred"), equalTo((Map)new HashMap()));
    }

    @Test
    public void subsequentAccessKeepsvalues(){
        map.get("fred").put("foo", "bar");

        assertThat(map.get("fred"), equalTo((Map)Maps.asMap("foo", "bar")));
    }

    @Before
    public void before() throws NoSuchMethodException {
        map = new PopulatingMap(HashMap.class.getConstructor());
    }
}
