/*
 * ReadEJReport.java
 *
 * Created on 16 January 2009, 14:09
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
 * Get data of electronic journal report Command: B3H. Length: 5 bytes. · System
 * Administrator password (4 bytes) 30 Answer: B3H. Length: (2+X) bytes. ·
 * Result Code (1 byte) · Report part or line (X bytes)
 ****************************************************************************/

public final class ReadEJDocumentLine extends PrinterCommand {
    // in
    private int password;
    // out
    private String data;

    /**
     * Creates a new instance of ReadEJReport
     */
    public ReadEJDocumentLine() {
    }

    public final int getCode() {
        return 0xB3;
    }

    public final String getText() {
        return "Read electronic journal report line";
    }

    public final void encode(CommandOutputStream out) throws Exception {
        out.writeInt(getPassword());
    }

    public final void decode(CommandInputStream in) throws Exception {
        setData(in.readString());
    }

    public String getData() {
        return data;
    }

    public int getPassword() {
        return password;
    }

    public void setPassword(int password) {
        this.password = password;
    }

    public void setData(String data) {
        this.data = data;
    }
}
