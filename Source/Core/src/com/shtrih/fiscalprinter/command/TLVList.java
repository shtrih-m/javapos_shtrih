/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.fiscalprinter.command;

import com.shtrih.util.encoding.IBM866;

import java.io.ByteArrayOutputStream;

/**
 * @author V.Kravtsov
 */
public class TLVList {

    private final ByteArrayOutputStream stream = new ByteArrayOutputStream();

    public void add(int tagId, String tagValue) throws Exception {
        addInt(tagId);
        addInt(tagValue.length());
        stream.write(tagValue.getBytes(new IBM866()));
    }

    public void add(int tagId, long tagValue, int len) throws Exception {
        addInt(tagId);
        addInt(len);
        stream.write(intToTLV(tagValue, len));
    }

    byte[] intToTLV(long value, int len) throws Exception {
        if ((len > 8) || (len < 1)) {
            throw new Exception("Invalid length");
        }
        byte[] result = new byte[len];
        for (int i = 0; i < len; i++) {
            result[i] = (byte) ((value >>> (i * 8)) & 0xFF);
        }
        return result;
    }

    private void addInt(int value) throws Exception {
        // TODO: check value > 0xFFFF

        stream.write((value >>> 0) & 0xFF);
        stream.write((value >>> 8) & 0xFF);
    }

    public byte[] getData() {
        return stream.toByteArray();
    }
}
