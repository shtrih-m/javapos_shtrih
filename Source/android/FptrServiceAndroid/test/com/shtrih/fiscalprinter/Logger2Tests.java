package com.shtrih.fiscalprinter;

import com.shtrih.util.Hex;
import com.shtrih.util.Logger2;

import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class Logger2Tests {
    private static final byte INPUT = (byte) 254;
    private static final int LOOP = 10000000;
    private static final String OUTPUT = "FE";

    @Ignore
    @Test
    public void commons_StringUtils() {
        for (int i = 0; i < 256; i++) {
            System.out.println("case (byte)" + (i) + ": return  \" " + String.format("%02X", (byte)i) + "\";");
        }
    }

    @Ignore
    @Test
    public void String_format() {
        
        assertEquals(OUTPUT, String.format("%02X", INPUT));

        for (int i = 0; i < LOOP; i++) {
            String.format("%02X", INPUT);
        }
    }

    @Test
    public void String_format2() {

        assertEquals(OUTPUT, Hex.toHex(INPUT));

        for (int i = 0; i < LOOP; i++) {
            Hex.toHex(INPUT);
        }
    }
}
