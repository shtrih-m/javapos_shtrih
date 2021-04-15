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
import com.shtrih.fiscalprinter.command.FSWriteTLVBuffer;


public class DIOReadRandomData extends DIOItem {

    public DIOReadRandomData(FiscalPrinterImpl service) {
        super(service);
    }

    public void execute(int[] data, Object object) throws Exception 
    {
        Object[] params = (Object[])object;
        DIOUtils.checkObjectMinLength(params, 1);
        FSReadRandomData command = new FSReadRandomData();
        int rc = getPrinter().fsReadRandomData(command);
        getPrinter().check(rc);
        params[0] = command.randomData;
    }
}
