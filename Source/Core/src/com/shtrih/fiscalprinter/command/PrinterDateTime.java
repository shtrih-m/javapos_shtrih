package com.shtrih.fiscalprinter.command;

import com.shtrih.util.StringUtils;
import java.util.Calendar;

public class PrinterDateTime {
    public static final int BodyLength = 5;

    private final int year;

    private final int month;

    private final int day;

    private final int hours;

    private final int minutes;

    public PrinterDateTime(byte[] data) {

        if (data.length != BodyLength)
            throw new RuntimeException("Incorrect data length. Expecte 5, but was " + data.length);

        year = data[0] + 2000;
        month = data[1];
        day = data[2];
        hours = data[3];
        minutes = data[4];
    }

    public PrinterDateTime(PrinterDate date, PrinterTime time) {
        year = date.getYear();
        month = date.getMonth();
        day = date.getDay();
        hours = time.getHour();
        minutes = time.getMin();
    }

    public Calendar getCalendar()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.set(getYear(), getMonth(), getDay(),
            getHours(), getMinutes(), 0);
        return calendar;
    }
    
    public boolean IsZero() {
        return year == 2000 && month == 0 && day == 0 && hours == 0 && minutes == 0;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public int getHours() {
        return hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public String toString() {
        return StringUtils.intToStr(day, 2) + "."
                + StringUtils.intToStr(month, 2) + "."
                + StringUtils.intToStr(year, 4) + " "
                + StringUtils.intToStr(hours, 2) + ":"
                + StringUtils.intToStr(minutes, 2);
    }
}
