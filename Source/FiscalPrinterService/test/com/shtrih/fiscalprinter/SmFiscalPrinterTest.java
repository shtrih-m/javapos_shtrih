/*
 * SmFiscalPrinterTest.java
 *
 * Created on 11 Март 2011 г., 11:54
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.shtrih.fiscalprinter;

import junit.framework.TestCase;

/**
 *
 * @author V.Kravtsov
 */

public class SmFiscalPrinterTest extends TestCase {
    /*
    MockPrinterDevice device;
    
    public SmFiscalPrinterTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
        device = new MockPrinterDevice();
    }

    protected void tearDown() throws Exception {
    }

    public void testSplitText() {
        System.out.println("testSplitText");
        
        SmFiscalPrinter printer = new SmFiscalPrinter(null, null);
        String text = "line1\r\nline2\rline3\nline4\r\rline6";
        String[] lines = printer.splitText(text, 10, true);
        
        assertEquals(6, lines.length);
        assertEquals("line1", lines[0]);
        assertEquals("line2", lines[1]);
        assertEquals("line3", lines[2]);
        assertEquals("line4", lines[3]);
        assertEquals("", lines[4]);
        assertEquals("line6", lines[5]);
    }

    public void testSplitText2() {
        System.out.println("testSplitText2");
        
        SmFiscalPrinter printer = new SmFiscalPrinter(null, null);
        String text = "line1";
        String[] lines = printer.splitText(text, 10, true);
        
        assertEquals(1, lines.length);
        assertEquals("line1", lines[0]);
    }
     * 
     */
}
