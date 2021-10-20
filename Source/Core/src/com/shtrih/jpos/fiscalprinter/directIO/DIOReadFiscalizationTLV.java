package com.shtrih.jpos.fiscalprinter.directIO;

import com.shtrih.jpos.DIOUtils;
import com.shtrih.jpos.fiscalprinter.FiscalPrinterImpl;

import java.io.ByteArrayOutputStream;

public class DIOReadFiscalizationTLV extends DIOItem {

    public DIOReadFiscalizationTLV(FiscalPrinterImpl service) {
        super(service);
    }

    public void execute(int[] data, Object object) throws Exception {

        DIOUtils.checkDataMinLength(data, 1);
        int fiscalizationNumber = data[0];

        getPrinter().fsReadFiscalizationTag(fiscalizationNumber, 0xFFFF);
        byte[] docdata = getPrinter().fsReadDocumentTLVToEnd();
        
        Object[] outParams = (Object[]) object;
        outParams[0] = docdata;
    }
}

