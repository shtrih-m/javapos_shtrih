/*
 * ReadSubtotal.java
 *
 * Created on 2 April 2008, 20:36
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
 * Get receipt subtotal Command: 89H. Length: 5 bytes. · Operator password (4
 * bytes) Answer: 89H. Length: 8 bytes. · Result Code (1 byte) · Operator index
 * number (1 byte) 1…30 · Receipt Subtotal (5 bytes) 0000000000…9999999999
 ****************************************************************************/

public final class ReadSubtotal extends PrinterCommand {
    // in
    private final int password;
    // out
    private int operator;
    private long sum;

    /**
     * Creates a new instance of ReadSubtotal
     */
    public ReadSubtotal(int password) {
        this.password = password;
    }

    public final int getCode() {
        return 0x89;
    }

    public final String getText() {
        return "Get receipt subtotal";
    }

    public final void encode(CommandOutputStream out) throws Exception {
        out.writeInt(password);
    }

    public final void decode(CommandInputStream in) throws Exception {
        operator = in.readByte();
        sum = in.readLong(5);
    }

    public int getOperator() {
        return operator;
    }

    public long getSum() {
        return sum;
    }

    public boolean getIsRepeatable() {
        return true;
    }
}
