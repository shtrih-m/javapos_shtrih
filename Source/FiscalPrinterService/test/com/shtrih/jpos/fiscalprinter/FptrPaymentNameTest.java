/*
 * FptrPaymentNameTest.java
 * JUnit based test
 *
 * Created on 25 ќкт€брь 2009 г., 19:31
 */

package com.shtrih.jpos.fiscalprinter;

import junit.framework.TestCase;

/**
 *
 * @author V.Kravtsov
 */
public class FptrPaymentNameTest extends TestCase {
    
    public FptrPaymentNameTest(String testName) {
        super(testName);
    }

    public void testFptrPaymentName() 
    {
        System.out.println("FptrPaymentName");
        FptrPaymentName instance = new FptrPaymentName(123, "test");
        assertEquals(123, instance.getCode());
        assertEquals("test", instance.getName());
    }
}
