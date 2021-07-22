/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.fiscalprinter;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Виталий
 */
public class Tag2115Test {
    
    public Tag2115Test() {
    }

    /**
     * Test of getValue method, of class Tag2115.
     */
    @Test
    public void testGetValue() throws Exception {
        System.out.println("getValue");
        
        String mc = "29293847568";
        String expResult = "0976";
        String result = Tag2115.getValue(mc);
        assertEquals(expResult, result);
        
        mc = "2938479283749287skdjfhksjdfh";
        expResult = "1325";
        result = Tag2115.getValue(mc);
        assertEquals(expResult, result);
    }
    
}
