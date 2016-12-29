/*
 * PrintCashIn.java
 *
 * Created on 2 April 2008, 20:18
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.shtrih.fiscalprinter.command;

import com.shtrih.util.MethodParameter;

/**
 *
 * @author V.Kravtsov
 */
/****************************************************************************
 * Cash In Command: 50H. Length: 10 bytes. · Operator password (4 bytes) · Sum
 * to be cashed in (5 bytes) Answer: 50H. Length: 5 bytes. · Result Code (1
 * byte) · Operator index number (1 byte) 1…30 · Current receipt number (2
 * bytes)
 ****************************************************************************/
public final class PrintCashIn extends PrinterCommand {
    // in

    private int password = 0; // Operator password (4 bytes)
    private long amount = 0; // Sum to be cashed in (5 bytes)
    // out
    private int operator = 0; // Operator index number (1 byte) 1…30
    private int receiptNumber = 0; // Current receipt number (2 bytes)

    /**
     * Creates a new instance of PrintCashIn
     */
    public PrintCashIn() {
        super();
    }

    public final int getCode() {
        return 0x50;
    }

    public final String getText() {
        return "Cash-in";
    }

    public final void encode(CommandOutputStream out) throws Exception {
        MethodParameter.checkRange(getAmount(), 0, 0xFFFFFFFFFFL, "sum");
        out.writeInt(getPassword());
        out.writeLong(getAmount(), 5);
    }

    public final void decode(CommandInputStream in) throws Exception {
        setOperator(in.readByte());
        setReceiptNumber(in.readShort());
    }

    public int getOperator() {
        return operator;
    }

    public int getReceiptNumber() {
        return receiptNumber;
    }

    public int getPassword() {
        return password;
    }

    public long getAmount() {
        return amount;
    }

    public void setPassword(int password) {
        this.password = password;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public void setOperator(int operator) {
        this.operator = operator;
    }

    public void setReceiptNumber(int receiptNumber) {
        this.receiptNumber = receiptNumber;
    }
}
