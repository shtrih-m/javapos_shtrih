package com.shtrih.jpos.fiscalprinter.directIO;

import com.shtrih.jpos.DIOUtils;
import com.shtrih.jpos.fiscalprinter.FiscalPrinterImpl;

public class DIOStartFiscalization extends DIOItem {

    public DIOStartFiscalization(FiscalPrinterImpl service) {
        super(service);
    }

    public void execute(int[] data, Object object) throws Exception 
    {
        DIOUtils.checkDataMinLength(data, 1);
        int reportType = data[0];
        service.getPrinter().check(service.getPrinter().fsStartFiscalization(reportType));
    }
}

