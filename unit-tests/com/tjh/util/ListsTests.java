package com.tjh.util;

import com.tjh.util.Block;
import com.tjh.util.Block2;
import com.tjh.util.Lists;
import com.tjh.util.Sets;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static com.tjh.util.Dates.monthsAgo;
import static com.tjh.util.Dates.monthsFromNow;
import static com.tjh.util.Lists.sequential;
import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.createStrictMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;

public class ListsTests {
    private static final String TIM = "Tim";
    private static final String NAILA = "Naila";
    private static final String JEFF = "Jeff";
    private static final String CALEB = "Caleb";
    private static final String BETH = "Beth";
    private Block<String, Boolean> mockBlock;
    private static final String RUSS = "Russ";

    @Test
    public void flatteningSingleListReturnsSameList() {
        List<String> list = Arrays.asList(CALEB, NAILA);

        Collection<String> returnedCollection = Lists.flatten(list);

        assertThat(returnedCollection, equalTo((Collection<String>) list));
        assertThat(list, equalTo(returnedCollection));
    }

    @Test
    public void flatteningTwoListsPreservesOrder() {
        List<String> listOne = Arrays.asList(TIM, NAILA);
        List<String> listTwo = Arrays.asList(CALEB, JEFF, BETH);

        assertThat(Lists.flatten(listOne, listTwo),
                equalTo((Collection<String>) Arrays.asList(TIM, NAILA, CALEB, JEFF, BETH)));
    }

    @Test
    public void asListWithOneItem() {
        assertThat(Lists.asList(CALEB), equalTo(Arrays.asList(CALEB)));
    }

    @Test
    public void asListWithSubclasses() {
        TheSubClass itemOne = new TheSubClass();
        TheSubClass itemTwo = new TheSubClass();

        assertThat(Lists.<TheSuperClass>asList(itemOne, itemTwo),
                equalTo(Arrays.<TheSuperClass>asList(itemOne, itemTwo)));
    }

    @Test
    public void reverse() {
        assertThat(Lists.reverse(Arrays.asList(BETH, CALEB, TIM)), equalTo(Arrays.asList(TIM, CALEB, BETH)));
    }

    @Test
    public void findReturnsFirstElementThatBlockReturnsTrueFor() {
        expect(mockBlock.invoke(CALEB)).andReturn(false);
        expect(mockBlock.invoke(TIM)).andReturn(true);
        expect(mockBlock.invoke(BETH)).andStubReturn(true);
        replay(mockBlock);

        assertThat(Lists.find(Arrays.asList(CALEB, TIM, BETH), mockBlock), equalTo(TIM));
        verify(mockBlock);
    }

    @Test
    public void findReturnsNullIfBlockAlwaysReturnsFalse() {
        expect(mockBlock.invoke((String) anyObject())).andStubReturn(false);
        replay(mockBlock);

        assertThat(Lists.find(Arrays.asList(CALEB, JEFF, NAILA), mockBlock), nullValue());
    }

    /**
     * This one was much easier to test all of the behavior as a single test. It was hard to construct a test
     * That concentrated on individual behaviors of inject without muddying the water
     */
    @Test
    public void injectBehavesProperly() {
        @SuppressWarnings({"unchecked"})
        Block2<String, Integer, Integer> mockBlock = createStrictMock(Block2.class);

        expect(mockBlock.invoke(TIM, 1)).andReturn(2);
        expect(mockBlock.invoke(CALEB, 2)).andReturn(3);
        expect(mockBlock.invoke(RUSS, 3)).andReturn(4);
        replay(mockBlock);

        assertThat(Lists.inject(Arrays.asList(TIM, CALEB, RUSS), 1, mockBlock), equalTo(4));
        verify(mockBlock);
    }

    @SuppressWarnings({"unchecked"})
    @Test
    public void collectAddsResultOfBlockToProvidedCollection() {
        Collection<Integer> mockCollection = createStrictMock(Collection.class);
        Block<String, Integer> mockBlock = createStrictMock(Block.class);

        expect(mockBlock.invoke(BETH)).andReturn(3);
        expect(mockCollection.add(3)).andReturn(true);
        expect(mockBlock.invoke(JEFF)).andReturn(-1);
        expect(mockCollection.add(-1)).andReturn(true);
        replay(mockBlock, mockCollection);

        assertThat(Lists.collect(Arrays.asList(BETH, JEFF), mockCollection, mockBlock), sameInstance(mockCollection));
        verify(mockBlock, mockCollection);
    }

    @SuppressWarnings({"unchecked"})
    @Test
    public void collectAllAddsAllResultsOfBlockToProvidedCollection() {
        Collection<Integer> mockCollection = createStrictMock(Collection.class);
        Block<String, Collection<Integer>> mockBlock = createStrictMock(Block.class);

        final List<Integer> bethCollection = Arrays.asList(3, 2);
        final List<Integer> jeffCollection = Arrays.asList(-10, -100);

        expect(mockBlock.invoke(BETH)).andReturn(bethCollection);
        expect(mockCollection.addAll(bethCollection)).andReturn(true);
        expect(mockBlock.invoke(JEFF)).andReturn(jeffCollection);
        expect(mockCollection.addAll(jeffCollection)).andReturn(true);
        replay(mockBlock, mockCollection);

        assertThat(Lists.collectAll(Arrays.asList(BETH, JEFF), mockCollection, mockBlock),
                sameInstance(mockCollection));
        verify(mockBlock, mockCollection);
    }

    @Test
    public void selectSavesAllTrueBlockInvocationsToCollection() {
        final Set<String> searchingFor = Sets.asSet(JEFF, RUSS);

        assertThat(Lists.select(Arrays.asList(TIM, JEFF, RUSS, CALEB, BETH), new ArrayList<String>(),
                new Block<String, Boolean>() {
                    public Boolean invoke(final String s) { return searchingFor.contains(s); }
                }), equalTo((Collection<String>) Arrays.asList(JEFF, RUSS)));
    }

    @Test
    public void rejectSavesAllFalseBlockInvocationsToCollection() {
        final Set<String> searchingFor = Sets.asSet(JEFF, RUSS);

        assertThat(Lists.reject(Arrays.asList(TIM, JEFF, RUSS, CALEB, BETH), new ArrayList<String>(),
                new Block<String, Boolean>() {
                    public Boolean invoke(final String s) { return searchingFor.contains(s); }
                }), equalTo((Collection<String>) Arrays.asList(TIM, CALEB, BETH)));
    }

    @Test
    public void compactRemovesNullValues() {
        assertThat(Lists.compact(Lists.asList("Bill", null, "Joe", null)), equalTo(Arrays.asList("Bill", "Joe")));
    }

    private final Integer[] myArray = new Integer[]{0, 1, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89, 144};

    @Test
    public void sliceFromStartWorks() {

        assertThat(Lists.slice(myArray, 2),
                equalTo(new Integer[]{1, 2, 3, 5, 8, 13, 21, 34, 55, 89, 144}));

    }

    @Test
    public void sliceFromRangeWorks(){
        assertThat(Lists.slice(myArray, 2, 6), equalTo(new Integer[]{1, 2, 3, 5}));
    }

    @Test
    public void sliceWorksWithMoreGenericContainer(){
        assertThat(Lists.slice(new Number[]{1, 1.2f, 1.3d, 4l}, 2, 4), equalTo(new Number[]{1.3d, 4l}));
    }

    @Test
    public void sequentialReturnsTrueForItemsInOrder() {
        assertThat(sequential(monthsAgo(2), monthsAgo(1), monthsFromNow(2)), is(true));
    }

    @Test
    public void sequentialReturnsFalseForItemsOutOfOrder(){
        assertThat(sequential(-1, -2, 2), is(false));
    }

    @Test
    public void sequentialReturnsTrueForSingleItem() {
        assertThat(sequential(BigDecimal.TEN), is(true));
    }

    @Test(expected = Exception.class)
    public void sequentialThrowsForNoDates() {
        Lists.<Integer>sequential();
    }

    @Test
    public void sequentialReturnsTrueForEqualDates() {
        final Date twoMonthsAgo = monthsAgo(2);
        final Date oneMonthAgo = monthsAgo(1);
        assertThat(sequential(twoMonthsAgo, twoMonthsAgo, oneMonthAgo, oneMonthAgo, oneMonthAgo, monthsFromNow(2)),
                is(true));
    }
    @Before
    public void before() {
        //noinspection unchecked
        mockBlock = createMock(Block.class);
    }

    private static class TheSuperClass {
    }

    private static class TheSubClass extends TheSuperClass {
    }
}
