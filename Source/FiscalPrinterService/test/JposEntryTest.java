/*
 * JposEntryTest.java
 *
 * Created on 10 Февраль 2011 г., 11:41
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

/**
 *
 * @author V.Kravtsov
 */

import jpos.config.JposEntry;
import jpos.config.JposEntryRegistry;
import jpos.config.JposRegPopulator;
import jpos.config.simple.SimpleEntry;
import jpos.config.simple.SimpleEntryRegistry;
import jpos.config.simple.xml.XercesRegPopulator;
import junit.framework.TestCase;


public class JposEntryTest extends TestCase {
    
    /**
     * Creates a new instance of StringTokenizerTest
     */
    public JposEntryTest(String testName) {
        super(testName);
    }
    
    public void testSave() 
    throws Exception
    {
        String fileName = "test.xml";
        String logicalName = "logicalName";
        System.out.println("testSave");
        JposRegPopulator populator = new XercesRegPopulator();
        JposEntryRegistry registry = new SimpleEntryRegistry(populator);
        JposEntry entry = new SimpleEntry(logicalName, populator);
        registry.addJposEntry(logicalName, entry);
        entry.addProperty("Property1", "Property1");
        entry.addProperty("Property2", "Property2");
        populator.save(registry.getEntries(), fileName);
    }
}
