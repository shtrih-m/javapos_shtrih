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
import com.shtrih.util.BitUtils;
import com.shtrih.util.Hex;
import com.shtrih.util.HexUtils;
import com.shtrih.util.StringUtils;
import com.shtrih.util.encoding.IBM866;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 *
 * @author V.Kravtsov
 */
public class TLVTag {

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

    public byte[] textToBin(String value) throws Exception {
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
                long unixTime = sdf.parse(text).getTime() / 1000;
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

    public String binToText(byte[] data) throws Exception {
        switch (getType()) {
            case itByte:
                return String.valueOf(toInt(data));
            case itUInt16:
                return String.valueOf(toInt(data));
            case itUInt32:
                return String.valueOf(toInt(data));
            case itVLN:
                return format(toVLN(data));
            case itFVLN:
                return format(toFVLN(data));
            case itUnixTime:
                return format(toDate(data));
            case itByteArray:
                return Hex.toHex(data).trim();
            case itASCII:
                return toASCII(data).trim();
            case itBitMask:
                return toBitMask(data);
            case itSTLV:
                return "";

            default:
                return Hex.toHex(data).trim();
        }
    }

    private long toInt(byte[] data) {
        return toInt(data, 0);
    }

    private long toInt(byte[] d, int offset) {
        long result = 0;
        for (int i = d.length - 1; i >= offset; i--) {
            result <<= 8;
            result |= d[i] & 0xFF;
        }
        return result;
    }

    public Date toDate(byte[] data) {
        long unixTime = toInt(data);
        Calendar mydate = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        mydate.setTimeInMillis(unixTime * 1000);
        return new Date(
                mydate.get(Calendar.YEAR) - 1900,
                mydate.get(Calendar.MONTH),
                mydate.get(Calendar.DAY_OF_MONTH),
                mydate.get(Calendar.HOUR_OF_DAY),
                mydate.get(Calendar.MINUTE),
                mydate.get(Calendar.SECOND));
    }

    public String toASCII(byte[] data) {
        return new String(data, new IBM866());
    }

    public BigDecimal toFVLN(byte[] data) throws Exception {
        if (data.length < 2) {
            throw new Exception("Неверная длина FVLN, ожидается минимум 2, получено " + data.length);
        }
        long value = toInt(data, 1);
        int scale = data[0];
        return BigDecimal.valueOf(value, scale);
    }

    public BigDecimal toVLN(byte[] data) {
        long value = toInt(data);
        int scale = 2;
        return BigDecimal.valueOf(value, scale);
    }

    public String toBitMask(byte[] data) {
        int value = (int) toInt(data);
        String text = "";
        Vector<TLVBit> bits = getBits();
        for (int i = 0; i < bits.size(); i++) {
            TLVBit bit = bits.get(i);
            if (BitUtils.testBit(value, bit.getBit())) {
                if (!text.isEmpty()) {
                    text += " ";
                }
                text += bit.getPrintName();
            }
        }
        return text;
    }

    private String format(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        return sdf.format(date);
    }

    private String format(BigDecimal value) {
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(value.scale());
        df.setMinimumFractionDigits(value.scale());
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.getDefault());
        otherSymbols.setDecimalSeparator('.');
        otherSymbols.setGroupingSeparator(' ');
        df.setDecimalFormatSymbols(otherSymbols);
        df.setGroupingUsed(false);
        return df.format(value);
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

    public String getPrintName(String text) {
        // При выводе e-mail в приказе ФНС прописано, что строка должна начинаться с "ЭЛ. АДР. ПОКУПАТЕЛЯ".
        // При выводе телефона - строка начинается с "ТЕЛ. ПОКУПАТЕЛЯ".
        if (getId() == 1008) {
            return text.contains("@") ? "ЭЛ. АДР. ПОКУПАТЕЛЯ" : "ТЕЛ. ПОКУПАТЕЛЯ";
        }
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

    public boolean isSTLV() {
        return getType() == TLVTag.TLVType.itSTLV;
    }

}
