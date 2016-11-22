/*
 * BitUtilsTest.java
 * JUnit based test
 *
 * Created on 20 March 2008, 17:27
 */

package com.shtrih.jpos;

import com.shtrih.util.Hex;
import junit.framework.TestCase;

/**
 *
 * @author V.Kravtsov
 */
public class EncodingTest extends TestCase {
    
    public EncodingTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }

   public void testEncode() 
        throws Exception
    {
       byte[] data;
       String src = "Ш";
       String tmp;
       String dst;
       String charsetName = "Cp866";
       //
       System.out.println(src);
       Hex.printHex(src.getBytes());
       
       data = src.getBytes(charsetName);
       Hex.printHex(data);
       tmp = new String(data, charsetName);
       Hex.printHex(tmp.getBytes());
       // 
       data = tmp.getBytes(charsetName);
       Hex.printHex(data);
       
       dst = new String(data, charsetName);
       System.out.println(dst);
    }
}
