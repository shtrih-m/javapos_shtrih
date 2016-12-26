/*
 * EndTest.java
 *
 * Created on April 2 2008, 20:15
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
 * Interrupt Test Command: 2BH. Length: 5 bytes. · Operator password (4 bytes)
 * Answer: 2BH. Length: 3 bytes. · Result Code (1 byte) · Operator index number
 * (1 byte) 1…30
 ****************************************************************************/
public final class EndTest extends PrinterCommand {
    // in
    private int password;
    // out
    private int operator;

    /**
     * Creates a new instance of EndTest
     */
    public EndTest() {
        super();
    }

    public final int getCode() {
        return 0x2B;
    }

    public final String getText() {
        return "Stop test";
    }

    public final void encode(CommandOutputStream out) throws Exception {
        out.writeInt(getPassword());
    }

    public final void decode(CommandInputStream in) throws Exception {
        setOperator(in.readByte());
    }

    public int getPassword() {
        return password;
    }

    public int getOperator() {
        return operator;
    }

    public void setPassword(int password) {
        this.password = password;
    }

    public void setOperator(int operator) {
        this.operator = operator;
    }
}
