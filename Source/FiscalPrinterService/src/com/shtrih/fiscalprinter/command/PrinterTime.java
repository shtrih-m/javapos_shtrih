/*
 * PrinterTime.java
 *
 * Created on April 2 2008, 17:02
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

public class PrinterTime {

    private final int hour;
    private final int min;
    private final int sec;

    public PrinterTime(int hour, int min, int sec) {
        this.hour = hour;
        this.min = min;
        this.sec = sec;
    }

    public PrinterTime() {
        Calendar calendar = new GregorianCalendar();
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        min = calendar.get(Calendar.MINUTE);
        sec = calendar.get(Calendar.SECOND);
    }

    public int getHour() {
        return hour;
    }

    public int getMin() {
        return min;
    }

    public int getSec() {
        return sec;
    }

    // 01:02:09
    public static String toString(PrinterTime time) {
        return StringUtils.intToStr(time.getHour(), 2) + ":"
                + StringUtils.intToStr(time.getMin(), 2) + ":"
                + StringUtils.intToStr(time.getSec(), 2);
    }

    public String toString() {
        return toString(this);
    }

    // 01:02:09
    public static PrinterTime fromText(String text) {
        StringTokenizer tokenizer = new StringTokenizer(text, ":");
        int hour = Integer.parseInt(tokenizer.nextToken());
        int min = Integer.parseInt(tokenizer.nextToken());
        int sec = Integer.parseInt(tokenizer.nextToken()) % 100;
        return new PrinterTime(hour, min, sec);
    }

    public static boolean compare(PrinterTime time1, PrinterTime time2) {
        return (time1.getHour() == time2.getHour())
                && (time1.getMin() == time2.getMin())
                && (time1.getSec() == time2.getSec());
    }
}
