package com.shtrih.fiscalprinter;

import org.junit.Test;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertEquals;

/**
 * @author P.Zhirkov
 */
public class TLVReaderTests {

    @Test
    public void Should_decode_vln_tag() throws Exception {
        byte[] data = fsWriteTag(1011, 12345);

        TLVParser reader = new TLVParser();
        reader.read(data);

        TLVItems items = reader.getItems();
        assertEquals(1, items.size());

        TLVItem item = items.get(0);

        assertEquals(12345, item.toInt(item.getData()));
        assertEquals("123.45", item.getText());
    }

    private byte[] fsWriteTag(final int tag, final int data) throws Exception {
        ByteBuffer buffer = ByteBuffer.allocate(8);

        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.putShort((short) (tag));
        buffer.putShort((short) (4));
        buffer.putInt(data);

        return buffer.array();
    }

    @Test
    public void Should_not_print_1084__tag() throws Exception {
        TLVWriter stlvDataWriter = new TLVWriter();
        stlvDataWriter.add(1085, "Назваие");
        stlvDataWriter.add(1086, "Значение");
        byte[] stlvData = stlvDataWriter.getBytes();

        TLVWriter stlvWriter = new TLVWriter();
        stlvWriter.add(1084, stlvData);

        byte[] data = stlvWriter.getBytes();

        TLVParser reader = new TLVParser();
        reader.read(data);

        List<String> items = reader.getPrintText();
        assertEquals(0, items.size());
    }

    @Test
    public void Should_not_print_1203__tag() throws Exception {
        TLVWriter stlvDataWriter = new TLVWriter();
        stlvDataWriter.add(1203, "0123456789");
        byte[] data = stlvDataWriter.getBytes();

        TLVParser reader = new TLVParser();
        reader.read(data);

        List<String> items = reader.getPrintText();
        assertEquals(0, items.size());
    }

    @Test
    public void Should_correctly_print_vln_tags() throws Exception {
        TLVWriter stlvDataWriter = new TLVWriter();
        stlvDataWriter.add(1184, 1234567,8);
        byte[] data = stlvDataWriter.getBytes();

        TLVParser reader = new TLVParser();
        reader.read(data);

        List<String> items = reader.getPrintText();
        assertEquals(1, items.size());
        assertEquals("СУММА КОРРЕКЦ. БЕЗ НДС: 12345.67", items.get(0));
    }

    @Test
    public void Should_correctly_print_customer_email() throws Exception {
        TLVWriter stlvDataWriter = new TLVWriter();
        stlvDataWriter.add(1008, "nyx@mail.ru");
        byte[] data = stlvDataWriter.getBytes();

        TLVParser reader = new TLVParser();
        reader.read(data);

        List<String> items = reader.getPrintText();
        assertEquals(1, items.size());
        assertEquals("ЭЛ. АДР. ПОКУПАТЕЛЯ: nyx@mail.ru", items.get(0));
    }

    @Test
    public void Should_correctly_print_customer_phone() throws Exception {
        TLVWriter stlvDataWriter = new TLVWriter();
        stlvDataWriter.add(1008, "+79006008070");
        byte[] data = stlvDataWriter.getBytes();

        TLVParser reader = new TLVParser();
        reader.read(data);

        List<String> items = reader.getPrintText();
        assertEquals(1, items.size());
        assertEquals("ТЕЛ. ПОКУПАТЕЛЯ: +79006008070", items.get(0));
    }
}
