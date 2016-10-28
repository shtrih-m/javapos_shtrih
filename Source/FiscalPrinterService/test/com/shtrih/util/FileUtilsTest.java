/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.shtrih.util;

import java.io.File;
import junit.framework.TestCase;

/**
 *
 * @author V.Kravtsov
 */
public class FileUtilsTest extends TestCase {
    
    public FileUtilsTest(String testName) {
        super(testName);
    }
    
    /**
     * Test of getExtention method, of class FileUtils.
     */
    public void testGetExtention() {
        System.out.println("getExtention");
        String result;

        String fileName = "C:\\test1.2\\file1.xml";
        int nameIndex = fileName.lastIndexOf(".");
        assertEquals(16, nameIndex);

        int pathIndex = fileName.lastIndexOf(File.separator);
        assertEquals(10, pathIndex);
        
        
        result = FileUtils.getExtention("C:\\test1\\file1.xml");
        assertEquals(".xml", result);
        result = FileUtils.getExtention("C:\\test1.1\\file1");
        assertEquals("", result);
    }

    /**
     * Test of removeExtention method, of class FileUtils.
     */
    public void testRemoveExtention() {
        System.out.println("removeExtention");
        String result;
        result = FileUtils.removeExtention("C:\\test1\\file1.xml");
        assertEquals("C:\\test1\\file1", result);
        result = FileUtils.removeExtention("C:\\test1.2\\file1");
        assertEquals("C:\\test1.2\\file1", result);
    }

}
