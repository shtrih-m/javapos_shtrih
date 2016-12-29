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
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.StringTokenizer;

import com.shtrih.util.StringUtils;

public class PrinterDate {

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
        this.year = calendar.get(Calendar.YEAR) - 2000;
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

    public String toString() {
        return StringUtils.intToStr(day, 2) + "."
                + StringUtils.intToStr(month, 2) + "."
                + StringUtils.intToStr(year + 2000, 4);
    }

    // 01.02.09
    public static String toText(PrinterDate date) {
        return StringUtils.intToStr(date.getDay(), 2) + "."
                + StringUtils.intToStr(date.getMonth(), 2) + "."
                + StringUtils.intToStr(date.getYear() + 2000, 4);
    }

    // 01.02.2009 or 01.02.09
    public static PrinterDate parse(String text) {
        StringTokenizer tokenizer = new StringTokenizer(text, ".");
        int day = Integer.parseInt(tokenizer.nextToken());
        int month = Integer.parseInt(tokenizer.nextToken());
        int year = Integer.parseInt(tokenizer.nextToken()) % 100;
        return new PrinterDate(day, month, year);
    }

    public static boolean compare(PrinterDate date1, PrinterDate date2) {
        return (date1.getDay() == date2.getDay())
                && (date1.getMonth() == date2.getMonth())
                && (date1.getYear() == date2.getYear());
    }

}
