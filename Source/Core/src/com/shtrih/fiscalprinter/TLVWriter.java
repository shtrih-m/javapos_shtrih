package com.shtrih.fiscalprinter;

import com.shtrih.fiscalprinter.command.CommandInputStream;
import com.shtrih.util.encoding.IBM866;

import java.util.List;
import java.util.Arrays;
import java.io.ByteArrayOutputStream;

/**
 * @author V.Kravtsov
 */
public class TLVWriter {

    private final ByteArrayOutputStream stream;

    public TLVWriter() {
        stream = new ByteArrayOutputStream();
    }

    public TLVWriter clear() {
        stream.reset();

        return this;
    }

    public byte[] getBytes() {
        return stream.toByteArray();
    }

    public TLVWriter add(long v, int len) {
        byte buffer[] = new byte[len];
        for (int i = 0; i < len; i++) {
            buffer[i] = (byte) ((v >>> (8 * i)) & 0xFF);
        }

        stream.write(buffer, 0, len);

        return this;
    }

    public TLVWriter addBE(long v, int len) {
        byte buffer[] = new byte[len];
        for (int i = 0; i < len; i++) {
            buffer[len - i - 1] = (byte) ((v >>> (8 * i)) & 0xFF);
        }

        stream.write(buffer, 0, len);

        return this;
    }

    public TLVWriter add(byte[] data) {
        stream.write(data, 0, data.length);

        return this;
    }

    public TLVWriter add(byte[] data, int length) {
        byte[] b = new byte[length];
        Arrays.fill(b, (byte) 0);
        System.arraycopy(data, 0, b, 0, Math.min(data.length, length));
        stream.write(b, 0, b.length);

        return this;
    }

    public TLVWriter add(String line) {
        add(line.getBytes(new IBM866()));

        return this;
    }

    public TLVWriter add(String line, int minLen) {
        byte[] data = line.getBytes(new IBM866());
        int len = Math.max(minLen, line.length());
        byte[] result = new byte[len];

        Arrays.fill(result, (byte) 0);
        for (int i = 0; i < data.length; i++) {
            result[i] = data[i];
        }
        add(result);

        return this;
    }

    public TLVWriter add(int tagId, String tagValue) {
        add(tagId, 2);
        add(tagValue.length(), 2);
        add(tagValue);

        return this;
    }

    public TLVWriter add(int tagId, byte[] data) {
        add(tagId, 2);
        add(data.length, 2);
        add(data);

        return this;
    }

    public TLVWriter add(int tagId, long value, int len) {
        add(tagId, 2);
        add(len, 2);
        add(value, len);

        return this;
    }

    public void addTag(int tagId, String value) throws Exception
    {
        TLVTags tags = TLVTags.getInstance();
        TLVTag tag = tags.find(tagId);
        add(tagId, tag.textToBin(value));
    }
    
    public byte[] getData(List<TLVItem> items) throws Exception {
        ByteArrayOutputStream stm = new ByteArrayOutputStream();
        for (int i = 0; i < items.size(); i++) {
            stm.write(getData(items.get(i)));
        }
        return stm.toByteArray();
    }

    public byte[] getData(TLVItems items) throws Exception {
        ByteArrayOutputStream stm = new ByteArrayOutputStream();
        for (int i = 0; i < items.size(); i++) {
            stm.write(getData(items.get(i)));
        }
        return stm.toByteArray();
    }
    
    public byte[] getData(TLVItem item) throws Exception {
        ByteArrayOutputStream stm = new ByteArrayOutputStream();
        byte[] data;
        if (item.isSTLV()) {
            data = getData(item.getItems());
        } else {
            data = item.getData();
        }
        stm.write(item.getId() & 0xFF);
        stm.write((item.getId() >>> 8) & 0xFF);
        stm.write(data.length & 0xFF);
        stm.write((data.length >>> 8) & 0xFF);
        stm.write(data);
        return stm.toByteArray();
    }

    public void add(TLVItem item) throws Exception {
        stream.write(getData(item));
    }

    public void add(List<TLVItem> items) throws Exception {
        for (int i = 0; i < items.size(); i++) {
            add(items.get(i));
        }
    }
    
    public void add(TLVItems items) throws Exception {
        for (int i = 0; i < items.size(); i++) {
            add(items.get(i));
        }
    }
}
