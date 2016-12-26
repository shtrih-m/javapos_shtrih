/*
 * BeginTest.java
 *
 * Created on April 2 2008, 20:12
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

/**
 *
 * @author V.Kravtsov
 */
package com.shtrih.fiscalprinter.command;

import com.shtrih.util.MethodParameter;

/*****************************************************************************
 * Start Test Command: 19H. Length: 6 bytes. · Operator password (4 bytes) ·
 * Test time out (1 byte) 1…99 Answer: 19H. Length: 3 bytes. · Result Code (1
 * byte) · Operator index number (1 byte) 1…30
 *****************************************************************************/
public class BeginTest extends PrinterCommand {
    // in params

    private int password;
    private int periodInMinutes;
    // out
    private int operator;

    /**
     * Creates a new instance of BeginTest
     */
    public BeginTest() {
        super();
    }

    
    public final int getCode() {
        return 0x19;
    }

    
    public final String getText() {
        return "Start test";
    }

    
    public final void encode(CommandOutputStream out) throws Exception {
        out.writeInt(getPassword());
        out.writeByte(getPeriodInMinutes());
    }

    
    public final void decode(CommandInputStream in) throws Exception {
        setOperator(in.readByte());
    }

    public int getPassword() {
        return password;
    }

    public int getPeriodInMinutes() {
        return periodInMinutes;
    }

    public int getOperator() {
        return operator;
    }

    public void setPassword(int password) {
        this.password = password;
    }

    public void setPeriodInMinutes(int periodInMinutes) throws Exception {
        MethodParameter.checkRange(periodInMinutes, 0, 0xFF, "test period");
        this.periodInMinutes = periodInMinutes;
    }

    public void setOperator(int operator) {
        this.operator = operator;
    }
}
