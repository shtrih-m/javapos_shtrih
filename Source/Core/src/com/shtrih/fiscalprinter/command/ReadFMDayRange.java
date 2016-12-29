/*
 * ReadFMDayRange.java
 *
 * Created on 15 January 2009, 14:09
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.shtrih.fiscalprinter.command;

/**
 *
 * @author V.Kravtsov
 */

/****************************************************************************
 * Get Dates And Days Ranges In FM Command: 64H. Length: 5 bytes. · Tax Officer
 * password (4 bytes) Answer: 64H. Length: 12 bytes. · Result Code (1 byte) ·
 * Date of first daily totals record in FM (3 bytes) DD-MM-YY · Date of last
 * daily totals record in FM (3 bytes) DD-MM-YY · Number of first daily totals
 * record in FM (2 bytes) 0000…2100 · Number of last daily totals record in FM
 * (2 bytes) 0000…2100
 ****************************************************************************/

public class ReadFMDayRange {
    // in params
    private final int password;
    // out params
    private PrinterDate firstDate;
    private PrinterDate lastDate;
    private int firstDay;
    private int lastDay;

    /**
     * Creates a new instance of ReadFMDayRange
     */
    public ReadFMDayRange(int password) {
        this.password = password;
    }

    public final int getCode() {
        return 0x64;
    }

    public final String getText() {
        return "Get dates and Days ranges in FM";
    }

    public final void encode(CommandOutputStream out) throws Exception {
        out.writeInt(password);
    }

    public final void decode(CommandInputStream in) throws Exception {
        firstDate = in.readDate();
        lastDate = in.readDate();
        firstDay = in.readShort();
        lastDay = in.readShort();
    }

    public PrinterDate getFirstDate() {
        return firstDate;
    }

    public PrinterDate getLastDate() {
        return lastDate;
    }

    public int getFirstDay() {
        return firstDay;
    }

    public int getLastDay() {
        return lastDay;
    }
}
