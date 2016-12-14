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
import org.junit.Ignore;
import org.junit.Test;

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
}
