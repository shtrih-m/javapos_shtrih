package com.shtrih.jpos.fiscalprinter.directIO;

import com.shtrih.jpos.fiscalprinter.FiscalPrinterImpl;

public class DIOFSPrintCalcReport extends DIOItem {

    public DIOFSPrintCalcReport(FiscalPrinterImpl service) {
        super(service);
    }

    public void execute(int[] data, Object object) throws Exception {

        service.fsPrintCalcReport();
    }

}
