/*
 * CommandInputStream.java
 *
 * Created on 2 April 2008, 17:06
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.shtrih.fiscalprinter.command;

/**
 *
 * @author V.Kravtsov
 */
import java.io.ByteArrayInputStream;

import com.shtrih.ej.EJDate;
import com.shtrih.ej.EJTime;
import com.shtrih.util.Localizer;

public class CommandInputStream {

    private final String charsetName;
    private ByteArrayInputStream stream;

    public CommandInputStream(String charsetName) {
        this.charsetName = charsetName;
    }

    public String getCharsetName() {
        return charsetName;
    }

    public void setData(byte[] data) {
        stream = new ByteArrayInputStream(data);
    }

    public int getSize() {
        return stream.available();
    }

    public int readByte() {
        int B = (byte) stream.read();
        return byteToInt(B);
    }

    public int byteToInt(int B) {
        if (B < 0) {
            B = (int) (256 + B);
        }
        return B;
    }

    public int readShort() throws Exception {
        int ch2 = readByte();
        int ch1 = readByte();
        return ((ch1 << 8) + (ch2 << 0));
    }

    public PrinterTime readTime() {
        int hour = readByte();
        int min = readByte();
        int sec = readByte();
        return new PrinterTime(hour, min, sec);
    }

    public PrinterTime readTime2() {
        int hour = readByte();
        int min = readByte();
        return new PrinterTime(hour, min, 0);
    }
    
    public PrinterDate readDate() {
        int day = readByte();
        int month = readByte();
        int year = readByte();
        return new PrinterDate(day, month, year);
    }

    public PrinterDate readDateYMD() {
        int year = readByte();
        int month = readByte();
        int day = readByte();
        return new PrinterDate(day, month, year);
    }    
    
    public EJDate readEJDate() {
        int year = readByte();
        int month = readByte();
        int day = readByte();
        return new EJDate(day, month, year);
    }

    public EJTime readEJTime() {
        int hour = readByte();
        int min = readByte();
        return new EJTime(hour, min);
    }

    public int readInt() throws Exception {
        int ch4 = readByte();
        int ch3 = readByte();
        int ch2 = readByte();
        int ch1 = readByte();
        return ((ch1 << 24) + (ch2 << 16) + (ch3 << 8) + (ch4 << 0));
    }

    public void checkLength(int value, int min, int max) throws Exception {
        if ((value < min) || (value > max)) {
            throw new IllegalArgumentException(
                    Localizer.getString(Localizer.InvalidAnswerLength));
        }
    }

    public long readLong(int len) throws Exception {
        checkLength(len, 1, 8);
        long B;
        long result = 0;
        for (int i = 0; i < len; i++) {
            B = readByte();
            result += (B << (8 * i));
        }
        return result;
    }

    public long readLong2(int len) throws Exception {
        long result = 0;
        checkLength(len, 1, 8);
        byte[] data = readBytes(len);
        if (!arrayEqual(data, (byte) 0xFF)) {
            for (int i = 0; i < len; i++) {
                long B = byteToInt(data[i]);
                result += (B << (8 * i));
            }
        }
        return result;
    }

    public boolean arrayEqual(byte[] data, byte value) {
        boolean result = false;
        for (int i = 0; i < data.length; i++) {
            result = data[i] == value;
            if (!result) {
                break;
            }
        }
        return result;
    }

    public byte[] readBytes(int len) {
        byte[] b = new byte[len];
        stream.read(b, 0, len);
        return b;
    }

    public String readString(int len) throws Exception {
        return new String(readBytes(len), charsetName);
    }

    public String readString() throws Exception {
        return readString(charsetName);
    }

    public String readString(String charsetName) throws Exception {
        byte[] data = trimRight(readBytes(stream.available()));
        return new String(data, charsetName);
    }

    public static byte[] trimRight(byte[] data) {
        byte[] result = new byte[0];

        int i = 0;
        while (i < data.length) {
            if (data[i] == 0) {
                break;
            }
            i++;
        }
        if (i > 0) {
            result = new byte[i];
            System.arraycopy(data, 0, result, 0, i);
        }
        return result;
    }
}
