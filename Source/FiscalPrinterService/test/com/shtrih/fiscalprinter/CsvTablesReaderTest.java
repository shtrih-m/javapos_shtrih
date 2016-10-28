/*
 * CsvTablesReaderTest.java
 * JUnit based test
 *
 * Created on November 19, 2009, 12:31 PM
 */

package com.shtrih.fiscalprinter;

import junit.framework.TestCase;
import com.shtrih.fiscalprinter.table.CsvTablesReader;

/**
 *
 * @author V.Kravtsov
 */
public class CsvTablesReaderTest extends TestCase {
    
    public CsvTablesReaderTest(String testName) {
        super(testName);
    }

    /**
     * Test of isComment method, of class com.shtrih.fiscalprinter.CsvTablesReader.
     */
    public void testIsComment() {
        System.out.println("isComment");
        CsvTablesReader instance = new CsvTablesReader();
        assertEquals(true, instance.isComment("// comment line"));
        assertEquals(false, instance.isComment("not comment line"));
    }

    /**
     * Test of getParamStr method, of class com.shtrih.fiscalprinter.CsvTablesReader.
     */
    public void testGetParamStr() {
        System.out.println("getParamStr");
        
        CsvTablesReader instance = new CsvTablesReader();
        String line = "1,2,STORE NUMBER,\"3\"";
        
        assertEquals("1", instance.getParamStr(line, 0));
        assertEquals("2", instance.getParamStr(line, 1));
        assertEquals("STORE NUMBER", instance.getParamStr(line, 2));
        assertEquals("3", instance.getParamStr(line, 3));
        assertEquals("", instance.getParamStr(line, 4));
    }

    /**
     * Test of getParamInt method, of class com.shtrih.fiscalprinter.CsvTablesReader.
     */
    public void testGetParamInt() {
        System.out.println("getParamInt");
        
        String line = "asd,sdf,dfg,123,sdf";
        int index = 3;
        CsvTablesReader instance = new CsvTablesReader();
        
        int expResult = 123;
        int result = instance.getParamInt(line, index);
        assertEquals(expResult, result);
    }

    /**
     * Test of load method, of class com.shtrih.fiscalprinter.CsvTablesReader.
     */
    public void testLoad() throws Exception {
        System.out.println("load");
 
        /*
        String fileName = "fields.csv";
        
        PrinterTables tables = new PrinterTables();
        tables.add(new PrinterTable(1, "table 1", 2, 3));
        tables.add(new PrinterTable(2, "table 2", 3, 4));
        
        CsvTablesWriter writer = new CsvTablesWriter();
        writer.save(fileName, tables);
        
        tables.clear();
        CsvTablesReader instance = new CsvTablesReader();
        instance.load(fileName, fields);
         * 
         */
    }
    
}
