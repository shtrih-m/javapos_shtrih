/*
 * ReadEJReportDates.java
 *
 * Created on 16 January 2009, 14:30
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

 Get Data Of Daily Totals Report In Dates Range From 
 electronic journal

 Command:  B8H. Length: 12 bytes.
 ·	System Administrator password (4 bytes) 30
 ·	Report type (1 byte) «0» – short, «1» – full
 ·	Date of first daily totals in range (3 bytes) DD-MM-YY
 ·	Date of last daily totals in range (3 bytes) DD-MM-YY

 Answer:		B8H. Length: 18 bytes.
 ·	Result Code (1 byte)
 ·	ECR model (16 bytes) string of WIN1251 code page characters

 NOTE: Command execution may take up to 100 seconds.

 ****************************************************************************/

import com.shtrih.ej.EJDate;

public final class ReadEJDayReportOnDates extends PrinterCommand {
    // in
    private final int password;
    private final int reportType;
    private final EJDate date1;
    private final EJDate date2;
    // out
    private String ecrModel = "";

    /**
     * Creates a new instance of ReadEJReportDates
     */
    public ReadEJDayReportOnDates(int password, int reportType, EJDate date1,
            EJDate date2) {
        this.password = password;
        this.reportType = reportType;
        this.date1 = date1;
        this.date2 = date2;
    }

    
    public final int getCode() {
        return 0xB8;
    }

    
    public final String getText() {
        return "Read electronic journal report in date range";
    }

    
    public final void encode(CommandOutputStream out) throws Exception {
        out.writeInt(password);
        out.writeByte(reportType);
        out.writeDate(date1);
        out.writeDate(date2);
    }

    
    public final void decode(CommandInputStream in) throws Exception {
        ecrModel = in.readString(in.getSize());
    }

    public String getEcrModel() {
        return ecrModel;
    }
}
