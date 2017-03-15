package com.shtrih.fiscalprinter.command;

import org.junit.Test;

import static com.shtrih.util.ByteUtils.byteArray;
import static org.junit.Assert.assertArrayEquals;

/**
 * @author P.Zhirkov
 */
public class FSReFiscalizationTests {
    @Test
    public void should_serialize() throws Exception {
        FSReFiscalization cmd = new FSReFiscalization(
                30, "7701234567", "0000000221017090", 17, 24, 4);

        byte[] data = cmd.encodeData();

        byte[] expectedData = byteArray(
                0xFF, 0x34,
                0x1E, 0x00, 0x00, 0x00,
                0x37, 0x37, 0x30, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x00, 0x00,
                0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x32, 0x32, 0x31, 0x30, 0x31, 0x37, 0x30, 0x39, 0x30, 0x00, 0x00, 0x00, 0x00,
                0x11,
                0x18,
                0x04);

        assertArrayEquals(expectedData, data);
    }
}
