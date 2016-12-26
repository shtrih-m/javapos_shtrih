/*
 * SlipConfigureUnv.java
 *
 * Created on January 16 2009, 11:55
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
 * Set Universal Configuration Of Slip Command: 7EH. Length: 11 bytes. ·
 * Operator password (4 bytes) · Slip width in printer steps (2 bytes)* · Slip
 * length in printer steps (2 bytes)* · Printing direction – clockwise rotation
 * in degrees (1 byte) «0» – 0?, «1» – 90?, «2» – 180?, «3» – 270? · Spacing
 * between lines in printer steps (1 byte)* Answer: 7EH. Length: 3 bytes. ·
 * Result Code (1 byte) · Operator index number (1 byte) 1…30 printer step value
 * depends on the printing mechanism of a specific fiscal printer. The
 * horizontal step value differs from vertical step value: these parameters are
 * given in User Guide documentation for a specific fiscal printer.
 ****************************************************************************/

public final class SlipConfigureUnv extends PrinterCommand {

    // in params
    private final int password;
    private final byte slipWidth;
    private final byte slipLength;
    private final byte direction;
    private final byte spacing;
    // out params
    private int operator;

    /** Creates a new instance of SlipConfigureUnv */
    public SlipConfigureUnv(int password, byte slipWidth, byte slipLength,
            byte direction, byte spacing) {
        this.password = password;
        this.slipWidth = slipWidth;
        this.slipLength = slipLength;
        this.direction = direction;
        this.spacing = spacing;
    }

    public final int getCode() {
        return 0x7E;
    }

    public final String getText() {
        return "Set universal slip configuration";
    }

    public final void encode(CommandOutputStream out) throws Exception {
        out.writeInt(password);
        out.writeByte(slipWidth);
        out.writeByte(slipLength);
        out.writeByte(direction);
        out.writeByte(spacing);
    }

    public final void decode(CommandInputStream in) throws Exception {
        operator = in.readByte();
    }

    public int getOperator() {
        return operator;
    }
}
