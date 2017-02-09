package com.shtrih.fiscalprinter.command;

import org.junit.Test;

import static com.shtrih.util.ByteUtils.byteArray;
import static org.junit.Assert.assertArrayEquals;

/**
 * @author P.Zhirkov
 */
public class FSFiscalizationTests {
    @Test
    public void should_serialize() throws Exception {
        FSFiscalization cmd = new FSFiscalization(
                30, "123456789101", "12345678901234567890", 33, 24);

        byte[] data = cmd.encodeData();

        byte[] expectedData = byteArray(
                0xFF, 0x06,
                0x1E, 0x00, 0x00, 0x00,
                0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39, 0x31, 0x30, 0x31,
                0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39, 0x30, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39, 0x30,
                0x21,
                0x18);

        assertArrayEquals(expectedData, data);
    }
}
