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
    private final TLVTag tag;
    private byte[] data = null;
    private final List<TLVItem> items = new ArrayList<TLVItem>();

    public TLVItem(int tagId, TLVTag tag) {
        this.id = tagId;
        this.tag = tag;
    }

    public TLVItem(int tagId, byte[] data, TLVTag tag) {
        this.id = tagId;
        this.tag = tag;
        this.data = data;
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

    public String getText() throws Exception {
        if (tag != null) {
            return tag.binToText(data);
        } else {
            return "";
        }
    }

    public void setText(String value) throws Exception {
        if (tag != null) {
            data = tag.textToBin(value);
        }
    }

    public List<TLVItem> getItems() {
        return items;
    }

    public void addItem(TLVItem item) {
        items.add(item);
    }

    public long toInt() {
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

    public void setData(byte[] data) {
        this.data = data;
    }

    public boolean isSTLV() {
        return (tag != null) && (tag.isSTLV());
    }
}
