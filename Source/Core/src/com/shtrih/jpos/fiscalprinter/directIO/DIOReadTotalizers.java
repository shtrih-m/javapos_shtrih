package com.shtrih.jpos.fiscalprinter.directIO;

import com.shtrih.jpos.DIOUtils;
import com.shtrih.fiscalprinter.SMFiscalPrinter;
import com.shtrih.jpos.fiscalprinter.FiscalPrinterImpl;

import java.io.ByteArrayOutputStream;

public class DIOReadTotalizers extends DIOItem {

    public DIOReadTotalizers(FiscalPrinterImpl service) {
        super(service);
    }

    public void execute(int[] data, Object object) throws Exception {
        DIOUtils.checkDataMinLength(data, 1);
        int recType = data[0];
        long[] totalizers = getPrinter().readTotalizers(recType);
        ((Object[])object)[0] = totalizers;
    }
}
