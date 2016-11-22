/*
 * WriteTable.java
 *
 * Created on April 2 2008, 20:07
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
 * Set Table Field Value Command: 1EH. Length: (9+X) bytes. · System
 * Administrator password (4 bytes) 30 · Table (1 byte) · Row (2 bytes) · Field
 * (1 byte) · Value (X bytes) up to 40 bytes Answer: 1EH. Length: 2 bytes. ·
 * Result Code (1 byte)
 ****************************************************************************/

public final class WriteTable extends PrinterCommand {
    // in params
    private final int password;
    private final int tableNumber;
    private final int rowNumber;
    private final int fieldNumber;
    private final byte[] fieldValue;

    /**
     * Creates a new instance of WriteTable
     */
    public WriteTable(int password, int tableNumber, int rowNumber,
            int fieldNumber, byte[] fieldValue) throws Exception {
        MethodParameter.checkRange(tableNumber, 0, 0xFF, "table number");
        MethodParameter.checkRange(fieldNumber, 0, 0xFF, "field number");
        MethodParameter.checkRange(rowNumber, 0, 0xFFFF, "row number");

        this.password = password;
        this.tableNumber = tableNumber;
        this.rowNumber = rowNumber;
        this.fieldNumber = fieldNumber;
        this.fieldValue = fieldValue;
    }

    
    public final int getCode() {
        return 0x1E;
    }

    
    public final String getText() {
        return "Write table";
    }

    
    public void encode(CommandOutputStream out) throws Exception {
        out.writeInt(password);
        out.writeByte(tableNumber);
        out.writeShort(rowNumber);
        out.writeByte(fieldNumber);
        out.writeBytes(fieldValue);
    }

    
    public void decode(CommandInputStream in) throws Exception {
    }
}
