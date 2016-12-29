/*
 * SlipConfigure.java
 *
 * Created on January 16 2009, 11:25
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
 * Configure Slip Command: 78H. Length: 209 bytes. · Operator password (4 bytes)
 * · Slip width in printer steps (2 bytes)* · Slip length in printer steps (2
 * bytes)* · Printing direction – clockwise rotation in degrees (1 byte) «0» –
 * 0?, «1» – 90?, «2» – 180?, «3» – 270? · Spacing between Line 1 and Line 2 in
 * printer steps (1 byte)* · Spacing between Line 2 and Line 3 in printer steps
 * (1 byte)* · And so on for Lines 3…199 in printer steps (1 byte)* · Spacing
 * between Line 199 and Line 200 in printer steps (1 byte)* Answer: 78H. Length:
 * 3 bytes. · Result Code (1 byte) · Operator index number (1 byte) 1…30 printer
 * step value depends on the printing mechanism of a specific fiscal printer.
 * The horizontal step value differs from vertical step value: these parameters
 * are given in User Guide documentation for a specific fiscal printer.
 ****************************************************************************/

public final class SlipConfigure extends PrinterCommand {
    class SlipConfigureParams {
        // Slip width in printer steps (2 bytes)*
        public int slipWidth;
        // Slip length in printer steps (2 bytes)*
        public int slipLength;
        // Printing direction – clockwise rotation in degrees (1 byte)
        // «0» – 0?, «1» – 90?, «2» – 180?, «3» – 270?
        public int rotation;
        // Spacing between Line 1 and Line 2 in printer steps (1 byte)*
        // Spacing between Line 2 and Line 3 in printer steps (1 byte)*
        // And so on for Lines 3…199 in printer steps (1 byte)*
        // Spacing between Line 199 and Line 200 in printer steps (1 byte)*
        public byte[] linesSpacing = new byte[199];
    }

    // in params
    private final int password;
    private final SlipConfigureParams params;
    // out params
    private int operator;

    /** Creates a new instance of SlipConfigure */
    public SlipConfigure(int password, SlipConfigureParams params) {
        this.password = password;
        this.params = params;
    }

    public final int getCode() {
        return 0x78;
    }

    public final String getText() {
        return "Configure slip";
    }

    public final void encode(CommandOutputStream out) throws Exception {
        out.writeInt(password);
        // params
        out.writeByte(params.slipWidth);
        out.writeByte(params.slipLength);
        out.writeByte(params.rotation);
        out.writeBytes(params.linesSpacing);
    }

    public final void decode(CommandInputStream in) throws Exception {
        operator = in.readByte();
    }

    public int getOperator() {
        return operator;
    }
}
