/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.jpos.fiscalprinter.directIO;

/**
 *
 * @author V.Kravtsov
 */

import com.shtrih.fiscalprinter.SMFiscalPrinter;
import com.shtrih.fiscalprinter.command.PrinterConst;
import com.shtrih.fiscalprinter.command.ReadLongStatus;
import com.shtrih.jpos.DIOUtils;
import com.shtrih.jpos.fiscalprinter.FiscalPrinterImpl;

public class DIOWriteCashierName extends DIOItem {

    public DIOWriteCashierName(FiscalPrinterImpl service) {
        super(service);
    }

    public void execute(int[] data, Object object) throws Exception 
    {
        String[] lines = (String[]) object;
        DIOUtils.checkObjectMinLength(lines, 1);
        String cashierName = lines[0];
        getPrinter().writeAdminName(cashierName);
        getPrinter().writeCasierName(cashierName);
    }
}
