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
        int fiscId = data[0];
        byte[] docdata = getPrinter().fsReadFiscalizationTag(fiscId, 0xFFFF);

        Object[] outParams = (Object[]) object;
        outParams[0] = docdata;
    }
}
