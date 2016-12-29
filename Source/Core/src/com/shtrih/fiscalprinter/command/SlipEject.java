/*
 * SlipEject.java
 *
 * Created on January 15 2009, 13:10
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
 * Eject Slip Command: 2AH. Length: 6 bytes. · Operator password (4 bytes) ·
 * Slip paper eject direction (1 byte) «0» – down, «1» – up Answer: 2AH. Length:
 * 3 bytes. · Result Code (1 byte) · Operator index number (1 byte) 1…30
 ****************************************************************************/

public final class SlipEject extends PrinterCommand {

    // in params
    private final int password;
    private final int direction;
    // out params
    private int operator;

    /** Creates a new instance of SlipEject */
    public SlipEject(int password, int direction) {
        this.password = password;
        this.direction = direction;
    }

    public final int getCode() {
        return 0x2A;
    }

    public final String getText() {
        return "Eject slip document";
    }

    public final void encode(CommandOutputStream out) throws Exception {
        out.writeInt(password);
        out.writeInt(direction);
    }

    public final void decode(CommandInputStream in) throws Exception {
        operator = in.readByte();
    }

    public int getOperator() {
        return operator;
    }
}
