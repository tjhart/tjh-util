package com.tjh.util;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;

import com.tjh.util.Sets;
import org.junit.Test;

import java.util.EnumSet;

public class SetsTests {

    @Test
    public void asSetUsesEnumSetWhereAppropriate(){
        assertThat(Sets.asSet(TestEnum.ONE, TestEnum.TWO), instanceOf(EnumSet.class));
    }

    enum TestEnum{ONE, TWO, THREE}
}
