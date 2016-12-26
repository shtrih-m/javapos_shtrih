/*
 * ReadEJDayTotal.java
 *
 * Created on 16 January 2009, 14:35
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
 * Get Data Of electronic journal Daily Totals Report Command: BAH. Length: 7
 * bytes. · System Administrator password (4 bytes) 30 · Number of daily totals
 * (2 bytes) 0000…2100 Answer: BAH. Length: 18 bytes. · Result Code (1 byte) ·
 * ECR model (16 bytes) string of WIN1251 code page characters NOTE: Command
 * execution may take up to 40 seconds.
 ****************************************************************************/
public final class ReadEJDayTotals extends PrinterCommand {
    // in

    private final int password;
    private final int dayNumber;
    // out
    private String ecrModel = "";

    /**
     * Creates a new instance of ReadEJDayTotal
     */
    public ReadEJDayTotals(int password, int dayNumber) {
        this.password = password;
        this.dayNumber = dayNumber;
    }

    public final int getCode() {
        return 0xBA;
    }

    public final String getText() {
        return "Read electronic journal day totals";
    }

    public final void encode(CommandOutputStream out) throws Exception {
        out.writeInt(password);
        out.writeShort(dayNumber);
    }

    public final void decode(CommandInputStream in) throws Exception {
        ecrModel = in.readString(in.getSize());
    }

    public String getEcrModel() {
        return ecrModel;
    }
}
