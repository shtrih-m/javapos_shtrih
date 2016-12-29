/*
 * PrintEJReportDepartmentDays.java
 *
 * Created on 16 January 2009, 12:16
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
 * Print Department Report In Daily Totals Numbers Range From electronic journal
 * Command: A1H. Length: 11 bytes. · System Administrator password (4 bytes) 30
 * · Report type (1 byte) «0» – short, «1» – full · Department number (1 byte)
 * 1…16 · First day number (2 bytes) 0000…2100 · Last day number (2 bytes)
 * 0000…2100 Answer: A1H. Length: 2 bytes. · Result Code (1 byte) NOTE: Command
 * execution may take up to 150 seconds.
 ****************************************************************************/
public final class PrintEJDepartmentReportOnDays extends PrinterCommand {
    // in
    private final int password;
    private final int reportType;
    private final int department;
    private final int dayNumber1;
    private final int dayNumber2;

    /**
     * Creates a new instance of PrintEJReportDepartmentDays
     */
    public PrintEJDepartmentReportOnDays(int password, int reportType,
            int department, int dayNumber1, int dayNumber2) {
        this.password = password;
        this.reportType = reportType;
        this.department = department;
        this.dayNumber1 = dayNumber1;
        this.dayNumber2 = dayNumber2;
    }

    public final int getCode() {
        return 0xA1;
    }

    public final String getText() {
        return "Print electronic journal department report in dayNumber range";
    }

    public final void encode(CommandOutputStream out) throws Exception {
        out.writeInt(password);
        out.writeByte(reportType);
        out.writeByte(department);
        out.writeShort(dayNumber1);
        out.writeShort(dayNumber2);
    }

    public final void decode(CommandInputStream in) throws Exception {
    }
}
