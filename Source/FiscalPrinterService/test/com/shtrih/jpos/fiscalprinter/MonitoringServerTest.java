/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.shtrih.jpos.fiscalprinter;

import java.net.Socket;
import junit.framework.TestCase;

/**
 *
 * @author V.Kravtsov
 */
public class MonitoringServerTest extends TestCase {
    
    public MonitoringServerTest(String testName) {
        super(testName);
    }
    
    protected void setUp() throws Exception {
        super.setUp();
    }
    
    protected void tearDown() throws Exception {    
        super.tearDown();
    }

    /**
     * Test of start method, of class MonitoringServer.
     */
    public void testStart() {
        System.out.println("start");
        int port = 0;
        MonitoringServer instance = new MonitoringServer(null);
        instance.start(port);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}
