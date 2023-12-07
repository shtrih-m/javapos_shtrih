/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.jpos.fiscalprinter.directIO;

/**
 *
 * @author V.Kravtsov
 */

import com.shtrih.jpos.fiscalprinter.FiscalPrinterImpl;
import com.shtrih.fiscalprinter.command.FSReadCommStatus;
import com.shtrih.fiscalprinter.command.FSReadDocumentSize;
import com.shtrih.fiscalprinter.command.PrinterDate;
import com.shtrih.fiscalprinter.command.PrinterTime;
import com.shtrih.jpos.DIOUtils;
import com.shtrih.jpos.fiscalprinter.JposFiscalPrinterDate;

public class DIOReadVVASupported extends DIOItem {

    public DIOReadVVASupported(FiscalPrinterImpl service) {
        super(service);
    }

    public void execute(int[] data, Object object) throws Exception
    {
        DIOUtils.checkDataMinLength(data, 1);
        data[0] = 0;
        if (service.getPrinter().isVVASupported()){
            data[1] = 1;
        }
    }
}
