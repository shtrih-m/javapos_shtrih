/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.fiscalprinter;

import org.junit.Test;
import static org.junit.Assert.*;
import com.shtrih.fiscalprinter.command.PrinterConst;

/**
 *
 * @author Виталий
 */
public class TLVFilterTest {
    
    public TLVFilterTest() {
    }

    /**
     * Test of filter method, of class TLVFilter.
     */
    @Test
    public void test1227_ffd105() throws Exception {
        System.out.println("filter");
        TLVItems src = new TLVItems();
        TLVItems dst = new TLVItems();
        int ffd = PrinterConst.FS_FORMAT_FFD_1_0_5;
        src.add(new TLVItem(1227, "БЕТА ООО"));
        src.add(new TLVItem(1228, "9705069985"));
        assertEquals(2, src.size());
        assertEquals(0, dst.size());
        
        TLVFilter.filter(src, dst, ffd);
        assertEquals(2, dst.size());
        assertEquals(1227, dst.get(0).getId());
        assertEquals(1228, dst.get(1).getId());
        assertEquals("БЕТА ООО", dst.get(0).getText());
        assertEquals("9705069985", dst.get(1).getText());
    }
    
    @Test
    public void test1227_ffd1_2() throws Exception {
        System.out.println("filter");
        TLVItems src = new TLVItems();
        TLVItems dst = new TLVItems();
        int ffd = PrinterConst.FS_FORMAT_FFD_1_2;
        src.add(new TLVItem(1227, "БЕТА ООО"));
        src.add(new TLVItem(1228, "9705069985"));
        assertEquals(2, src.size());
        assertEquals(0, dst.size());
        
        TLVFilter.filter(src, dst, ffd);
        assertEquals(1, dst.size());
        assertEquals(1256, dst.get(0).getId());
        TLVItems items = dst.get(0).getItems();
        assertEquals(2, items.size());
        assertEquals(1227, items.get(0).getId());
        assertEquals(1228, items.get(1).getId());
        assertEquals("БЕТА ООО", items.get(0).getText());
        assertEquals("9705069985", items.get(1).getText());
    }
    
}
