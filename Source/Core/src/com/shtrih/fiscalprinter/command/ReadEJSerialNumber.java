/*
 * ReadEJSerialNumber.java
 *
 * Created on 16 January 2009, 13:13
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
 * Get electronic journal serial number Command: ABH. Length: 5 bytes. · System
 * Administrator password (4 bytes) 30 Answer: ABH. Length: 7 bytes. · Result
 * Code (1 byte) · Electronic journal serial number (5 bytes)
 * 0000000000…9999999999
 ****************************************************************************/
public final class ReadEJSerialNumber extends PrinterCommand {
    // in
    private int password;
    // out
    private long serial;

    /**
     * Creates a new instance of ReadEJSerialNumber
     */
    public ReadEJSerialNumber() {
    }

    public final int getCode() {
        return 0xAB;
    }

    public final String getText() {
        return "Read electronic journal serial number";
    }

    public final void encode(CommandOutputStream out) throws Exception {
        out.writeInt(getPassword());
    }

    public final void decode(CommandInputStream in) throws Exception {
        setSerial(in.readLong(5));
    }

    public int getPassword() {
        return password;
    }

    public void setPassword(int password) {
        this.password = password;
    }

    public long getSerial() {
        return serial;
    }

    public void setSerial(long serial) {
        this.serial = serial;
    }
}
