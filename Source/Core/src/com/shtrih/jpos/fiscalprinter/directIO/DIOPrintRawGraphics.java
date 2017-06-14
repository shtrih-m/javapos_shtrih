package com.shtrih.jpos.fiscalprinter.directIO;

import com.shtrih.jpos.fiscalprinter.FiscalPrinterImpl;

public class DIOPrintRawGraphics extends DIOItem {

    public DIOPrintRawGraphics(FiscalPrinterImpl service) {
        super(service);
    }

    public void execute(int[] data, Object object) throws Exception {
        byte[][] image  = (byte[][]) object;
        service.printRawGraphics(image);
    }

}
