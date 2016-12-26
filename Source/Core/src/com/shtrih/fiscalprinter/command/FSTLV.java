/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.fiscalprinter.command;

import java.io.ByteArrayOutputStream;

/**
 *
 * @author V.Kravtsov
 */
public class FSTLV {

    private final ByteArrayOutputStream stream = new ByteArrayOutputStream();

    public FSTLV() {
    }

    public void addLong(long value, int size) throws Exception {
        if ((size > 8) || (size < 1)) {
            throw new Exception("Size can be 1..8");
        }
        for (int i = 0; i < size; i++) {
            byte b = (byte)((value >> (i * 8)) & 0xFF);
            stream.write(b);
        }

    }

    public void addTag(int tag) throws Exception {
        addLong(tag, 2);
    }

    public void add(int tag, String text) throws Exception {
        addTag(tag);
        byte[] data = text.getBytes();
        stream.write(data, 0, data.length);
    }

    public byte[] toByteArray() {
        return stream.toByteArray();
    }
}
