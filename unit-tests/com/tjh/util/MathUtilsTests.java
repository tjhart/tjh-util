package com.tjh.util;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import com.tjh.util.Maps;
import com.tjh.util.MathUtils;
import org.junit.Test;
import org.junit.experimental.theories.DataPoint;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import java.util.Map;

@RunWith(Theories.class)
public class MathUtilsTests {

    @Test
    public void decimalDigit(){
        assertThat(MathUtils.decimalDigit(30, 2), equalTo(3));
    }

    @Test
    public void decimalDigitAtOne(){
        assertThat(MathUtils.decimalDigit(34, 1), equalTo(4));
    }

    @Test
    public void checkDigitWithEvenNumbersOnly(){
        assertThat(MathUtils.checkDigit(1234567890), equalTo(5));
//        assertThat(MathUtils.checkDigit(1010101010), equalTo(5));
    }

    @Test
    public void checkDigitWithOddNumbersOnly(){
        assertThat(MathUtils.checkDigit(101010101), equalTo(5));
    }

    @Test
    public void checkDigitWithNegativeNumberIsPositive(){
        assertThat(MathUtils.checkDigit(-1525400), equalTo(3));
    }

    @Test
    public void checkDigitWithOddNumberOfDigits(){
        assertThat(MathUtils.checkDigit(1234567), equalTo(8));
    }

    @DataPoint
    public static final Map<String, Long> EXAMPLE1 = Maps.asMap("seed", 2021L, "checkDigit", 7L);
    @DataPoint
    public static final Map<String, Long> EXAMPLE2 = Maps.asMap("seed", 454545L, "checkDigit", 9L);
    @DataPoint
    public static final Map<String, Long> EXAMPLE3 = Maps.asMap("seed", 56565656L, "checkDigit", 6L);
    @DataPoint
    public static final Map<String, Long> EXAMPLE4 = Maps.asMap("seed", 1525400L, "checkDigit", 3L);
    @DataPoint
    public static final Map<String, Long> EXAMPLE5 = Maps.asMap("seed", 7654321L, "checkDigit", 8L);

    @Theory
    public void checkDigitFullTest(final Map<String, Long> example){
        assertThat(MathUtils.checkDigit(example.get("seed")), equalTo(example.get("checkDigit").intValue()));
    }
}
