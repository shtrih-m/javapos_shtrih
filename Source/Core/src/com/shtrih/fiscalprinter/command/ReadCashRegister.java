/*
 * ReadCashRegister.java
 *
 * Created on 2 April 2008, 20:37
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.shtrih.fiscalprinter.command;

/**
 *
 * @author V.Kravtsov
 */
import com.shtrih.util.MethodParameter;

/**
 * **************************************************************************
 * Get Cash Totalizer Value Command: 1AH. Length: 6 bytes. · Operator password
 * (4 bytes) · Cash totalizer number (1 byte) 0…255 Answer: 1AH. Length: 9
 * bytes. · Result Code (1 byte) · Operator index number (1 byte) 1…30 · Cash
 * totalizer value (6 bytes)
 ***************************************************************************
 */
public final class ReadCashRegister extends PrinterCommand {

    // in

    private int password;
    private int number;
    // out
    private int operator = 0;
    private long value = 0;

    /**
     * Creates a new instance of ReadCashRegister
     */
    public ReadCashRegister(int password, int number) throws Exception {
        MethodParameter.checkRange(number, 0, 0xFFFF, "totalizer number");

        this.password = password;
        this.number = number;
    }

    public final int getCode() {
        return 0x1A;
    }

    public final String getText() {
        return "Get cash totalizer value";
    }

    public final void encode(CommandOutputStream out) throws Exception {
        out.writeInt(password);
        if (number <= 0xFF) {
            out.writeByte(number);
        } else {
            out.writeShort(number);
        }

    }

    public final void decode(CommandInputStream in) throws Exception {
        operator = in.readByte();
        value = in.readLong(6);
    }

    public int getOperator() {
        return operator;
    }

    public long getValue() {
        return value;
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
     * @return the number
     */
    public int getNumber() {
        return number;
    }

    /**
     * @param number the number to set
     */
    public void setNumber(int number) {
        this.number = number;
    }

    /**
     * @param operator the operator to set
     */
    public void setOperator(int operator) {
        this.operator = operator;
    }

    /**
     * @param value the value to set
     */
    public void setValue(long value) {
        this.value = value;
    }
}
