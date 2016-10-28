/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package acceptance;

import junit.framework.TestCase;
import jpos.services.FiscalPrinterService113;
import jpos.FiscalPrinter113;
import com.shtrih.jpos.DeviceService;
import com.shtrih.fiscalprinter.ShtrihFiscalPrinter;
import com.shtrih.jpos.fiscalprinter.FiscalPrinterImpl;
import com.shtrih.jpos.fiscalprinter.FiscalPrinterService;
import jpos.FiscalPrinterControl113;

/**
 *
 * @author V.Kravtsov
 */
public class FiscalPrinterServiceTest extends TestCase 
{
    public FiscalPrinterServiceTest(String testName) {
        super(testName);
    }
    
    protected void setUp() throws Exception {
        super.setUp();
    }
    
    protected void tearDown() throws Exception {
        super.tearDown();
    }
    
    
    
    public void testParseEJActivation() throws Exception 
    {
        FiscalPrinterEmulator printer = new FiscalPrinterEmulator();
        FiscalPrinterControl113 control = new FiscalPrinter113();
        ShtrihFiscalPrinter driver = new ShtrihFiscalPrinter(control);
    }
    
}
