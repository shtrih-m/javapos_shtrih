package com.shtrih.fiscalprinter;

import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Date;
import java.util.List;

import static com.shtrih.util.ByteUtils.byteArray;
import com.shtrih.util.Hex;
import static org.junit.Assert.assertEquals;

/**
 * @author P.Zhirkov
 */
public class TLVItemTests {

    @Test
    public void Should_decode_phrases() throws Exception {
        String s = "Мама, мыла; раму!_AaE)";

        byte[] data = new byte[]{(byte) 0x8C, (byte) 0xA0, (byte) 0xAC, (byte) 0xA0, (byte) 0x2C, (byte) 0x20, (byte) 0xAC, (byte) 0xEB, (byte) 0xAB, (byte) 0xA0, (byte) 0x3B, (byte) 0x20, (byte) 0xE0, (byte) 0xA0, (byte) 0xAC, (byte) 0xE3, (byte) 0x21, (byte) 0x5F, (byte) 0x41, (byte) 0x61, (byte) 0x45, (byte) 0x29};

        TLVItem item = new TLVItem(1, data, new TLVTag(666, TLVTag.TLVType.itASCII));
        assertEquals(s, item.getText());
    }

    @Test
    public void Should_decode_int() throws Exception {
        byte[] data = fsWriteTag(12345);

        TLVItem item = new TLVItem(1, data, new TLVTag(666, TLVTag.TLVType.itVLN));
        assertEquals(12345, item.toInt());
        assertEquals("123.45", item.getText());
    }

    @Test
    public void Should_decode_vln() throws Exception {
        byte[] data = new byte[]{-124};

        TLVItem item = new TLVItem(1, data, new TLVTag(666, TLVTag.TLVType.itVLN));
        assertEquals(132, item.toInt());
        assertEquals("1.32", item.getText());
    }

    @Test
    public void Should_decode_vln2() throws Exception {
        byte[] data = new byte[]{-124, 0};

        TLVItem item = new TLVItem(1, data, new TLVTag(666, TLVTag.TLVType.itVLN));
        assertEquals(132, item.toInt());
        assertEquals("1.32", item.getText());
    }

    @Test
    public void Should_decode_vln3() throws Exception {
        byte[] data = new byte[]{-124, 0, 0, 0};

        TLVItem item = new TLVItem(1, data, new TLVTag(666, TLVTag.TLVType.itVLN));
        assertEquals(132, item.toInt());
        assertEquals("1.32", item.getText());
    }

    @Test
    public void Should_decode_date() throws Exception {
        byte[] data = byteArray(0x60, 0x73, 0xC2, 0x5A);

        TLVItem item = new TLVItem(1, data, new TLVTag(666, TLVTag.TLVType.itUnixTime));
        assertEquals("02.04.2018 18:16:00", item.getText());
    }

    @Test
    public void Should_decode_fvln() throws Exception {
        byte[] data = byteArray(0x06, 0x40, 0x42, 0x0F);

        TLVItem item = new TLVItem(1, data, new TLVTag(666, TLVTag.TLVType.itFVLN));
        assertEquals("1.000000", item.getText());
    }

    @Test
    public void Should_decode_fvln2() throws Exception {
        byte[] data = byteArray(0x06, 0x15, 0xCD, 0x5B, 0x07);

        TLVItem item = new TLVItem(1, data, new TLVTag(666, TLVTag.TLVType.itFVLN));
        assertEquals("123.456789", item.getText());
    }

    @Test
    public void Should_decode_fvln3() throws Exception {
        TLVItem item = new TLVItem(1023, "123.456789");
        assertEquals("123.456789", item.getText());
    }
    
    private byte[] fsWriteTag(final int data) throws Exception {
        ByteBuffer buffer = ByteBuffer.allocate(8);

        buffer.order(ByteOrder.LITTLE_ENDIAN);

        buffer.putInt(data);

        return buffer.array();
    }
    
    @Test
    public void testTLVWriter() throws Exception 
    {
        TLVItem item;
        TLVWriter writer = new TLVWriter();
        writer.add(1085, "наименование дополнительного реквизита пользователя");
        writer.add(1086, "значение дополнительного реквизита пользователя");
        byte[] data = writer.getBytes();
        writer.clear();
        writer.add(1084, data);
        data = writer.getBytes();
        
        TLVReader reader = new TLVReader();
        TLVItems items = reader.read(data);
        
        assertEquals(1, items.size());
        assertEquals(1084, items.get(0).getId());
        assertEquals(true, items.get(0).isSTLV());
        assertEquals(null, items.get(0).getData());
        
        TLVItems items2 = items.get(0).getItems();
        assertEquals(2, items2.size());
        
        item = items2.get(0);
        assertEquals(1085, item.getId());
        assertEquals(false, item.isSTLV());
        assertEquals("наименование дополнительного реквизита пользователя", item.getText());
        
        item = items2.get(1);
        assertEquals(1086, item.getId());
        assertEquals(false, item.isSTLV());
        assertEquals("значение дополнительного реквизита пользователя", item.getText());
        
        writer.clear();
        writer.add(items);
        
        String dataHex1 = Hex.toHex(data);
        String dataHex2 = Hex.toHex(writer.getBytes());
        assertEquals(dataHex1, dataHex2);
    }
    
    @Test
    public void testTLVWriter2() throws Exception 
    {
        TLVItem item;
        TLVWriter writer = new TLVWriter();
        writer.add(1085, "наименование дополнительного реквизита пользователя");
        writer.add(1086, "значение дополнительного реквизита пользователя");
        byte[] data = writer.getBytes();
        writer.clear();
        writer.add(1084, data);
        data = writer.getBytes();
        
        TLVReader reader = new TLVReader();
        TLVItems items = reader.read(data);
        items.get(0).getItems().get(0).setText("123");
        items.get(0).getItems().get(1).setText("234");
        
        writer.clear();
        writer.add(items);
        
        reader = new TLVReader();
        items = reader.read(writer.getBytes());
        
        item = items.get(0).getItems().get(0);
        assertEquals(1085, item.getId());
        assertEquals(false, item.isSTLV());
        assertEquals("123", item.getText());
        
        item = items.get(0).getItems().get(1);
        assertEquals(1086, item.getId());
        assertEquals(false, item.isSTLV());
        assertEquals("234", item.getText());
    }
    
    @Test
    public void testTLVWriter3() throws Exception 
    {
        TLVItem item;
        TLVWriter writer = new TLVWriter();
        writer.addTag(1023, "9.95");
        byte[] data = writer.getBytes();
        assertEquals("FF 03 03 00 02 E3 03", Hex.toHex(data));
    }

    
}
