package com.tjh.util;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import com.tjh.util.Maps;
import com.tjh.util.Ordinals;
import org.junit.Test;
import org.junit.experimental.theories.DataPoint;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import java.util.Map;

@RunWith(Theories.class)
public class OrdinalsTests {

    @DataPoint
    public static final Map<String, Object> FIRST = Maps.asMap("seed", (Object)1, "expected", Ordinals.FIRST);
    @DataPoint
    public static final Map<String, Object> SECOND = Maps.asMap("seed", (Object)2, "expected", Ordinals.SECOND);
    @DataPoint
    public static final Map<String, Object> THIRD = Maps.asMap("seed", (Object)3, "expected", Ordinals.THIRD);
    @DataPoint
    public static final Map<String, Object> FOURTH = Maps.asMap("seed", (Object)4, "expected", Ordinals.FOURTH);
    @DataPoint
    public static final Map<String, Object> FIFTH = Maps.asMap("seed", (Object)5, "expected", Ordinals.FIFTH);
    @DataPoint
    public static final Map<String, Object> SIXTH = Maps.asMap("seed", (Object)6, "expected", Ordinals.SIXTH);
    @DataPoint
    public static final Map<String, Object> SEVENTH = Maps.asMap("seed", (Object)7, "expected", Ordinals.SEVENTH);
    @DataPoint
    public static final Map<String, Object> EIGHTH = Maps.asMap("seed", (Object)8, "expected", Ordinals.EIGHTH);
    @DataPoint
    public static final Map<String, Object> NINTH = Maps.asMap("seed", (Object)9, "expected", Ordinals.NINTH);
    @DataPoint
    public static final Map<String, Object> TENTH = Maps.asMap("seed", (Object) 10, "expected", Ordinals.TENTH);

    @Theory
    public void valueOf(final Map<String, Object> map){
        assertThat(Ordinals.valueOf((Integer)map.get("seed")), equalTo(map.get("expected")));
    }

    @Test
    public void string(){
        assertThat(Ordinals.THIRD.toString(), equalTo("Third"));
    }
}
