package com.tjh.util;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

import static com.tjh.test.Matchers.containsOnly;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class DefaultingMapValuesTests {
    private static final String DEFAULT_VALUE = "foo";
    private DefaultMap<String, String> map;

    @Test
    public void initialValuesSizeIsOne() {
        assertThat(map.values().size(), equalTo(1));
    }

    @Test
    public void initialValuesSizeIsZeroIfDefaultIsNull() {
        assertThat(new DefaultMap<String, Map>(null).values().size(), equalTo(0));
    }

    @Test
    public void valuesSizeReflectsMapChange() {
        Map<String, Object> map = new DefaultMap<String, Object>("null");
        Collection<Object> values = map.values();
        map.put(DEFAULT_VALUE, "bar");

        assertThat(values.size(), equalTo(2));
    }

    @Test
    public void isEmptyReturnsFalseIfDefault() {
        assertThat(map.values().isEmpty(), is(false));
    }

    @Test
    public void isEmptyReturnsTrueWithNullDefault() {
        assertThat(new DefaultMap<String, String>(null).values().isEmpty(), is(true));
    }

    @Test
    public void valuesContainsReturnsTrueForDefaultValue() {
        assertThat(map.values().contains(DEFAULT_VALUE), is(true));
    }

    @Test
    public void valuesContainsReturnsFalseForNullDefault() {
        assertThat(new DefaultMap<String, String>(null).values().contains("fred"), is(false));
    }

    @Test
    public void valuesContainsReturnsTrueIfMapValuesContains() {
        Collection<String> values = map.values();
        map.put("blue", "tooth");
        assertThat(values.contains("tooth"), is(true));
    }

    @Test
    public void toArrayContainsDefault() {
        assertThat(map.values().toArray(), equalTo(new Object[]{DEFAULT_VALUE}));
    }

    @Test
    public void toArrayDoesNotContainNullDefault() {
        assertThat(new DefaultMap<String, String>(null).values().toArray(), equalTo(new Object[]{}));
    }

    @Test
    public void toArrayFillsProvidedArray() {
        final String[] strings = new String[1];
        map.values().toArray(strings);

        assertThat(strings[0], equalTo(DEFAULT_VALUE));
    }

    @Test
    public void toArrayIncludesDelegateValues() {
        final String[] strings = new String[2];
        map.put("yay", "boo");
        map.values().toArray(strings);

        assertThat(strings, equalTo(new String[]{"boo", DEFAULT_VALUE}));
    }

    @Test
    public void toArrayProvidesNewArrayIfArrayIsTooSmall() {
        map.put("hoo", "rah");

        //noinspection ToArrayCallWithZeroLengthArrayArgument
        assertThat(map.values().toArray(new String[0]), equalTo(new String[]{"rah", DEFAULT_VALUE}));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void addIsNotSupported() {
        map.values().add("fred");
    }

    @Test
    public void removeWorksForMapValues() {
        map.put("rah", "blah");
        map.values().remove("blah");

        assertThat(map.get("rah"), equalTo(DEFAULT_VALUE));
    }

    @Test
    public void removeDefaultValueDoesNothing() {
        map.values().remove(DEFAULT_VALUE);

        assertThat(map.get("nah"), equalTo(DEFAULT_VALUE));
    }

    @Test
    public void containsAllConsidersDefaultValue() {
        assertThat(map.values().containsAll(Arrays.asList(DEFAULT_VALUE)), is(true));
    }

    @Test
    public void containsAllConsidersMapValues() {
        map.put("hoop", "dee");
        assertThat(map.values().containsAll(Arrays.asList(DEFAULT_VALUE, "dee")), is(true));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void addAllIsNotSupported() {
        map.values().addAll(Arrays.asList("boo"));
    }

    @Test
    public void removeAllWorks() {
        map.put("dah", "dah");
        map.put("foo", "bar");
        map.values().removeAll(Arrays.asList("dah", "bar"));

        assertThat(map.get("dah"), equalTo(DEFAULT_VALUE));
        assertThat(map.get("foo"), equalTo(DEFAULT_VALUE));
    }

    @Test
    public void retainAllWorks() {
        map.put("dah", "dah");
        map.put("foo", "bar");
        map.values().retainAll(Arrays.asList("bar"));

        assertThat(map.get("dah"), equalTo(DEFAULT_VALUE));
        assertThat(map.get("foo"), equalTo("bar"));
    }

    @Test
    public void clearWorks() {
        map.put("thin", "hair");
        map.values().clear();

        assertThat(map.get("thin"), equalTo(DEFAULT_VALUE));
    }

    @Test
    public void iteratorHasNextConsidersDefault() {
        assertThat(map.values().iterator().hasNext(), is(true));
    }

    @Test
    public void iteratorIteratesDefault() {
        Iterator<String> iter = map.values().iterator();
        assertThat(iter.next(), equalTo(DEFAULT_VALUE));
    }

    @Test
    public void iteratorStopsAfterDefault() {
        Iterator<String> iter = map.values().iterator();
        iter.next();
        assertThat(iter.hasNext(), is(false));
    }

    @Test
    public void iteratorWorksWithoutDefault() {
        map = new DefaultMap<String, String>(null);
        map.put("nice", "try");
        Iterator<String> iter = map.values().iterator();

        assertThat(iter.next(), equalTo("try"));
        assertThat(iter.hasNext(), is(false));
    }

    @Test(expected = NoSuchElementException.class)
    public void iteratorThrowsRightWithoutDefault() {
        map = new DefaultMap<String, String>(null);
        map.put("nice", "try");
        Iterator<String> iter = map.values().iterator();

        iter.next();
        iter.next();
    }

    @Test(expected = NoSuchElementException.class)
    public void iteratorThrowsWhenDone() {
        Iterator<String> iter = map.values().iterator();
        iter.next();
        iter.next();
    }

    @Test
    public void iteratorIncludesMapValues() {
        map.put("one", "two");
        Iterator<String> iter = map.values().iterator();

        assertThat(iter.next(), equalTo("two"));
        assertThat(iter.next(), equalTo(DEFAULT_VALUE));
        assertThat(iter.hasNext(), is(false));
    }

    @Test
    public void iterRemoveWorksForMapValues() {
        map.put("one", "two");
        Iterator<String> iter = map.values().iterator();

        iter.next();
        iter.remove();
        assertThat(map.get("one"), equalTo(DEFAULT_VALUE));
    }

    @Test
    public void valuesIncludesDefault() {
        assertThat(map.values(), containsOnly(DEFAULT_VALUE));
    }

    @Test
    public void valuesIncludesRealValues() {
        Map<Character, String> map = new DefaultMap<Character, String>(" ");
        map.put('X', "X");
        assertThat(map.values(), containsOnly(" ", "X"));
    }

    @Before
    public void before() {
        map = new DefaultMap<String, String>(DEFAULT_VALUE);
    }
}
