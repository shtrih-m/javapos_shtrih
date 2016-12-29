/*
 * PrintZReport.java
 *
 * Created on April 2 2008, 20:58
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.shtrih.fiscalprinter.command;

/**
 *
 * @author V.Kravtsov
 */
/**
 * **************************************************************************
 * Print Z-Report Command: 41H. Length: 5 bytes. · Administrator or System
 * Administrator password (4 bytes) 29, 30 Answer: 41H. Length: 3 bytes. ·
 * Result Code (1 byte) · Operator index number (1 byte) 29, 30
 ***************************************************************************
 */
public final class PrintZReport extends PrinterCommand {
    // in

    private int password;
    // out
    private int operator;

    /**
     * Creates a new instance of PrintZReport
     */
    public PrintZReport() {
    }

    public final int getCode() {
        return 0x41;
    }

    public final String getText() {
        return "Print Z report";
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
