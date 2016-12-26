/*
 * PrintEJDocument.java
 *
 * Created on 16 January 2009, 12:55
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
 * Print document copy CRC from electronic journal Command: A5H. Length: 9
 * bytes. · System Administrator password (4 bytes) 30 · KPK number (4 bytes)
 * 00000000…99999999 Answer: A5H. Length: 2 bytes. · Result Code (1 byte) NOTE:
 * Command execution may take up to 40 seconds.
 ****************************************************************************/
public final class PrintEJDocument extends PrinterCommand {
    // in
    private final int password;
    private final int macNumber;

    /**
     * Creates a new instance of PrintEJDocument
     */
    public PrintEJDocument(int password, int macNumber) {
        this.password = password;
        this.macNumber = macNumber;
    }

    public final int getCode() {
        return 0xA5;
    }

    public final String getText() {
        return "Print electronic journal document";
    }

    public final void encode(CommandOutputStream out) throws Exception {
        out.writeInt(password);
        out.writeInt(macNumber);
    }

    public final void decode(CommandInputStream in) throws Exception {
    }
}
