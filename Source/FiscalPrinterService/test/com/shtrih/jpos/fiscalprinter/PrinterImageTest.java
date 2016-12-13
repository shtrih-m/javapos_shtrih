/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.shtrih.jpos.fiscalprinter;

import jpos.config.JposEntry;
import junit.framework.TestCase;
import jpos.config.simple.SimpleEntry;

/**
 *
 * @author V.Kravtsov
 */
public class PrinterImageTest extends TestCase {
    
    private final PrinterImage instance = new PrinterImage();
    
    public PrinterImageTest(String testName) {
        super(testName);
    }
    
    protected void setUp() throws Exception {
        super.setUp();
    }
    
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of getFileName method, of class PrinterImage.
     */
    public void testFileName() {
        System.out.println("getFileName");
        assertEquals("", instance.getFileName());
        instance.setFileName("12345");
        assertEquals("12345", instance.getFileName());
    }

    /**
     * Test of getEndPos method, of class PrinterImage.
     */
    public void testGetEndPos() {
        System.out.println("getEndPos");
        assertEquals(-1, instance.getEndPos());
        instance.setStartPos(1);
        instance.setHeight(2);
        assertEquals(2, instance.getEndPos());
    }

    /**
     * Test of getHeight method, of class PrinterImage.
     */
    public void testGetHeight() {
        System.out.println("getHeight");
        assertEquals(0, instance.getHeight());
        instance.setHeight(123);
        assertEquals(123, instance.getHeight());
    }

    /**
     * Test of getStartPos method, of class PrinterImage.
     */
    public void testGetStartPos() {
        System.out.println("getStartPos");
        assertEquals(0, instance.getStartPos());
        instance.setStartPos(123);
        assertEquals(123, instance.getStartPos());
    }

    /**
     * Test of getIsLoaded method, of class PrinterImage.
     */
    public void testGetIsLoaded() 
    {
        System.out.println("getIsLoaded");
        PrinterImage instance = new PrinterImage();
        assertEquals(false, instance.getIsLoaded());
    }

    /**
     * Test of getWidth method, of class PrinterImage.
     */
    public void testGetWidth() {
        System.out.println("getWidth");
        assertEquals(0, instance.getWidth());
    }

    /**
     * Test of setIsLoaded method, of class PrinterImage.
     */
    public void testSetIsLoaded() {
        System.out.println("setIsLoaded");
        assertEquals(false, instance.getIsLoaded());
        instance.setIsLoaded(true);        
        assertEquals(true, instance.getIsLoaded());
    }

    /**
     * Test of setHeight method, of class PrinterImage.
     */
    public void testSetHeight() {
        System.out.println("setHeight");
        assertEquals(0, instance.getHeight());
        instance.setHeight(123);
        assertEquals(123, instance.getHeight());
    }

    /**
     * Test of load method, of class PrinterImage.
     */
    public void testLoad() throws Exception {
        System.out.println("load");
        JposEntry jposEntry = new SimpleEntry();
        jposEntry.addProperty("0FileName", "Logo.bmp");
        jposEntry.addProperty("0Height", new Integer(0));
        jposEntry.addProperty("0FirstLine", new Integer(0));
        jposEntry.addProperty("0IsLoaded", new Boolean(false));
        
//        PrinterImage result = PrinterImage.load("0", jposEntry);
//        assertEquals(0, result.getHeight());
//        assertEquals(0, result.getWidth());
//        assertEquals(true, result.getIsLoaded());
    }
}
