/*
 * Beep.java
 *
 * Created on 2 April 2008, 19:22
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
 * Beep Command: 13H. Length: 5 bytes. · Operator password (4 bytes) Answer:
 * 13H. Length: 3 bytes. · Result Code (1 byte) · Operator index number (1 byte)
 * 1…30
 ****************************************************************************/
public class Beep extends PrinterCommand {
    // in
    private int password = 0; // Operator password (4 bytes)
    // out
    private int operator = 0; // Operator index number (1 byte) 1…30

    /**
     * Creates a new instance of Beep
     */
    public Beep() {
        super();
    }

    public final int getCode() {
        return 0x13;
    }

    public final String getText() {
        return "Beep";
    }

    public void encode(CommandOutputStream out) throws Exception {
        out.writeInt(password);
    }

    public void decode(CommandInputStream in) throws Exception {
        setOperator(in.readByte());
    }

    public int getOperator() {
        return operator;
    }

    public int getPassword() {
        return password;
    }

    public void setPassword(int password) {
        this.password = password;
    }

    public void setOperator(int operator) {
        this.operator = operator;
    }
}
