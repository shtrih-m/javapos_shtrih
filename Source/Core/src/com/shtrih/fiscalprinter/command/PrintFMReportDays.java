/*
 * PrintFMReportDays.java
 *
 * Created on April 2 2008, 20:41
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
 * Print fiscal report for daily totals numbers range Command: 67H. Length: 10
 * bytes. · Tax Officer password (4 bytes) · Report type (1 byte) «0» – short,
 * «1» – full · Day number of first daily totals record in FM (2 bytes)
 * 0000…2100 · Day number of last daily totals record in FM (2 bytes) 0000…2100
 * Answer: 67H. Length: 12 bytes. · Result Code (1 byte) · Date of first daily
 * totals record in FM (3 bytes) DD-MM-YY · Date of last daily totals record in
 * FM (3 bytes) DD-MM-YY · Number of first daily totals record in FM (2 bytes)
 * 0000…2100 · Number of last daily totals record in FM (2 bytes) 0000…2100
 ***************************************************************************
 */
public final class PrintFMReportDays extends PrinterCommand {
    // in

    private int password;
    private int session1;
    private int session2;
    private int reportType;
    // out
    private int sessionNumber1 = 0;
    private int sessionNumber2 = 0;
    private PrinterDate sessionDate1 = new PrinterDate();
    private PrinterDate sessionDate2 = new PrinterDate();

    /**
     * Creates a new instance of PrintFMReportDays
     */
    public PrintFMReportDays() {
    }

    public final int getCode() {
        return 0x67;
    }

    public final String getText() {
        return "Print fiscal report for daily totals numbers range";
    }

    public final void encode(CommandOutputStream out) throws Exception {
        out.writeInt(getPassword());
        out.writeByte(getReportType());
        out.writeShort(getSession1());
        out.writeShort(getSession2());
    }

    public final void decode(CommandInputStream in) throws Exception {
        setSessionDate1(in.readDate());
        setSessionDate2(in.readDate());
        setSessionNumber1(in.readShort());
        setSessionNumber2(in.readShort());
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
     * @return the session1
     */
    public int getSession1() {
        return session1;
    }

    /**
     * @param session1 the session1 to set
     */
    public void setSession1(int session1) {
        this.session1 = session1;
    }

    /**
     * @return the session2
     */
    public int getSession2() {
        return session2;
    }

    /**
     * @param session2 the session2 to set
     */
    public void setSession2(int session2) {
        this.session2 = session2;
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
     * @return the sessionNumber1
     */
    public int getSessionNumber1() {
        return sessionNumber1;
    }

    /**
     * @param sessionNumber1 the sessionNumber1 to set
     */
    public void setSessionNumber1(int sessionNumber1) {
        this.sessionNumber1 = sessionNumber1;
    }

    /**
     * @return the sessionNumber2
     */
    public int getSessionNumber2() {
        return sessionNumber2;
    }

    /**
     * @param sessionNumber2 the sessionNumber2 to set
     */
    public void setSessionNumber2(int sessionNumber2) {
        this.sessionNumber2 = sessionNumber2;
    }

    /**
     * @return the sessionDate1
     */
    public PrinterDate getSessionDate1() {
        return sessionDate1;
    }

    /**
     * @param sessionDate1 the sessionDate1 to set
     */
    public void setSessionDate1(PrinterDate sessionDate1) {
        this.sessionDate1 = sessionDate1;
    }

    /**
     * @return the sessionDate2
     */
    public PrinterDate getSessionDate2() {
        return sessionDate2;
    }

    /**
     * @param sessionDate2 the sessionDate2 to set
     */
    public void setSessionDate2(PrinterDate sessionDate2) {
        this.sessionDate2 = sessionDate2;
    }
}
