/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.jpos.fiscalprinter.directIO;

/**
 *
 * @author V.Kravtsov
 */

import com.shtrih.jpos.DIOUtils;
import com.shtrih.fiscalprinter.GS1Barcode;
import com.shtrih.fiscalprinter.SMFiscalPrinter;
import com.shtrih.fiscalprinter.command.FSCheckMC;
import com.shtrih.fiscalprinter.command.FSReadRandomData;
import com.shtrih.jpos.fiscalprinter.FiscalPrinterImpl;
import com.shtrih.fiscalprinter.command.FSStartReadMCNotifications;


public class DIOStartReadMCNotifications extends DIOItem {

    public DIOStartReadMCNotifications(FiscalPrinterImpl service) {
        super(service);
    }

    public void execute(int[] data, Object object) throws Exception 
    {
        Object[] params = (Object[])object;
        DIOUtils.checkObjectMinLength(params, 3);
        FSStartReadMCNotifications command = new FSStartReadMCNotifications();
        int rc = getPrinter().startReadMCNotifications(command);
        getPrinter().check(rc);
        
        params[0] = new Integer(command.count);
        params[1] = new Long(command.number); 
        params[2] = new Integer(command.size);
    }
}
