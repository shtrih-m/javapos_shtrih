package com.shtrih.jpos.fiscalprinter.directIO;

import com.shtrih.jpos.fiscalprinter.FiscalPrinterImpl;

public class DIOGetFSServiceState extends DIOItem {

    public DIOGetFSServiceState(FiscalPrinterImpl service) {
        super(service);
    }

    public void execute(int[] data, Object object) throws Exception {
        Object[] outParams = (Object[]) object;
        outParams[0] = service.isFSServiceRunning();
    }
}
