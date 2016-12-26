/*
 * SaveZReport.java
 *
 * Created on January 16 2009, 14:46
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
 * Buffer Z-Report Command: C6H. Length: 5 bytes. · Administrator or System
 * Administrator password (4 bytes) 29, 30 Answer: C6H. Length: 3 bytes. ·
 * Result Code (1 byte) · Operator index number (1 byte) 29, 30
 ****************************************************************************/
public final class BufferZReport extends PrinterCommand {
    // in

    private int password;
    // out
    private int operator;

    /**
     * Creates a new instance of SaveZReport
     */
    public BufferZReport() {
        super();
    }

    public final int getCode() {
        return 0xC6;
    }

    public final String getText() {
        return "Buffer Z-report";
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
