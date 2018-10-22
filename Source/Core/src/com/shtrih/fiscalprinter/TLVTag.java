/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.fiscalprinter;

import java.util.Vector;
import java.io.ByteArrayOutputStream;
import com.shtrih.fiscalprinter.command.CommandOutputStream;
import com.shtrih.fiscalprinter.command.PrinterDate;
import com.shtrih.fiscalprinter.command.PrinterTime;
import com.shtrih.jpos.fiscalprinter.JposFiscalPrinterDate;
import com.shtrih.util.HexUtils;
import com.shtrih.util.StringUtils;
import com.shtrih.util.encoding.IBM866;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 *
 * @author V.Kravtsov
 */
public class TLVTag {

    private String value = "";
    private final int id;
    private final int size;
    private final boolean fixedSize;
    private final TLVType type;
    private final String displayName;
    private final String printName;
    private final Vector<TLVBit> bits = new Vector<TLVBit>();

    public enum TLVType {
        itByte, itUInt16, itUInt32, itVLN, itFVLN, itBitMask,
        itUnixTime, itASCII, itSTLV, itByteArray
    }

    public byte[] getData() throws Exception {
        CommandOutputStream stream = new CommandOutputStream("");
        byte[] bytes = getBinValue();
        stream.writeShort(id);
        stream.writeShort(bytes.length);
        stream.writeBytes(bytes);
        return stream.getData();
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public byte[] strToTLV(String text) throws Exception {
        String strValue = text;
        if (strValue.length() > size) {
            strValue = strValue.substring(0, size - 1);
        }
        if (fixedSize) {
            strValue += StringUtils.stringOfChar(' ', size - strValue.length());
        }
        return strValue.getBytes(new IBM866());
    }

    public byte[] intToTLV(long v, int size) {
        byte[] result = new byte[size];
        for (int i = 0; i < size; i++) {
            result[i] = (byte) ((v >>> (8 * i)) & 0xFF);
        }
        return result;
    }

    public byte[] vlnToTLV(long v) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        for (int i = 0; i < 8; i++) {
            stream.write((int) (v >>> (8 * i)) & 0xFF);
            if ((v >>> (8 * (i + 1))) == 0) {
                break;
            }
        }
        return stream.toByteArray();
    }

    public byte[] fvlnToTLV(double v) throws Exception {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        int k = 0;
        double c = v;
        long l = Math.round(v);
        for (int i = 0; i <= 4; i++) {
            if (c == l) {
                break;
            }
            c = v * 10;
            l = Math.round(c);
            k++;
        }
        stream.write(k);
        stream.write(vlnToTLV(l));
        return stream.toByteArray();
    }

    public byte[] getBinValue() throws Exception {
        return valueToBin(value);
    }
    
    public byte[] valueToBin(String value) throws Exception {
        switch (type) {
            case itByte:
                return intToTLV(Integer.parseInt(value), 1);
            case itUInt16:
                return intToTLV(Integer.parseInt(value), 2);
            case itUInt32:
                return intToTLV(Integer.parseInt(value), 4);
            case itUnixTime:
                SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
                sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
                String text = value.substring(0, 8);
                long unixTime = sdf.parse(text).getTime()/1000;
                return intToTLV(unixTime, 4);
            case itByteArray:
                return HexUtils.hexToBytes(value);
            case itASCII:
                return strToTLV(value);
            case itBitMask:
                return intToTLV(Integer.parseInt(value), size);
            case itVLN:
                return vlnToTLV(Integer.parseInt(value));
            case itSTLV:
                return strToTLV(value);
            case itFVLN:
                return fvlnToTLV(Double.parseDouble(value));
            default:
                throw new Exception("Invalid type value");
        }
    }

    public TLVTag(int id, String displayName, String printName,
            TLVType type, int size) {
        this.id = id;
        this.displayName = displayName;
        this.printName = printName;
        this.type = type;
        this.size = size;
        this.fixedSize = false;
    }

    public TLVTag(int id, String displayName, String printName,
            TLVType type, int size, boolean fixedSize) {
        this.id = id;
        this.displayName = displayName;
        this.printName = printName;
        this.type = type;
        this.size = size;
        this.fixedSize = fixedSize;
    }

    public TLVTag(int id, TLVType type) {
        this.id = id;
        this.displayName = "";
        this.printName = "";
        this.type = type;
        this.size = 0;
        this.fixedSize = false;
    }

    public int getId() {
        return id;
    }

    public boolean isFixedSize() {
        return fixedSize;
    }

    public TLVType getType() {
        return type;
    }

    public int getSize() {
        return size;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getPrintName() {
        return printName;
    }

    public Vector<TLVBit> getBits() {
        return bits;
    }

    public TLVBit addBit(int bit, String name) {
        TLVBit item = new TLVBit(bit, name, "");
        bits.add(item);
        return item;
    }

    public TLVBit addBit(int bit, String name, String printName) {
        TLVBit item = new TLVBit(bit, name, printName);
        bits.add(item);
        return item;
    }
}
