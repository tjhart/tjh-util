package com.tjh.util;

import com.tjh.util.Block2;
import com.tjh.util.Maps;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import static com.pica.test.Matchers.containsOnly;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;

/**
 * User: thart Date: Aug 26, 2008 Time: 8:41:19 AM
 */
public class MapsTests {
    private Map<String, Map<String, Map<String, String>>> nestedMap =
            Maps.asMap("key1", Maps.asMap("key2", Maps.asMap("key3", "foo")));
    private static final String NESTED_MAP_KEYPATH = "key1.key2.key3";
    private static final String SUCCESS = "SUCCESS";
    private Map<String, Map<BigDecimal, String>> variedKeyNestedMap =
            Maps.asMap("green", Maps.asMap(BigDecimal.TEN, SUCCESS));

    @Test
    public void asMapAssociatesKeysAndValuesAppropriately() {
        final Date today = new Date();
        Object[] keys = {"key1", BigDecimal.ONE, today};
        Object[] values = {today, "value2", BigDecimal.TEN};

        Map<Object, Object> expectedMap = new HashMap<Object, Object>();
        for (int i = 0; i < keys.length; i++) {
            Object key = keys[i];
            Object value = values[i];

            expectedMap.put(key, value);
        }

        assertThat(Maps.asMap(keys[0], values[0], keys[1], values[1], keys[2], values[2]), equalTo(expectedMap));
    }

    @Test(expected = ClassCastException.class)
    public void asMapWithClassesThrowsClassCastExceptionIfKeyTypesDontMatch() {
        Maps.asMap(String.class, Object.class, "FOO", new Object(), BigDecimal.ONE, new Object());
    }

    @Test(expected = ClassCastException.class)
    public void asMapWithClassesthrowsClassCastExceptionIfValueTypesDontMatch() {
        Maps.asMap(Object.class, String.class, new Object(), "BAR", new Object(), new Date());
    }

    @Test
    public void toMapTranslatesProperly() {
        assertThat(Maps.toMap("{foo=bar, baz=blah}"), equalTo(Maps.asMap("foo", "bar", "baz", "blah")));
    }

    @Test
    public void toMapWithBlockCallsBlockForEachKey() throws Exception {
        Block2<String, String, Map.Entry<String, Object>> mockBlock = createMock(Block2.class);
        Map.Entry<String, Object> mockMapEntry = createNiceMock(Map.Entry.class);

        expect(mockBlock.invoke("stringKey", "stringVal")).andReturn(mockMapEntry);
        expect(mockBlock.invoke("intKey", "1")).andReturn(mockMapEntry);
        expect(mockBlock.invoke("bdKey", "12.345")).andReturn(mockMapEntry);

        expect(mockMapEntry.getKey()).andReturn("key").times(3);
        expect(mockMapEntry.getValue()).andReturn("value").times(3);
        replay(mockBlock, mockMapEntry);

        assertThat(Maps.toMap("{stringKey=stringVal, intKey=1, bdKey=12.345}", mockBlock),
                equalTo(Maps.<String, Object>asMap("key", "value")));
        verify(mockBlock, mockMapEntry);
    }

    @Test
    public void keysForReturnsAllKeysMappedToValue() {
        assertThat(Maps.keysFor("joe", Maps.asMap("billy", "joe", "bobby", "joe", "mary", "jane")),
                containsOnly("billy", "bobby"));
    }

    @Test
    public void asMapWithClassesUsesEnumMapIfAppropriate() {
        assertThat(Maps.asMap(TestEnum.class, String.class, TestEnum.ONE, "joe"), instanceOf(EnumMap.class));
    }

    @Test
    public void valueForFindsValue() {
        assertThat(Maps.<String>valueFor(nestedMap, NESTED_MAP_KEYPATH), equalTo("foo"));
    }

    @Test
    public void valueForHandlesSingleKey() {
        assertThat(Maps.<String>valueFor(Maps.asMap("foo", "bar"), "foo"), equalTo("bar"));
    }

    @Test
    public void valueForHandlesKeySequence() {
        assertThat(Maps.<Object, String>valueFor(variedKeyNestedMap,
                Arrays.asList("green", BigDecimal.TEN)), equalTo(SUCCESS));
    }

    @Test
    public void valueForHandlesKeyArray() {
        assertThat(Maps.<Object, String>valueFor(variedKeyNestedMap, "green", BigDecimal.TEN), equalTo(SUCCESS));
    }

    @Test
    public void putValueForReplacesValue() {
        Maps.putValueFor(nestedMap, NESTED_MAP_KEYPATH, "bar");
        assertThat(Maps.<String>valueFor(nestedMap, NESTED_MAP_KEYPATH), equalTo("bar"));
    }

    @Test
    public void putValueForReturnsOldValue() {
        assertThat(Maps.putValueFor(nestedMap, NESTED_MAP_KEYPATH, "bar"), equalTo("foo"));
    }

    @Test
    public void putValueForHandlesSingleKey() {
        final Map<String, String> map = Maps.asMap("foo", "bar");
        Maps.putValueFor(map, "foo", "baz");
        assertThat(map.get("foo"), equalTo("baz"));
    }

    @Test(expected = RuntimeException.class)
    public void asMapWithOddParmListFails() {
        Maps.asMap("Foo", "bar", "baz");
    }

    @Test(expected = RuntimeException.class)
    public void asMapWithClassesAndOddParmlistFails() {
        Maps.asMap(String.class, Integer.class, "one", 1, "two");
    }

    @Test
    public void putValueForWithKeySequenceWorks() {
        assertThat(Maps.<Object, String>putValueFor(variedKeyNestedMap, "fred", "green", BigDecimal.TEN),
                equalTo(SUCCESS));
        assertThat(Maps.<Object, String>valueFor(variedKeyNestedMap, "green", BigDecimal.TEN), equalTo("fred"));
    }

    enum TestEnum {
        ONE, TWO, THREE
    }
}
