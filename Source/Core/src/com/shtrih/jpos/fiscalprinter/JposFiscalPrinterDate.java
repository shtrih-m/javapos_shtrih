/*
 * JposFiscalPrinterDate.java
 *
 * Created on April 18 2008, 12:44
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.shtrih.jpos.fiscalprinter;

/**
 *
 * @author V.Kravtsov
 */
import java.util.TimeZone;
import java.util.GregorianCalendar;

import jpos.JposConst;
import jpos.JposException;

import com.shtrih.fiscalprinter.command.PrinterDate;
import com.shtrih.fiscalprinter.command.PrinterTime;
import com.shtrih.util.Localizer;
import com.shtrih.util.StringUtils;

public class JposFiscalPrinterDate implements JposConst {

    private final int day;
    private final int month;
    private final int year;
    private final int hour;
    private final int min;
    private final int sec;

    public JposFiscalPrinterDate(int day, int month, int year, int hour, int min) throws Exception {
        checkInt(day, 1, 31, "day");
        checkInt(month, 1, 12, "month");
        checkInt(year, 0, 9999, "year");
        checkInt(hour, 0, 23, "hour");
        checkInt(min, 0, 59, "minute");

        this.day = day;
        this.month = month;
        this.year = year;
        this.hour = hour;
        this.min = min;
        this.sec = 0;
    }

    public JposFiscalPrinterDate(int day, int month, int year, int hour, int min, int sec) throws Exception {
        checkInt(day, 1, 31, "day");
        checkInt(month, 1, 12, "month");
        checkInt(year, 0, 9999, "year");
        checkInt(hour, 0, 23, "hour");
        checkInt(min, 0, 59, "minute");
        checkInt(sec, 0, 59, "seconds");

        this.day = day;
        this.month = month;
        this.year = year;
        this.hour = hour;
        this.min = min;
        this.sec = sec;
    }

    public JposFiscalPrinterDate(PrinterDate date, PrinterTime time) {
        this.day = date.getDay();
        this.month = date.getMonth();
        this.year = date.getYear();
        this.hour = time.getHour();
        this.min = time.getMin();
        this.sec = time.getSec();
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

    public int getHour() {
        return hour;
    }

    public int getMin() {
        return min;
    }

    public int getSec() {
        return sec;
    }

    private String intToString(int value, int len) {
        String result = String.valueOf(value);
        if (result.length() > len) {
            result = String.copyValueOf(result.toCharArray(), 0, len);
        }
        int count = len - result.length();
        for (int i = 0; i < count; i++) {
            result = "0" + result;
        }
        return result;
    }

    public static void checkInt(int value, int minValue,
            int maxValue, String valueText) throws Exception {
        String S = "";
        if (value < minValue) {
            throw new JposException(JPOS_E_ILLEGAL,
                    Localizer.getString(Localizer.invalidParameterValue)
                    + valueText);
        }
        if (value > maxValue) {
            throw new JposException(JPOS_E_ILLEGAL,
                    Localizer.getString(Localizer.invalidParameterValue)
                    + valueText);
        }
    }

    public JposFiscalPrinterDate currentDate() throws Exception {
        GregorianCalendar calendar = new GregorianCalendar();
        return new JposFiscalPrinterDate(
                calendar.get(GregorianCalendar.DAY_OF_MONTH),
                calendar.get(GregorianCalendar.MONTH),
                calendar.get(GregorianCalendar.YEAR),
                calendar.get(GregorianCalendar.HOUR_OF_DAY),
                calendar.get(GregorianCalendar.MINUTE),
                calendar.get(GregorianCalendar.SECOND));
    }

    public static JposFiscalPrinterDate parseDateTime(String date) throws Exception {
        int day = StringUtils.stringToInt(date, 0, 2, "day");
        int month = StringUtils.stringToInt(date, 2, 2, "month");
        int year = StringUtils.stringToInt(date, 4, 4, "year");
        int hour = StringUtils.stringToInt(date, 8, 2, "hour");
        int min = StringUtils.stringToInt(date, 10, 2, "minute");
        int sec = 0;
        if (date.length() >= 14) {
            sec = StringUtils.stringToInt(date, 12, 2, "seconds");
        }
        return new JposFiscalPrinterDate(day, month, year, hour, min, sec);
    }

    public static JposFiscalPrinterDate parseDate(String date) throws Exception {
        int day = StringUtils.stringToInt(date, 0, 2, "day");
        int month = StringUtils.stringToInt(date, 2, 2, "month");
        int year = StringUtils.stringToInt(date, 4, 4, "year");
        return new JposFiscalPrinterDate(day, month, year, 0, 0, 0);
    }

    public String toString() {
        return intToString(day, 2) + intToString(month, 2)
                + intToString(year, 4) + intToString(hour, 2)
                + intToString(min, 2) + intToString(sec, 2);
    }

    public PrinterDate getPrinterDate() {
        return new PrinterDate(day, month, year);
    }

    public PrinterTime getPrinterTime() {
        return new PrinterTime(hour, min, sec);
    }

}
