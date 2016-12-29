/*
 * WriteSerial.java
 *
 * Created on January 15 2009, 13:27
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
 * Set Serial Number Command: 60H. Length: 9 bytes. · Password (4 bytes)
 * (default value is «0») · Serial number (4 bytes) 00000000…99999999 Answer:
 * 60H. Length: 2 bytes. · Result Code (1 byte)
 ****************************************************************************/

public final class WriteSerial extends PrinterCommand {

    // in params
    private final int password;
    private final int serial;

    /**
     * Creates a new instance of WriteSerial
     */
    public WriteSerial(int password, int serial) {
        this.password = password;
        this.serial = serial;
    }

    public final int getCode() {
        return 0x60;
    }

    public final String getText() {
        return "Set printer serial number";
    }

    public final void encode(CommandOutputStream out) throws Exception {
        out.writeInt(password);
        out.writeInt(serial);
    }

    public final void decode(CommandInputStream in) throws Exception {
    }
}
