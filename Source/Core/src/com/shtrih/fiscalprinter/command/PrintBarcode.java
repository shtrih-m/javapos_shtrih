/*
 * PrintBarcode.java
 *
 * Created on March 7 2008, 14:38
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.shtrih.fiscalprinter.command;

/**
 *
 * @author V.Kravtsov
 */
/*****************************************************************************
 * Print barcode 
 * Command: C2H. Length: 10 bytes. 
 * - Operator password (4 bytes) ·
 * - Barcode (5 bytes) 000000000000…999999999999 
 * Answer: C2H. Length: 3 bytes. ·
 * - Result Code (1 byte)
 * - Operator index number (1 byte) 1…30
 *****************************************************************************/
public final class PrintBarcode extends PrinterCommand {
    // in

    private int password; // operator password
    private String barcode; // barcode text
    // out
    private int operator;

    public PrintBarcode() {
        super();
    }

    public final int getCode() {
        return 0xC2;
    }

    public final String getText() {
        return "Print barcode";
    }

    public final void encode(CommandOutputStream out) throws Exception {
        out.writeInt(getPassword());
        out.writeLong(Long.parseLong(getBarcode()), 5);
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

    public String getBarcode() {
        return barcode;
    }

    public void setPassword(int password) {
        this.password = password;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public void setOperator(int operator) {
        this.operator = operator;
    }
}
