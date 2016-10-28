/*
 * BitUtilsTest.java
 * JUnit based test
 *
 * Created on 20 March 2008, 17:27
 */

package com.shtrih.jpos;

import com.shtrih.util.BitUtils;
import java.util.BitSet;
import junit.framework.TestCase;

/**
 *
 * @author V.Kravtsov
 */
public class BitUtilsTest extends TestCase {
    
    public BitUtilsTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }

    public void testSetBit() {
        assertEquals(BitUtils.setBit((int)0), 1);
        assertEquals(BitUtils.setBit((int)1), 2);
        assertEquals(BitUtils.setBit((int)2), 4);
        assertEquals(BitUtils.setBit((int)3), 8);
        assertEquals(BitUtils.setBit((int)4), 16);
        assertEquals(BitUtils.setBit((int)5), 32);
        assertEquals(BitUtils.setBit((int)6), 64);
        assertEquals(BitUtils.setBit((int)7), 128);
    }

    public void testTestBit() {
        assertEquals(BitUtils.testBit(0, 0), false);
        assertEquals(BitUtils.testBit(1, 0), true);
        assertEquals(BitUtils.testBit(2, 1), true);
        assertEquals(BitUtils.testBit(4, 2), true);
        assertEquals(BitUtils.testBit(8, 3), true);
        assertEquals(BitUtils.testBit(16, 4), true);
        assertEquals(BitUtils.testBit(32, 5), true);
        assertEquals(BitUtils.testBit(64, 6), true);
        assertEquals(BitUtils.testBit(128, 7), true);
    }

    public void testSwapByte() 
    {
        assertEquals(BitUtils.swapBits((byte)0), 0);
        assertEquals(BitUtils.swapBits((byte)1), 128);
        assertEquals(BitUtils.swapBits((byte)2), 64);
        assertEquals(BitUtils.swapBits((byte)4), 32);
    }
    
    /**
     * Test of fromByteArray method, of class com.shtrih.jpos.BitUtils.
     */
    public void testFromByteArray() {
        byte[] bytes = null;
        
        BitSet expResult = null;
        BitSet result = BitUtils.fromByteArray(bytes);
        assertEquals(expResult, result);
        
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of toByteArray method, of class com.shtrih.jpos.BitUtils.
     */
    public void testToByteArray() {
        BitSet bits = null;
        
        byte[] expResult = null;
        byte[] result = BitUtils.toByteArray(bits);
        assertEquals(expResult, result);
        
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
    
    public void testCode39() 
    {
    }
}
