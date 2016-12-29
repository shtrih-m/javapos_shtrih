/*
 * SlipConfigureStd.java
 *
 * Created on January 16 2009, 11:34
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
 * Set Standard Configuration Of Slip Command: 79H. Length: 5 bytes. · Operator
 * password (4 bytes) Answer: 79H. Length: 3 bytes. · Result Code (1 byte) ·
 * Operator index number (1 byte) 1…30
 ****************************************************************************/

public class SlipConfigureStd {

    // in params
    private final int password;
    // out params
    private int operator;

    /** Creates a new instance of SlipConfigureStd */
    public SlipConfigureStd(int password) {
        this.password = password;
    }

    public final int getCode() {
        return 0x79;
    }

    public final String getText() {
        return "Set standard slip configuration";
    }

    public final void encode(CommandOutputStream out) throws Exception {
        out.writeInt(password);
    }

    public final void decode(CommandInputStream in) throws Exception {
        operator = in.readByte();
    }

    public int getOperator() {
        return operator;
    }
}
