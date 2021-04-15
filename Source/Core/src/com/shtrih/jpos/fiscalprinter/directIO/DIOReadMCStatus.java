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
import com.shtrih.fiscalprinter.command.FSReadMCStatus;


public class DIOReadMCStatus extends DIOItem {

    public DIOReadMCStatus(FiscalPrinterImpl service) {
        super(service);
    }

    public void execute(int[] data, Object object) throws Exception 
    {
        Object[] params = (Object[])object;
        DIOUtils.checkObjectMinLength(params, 7);
        FSReadMCStatus command = new FSReadMCStatus();
        int rc = getPrinter().fsReadMCStatus(command);
        getPrinter().check(rc);
        
        params[0] = new Integer(command.getStatus());
        params[1] = new Integer(command.getNotificationStatus()); 
        params[2] = new Integer(command.getCommandFlags());
        params[3] = new Integer(command.getResultSavedCount());
        params[4] = new Integer(command.getRealizationCount());
        params[5] = new Integer(command.getStorageSize());
        params[6] = new Integer(command.getMessageCount());
    }
}
