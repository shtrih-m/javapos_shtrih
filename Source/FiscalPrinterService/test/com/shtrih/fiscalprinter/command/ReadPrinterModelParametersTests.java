package com.shtrih.fiscalprinter.command;

import org.junit.Test;

import static com.shtrih.util.ByteUtils.byteArray;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * @author P.Zhirkov
 */
public class ReadPrinterModelParametersTests {

    @Test
    public void should_serialize_request() throws Exception {
        ReadPrinterModelParameters cmd = new ReadPrinterModelParameters();

        byte[] data = cmd.encodeData();

        byte[] expectedData = byteArray(
                0xF7, 0x01);

        assertArrayEquals(expectedData, data);
    }

    @Test
    public void should_deserialize_response() throws Exception {
        ReadPrinterModelParameters cmd = new ReadPrinterModelParameters();

        byte[] responseData = byteArray(
                0xF7, 0x00, 0x18, 0x00, 0x00, 0x37, 0x00, 0x1E, 0x00, 0x00,
                0x00, 0x00, 0x01, 0x0C, 0x0A, 0x00, 0x00, 0x00, 0x00, 0x00,
                0x00, 0x1E, 0x00, 0x00, 0x00, 0x0C, 0x06, 0xFF, 0x00, 0x20,
                0x20, 0x58, 0x02, 0x0E, 0x0F
        );

        cmd.decodeData(responseData);

        PrinterModelParameters status = cmd.getParameters();

        assertEquals(true, status.isGraphics512Supported());
        assertEquals(256, status.getGraphicsWidth());
        assertEquals(256, status.getGraphics512Width());
    }

    @Test
    public void should_deserialize_response2() throws Exception {
        ReadPrinterModelParameters cmd = new ReadPrinterModelParameters();

        byte[] responseData = byteArray(
                0xF7, 0x00, 0x5A, 0xA0, 0x40, 0x03, 0x00, 0x0C, 0x00, 0x00,
                0x0C, 0x18, 0x01, 0x0C, 0x10, 0x00, 0x00, 0x00, 0x00, 0x00,
                0x00, 0x1E, 0x00, 0x00, 0x00, 0x00, 0x06, 0xFF, 0x00, 0x40,
                0x40, 0xB6, 0x03
        );

        cmd.decodeData(responseData);

        PrinterModelParameters status = cmd.getParameters();

        assertEquals(true, status.isGraphics512Supported());
        assertEquals(512, status.getGraphicsWidth());
        assertEquals(512, status.getGraphics512Width());
    }
}

