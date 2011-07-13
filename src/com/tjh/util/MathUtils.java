package com.tjh.util;

import java.io.File;
import java.math.BigDecimal;

public class MathUtils {

    /**
     * Calculates the rounding error when <code>dividend</code> is divided by <code>divisor</code>
     *
     * @param dividend The dividend
     * @param divisor The devisor
     * @param roundingMode The rounding mode to use when dividing
     * @return The rounding error
     */
    public static BigDecimal roundingError(final BigDecimal dividend, final BigDecimal divisor, final int roundingMode) {
        return dividend
                .subtract(dividend
                        .divide(divisor, divisor.scale(), roundingMode)
                        .multiply(divisor));
    }

    /**
     * Calculates the ocr check digit for <code>value</code>
     * @param value The value to calculate the checkdigit from
     * @return an OCR check digit
     */
    public static int checkDigit(final long value) {
        int result = (sumDigits(value, 2, 4, 6, 8, 10) * 3 + sumDigits(value, 1, 3, 5, 7, 9)) % 10;
        if (result != 0) {
            result = 10 - result;
        }
        return result;
    }

    /**
     * Sums all the digits for the given value
     * @param value The source for the sum
     * @param digits The list of digits to sum
     * @return The sum of all the digits requested
     */
    public static int sumDigits(final long value, final int... digits) {
        int result = 0;

        for (final int i : digits) {
            result += decimalDigit(value, i);
        }

        return result;
    }

    /**
     * Return a particular digit value from a given number
     * @param value The source to retrieve the digit
     * @param i The place to retreive
     * @return The digit value
     */
    public static int decimalDigit(final long value, final int i) {
        int result = (int) (Math.abs(value) % Math.pow(10, i));
        if (i > 1) {
            result /= Math.pow(10, i - 1);
        }
        return result;
    }
}
