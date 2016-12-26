/*
 * ReadEJReportDays.java
 *
 * Created on 16 January 2009, 14:32
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
 * Get Data Of Daily Totals Report In Daily Totals Numbers Range From electronic
 * journal Command: B9H. Length: 10 bytes. · System Administrator password (4
 * bytes) 30 · Report type (1 byte) «0» – short, «1» – full · Number of first
 * daily totals in range (2 bytes) 0000…2100 · Number of last daily totals in
 * range (2 bytes) 0000…2100 Answer: B9H. Length: 18 bytes. · Result Code (1
 * byte) · ECR model (16 bytes) string of WIN1251 code page characters NOTE:
 * Command execution may take up to 100 seconds.
 ****************************************************************************/
public final class ReadEJDayReportOnDays extends PrinterCommand {
    // in

    private final int password;
    private final int reportType;
    private final int dayNumber1;
    private final int dayNumber2;
    // out
    private String ecrModel = "";

    /**
     * Creates a new instance of ReadEJReportDays
     */
    public ReadEJDayReportOnDays(int password, int reportType, int dayNumber1,
            int dayNumber2) {
        this.password = password;
        this.reportType = reportType;
        this.dayNumber1 = dayNumber1;
        this.dayNumber2 = dayNumber2;
    }

    public final int getCode() {
        return 0xB9;
    }

    public final String getText() {
        return "Read electronic journal dayNumber report in dayNumber range";
    }

    public final void encode(CommandOutputStream out) throws Exception {
        out.writeInt(password);
        out.writeByte(reportType);
        out.writeShort(dayNumber1);
        out.writeShort(dayNumber2);
    }

    public final void decode(CommandInputStream in) throws Exception {
        ecrModel = in.readString(in.getSize());
    }

    public String getEcrModel() {
        return ecrModel;
    }
}
