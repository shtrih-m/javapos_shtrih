/*
 * PrintString.java
 *
 * Created on April 2 2008, 19:29
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
 * Print String Command: 17H. Length: 46 bytes. · Operator password (4 bytes) ·
 * Flags (1 byte) Bit 0 – print on journal station, Bit 1 – print on receipt
 * station. · String of characters to print (40 bytes) Answer: 17H. Length: 3
 * bytes. · Result Code (1 byte) · Operator index number (1 byte) 1…30 NOTE:
 * Only WIN1251 code page characters can be printed. Characters with codes 0 to
 * 31 are ignored.
 ****************************************************************************/

public class PrintString extends PrinterCommand {

    // in params
    private final int password;
    private final int station;
    private final String line;
    // out
    private int operator;

    /**
     * Creates a new instance of PrintString
     */
    public PrintString(int password, int station, String line) {
        this.password = password;
        this.station = station;
        this.line = line;
    }

    public final int getCode() {
        return 0x17;
    }

    public final String getText() {
        return "Print string";
    }

    public final void encode(CommandOutputStream out) throws Exception {
        out.writeInt(password);
        out.writeByte(station);
        out.writeString(line, PrinterConst.MIN_TEXT_LENGTH);
    }

    public final void decode(CommandInputStream in) throws Exception {
        operator = in.readByte();
    }

    public int getOperator() {
        return operator;
    }

    public String getLine() {
        return line;
    }
}
