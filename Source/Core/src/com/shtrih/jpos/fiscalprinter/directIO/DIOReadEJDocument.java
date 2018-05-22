package com.shtrih.jpos.fiscalprinter.directIO;

import com.shtrih.jpos.fiscalprinter.FiscalPrinterImpl;

public class DIOReadEJDocument extends DIOItem {

    public DIOReadEJDocument(FiscalPrinterImpl service) {
        super(service);
    }

    public void execute(int[] data, Object object) throws Exception 
    {
        int documentNumber = 0;
        if (data.length > 0){
            documentNumber = data[0];
        }
        Object[] objects = (Object[])object;
        objects[0] = service.getPrinter().readEJDocument(documentNumber);
    }
}

