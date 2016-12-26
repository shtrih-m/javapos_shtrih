/*
 * PrintEJReportDays.java
 *
 * Created on 16 January 2009, 12:34
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.shtrih.fiscalprinter.command;

/**
 *
 * @author V.Kravtsov
 */
/**
 * **************************************************************************
 * Print Daily Totals Report In Daily Totals Numbers Range from electronic
 * journal Command: A3H. Length: 10 bytes. · System Administrator password (4
 * bytes) 30 · Report type (1 byte) «0» – short, «1» – full · First day number
 * (2 bytes) 0000…2100 · Last day number (2 bytes) 0000…2100 Answer: A3H.
 * Length: 2 bytes. · Result Code (1 byte) NOTE: Command execution may take up
 * to 100 seconds.
 ***************************************************************************
 */
public final class PrintEJDayReportOnDays extends PrinterCommand {
    // in

    private int password;
    private int reportType;
    private int dayNumber1;
    private int dayNumber2;

    /**
     * Creates a new instance of PrintEJReportDays
     */
    public PrintEJDayReportOnDays() {
    }

    public final int getCode() {
        return 0xA3;
    }

    public final String getText() {
        return "Print electronic journal dayNumber report in dayNumber range";
    }

    public final void encode(CommandOutputStream out) throws Exception {
        out.writeInt(getPassword());
        out.writeByte(getReportType());
        out.writeShort(getDayNumber1());
        out.writeShort(getDayNumber2());
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
     * @return the dayNumber1
     */
    public int getDayNumber1() {
        return dayNumber1;
    }

    /**
     * @param dayNumber1 the dayNumber1 to set
     */
    public void setDayNumber1(int dayNumber1) {
        this.dayNumber1 = dayNumber1;
    }

    /**
     * @return the dayNumber2
     */
    public int getDayNumber2() {
        return dayNumber2;
    }

    /**
     * @param dayNumber2 the dayNumber2 to set
     */
    public void setDayNumber2(int dayNumber2) {
        this.dayNumber2 = dayNumber2;
    }
}
