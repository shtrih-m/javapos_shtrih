package com.shtrih.fiscalprinter.command;

import org.junit.Test;

import static com.shtrih.util.ByteUtils.byteArray;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * @author P.Zhirkov
 */
public class GenerateMonoTokenTests {

    @Test
    public void should_serialize_request() throws Exception {
        GenerateMonoTokenCommand cmd = new GenerateMonoTokenCommand();

        byte[] data = cmd.encodeData();

        byte[] expectedData = byteArray(
                0xFF, 0x53);

        assertArrayEquals(expectedData, data);
    }

    @Test
    public void should_deserialize_response() throws Exception {
        GenerateMonoTokenCommand cmd = new GenerateMonoTokenCommand();

        byte[] responseData = byteArray(
                0xFF, 0x53, 0x00, 0x31, 0x39, 0x38, 0x32, 0x36, 0x30, 0x30, 0x34, 0x33, 0x39
        );

        cmd.decodeData(responseData);

        assertEquals("1982600439", cmd.getToken());
    }
}

