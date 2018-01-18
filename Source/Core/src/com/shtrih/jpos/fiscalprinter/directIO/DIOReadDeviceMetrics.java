package com.shtrih.jpos.fiscalprinter.directIO;

import com.shtrih.fiscalprinter.SMFiscalPrinter;
import com.shtrih.fiscalprinter.SmFiscalPrinterException;
import com.shtrih.jpos.DIOUtils;
import com.shtrih.jpos.fiscalprinter.FiscalPrinterImpl;

import java.io.ByteArrayOutputStream;

public class DIOReadDeviceMetrics extends DIOItem {

    public DIOReadDeviceMetrics(FiscalPrinterImpl service) {
        super(service);
    }

    public void execute(int[] data, Object object) throws Exception {

        SMFiscalPrinter printer = getPrinter();

        int rc = printer.readDeviceMetrics();
        printer.check(rc);

        Object[] outParams = (Object[]) object;
        outParams[0] = printer.getDeviceMetrics();
    }
}
