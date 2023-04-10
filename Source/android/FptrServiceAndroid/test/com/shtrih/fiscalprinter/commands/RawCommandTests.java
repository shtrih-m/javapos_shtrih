package com.shtrih.fiscalprinter.commands;

import com.shtrih.fiscalprinter.command.RawCommand;

import org.junit.Test;

import static com.shtrih.util.ByteUtils.byteArray;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class RawCommandTests {
    @Test
    public void should_handle_2_byte_command() throws Exception {

        byte[] requestData = byteArray(
                0xFF, 0x30,
                0x1E, 0x00, 0x00, 0x00);

        RawCommand cmd = new RawCommand(requestData);

        byte[] data = cmd.encodeData();

        assertEquals(0xFF30, cmd.getCode());
        assertArrayEquals(requestData, data);

        byte[] responseData = byteArray(
                0xFF, 0x30,
                0x00, 0x4E, 0x01, 0xF0);

        cmd.decodeData(responseData);
        assertEquals(0x00, cmd.getResultCode());
        assertArrayEquals(responseData, cmd.getRxData());
    }

    @Test
    public void should_handle_1_byte_0xFF_command() throws Exception {

        byte[] requestData = byteArray(0xFF);

        RawCommand cmd = new RawCommand(requestData);

        byte[] data = cmd.encodeData();

        assertEquals(0xFF, cmd.getCode());
        assertArrayEquals(requestData, data);

        byte[] responseData = byteArray(
                0xFF, 0x30,
                0x00, 0x4E, 0x01, 0xF0);

        cmd.decodeData(responseData);
        assertEquals(0x30, cmd.getResultCode());
        assertArrayEquals(responseData, cmd.getRxData());
    }

    @Test
    public void should_handle_1_byte_command() throws Exception {

        byte[] requestData = byteArray(0xFE, 0xF3, 0x00, 0x00, 0x00);

        RawCommand cmd = new RawCommand(requestData);

        byte[] data = cmd.encodeData();

        assertEquals(0xFE, cmd.getCode());
        assertArrayEquals(requestData, data);

        byte[] responseData = byteArray(
                0xFE, 0x00);

        cmd.decodeData(responseData);
        assertEquals(0x00, cmd.getResultCode());
        assertArrayEquals(responseData, cmd.getRxData());
    }
}
