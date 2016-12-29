/*
 * WriteLicense.java
 *
 * Created on January 15 2009, 12:19
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
 * Set License Command: 1CH. Length: 10 bytes. · System Administrator password
 * (4 bytes) 30 · License (5 bytes) 0000000000…9999999999 Answer: 1CH. Length: 2
 * bytes. · Result Code (1 byte)
 ****************************************************************************/

public final class WriteLicense extends PrinterCommand {

    // in params
    private final int password;
    private final long license;

    /**
     * Creates a new instance of WriteLicense
     */
    public WriteLicense(int password, long license) {
        this.password = password;
        this.license = license;
    }

    public final int getCode() {
        return 0x1C;
    }

    public final String getText() {
        return "Set license";
    }

    public final void encode(CommandOutputStream out) throws Exception {
        out.writeInt(password);
        out.writeLong(license, 5);
    }

    public final void decode(CommandInputStream in) throws Exception {
    }
}
