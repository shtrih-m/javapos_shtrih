/*
 * CutPaper.java
 *
 * Created on 2 April 2008, 21:18
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
 * Cut receipt Command: 25H. Length: 6 bytes. · Operator password (4 bytes) ·
 * Cut type (1 byte) «0» – complete, «1» – incomplete Answer: 25H. Length: 3
 * bytes. · Result Code (1 byte) · Operator index number (1 byte) 1…30
 ****************************************************************************/
public final class CutPaper extends PrinterCommand {
    // in

    private int password; // Operator password (4 bytes)
    private int cutType; // Cut type (1 byte)
    // out
    private int operator = 0; // Operator index number (1 byte) 1…30

    /**
     * Creates a new instance of CutPaper
     */
    public CutPaper() {
        super();
    }

    public final int getCode() {
        return 0x25;
    }

    public final String getText() {
        return "Cut receipt";
    }

    public final void encode(CommandOutputStream out) throws Exception {
        out.writeInt(getPassword());
        out.writeByte(getCutType());
    }

    public final void decode(CommandInputStream in) throws Exception {
        setOperator(in.readByte());
    }

    public int getOperator() {
        return operator;
    }

    public int getPassword() {
        return password;
    }

    public int getCutType() {
        return cutType;
    }

    public void setPassword(int password) {
        this.password = password;
    }

    public void setCutType(int cutType) {
        this.cutType = cutType;
    }

    public void setOperator(int operator) {
        this.operator = operator;
    }
}
