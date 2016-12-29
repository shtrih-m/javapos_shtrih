/*
 * VoidFiscalReceipt.java
 *
 * Created on 2 April 2008, 20:32
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
 * Cancel receipt Command: 88H. Length: 5 bytes. · Operator password (4 bytes)
 * Answer: 88H. Length: 3 bytes. · Result Code (1 byte) · Operator index number
 * (1 byte) 1…30
 *****************************************************************************/

public final class VoidFiscalReceipt extends PrinterCommand {
    // in
    private int password = 0; // Operator password (4 bytes)
    // out
    private int operator = 0; // Operator index number (1 byte) 1…30

    /**
     * Creates a new instance of VoidFiscalReceipt
     */
    public VoidFiscalReceipt() {
    }

    public final int getCode() {
        return 0x88;
    }

    public final String getText() {
        return "Cancel receipt";
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
     * @param operator the operator to set
     */
    public void setOperator(int operator) {
        this.operator = operator;
    }
}
