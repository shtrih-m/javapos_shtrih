/*
 * JposDeviceStatisticsTest.java
 * JUnit based test
 *
 * Created on 20 May 2008, 21:26
 */

package com.shtrih.jpos;

import junit.framework.TestCase;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 *
 * @author V.Kravtsov
 */
public class JposDeviceStatisticsTest extends TestCase {
    
    public JposDeviceStatisticsTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }

    /**
     * Test of reset method, of class com.shtrih.jpos.JposDeviceStatistics.
     */
    public void testReset() throws Exception {
        System.out.println("reset");
        
        String statisticsBuffer = "";
        JposDeviceStatistics instance = new JposDeviceStatistics();
        
        instance.reset(statisticsBuffer);
        
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of update method, of class com.shtrih.jpos.JposDeviceStatistics.
     */
    public void testUpdate() throws Exception {
        System.out.println("update");
        
        String statisticsBuffer = "";
        JposDeviceStatistics instance = new JposDeviceStatistics();
        
        instance.update(statisticsBuffer);
        
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of retrieve method, of class com.shtrih.jpos.JposDeviceStatistics.
     */
    public void testRetrieve() throws Exception {
        System.out.println("retrieve");
        
        String[] statisticsBuffer = null;
        JposDeviceStatistics instance = new JposDeviceStatistics();
        
        instance.retrieve(statisticsBuffer);
        
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setParameter method, of class com.shtrih.jpos.JposDeviceStatistics.
     */
    public void testSetParameter() {
        System.out.println("setParameter");
        
        Node root = null;
        Document xmldoc = null;
        String parameterName = "";
        String parameterValue = "";
        JposDeviceStatistics instance = new JposDeviceStatistics();
        
        instance.setParameter(root, xmldoc, parameterName, parameterValue);
        
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setAttribute method, of class com.shtrih.jpos.JposDeviceStatistics.
     */
    public void testSetAttribute() {
        System.out.println("setAttribute");
        
        Node root = null;
        Document xmldoc = null;
        String nodeName = "";
        String attributeName = "";
        String attributeValue = "";
        JposDeviceStatistics instance = new JposDeviceStatistics();
        
        instance.setAttribute(root, xmldoc, nodeName, attributeName, attributeValue);
        
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of changeStatistic method, of class com.shtrih.jpos.JposDeviceStatistics.
     */
    public void testChangeStatistic() {
        System.out.println("changeStatistic");
        
        String name = "";
        long value = 0L;
        JposDeviceStatistics instance = new JposDeviceStatistics();
        
        //instance.changeStatistic(name, value);
        
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of defineStatistic method, of class com.shtrih.jpos.JposDeviceStatistics.
     */
    public void testDefineStatistic() {
        System.out.println("defineStatistic");
        
        String statisticName = "";
        JposDeviceStatistics instance = new JposDeviceStatistics();
        
        JposDeviceStatistics.StatisticItem expResult = null;
        JposDeviceStatistics.StatisticItem result = instance.defineStatistic(statisticName);
        assertEquals(expResult, result);
        
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
