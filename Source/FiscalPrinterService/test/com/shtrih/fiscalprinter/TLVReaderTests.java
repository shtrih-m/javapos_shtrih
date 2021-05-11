package com.shtrih.fiscalprinter;

import org.junit.Test;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.List;
import java.util.Vector;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertEquals;

/**
 * @author P.Zhirkov
 */
public class TLVReaderTests {

    @Test
    public void Should_decode_vln_tag() throws Exception {
        byte[] data = fsWriteTag(1011, 12345);

        TLVReader reader = new TLVReader();
        List<TLVItem> items = reader.read(data);

        assertEquals(1, items.size());

        TLVItem item = items.get(0);

        assertEquals(12345, item.toInt());
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

        TLVReader reader = new TLVReader();
        TLVTextWriter writer = new TLVTextWriter(reader.read(data));

        List<String> items = new Vector<String>();
        writer.getPrintText(items);
        assertEquals(0, items.size());
    }

    @Test
    public void Should_not_print_1203__tag() throws Exception {
        TLVWriter stlvDataWriter = new TLVWriter();
        stlvDataWriter.add(1203, "0123456789");
        byte[] data = stlvDataWriter.getBytes();

        TLVReader reader = new TLVReader();
        TLVTextWriter writer = new TLVTextWriter(reader.read(data));

        List<String> items = new Vector<String>();
        writer.getPrintText(items);
        assertEquals(0, items.size());
    }

    @Test
    public void Should_correctly_print_vln_tags() throws Exception {
        TLVWriter stlvDataWriter = new TLVWriter();
        stlvDataWriter.add(1184, 1234567, 8);
        byte[] data = stlvDataWriter.getBytes();

        TLVReader reader = new TLVReader();
        TLVTextWriter writer = new TLVTextWriter(reader.read(data));

        List<String> items = new Vector<String>();
        writer.getPrintText(items);
        assertEquals(1, items.size());
        assertEquals("СУММА КОРРЕКЦ. БЕЗ НДС: 12345.67", items.get(0));
    }

    @Test
    public void Should_correctly_print_customer_email() throws Exception {
        TLVWriter stlvDataWriter = new TLVWriter();
        stlvDataWriter.add(1008, "nyx@mail.ru");
        byte[] data = stlvDataWriter.getBytes();

        TLVReader reader = new TLVReader();
        TLVTextWriter writer = new TLVTextWriter(reader.read(data));

        List<String> items = new Vector<String>();
        writer.getPrintText(items);
        assertEquals(1, items.size());
        assertEquals("ЭЛ. АДР. ПОКУПАТЕЛЯ: nyx@mail.ru", items.get(0));
    }

    @Test
    public void Should_correctly_print_customer_phone() throws Exception {
        TLVWriter stlvDataWriter = new TLVWriter();
        stlvDataWriter.add(1008, "+79006008070");
        byte[] data = stlvDataWriter.getBytes();

        TLVReader reader = new TLVReader();
        TLVTextWriter writer = new TLVTextWriter(reader.read(data));

        List<String> items = new Vector<String>();
        writer.getPrintText(items);
        assertEquals(1, items.size());
        assertEquals("ТЕЛ. ПОКУПАТЕЛЯ: +79006008070", items.get(0));
    }

    @Test
    public void Should_correctly_print_tag_1075() throws Exception {
        TLVWriter stlvDataWriter = new TLVWriter();
        stlvDataWriter.add(1075, "+79006008070");
        byte[] data = stlvDataWriter.getBytes();

        TLVReader reader = new TLVReader();
        TLVTextWriter writer = new TLVTextWriter(reader.read(data));

        List<String> items = new Vector<String>();
        writer.getPrintText(items);
        assertEquals(1, items.size());
        assertEquals("ТЛФ. ОП. ПЕРЕВОДА: +79006008070", items.get(0));
    }

    @Test
    public void Should_correctly_print_tag_1044() throws Exception {
        TLVWriter stlvDataWriter = new TLVWriter();
        stlvDataWriter.add(1044, "+79006008070");
        byte[] data = stlvDataWriter.getBytes();

        TLVReader reader = new TLVReader();
        TLVTextWriter writer = new TLVTextWriter(reader.read(data));

        List<String> items = new Vector<String>();
        writer.getPrintText(items);
        assertEquals(1, items.size());
        assertEquals("ОП. АГЕНТА: +79006008070", items.get(0));
    }

    @Test
    public void Should_correctly_print_tag_1073() throws Exception {
        TLVWriter stlvDataWriter = new TLVWriter();
        stlvDataWriter.add(1073, "+79006008070");
        byte[] data = stlvDataWriter.getBytes();

        TLVReader reader = new TLVReader();
        TLVTextWriter writer = new TLVTextWriter(reader.read(data));

        List<String> items = new Vector<String>();
        writer.getPrintText(items);
        assertEquals(1, items.size());
        assertEquals("ТЛФ. ПЛ. АГЕНТА: +79006008070", items.get(0));
    }

    @Test
    public void Should_correctly_print_tag_1074() throws Exception {
        TLVWriter stlvDataWriter = new TLVWriter();
        stlvDataWriter.add(1074, "+79006008070");
        byte[] data = stlvDataWriter.getBytes();

        TLVReader reader = new TLVReader();
        TLVTextWriter writer = new TLVTextWriter(reader.read(data));

        List<String> items = new Vector<String>();
        writer.getPrintText(items);
        assertEquals(1, items.size());
        assertEquals("ТЛФ. ОП. ПР. ПЛАТЕЖА: +79006008070", items.get(0));
    }

    @Test
    public void Should_correctly_print_tag_1026() throws Exception {
        TLVWriter stlvDataWriter = new TLVWriter();
        stlvDataWriter.add(1026, "+79006008070");
        byte[] data = stlvDataWriter.getBytes();

        TLVReader reader = new TLVReader();
        TLVTextWriter writer = new TLVTextWriter(reader.read(data));

        List<String> items = new Vector<String>();
        writer.getPrintText(items);
        assertEquals(1, items.size());
        assertEquals("ОПЕРАТОР ПЕРЕВОДА: +79006008070", items.get(0));
    }

    @Test
    public void Should_correctly_print_tag_1005() throws Exception {
        TLVWriter stlvDataWriter = new TLVWriter();
        stlvDataWriter.add(1005, "+79006008070");
        byte[] data = stlvDataWriter.getBytes();

        TLVReader reader = new TLVReader();
        TLVTextWriter writer = new TLVTextWriter(reader.read(data));

        List<String> items = new Vector<String>();
        writer.getPrintText(items);
        assertEquals(1, items.size());
        assertEquals("АДР. ОП. ПЕРЕВОДА: +79006008070", items.get(0));
    }

    @Test
    public void Should_correctly_print_tag_1016() throws Exception {
        TLVWriter stlvDataWriter = new TLVWriter();
        stlvDataWriter.add(1016, "+79006008070");
        byte[] data = stlvDataWriter.getBytes();

        TLVReader reader = new TLVReader();
        TLVTextWriter writer = new TLVTextWriter(reader.read(data));

        List<String> items = new Vector<String>();
        writer.getPrintText(items);
        assertEquals(1, items.size());
        assertEquals("ИНН ОП. ПЕРЕВОДА: +79006008070", items.get(0));
    }

    @Test
    public void Should_correctly_print_tag_1171() throws Exception {
        TLVWriter stlvDataWriter = new TLVWriter();
        stlvDataWriter.add(1171, "+79006008070");
        byte[] data = stlvDataWriter.getBytes();

        TLVReader reader = new TLVReader();
        TLVTextWriter writer = new TLVTextWriter(reader.read(data));

        List<String> items = new Vector<String>();
        writer.getPrintText(items);
        assertEquals(1, items.size());
        assertEquals("ТЛФ. ПОСТ.: +79006008070", items.get(0));
    }

    @Test
    public void Should_correctly_print_agent_type() throws Exception {
        TLVWriter stlvDataWriter = new TLVWriter();
        stlvDataWriter.add(1057, 127, 1);
        byte[] data = stlvDataWriter.getBytes();

        TLVReader reader = new TLVReader();
        TLVTextWriter writer = new TLVTextWriter(reader.read(data));

        List<String> items = new Vector<String>();
        writer.getPrintText(items);
        assertEquals(1, items.size());
        assertEquals("БАНК. ПЛ. АГЕНТ БАНК. ПЛ. СУБАГЕНТ ПЛ. АГЕНТ ПЛ. СУБАГЕНТ ПОВЕРЕННЫЙ КОМИССИОНЕР АГЕНТ", items.get(0));
    }

    @Test
    public void Should_correctly_print_agent_type2() throws Exception {
        TLVWriter stlvDataWriter = new TLVWriter();
        stlvDataWriter.add(1057, 21, 1);
        byte[] data = stlvDataWriter.getBytes();

        TLVReader reader = new TLVReader();
        TLVTextWriter writer = new TLVTextWriter(reader.read(data));

        List<String> items = new Vector<String>();
        writer.getPrintText(items);
        assertEquals(1, items.size());
        assertEquals("БАНК. ПЛ. АГЕНТ ПЛ. АГЕНТ ПОВЕРЕННЫЙ", items.get(0));
    }

    @Test
    public void Should_correctly_print_agent_type3() throws Exception {
        TLVWriter stlvDataWriter = new TLVWriter();
        stlvDataWriter.add(1057, 32, 1);
        byte[] data = stlvDataWriter.getBytes();

        TLVReader reader = new TLVReader();
        TLVTextWriter writer = new TLVTextWriter(reader.read(data));

        List<String> items = new Vector<String>();
        writer.getPrintText(items);
        assertEquals(1, items.size());
        assertEquals("КОМИССИОНЕР", items.get(0));
    }
}
