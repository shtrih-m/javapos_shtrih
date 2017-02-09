package com.shtrih.fiscalprinter.command;

import org.junit.Test;

import static com.shtrih.util.ByteUtils.byteArray;
import static org.junit.Assert.assertArrayEquals;

/**
 * @author P.Zhirkov
 */
public class FSStartFiscalizationTests {
    @Test
    public void should_serialize() throws Exception {
        FSStartFiscalization cmd = new FSStartFiscalization(30, 2);

        byte[] data = cmd.encodeData();

        byte[] expectedData = byteArray(
                0xFF, 0x05,
                0x1E, 0x00, 0x00, 0x00,
                0x02);

        assertArrayEquals(expectedData, data);
    }
}

