/*
 * SlipPrint.java
 *
 * Created on January 16 2009, 11:48
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
 * Print Slip Command: 7DH. Length: 7 bytes. · Operator password (4 bytes) ·
 * Clear non-fiscal data from slip buffer upon printing (1 byte) «0» – yes, «1»
 * – no · Data type to be printed on slip (1 byte) «0» – non-fiscal data only,
 * «1» – fiscal data only, «2» – both fiscal and non-fiscal data Answer: 7DH.
 * Length: 3 bytes. · Result Code (1 byte) · Operator index number (1 byte) 1…30
 ****************************************************************************/

public final class SlipPrint extends PrinterCommand {
    // in params
    private final int password;
    private final byte clearMode;
    private final byte dataMode;
    // out params
    private int operator;

    /** Creates a new instance of SlipPrint */
    public SlipPrint(int password, byte clearMode, byte dataMode) {
        this.password = password;
        this.clearMode = clearMode;
        this.dataMode = dataMode;
    }

    
    public final int getCode() {
        return 0x7D;
    }

    
    public final String getText() {
        return "Print slip";
    }

    
    public final void encode(CommandOutputStream out) throws Exception {
        out.writeInt(password);
        out.writeByte(clearMode);
        out.writeByte(dataMode);
    }

    
    public final void decode(CommandInputStream in) throws Exception {
        operator = in.readByte();
    }

    public int getOperator() {
        return operator;
    }

}
