package com.shtrih.fiscalprinter;

import android.support.test.runner.AndroidJUnit4;

import com.shtrih.fiscalprinter.command.TLVList;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertArrayEquals;

/**
 * @author P.Zhirkov
 */
@RunWith(AndroidJUnit4.class)
public class TLVListTests {
    @Test
    public void Should_encode_phrases() throws Exception {
        String s = "Мама, мыла; раму!_AaE)";
        TLVList  list = new TLVList();
        list.add(666, s);
        byte[] expectedData = new byte[]{ (byte)0x9A, (byte)0x02, (byte)0x16, (byte)0x00, (byte)0x8C, (byte)0xA0, (byte)0xAC, (byte)0xA0, (byte)0x2C, (byte)0x20, (byte)0xAC, (byte)0xEB, (byte)0xAB, (byte)0xA0, (byte)0x3B, (byte)0x20, (byte)0xE0, (byte)0xA0, (byte)0xAC, (byte)0xE3, (byte)0x21, (byte)0x5F, (byte)0x41, (byte)0x61, (byte)0x45, (byte)0x29} ;//list.getData();

        assertArrayEquals(expectedData, list.getData());
    }
}
