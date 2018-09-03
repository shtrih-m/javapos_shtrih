/*
 * PrinterDate.java
 *
 * Created on April 2 2008, 17:04
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.shtrih.fiscalprinter.command;

/**
 *
 * @author V.Kravtsov
 */
import com.shtrih.util.MathUtils;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.StringTokenizer;
import java.util.TimeZone;

import com.shtrih.util.StringUtils;

public class PrinterDate {

    public static PrinterDate now() {
        return new PrinterDate();
    }

    private final int day;
    private final int month;
    private final int year;

    public PrinterDate(int day, int month, int year) {
        this.day = day;
        this.month = month;
        this.year = year;
    }

    public PrinterDate() {
        Calendar calendar = new GregorianCalendar();
        this.day = calendar.get(Calendar.DAY_OF_MONTH);
        this.month = calendar.get(Calendar.MONTH) + 1;
        this.year = calendar.get(Calendar.YEAR);
    }

    public int getDay() {
        return day;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public String toJposString() {
        return StringUtils.intToStr(day, 2)
                + StringUtils.intToStr(month, 2)
                + StringUtils.intToStr(year, 4);
    }

    public String toString() {
        return StringUtils.intToStr(day, 2) + "."
                + StringUtils.intToStr(month, 2) + "."
                + StringUtils.intToStr(year, 4);
    }

    public String toStringShort() {
        return StringUtils.intToStr(day, 2) + "."
                + StringUtils.intToStr(month, 2) + "."
                + StringUtils.intToStr(year %100, 2);
    }

    // 01.02.09
    public String toText() {
        return StringUtils.intToStr(getDay(), 2) + "."
                + StringUtils.intToStr(getMonth(), 2) + "."
                + StringUtils.intToStr(getYear(), 4);
    }

    // 01.02.2009 or 01.02.09
    public static PrinterDate parse(String text) {
        StringTokenizer tokenizer = new StringTokenizer(text, ".");
        int day = Integer.parseInt(tokenizer.nextToken());
        int month = Integer.parseInt(tokenizer.nextToken());
        int year = Integer.parseInt(tokenizer.nextToken());
        return new PrinterDate(day, month, year);
    }

    public boolean isEqual(PrinterDate date) {
        return compare(date) == 0;
    }

    public boolean isEqualOrOlder(PrinterDate date) {
        return compare(date) <= 0;
    }

    public boolean isOlder(PrinterDate date) {
        return compare(date) == -1;
    }

    public int compare(PrinterDate date) {
        int rc = MathUtils.compare(year, date.getYear());
        if (rc != 0) {
            return rc;
        }
        rc = MathUtils.compare(month, date.getMonth());
        if (rc != 0) {
            return rc;
        }
        rc = MathUtils.compare(day, date.getDay());
        return rc;
    }
}
