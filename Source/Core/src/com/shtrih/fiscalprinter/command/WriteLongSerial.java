/*
 * WriteLongSerial.java
 *
 * Created on April 2 2008, 18:09
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
 * Set Long Serial Number Command: 0EH. Length: 12 bytes. · Password (4 bytes)
 * (default password value «0») · Long Serial Number (7 bytes)
 * 00000000000000…99999999999999 Answer: 0EH. Length: 2 bytes. · Result Code (1
 * byte) NOTE: The command is introduced into this protocol to conform to the
 * Byelorussian legislation that requires Electronic Cash Registers to have
 * serial number to be 14 digits long, where as in Russia it must be 10 digits
 * long.
 ****************************************************************************/

public class WriteLongSerial extends PrinterCommand {

    private final int password; // Password (4 bytes)
    private final long serial; // Long Serial Number (7 bytes)

    /**
     * Creates a new instance of WriteLongSerial
     */
    public WriteLongSerial(int password, long serial) {
        this.password = password;
        this.serial = serial;
    }

    public final int getCode() {
        return 0x0E;
    }

    public final String getText() {
        return "Set Long Serial Number";
    }

    public final void encode(CommandOutputStream out) throws Exception {
        out.writeInt(password);
        out.writeLong(serial, 7);
    }

    public final void decode(CommandInputStream in) throws Exception {
    }
}
