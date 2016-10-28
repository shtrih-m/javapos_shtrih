/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.shtrih.fiscalprinter;

import com.shtrih.fiscalprinter.model.PrinterModels;
import junit.framework.Assert;
import junit.framework.TestCase;

/**
 *
 * @author V.Kravtsov
 */
   
public class PrinterProtocol_2Test extends TestCase {

    public PrinterProtocol_2Test(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testCRC() {
        PrinterProtocol_2 instance = new PrinterProtocol_2();
        byte[] data = {0x00, 0x00};
        int crc = 0x1D0F;
        int crc2 = instance.getFrame().getCRC(data);
        assertEquals(crc, crc2);
        
        byte[] data2 = {0x03, 0x00, 0x06, 0x00, (byte)0xFC};
        crc = 0x63ED;
        crc2 = instance.getFrame().getCRC(data2);
        assertEquals(crc, crc2);
    }
    
}
