package com.shtrih.fiscalprinter.command;

import org.junit.Test;

import static com.shtrih.util.ByteUtils.byteArray;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
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
    public void should_deserialize_check() throws Exception {
        FSFindDocument cmd = new FSFindDocument(30, 163);

        byte[] responseData = byteArray(
                0xFF, 0x0A,
                0x00, 0x03, 0x00,
                0x11, 0x03, 0x18, 0x14, 0x35, 0xA3, 0x00, 0x00, 0x00, 0xEB,
                0xEA, 0x4B, 0x2E, 0x01, 0x20, 0x4E, 0x00, 0x00, 0x00
        );

        cmd.decodeData(responseData);

        FSDocument document = cmd.getDocument();

        assertEquals(3, document.getDocType().getValue());
        assertEquals(false, document.getSentToOFD());
        assertArrayEquals(byteArray(
                0x01, 0x20, 0x4E, 0x00, 0x00, 0x00
        ), document.getData());

        assertEquals(2017, document.getDateTime().getYear());
        assertEquals(3, document.getDateTime().getMonth());
        assertEquals(24, document.getDateTime().getDay());
        assertEquals(20, document.getDateTime().getHours());
        assertEquals(53, document.getDateTime().getMinutes());
        assertEquals(0xA3, document.getDocNumber());
        assertEquals(0x2E4BEAEBL, document.getFP());
    }

    @Test
    public void should_deserialize_fiscalization() throws Exception {
        FSFindDocument cmd = new FSFindDocument(30, 163);

        byte[] responseData = byteArray(
                0xFF, 0x0A, 0x00, 0x01, 0x00, 0x11, 0x07, 0x06, 0x02, 0x34,
                0x01, 0x00, 0x00, 0x00, 0xB7, 0x9F, 0xD1, 0x4D, 0x35, 0x30,
                0x32, 0x34, 0x30, 0x35, 0x34, 0x34, 0x34, 0x35, 0x20, 0x20,
                0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x31, 0x33, 0x35, 0x38,
                0x30, 0x32, 0x36, 0x31, 0x33, 0x31, 0x20, 0x20, 0x20, 0x20,
                0x3F, 0x01
        );

        cmd.decodeData(responseData);

        FSDocument document = cmd.getDocument();

        assertEquals(1, document.getDocType().getValue());
        assertEquals(false, document.getSentToOFD());
        assertArrayEquals(byteArray(
                0x35, 0x30, 0x32, 0x34, 0x30, 0x35, 0x34, 0x34, 0x34, 0x35,
                0x20, 0x20, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x31, 0x33,
                0x35, 0x38, 0x30, 0x32, 0x36, 0x31, 0x33, 0x31, 0x20, 0x20,
                0x20, 0x20, 0x3F, 0x01
        ), document.getData());

        assertEquals(2017, document.getDateTime().getYear());
        assertEquals(7, document.getDateTime().getMonth());
        assertEquals(6, document.getDateTime().getDay());
        assertEquals(2, document.getDateTime().getHours());
        assertEquals(52, document.getDateTime().getMinutes());
        assertEquals(1, document.getDocNumber());
        assertEquals(1305583543, document.getFP());
    }

    @Test
    public void should_deserialize_refiscalization() throws Exception {
        FSFindDocument cmd = new FSFindDocument(30, 163);

        byte[] responseData = byteArray(
                0xFF, 0x0A, 0x00, 0x0B, 0x00, 0x11, 0x07, 0x06, 0x02, 0x35,
                0x02, 0x00, 0x00, 0x00, 0x22, 0xE8, 0x9A, 0x18, 0x35, 0x30,
                0x32, 0x34, 0x30, 0x35, 0x34, 0x34, 0x34, 0x35, 0x20, 0x20,
                0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x31, 0x33, 0x35, 0x38,
                0x30, 0x32, 0x36, 0x31, 0x33, 0x31, 0x20, 0x20, 0x20, 0x20,
                0x3F, 0x01, 0x04
        );

        cmd.decodeData(responseData);

        FSDocument document = cmd.getDocument();

        assertEquals(11, document.getDocType().getValue());
        assertEquals(false, document.getSentToOFD());
        assertArrayEquals(byteArray(
                0x35, 0x30, 0x32, 0x34, 0x30, 0x35, 0x34, 0x34, 0x34, 0x35,
                0x20, 0x20, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x31, 0x33,
                0x35, 0x38, 0x30, 0x32, 0x36, 0x31, 0x33, 0x31, 0x20, 0x20,
                0x20, 0x20, 0x3F, 0x01, 0x04
        ), document.getData());

        assertEquals(2017, document.getDateTime().getYear());
        assertEquals(7, document.getDateTime().getMonth());
        assertEquals(6, document.getDateTime().getDay());
        assertEquals(2, document.getDateTime().getHours());
        assertEquals(53, document.getDateTime().getMinutes());
        assertEquals(2, document.getDocNumber());
        assertEquals(412805154, document.getFP());
    }

    @Test
    public void should_deserialize_shift_open() throws Exception {
        FSFindDocument cmd = new FSFindDocument(30, 163);

        byte[] responseData = byteArray(
                0xFF, 0x0A, 0x00, 0x02, 0x00, 0x11, 0x07, 0x06, 0x02, 0x35,
                0x03, 0x00, 0x00, 0x00, 0x5B, 0xE1, 0xAE, 0xD0, 0x01, 0x00
        );

        cmd.decodeData(responseData);

        FSDocument document = cmd.getDocument();

        assertEquals(2, document.getDocType().getValue());
        assertEquals(false, document.getSentToOFD());
        assertArrayEquals(byteArray(
                0x01, 0x00
        ), document.getData());

        assertEquals(2017, document.getDateTime().getYear());
        assertEquals(7, document.getDateTime().getMonth());
        assertEquals(6, document.getDateTime().getDay());
        assertEquals(2, document.getDateTime().getHours());
        assertEquals(53, document.getDateTime().getMinutes());
        assertEquals(3, document.getDocNumber());
        assertEquals(3501121883L, document.getFP());
    }

    @Test
    public void should_deserialize_shift_close() throws Exception {
        FSFindDocument cmd = new FSFindDocument(30, 163);

        byte[] responseData = byteArray(
                0xFF, 0x0A, 0x00, 0x05, 0x00, 0x11, 0x07, 0x06, 0x02, 0x36,
                0x06, 0x00, 0x00, 0x00, 0xFD, 0x00, 0x61, 0xD9, 0x01, 0x00
        );

        cmd.decodeData(responseData);

        FSDocument document = cmd.getDocument();

        assertEquals(5, document.getDocType().getValue());
        assertEquals(false, document.getSentToOFD());
        assertArrayEquals(byteArray(
                0x01, 0x00
        ), document.getData());

        assertEquals(2017, document.getDateTime().getYear());
        assertEquals(7, document.getDateTime().getMonth());
        assertEquals(6, document.getDateTime().getDay());
        assertEquals(2, document.getDateTime().getHours());
        assertEquals(54, document.getDateTime().getMinutes());
        assertEquals(6, document.getDocNumber());
        assertEquals(3647013117L, document.getFP());
    }
}

