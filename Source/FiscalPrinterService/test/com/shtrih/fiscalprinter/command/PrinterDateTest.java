/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.fiscalprinter.command;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author V.Kravtsov
 */
public class PrinterDateTest {
 
    @Test
    public void compare_equals() throws Exception {
        PrinterDate date1 = new PrinterDate(31, 05, 23);
        PrinterDate date2 = new PrinterDate(31, 05, 23);
        assertEquals(0, date1.compare(date2));
    }

    @Test
    public void compare_greater() throws Exception {
        PrinterDate date1 = new PrinterDate(31, 05, 24);
        PrinterDate date2 = new PrinterDate(31, 05, 23);
        assertEquals(1, date1.compare(date2));
        
        date1 = new PrinterDate(31, 06, 23);
        date2 = new PrinterDate(31, 05, 23);
        assertEquals(1, date1.compare(date2));
        
        date1 = new PrinterDate(31, 05, 23);
        date2 = new PrinterDate(30, 05, 23);
        assertEquals(1, date1.compare(date2));
    }
    
    @Test
    public void compare_less() throws Exception {
        PrinterDate date1 = new PrinterDate(31, 05, 23);
        PrinterDate date2 = new PrinterDate(31, 05, 24);
        assertEquals(-1, date1.compare(date2));
        
        date1 = new PrinterDate(31, 05, 23);
        date2 = new PrinterDate(31, 06, 23);
        assertEquals(-1, date1.compare(date2));
        
        date1 = new PrinterDate(30, 05, 23);
        date2 = new PrinterDate(31, 05, 23);
        assertEquals(-1, date1.compare(date2));
    }
    
}
