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

import java.util.GregorianCalendar;

import jpos.JposConst;
import jpos.JposException;

import com.shtrih.fiscalprinter.command.PrinterDate;
import com.shtrih.fiscalprinter.command.PrinterTime;
import com.shtrih.util.Localizer;

public class JposFiscalPrinterDate implements JposConst {
    private final int day;
    private final int month;
    private final int year;
    private final int hour;
    private final int min;

    public JposFiscalPrinterDate(int day, int month, int year, int hour, int min) {
        this.day = day;
        this.month = month;
        this.year = year;
        this.hour = hour;
        this.min = min;
    }

    public JposFiscalPrinterDate(PrinterDate date, PrinterTime time) {
        this.day = date.getDay();
        this.month = date.getMonth();
        this.year = 2000 + date.getYear();
        this.hour = time.getHour();
        this.min = time.getMin();
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

    private static int stringToInt(String value, int index, int len, String text)
            throws JposException {
        String substring = "";
        try {
            substring = value.substring(index, index + len);
            return Integer.parseInt(substring);
        } catch (Exception e) {
            throw new JposException(JPOS_E_ILLEGAL, e.getMessage());
        }
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

    public static void checkInt(String date, int value, int minValue,
            int maxValue, String valueText) throws Exception {
        String S = "";
        if (value < minValue) {
            throw new JposException(JPOS_E_ILLEGAL,
                    Localizer.getString(Localizer.invalidParameterValue)
                            + "Date");
        }
        if (value > maxValue) {
            throw new JposException(JPOS_E_ILLEGAL,
                    Localizer.getString(Localizer.invalidParameterValue)
                            + "Date");
        }
    }

    public JposFiscalPrinterDate currentDate() {
        GregorianCalendar calendar = new GregorianCalendar();
        return new JposFiscalPrinterDate(
                calendar.get(GregorianCalendar.DAY_OF_MONTH),
                calendar.get(GregorianCalendar.MONTH),
                calendar.get(GregorianCalendar.YEAR),
                calendar.get(GregorianCalendar.HOUR_OF_DAY),
                calendar.get(GregorianCalendar.MINUTE));
    }

    public static JposFiscalPrinterDate valueOf(String date) throws Exception {
        int _day = stringToInt(date, 0, 2, "day");
        int _month = stringToInt(date, 2, 2, "month");
        int _year = stringToInt(date, 4, 4, "year");
        int _hour = stringToInt(date, 8, 2, "hour");
        int _min = stringToInt(date, 10, 2, "minute");

        checkInt(date, _day, 1, 31, "day");
        checkInt(date, _month, 1, 12, "month");
        checkInt(date, _year, 0, 9999, "year");
        checkInt(date, _hour, 0, 23, "hour");
        checkInt(date, _min, 0, 59, "minute");

        return new JposFiscalPrinterDate(_day, _month, _year, _hour, _min);
    }

    public String toString() {
        return intToString(day, 2) + intToString(month, 2)
                + intToString(year, 4) + intToString(hour, 2)
                + intToString(min, 2);
    }

    public PrinterDate getPrinterDate() {
        return new PrinterDate(day, month, year - 2000);
    }

    public PrinterTime getPrinterTime() {
        return new PrinterTime(hour, min, 0);
    }
}
