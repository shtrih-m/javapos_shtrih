/*
 * PrintCashOut.java
 *
 * Created on 2 April 2008, 20:20
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
/**
 * **************************************************************************
 * Cash-Out Command: 51H. Length: 10 bytes. · Operator password (4 bytes) · Sum
 * to be cashed out (5 bytes) Answer: 51H. Length: 5 bytes. · Result Code (1
 * byte) · Operator index number (1 byte) 1…30 · Current receipt number (2
 * bytes)
 */
public final class PrintCashOut extends PrinterCommand {

    // in
    private int password = 0; // Operator password (4 bytes)
    private long amount = 0; // Sum to be cashed in (5 bytes)
    // out
    private int operator = 0; // Operator index number (1 byte) 1…30
    private int receiptNumber = 0; // Current receipt number (2 bytes)

    /**
     * Creates a new instance of PrintCashOut
     */
    public PrintCashOut() {
    }

    public final int getCode() {
        return 0x51;
    }

    public final String getText() {
        return "Cash-out";
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

    public long getAmount() {
        return amount;
    }

    public int getOperator() {
        return operator;
    }

    public int getReceiptNumber() {
        return receiptNumber;
    }

    /**
     * @return the password
     */
    public int getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(int password) {
        this.password = password;
    }

    /**
     * @param amount the amount to set
     */
    public void setAmount(long amount) {
        this.amount = amount;
    }

    /**
     * @param operator the operator to set
     */
    public void setOperator(int operator) {
        this.operator = operator;
    }

    /**
     * @param receiptNumber the receiptNumber to set
     */
    public void setReceiptNumber(int receiptNumber) {
        this.receiptNumber = receiptNumber;
    }
}
