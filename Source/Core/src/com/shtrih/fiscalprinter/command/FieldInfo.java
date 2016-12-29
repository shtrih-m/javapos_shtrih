/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.fiscalprinter.command;

import com.shtrih.util.Localizer;
import java.io.UnsupportedEncodingException;
import java.security.InvalidParameterException;

/**
 *
 * @author V.Kravtsov
 */
public class FieldInfo {

    private final int table;
    private final int field;
    private final int size;
    private final int type;
    private final long min;
    private final long max;
    private final String name;

    public FieldInfo(int table, int field, int size, int type, long min, long max, String name) {
        this.table = table;
        this.field = field;
        this.size = size;
        this.type = type;
        this.min = min;
        this.max = max;
        this.name = name;
    }

    public int getTable() {
        return table;
    }

    public int getField() {
        return field;
    }

    public int getSize() {
        return size;
    }

    public int getType() {
        return type;
    }

    public long getMin() {
        return min;
    }

    public long getMax() {
        return max;
    }

    public String getName() {
        return name;
    }

    public boolean isInteger() {
        return type == PrinterConst.FIELD_TYPE_INT;
    }

    public boolean isString() {
        return type == PrinterConst.FIELD_TYPE_STR;
    }

    public void checkValue(String value) throws Exception {
        if (isInteger()) {
            long fieldValueLong = new Integer(value).longValue();
            if ((fieldValueLong < min) || (fieldValueLong > max)) {
                throw new InvalidParameterException(
                        Localizer.getString(Localizer.InvalidFieldValue));
            }
        }
    }

    public byte[] getBytes(String value) {
        byte[] result = new byte[size];
        long fieldValueLong = new Integer(value).longValue();

        for (int i = 0; i < size; i++) {
            result[i] = (byte) ((fieldValueLong >> i * 8) & 0xFF);
        }
        return result;
    }

    public static byte[] copyOfRange(byte[] original, int from, int to) {
        int newLength = to - from;
        if (newLength < 0) {
            throw new IllegalArgumentException(from + " > " + to);
        }
        byte[] copy = new byte[newLength];
        System.arraycopy(original, from, copy, 0,
                Math.min(original.length - from, newLength));
        return copy;
    }

    public byte[] fieldToBytes(String fieldValue, String charsetName)
            throws UnsupportedEncodingException {
        if (isString()) {
            return copyOfRange(fieldValue.getBytes(charsetName), 0, size);
        } else {
            return getBytes(fieldValue);
        }
    }

    public static int bytesToInt(byte[] b, int fieldSize) {
        int count = Math.min(b.length, fieldSize);
        int result = 0;
        for (int i = 0; i < count; i++) {
            int v = b[i];
            if (v < 0) {
                v = 256 + v;
            }
            result = result + (v << 8 * i);
        }
        return result;
    }
    
    public String bytesToField(byte[] b, String charsetName)
            throws UnsupportedEncodingException {
        String result = "";
        if (isString()) {
            result = new String(CommandInputStream.trimRight(b), charsetName);
        } else {
            result = String.valueOf(bytesToInt(b, size));
        }
        return result;
    }

    public boolean isEqual(int table, int field) {
        return (this.table == table)
                && (this.field == field);
    }
}
