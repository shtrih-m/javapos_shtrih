/*
 * OpenReceipt.java
 *
 * Created on April 2 2008, 21:17
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
 * Open Receipt Command: 8DH. Length: 6 bytes. · Operator password (4 bytes) ·
 * Receipt type (1 byte): 0 – Sale; 1 – Buy; 2 – Sale Refund; 3 – Buy Refund.
 * Answer: 8DH. Length: 3 bytes. · Result Code (1 byte) · Operator index number
 * (1 byte) 1…30
 ****************************************************************************/
public final class OpenReceipt extends PrinterCommand {
    // in

    private int password;
    private int receiptType;
    // out
    private int operator;

    /**
     * Creates a new instance of BeginFiscalReceipt
     */
    public OpenReceipt() {
        super();
    }

    public final int getCode() {
        return 0x8D;
    }

    public final String getText() {
        return "Open receipt";
    }

    public final void encode(CommandOutputStream out) throws Exception {
        out.writeInt(getPassword());
        out.writeByte(getReceiptType());
    }

    public final void decode(CommandInputStream in) throws Exception {
        setOperator(in.readByte());
    }

    public int getOperator() {
        return operator;
    }

    public int getPassword() {
        return password;
    }

    public int getReceiptType() {
        return receiptType;
    }

    public void setPassword(int password) {
        this.password = password;
    }

    public void setReceiptType(int receiptType) {
        this.receiptType = receiptType;
    }

    public void setOperator(int operator) {
        this.operator = operator;
    }
}
