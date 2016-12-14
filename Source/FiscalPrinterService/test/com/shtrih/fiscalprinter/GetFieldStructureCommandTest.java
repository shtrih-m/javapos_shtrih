/*
 * GetFieldStructureCommandTest.java
 * JUnit based test
 *
 * Created on 13 Октябрь 2009 г., 11:47
 */

package com.shtrih.fiscalprinter;

import com.shtrih.util.Hex;
import junit.framework.TestCase;
import com.shtrih.fiscalprinter.command.FieldInfo;
import com.shtrih.fiscalprinter.command.ReadFieldInfo;
import com.shtrih.fiscalprinter.command.CommandInputStream;
import com.shtrih.fiscalprinter.command.CommandOutputStream;


/**
 *
 * @author V.Kravtsov
 */
public class GetFieldStructureCommandTest extends TestCase {
    
    public GetFieldStructureCommandTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }

    /**
     * Test of getCode method, of class com.shtrih.fiscalprinter.ReadFieldInfo.
     */
    public void testGetCode() {
        System.out.println("getCode");
        
        ReadFieldInfo instance = new ReadFieldInfo();
        int result = instance.getCode();
        assertEquals(0x2E, result);
    }

    /**
     * Test of getText method, of class com.shtrih.fiscalprinter.ReadFieldInfo.
     */
    public void testGetText() {
        System.out.println("getText");
        
        ReadFieldInfo instance = new ReadFieldInfo();
        String expResult = "Read field structure";
        String result = instance.getText();
        assertEquals(expResult, result);
    }

    /**
     * Test of encode method, of class com.shtrih.fiscalprinter.ReadFieldInfo.
     */
    public void testEncode() throws Exception {
        System.out.println("encode");
        
        CommandOutputStream out = new CommandOutputStream("cp866");
        ReadFieldInfo instance = new ReadFieldInfo();
        
        instance.setTable(0x34);
        instance.setField(0x12);
        instance.setPassword(0x12345678);
        instance.encode(out);
        
        byte[] result = out.getData();
        byte[] expResult = {0x78, 0x56, 0x34, 0x12, 0x34, 0x12};
        assertEquals(Hex.toHex(expResult), Hex.toHex(result));
    }

    /**
     * Test of decode method, of class com.shtrih.fiscalprinter.ReadFieldInfo.
     */
    public void testDecode() throws Exception {
        System.out.println("decode");
        
        CommandInputStream in = new CommandInputStream("cp866");
        CommandOutputStream out = new CommandOutputStream("cp866");
        ReadFieldInfo instance = new ReadFieldInfo();
        
        out.writeString("fieldName  ", 40);
        out.writeByte(0x00);
        out.writeByte(0x04);
        out.writeLong(0x12345678, 4);
        out.writeLong(0x23456789, 4);
        
        in.setData(out.getData());
        instance.decode(in);
        
        FieldInfo fieldInfo = instance.getFieldInfo();
        assertEquals("fieldName", fieldInfo.getName());
        assertEquals(0, fieldInfo.getType());
        assertEquals(4, fieldInfo.getSize());
        assertEquals(0x12345678, fieldInfo.getMin());
        assertEquals(0x23456789, fieldInfo.getMax());
    }
}
