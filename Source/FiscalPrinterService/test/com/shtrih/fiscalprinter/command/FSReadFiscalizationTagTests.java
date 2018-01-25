package com.shtrih.fiscalprinter.command;

import org.junit.Test;

import static com.shtrih.util.ByteUtils.byteArray;
import static org.junit.Assert.assertArrayEquals;

/**
 * @author P.Zhirkov
 */
public class FSReadFiscalizationTagTests {

    @Test
    public void should_serialize_request() throws Exception {
        FSReadFiscalizationTag cmd = new FSReadFiscalizationTag(30, 240, 1041);

        byte[] data = cmd.encodeData();

        byte[] expectedData = byteArray(
                0xFF, 0x0E,
                0x1E, 0x00, 0x00, 0x00, 0xF0, 0x11, 0x04);

        assertArrayEquals(expectedData, data);
    }

    @Test
    public void should_deserialize_response() throws Exception {
        FSReadFiscalizationTag cmd = new FSReadFiscalizationTag(30, 240, 144);

        byte[] responseData = byteArray(
                0xFF, 0x0E, 0x00, 0x11, 0x04, 0x10, 0x00, 0x39, 0x39, 0x39,
                0x39, 0x30, 0x37, 0x38, 0x39, 0x30, 0x30, 0x30, 0x30, 0x32,
                0x32, 0x37, 0x30
        );

        cmd.decodeData(responseData);

        assertArrayEquals(byteArray(
                0x11, 0x04, 0x10, 0x00, 0x39, 0x39, 0x39, 0x39, 0x30, 0x37,
                0x38, 0x39, 0x30, 0x30, 0x30, 0x30, 0x32, 0x32, 0x37, 0x30), cmd.getTagData());
    }
}

