/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.fiscalprinter.table;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Виталий
 */
public class CsvTablesReaderTest {
    
    public CsvTablesReaderTest() {
    }

    /**
     * Test of getModelName method, of class CsvTablesReader.
     */
    @Test
    public void testGetModelName() {
        System.out.println("getModelName");
        
        String line = "/// Модель: ЯРУС-01К; №00001000";
        String expResult = "ЯРУС-01К; №00001000";
        String result = CsvTablesReader.getModelName(line);
        assertEquals(expResult, result);
        
        line = "/// model: ЯРУС-01К; №00001000";
        expResult = "ЯРУС-01К; №00001000";
        result = CsvTablesReader.getModelName(line);
        assertEquals(expResult, result);
        
        line = "/// model: ЯРУС-01К; RETAIL-01K; №00001000";
        expResult = "ЯРУС-01К; RETAIL-01K; №00001000";
        result = CsvTablesReader.getModelName(line);
        assertEquals(expResult, result);
    }
    
}
