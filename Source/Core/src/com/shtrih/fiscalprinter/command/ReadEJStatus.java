/*
 * ReadEJStatus.java
 *
 * Created on 16 January 2009, 13:18
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.shtrih.fiscalprinter.command;

/**
 *
 * @author V.Kravtsov
 */

import com.shtrih.ej.EJFlags;
import com.shtrih.ej.EJStatus;

/****************************************************************************
 * Get EJ Status 1 Command: ADH. Length: 5 bytes. · System Administrator
 * password (4 bytes) 30 Answer: ADH. Length: 22 bytes. · Result Code (1 byte) ·
 * KPK value of last fiscal receipt (5 bytes) 0000000000…9999999999 · Date of
 * last KPK (3 bytes) DD-MM-YY · Time of last KPK (2 bytes) HH-MM · Number of
 * last KPK (4 bytes) 00000000…99999999 · EJ serial number (5 bytes)
 * 0000000000…9999999999 · EJ flags (1 byte) EJ flags Bits 0&1 Flag t – Receipt
 * type marker:· «00» – Sale· «01» – Buy· «10» – Sale Refund· «11» – Buy
 * RefundFlag t value is set simultaneously with Flag d value Bit 2 Flag i – EJ
 * is activated (0 – no, 1 – yes).Flag i value turns «1» after successful
 * execution of command A9h «Activate EJ». Flag i value turns «0» after
 * successful execution of command AAh «Close EJ Archive» or in case of EJ
 * archive overflow. Bit 3 Flag f – Paper in slip station lower sensor (0 – no,
 * 1 – yes). Flag f value turns «1» after successful execution of Command A9h
 * «Activate EJ». After EJ activation Flag f value is always «1». Bit 4 Flag w –
 * EJ in report mode (0 – no, 1 – yes).Flag f value turns «1», if commands B3H –
 * BBH. Flag f value turns «0», if EJ returns «no data» upon successful
 * execution of command B3H Get Data Of EJ Report, after successful execution of
 * command ACH Cancel Active EJ Operation, and if flag a turns «1». Bit 5 Flag d
 * – Fiscal receipt open (0 – no, 1 – yes). Flag d value turns «1», if commands
 * 80H Sale, 81H Buy, 82H Sale Refund, and 82H Buy Refund have been successfully
 * executed. Flag d value turns «0», if commands 84H Void Transaction, 85H Close
 * Receipt have been successfully executed, and if flag a turns «1» Bit 6 Flag s
 * – Fiscal day open (0 – no, 1 – yes). Flag s value turns «1», if any record
 * that has time is saved in EJ archive. Flag s value turns «0», after
 * successful execution of commands A9H Activate EJ and 41H Print Z-Report. Bit
 * 7 Flag a – EJ fatal error code (0 – no fatal error, 1 – fatal error). Flag a
 * value turns «1» when a checksum or writing to archive error occurs, or when
 * EJ archive structure is corrupted.
 ****************************************************************************/
public final class ReadEJStatus extends PrinterCommand {

    // in
    private int password;
    // out
    private final EJStatus status = new EJStatus();

    /**
     * Creates a new instance of ReadEJStatus
     */
    public ReadEJStatus() {
    }

    public final int getCode() {
        return 0xAD;
    }

    public final String getText() {
        return "Read electronic journal status by code 1";
    }

    public final void encode(CommandOutputStream out) throws Exception {
        out.writeInt(getPassword());
    }

    public final void decode(CommandInputStream in) throws Exception {
        status.setDocMAC(in.readLong(5));
        status.setDocDate(in.readEJDate());
        status.setDocTime(in.readEJTime());
        status.setDocMACNumber(in.readInt());
        status.setSerialNumber(in.readLong(5));
        status.setFlags(new EJFlags(in.readByte()));
    }

    public int getPassword() {
        return password;
    }

    public void setPassword(int password) {
        this.password = password;
    }

    public EJStatus getStatus() {
        return status;
    }

}
