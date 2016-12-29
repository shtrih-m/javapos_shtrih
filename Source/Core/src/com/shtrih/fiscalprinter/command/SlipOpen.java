/*
 * SlipOpen.java
 *
 * Created on January 15 2009, 14:40
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
 * Open Fiscal Slip Command: 70H. Length: 26 bytes. · Operator password (4
 * bytes) · Slip type (1 byte) «0» – Sale, «1» – Buy, «2» – Sale Refund, «3» –
 * Buy Refund · Slip duplicates type (1 byte) «0» – duplicates as columns, «1» –
 * duplicates as line blocks · Number of duplicates (1 byte) 0…5 · Spacing
 * between Original and Duplicate 1 (1 byte) * · Spacing between Duplicate 1 and
 * Duplicate 2 (1 byte) * · Spacing between Duplicate 2 and Duplicate 3 (1 byte)
 * * · Spacing between Duplicate 3 and Duplicate 4 (1 byte) * · Spacing between
 * Duplicate 4 and Duplicate 5 (1 byte) * · Font number of fixed header (1 byte)
 * · Font number of header (1 byte) · Font number of EJ serial number (1 byte) ·
 * Font number of KPK value and KPK number (1 byte) · Vertical position of the
 * first line of fixed header (1 byte) · Vertical position of the first line of
 * header (1 byte) · Vertical position of line with EJ number (1 byte) ·
 * Vertical position of line with duplicate marker (1 byte) · Horizontal
 * position of fixed header in line (1 byte) · Horizontal position of header in
 * line (1 byte) · Horizontal position of EJ number in line (1 byte) ·
 * Horizontal position of KPK value and KPK number in line (1 byte) · Horizontal
 * position of duplicate marker in line (1 byte) Answer: 70H. Length: 5 bytes. ·
 * Result Code (1 byte) · Operator index number (1 byte) 1…30 · Current receipt
 * number (2 bytes) – for duplicates as columns the spacing is set in
 * characters, for duplicates as line blocks the spacing is set in strings
 ****************************************************************************/

public final class SlipOpen extends PrinterCommand {
    // in params
    private final int password;
    private final SlipOpenParams openParams;
    private final SlipParams slipParams;
    // out params
    private int operator;
    private int recNumber;

    /** Creates a new instance of SlipOpen */
    public SlipOpen(int password, SlipOpenParams openParams,
            SlipParams slipParams) {
        this.password = password;
        this.openParams = openParams;
        this.slipParams = slipParams;
    }

    public final int getCode() {
        return 0x70;
    }

    public final String getText() {
        return "Open fiscal slip";
    }

    public final void encode(CommandOutputStream out) throws Exception {
        out.writeInt(password);
        // openParams
        out.writeByte(openParams.slipType);
        out.writeByte(openParams.slipDupType);
        out.writeByte(openParams.numCopies);
        out.writeByte(openParams.spacing1);
        out.writeByte(openParams.spacing2);
        out.writeByte(openParams.spacing3);
        out.writeByte(openParams.spacing4);
        out.writeByte(openParams.spacing5);
        // slipParams
        out.writeByte(slipParams.fixedHeaderFont);
        out.writeByte(slipParams.headerFont);
        out.writeByte(slipParams.EJFont);
        out.writeByte(slipParams.EJCRCFont);
        out.writeByte(slipParams.fixedHeaderY);
        out.writeByte(slipParams.headerY);
        out.writeByte(slipParams.EJY);
        out.writeByte(slipParams.copySignY);
        out.writeByte(slipParams.fixedHeaderX);
        out.writeByte(slipParams.headerX);
        out.writeByte(slipParams.EJX);
        out.writeByte(slipParams.EJCRC);
        out.writeByte(slipParams.copySignX);
    }

    public final void decode(CommandInputStream in) throws Exception {
        operator = in.readByte();
        recNumber = in.readShort();
    }

    public int getOperator() {
        return operator;
    }

    public int getRecNumber() {
        return recNumber;
    }
}
