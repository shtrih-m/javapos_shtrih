/*
 * ReadFMLastRecordDate.java
 *
 * Created on 2 April 2008, 20:55
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
 * Get date of last record in FM Command: 63H. Length: 5 bytes. · Administrator
 * or System Administrator password (4 bytes) 29, 30 Answer: 63H. Length: 7
 * bytes. · Result Code (1 byte) · Operator index number (1 byte) 29, 30 · Type
 * of last record in FM (1 byte) «0» – fiscalization/refiscalization, «1» –
 * daily totals · Date (3 bytes) DD-MM-YY
 ****************************************************************************/
public final class ReadFMLastRecordDate extends PrinterCommand {
    // in
    private int password;
    // out
    private int operator;
    private int recordType;
    private PrinterDate recordDate;

    /**
     * Creates a new instance of ReadFMLastRecordDate
     */
    public ReadFMLastRecordDate() {
    }

    public final int getCode() {
        return 0x63;
    }

    public final String getText() {
        return "Get date of last record in FM";
    }

    public final void encode(CommandOutputStream out) throws Exception {
        out.writeInt(password);
    }

    public final void decode(CommandInputStream in) throws Exception {
        operator = in.readByte();
        recordType = in.readByte();
        recordDate = in.readDate();
    }

    public void setPassword(int password) {
        this.password = password;
    }

    public int getPassword() {
        return password;
    }

    public int getOperator() {
        return operator;
    }

    public int getRecordType() {
        return recordType;
    }

    public PrinterDate getRecordDate() {
        return recordDate;
    }
}
