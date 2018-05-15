package com.shtrih.fiscalprinter;

import org.junit.Test;

import static com.shtrih.util.ByteUtils.byteArray;
import static org.junit.Assert.assertArrayEquals;

public class GCNGeneratorTests {

    @Test
    public void should_generate_02()  {

        byte[] data = GCNGenerator.generate02(98765432101234L,"RU-430302-ABC1234567");

        byte[] expectedData = byteArray(
                0x00, 0x02,
                0x59, 0xD3, 0x9E, 0x7F, 0x19, 0x72,
                0x52, 0x55, 0x2D, 0x34, 0x33, 0x30, 0x33, 0x30, 0x32, 0x2D, 0x41, 0x42, 0x43, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37);

        assertArrayEquals(expectedData, data);
    }

    @Test
    public void should_generate_03()  {

        byte[] data = GCNGenerator.generate03(98765432101234L, "ABC1234567890");

        byte[] expectedData = byteArray(
                0x00, 0x03,
                0x41, 0x42, 0x43, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39, 0x30,
                0x59, 0xD3, 0x9E, 0x7F, 0x19, 0x72);

        assertArrayEquals(expectedData, data);

    }
}