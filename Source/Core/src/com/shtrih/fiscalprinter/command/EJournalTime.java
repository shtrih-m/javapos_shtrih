/*
 * EJournalTime.java
 *
 * Created on 14 Май 2010 г., 14:06
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.shtrih.fiscalprinter.command;

/**
 *
 * @author V.Kravtsov
 */

import java.util.StringTokenizer;

import com.shtrih.util.StringUtils;

public class EJournalTime {

    private final int hour;
    private final int min;

    /** Creates a new instance of EJournalTime */
    public EJournalTime(int hour, int min) {
        this.hour = hour;
        this.min = min;
    }

    public int getHour() {
        return hour;
    }

    public int getMin() {
        return min;
    }

    // 01:02
    public static String toText(EJournalTime time) {
        return StringUtils.intToStr(time.getHour(), 2) + ":"
                + StringUtils.intToStr(time.getMin(), 2);
    }

    // 01:02
    public static EJournalTime fromText(String text) {
        StringTokenizer tokenizer = new StringTokenizer(text, ":");
        int hour = Integer.parseInt(tokenizer.nextToken());
        int min = Integer.parseInt(tokenizer.nextToken());
        return new EJournalTime(hour, min);
    }

    public static boolean compare(EJournalTime time1, EJournalTime time2) {
        return (time1.getHour() == time2.getHour())
                && (time1.getMin() == time2.getMin());
    }

}
