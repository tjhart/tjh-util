package com.tjh.util;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tjh.test.Matchers.containsOnly;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * User: thart Date: Aug 26, 2008 Time: 8:41:19 AM
 */
public class MapsTests{
    private Map<String, Map<String, Map<String, String>>> nestedMap =
            Maps.asMap("key1", Maps.asMap("key2", Maps.asMap("key3", "foo")));
    private static final String NESTED_MAP_KEYPATH = "key1.key2.key3";
    private static final String SUCCESS = "SUCCESS";
    private Map<String, Map<BigDecimal, String>> variedKeyNestedMap =
            Maps.asMap("green", Maps.asMap(BigDecimal.TEN, SUCCESS));
    private Map<String, String> basicMap;
    private Block2<String, String, Object> mockBlock2;

    @Test
    public void asMapAssociatesKeysAndValuesAppropriately(){
        final Date today = new Date();
        Object[] keys = {"key1", BigDecimal.ONE, today};
        Object[] values = {today, "value2", BigDecimal.TEN};

        Map<Object, Object> expectedMap = new HashMap<Object, Object>();
        for(int i = 0; i < keys.length; i++){
            Object key = keys[i];
            Object value = values[i];

            expectedMap.put(key, value);
        }

        assertThat(Maps.asMap(keys[0], values[0], keys[1], values[1], keys[2], values[2]), equalTo(expectedMap));
    }

    @Test(expected = ClassCastException.class)
    public void asMapWithClassesThrowsClassCastExceptionIfKeyTypesDontMatch(){
        Maps.asMap(String.class, Object.class, "FOO", new Object(), BigDecimal.ONE, new Object());
    }

    @Test(expected = ClassCastException.class)
    public void asMapWithClassesthrowsClassCastExceptionIfValueTypesDontMatch(){
        Maps.asMap(Object.class, String.class, new Object(), "BAR", new Object(), new Date());
    }

    @Test
    public void toMapTranslatesProperly(){
        assertThat(Maps.toMap("{foo=bar, baz=blah}"), equalTo(Maps.asMap("foo", "bar", "baz", "blah")));
    }

    @Test
    public void toMapWithBlockCallsBlockForEachKey() throws Exception{
        Block2<String, String, Map.Entry<String, Object>> mockBlock = mock(Block2.class);
        Map.Entry<String, Object> mockMapEntry = mock(Map.Entry.class);

        when(mockBlock.invoke("stringKey", "stringVal")).thenReturn(mockMapEntry);
        when(mockBlock.invoke("intKey", "1")).thenReturn(mockMapEntry);
        when(mockBlock.invoke("bdKey", "12.345")).thenReturn(mockMapEntry);

        when(mockMapEntry.getKey()).thenReturn("key");
        when(mockMapEntry.getValue()).thenReturn("value");

        assertThat(Maps.toMap("{stringKey=stringVal, intKey=1, bdKey=12.345}", mockBlock),
                equalTo(Maps.<String, Object>asMap("key", "value")));

        verify(mockBlock).invoke("stringKey", "stringVal");
        verify(mockBlock).invoke("intKey", "1");
        verify(mockBlock).invoke("bdKey", "12.345");

        verify(mockMapEntry, times(3)).getKey();
        verify(mockMapEntry, times(3)).getValue();
    }

    @Test
    public void keysForReturnsAllKeysMappedToValue(){
        assertThat(Maps.keysFor("joe", Maps.asMap("billy", "joe", "bobby", "joe", "mary", "jane")),
                containsOnly("billy", "bobby"));
    }

    @Test
    public void asMapWithClassesUsesEnumMapIfAppropriate(){
        assertThat(Maps.asMap(TestEnum.class, String.class, TestEnum.ONE, "joe"), instanceOf(EnumMap.class));
    }

    @Test
    public void valueForFindsValue(){
        assertThat(Maps.<String>valueFor(nestedMap, NESTED_MAP_KEYPATH), equalTo("foo"));
    }

    @Test
    public void valueForHandlesSingleKey(){
        assertThat(Maps.<String>valueFor(Maps.asMap("foo", "bar"), "foo"), equalTo("bar"));
    }

    @Test
    public void valueForHandlesKeySequence(){
        assertThat(Maps.<Object, String>valueFor(variedKeyNestedMap,
                Arrays.asList("green", BigDecimal.TEN)), equalTo(SUCCESS));
    }

    @Test
    public void valueForHandlesKeyArray(){
        assertThat(Maps.<Object, String>valueFor(variedKeyNestedMap, "green", BigDecimal.TEN), equalTo(SUCCESS));
    }

    @Test
    public void putValueForReplacesValue(){
        Maps.putValueFor(nestedMap, NESTED_MAP_KEYPATH, "bar");
        assertThat(Maps.<String>valueFor(nestedMap, NESTED_MAP_KEYPATH), equalTo("bar"));
    }

    @Test
    public void putValueForReturnsOldValue(){
        assertThat(Maps.putValueFor(nestedMap, NESTED_MAP_KEYPATH, "bar"), equalTo("foo"));
    }

    @Test
    public void putValueForHandlesSingleKey(){
        final Map<String, String> map = Maps.asMap("foo", "bar");
        Maps.putValueFor(map, "foo", "baz");
        assertThat(map.get("foo"), equalTo("baz"));
    }

    @Test(expected = RuntimeException.class)
    public void asMapWithOddParmListFails(){
        Maps.asMap("Foo", "bar", "baz");
    }

    @Test(expected = RuntimeException.class)
    public void asMapWithClassesAndOddParmlistFails(){
        Maps.asMap(String.class, Integer.class, "one", 1, "two");
    }

    @Test
    public void putValueForWithKeySequenceWorks(){
        assertThat(Maps.<Object, String>putValueFor(variedKeyNestedMap, "fred", "green", BigDecimal.TEN),
                equalTo(SUCCESS));
        assertThat(Maps.<Object, String>valueFor(variedKeyNestedMap, "green", BigDecimal.TEN), equalTo("fred"));
    }

    @Test
    public void eachPairInvokesBlockForEachPair(){

        Maps.eachPair(basicMap, mockBlock2);

        verify(mockBlock2).invoke("one", "ONE");
        verify(mockBlock2).invoke("two", "TWO");
    }

    @Test
    public void deleteIfRemovesKeysForWhichBlockReturnsTrue(){

        Maps.deleteIf(basicMap, new Block2<String, String, Boolean>(){
            public Boolean invoke(String key, String value){ return "one".equals(key); }
        });

        assertThat(basicMap, equalTo(Maps.asMap("two", "TWO")));
    }

    @Test
    public void eachKeyInvokesBlockForEachKey(){
        Block<String, ?> mockBlock = mock(Block.class);
        Maps.eachKey(basicMap, mockBlock);

        verify(mockBlock).invoke("one");
        verify(mockBlock).invoke("two");
    }

    @Test
    public void eachValueInvokesBlockForEachValue(){
        Block<String, ?> mockBlock = mock(Block.class);
        Maps.eachValue(basicMap, mockBlock);

        verify(mockBlock).invoke("ONE");
        verify(mockBlock).invoke("TWO");
    }

    @Test
    public void flattenReturnsList(){
        List<String> list = Maps.flatten(basicMap);

        assertThat(Sets.asSet(list.subList(0, 2), list.subList(2, 4)), containsOnly(Arrays.asList("one", "ONE"),
                Arrays.asList("two", "TWO")));
    }

    @Test
    public void invertWorks(){
        assertThat(Maps.invert(basicMap), equalTo(Maps.asMap("ONE", "one", "TWO", "two")));
    }

    @Test
    public void keepIfOnlyKeepsKeysForWhichBlockReturnsTrue(){
        Maps.keepIf(basicMap, new Block2<String, String, Boolean>(){
            public Boolean invoke(String key, String value){ return "one".equals(key); }
        });

        assertThat(basicMap, equalTo(Maps.asMap("one", "ONE")));
    }

    @Test
    public void rejectIsLikeDeleteIfButLeavesOriginalMapIntact(){
        Map<String, String> result =
                Maps.reject(Collections.unmodifiableMap(basicMap), new Block2<String, String, Boolean>(){
                    public Boolean invoke(String key, String value){ return "two".equals(key); }
                });

        assertThat(result, equalTo(Maps.asMap("one", "ONE")));
    }

    @Test
    public void selectIsLikeKeepIfButLeavesOriginalMapIntact(){
        Map<String, String> result =
                Maps.select(Collections.unmodifiableMap(basicMap), new Block2<String, String, Boolean>(){
                    public Boolean invoke(String key, String value){ return "two".equals(key); }
                });
        assertThat(result, equalTo(Maps.asMap("two", "TWO")));
    }

    @Test
    public void valuesForReturnsValuesForAllKeypaths(){
        assertThat(Maps.valuesFor(basicMap, "one", "two"), containsOnly("ONE", "TWO"));
    }

    @Test
    public void mergeCombinesAllMaps(){
        assertThat(Maps.merge(basicMap, Maps.asMap("three", "THREE", "four", "FOUR")),
                equalTo(Maps.asMap("one", "ONE", "two", "TWO", "three", "THREE", "four", "FOUR")));
    }

    @Before
    public void before(){
        basicMap = Maps.asMap("one", "ONE", "two", "TWO");
        mockBlock2 = mock(Block2.class);
    }

    enum TestEnum{
        ONE,
        TWO,
        THREE
    }
}
