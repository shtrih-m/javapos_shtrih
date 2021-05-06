package com.shtrih.jpos.fiscalprinter.directIO;

import com.shtrih.jpos.fiscalprinter.FiscalPrinterImpl;

public class DIOFSWriteTLV extends DIOItem {

    public DIOFSWriteTLV(FiscalPrinterImpl service) {
        super(service);
    }

    public void execute(int[] data, Object object) throws Exception {
        boolean print = true;
        if (data != null) {
            print = (data[0] == 1);
        }
        service.fsWriteTLV((byte[]) object, print);
    }
}
