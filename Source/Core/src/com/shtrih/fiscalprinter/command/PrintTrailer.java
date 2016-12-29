/*
 * PrintTrailer.java
 *
 * Created on January 15 2009, 13:36
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
 * Print receipt trailer Command: 54H. Message length: 5 bytes. · OPerator
 * pasword (4 bytes) Answer: 54H. Message length: 3 bytes. · Result code (1
 * byte) · Operator number (1 byte) 1…30
 ****************************************************************************/

public final class PrintTrailer extends PrinterCommand {
    // in params
    private final int password;
    // out params
    private int operator;

    /** Creates a new instance of PrintTrailer */
    public PrintTrailer(int password) {
        this.password = password;
    }

    public final int getCode() {
        return 0x54;
    }

    public final String getText() {
        return "Print fixed trailer";
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
