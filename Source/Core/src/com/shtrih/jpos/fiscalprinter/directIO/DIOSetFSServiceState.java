package com.shtrih.jpos.fiscalprinter.directIO;

import com.shtrih.jpos.DIOUtils;
import com.shtrih.jpos.fiscalprinter.FiscalPrinterImpl;

public class DIOSetFSServiceState extends DIOItem {

    public DIOSetFSServiceState(FiscalPrinterImpl service) {
        super(service);
    }

    public void execute(int[] data, Object object) throws Exception {

        DIOUtils.checkDataMinLength(data, 1);

        boolean stop = data[0] == 0;

        if(stop)
            service.stopFSService();
        else
            service.startFSService();
    }
}
