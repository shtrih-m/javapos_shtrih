package com.shtrih.jpos.fiscalprinter.directIO;

import com.shtrih.fiscalprinter.DocumentTLV;
import com.shtrih.jpos.DIOUtils;
import com.shtrih.jpos.fiscalprinter.FiscalPrinterImpl;

public class DIOReadDocumentTLV extends DIOItem {

    public DIOReadDocumentTLV(FiscalPrinterImpl service) {
        super(service);
    }

    public void execute(int[] data, Object object) throws Exception {

        DIOUtils.checkDataMinLength(data, 1);
        int documentNumber = data[0];

        DocumentTLV document = getPrinter().fsReadDocumentTLV(documentNumber);

        Object[] outParams = (Object[]) object;
        outParams[0] = document.getDocumentType();
        outParams[1] = document.getTLV();

    }
}

