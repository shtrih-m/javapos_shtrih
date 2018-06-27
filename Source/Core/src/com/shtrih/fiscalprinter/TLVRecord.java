package com.shtrih.fiscalprinter;

public class TLVRecord {

    private final int tagId;
    private final byte[] data;

    public int getTagId() {
        return tagId;
    }

    public byte[] getData() {
        return data;
    }

    public TLVRecord(int tagId, byte[] data) {
        this.tagId = tagId;
        this.data = data;
    }

    public long toInt() {
        int len = data.length;

        checkLength(len, 1, 8);

        long result = 0;
        for (int i = data.length - 1; i >= 0; i--) {

            result <<= 8;
            result |= (data[i] & 0xFF);
        }
        return result;
    }

    public void checkLength(int value, int min, int max) {
        if ((value < min) || (value > max)) {
            throw new IllegalArgumentException("Incorrect data length, expected between " + min + " and " + max + ", but was " + value);
        }
    }
}
