package com.shtrih.fiscalprinter;

import org.junit.Test;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import static org.junit.Assert.assertEquals;

/**
 * @author P.Zhirkov
 */
public class TLVItemTests {

    @Test
    public void Should_decode_phrases() throws Exception {
        String s = "Мама, мыла; раму!_AaE)";

        byte[] data = new byte[]{(byte) 0x8C, (byte) 0xA0, (byte) 0xAC, (byte) 0xA0, (byte) 0x2C, (byte) 0x20, (byte) 0xAC, (byte) 0xEB, (byte) 0xAB, (byte) 0xA0, (byte) 0x3B, (byte) 0x20, (byte) 0xE0, (byte) 0xA0, (byte) 0xAC, (byte) 0xE3, (byte) 0x21, (byte) 0x5F, (byte) 0x41, (byte) 0x61, (byte) 0x45, (byte) 0x29};

        TLVItem item = new TLVItem(new TLVInfo(666, TLVInfo.TLVType.itASCII), data, 12);
        assertEquals(s, item.getText());
    }

    @Test
    public void Should_decode_int() throws Exception {
        byte[] data = fsWriteTag(12345);

        TLVItem item = new TLVItem(new TLVInfo(666, TLVInfo.TLVType.itVLN), data, 12);
        assertEquals(12345, item.toInt(item.getData()));
        assertEquals("123,45", item.getText());
    }

    @Test
    public void Should_decode_vln() throws Exception {
        byte[] data = new byte[]{-124};

        TLVItem item = new TLVItem(new TLVInfo(666, TLVInfo.TLVType.itVLN), data, 12);
        assertEquals(132, item.toInt(item.getData()));
        assertEquals("1,32", item.getText());
    }

    @Test
    public void Should_decode_vln2() throws Exception {
        byte[] data = new byte[]{-124, 0};

        TLVItem item = new TLVItem(new TLVInfo(666, TLVInfo.TLVType.itVLN), data, 12);
        assertEquals(132, item.toInt(item.getData()));
        assertEquals("1,32", item.getText());
    }

    @Test
    public void Should_decode_vln3() throws Exception {
        byte[] data = new byte[]{-124, 0, 0, 0};

        TLVItem item = new TLVItem(new TLVInfo(666, TLVInfo.TLVType.itVLN), data, 12);
        assertEquals(132, item.toInt(item.getData()));
        assertEquals("1,32", item.getText());
    }

    private byte[] fsWriteTag(final int data) throws Exception {
        ByteBuffer buffer = ByteBuffer.allocate(8);

        buffer.order(ByteOrder.LITTLE_ENDIAN);

        buffer.putInt(data);

        return buffer.array();
    }
}
