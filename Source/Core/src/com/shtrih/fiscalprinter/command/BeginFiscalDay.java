/*
 * BeginFiscalDay.java
 *
 * Created on January 16 2009, 16:29
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
 * Open Fiscal Day Command: E0H. Length: 5 bytes. · Operator password (4 bytes)
 * Answer: E0H. Length: 2 bytes. · Operator index number (1 byte) 1…30 NOTE:
 * Command opens new fiscal day in FM and changes FP mode to «2 Open Fiscal
 * Day».
 ****************************************************************************/
public final class BeginFiscalDay extends PrinterCommand {
    // in

    private int password;
    // out
    private int operator;

    /**
     * Creates a new instance of BeginFiscalDay
     */
    public BeginFiscalDay() {
        super();
    }

    public final int getCode() {
        return 0xE0;
    }

    public final String getText() {
        return "Open fiscal day";
    }

    public final void encode(CommandOutputStream out) throws Exception {
        out.writeInt(getPassword());
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

    public void setPassword(int password) {
        this.password = password;
    }

    public void setOperator(int operator) {
        this.operator = operator;
    }
}
