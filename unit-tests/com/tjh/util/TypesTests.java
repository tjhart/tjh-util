package com.tjh.util;

import org.junit.experimental.theories.DataPoint;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(Theories.class)
public class TypesTests {
    @DataPoint
    public static final Map<String, Object> STRING = Maps.<String, Object>asMap("string", "string",
            "class", String.class,
            "expected", "string");
    @DataPoint
    public static final Map<String, Object> INTEGER = Maps.<String, Object>asMap("string", "1",
            "class", int.class,
            "expected", 1);
    @DataPoint
    public static final Map<String, Object> CHAR = Maps.<String, Object>asMap("string", "c",
            "class", char.class,
            "expected", 'c');
    @DataPoint
    public static final Map<String, Object> LONG = Maps.<String, Object>asMap("string", "12345",
            "class", long.class,
            "expected", 12345l);
    @DataPoint
    public static final Map<String, Object> FLOAT = Maps.<String, Object>asMap("string", "3.14",
            "class", float.class,
            "expected", 3.14f);
    @DataPoint
    public static final Map<String, Object> DOUBLE = Maps.<String, Object>asMap("string", "2.17",
            "class", double.class,
            "expected", 2.17d);
    @DataPoint
    public static final Map<String, Object> BOOLEAN = Maps.<String, Object>asMap("string", "true",
            "class", boolean.class,
            "expected", true);
    @DataPoint
    public static final Map<String, Object> BYTE = Maps.<String, Object>asMap("string", "1",
            "class", byte.class,
            "expected", (byte) 1);
    @DataPoint
    public static final Map<String, Object> SHORT = Maps.<String, Object>asMap("string", "9",
            "class", short.class,
            "expected", (short) 9);

    @Theory
    public void translateHandlesExpectedTypes(final Map<String, Object> map) {
        assertThat(Types.translate((String) map.get("string"), (Class<?>) map.get("class")),
                equalTo(map.get("expected")));
    }

}
