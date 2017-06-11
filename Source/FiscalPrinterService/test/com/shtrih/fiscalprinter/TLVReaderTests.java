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
        byte[] data =  fsWriteTag(1011, 12345);

        TLVParser reader = new TLVParser();
        reader.read(data);

        List<TLVItem> items = reader.getItems();
        assertEquals(1, items.size());

        TLVItem item = items.get(0);

        assertEquals(12345, item.toInt(item.getData()));
        assertEquals("123.45", item.getText());
    }

    private byte[] fsWriteTag(final int tag, final int data) throws Exception {
        ByteBuffer buffer = ByteBuffer.allocate(8);

        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.putShort((short)(tag));
        buffer.putShort((short)(4));
        buffer.putInt(data);

        return buffer.array();
    }
}
