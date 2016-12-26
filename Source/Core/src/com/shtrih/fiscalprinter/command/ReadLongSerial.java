/*
 * ReadLongSerial.java
 *
 * Created on 2 April 2008, 18:33
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
 * Get Long Serial Number And Long ECRRN Command: 0FH. Length: 5 bytes. ·
 * Operator password (4 bytes) Answer: 0FH. Length: 16 bytes. · Result Code (1
 * byte) · Long Serial Number (7 bytes) 00000000000000…99999999999999 · Long
 * ECRRN (7 bytes) 00000000000000…99999999999999
 ****************************************************************************/

public class ReadLongSerial extends PrinterCommand {
    // in
    private final int password; // Password (4 bytes)
    // out
    private long serial = 0; // Long Serial Number (7 bytes)
    private long ecprn = 0; // Long ECRRN (7 bytes)

    /**
     * Creates a new instance of ReadLongSerial
     */
    public ReadLongSerial(int password) {
        this.password = password;
    }

    public final int getCode() {
        return 0x0F;
    }

    public final String getText() {
        return "Get Long Serial Number And Long ECRRN";
    }

    public final void encode(CommandOutputStream out) throws Exception {
        out.writeInt(password);
    }

    public final void decode(CommandInputStream in) throws Exception {
        serial = in.readLong(7);
        ecprn = in.readLong(7);
    }

    public boolean getIsRepeatable() {
        return true;
    }
}
