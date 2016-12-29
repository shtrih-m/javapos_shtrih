/*
 * PrintHeader.java
 *
 * Created on January 15 2009, 13:24
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
 * Print Fixed Header Command: 52H. Length: 5 bytes. · Operator password (4
 * bytes) Answer: 52H. Length: 3 bytes. · Result Code (1 byte) · Operator index
 * number (1 byte) 1…30
 ****************************************************************************/

public final class PrintHeader extends PrinterCommand {

    // in params
    private final int password;
    // out params
    private int operator;

    /** Creates a new instance of PrintHeader */
    public PrintHeader(int password) {
        this.password = password;
    }

    public final int getCode() {
        return 0x52;
    }

    public final String getText() {
        return "Print fixed header";
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
