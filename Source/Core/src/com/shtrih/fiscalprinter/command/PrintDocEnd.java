/*
 * PrintDocEnd.java
 *
 * Created on January 15 2009, 13:32
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
 * Print document ending Command: 53H. Message length: 6 bytes. · Operator
 * password (4 bytes) · Parameter (1 byte) · 0 - do not print receipt trailer ·
 * 1 - print receipt trailer Answer: 53H. Message length: 3 bytes. · Result code
 * (1 byte) · Operator number (1 byte) 1…30
 ****************************************************************************/
public final class PrintDocEnd extends PrinterCommand {

    // in params
    private int password;
    private byte mode;
    // out params
    private int operator;

    /** Creates a new instance of PrintDocEnd */
    public PrintDocEnd() {
        super();
    }

    public final int getCode() {
        return 0x53;
    }

    public final String getText() {
        return "Print document ending";
    }

    public final void encode(CommandOutputStream out) throws Exception {
        out.writeInt(getPassword());
        out.writeByte(getMode());
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

    public byte getMode() {
        return mode;
    }

    public void setPassword(int password) {
        this.password = password;
    }

    public void setMode(byte mode) {
        this.mode = mode;
    }

    public void setOperator(int operator) {
        this.operator = operator;
    }
}
