package com.shtrih.fiscalprinter.command;

import org.junit.Test;

import static com.shtrih.util.ByteUtils.byteArray;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * @author P.Zhirkov
 */
public class FSFindDocumentTests {

    @Test
    public void should_serialize_request() throws Exception {
        FSFindDocument cmd = new FSFindDocument(30, 163);

        byte[] data = cmd.encodeData();

        byte[] expectedData = byteArray(0xFF, 0x0A, 0x1E, 0x00, 0x00, 0x00, 0xA3, 0x00, 0x00, 0x00);

        assertArrayEquals(expectedData, data);
    }

    @Test
    public void should_deserialize_response() throws Exception {
        FSFindDocument cmd = new FSFindDocument(30, 163);

        byte[] responseData = byteArray(
                0xFF, 0x0A,
                0x00, 0x03, 0x00,
                0x11, 0x03, 0x18, 0x14, 0x35, 0xA3, 0x00, 0x00, 0x00, 0xEB,
                0xEA, 0x4B, 0x2E, 0x01, 0x20, 0x4E, 0x00, 0x00, 0x00
        );

        cmd.decodeData(responseData);

        FSDocumentInfo document = cmd.getDocument();

        assertEquals(3, document.getDocumentType().getValue());
        assertEquals(false, document.getSentToOFD());
        assertArrayEquals(byteArray(

                0xA3, 0x00, 0x00, 0x00, 0xEB,
                0xEA, 0x4B, 0x2E, 0x01, 0x20, 0x4E, 0x00, 0x00, 0x00
        ), document.getData());

        assertEquals(2017, document.getDateTime().getYear());
        assertEquals(3, document.getDateTime().getMonth());
        assertEquals(24, document.getDateTime().getDay());
        assertEquals(20, document.getDateTime().getHours());
        assertEquals(53, document.getDateTime().getMinutes());
    }
}

