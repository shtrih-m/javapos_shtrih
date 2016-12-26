/*
 * CloseNonFiscal.java
 *
 * Created on January 16 2009, 16:57
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
 * Close nonfiscal document Command: E3H. Message length: 5 bytes. · OPerator
 * password (4 bytes) Answer: E3H. Message length: 3 bytes. · Result code (1
 * byte) · Operator number (1 byte) 1…30 This command closes nonfiscal document
 ****************************************************************************/
public final class CloseNonFiscal extends PrinterCommand {
    // in

    private int password;
    // out
    private int operator;

    /**
     * Creates a new instance of CloseNonFiscal
     */
    public CloseNonFiscal() {
        super();
    }

    public final int getCode() {
        return 0xE3;
    }

    public final String getText() {
        return "Close non fiscal document";
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
