/*
 * PrintEJReportDepartmentDates.java
 *
 * Created on 16 January 2009, 12:10
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

 Print EJ Department Report In Dates Range

 Command:  A0H. Length: 13 bytes.
 ·	System Administrator password (4 bytes) 30
 ·	Report type (1 byte) «0» – short, «1» – full
 ·	Department number (1 byte) 1…16
 ·	Date of first daily totals in range (3 bytes) DD-MM-YY
 ·	Date of last daily totals in range (3 bytes) DD-MM-YY

 Answer:		A0H. Length: 2 bytes.
 ·	Result Code (1 byte)

 NOTE: Command execution may take up to 150 seconds.

 ****************************************************************************/
import com.shtrih.fiscalprinter.command.PrinterDate;

public final class PrintEJDepartmentReportOnDates extends PrinterCommand {
    // in
    private final int password;
    private final int reportType;
    private final int department;
    private final PrinterDate date1;
    private final PrinterDate date2;

    /**
     * Creates a new instance of PrintEJReportDepartmentDates
     */
    public PrintEJDepartmentReportOnDates(int password, int reportType,
            int department, PrinterDate date1, PrinterDate date2) {
        this.password = password;
        this.reportType = reportType;
        this.department = department;
        this.date1 = date1;
        this.date2 = date2;
    }

    
    public final int getCode() {
        return 0xA0;
    }

    
    public final String getText() {
        return "Print electronic journal department report in date range";
    }

    
    public final void encode(CommandOutputStream out) throws Exception {
        out.writeInt(password);
        out.writeByte(reportType);
        out.writeByte(department);
        out.writeDateDMY(date1);
        out.writeDateDMY(date2);
    }

    
    public final void decode(CommandInputStream in) throws Exception {
    }
}
