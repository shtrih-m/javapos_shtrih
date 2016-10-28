/*
 * PrintGraphicLine.java
 *
 * Created on April 2 2008, 21:29
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
 * Print graphic line Command: C5H. Length: X + 7 bytes. · Operator password (4
 * bytes) · Number of repetitions (2 bytes) · Graphical data (X bytes) Answer:
 * C5H. Length: 3 bytes. · Result Code (1 byte) · Operator index number (1 byte)
 * 1…30
 ****************************************************************************/

public final class PrintGraphicLine extends PrinterCommand {
    // in
    private final int password; // Operator password
    private final int height; // Number of repetitions
    private final byte[] data; // Graphical data
    // out
    private int operator = 0;

    /**
     * Creates a new instance of PrintGraphicLine
     */
    public PrintGraphicLine(int password, int height, byte[] data) {
        this.password = password;
        this.height = height;
        this.data = data;
    }

    public final int getCode() {
        return 0xC5;
    }

    public final String getText() {
        return "Print graphic line";
    }

    public final void encode(CommandOutputStream out) throws Exception {
        out.writeInt(password);
        out.writeShort(height);
        out.writeBytes(data);
    }

    public final void decode(CommandInputStream in) throws Exception {
        operator = in.readByte();
    }

    public int getOperator() {
        return operator;
    }
}
