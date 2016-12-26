/*
 * PBGetLine.java
 *
 * Created on January 16 2009, 15:00
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
 * Read print buffer line Command: C9H. Message length: 7 bytes. · Operator
 * password (4 bytes) · Line number (2 bytes) Answer: C9H. Message length: 2 + n
 * bytes · Result code (1 byte) · Line data (n bytes)
 ****************************************************************************/
public final class PBGetLine extends PrinterCommand {
    // in

    private int password;
    private int lineNumber;
    // out
    private String lineText = "";

    /** Creates a new instance of PBGetLine */
    public PBGetLine() {
        super();
    }

    public final int getCode() {
        return 0xC9;
    }

    public final String getText() {
        return "Read print buffer line";
    }

    public final void encode(CommandOutputStream out) throws Exception {
        out.writeInt(getPassword());
        out.writeShort(getLineNumber());
    }

    public final void decode(CommandInputStream in) throws Exception {
        setLineText(in.readString(in.getSize()));
    }

    public int getPassword() {
        return password;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public String getLineText() {
        return lineText;
    }

    public void setPassword(int password) {
        this.password = password;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    public void setLineText(String lineText) {
        this.lineText = lineText;
    }
}
