/*
 * CashDrawerStatisticsTest.java
 * JUnit based test
 *
 * Created on 20 May 2008, 21:28
 */

package com.shtrih.jpos.cashdrawer;

import junit.framework.TestCase;
import com.shtrih.util.BasicConfigurator;

/**
 *
 * @author V.Kravtsov
 */
public class CashDrawerStatisticsTest extends TestCase {
    
    public CashDrawerStatisticsTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception 
    {
        BasicConfigurator.configure();
    }

    protected void tearDown() throws Exception {
    }

    /**
     * Test of drawerOpenSucceeded method, of class com.shtrih.jpos.cashdrawer.CashDrawerStatistics.
     */
/*    
    public void testDrawerOpenSucceeded() 
    {
        System.out.println("drawerOpenSucceeded");
        
        CashDrawerStatistics instance = new CashDrawerStatistics();
        
        instance.drawerOpenSucceeded();
        assertEquals(instance.statisticByName(
            instance.JPOS_STAT_DrawerFailedOpenCount), "0");
        assertEquals(instance.statisticByName(
            instance.JPOS_STAT_DrawerGoodOpenCount), "1");
        
        String statisticBuffer[] = new String[1];
        statisticBuffer[0] = "";
        try
        {
            instance.retrieve(statisticBuffer);
            System.out.print(statisticBuffer[0]);
            
            // update
            instance.update("\"\"=5");
            statisticBuffer[0] = "";
            instance.retrieve(statisticBuffer);
            System.out.print(statisticBuffer[0]);
            // reset
            instance.reset("");
            statisticBuffer[0] = "";
            instance.retrieve(statisticBuffer);
            System.out.print(statisticBuffer[0]);
            
        }
        catch(JposException e)
        {
            fail(e.getMessage());
        }
    }

    /**
     * Test of drawerOpenFailed method, of class com.shtrih.jpos.cashdrawer.CashDrawerStatistics.
     */
/*    
    public void testDrawerOpenFailed() {
        System.out.println("drawerOpenFailed");
        
        CashDrawerStatistics instance = new CashDrawerStatistics();
        
        instance.drawerOpenFailed();
        assertEquals(instance.statisticByName(
            instance.JPOS_STAT_DrawerGoodOpenCount), "0");
        assertEquals(instance.statisticByName(
            instance.JPOS_STAT_DrawerFailedOpenCount), "1");
        
        
    }
*/
 
    public void testSave() 
    {
        /*
        System.out.println("save");
        CashDrawerStatistics instance = new CashDrawerStatistics();
        
        instance.unifiedPOSVersion = "1.13";
        instance.deviceCategory = "CashDrawer";
        instance.manufacturerName = "SHTRIH-M";
        instance.modelName = "Fiscal printer cash drawer";
        instance.serialNumber = "123";
        instance.firmwareRevision = "234";
        instance.physicalInterface = "RS232";
        instance.installationDate = "345";
        instance.setStatistic(instance.JPOS_STAT_DrawerGoodOpenCount, "123");
        instance.setStatistic(instance.JPOS_STAT_DrawerFailedOpenCount, "234");
        
        instance.save("stat.xml");
        instance = new CashDrawerStatistics();
        instance.load("stat.xml");
        
        assertEquals(instance.unifiedPOSVersion, "1.13");
        assertEquals(instance.deviceCategory, "CashDrawer");
        assertEquals(instance.manufacturerName, "SHTRIH-M");
        assertEquals(instance.modelName, "Fiscal printer cash drawer");
        assertEquals(instance.serialNumber, "123");
        assertEquals(instance.firmwareRevision, "234");
        assertEquals(instance.physicalInterface, "RS232");
        assertEquals(instance.installationDate, "345");
        assertEquals(instance.getStatistic(instance.JPOS_STAT_DrawerGoodOpenCount), "123");
        assertEquals(instance.getStatistic(instance.JPOS_STAT_DrawerFailedOpenCount), "234");
         */
    }

}
