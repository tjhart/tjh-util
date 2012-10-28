package com.tjh.util;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class RangeTests {

    @Test(expected = IllegalArgumentException.class)
    public void constructorRequiresFirstArgumentToBeLessThanOrEqualToSecondArgument() {
        new Range<Long>(10L, 1L);
    }

    @Test
    public void containsReturnsTrueWhenRangeEncompasesValue() {
        assertThat(new Range<Integer>(1, 10).contains(5), is(true));
    }

    @Test
    public void containsReturnsFalseWhenValueIsTooLow() {
        assertThat(new Range<Integer>(5, 10).contains(1), is(false));
    }

    @Test
    public void containsReturnsFalseWhenValueIsTooHigh() {
        assertThat(new Range<Integer>(1, 5).contains(10), is(false));
    }

    @Test
    public void exclusiveRangeDoesNotContainMax() {
        assertThat(new Range<Integer>(1, 6, false).contains(6), is(false));
    }

    @Test
    public void containsAnotherRange(){
        assertThat(new Range<Integer>(1,4).contains(new Range<Integer>(2,3)), is(true));
    }

    @Test
    public void doesNotContainDisjointRange(){
        assertThat(new Range<Character>('a', 'b').contains(new Range<Character>('d', 'e')), is(false));
    }

    @Test
    public void doesNotContainOverlappingRange(){
        assertThat(new Range<Float>(1.0f, 4.0f).contains(new Range<Float>(3.14f, 5f)), is(false));
    }

    @Test
    public void intersectionReturnsCorrectValues(){

        final Range<Long> intersection = new Range<Long>(1L, 4L).intersection(new Range<Long>(2L, 5L));

        assertThat(intersection.getMin(), equalTo(2L));
        assertThat(intersection.getMax(), equalTo(4L));
    }

    @Test
    public void unionReturnsCorrectValues(){
        final Range<Long> union = new Range<Long>(1L, 4L).union(new Range<Long>(2L, 5L));

        assertThat(union.getMin(), equalTo(1L));
        assertThat(union.getMax(), equalTo(5L));
    }
}
