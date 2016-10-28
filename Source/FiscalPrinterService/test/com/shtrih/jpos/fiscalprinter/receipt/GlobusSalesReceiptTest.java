/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.jpos.fiscalprinter.receipt;

import junit.framework.TestCase;

/**
 *
 * @author V.Kravtsov
 */
public class GlobusSalesReceiptTest extends TestCase {
    
    public GlobusSalesReceiptTest(String testName) {
        super(testName);
    }
    
    protected void setUp() throws Exception {
        super.setUp();
    }
    
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of alignLines method, of class GlobusSalesReceipt.
     */
    public void testAlignLines() {
        System.out.println("alignLines");
        String line1 = "test1";
        String line2 = "test2";
        int len = 20;
        String expResult = "test1          test2";
        String result = GlobusSalesReceipt.alignLines(line1, line2, len);
        assertEquals(expResult, result);
    }
    
   
}
