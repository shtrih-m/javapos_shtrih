/*
 * CloseEJArhive.java
 *
 * Created on 16 January 2009, 13:09
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
 * Close electronic journal archive Command: AAH. Length: 5 bytes. · System
 * Administrator password (4 bytes) 30 Answer: AAH. Length: 2 bytes. · Result
 * Code (1 byte)
 ****************************************************************************/
public final class CloseEJArhive extends PrinterCommand {
    // in
    private int password;

    /**
     * Creates a new instance of CloseEJArhive
     */
    public CloseEJArhive() {
        super();
    }

    public final int getCode() {
        return 0xAA;
    }

    public final String getText() {
        return "Close electronic journal archive";
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
