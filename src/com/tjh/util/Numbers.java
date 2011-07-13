package com.tjh.util;

import java.math.BigInteger;

/**
 * Utility class containing
 * various utility classes
 * to convert between numbers
 */
public class Numbers {

    /**
     * Converts a @{Integer} to a  @{BigInteger}.  If the @Integer is null,
     * null is returned
     */
    public static BigInteger toBigInteger( Integer integer ){
        BigInteger value = null;
        if( integer != null )
        {
            value = BigInteger.valueOf( integer );
        }
        return value;
    }
}
