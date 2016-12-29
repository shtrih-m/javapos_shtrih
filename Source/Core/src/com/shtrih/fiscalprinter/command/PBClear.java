/*
 * PBClear.java
 *
 * Created on January 16 2009, 15:23
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
 * Clear print buffer Command: CAH. Message length: 5 bytes. · Operator password
 * (4 bytes) Answer: CAH. Message length: 2 bytes · Result code (1 byte)
 ****************************************************************************/
public final class PBClear extends PrinterCommand {
    // in

    private int password;

    /** Creates a new instance of PBClear */
    public PBClear() {
        super();
    }

    public final int getCode() {
        return 0xCA;
    }

    public final String getText() {
        return "Clear print buffer";
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
