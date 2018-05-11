package com.shtrih.jpos.fiscalprinter.directIO;

import com.shtrih.jpos.fiscalprinter.FiscalPrinterImpl;

public class DIOFSWriteOperationTLV extends DIOItem {

    public DIOFSWriteOperationTLV(FiscalPrinterImpl service) {
        super(service);
    }

    public void execute(int[] data, Object object) throws Exception {
        service.fsWriteOperationTLV((byte[]) object);
    }
}
