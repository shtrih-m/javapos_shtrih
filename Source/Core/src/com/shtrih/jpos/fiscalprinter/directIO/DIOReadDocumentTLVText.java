package com.shtrih.jpos.fiscalprinter.directIO;

import com.shtrih.fiscalprinter.DocumentTLV;
import com.shtrih.jpos.DIOUtils;
import com.shtrih.jpos.fiscalprinter.FiscalPrinterImpl;
import java.util.Vector;

public class DIOReadDocumentTLVText extends DIOItem {

    public DIOReadDocumentTLVText(FiscalPrinterImpl service) {
        super(service);
    }

    public void execute(int[] data, Object object) throws Exception {

        DIOUtils.checkDataMinLength(data, 1);
        int documentNumber = data[0];

        Vector<String> document = getPrinter().fsReadDocumentTLVAsText(documentNumber);
        Object[] outParams = (Object[]) object;
        outParams[0] = document;
    }
}

