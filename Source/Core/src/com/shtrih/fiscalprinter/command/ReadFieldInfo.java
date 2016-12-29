/*
 * ReadFieldInfo.java
 *
 * Created on 2 April 2008, 20:04
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.shtrih.fiscalprinter.command;

/**
 *
 * @author V.Kravtsov
 */
import java.io.UnsupportedEncodingException;
import java.security.InvalidParameterException;

import com.shtrih.util.Localizer;
import com.shtrih.util.MethodParameter;

/****************************************************************************
 * Read field structure Command: 2EH. Length: 7 bytes. · System Administrator
 * password (4 bytes) 30 · Table number (1 byte) · Field number (1 byte) Answer:
 * 2EH. Length: (44+X+X) bytes. · Result Code (1 byte) · Field name (40 bytes) ·
 * Field type (1 byte) «0» – BIN, «1» – CHAR · Number of bytes – X (1 byte) ·
 * Field minimum value (X bytes) for BIN-type fields only · Field maximum value
 * (X bytes) for BIN-type fields only
 ****************************************************************************/
public final class ReadFieldInfo extends PrinterCommand {
    // in params

    private int password;
    private int table;
    private int field;
    // out
    private FieldInfo fieldInfo = null;

    /**
     * Creates a new instance of ReadFieldInfo
     */
    public ReadFieldInfo() {
    }

    public void setPassword(int value) {
        this.password = value;
    }

    public void setTable(int value) throws Exception {
        MethodParameter.checkRange(value, 0, 0xFF, "table number");
        this.table = value;
    }

    public void setField(int value) throws Exception {
        MethodParameter.checkRange(value, 0, 0xFF, "field number");
        this.field = value;
    }

    public int getTable() {
        return table;
    }

    public int getField() {
        return field;
    }
   
    public FieldInfo getFieldInfo() {
        return fieldInfo;
    }
    
    public final int getCode() {
        return 0x2E;
    }
    
    public final String getText() {
        return "Read field structure";
    }
    
    public final void encode(CommandOutputStream out) throws Exception {
        out.writeInt(password);
        out.writeByte(table);
        out.writeByte(field);
    }

    
    public final void decode(CommandInputStream in) throws Exception {
        long min = 0;
        long max = 0;
        String name = in.readString(40).trim();
        int type = in.readByte();
        int size = in.readByte();
        if (type == 0) {
            min = in.readLong(size);
            max = in.readLong(size);
        }
        fieldInfo = new FieldInfo(table, field, size, type, min, max, name);
    }

    public boolean getIsRepeatable() {
        return true;
    }
}
