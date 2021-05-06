package com.shtrih.jpos.fiscalprinter.directIO;

import com.shtrih.jpos.fiscalprinter.FiscalPrinterImpl;

public class DIOFSWriteOperationTLV extends DIOItem {

    public DIOFSWriteOperationTLV(FiscalPrinterImpl service) {
        super(service);
    }

    public void execute(int[] data, Object object) throws Exception 
    {
        boolean print = true;
        if (data != null) {
            print = (data[0] == 1);
        }
        service.fsWriteOperationTLV((byte[]) object, print);
    }
}
