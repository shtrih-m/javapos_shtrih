package com.shtrih.fiscalprinter.command;

import org.junit.Test;

import static com.shtrih.util.ByteUtils.byteArray;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * @author P.Zhirkov
 */
public class WriteSerialNumberCommandTests {

    @Test
    public void should_serialize_request() throws Exception {
        WriteSerialNumberCommand cmd = new WriteSerialNumberCommand("0000000123456789");

        byte[] data = cmd.encodeData();

        byte[] expectedData = byteArray(
                0xFE, 0xF5, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x31,
                0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39);

        assertArrayEquals(expectedData, data);
    }
}

