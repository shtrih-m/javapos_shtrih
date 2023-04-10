package com.shtrih.fiscalprinter.scoc.commands;

import org.junit.Test;

import static com.shtrih.util.ByteUtils.byteArray;
import static org.junit.Assert.assertEquals;

public class DeviceStatusResponseTests {

    @Test
    public void Should_read_from_input_stream() throws Exception {
        byte[] data = byteArray(
                0xA5, 0x1F, 0x13, 0x00, 0x15, 0x20, 0x01, 0x00, 0x00, 0x17,
                0x20, 0x02, 0x00, 0x08, 0x00, 0x08, 0x20, 0x04, 0x00, 0xD2,
                0x02, 0x96, 0x49);

        DeviceStatusResponse cmd = DeviceStatusResponse.read(data);

        assertEquals(0, cmd.getResultCode());
        assertEquals(8, cmd.getFlags());
        assertEquals(1234567890L, cmd.getDocumentNumber());
    }
}

