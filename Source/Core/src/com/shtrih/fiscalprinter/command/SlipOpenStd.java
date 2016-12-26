/*
 * SlipOpenStd.java
 *
 * Created on January 15 2009, 15:03
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
 * Open Standard Fiscal Slip Command: 71H. Length: 13 bytes. · Operator password
 * (4 bytes) · Slip type (1 byte) «0» – Sale, «1» – Buy, «2» – Sale Refund, «3»
 * – Buy Refund · Slip duplicates type (1 byte) «0» – duplicates as columns, «1»
 * – duplicates as line blocks · Number of duplicates (1 byte) 0…5 · Spacing
 * between Original and Duplicate 1 (1 byte) * · Spacing between Duplicate 1 and
 * Duplicate 2 (1 byte) * · Spacing between Duplicate 2 and Duplicate 3 (1 byte)
 * * · Spacing between Duplicate 3 and Duplicate 4 (1 byte) * · Spacing between
 * Duplicate 4 and Duplicate 5 (1 byte) * Answer: 71H. Length: 5 bytes. · Result
 * Code (1 byte) · Operator index number (1 byte) 1…30 · Current receipt number
 * (2 bytes) – for duplicates as columns the spacing is set in characters, for
 * duplicates as line blocks the spacing is set in strings
 ****************************************************************************/

public final class SlipOpenStd extends PrinterCommand {

    // in params
    private final int password;
    private final SlipOpenParams openParams;
    // out params
    private int operator;
    private int recNumber;

    /**
     * Creates a new instance of SlipOpenStd
     */
    public SlipOpenStd(int password, SlipOpenParams openParams) {
        this.password = password;
        this.openParams = openParams;
    }

    
    public final int getCode() {
        return 0x71;
    }

    
    public final String getText() {
        return "Open standard fiscal slip";
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
