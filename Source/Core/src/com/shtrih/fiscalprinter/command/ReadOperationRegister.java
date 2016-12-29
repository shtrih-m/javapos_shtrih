/*
 * ReadOperationRegister.java
 *
 * Created on 2 April 2008, 20:39
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

/****************************************************************************
 * Get Operation Totalizer Value Command: 1BH. Length: 6 bytes. · Operator
 * password (4 bytes) · Operation totalizer number (1 byte) 0…255 Answer: 1BH.
 * Length: 5 bytes. · Result Code (1 byte) · Operator index number (1 byte) 1…30
 * · Operation totalizer value (2 bytes)
 ****************************************************************************/
public final class ReadOperationRegister extends PrinterCommand {
    // in

    private final int password;
    private final int number;
    // out
    private int operator;
    private int value;

    /**
     * Creates a new instance of ReadOperationRegister
     */
    public ReadOperationRegister(int password, int number) throws Exception {
        MethodParameter.checkRange(number, 0, 0xFF, "totalizer number");

        this.password = password;
        this.number = number;
    }

    
    public final int getCode() {
        return 0x1B;
    }

    
    public final String getText() {
        return "Get operation totalizer value";
    }

    
    public final void encode(CommandOutputStream out) throws Exception {
        out.writeInt(password);
        out.writeByte(number);
    }

    
    public final void decode(CommandInputStream in) throws Exception {
        operator = in.readByte();
        value = in.readShort();
    }

    public int getValue() {
        return value;
    }

    
    public boolean getIsRepeatable() {
        return true;
    }
}
