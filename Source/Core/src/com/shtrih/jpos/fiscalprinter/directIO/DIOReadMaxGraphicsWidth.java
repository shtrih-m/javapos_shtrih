package com.shtrih.jpos.fiscalprinter.directIO;

import com.shtrih.jpos.DIOUtils;
import com.shtrih.jpos.fiscalprinter.FiscalPrinterImpl;

public class DIOReadMaxGraphicsWidth {

    private FiscalPrinterImpl service;

    public DIOReadMaxGraphicsWidth(FiscalPrinterImpl service) {
        this.service = service;
    }

    public void execute(int[] data, Object object) throws Exception {

        DIOUtils.checkDataMinLength(data, 1);

        data[0] = service.getPrinter().getMaxGraphicsWidth();
    }
}
