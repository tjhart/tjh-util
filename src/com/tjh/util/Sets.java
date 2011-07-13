package com.tjh.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.EnumSet;

public class Sets {

    /**
     * Creates a set from the list of <code>items</code>
     *
     * @param items The source items for the set
     * @param <T> The type of items in the set
     * @return a set containing items
     */
    public static <T> Set<T> asSet(final T... items) { return new HashSet<T>(Arrays.asList(items)); }

    /**
     * Specialized version of 'asSet' when <code>items</code> are enums
     * @param items The source items for the set
     * @param <T> The type of items in the set
     * @return a set containing items
     */
    public static <T extends Enum<T>> Set<T> asSet(final T... items){
        return EnumSet.copyOf(Arrays.asList(items));
    }

    /**
     * Creates an unmodifiable set from the list of <code>items</code>
     * @param items The source items for the set
     * @param <T> The type of items in the set
     * @return a set containing items
     */
    public static <T> Set<T> asConstantSet(final T... items){ return Collections.unmodifiableSet(asSet(items)); }

    /**
     * Specialized version of 'asSet' when <code>items</code> are enums
     * @param items The source items for the set
     * @param <T> The type of items in the set
     * @return a set containing items
     */
    public static <T extends Enum<T>> Set<T> asConstantSet(final T... items){
        return Collections.unmodifiableSet(asSet(items));
    }
}
