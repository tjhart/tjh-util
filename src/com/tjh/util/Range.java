package com.tjh.util;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Range<T extends Comparable<? super T>> implements Iterable<T> {
    private T min;
    private T max;
    private boolean inclusive;

    //useful to allow dynamic proxies and enhanced classes (ala cglib)
    Range() {}

    public Range(final T min, final T max) { this(min, max, true); }

    public Range(T min, T max, boolean inclusive) {
        if (min == null || max == null) {
            throw new IllegalArgumentException("No nulls allowed: <" + min + ", " + max + ">");
        }

        if (min.compareTo(max) > 0) {
            throw new IllegalArgumentException("min value cannot be greater than max value:" + min + " > " + max);
        }

        this.min = min;
        this.max = max;
        this.inclusive = inclusive;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final Range range = (Range) o;

        return max.equals(range.max) && min.equals(range.min);
    }

    @Override
    public int hashCode() {
        int result = min.hashCode();
        result = 31 * result + max.hashCode();
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder("<");

        result.append(getClass().getName()).append(":");
        result.append("\nmin: ").append(min);
        result.append("\nmax: ").append(max);
        result.append("\n>");

        return result.toString();
    }

    private boolean containsMax(T member) {
        final int comparison = max.compareTo(member);
        return(inclusive ? comparison >= 0 : comparison > 0);
    }

    public Range<T> intersection(final Range<? extends T> otherRange) {
        final T newMin = min.compareTo(otherRange.min) > 0 ? min : otherRange.min;
        final T newMax = max.compareTo(otherRange.max) < 0 ? max : otherRange.max;
        return new Range<T>(newMin, newMax);
    }

    public Range<T> union(final Range<? extends T> otherRange) {
        final T newMin = min.compareTo(otherRange.min) < 0 ? min : otherRange.min;
        final T newMax = max.compareTo(otherRange.max) > 0 ? max : otherRange.max;
        return new Range<T>(newMin, newMax);
    }

    public boolean contains(final Range<? extends T> otherRange) {
        return contains(otherRange.min) && containsMax(otherRange.max);
    }

    public boolean contains(final T member) { return min.compareTo(member) <= 0 && containsMax(member); }

    public Iterator<T> iterator() { return new RangeIterator(); }

    public T getMin() { return min; }

    public T getMax() { return max; }

    private class RangeIterator implements Iterator<T> {
        private Mutable<T> current = Mutables.forValue(min);

        public T next() {
            T result;
            if (hasNext()) {
                result = current.getValue();
                current.next();
            } else {
                throw new NoSuchElementException();
            }

            return result;
        }

        public boolean hasNext() { return current.compareTo(max) <= 0; }

        public void remove() { throw new UnsupportedOperationException(); }
    }
}
