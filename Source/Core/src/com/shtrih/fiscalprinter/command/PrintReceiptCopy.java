/*
 * PrintReceiptCopy.java
 *
 * Created on 2 April 2008, 21:15
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
 * Duplicate receipt Command: 8CH. Length: 5 bytes. · Operator password (4
 * bytes) Answer: 8CH. Length: 3 bytes. · Result Code (1 byte) · Operator index
 * number (1 byte) 1…30 NOTE: Command permits to print a duplicate of the last
 * closed fiscal receipt of sale, buy, sale refund, or buy refund.
 ****************************************************************************/

public final class PrintReceiptCopy extends PrinterCommand {
    // in
    private final int password;
    // out
    private int operator = 0;

    /**
     * Creates a new instance of PrintReceiptCopy
     */
    public PrintReceiptCopy(int password) {
        this.password = password;
    }

    public final int getCode() {
        return 0x8C;
    }

    public final String getText() {
        return "Duplicate receipt";
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
