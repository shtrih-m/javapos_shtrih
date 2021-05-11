package com.shtrih.fiscalprinter;

import com.shtrih.util.BitUtils;
import com.shtrih.util.Hex;
import com.shtrih.util.HexUtils;
import com.shtrih.util.encoding.IBM866;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author V.Kravtsov
 */
public class TLVItem {

    private final int id;
    private final byte[] data;
    private final TLVTag tag;
    private final List<TLVItem> items = new ArrayList<TLVItem>();

    public TLVItem(int tagId, byte[] data, TLVTag tag) {
        this.id = tagId;
        this.data = data;
        this.tag = tag;
    }

    public int getId() {
        return id;
    }

    public byte[] getData() {
        return data;
    }

    public TLVTag getTag() {
        return tag;
    }

    public List<TLVItem> getItems() {
        return items;
    }

    public void addItem(TLVItem item) {
        items.add(item);
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

    public String toASCII() {
        return new String(data, new IBM866());
    }

    public BigDecimal toFVLN() throws Exception {
        if (data.length < 2) {
            throw new Exception("Неверная длина FVLN, ожидается минимум 2, получено " + data.length);
        }
        long value = toInt(data, 1);
        int scale = data[0];
        return BigDecimal.valueOf(value, scale);
    }

    public BigDecimal toVLN() {
        long value = toInt();
        int scale = 2;
        return BigDecimal.valueOf(value, scale);
    }

    public String getText() throws Exception 
    {
        if (tag == null){
            return Hex.toHex(data).trim();
        }
        
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
            case itSTLV:
                return "";

            default:
                return Hex.toHex(data).trim();
        }
    }

    public String toBitMask() {
        int value = (int) toInt(data);
        String text = "";
        Vector<TLVBit> bits = tag.getBits();
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
}
