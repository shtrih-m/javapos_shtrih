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
public class EJDateTest extends TestCase {
    
    public EJDateTest(String testName) {
        super(testName);
    }
    
    protected void setUp() throws Exception {
        super.setUp();
    }
    
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of getDay method, of class EJDate.
     */
    public void testGetDay() {
        System.out.println("getDay");
        EJDate instance = new EJDate(1, 2, 3);
        int expResult = 1;
        int result = instance.getDay();
        assertEquals(expResult, result);
    }

    /**
     * Test of getMonth method, of class EJDate.
     */
    public void testGetMonth() {
        System.out.println("getMonth");
        EJDate instance = new EJDate(1, 2, 3);
        int expResult = 2;
        int result = instance.getMonth();
        assertEquals(expResult, result);
    }

    /**
     * Test of getYear method, of class EJDate.
     */
    public void testGetYear() {
        System.out.println("getYear");
        EJDate instance = new EJDate(1, 2, 3);
        int expResult = 3;
        int result = instance.getYear();
        assertEquals(expResult, result);
    }

    /**
     * Test of toString method, of class EJDate.
     */
    public void testToString() {
        System.out.println("toString");
        EJDate instance = new EJDate(1, 2, 3);
        String expResult = "01.02.2003";
        String result = instance.toString();
        assertEquals(expResult, result);
    }

    /**
     * Test of parse method, of class EJDate.
     */
    public void testParse() {
        System.out.println("parse");
        String text = "01.02.2003";
        String expResult = text;
        EJDate result = EJDate.parse(text);
        assertEquals(expResult, result.toString());
    }

    /**
     * Test of compare method, of class EJDate.
     */
    public void testCompare() {
        System.out.println("compare");
        EJDate date1 = new EJDate(1,2,3);
        EJDate date2 = new EJDate(1,2,3);
        boolean expResult = true;
        boolean result = EJDate.compare(date1, date2);
        assertEquals(expResult, result);
        
        date1 = new EJDate(1,2,3);
        date2 = new EJDate(2,2,3);
        expResult = false;
        result = EJDate.compare(date1, date2);
        assertEquals(expResult, result);
        
        date1 = new EJDate(1,2,3);
        date2 = new EJDate(1,3,3);
        expResult = false;
        result = EJDate.compare(date1, date2);
        assertEquals(expResult, result);
        
        date1 = new EJDate(1,2,3);
        date2 = new EJDate(1,2,4);
        expResult = false;
        result = EJDate.compare(date1, date2);
        assertEquals(expResult, result);
    }
}
