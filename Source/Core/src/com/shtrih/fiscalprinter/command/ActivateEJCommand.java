/*
 * ActivateEJCommand.java
 *
 * Created on 16 January 2009, 13:07
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
 * Activate electronic journal Command: A9H. Length: 5 bytes. · System
 * Administrator password (4 bytes) 30 Answer: A9H. Length: 2 bytes. · Result
 * Code (1 byte)
 ****************************************************************************/
public final class ActivateEJCommand extends PrinterCommand {
    // in
    private int password;

    /**
     * Creates a new instance of ActivateEJCommand
     */
    public ActivateEJCommand() {
        super();
    }

    public final int getCode() {
        return 0xA9;
    }

    public final String getText() {
        return "Electronic journal activation";
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
