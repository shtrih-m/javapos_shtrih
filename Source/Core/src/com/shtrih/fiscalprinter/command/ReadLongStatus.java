/*
 * CommandReadLongStatus.java
 *
 * Created on 2 April 2008, 19:14
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
 * Get FP Status Command: 11H. Length: 5 bytes. · Operator password (4 bytes)
 * Answer: 11H. Length: 48 bytes. · Result Code (1 byte) · Operator index number
 * (1 byte) 1…30 · FP firmware version (2 bytes) · FP firmware build (2 bytes) ·
 * FP firmware date (3 bytes) DD-MM-YY · Number of FP in checkout line (1 byte)
 * · Current receipt number (2 bytes) · FP flags (2 bytes) · FP mode (1 byte) ·
 * FP submode (1 byte) · FP port (1 byte) · FM firmware version (2 bytes) · FM
 * firmware build (2 bytes) · FM firmware date (3 bytes) DD-MM-YY · Current date
 * (3 bytes) DD-MM-YY · Current time (3 bytes) HH-MM-SS · FM flags (1 byte) ·
 * Serial number (4 bytes) · Number of last daily totals record in FM (2 bytes)
 * 0000…2100 · Quantity of free daily totals records left in FM (2 bytes) · Last
 * fiscalization/refiscalization record number in FM (1 byte) 1…16 · Quantity of
 * free fiscalization/refiscalization records left in FM (1 byte) 0…15 ·
 * Taxpayer ID (6 bytes)
 ****************************************************************************/

public class ReadLongStatus extends PrinterCommand {
    // in
    private int password;
    // out params
    private LongPrinterStatus status = null;

    /**
     * Creates a new instance of CommandReadLongStatus
     */
    public ReadLongStatus() {
    }

    public final int getCode() {
        return 0x11;
    }

    public final String getText() {
        return "Get status";
    }

    public final void encode(CommandOutputStream out) throws Exception {
        out.writeInt(getPassword());
    }

    public final void decode(CommandInputStream in) throws Exception {
        setStatus(new LongPrinterStatus(in));
    }

    public LongPrinterStatus getStatus() {
        return status;
    }

    public boolean getIsRepeatable() {
        return true;
    }

    /**
     * @return the password
     */
    public int getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(int password) {
        this.password = password;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(LongPrinterStatus status) {
        this.status = status;
    }
}
