/*
 * ReadTable.java
 *
 * Created on April 2 2008, 20:10
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
 * Get Table Field Value Command: 1FH. Length: 9 bytes. · System Administrator
 * password (4 bytes) 30 · Table (1 byte) · Row (2 bytes) · Field (1 byte)
 * Answer: 1FH. Length: (2+X) bytes. · Result Code (1 byte)
 ****************************************************************************/

public class ReadTable extends PrinterCommand {
    // in params
    private final int password;
    private final int tableNumber;
    private final int rowNumber;
    private final int fieldNumber;
    // out params
    public byte[] fieldValue;

    /**
     * Creates a new instance of ReadTable
     */
    public ReadTable(int password, int tableNumber, int rowNumber,
            int fieldNumber) throws Exception {
        MethodParameter.checkRange(tableNumber, 0, 0xFF, "table number");
        MethodParameter.checkRange(fieldNumber, 0, 0xFF, "field number");
        MethodParameter.checkRange(rowNumber, 0, 0xFFFF, "row number");

        this.password = password;
        this.tableNumber = tableNumber;
        this.rowNumber = rowNumber;
        this.fieldNumber = fieldNumber;
    }

    
    public final int getCode() {
        return 0x1F;
    }

    
    public final String getText() {
        return "Get table field value";
    }

    
    public final void encode(CommandOutputStream out) throws Exception {
        out.writeInt(password);
        out.writeByte(tableNumber);
        out.writeShort(rowNumber);
        out.writeByte(fieldNumber);
    }

    
    public final void decode(CommandInputStream in) throws Exception {
        fieldValue = in.readBytes(in.getSize());
    }

    
    public boolean getIsRepeatable() {
        return true;
    }
}
