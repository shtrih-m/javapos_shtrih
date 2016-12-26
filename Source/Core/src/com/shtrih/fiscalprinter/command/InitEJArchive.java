/*
 * InitEJArchive.java
 *
 * Created on 16 January 2009, 14:03
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
 * Initialize electronic journal archive Command: B2H. Length: 5 bytes. · System
 * Administrator password (4 bytes) 30 Answer: B2H. Length: 2 bytes. · Result
 * Code (1 byte) NOTE: Command can be executed only with electronic journal
 * development kit. Command execution may take up to 20 seconds.
 ****************************************************************************/
public final class InitEJArchive extends PrinterCommand {
    // in
    private int password;

    /**
     * Creates a new instance of InitEJArchive
     */
    public InitEJArchive() {
        super();
    }

    public final int getCode() {
        return 0xB2;
    }

    public final String getText() {
        return "Initialize electronic journal archive";
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
