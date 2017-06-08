package com.shtrih.fiscalprinter.command;

public class FSDateTime {
    public static final int BodyLength = 5;

    private final int year;

    private final int month;

    private final int day;

    private final int hours;

    private final int minutes;

    public FSDateTime(byte[] data) {

        if (data.length != BodyLength)
            throw new RuntimeException("Incorrect data length. Expecte 5, but was " + data.length);

        year = data[0] + 2000;
        month = data[1];
        day = data[2];
        hours = data[3];
        minutes = data[4];
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
}
