package com.shtrih.jpos.fiscalprinter.directIO;

import com.shtrih.jpos.fiscalprinter.FiscalPrinterImpl;

public class DIOStartDayClose extends DIOItem {

    public DIOStartDayClose(FiscalPrinterImpl service) {
        super(service);
    }

    public void execute(int[] data, Object object) throws Exception {
        service.getPrinter().check(service.getPrinter().fsStartDayClose());
    }
}

