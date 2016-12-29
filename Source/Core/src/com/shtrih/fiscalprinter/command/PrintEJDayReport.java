/*
 * PrintEJDayJournal.java
 *
 * Created on 16 January 2009, 12:59
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
 * Print daily log report for daily totals number from electronic journal
 * Command: A6H. Length: 7 bytes. · System Administrator password (4 bytes) 30 ·
 * Day number (2 bytes) 0000…2100 Answer: A6H. Length: 2 bytes. · Result Code (1
 * byte) NOTE: Command execution may take up to 40 seconds.
 ****************************************************************************/
public final class PrintEJDayReport extends PrinterCommand {
    // in
    private final int password;
    private final int dayNumber;

    /**
     * Creates a new instance of PrintEJDayJournal
     */
    public PrintEJDayReport(int password, int dayNumber) {
        this.password = password;
        this.dayNumber = dayNumber;
    }

    public final int getCode() {
        return 0xA6;
    }

    public final String getText() {
        return "Print electronic journal dayNumber journal";
    }

    public final void encode(CommandOutputStream out) throws Exception {
        out.writeInt(password);
        out.writeShort(dayNumber);
    }

    public final void decode(CommandInputStream in) throws Exception {
    }
}
