/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.ej;

import junit.framework.TestCase;

/**
 *
 * @author V.Kravtsov
 */
public class EJFlagsTest extends TestCase {
    
    public EJFlagsTest(String testName) {
        super(testName);
    }
    
    protected void setUp() throws Exception {
        super.setUp();
    }
    
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of getFlags method, of class EJFlags.
     */
    public void testGetFlags() {
        System.out.println("getFlags");
        EJFlags instance = new EJFlags(123);
        int expResult = 123;
        int result = instance.getFlags();
        assertEquals(expResult, result);
    }

    /**
     * Test of getDocType method, of class EJFlags.
     */
    public void testGetDocType() {
        System.out.println("getDocType");
        EJFlags instance = new EJFlags(0x63);
        int expResult = 3;
        int result = instance.getDocType();
        assertEquals(expResult, result);
    }

    /**
     * Test of isArchiveOpened method, of class EJFlags.
     */
    public void testIsArchiveOpened() {
        System.out.println("isArchiveOpened");
        EJFlags instance = new EJFlags(0x04);
        boolean expResult = true;
        boolean result = instance.isArchiveOpened();
        assertEquals(expResult, result);
        
        instance = new EJFlags(0xFB);
        expResult = false;
        result = instance.isArchiveOpened();
        assertEquals(expResult, result);
    }

    /**
     * Test of isActivated method, of class EJFlags.
     */
    public void testIsActivated() {
        System.out.println("isActivated");
        EJFlags instance = new EJFlags(8);
        boolean expResult = true;
        boolean result = instance.isActivated();
        assertEquals(expResult, result);
        
        instance = new EJFlags(0xF7);
        expResult = false;
        result = instance.isActivated();
        assertEquals(expResult, result);
    }

    /**
     * Test of isReportMode method, of class EJFlags.
     */
    public void testIsReportMode() {
        System.out.println("isReportMode");
        EJFlags instance = new EJFlags(0x10);
        boolean expResult = true;
        boolean result = instance.isReportMode();
        assertEquals(expResult, result);
        
        instance = new EJFlags(0xEF);
        expResult = false;
        result = instance.isReportMode();
        assertEquals(expResult, result);
    }

    /**
     * Test of isDocOpened method, of class EJFlags.
     */
    public void testIsDocOpened() {
        System.out.println("isDocOpened");
        EJFlags instance = new EJFlags(0x20);
        boolean expResult = true;
        boolean result = instance.isDocOpened();
        assertEquals(expResult, result);
        
        instance = new EJFlags(0xFF-0x20);
        expResult = false;
        result = instance.isDocOpened();
        assertEquals(expResult, result);
    }

    /**
     * Test of isDayOpened method, of class EJFlags.
     */
    public void testIsDayOpened() {
        System.out.println("isDayOpened");
        EJFlags instance = new EJFlags(0x40);
        boolean expResult = true;
        boolean result = instance.isDayOpened();
        assertEquals(expResult, result);
        
        instance = new EJFlags(0xFF-0x40);
        expResult = false;
        result = instance.isDayOpened();
        assertEquals(expResult, result);
    }

    /**
     * Test of isFatalError method, of class EJFlags.
     */
    public void testIsFatalError() {
        System.out.println("isFatalError");
        EJFlags instance = new EJFlags(0x80);
        boolean expResult = true;
        boolean result = instance.isFatalError();
        assertEquals(expResult, result);
        
        instance = new EJFlags(0xFF-0x80);
        expResult = false;
        result = instance.isFatalError();
        assertEquals(expResult, result);
    }
}
