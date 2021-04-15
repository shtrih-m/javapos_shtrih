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
import com.shtrih.jpos.fiscalprinter.FiscalPrinterImpl;
import com.shtrih.fiscalprinter.command.FSReadMemorySize;


public class DIOReadMemorySize extends DIOItem {

    public DIOReadMemorySize(FiscalPrinterImpl service) {
        super(service);
    }

    public void execute(int[] data, Object object) throws Exception 
    {
        Object[] params = (Object[])object;
        DIOUtils.checkObjectMinLength(params, 2);
        FSReadMemorySize command = new FSReadMemorySize();
        int rc = getPrinter().fsReadMemorySize(command);
        getPrinter().check(rc);
        params[0] = new Integer(command.maxDocCount);
        params[1] = new Integer(command.memSizeInKb);
                
    }
}
