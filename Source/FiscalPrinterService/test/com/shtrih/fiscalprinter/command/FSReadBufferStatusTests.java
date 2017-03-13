package com.shtrih.fiscalprinter.command;

import org.junit.Test;

import static com.shtrih.util.ByteUtils.byteArray;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * @author P.Zhirkov
 */
public class FSReadBufferStatusTests {
    @Test
    public void should_serialize_request() throws Exception {
        FSReadBufferStatus cmd = new FSReadBufferStatus(30);

        byte[] data = cmd.encodeData();

        byte[] expectedData = byteArray(
                0xFF, 0x30,
                0x1E, 0x00, 0x00, 0x00);

        assertArrayEquals(expectedData, data);
    }

    @Test
    public void should_deserialize_response() throws Exception {
        FSReadBufferStatus cmd = new FSReadBufferStatus(30);

        byte[] responseData = byteArray(
                0xFF, 0x30,
                0x00, 0x4E, 0x01, 0xF0);

        cmd.decodeData(responseData);

        assertEquals(334, cmd.getDataSize());
        assertEquals(240, cmd.getBlockSize());
    }
}

