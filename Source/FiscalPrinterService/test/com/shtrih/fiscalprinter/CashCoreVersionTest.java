/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.fiscalprinter;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author V.Kravtsov
 */
public class CashCoreVersionTest {
 
    @Test
    public void compare_equals() throws Exception {
        CashCoreVersion version1 = new CashCoreVersion("1.16.43098");
        CashCoreVersion version2 = new CashCoreVersion("1.16.43098");
        assertEquals(0, version1.compare(version2));
        assertEquals(0, version2.compare(version1));
    }
    
    @Test
    public void compare_greater() throws Exception {
        CashCoreVersion version1 = new CashCoreVersion("2.16.43098");
        CashCoreVersion version2 = new CashCoreVersion("1.16.43098");
        assertEquals(1, version1.compare(version2));
        assertEquals(-1, version2.compare(version1));
        
        version1 = new CashCoreVersion("1.17.43098");
        version2 = new CashCoreVersion("1.16.43098");
        assertEquals(1, version1.compare(version2));
        assertEquals(-1, version2.compare(version1));
        
        version1 = new CashCoreVersion("1.16.43099");
        version2 = new CashCoreVersion("1.16.43098");
        assertEquals(1, version1.compare(version2));
        assertEquals(-1, version2.compare(version1));
    }
    
}
