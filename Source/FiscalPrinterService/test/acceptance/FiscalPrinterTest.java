/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package acceptance;

import junit.framework.TestCase;

import jpos.config.JposEntry;
import jpos.FiscalPrinter113;
import jpos.FiscalPrinterControl113;
import jpos.services.FiscalPrinterService113;

import com.shtrih.jpos.DeviceService;
import com.shtrih.fiscalprinter.SMFiscalPrinterNull;
import com.shtrih.fiscalprinter.ShtrihFiscalPrinter;
import com.shtrih.jpos.fiscalprinter.FptrParameters;
import com.shtrih.jpos.fiscalprinter.FiscalPrinterImpl;
import com.shtrih.jpos.fiscalprinter.FiscalPrinterService;

/**
 *
 * @author V.Kravtsov
 */
public class FiscalPrinterTest extends TestCase 
{
    public FiscalPrinterTest(String testName) {
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
        /*
        FptrParameters parameters = new FptrParameters();
        SMFiscalPrinterNull printer = new SMFiscalPrinterNull(parameters);
        FiscalPrinterImpl service = new FiscalPrinterImpl();
        service.setPrinter(printer);
        JposEntry jposEntry = new JposEntry();
        service.setJposEntry(jposEntry);
        service.open("ShtrihFptr", null);
        */
    }
    
}
