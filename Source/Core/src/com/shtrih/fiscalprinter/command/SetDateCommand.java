/*
 * SetDateCommand.java
 *
 * Created on April 2 2008, 21:02
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
 * Set Calendar Date Command: 22H. Length: 8 bytes. · System Administrator
 * password (4 bytes) 30 · Date (3 bytes) DD-MM-YY Answer: 22H. Length: 2 bytes.
 * · Result Code (1 byte)
 ****************************************************************************/

public final class SetDateCommand extends PrinterCommand {
    // in
    private final int password;
    private final PrinterDate date;

    /**
     * Creates a new instance of SetDateCommand
     */
    public SetDateCommand(int password, PrinterDate date) {
        this.password = password;
        this.date = date;
    }

    public final int getCode() {
        return 0x22;
    }

    public final String getText() {
        return "Set date";
    }

    public final void encode(CommandOutputStream out) throws Exception {
        out.writeInt(password);
        out.writeDate(date);
    }

    public final void decode(CommandInputStream in) throws Exception {
    }
}
