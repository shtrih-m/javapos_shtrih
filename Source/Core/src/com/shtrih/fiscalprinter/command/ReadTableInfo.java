/*
 * ReadTableInfo.java
 *
 * Created on 2 April 2008, 19:59
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.shtrih.fiscalprinter.command;

import com.shtrih.fiscalprinter.table.PrinterTable;

/**
 *
 * @author V.Kravtsov
 */

/****************************************************************************
 * Get Table Structure Command: 2DH. Length: 6 bytes. · System Administrator
 * password (4 bytes) 30 · Table number (1 byte) Answer: 2DH. Length: 45 bytes.
 * · Result Code (1 byte) · Table name (40 bytes) · Number of rows (2 bytes) ·
 * Number of fields (1 byte)
 ****************************************************************************/

public class ReadTableInfo extends PrinterCommand {
    // in params
    private int password;
    private int tableNumber;
    // out
    private PrinterTable table;

    /**
     * Creates a new instance of ReadTableInfo
     */
    public ReadTableInfo() {
    }

    public final int getCode() {
        return 0x2D;
    }

    public final String getText() {
        return "Get table structure";
    }

    public final void encode(CommandOutputStream out) throws Exception {
        out.writeInt(getPassword());
        out.writeByte(getTableNumber());
    }

    public final void decode(CommandInputStream in) throws Exception {
        String name= in.readString(40).trim();
        int rowCount = in.readShort();
        int fieldCount = in.readByte();
        table = new PrinterTable(tableNumber, name, rowCount, fieldCount);
    }

    public int getTableNumber() {
        return tableNumber;
    }

    public PrinterTable getTable() {
        return table;
    }

    public boolean getIsRepeatable() {
        return true;
    }

    public int getRowCount() {
        return getTable().getRowCount();
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
     * @param tableNumber the tableNumber to set
     */
    public void setTableNumber(int tableNumber) {
        this.tableNumber = tableNumber;
    }

}
