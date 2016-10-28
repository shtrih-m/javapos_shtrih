/*
 * JposEntryTest.java
 *
 * Created on 10 ������� 2011 �., 11:41
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
import jpos.config.simple.SimpleEntry2;
import jpos.config.simple.SimpleEntryRegistry;
import jpos.config.simple.xml.XercesRegPopulator2;
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
        JposRegPopulator populator = new XercesRegPopulator2();
        JposEntryRegistry registry = new SimpleEntryRegistry(populator);
        JposEntry entry = new SimpleEntry2(logicalName, populator);
        registry.addJposEntry(logicalName, entry);
        entry.addProperty("Property1", "Property1");
        entry.addProperty("Property2", "Property2");
        populator.save(registry.getEntries(), fileName);
    }
}
