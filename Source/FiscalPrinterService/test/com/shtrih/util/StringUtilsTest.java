/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.util;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import junit.framework.TestCase;

/**
 *
 * @author V.Kravtsov
 */
public class StringUtilsTest extends TestCase {
    
    public StringUtilsTest(String testName) {
        super(testName);
    }
    
    protected void setUp() throws Exception {
        super.setUp();
    }
    
    protected void tearDown() throws Exception {
        super.tearDown();
    }


    /**
     * Test of rtrim method, of class StringUtils.
     */
    public void testRtrim() {
        System.out.println("rtrim");
        String text = "  172  35712635     ";
        String expResult = "  172  35712635";
        String result = StringUtils.rtrim(text);
        assertEquals(expResult, result);
    }

}
