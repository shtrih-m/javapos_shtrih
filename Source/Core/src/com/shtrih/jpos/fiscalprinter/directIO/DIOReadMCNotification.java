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
import com.shtrih.fiscalprinter.command.ReadMCNotification;


public class DIOReadMCNotification extends DIOItem {

    public DIOReadMCNotification(FiscalPrinterImpl service) {
        super(service);
    }

    public void execute(int[] data, Object object) throws Exception 
    {
        Object[] params = (Object[])object;
        DIOUtils.checkObjectMinLength(params, 5);
        ReadMCNotification command = new ReadMCNotification();
        int rc = getPrinter().readMCNotification(command);
        getPrinter().check(rc);
        
        params[0] = new Integer(command.number);
        params[1] = new Integer(command.size); 
        params[2] = new Integer(command.offset);
        params[3] = new Integer(command.blockSize);
        params[4] = command.blockData;
    }
}
