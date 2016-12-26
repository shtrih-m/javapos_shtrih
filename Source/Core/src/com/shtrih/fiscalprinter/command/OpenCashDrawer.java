/*
 * OpenCashDrawer.java
 *
 * Created on April 23 2008, 21:36
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
 * Open Cash Drawer Command: 28H. Length: 6 bytes. · Operator password (4 bytes)
 * · Cash drawer number (1 byte) 0, 1 Answer: 28H. Length: 3 bytes. · Result
 * Code (1 byte) · Operator index number (1 byte) 1…30
 ****************************************************************************/
public class OpenCashDrawer extends PrinterCommand {
    // in

    private int password; // Operator password (4 bytes)
    private int drawerNumber; // Cash drawer number (1 byte) 0, 1
    // out
    private int operator = 0; // Operator index number (1 byte) 1…30

    /** Creates a new instance of CutReceiptCommand */
    public OpenCashDrawer() {
        super();
    }

    public final int getCode() {
        return 0x28;
    }

    public final String getText() {
        return "Open cash drawer";
    }

    public final void encode(CommandOutputStream out) throws Exception {
        out.writeInt(getPassword());
        out.writeByte(getDrawerNumber());
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

    public int getDrawerNumber() {
        return drawerNumber;
    }

    public void setPassword(int password) {
        this.password = password;
    }

    public void setDrawerNumber(int drawerNumber) {
        this.drawerNumber = drawerNumber;
    }

    public void setOperator(int operator) {
        this.operator = operator;
    }
}
