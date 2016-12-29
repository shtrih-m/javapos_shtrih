/*
 * SlipClearLine.java
 *
 * Created on January 16 2009, 11:42
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
 * Clear Non-fiscal Data From Slip Line Buffer Command: 7BH. Length: 6 bytes. ·
 * Operator password (4 bytes) · Slip line number (1 byte) 1…200 Answer: 7BH.
 * Length: 3 bytes. · Result Code (1 byte) · Operator index number (1 byte) 1…30
 ****************************************************************************/

public final class SlipClearLine extends PrinterCommand {
    // in params
    private final int password;
    private final int line;
    // out params
    private int operator;

    /** Creates a new instance of SlipClearLine */
    public SlipClearLine(int password, int line) {
        this.password = password;
        this.line = line;
    }

    public final int getCode() {
        return 0x7B;
    }

    public final String getText() {
        return "Clear slip line";
    }

    public final void encode(CommandOutputStream out) throws Exception {
        out.writeInt(password);
        out.writeByte(line);
    }

    public final void decode(CommandInputStream in) throws Exception {
        operator = in.readByte();
    }

    public int getOperator() {
        return operator;
    }

}
