
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.RollingFileAppender;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import junit.framework.TestCase;
import org.apache.log4j.Appender;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author V.Kravtsov
 */
    
public class FileAppenderTest extends TestCase {

    public FileAppenderTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of debug method, of class Logger.
     */
    public void testSpeed() 
    {
        RollingFileAppender appender = new RollingFileAppender();
        appender.setName("FileLogger");
        appender.setFile("shtrihjavapos.log");
        appender.setEncoding("UTF-8");
        appender.setMaxFileSize("4Mb");
        appender.setMaxBackupIndex(10);
        appender.setLayout(new PatternLayout("%d [%7r] [%t] %6p - %c - %m%n"));
        appender.setAppend(true);
        appender.activateOptions();
        BasicConfigurator.configure(appender);
        
        Logger logger = Logger.getLogger(FileAppenderTest.class);
        long time = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            logger.debug("Test logger");
        }
        time = System.currentTimeMillis() - time;
        System.out.println("Execution time: " + time);
        assertTrue("Execution time > 150ms", time <= 150);
    }

}
