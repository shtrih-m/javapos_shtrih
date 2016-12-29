/*
 * ReadDump.java
 *
 * Created on April 2 2008, 17:52
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.shtrih.fiscalprinter.command;

/**
 *
 * @author V.Kravtsov
 */

/*****************************************************************************
 * Get Data Block From Dump Command: 02H. Length: 5 bytes. · Service support
 * center specialist password, or system administrator password in case Service
 * Support Center specialist password is not defined (4 bytes) Answer: 02H.
 * Length: 37 bytes. · Result Code (1 byte) · Fiscal Printer unit code (1 byte)
 * · 01 – Fiscal Memory 1 · 02 – Fiscal Memory 1 · 03 – Clock · 04 – Nonvolatile
 * memory · 05 – Fiscal Memory processor · 06 – Fiscal Printer ROM · 07 – Fiscal
 * Printer RAM · Data block number (2 bytes) · Data block contents (32 bytes)
 *****************************************************************************/

public final class ReadDump extends PrinterCommand {
    // in
    private final int password;
    // out params
    private int deviceCode;
    private int dataBlockNumber;
    private byte[] dataBlock;

    /**
     * Creates a new instance of ReadDump
     */
    public ReadDump(int password) {
        this.password = password;
    }

    public final int getCode() {
        return 0x02;
    }

    public final String getText() {
        return "Get next dump block";
    }

    public final void encode(CommandOutputStream out) throws Exception {
        out.writeInt(password);
    }

    public final void decode(CommandInputStream in) throws Exception {
        deviceCode = in.readByte();
        dataBlockNumber = in.readInt();
        dataBlock = in.readBytes(32);
    }

    public boolean getIsRepeatable() {
        return true;
    }

    public int getDeviceCode() {
        return deviceCode;
    }

    public int getDataBlockNumber() {
        return dataBlockNumber;
    }

    public byte[] getDataBlock() {
        return dataBlock;
    }

}
