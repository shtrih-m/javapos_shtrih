package com.shtrih.util;

import android.support.test.runner.AndroidJUnit4;

import com.shtrih.util.encoding.IBM866;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.UnsupportedEncodingException;

import static org.junit.Assert.assertEquals;

/**
 * @author P.Zhirkov
 */
@RunWith(AndroidJUnit4.class)
public class Cp866EncodingTests {

    @Test
    public void Should_encode_phrases() throws UnsupportedEncodingException {
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

        int[] expected = new int[]{0x8C, 0xA0, 0xAC, 0xA0, 0x2C, 0x20, 0xAC, 0xEB, 0xAB, 0xA0, 0x3B, 0x20, 0xE0, 0xA0, 0xAC, 0xE3, 0x21, 0x5F, 0x41, 0x61, 0x45, 0x29};

        byte[] expectedBytes = new byte[expected.length];
        for (int i = 0; i < expected.length; i++) {
            expectedBytes[i] = (byte) expected[i];
        }

        assertEquals(s, new String(expectedBytes, new IBM866()));
    }
}
