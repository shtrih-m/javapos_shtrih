/*
 * PrintTotalizers.java
 *
 * Created on January 15 2009, 13:15
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
 * Print Operation Totalizers Report Command: 2CH. Length: 5 bytes. ·
 * Administrator or System Administrator password (4 bytes) 29, 30 Answer: 2CH.
 * Length: 3 bytes. · Result Code (1 byte) · Operator index number (1 byte) 29,
 * 30
 ****************************************************************************/
public final class PrintTotalizers extends PrinterCommand {
    // in params

    private final int password;
    // out params
    private int operator;

    /** Creates a new instance of PrintTotalizers */
    public PrintTotalizers(int password) {
        this.password = password;
    }

    public final int getCode() {
        return 0x2C;
    }

    public final String getText() {
        return "Print operation totalizers report";
    }

    public final void encode(CommandOutputStream out) throws Exception {
        out.writeInt(password);
    }

    public final void decode(CommandInputStream in) throws Exception {
        operator = in.readByte();
    }

    public int getOperator() {
        return operator;
    }
}
