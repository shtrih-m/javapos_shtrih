package com.shtrih.fiscalprinter;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

/**
 * @author P.Zhirkov
 */
@RunWith(AndroidJUnit4.class)
public class TLVItemTests {

    @Test
    public void Should_decode_phrases() throws Exception {
        String s = "Мама, мыла; раму!_AaE)";

        //byte[] data = new byte[]{(byte)0x8C, (byte)0xA0, (byte)0xAC, (byte)0xA0, (byte)0x2C, (byte)0x20, (byte)0xAC, (byte)0xEB, (byte)0xAB, (byte)0xA0, (byte)0x3B, (byte)0x20, (byte)0xE0, (byte)0xA0, (byte)0xAC, (byte)0xE3, (byte)0x21, (byte)0x5F, (byte)0x41, (byte)0x61, (byte)0x45, (byte)0x29} ;

       // TLVItem item = new TLVItem(new TLVInfo(666, TLVInfo.TLVType.itASCII), data, 12);
        //assertEquals(s, item.getText());
    }
}
