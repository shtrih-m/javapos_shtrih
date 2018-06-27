package com.shtrih.fiscalprinter.scoc.commands;

import com.shtrih.fiscalprinter.scoc.commands.DeviceStatusCommand;

import org.junit.Test;

import static com.shtrih.fiscalprinter.ByteUtils.byteArray;
import static org.junit.Assert.assertArrayEquals;

public class DeviceStatusCommandTests {
    @Test
    public void Should_build_command() {
        byte[] expectedData = byteArray(
                0x47, 0x1F, 0x1C, 0x00,
                0x08, 0x20, 0x04, 0x00, 0x01, 0x00, 0x00, 0x00,
                0x58, 0x1B, 0x04, 0x00, 0x51, 0xB6, 0x2C, 0x5B,
                0x1F, 0x20, 0x08, 0x00,
                  0x1B, 0x20, 0x04, 0x00, 0xA8, 0xDC, 0x8D, 0x59);

        DeviceStatusCommand cmd = new DeviceStatusCommand(1, 1529656913, 1502469288);

        assertArrayEquals(expectedData, cmd.toBytes());
    }
}
