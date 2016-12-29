/*
 * ReadEJDayJournal.java
 *
 * Created on 16 January 2009, 14:13
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
 * Get Data Of electronic journal Daily Log Report Command: B4H. Length: 7
 * bytes. · System Administrator password (4 bytes) 30 · Day number (2 bytes)
 * 0000…2100 Answer: B4H. Length: 18 bytes. · Result Code (1 byte) · ECR model
 * (16 bytes) string of WIN1251 code page characters NOTE: Command execution may
 * take up to 40 seconds.
 ****************************************************************************/
public final class ReadEJDayReport extends PrinterCommand {
    // in

    private final int password;
    private final int dayNumber;
    // out
    private String ecrModel = "";

    /**
     * Creates a new instance of ReadEJDayJournal
     */
    public ReadEJDayReport(int password, int dayNumber) {
        this.password = password;
        this.dayNumber = dayNumber;
    }

    public final int getCode() {
        return 0xB4;
    }

    public final String getText() {
        return "Read electronic journal dayNumber journal";
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
