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
import com.shtrih.fiscalprinter.command.FMTotals;
import com.shtrih.jpos.DIOUtils;
import com.shtrih.jpos.fiscalprinter.FiscalPrinterImpl;
import com.shtrih.jpos.fiscalprinter.SmFptrConst;

public class DIOReadTotals extends DIOItem {

    public DIOReadTotals(FiscalPrinterImpl service) {
        super(service);
    }

    public void execute(int[] data, Object object) throws Exception {
        int mode = SmFptrConst.FMTOTALS_ALL_FISCALIZATIONS;
        if (data.length > 0){
            mode = data[0];
        }
        SMFiscalPrinter printer = getPrinter();
        FMTotals totals = printer.readFPTotals(mode);
        Object[] outParams = (Object[]) object;
        outParams[0] = new Long(totals.getSalesAmount());
        outParams[1] = new Long(totals.getBuyAmount());
        outParams[2] = new Long(totals.getRetSaleAmount());
        outParams[3] = new Long(totals.getRetBuyAmount());
    }

}
