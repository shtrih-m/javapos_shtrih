/*
 * ReadLicense.java
 *
 * Created on 15 January 2009, 12:42
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
 * Read License Command: 1DH. Length: 5 bytes. · System Administrator password
 * (4 bytes) 30 Answer: 1DH. Length: 7 bytes. · Result Code (1 byte) · License
 * (5 bytes) 0000000000…9999999999
 ****************************************************************************/
public final class ReadLicense extends PrinterCommand {
    // in params

    private final int password;
    // out params
    private long license;

    /**
     * Creates a new instance of ReadLicense
     */
    public ReadLicense(int password) {
        this.password = password;
    }

    public final int getCode() {
        return 0x1D;
    }

    public final String getText() {
        return "Get license";
    }

    public final void encode(CommandOutputStream out) throws Exception {
        out.writeInt(password);
    }

    public final void decode(CommandInputStream in) throws Exception {
        license = in.readLong(5);
    }

    public long getLicense() {
        return license;
    }

    public boolean getIsRepeatable() {
        return true;
    }
}
