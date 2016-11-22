/*
 * FlexCommandsReaderTest.java
 * JUnit based test
 *
 * Created on 19 Ноябрь 2009 г., 18:29
 */

package com.shtrih.fiscalprinter;

import junit.framework.TestCase;
import com.shtrih.fiscalprinter.command.FlexCommands;
import com.shtrih.fiscalprinter.command.FlexCommandsReader;

/**
 *
 * @author V.Kravtsov
 */
public class FlexCommandsReaderTest extends TestCase {
    
    public FlexCommandsReaderTest(String testName) {
        super(testName);
    }

    /**
     * Test of loadFromXml method, of class com.shtrih.fiscalprinter.FlexCommandsReader.
     */
    public void testLoadFromXml() throws Exception {
        System.out.println("loadFromXml");
        
        FlexCommands commands = new FlexCommands();
        FlexCommandsReader instance = new FlexCommandsReader();
        instance.load(commands);
        assertEquals(132, commands.size());
    }
    
}
