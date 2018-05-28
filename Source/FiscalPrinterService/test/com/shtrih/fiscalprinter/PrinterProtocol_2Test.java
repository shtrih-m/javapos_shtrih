package com.shtrih.fiscalprinter;

import junit.framework.TestCase;

public class PrinterProtocol_2Test extends TestCase {

    public void testCRC() {
        PrinterProtocol_2.Frame frame = new PrinterProtocol_2.Frame();
        byte[] data = {0x00, 0x00};
        int crc = 0x1D0F;
        int crc2 = frame.getCRC(data);
        assertEquals(crc, crc2);
        
        byte[] data2 = {0x03, 0x00, 0x06, 0x00, (byte)0xFC};
        crc = 0x63ED;
        crc2 = frame.getCRC(data2);
        assertEquals(crc, crc2);
    }
    
}
