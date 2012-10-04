package com.tjh.util;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Lists {

    /**
     * Combine all collections into a single collection. <i>Note: this does not do deep traversal</i>
     *
     * @param collections The collections to combine
     * @param <T>         The common base type for all collections
     * @return a single collection containing all elements from <code>collections</code>
     */
    public static <T> List<T> flatten(final Collection<? extends T>... collections) {
        final List<T> result = new ArrayList<T>();
        for (Collection<? extends T> t : collections) {
            result.addAll(t);
        }

        return result;
    }

    /**
     * @param arrays The arrays to flatten
     * @param <T>    The common base type for all arrays
     * @return a single array containing all elements from <code>arrays</code>
     */
    public static <T> T[] flatten(final T[]... arrays) {
        final List<T> result = new ArrayList<T>();
        final Collection<Class> classes = new ArrayList<Class>(arrays.length);
        for (T[] array : arrays) {
            result.addAll(Arrays.asList(array));
            classes.add(array.getClass().getComponentType());
        }

        //noinspection unchecked
        return result.toArray((T[]) Array.newInstance(Classes.commonBaseClasses(classes).iterator().next(), result.size()));
    }

    /**
     * Similar to Arrays#asList, but the return value is modifiable
     *
     * @param items the items to create a list for
     * @param <T>   The generic type of the returned list
     * @return a list containing <code>items</code>, in the same order they were passed.
     */
    public static <T> List<T> asList(final T... items) {
        final List<T> list = new LinkedList<T>();
        list.addAll(Arrays.asList(items));

        return list;
    }

    /**
     * Search iterable for an element.
     *
     * @param iterable The iterable containing the interesting element
     * @param block    Code to determine whether or not the element is interesting. Will be passed one item from the
     *                 collection
     * @param <T>      The type of element being searched
     * @return The first element in <code>iterable</code> that <code>block</code> returns true for, or null
     */
    public static <T> T find(final Iterable<? extends T> iterable, final Block<? super T, Boolean> block) {
        T result = null;
        for (final T t : iterable) {
            if (block.invoke(t)) {
                result = t;
                break;
            }
        }

        return result;
    }

    /**
     *
     * Iterate through <code>tArray</code>, returning the first item for which <code>block</code> returns true
     * 
     * @see Lists#find(Iterable, Block)
     */
    public static <T> T find(final T[] tArray, final Block<? super T, Boolean> block) {
        return find(Arrays.asList(tArray), block);
    }

    /**
     * Calls <code>block</code> for each item in <code>iterable</code>. Each invocation of <code>block</code> is passed
     * the result of the last invocation. The first invocation is passed <code>initial</code> <br><br> This method can
     * be used to aggregate values in a collection.
     *
     * @param iterable The collection to iterate over
     * @param initial  The initial value to pass to the block
     * @param block    Code to operate on items in <code>iterable</code> and the result of previous calls
     * @param <T>      The type of item in <code>iterable</code>
     * @param <U>      The return type of <code>block</code>
     * @return The result of the last call to <code>block</code>
     */
    public static <T, U> U inject(final Iterable<? extends T> iterable, U initial,
                                  final Block2<? super T, ? super U, ? extends U> block) {
        for (final T t : iterable) {
            initial = block.invoke(t, initial);
        }

        return initial;
    }

    /**
     * Calls <code>block</code> for each item in <code>iterable</code>, and adds the result to <code>collection</code>
     *
     * @param iterable   The collection to iterate over
     * @param collection The containing collection for the results of each call to <code>block</code>
     * @param block      Code that generates the interesting collection from <code>T</code>
     * @param <T>        The type of item in <code>iterable</code>
     * @param <U>        The type of object returned by <code>block</code>
     * @return <code>collection</code>
     */
    public static <T, U, V extends Collection<? super U>> V collect(final Iterable<? extends T> iterable, V collection,
                                                            final Block<? super T, ? extends U> block) {
        for (final T t : iterable) {
            collection.add(block.invoke(t));
        }

        return collection;
    }

    /**
     * Just like calling {@link #collect(Iterable, java.util.Collection, Block)} as:
     * <p/>
     * <code>Lists.collect(iterable, new ArrayList<U>(), block);</code>
     *
     * @param iterable The collection to iterate over
     * @param block    Code that generates the interesting collection from <code>T</code>
     * @param <T>      The type of item in <code>iterable</code>
     * @param <U>      The type of object returned by <code>block</code>
     * @return <code>collection</code>
     */
    public static <T, U> Collection<U> collect(final Iterable<? extends T> iterable,
                                               final Block<? super T, ? extends U> block) {
        return collect(iterable, new ArrayList<U>(), block);
    }

    /**
     * @param ts         - the array to iterate over
     * @param collection The containing collection for the results of each call to <code>block</code>
     * @param block      Code that generates the interesting collection from <code>T</code>
     * @param <T>        The type of item in <code>iterable</code>
     * @param <U>        The type of object returned by <code>block</code>
     * @return <code>collection</code>
     * @see #collect(Iterable, java.util.Collection, Block)
     */
    public static <T, U, V extends Collection<? super U>> V collect(final T[] ts, final V collection,
                                                            final Block<? super T, ? extends U> block) {
        return collect(Arrays.asList(ts), collection, block);
    }

    /**
     * Just like calling {@link #collect(Iterable, java.util.Collection, Block)} as:
     * <p/>
     * <code>Lists.collect(ts, new ArrayList<U>(), block);</code>
     *
     * @param ts    The array to iterate over
     * @param block Code that generates the interesting collection from <code>T</code>
     * @param <T>   The type of item in <code>iterable</code>
     * @param <U>   The type of object returned by <code>block</code>
     * @return <code>collection</code>
     */
    public static <T, U> Collection<U> collect(final T[] ts, final Block<? super T, ? extends U> block) {
        return collect(Arrays.asList(ts), block);
    }

    /**
     * Calls <code>block</code> for each item in <code>iterable</code>, and adds the result to <code>collection</code>
     *
     * @param iterable   The collection to iterate over
     * @param collection The containing collection for the results of each call to <code>block</code>
     * @param block      Code that generates the interesting collection from <code>T</code>
     * @param <T>        The type of item in <code>iterable</code>
     * @param <U>        The type of object returned by <code>block</code>
     * @return <code>collection</code>
     */
    public static <T, U, V extends Collection<? super U>> V collectAll(final Iterable<? extends T> iterable,
                                                                       final V collection,
                                                                       final Block<? super T, Collection<U>> block) {
        for (final T t : iterable) {
            collection.addAll(block.invoke(t));
        }

        return collection;
    }

    /**
     * Just like calling {@link #collectAll(Iterable, java.util.Collection, Block)} as:
     * <p/>
     * <code>Lists.collectAll(iterable, new ArrayList<U>(), block);</code>
     *
     * @param iterable The collection to iterate over
     * @param block    Code that generates the interesting collection from <code>T</code>
     * @param <T>      The type of item in <code>iterable</code>
     * @param <U>      The type of object returned by <code>block</code>
     * @return <code>collection</code>
     */
    public static <T, U> Collection<U> collectAll(final Iterable<? extends T> iterable,
                                                  final Block<? super T, Collection<U>> block) {
        return collectAll(iterable, new ArrayList<U>(), block);
    }

    /**
     * @param ts         - the array to iterate over
     * @param collection The containing collection for the results of each call to <code>block</code>
     * @param block      Code that generates the interesting collection from <code>T</code>
     * @param <T>        The type of item in <code>iterable</code>
     * @param <U>        The type of object returned by <code>block</code>
     * @return <code>collection</code>
     * @see #collect(Iterable, java.util.Collection, Block)
     */
    public static <T, U, V extends Collection<? super U>> V collectAll(final T[] ts, final V collection,
                                                                       final Block<? super T, Collection<U>> block) {
        return collectAll(Arrays.asList(ts), collection, block);
    }

    /**
     * Just like calling {@link #collectAll(Iterable, java.util.Collection, Block)} as:
     * <p/>
     * <code>Lists.collectAll(ts, new ArrayList<U>(), block);</code>
     *
     * @param ts    - the array to iterate over
     * @param block Code that generates the interesting collection from <code>T</code>
     * @param <T>   The type of item in <code>iterable</code>
     * @param <U>   The type of object returned by <code>block</code>
     * @return <code>collection</code>
     * @see #collect(Iterable, java.util.Collection, Block)
     */
    public static <T, U> Collection<U> collectAll(final T[] ts, final Block<? super T, Collection<U>> block) {
        return collectAll(Arrays.asList(ts), block);
    }

    /**
     * Calls <code>block</code> for each item in <code>iterable</code>, adding the item to <code>collection</code> only
     * if <code>block#invoke</code> returns true.
     *
     * @param iterable   The collection to iterate over
     * @param collection The collection for the results
     * @param block      The block used to determine membership in <code>collection</code>
     * @param <T>        The type of item in <code>iterable</code>
     * @return <code>collection</code>
     */
    public static <T, U extends Collection<? super T>> U select(final Iterable<? extends T> iterable, final U collection,
                                                        final Block<? super T, Boolean> block) {
        for (final T t : iterable) {
            if (block.invoke(t)) {
                collection.add(t);
            }
        }

        return collection;
    }

    /**
     * Just like calling {@link #select(Iterable, java.util.Collection, Block)} as:
     * <p/>
     * <code>Lists.select(iterable, new ArrayList<T>(), block);
     *
     * @param iterable The collection to iterate over
     * @param block    The block used to determine membership in <code>collection</code>
     * @param <T>      The type of item in <code>iterable</code>
     * @return <code>collection</code>
     */
    public static <T> Collection<T> select(final Iterable<? extends T> iterable,
                                           final Block<? super T, Boolean> block) {
        return select(iterable, new ArrayList<T>(), block);
    }

    /**
     * @param ts    the array of Ts to select over
     * @param block The block to execute for each member of ts
     * @param <T>   t's type
     * @return a collection of ts for which <code>block</code> returns true
     */

    public static <T> Collection<T> select(final T[] ts, final Block<? super T, Boolean> block) {
        return select(Arrays.asList(ts), block);
    }

    /**
     * Calls <code>block</code> for each item in <code>iterable</code>, adding the item to <code>collection</code> only
     * if <code>block#invoke</code> returns false.
     *
     * @param iterable   The collection to iterate over
     * @param collection The collection for the results
     * @param block      The block used to determine membership in <code>collection</code>
     * @param <T>        The type of item in <code>iterable</code>
     * @return <code>collection</code>
     */
    public static <T, U extends Collection<? super T>> U reject(final Iterable<? extends T> iterable, final U collection,
                                                        final Block<? super T, Boolean> block) {
        return select(iterable, collection, new Block<T, Boolean>() {
            public Boolean invoke(T t) { return !block.invoke(t); }
        });
    }

    /**
     * Just like calling {@link #reject(Iterable, java.util.Collection, Block)} as:
     * <p/>
     * <code>Lists.reject(iterable, new ArrayList<T>(), block);
     *
     * @param iterable The collection to iterate over
     * @param block    The block used to determine membership in <code>collection</code>
     * @param <T>      The type of item in <code>iterable</code>
     * @return <code>collection</code>
     */
    public static <T> Collection<T> reject(final Iterable<? extends T> iterable,
                                           final Block<? super T, Boolean> block) {
        return reject(iterable, new ArrayList<T>(), block);
    }

    /**
     * Similar to Collections.reverse, except the passed in collection is not modified. A new, reversed collection
     * is returned instead.
     *
     * @param collection The collection to be reversed
     * @param <T>        The base type of the collection
     * @return A new collection in reverse order as defined by T.compareTo
     */
    public static <T> List<T> reverse(final List<? extends T> collection) {
        final List<T> result = new ArrayList<T>(collection);
        Collections.reverse(result);
        return result;
    }

    /**
     * Removes null elements from <code>list</code>. Note that this modifies <code>list</code> in place
     *
     * @param list an iterable
     * @param <T>  the type of iterable
     * @return <code>list</code> itself
     */
    public static <T extends Iterable> T compact(final T list) {
        final Iterator iter = list.iterator();
        while (iter.hasNext()) {
            if (null == iter.next()) { iter.remove(); }
        }

        return list;
    }

    public static <T> T[] slice(final T[] theArray, final int start, final int end) {
        @SuppressWarnings({"unchecked"})
        final T[] fillMe = (T[]) Array.newInstance(theArray.getClass().getComponentType(), end - start);
        return Arrays.asList(theArray).subList(start, end).toArray(fillMe);
    }

    public static <T> T[] slice(final T[] theArray, final int start) { return slice(theArray, start, theArray.length); }

    public static <T extends Comparable<? super T>> boolean sequential(Collection<T> items){
        if (items.size() == 1) return true;

        T last = items.iterator().next();
        for (T item : items) {
            if (last.compareTo(item) > 0) return false;
            last = item;
        }
        return true;

    }

    public static <T extends Comparable<? super T>> boolean sequential(final T... items) {
        return sequential(Arrays.asList(items));
    }
}
