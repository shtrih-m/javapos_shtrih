/*
 * BeginNonFiscalDocument.java
 *
 * Created on January 16 2009, 16:54
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
 * Open nonfiscal document Command: E2H. Message length: 5 bytes. · Operator
 * password (4 bytes) Answer: E2H. Message length: 3 bytes. · Result code (1
 * byte) · Operator number (1 byte) 1…30 Command opens nonfiscal document.
 ****************************************************************************/
public final class BeginNonFiscalDocument extends PrinterCommand {
    // in

    private int password;
    // out
    private int operator;

    /**
     * Creates a new instance of BeginNonFiscalDocument
     */
    public BeginNonFiscalDocument() {
        super();
    }

    public final int getCode() {
        return 0xE2;
    }

    public final String getText() {
        return "Open non fiscal document";
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
