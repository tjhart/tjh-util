package com.tjh.util;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * User: thart Date: Aug 11, 2008 Time: 10:49:59 AM
 */
public class Dates {

    private static final long MILLIS_IN_A_DAY = 24 * 60 * 60 * 1000;

    public static Date from(final Date date, final int field, final int unit) {
        final Calendar calendar = calendarAtDate(date);
        calendar.add(field, unit);

        return calendar.getTime();
    }

    public static Date midnightOn(final Date date) {
        final Calendar calendar = calendarAtDate(date);

        calendar.set(Calendar.AM_PM, Calendar.AM);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTime();
    }

    public static Calendar calendarAtDate(final Date date) {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    public static Date asCommonDate(final Date date) {
        java.util.Date result;

        if (date instanceof Timestamp) {
            Timestamp timestamp = (Timestamp) date;
            result = new java.util.Date(timestamp.getTime() + timestamp.getNanos() / 1000000);
        } else {
            long millis = date.getTime();
            result = new java.util.Date(millis - millis % 1000);
        }

        return result;
    }

    public static XMLGregorianCalendar xmlCalendarFrom(final Date date) {
        if(date == null) return null;
        try {
            final XMLGregorianCalendar startDate = DatatypeFactory.newInstance().newXMLGregorianCalendar();
            final Calendar calendar = calendarAtDate(date);
            startDate.setYear(calendar.get(Calendar.YEAR));
            startDate.setMonth(calendar.get(Calendar.MONTH) + 1);
            startDate.setDay(calendar.get(Calendar.DAY_OF_MONTH));
            startDate.setHour(calendar.get(Calendar.HOUR_OF_DAY));
            startDate.setMinute(calendar.get(Calendar.MINUTE));
            startDate.setSecond(calendar.get(Calendar.SECOND));
            return startDate;
        }
        catch (DatatypeConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    public static Date atTheEndOfTheMonth(final Date date) {
        Calendar cal = calendarAtDate(date);

        cal.add(Calendar.MONTH, 1);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        return midnightOn(cal.getTime());
    }

    /**
     * @param date1 the first date
     * @param date2 the second date
     * @return the absolute differnce in days (date1 - date2)Note than anything less than 24 hours is chopped off the
     *         result, so 2 dates with a difference of less than 24 hours will show 0 days
     */
    public static long differenceInDays(final Date date1, final Date date2) {
        final Calendar endCal = calendarAtDate(date1);
        final Calendar startCal = calendarAtDate(date2);

        final long endMills = endCal.getTimeInMillis() + endCal.getTimeZone().getOffset(endCal.getTimeInMillis());
        final long startMills =
                startCal.getTimeInMillis() + startCal.getTimeZone().getOffset(startCal.getTimeInMillis());
        return (endMills - startMills) / MILLIS_IN_A_DAY;
    }

    public static int age(Date dob) { return ageAsOf(dob, new Date()); }

    public static int ageAsOf(Date dob, Date asOf){
        Calendar dobCal = calendarAtDate(dob);
        Calendar asOfCal = calendarAtDate(asOf);

        int asOfYear = asOfCal.get(Calendar.YEAR);
        int result = asOfYear - dobCal.get(Calendar.YEAR);

        dobCal.set(Calendar.YEAR, asOfYear);
        if (dobCal.after(asOfCal)) result--;
        return result;

    }

    public static String dateAsString(final Date date, final String format) {
        return new SimpleDateFormat(format).format(date);
    }

    public static String todayAsString(final String format) { return dateAsString(today(), format);}

    public static Date today() {return new Date();}

    public static Date fromNow(final int field, final int i) { return from(today(), field, i); }

    public static Date daysFrom(final Date date, final int days) { return from(date, Calendar.DAY_OF_MONTH, days); }

    public static Date weeksFrom(final Date date, final int weeks) { return from(date, Calendar.WEEK_OF_MONTH, weeks); }

    public static Date monthsFrom(final Date date, final int months) { return from(date, Calendar.MONTH, months); }

    public static Date yearsFrom(final Date date, final int years) { return from(date, Calendar.YEAR, years); }

    public static Date daysFromNow(final int days) { return daysFrom(today(), days); }

    public static Date weeksFromNow(final int weeks) { return weeksFrom(today(), weeks); }

    public static Date monthsFromNow(final int months) { return monthsFrom(today(), months); }

    public static Date yearsFromNow(final int years) { return yearsFrom(today(), years); }

    public static Date tomorrow() {return daysFromNow(1);}

    public static Date aWeekFromNow() {return weeksFromNow(1);}

    public static Date nextWeek() { return aWeekFromNow(); }

    public static Date aMonthFromNow() {return monthsFromNow(1);}

    public static Date nextMonth() {return aMonthFromNow(); }

    public static Date aYearFromNow() {return yearsFromNow(1);}

    public static Date nextYear() { return aYearFromNow();}

    public static Date daysAgo(final int i) { return daysFromNow(-i);}

    public static Date weeksAgo(final int i) {return weeksFromNow(-i);}

    public static Date monthsAgo(final int i) {return monthsFromNow(-i);}

    public static Date yearsAgo(final int i) {return yearsFromNow(-i);}

    public static Date yesterday() {return daysAgo(1);}

    public static Date aWeekAgo() {return weeksAgo(1);}

    public static Date lastWeek() {return aWeekAgo();}

    public static Date aMonthAgo() {return monthsAgo(1);}

    public static Date aYearAgo() {return yearsAgo(1);}

    public static Date lastYear() { return aYearAgo();}

    public static Date aYearFrom(final Date date) { return yearsFrom(date, 1); }
}