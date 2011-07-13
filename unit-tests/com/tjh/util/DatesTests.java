package com.tjh.util;

import com.tjh.util.Dates;
import org.junit.Before;
import org.junit.Test;

import javax.xml.datatype.XMLGregorianCalendar;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import static com.tjh.util.Dates.asCommonDate;
import static com.tjh.util.Dates.daysFromNow;
import static com.tjh.util.Dates.differenceInDays;
import static com.tjh.util.Dates.from;
import static com.tjh.util.Dates.midnightOn;
import static com.tjh.util.Dates.weeksFrom;
import static com.tjh.util.Dates.weeksFromNow;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class DatesTests {
    private Date now;

    @Test
    public void differenceInDaysReturnsCorrectValue() {
        assertThat(differenceInDays(daysFromNow(1), now), equalTo(1L));
    }

    @Test
    public void weeksFromReturnsCorrectValue() {
        assertThat(differenceInDays(weeksFrom(now, 1), now), equalTo(7L));
    }

    @Test
    public void weeksFromNowReturnsCorrectValue() {
        assertThat(differenceInDays(weeksFromNow(2), now), equalTo(14L));
    }

    @Test
    public void fromReturnsCorrectValue() {
        assertThat(differenceInDays(from(now, Calendar.DAY_OF_YEAR, 3), now), equalTo(3L));
    }

    @Test
    public void midnightOnSetsTimeAppropriately() {
        Date midnightToday = midnightOn(now);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(midnightToday);
        assertThat(calendar.get(Calendar.AM_PM), is(Calendar.AM));
        assertThat(calendar.get(Calendar.HOUR_OF_DAY), equalTo(0));
        assertThat(calendar.get(Calendar.MINUTE), equalTo(0));
        assertThat(calendar.get(Calendar.SECOND), equalTo(0));
        assertThat(calendar.get(Calendar.MILLISECOND), equalTo(0));
    }

    @Test
    public void asCommonDateStripsNanosFromTimestamp() {
        final Timestamp timestamp = new Timestamp(now.getTime());
        assertThat(asCommonDate(timestamp).getTime(), equalTo(timestamp.getTime() + timestamp.getNanos() / 1000000));
    }

    @Test
    public void asCommonDateStripsMillisFromDate() {
        assertThat(asCommonDate(now), equalTo(new Date(now.getTime() - now.getTime() % 1000)));
    }

    @Test
    public void xmlCalendarFrom() {
        XMLGregorianCalendar xmlCalendar = Dates.xmlCalendarFrom(now);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        assertThat(xmlCalendar.getYear(), equalTo(calendar.get(Calendar.YEAR)));
        assertThat(xmlCalendar.getMonth(), equalTo(calendar.get(Calendar.MONTH) + 1));
        assertThat(xmlCalendar.getDay(), equalTo(calendar.get(Calendar.DAY_OF_MONTH)));
    }

    @Before
    public void before() {
        now = new Date();
    }
}
