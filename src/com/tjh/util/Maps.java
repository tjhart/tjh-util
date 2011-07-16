package com.tjh.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User: thart Date: Aug 23, 2008 Time: 3:27:46 PM
 */
@SuppressWarnings({"UnusedDeclaration"})
public class Maps {
    private static final Pattern MAP_PATTERN = Pattern.compile("^\\{\\s*(.*)}$");
    private static final Pattern ENTRY_PATTERN = Pattern.compile("([^=]+)=([^,\\s]+),?\\s*");

    /**
     * Convenience method for creating maps. <b>Note that you lose all type safety beyond the first two parameters.</b>
     * That is, if you intend for your map to be of type <code>Map&lt;String, Integer&gt;</code>,
     * but call it like this:
     * <p><code> Map&lt;String, Integer&gt; myMap = Maps.asMap("string", 3, "string2", new Foo(), new Date(),
     * Object.class); </code></p> <p/> You will not receive any compile time failures. You will only receive runtime
     * failures if you cast and/or assign returned keys or values incorrectly: <p><code> <br> for(Object key :
     * myMap.keyset()); //ok <br> for(String key : myMap.keyset()); //will throw ClassCastException when 'new Date()'
     * is
     * accessed <br><br> Object foo = myMap.get("string");//ok <br> Integer bar = myMap.get("string2");//with throw
     * ClassCastException </code></p>
     * <p/>
     * For a more typesafe call, use {@link #asMap(Class, Class, Object...)}
     *
     * @param key1   The first key
     * @param value1 The first value
     * @param others subsequent keys and values
     * @param <K>    The key type
     * @param <V>    the value type
     * @return a Map initialized with the keys and values passed in
     */
    public static <K, V> Map<K, V> asMap(final K key1, final V value1, final Object... others) {
        final Map<K, V> result = new HashMap<K, V>((others.length + 1) / 2);
        return fillMap(result, key1, value1, others);
    }

    /**
     * Like {@link #asMap(Object, Object, Object...)}, but customized for better behavior with Enum keys
     *
     * @param key1   The first key
     * @param value1 The first value
     * @param others subsequent keys and values
     * @param <K>    The key type
     * @param <V>    the value type
     * @return a Map initialized with the keys and values passed in
     * @see #asMap(Object, Object, Object...)
     */
    public static <K extends Enum<K>, V> Map<K, V> asMap(final K key1, final V value1, final Object... others) {
        @SuppressWarnings({"unchecked"})
        final Map<K, V> result = new EnumMap<K, V>((Class<K>) key1.getClass());
        return fillMap(result, key1, value1, others);
    }

    @SuppressWarnings({"unchecked"})
    private static <K, V> Map<K, V> fillMap(final Map<K, V> result, final K key1, final V value1,
                                            final Object... others) {
        result.put(key1, value1);
        for (int i = 0; i < others.length; i++) {
            result.put((K) others[i], (V) others[++i]);
        }

        return result;
    }

    /**
     * Convenience method for creating maps. More typesafe than {@link #asMap(Object, Object, Object...)} since you
     * provide types themselves as the first two parameters. This version of <code>asMap</code> will verify that all
     * arguments can be cast to the appropriate types via {@link Class#cast}. Compile issues are still possible, but
     * run-time problems are discovered at the call to 'asMap', instead of further away where values are used
     *
     * @param keyType       key type for the map
     * @param valueType     value type for the map
     * @param keysAndValues keys and values for the map
     * @param <K>           The key type
     * @param <V>           the value type
     * @return A Map initialized with <code>keysAndValues</code>
     */
    @SuppressWarnings({"unchecked"})
    public static <K, V> Map<K, V> asMap(final Class<K> keyType, final Class<V> valueType,
                                         final Object... keysAndValues) {
        final Map<K, V> result;
        if (keyType.isEnum()) {
            result = new EnumMap(keyType);
        } else {
            result = new HashMap<K, V>(keysAndValues.length / 2);
        }

        for (int i = 0; i < keysAndValues.length; i++) {
            final Object key = keysAndValues[i];
            final Object value = keysAndValues[++i];
            result.put(keyType.cast(key), valueType.cast(value));
        }
        return result;
    }

    /**
     * Takes a string of the form <code>"{key=value, ...}"</code> and coverts it to a map
     *
     * @param mapString The String to convert
     * @return A Map
     */
    public static Map<String, String> toMap(final String mapString) {
        return toMap(mapString, new Block2<String, String, Map.Entry<String, String>>() {
            public Map.Entry<String, String> invoke(final String key, final String value) {
                return new Map.Entry<String, String>() {
                    public String getKey() { return key; }

                    public String getValue() { return value; }

                    public String setValue(final String value) { throw new UnsupportedOperationException(); }
                };
            }
        });
    }

    /**
     * Takes a string of the form "{key=value, ...}" and converts it to a map, calling <code>converter</code> for each
     * of the entries.
     * <p/>
     * The converter accepts the key and value as strings, and returns a Map.Entry<K, V> instance
     *
     * @param mapString The string to convert
     * @param converter The block to call for each map entry
     * @param <K>       Key type for the result Map
     * @param <V>       Value type for the result map
     * @return A Map populated with the results of the calls to <code>converter</code>
     */
    public static <K, V> Map<K, V> toMap(final String mapString,
                                         final Block2<String, String, ? extends Map.Entry<K, V>> converter) {
        final Map<K, V> result = new HashMap<K, V>();

        final Matcher mapMatcher = MAP_PATTERN.matcher(mapString);
        if (mapMatcher.matches()) {
            final Matcher entryMatcher = ENTRY_PATTERN.matcher(mapMatcher.group(1));
            while (entryMatcher.find()) {
                final Map.Entry<K, V> entry = converter.invoke(entryMatcher.group(1), entryMatcher.group(2));
                result.put(entry.getKey(), entry.getValue());
            }
        }

        return result;
    }

    /**
     * Returns all of the keys in <code>map</code> that are associated with <code>value</code>
     *
     * @param value The value to search for
     * @param map   The map to search
     * @param <K>   Key type
     * @param <T>   value type
     * @return The collection of keys associated with <code>value</code>
     */
    public static <K, T> Collection<K> keysFor(final T value, final Map<? extends K, ? extends T> map) {
        final List<K> keyList = new ArrayList<K>();

        for (final Map.Entry<? extends K, ? extends T> entry : map.entrySet()) {
            if (entry.getValue().equals(value)) {
                keyList.add(entry.getKey());
            }
        }

        return keyList;
    }

    /**
     * Like {@link #asMap(Object, Object, Object...)}, but returns an unmodifiable map
     *
     * @param key1   The first key
     * @param value1 The first value
     * @param others subsequent keys and values
     * @param <K>    The key type
     * @param <V>    the value type
     * @return an unmodifiable map initialized with the keys and values passed in
     * @see #asMap(Object, Object, Object...)
     */
    public static <K, V> Map<K, V> asConstantMap(final K key1, final V value1, final Object... others) {
        return Collections.unmodifiableMap(asMap(key1, value1, others));
    }

    /**
     * Like {@link #asMap(Class, Class, Object...)}, but returns an unmodifiable map
     *
     * @param keyType       key type for the map
     * @param valueType     value type for the map
     * @param keysAndValues keys and values for the map
     * @param <K>           The key type
     * @param <V>           the value type
     * @return an unmodifiable map initialized with the keys and values passed in
     * @see #asMap(Class, Class, Object...)
     */
    public static <K, V> Map<K, V> asConstantMap(final Class<? extends K> keyType, final Class<? extends V> valueType,
                                                 final Object... keysAndValues) {
        return Collections.unmodifiableMap(asMap(keyType, valueType, keysAndValues));
    }

    /**
     * Convenience method for navigating nested maps. This method takes a Map of nested maps. Each map in the chain to
     * be navigated must use strings for the key values.
     * <p/>
     * The keypath should be of the form "key1.key2.key3". The last returned object is of type <V>
     *
     * @param map     The map to navigate
     * @param keypath The keypath to return the value for
     * @param <V>     The type of the return value
     * @return The value at <code>keypath</code>
     */
    @SuppressWarnings({"RedundantTypeArguments"})
    public static <V> V valueFor(final Map<String, ?> map, final String keypath) {
        return Maps.<String, V>valueFor(map, keypath.split("\\."));
    }

    /**
     * Convenience method for putting a value in a nested map structure. This method takes a Map of nested maps. Each
     * map in the chain to be navigated must use strings for the key values.
     * <p/>
     * The keypath should be of the form "key1.key2.key3". The type of the value to be put is <V>
     *
     * @param map     the map to navigate
     * @param keypath the keypath representing the value to be replaced
     * @param value   the new value
     * @param <V>     the value type
     * @return the old value at <code>keypath</code>
     */
    @SuppressWarnings({"unchecked"})
    public static <V> V putValueFor(Map<String, ?> map, final String keypath, final V value) {
        return putValueFor(map, value, keypath.split("\\."));
    }

    /**
     * Convenience method for navigating nested maps. This method takes a Map of nested maps, and a sequence of keys to
     * navigate.
     *
     * @param map         A map, possibly a map of maps if the keySequence length is greater than 1
     * @param keySequence A list of keys to navigate
     * @param <K>         The key type
     * @param <V>         The expected return type
     * @return The value at <code>keySequence</code>
     */
    @SuppressWarnings({"unchecked"})
    public static <K, V> V valueFor(final Map<? extends K, ?> map, final List<? extends K> keySequence) {
        Object result = map;
        for (final K key : keySequence) {
            result = ((Map<K, ?>) result).get(key);
        }

        return (V) result;
    }

    /**
     * Convenience method for navigating nested maps.
     *
     * @param map  A map, possibly a map of maps if the keySequence length is greater than 1
     * @param keys A list of keys to navigate
     * @param <K>  The key type
     * @param <V>  The expected return type
     * @return The value at <code>keys</code>
     * @see #valueFor(Map, List)
     */
    @SuppressWarnings({"RedundantTypeArguments"})
    public static <K, V> V valueFor(final Map<? extends K, ?> map, final K... keys) {
        return Maps.<K, V>valueFor(map, Arrays.asList(keys));
    }

    /**
     * Convenience method for putting a value in a nested map structure. This method takes a Map of nested maps, and a
     * list of keys to navigate.
     * <p/>
     * The method effectively does <code>map.get(key1).get(key2) ... put(keyn, value)</code>
     * <p/>
     * a bit more conveniently
     *
     * @param map   the map to navigate
     * @param value the new value
     * @param keys  the keys representing the value to be replaced
     * @param <K>   the key type
     * @param <V>   the value type
     * @return the old value at <code>keys</code>
     */
    @SuppressWarnings({"unchecked"})
    public static <K, V> V putValueFor(Map<? extends K, ?> map, final V value, final List<? extends K> keys) {
        int i = 0;
        for (; i < keys.size() - 1; i++) {
            map = (Map<? extends K, ?>) map.get(keys.get(i));
        }

        return ((Map<K, V>) map).put(keys.get(i), value);
    }

    /**
     * Convenience method for putting a value in a nested map structure. This method takes a Map of nested maps, and a
     * list of keys to navigate.
     * <p/>
     * The method effectively does <code>map.get(key1).get(key2) ... put(keyn, value)</code>
     * <p/>
     * a bit more conveniently
     *
     * @param map   the map to navigate
     * @param value the new value
     * @param keys  the keys representing the value to be replaced
     * @param <K>   the key type
     * @param <V>   the value type
     * @return the old value at <code>keys</code>
     */
    public static <K, V> V putValueFor(final Map<? extends K, ?> map, final V value, final K... keys) {
        return putValueFor(map, value, Arrays.asList(keys));
    }

    public static <K, V> Map<K, V> eachPair(Map<K, V> map, Block2<K, V, ?> block2) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            block2.invoke(entry.getKey(), entry.getValue());
        }
        return map;
    }

    public static <K, V> Map<K, V> deleteIf(final Map<K, V> map, final Block2<K, V, Boolean> block2) {
        return eachPair(map, new Block2<K, V, Object>() {
            public Object invoke(K k, V v) {
                if (block2.invoke(k, v)) map.remove(k);
                return null;
            }
        });
    }

    public static <K, V> Map<K, V> eachKey(Map<K, V> map, Block<K, ?> block) {
        for (K key : map.keySet()) block.invoke(key);

        return map;
    }

    public static <K, V> Map<K, V> eachValue(Map<K, V> map, Block<V, ?> block) {
        for (V value : map.values()) block.invoke(value);

        return map;
    }

    public static <K, V, R> List<R> flatten(Map<K, V> map) {
        final List<R> result = new ArrayList<R>();
        eachPair(map, new Block2<K, V, Object>() {
            @SuppressWarnings({"unchecked"})
            public Object invoke(K k, V v) {
                result.add((R) k);
                result.add((R) v);
                return null;
            }
        });

        return result;
    }

    public static <K, V> Map<V, K> invert(Map<K, V> originalMap) {
        final Map<V, K> result = new HashMap<V, K>();
        eachPair(originalMap, new Block2<K, V, Object>() {
            public Object invoke(K k, V v) {
                result.put(v, k);
                return null;
            }
        });
        return result;
    }

    public static <K, V> Map<K, V> keepIf(final Map<K, V> map, final Block2<K, V, Boolean> block2) {
        final List<K> removables = new ArrayList<K>();
        eachPair(map, new Block2<K, V, Object>() {
            public Object invoke(K k, V v) {
                if (!block2.invoke(k, v)) removables.add(k);
                return null;
            }
        });

        map.keySet().removeAll(removables);

        return map;
    }

    public static <K, V> Map<K, V> reject(Map<K, V> map, final Block2<K, V, Boolean> block2) {
        final Map<K, V> result = new HashMap<K, V>();
        eachPair(map, new Block2<K, V, Object>() {
            public Object invoke(K k, V v) {
                if (!block2.invoke(k, v)) result.put(k, v);
                return null;
            }
        });

        return result;
    }

    public static <K, V> Map<K, V> select(Map<K, V> map, final Block2<K, V, Boolean> block2) {
        final Map<K, V> result = new HashMap<K, V>();
        eachPair(map, new Block2<K, V, Object>() {
            public Object invoke(K k, V v) {
                if (block2.invoke(k, v)) result.put(k, v);
                return null;
            }
        });

        return result;
    }

    public static <K, V> List<V> valuesFor(Map<K, V> map, K... keys) {
        List<V> result = new ArrayList<V>(keys.length);
        for (int i = 0; i < keys.length; i++) {
            result.add(map.get(keys[i]));
        }

        return result;
    }

    public static <K, V> Map<K, V> merge(Map<? extends K, ? extends V>... map) {
        Map<K, V> result = new HashMap<K, V>();

        for (int i = 0; i < map.length; i++) {
            result.putAll(map[i]);
        }
        return result;
    }

    public static <K, V> Map.Entry<K, V> find(Map<? extends K, ? extends V> map,
                                              Block2<? super K, ? super V, Boolean> block) {
        Map.Entry<K, V> result = null;
        for (Map.Entry<? extends K, ? extends V> entry : map.entrySet()) {
            if(block.invoke(entry.getKey(), entry.getValue())){
                result = (Map.Entry<K, V>) entry;
                break;
            }
        }

        return result;
    }
}
