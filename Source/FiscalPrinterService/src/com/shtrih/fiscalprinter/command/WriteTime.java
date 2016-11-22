/*
 * WriteTime.java
 *
 * Created on April 2 2008, 20:59
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.shtrih.fiscalprinter.command;

/**
 *
 * @author V.Kravtsov
 */

/*****************************************************************************
 * Set Clock Time Command: 21H. Length: 8 bytes. · System Administrator password
 * (4 bytes) 30 · Time (3 bytes) HH-MM-SS Answer: 21H. Length: 2 bytes. · Result
 * Code (1 byte)
 *****************************************************************************/

public final class WriteTime extends PrinterCommand {
    // in
    private final int password;
    private final PrinterTime time;

    /**
     * Creates a new instance of WriteTime
     */
    public WriteTime(int password, PrinterTime time) {
        this.password = password;
        this.time = time;
    }

    public final int getCode() {
        return 0x21;
    }

    public final String getText() {
        return "Set clock time";
    }

    public final void encode(CommandOutputStream out) throws Exception {
        out.writeInt(password);
        out.writeTime(time);
    }

    public final void decode(CommandInputStream in) throws Exception {
    }
}
