/*
 * JposFiscalPrinterTest.java
 * JUnit based test
 *
 * Created on 9 Сентябрь 2010 г., 19:40
 */

package com.shtrih.jpos.fiscalprinter;

import junit.framework.TestCase;

/**
 *
 * @author V.Kravtsov
 */
public class JposFiscalPrinterTest extends TestCase {
    public JposFiscalPrinterTest(String testName) {
        super(testName);
    }

    /**
     * Test of splitText method, of class com.shtrih.jpos.fiscalprinter.SmFiscalPrinter.
     */
    public void testSplitText() {
        System.out.println("splitText");
        
    /*
        String text;
        String[] lines;
        
        text = "";
        lines = SmFiscalPrinter.splitText(text, 40, true);
        assertEquals(1, lines.length);
        assertEquals("", lines[0]);
        
        text = "Line1";
        lines = SmFiscalPrinter.splitText(text, 40, true);
        assertEquals(1, lines.length);
        assertEquals("Line1", lines[0]);
        
        text = "Line1";
        lines = SmFiscalPrinter.splitText(text, 5, true);
        assertEquals(1, lines.length);
        assertEquals("Line1", lines[0]);
        
        text = "\r";
        lines = SmFiscalPrinter.splitText(text, 40, true);
        assertEquals(0, lines.length);
        
        text = "\n";
        lines = SmFiscalPrinter.splitText(text, 40, true);
        assertEquals(1, lines.length);
        assertEquals("", lines[0]);
        
        text = "\r\n";
        lines = SmFiscalPrinter.splitText(text, 40, true);
        assertEquals(1, lines.length);
        assertEquals("", lines[0]);
        
        text = "\n\r";
        lines = SmFiscalPrinter.splitText(text, 40, true);
        assertEquals(1, lines.length);
        assertEquals("", lines[0]);
        
        text = "Line1\r\nLine2";
        lines = SmFiscalPrinter.splitText(text, 40, true);
        assertEquals(2, lines.length);
        assertEquals("Line1", lines[0]);
        assertEquals("Line2", lines[1]);
        
        text = "Line1\r\n";
        lines = SmFiscalPrinter.splitText(text, 40, true);
        assertEquals(1, lines.length);
        assertEquals("Line1", lines[0]);
        
        text = "Line1\n";
        lines = SmFiscalPrinter.splitText(text, 40, true);
        assertEquals(1, lines.length);
        assertEquals("Line1", lines[0]);
        
        text = "Line1\r";
        lines = SmFiscalPrinter.splitText(text, 40, true);
        assertEquals(1, lines.length);
        assertEquals("Line1", lines[0]);
        
        text = "Line1\r\n";
        lines = SmFiscalPrinter.splitText(text, 2, true);
        assertEquals(3, lines.length);
        assertEquals("Li", lines[0]);
        assertEquals("ne", lines[1]);
        assertEquals("1", lines[2]);
         * 
         */
    }

    /**
     * Test of splitTextOld method, of class com.shtrih.jpos.fiscalprinter.SmFiscalPrinter.
     */
    public void testSplitTextOld() {
        System.out.println("splitText");
        
    /*
        String text;
        String[] lines;
        
        text = "Line1";
        lines = SmFiscalPrinter.splitText(text, 40, true);
        assertEquals(1, lines.length);
        assertEquals("Line1", lines[0]);
        
        text = "Line1";
        lines = SmFiscalPrinter.splitText(text, 5, true);
        assertEquals(1, lines.length);
        assertEquals("Line1", lines[0]);
     * 
     */
    }
    
}
