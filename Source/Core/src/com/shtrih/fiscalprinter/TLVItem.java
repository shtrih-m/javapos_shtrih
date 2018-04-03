package com.shtrih.fiscalprinter;

import com.shtrih.util.BitUtils;
import com.shtrih.util.Hex;
import com.shtrih.util.encoding.IBM866;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

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

    public long toInt() {
        return toInt(data);
    }

    private long toInt(byte[] d) {
        return toInt(d, 0);
    }

    private long toInt(byte[] d, int offset) {
        long result = 0;
        for (int i = d.length - 1; i >= offset; i--) {
            result <<= 8;
            result |= d[i] & 0xFF;
        }
        return result;
    }

    public Date toDate() {
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

    public String toASCII() throws Exception {
        return new String(data, new IBM866());
    }

    public BigDecimal toFVLN() throws Exception {
        if (data.length < 2) {
            throw new Exception("Неверная длина FVLN, ожидается минимум 2, получено " + data.length);
        }
        long value = toInt(data, 1);
        int scale  = data[0];
        return BigDecimal.valueOf(value, scale);
    }

    public BigDecimal toVLN() throws Exception {
        long value = toInt();
        int scale  = 2;
        return BigDecimal.valueOf(value, scale);
    }

    public String toBitMask() {
        int value = (int) toInt(data);
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
                return format(toVLN());
            case itFVLN:
                return format(toFVLN());
            case itUnixTime:
                return format(toDate());
            case itByteArray:
                return Hex.toHex(data).trim();
            case itASCII:
                return toASCII().trim();
            case itBitMask:
                return toBitMask();

            default:
                return "";
        }
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
}
