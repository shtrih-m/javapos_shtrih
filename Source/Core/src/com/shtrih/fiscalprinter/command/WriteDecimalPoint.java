/*
 * WriteDecimalPoint.java
 *
 * Created on January 15 2009, 12:47
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
 * Set Decimal Dot Position Command: 20H. Length: 6 bytes. · System
 * Administrator password (4 bytes) 30 · Decimal dot position (1 byte) «0» – 0
 * digits after the dot, «1» – 2 digits after the dot Answer: 20H. Length: 2
 * bytes. · Result Code (1 byte)
 ****************************************************************************/

public final class WriteDecimalPoint extends PrinterCommand {

    // in params
    private final int password;
    private final int position;

    /**
     * Creates a new instance of WriteDecimalPoint
     */
    public WriteDecimalPoint(int password, int position) {
        this.password = password;
        this.position = position;
    }

    public final int getCode() {
        return 0x20;
    }

    public final String getText() {
        return "Set decimal point position";
    }

    public final void encode(CommandOutputStream out) throws Exception {
        out.writeInt(password);
        out.writeInt(position);
    }

    public final void decode(CommandInputStream in) throws Exception {
    }

}
