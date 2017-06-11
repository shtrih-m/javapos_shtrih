/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.fiscalprinter;

import com.shtrih.util.BitUtils;
import com.shtrih.util.Hex;
import com.shtrih.util.encoding.IBM866;

import java.util.Date;
import java.util.Locale;
import java.util.Vector;

/**
 * @author V.Kravtsov
 */
public class TLVItem {

    private final int level;
    private final byte[] data;
    private final TLVTag tag;

    public TLVItem(TLVTag tag, byte[] data, int level) {
        this.tag = tag;
        this.data = data;
        this.level = level;
    }

    public TLVTag getTag() {
        return tag;
    }

    public byte[] getData() {
        return data;
    }

    public int getLevel() {
        return level;
    }

    public long toInt(byte[] d) {
        long result = 0;
        for (int i = d.length - 1; i >= 0; i--) {
            result <<= 8;
            result |= d[i] & 0xFF;
        }
        return result;
    }

    public long toInt(byte[] d, int len) {
        long result = 0;
        for (int i = len - 1; i >= 0; i--) {
            result <<= 8;
            result |= d[i] & 0xFF;
        }
        return result;
    }

    public Date toDate(byte[] d) {
        return new Date(toInt(d) / 86400);
    }

    public String calcTypeToStr(int type) {
        switch (type) {
            case 1:
                return "Приход";
            case 2:
                return "Возврат прихода";
            case 3:
                return "Расход";
            case 4:
                return "Возврат расхода";
            default:
                return "Неизв. тип: " + String.valueOf(type);
        }
    }

    //
    /*
            0 Общая
            1 Упрощенная Доход
            2 Упрощенная Доход минус Расход
            3 Единый налог на вмененный доход
            4 Единый сельскохозяйственный налог
            5 Патентная система налогообложения
     */
    public String taxSystemToStr(int type) {
        if (type == 0) {
            return "Нет";
        }

        String result = "";
        if (BitUtils.testBit(type, 0)) {
            result += "Общ.";
        }
        if (BitUtils.testBit(type, 1)) {
            result += "+УД";
        }
        if (BitUtils.testBit(type, 2)) {
            result += "+УДМР";
        }
        if (BitUtils.testBit(type, 3)) {
            result += "+ЕНВД";
        }
        if (BitUtils.testBit(type, 4)) {
            result += "+ЕСН";
        }
        if (BitUtils.testBit(type, 5)) {
            result += "+ПСН";
        }
        return result;
    }

    public String toASCII(byte[] data) throws Exception {
        return new String(data, new IBM866());
    }

    public double toFVLN(byte[] data) throws Exception {
        if (data[0] > 4) {
            throw new Exception("Неверная длина FVLN");
        }
        if (data.length < 2) {
            throw new Exception("Неверная длина FVLN");
        }
        byte[] d = new byte[data.length - 1];
        System.arraycopy(data, 1, d, 0, d.length);
        double result = toInt(d);
        int power = data[0];
        for (int i = 0; i < power; i++) {
            result = result / 10;
        }
        return result;
    }

    public String toFVLNS(byte[] data) throws Exception {
        double result = toFVLN(data);
        int power = data[0];
        return String.format(Locale.US, "%." + power + "f", result);
    }

    public String toBitMask(byte[] data) {
        int size = tag.getSize();
        int value = (int) toInt(data, size);
        String text = "";
        Vector<TLVBit> bits = tag.getBits();
        for (int i = 0; i < bits.size(); i++) {
            TLVBit bit = bits.get(i);
            if (BitUtils.testBit(value, bit.getBit())) {
                if (!text.isEmpty()) {
                    text += "/r/n";
                }
                text += bit.getName();
            }
        }
        return text;
    }

    public String getText() throws Exception {
        switch (tag.getType()) {
            case itByte:
                return String.valueOf(toInt(data));
            case itUInt16:
                return String.valueOf(toInt(data));
            case itUInt32:
                return String.valueOf(toInt(data));
            case itVLN:
                return String.format(Locale.US, "%.2f", toInt(data) / 100.0);
            case itFVLN:
                return toFVLNS(data);
            case itUnixTime:
                return toDate(data).toString();
            case itByteArray:
                return Hex.toHex(data);
            case itASCII:
                return toASCII(data).trim();
            case itBitMask:
                return toBitMask(data);
                
            default:
                return "";
        }
    }
}
