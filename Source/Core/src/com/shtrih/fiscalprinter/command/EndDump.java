/*
 * EndDump.java
 *
 * Created on 2 April 2008, 18:03
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
 * Stop Getting Data From Dump Command: 03H. Length: 5 bytes. · System
 * Administrator password (4 bytes) 30 Answer: 03H. Length: 2 bytes. · Result
 * Code (1 byte)
 *****************************************************************************/
public final class EndDump extends PrinterCommand {
    // in

    private int password;

    /**
     * Creates a new instance of EndDump
     */
    public EndDump() {
        super();
    }

    public final int getCode() {
        return 0x03;
    }

    public final String getText() {
        return "End dump";
    }

    public final void encode(CommandOutputStream out) throws Exception {
        out.writeInt(getPassword());
    }

    public final void decode(CommandInputStream in) throws Exception {
    }

    public int getPassword() {
        return password;
    }

    public void setPassword(int password) {
        this.password = password;
    }
}
