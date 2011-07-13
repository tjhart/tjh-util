package com.tjh.util;

public class Strings {

    private Strings() {}

    public static String aOrAn(final String stringToCheck) {
        return stringToCheck.toUpperCase().matches("^[A|E|I|O|U|X].*") ? "an" : "a";
    }

    public static String join(final String[] strings, final String separator) { return join(separator, strings); }

    public static String join(final String separator, final String... strings){
        final StringBuffer result = new StringBuffer();
        boolean inTheMiddle = false;
        for (final String string : strings) {
            if(inTheMiddle){
                result.append(separator);
            }
            result.append(string);
            inTheMiddle = true;
        }

        return result.toString();
    }

    public static String substring(String string, Range<Integer> range){
        return string.substring(range.getMin(), range.getMax());
    }
}
