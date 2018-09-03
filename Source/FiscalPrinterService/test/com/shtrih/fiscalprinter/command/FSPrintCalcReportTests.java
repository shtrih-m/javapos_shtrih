package com.shtrih.fiscalprinter.command;

import org.junit.Test;

import static com.shtrih.util.ByteUtils.byteArray;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * @author P.Zhirkov
 */
public class FSPrintCalcReportTests {

    @Test
    public void should_serialize_request() throws Exception {
        FSPrintCalcReport cmd = new FSPrintCalcReport(30);

        byte[] data = cmd.encodeData();

        byte[] expectedData = byteArray(
                0xFF, 0x38,
                0x1E, 0x00, 0x00, 0x00);

        assertArrayEquals(expectedData, data);
    }

    @Test
    public void should_deserialize_response() throws Exception {
        FSPrintCalcReport cmd = new FSPrintCalcReport(30);

        byte[] responseData = byteArray(
            0xFF, 0x38, 0x00, 0x0D, 0x00, 0x00, 0x00, 0x3B, 0x92, 0x3F, 0x07, 0x02, 0x00, 0x00, 0x00, 0x12, 0x01, 0x19
        );

        cmd.decodeData(responseData);

        assertEquals(cmd.getDocumentNumber(),13);
        assertEquals(cmd.getDocumentDigest(),121606715);
        assertEquals(cmd.getDocumentCount(),2);
        assertEquals(cmd.getDocumentDate().getDay(),25);
        assertEquals(cmd.getDocumentDate().getMonth(),1);
        assertEquals(cmd.getDocumentDate().getYear(), 2018);
    }
}
