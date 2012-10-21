package com.tjh.util;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Range<T extends Comparable<? super T>>{
    private T min;
    private T max;
    private boolean inclusive;

    //useful to allow dynamic proxies and enhanced classes (ala cglib)
    @SuppressWarnings("UnusedDeclaration")
    Range() {}

    public Range(final T min, final T max) { this(min, max, true); }

    public Range(T min, T max, boolean inclusive) {
        if (min == null && max == null) {
            throw new IllegalArgumentException("both min and max cannot be null: <" + min + ", " + max + ">");
        }

        if (min != null && max != null && min.compareTo(max) > 0) {
            throw new IllegalArgumentException("min value cannot be greater than max value:" + min + " > " + max);
        }

        this.min = min;
        this.max = max;
        this.inclusive = inclusive;
    }

    public Range<T> intersection(final Range<? extends T> otherRange) {
        T newMin = min;
        if (otherRange.min != null) {
            newMin = min.compareTo(otherRange.min) > 0 ? min : otherRange.min;
        }
        T newMax = max;
        if (otherRange.max != null) {
            newMax = max.compareTo(otherRange.max) < 0 ? max : otherRange.max;
        }
        return new Range<T>(newMin, newMax);
    }

    public Range<T> union(final Range<? extends T> otherRange) {
        T newMin = null;
        if (min != null && otherRange.min != null) {
            newMin = min.compareTo(otherRange.min) < 0 ? min : otherRange.min;
        }
        T newMax = null;
        if (max != null && otherRange.max != null) {
            newMax = max.compareTo(otherRange.max) > 0 ? max : otherRange.max;
        }
        return new Range<T>(newMin, newMax);
    }

    public boolean contains(final Range<? extends T> otherRange) {
        return contains(otherRange.min) && containsMax(otherRange.max);
    }

    public boolean contains(final T member) {
        return (min == null || min.compareTo(member) <= 0) && containsMax(member);
    }

    private boolean containsMax(T member) {
        boolean result = true;
        if (max != null) {
            final int comparison = max.compareTo(member);
            result = (inclusive ? comparison >= 0 : comparison > 0);
        }

        return result;
    }

    //iterable

    public Iterator<T> iterator() { return new RangeIterator(); }

    //object

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Range range = (Range) o;

        return inclusive == range.inclusive
                && !(max != null ? !max.equals(range.max) : range.max != null)
                && !(min != null ? !min.equals(range.min) : range.min != null);
    }

    @Override
    public int hashCode() {
        int result = min != null ? min.hashCode() : 0;
        result = 31 * result + (max != null ? max.hashCode() : 0);
        result = 31 * result + (inclusive ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Range");
        sb.append("{min=").append(min);
        sb.append(", max=").append(max);
        sb.append(", inclusive=").append(inclusive);
        sb.append('}');
        return sb.toString();
    }

    //bean
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
