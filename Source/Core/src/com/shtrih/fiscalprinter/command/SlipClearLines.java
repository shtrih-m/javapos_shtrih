/*
 * SlipClearLines.java
 *
 * Created on January 16 2009, 11:45
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
 * Clear All Non-fiscal Data From Slip Buffer Command: 7CH. Length: 5 bytes. ·
 * Operator password (4 bytes) Answer: 7CH. Length: 3 bytes. · Result Code (1
 * byte) · Operator index number (1 byte) 1…30
 ****************************************************************************/

public final class SlipClearLines extends PrinterCommand {
    // in params
    private final int password;
    // out params
    private int operator;

    /** Creates a new instance of SlipClearLines */
    public SlipClearLines(int password) {
        this.password = password;
    }

    public final int getCode() {
        return 0x7C;
    }

    public final String getText() {
        return "Clear all slip lines";
    }

    public final void encode(CommandOutputStream out) throws Exception {
        out.writeInt(password);
    }

    public final void decode(CommandInputStream in) throws Exception {
        operator = in.readByte();
    }

    public int getOperator() {
        return operator;
    }
}
