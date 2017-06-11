/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.fiscalprinter;

import com.shtrih.ej.EJDate;
import com.shtrih.ej.EJTime;
import com.shtrih.fiscalprinter.command.PrinterDate;
import com.shtrih.fiscalprinter.command.PrinterTime;
import com.shtrih.util.encoding.IBM866;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;

/**
 *
 * @author V.Kravtsov
 */
public class TLVWriter {

    private final ByteArrayOutputStream stream;

    public TLVWriter() {
        stream = new ByteArrayOutputStream();
    }

    public void clear(){
        stream.reset();
    }
    
    public byte[] getBytes() {
        return stream.toByteArray();
    }

    public void add(long v, int len) throws Exception {
        byte buffer[] = new byte[len];
        for (int i = 0; i < len; i++) {
            buffer[i] = (byte) ((v >>> (8 * i)) & 0xFF);
        }
        stream.write(buffer, 0, len);
    }

    public void add(byte[] data) throws Exception {
        stream.write(data);
    }

    public void add(byte[] data, int length) throws Exception {
        byte[] b = new byte[length];
        Arrays.fill(b, (byte) 0);
        System.arraycopy(data, 0, b, 0, Math.min(data.length, length));
        stream.write(b);
    }

    public void add(String line) throws Exception {
        add(line.getBytes(new IBM866()));
    }

    public void add(String line, int minLen) throws Exception {
        byte[] data = line.getBytes(new IBM866());
        int len = Math.max(minLen, line.length());
        byte[] result = new byte[len];

        Arrays.fill(result, (byte) 0);
        for (int i = 0; i < data.length; i++) {
            result[i] = data[i];
        }
        add(result);
    }

    public void add(int tagId, String tagValue) throws Exception {
        add(tagId, 2);
        add(tagValue.length(), 2);
        add(tagValue);
    }
    
    public void add(int tagId, byte[] data) throws Exception {
        add(tagId, 2);
        add(data.length, 2);
        add(data);
    }
}
