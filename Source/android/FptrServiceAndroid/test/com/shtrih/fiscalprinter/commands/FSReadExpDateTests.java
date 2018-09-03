package com.shtrih.fiscalprinter.commands;

import com.shtrih.fiscalprinter.command.FSReadExpDate;

import org.junit.Test;

import static com.shtrih.fiscalprinter.ByteUtils.byteArray;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * @author P.Zhirkov
 */
public class FSReadExpDateTests {

    @Test
    public void should_serialize_request() throws Exception {
        FSReadExpDate cmd = new FSReadExpDate();
        cmd.setSysPassword(30);

        byte[] data = cmd.encodeData();

        byte[] expectedData = byteArray(0xFF, 0x03, 0x1E, 0x00, 0x00, 0x00);

        assertArrayEquals(expectedData, data);
    }

    @Test
    public void should_deserialize_response() throws Exception {
        FSReadExpDate cmd = new FSReadExpDate();
        cmd.setSysPassword(30);

        byte[] responseData = byteArray(
                0xFF, 0x03, 0x00, 0x13, 0x09, 0x02, 0x1D, 0x01
        );

        cmd.decodeData(responseData);

        assertEquals(2, cmd.getDate().getDay());
        assertEquals(9, cmd.getDate().getMonth());
        assertEquals(2019, cmd.getDate().getYear());
        assertEquals(29, cmd.getRemainingRegistrationsCount());
        assertEquals(1, cmd.getRegistrationsCount());
    }
}

