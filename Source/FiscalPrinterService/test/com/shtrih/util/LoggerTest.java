/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import junit.framework.TestCase;

/**
 *
 * @author V.Kravtsov
 */
public class LoggerTest extends TestCase {

    public LoggerTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of debug method, of class CompositeLogger.
     */
    public void testSpeed() {
        CompositeLogger logger = CompositeLogger.getLogger(LoggerTest.class);
        logger.setEnabled(true);

        long time = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            logger.debug("Test logger");
        }
        time = System.currentTimeMillis() - time;
        System.out.println("Execution time: " + time);
        assertTrue("Execution time > 250ms", time <= 250);
    }

    public void testSave() {
        try {
            CompositeLogger logger = CompositeLogger.getLogger(LoggerTest.class);
            logger.setEnabled(true);
            logger.deleteFile();

            logger.debug("Debug line");
            logger.debug("Error line");
            logger.closeFile();

            FileInputStream stream = new FileInputStream(logger.getFileName());
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            assertEquals("Debug line", reader.readLine().substring(59));
            assertEquals("Error line", reader.readLine().substring(59));
        } catch (Exception e) {
            fail(e.getMessage());
        }

    }
}
