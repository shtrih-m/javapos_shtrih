/*
 * PrintTaxReport.java
 *
 * Created on January 15 2009, 13:22
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
 * Print Taxes Report Command: 43H. Length: 5 bytes. · Administrator or System
 * Administrator password (4 bytes) 29, 30 Answer: 43H. Length: 3 bytes. ·
 * Result Code (1 byte) · Operator index number (1 byte) 29, 30
 ****************************************************************************/

public final class PrintTaxReport extends PrinterCommand {

    // in params
    private final int password;
    // out params
    private int operator;

    /** Creates a new instance of PrintTaxReport */
    public PrintTaxReport(int password) {
        this.password = password;
    }

    public final int getCode() {
        return 0x43;
    }

    public final String getText() {
        return "Print tax report";
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
