/*
 * PrintEJReportDates.java
 *
 * Created on 16 January 2009, 12:22
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

 Print Daily Totals Report In Dates Range From electronic journal

 Command:  A2H. Length: 12 bytes.
 ·	System Administrator password (4 bytes) 30
 ·	Report type (1 byte) «0» – short, «1» – full
 ·	Date of first daily totals in range (3 bytes) DD-MM-YY
 ·	Date of last daily totals in range (3 bytes) DD-MM-YY

 Answer:		A2H. Length: 2 bytes.
 ·	Result Code (1 byte)

 NOTE: Command execution may take up to 100 seconds.

 ****************************************************************************/

import com.shtrih.ej.EJDate;

public final class PrintEJDayReportOnDates extends PrinterCommand {
    // in

    private int password;
    private int reportType;
    private EJDate date1;
    private EJDate date2;

    /**
     * Creates a new instance of PrintEJReportDates
     */
    public PrintEJDayReportOnDates() {
    }

    
    public final int getCode() {
        return 0xA2;
    }

    
    public final String getText() {
        return "Print electronic journal report in date range";
    }

    
    public final void encode(CommandOutputStream out) throws Exception {
        out.writeInt(getPassword());
        out.writeByte(getReportType());
        out.writeDate(getDate1());
        out.writeDate(getDate2());
    }

    
    public final void decode(CommandInputStream in) throws Exception {
    }

    /**
     * @return the password
     */
    public int getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(int password) {
        this.password = password;
    }

    /**
     * @return the reportType
     */
    public int getReportType() {
        return reportType;
    }

    /**
     * @param reportType the reportType to set
     */
    public void setReportType(int reportType) {
        this.reportType = reportType;
    }

    /**
     * @return the date1
     */
    public EJDate getDate1() {
        return date1;
    }

    /**
     * @param date1 the date1 to set
     */
    public void setDate1(EJDate date1) {
        this.date1 = date1;
    }

    /**
     * @return the date2
     */
    public EJDate getDate2() {
        return date2;
    }

    /**
     * @param date2 the date2 to set
     */
    public void setDate2(EJDate date2) {
        this.date2 = date2;
    }
}
