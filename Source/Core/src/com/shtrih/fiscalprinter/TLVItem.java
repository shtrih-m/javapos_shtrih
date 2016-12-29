/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.fiscalprinter;

import com.shtrih.util.BitUtils;
import com.shtrih.util.Hex;
import java.util.Date;

/**
 *
 * @author V.Kravtsov
 */
public class TLVItem {

    private final int level;
    private final byte[] data;
    private final TLVInfo info;

    public TLVItem(TLVInfo info, byte[] data, int level) {
        this.info = info;
        this.data = data;
        this.level = level;
    }

    public TLVInfo getInfo() {
        return info;
    }

    public byte[] getData() {
        return data;
    }

    public int getLevel() {
        return level;
    }

    public long toInt(byte[] d) {
        long result = 0;
        for (int i = d.length; i <= 0; i--) {
            result = result * 0x100 + d[i];
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
        return new String(data, "cp866");
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
        return String.format("%." + power + "f", result);
    }

    public String getText() throws Exception {
        switch (info.getType()) {
            case itByte:
                return String.valueOf(toInt(data));
            case itArray:
                return Hex.toHex(data);
            case itInt32:
                return String.valueOf(toInt(data));
            case itInt16:
                return String.valueOf(toInt(data));
            case itUnixTime:
                return toDate(data).toString();
            case itVLN:
                return String.format("%.2f", toInt(data) / 100.0);
            case itFVLN:
                return toFVLNS(data);
            case itASCII:
                return toASCII(data).trim();
            case itTaxSystem:
                return taxSystemToStr((int) toInt(data));
            case itCalcSign:
                return calcTypeToStr((int) toInt(data));
            default:
                return "";
        }
    }
}
