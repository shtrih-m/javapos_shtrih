package com.shtrih.util;

import com.shtrih.util.encoding.IBM866;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import static org.junit.Assert.assertEquals;

/**
 * Created by P.Zhirkov on 02.02.2017.
 */
public class Cp866EncodingTests {
    @Test
    public void Should_encode_every_char() throws UnsupportedEncodingException {
        String str = "";
        for (int i = 0; i < 256; i++) {
            byte[] c = new byte[]{(byte) i};

            String s = new String(c, Charset.forName("cp866"));
            byte[] cp866Bytes = s.getBytes(new IBM866());
            ;
            assertEquals(1, cp866Bytes.length);
            assertEquals(c[0], cp866Bytes[0]);

            str += s;
        }

        // System.out.println(Hex.toHex(str.getBytes("cp866")));
    }
    
    @Test
    public void Should_decode_every_char() throws UnsupportedEncodingException {
        for (int i = 0; i < 256; i++) {
            byte[] c = new byte[]{(byte) i};

            String s = new String(c, Charset.forName("cp866"));
            assertEquals(s, new String(c, new IBM866()));
        }
    }

    @Test
    public void Should_encode_phrases() throws UnsupportedEncodingException {
        String s = "Мама, мыла; раму!_AaE)";
        byte[] cp866Bytes = s.getBytes(new IBM866());

        for (int i = 0; i < cp866Bytes.length; i++) {
            assertEquals(s.getBytes("cp866")[i], cp866Bytes[i]);
        }

        // System.out.println(Hex.toHex(s.getBytes("cp866")));
    }

    @Test
    public void Should_encode_phrases2() throws UnsupportedEncodingException {
        String s = "Мама, мыла; раму!_AaE)";
        byte[] cp866Bytes = s.getBytes(new IBM866());

        int[] expected = new int[]{0x8C, 0xA0, 0xAC, 0xA0, 0x2C, 0x20, 0xAC, 0xEB, 0xAB, 0xA0, 0x3B, 0x20, 0xE0, 0xA0, 0xAC, 0xE3, 0x21, 0x5F, 0x41, 0x61, 0x45, 0x29};

        for (int i = 0; i < cp866Bytes.length; i++) {
            assertEquals((byte) expected[i], cp866Bytes[i]);
        }
    }

    @Test
    public void Should_decode_phrases() throws UnsupportedEncodingException {
        String s = "Мама, мыла; раму!_AaE)";
        byte[] cp866Bytes = s.getBytes(new IBM866());

        int[] expected = new int[]{0x8C, 0xA0, 0xAC, 0xA0, 0x2C, 0x20, 0xAC, 0xEB, 0xAB, 0xA0, 0x3B, 0x20, 0xE0, 0xA0, 0xAC, 0xE3, 0x21, 0x5F, 0x41, 0x61, 0x45, 0x29};

        byte[] expectedBytes = new byte[expected.length];
        for (int i = 0; i < expected.length; i++) {
            expectedBytes[i] = (byte) expected[i];
        }

        assertEquals(s, new String(expectedBytes, new IBM866()));
    }
}
