package com.shtrih.fiscalprinter.command;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.shtrih.util.Localizer;

public class CommandInputStream {

    private final String charsetName;
    private InputStream stream;

    public CommandInputStream(String charsetName) {
        this.charsetName = charsetName;
    }

    public CommandInputStream(String charsetName, byte[] data) {
        this.charsetName = charsetName;
        this.stream = new ByteArrayInputStream(data);
    }

    public CommandInputStream(byte[] data) {
        this.charsetName = "";
        this.stream = new ByteArrayInputStream(data);
    }

    public CommandInputStream(String charsetName, InputStream in) {
        this.charsetName = charsetName;
        this.stream = in;
    }

    public String getCharsetName() {
        return charsetName;
    }

    public void setData(byte[] data) {
        stream = new ByteArrayInputStream(data);
    }

    public int getSize() throws IOException {
        return stream.available();
    }
    
//    public void checkAvailable(int len) throws Exception
//    {
//        if (stream.available() < len){
//            throw new Exception("No data available");
//        }
//    }
        
    public int readByte() throws Exception
    {
        // checkAvailable(1);
        int b = stream.read();

        if(b == -1)
            throw new Exception("No data available");

        return byteToInt(b);
    }

    public void mark() throws IOException {
        stream.mark(stream.available());
    }
            
    public void reset() throws IOException {
        stream.reset();
    }
    
    public byte[] readBytesToEnd() throws Exception
    {
        return readBytes(stream.available());
    }

    public FSDateTime readFSDateTime() throws Exception
    {
        return new FSDateTime(readBytes(FSDateTime.BodyLength));
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

    public PrinterTime readTimeHMS() throws Exception
    {
        int hour = readByte();
        int min = readByte();
        int sec = readByte();
        return new PrinterTime(hour, min, sec);
    }

    public PrinterTime readTimeHM() throws Exception
    {
        int hour = readByte();
        int min = readByte();
        return new PrinterTime(hour, min, 0);
    }

    public PrinterDate readDate() throws Exception
    {
        int day = readByte();
        int month = readByte();
        int year = readByte() + 2000;
        return new PrinterDate(day, month, year);
    }

    public PrinterDate readFSDate() throws Exception
    {
        int year = readByte() + 2000;
        int month = readByte();
        int day = readByte();
        return new PrinterDate(day, month, year);
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

    public byte[] readBytes(int len) throws Exception
    {
        byte[] data = new byte[len];
        int offset = 0;
        while (len > 0) {
            int count = stream.read(data, offset, len);
            if (count == 0 || count == -1) {
                throw new Exception("No data available");
            }
            len -= count;
            offset += count;
        }
        return data;
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
