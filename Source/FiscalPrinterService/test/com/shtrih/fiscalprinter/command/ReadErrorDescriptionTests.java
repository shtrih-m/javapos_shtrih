package com.shtrih.fiscalprinter.command;

import org.junit.Test;

import static com.shtrih.util.ByteUtils.byteArray;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * @author P.Zhirkov
 */
public class ReadErrorDescriptionTests {

    @Test
    public void should_serialize_request() throws Exception {
        ReadErrorTextByCode cmd = new ReadErrorTextByCode(94);

        byte[] data = cmd.encodeData();

        byte[] expectedData = byteArray(
                0x6B, 0x5E);

        assertArrayEquals(expectedData, data);
    }

    @Test
    public void should_deserialize_response() throws Exception {
        ReadErrorTextByCode cmd = new ReadErrorTextByCode(94);

        byte[] responseData = byteArray(
                0x6B, 0x00, 0xCA, 0xCA, 0xD2, 0x3A, 0x20, 0xCD, 0xE5, 0xE2,
                0xE5, 0xF0, 0xED, 0xE0, 0xFF, 0x20, 0xEE, 0xEF, 0xE5, 0xF0,
                0xE0, 0xF6, 0xE8, 0xFF
        );

        cmd.decodeData(responseData);

        assertEquals("ККТ: Неверная операция", cmd.getErrorDescription());
    }
}

