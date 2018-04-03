package com.shtrih.fiscalprinter;

import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Date;

import static com.shtrih.util.ByteUtils.byteArray;
import static org.junit.Assert.assertEquals;

/**
 * @author P.Zhirkov
 */
public class TLVItemTests {

    @Test
    public void Should_decode_phrases() throws Exception {
        String s = "Мама, мыла; раму!_AaE)";

        byte[] data = new byte[]{(byte) 0x8C, (byte) 0xA0, (byte) 0xAC, (byte) 0xA0, (byte) 0x2C, (byte) 0x20, (byte) 0xAC, (byte) 0xEB, (byte) 0xAB, (byte) 0xA0, (byte) 0x3B, (byte) 0x20, (byte) 0xE0, (byte) 0xA0, (byte) 0xAC, (byte) 0xE3, (byte) 0x21, (byte) 0x5F, (byte) 0x41, (byte) 0x61, (byte) 0x45, (byte) 0x29};

        TLVItem item = new TLVItem(new TLVTag(666, TLVTag.TLVType.itASCII), data, 12);
        assertEquals(s, item.getText());
    }

    @Test
    public void Should_decode_int() throws Exception {
        byte[] data = fsWriteTag(12345);

        TLVItem item = new TLVItem(new TLVTag(666, TLVTag.TLVType.itVLN), data, 12);
        assertEquals(12345, item.toInt());
        assertEquals("123.45", item.getText());
    }

    @Test
    public void Should_decode_vln() throws Exception {
        byte[] data = new byte[]{-124};

        TLVItem item = new TLVItem(new TLVTag(666, TLVTag.TLVType.itVLN), data, 12);
        assertEquals(132, item.toInt());
        assertEquals(BigDecimal.valueOf(132, 2), item.toVLN());
        assertEquals("1.32", item.getText());
    }

    @Test
    public void Should_decode_vln2() throws Exception {
        byte[] data = new byte[]{-124, 0};

        TLVItem item = new TLVItem(new TLVTag(666, TLVTag.TLVType.itVLN), data, 12);
        assertEquals(132, item.toInt());
        assertEquals(BigDecimal.valueOf(132, 2), item.toVLN());
        assertEquals("1.32", item.getText());
    }

    @Test
    public void Should_decode_vln3() throws Exception {
        byte[] data = new byte[]{-124, 0, 0, 0};

        TLVItem item = new TLVItem(new TLVTag(666, TLVTag.TLVType.itVLN), data, 12);
        assertEquals(132, item.toInt());
        assertEquals(BigDecimal.valueOf(132, 2), item.toVLN());
        assertEquals("1.32", item.getText());
    }

    @Test
    public void Should_decode_date() throws Exception {
        byte[] data = byteArray(0x60, 0x73, 0xC2, 0x5A);

        TLVItem item = new TLVItem(new TLVTag(666, TLVTag.TLVType.itUnixTime), data, 12);
        assertEquals(new Date(2018 - 1900, 4 - 1, 2, 18, 16, 0), item.toDate());
        assertEquals("02.04.2018 18:16:00", item.getText());
    }

    @Test
    public void Should_decode_fvln() throws Exception {
        byte[] data = byteArray(0x06, 0x40, 0x42, 0x0F);

        TLVItem item = new TLVItem(new TLVTag(666, TLVTag.TLVType.itFVLN), data, 12);

        assertEquals(new BigDecimal(1000000).divide(new BigDecimal(1000000), 6, RoundingMode.HALF_UP), item.toFVLN());
        assertEquals("1.000000", item.getText());
    }

    @Test
    public void Should_decode_fvln2() throws Exception {
        byte[] data = byteArray(0x06, 0x15, 0xCD, 0x5B, 0x07);

        TLVItem item = new TLVItem(new TLVTag(666, TLVTag.TLVType.itFVLN), data, 12);
        assertEquals(BigDecimal.valueOf(123456789, 6), item.toFVLN());
        assertEquals("123.456789", item.getText());
    }

    private byte[] fsWriteTag(final int data) throws Exception {
        ByteBuffer buffer = ByteBuffer.allocate(8);

        buffer.order(ByteOrder.LITTLE_ENDIAN);

        buffer.putInt(data);

        return buffer.array();
    }
}
