/*
 * CancelEJOperation.java
 *
 * Created on 16 January 2009, 13:16
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
 * Cancel active electronic journal operation Command: ACH. Length: 5 bytes. ·
 * System Administrator password (4 bytes) 30 Answer: ACH. Length: 2 bytes. ·
 * Result Code (1 byte)
 ****************************************************************************/
public final class CancelEJDocument extends PrinterCommand {
    // in

    private int password;

    /**
     * Creates a new instance of CancelEJOperation
     */
    public CancelEJDocument() {
        super();
    }

    public final int getCode() {
        return 0xAC;
    }

    public final String getText() {
        return "Cancel active operation";
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
