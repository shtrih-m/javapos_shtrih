package com.shtrih.jpos.fiscalprinter.directIO;

import com.shtrih.fiscalprinter.command.RawCommand;
import com.shtrih.jpos.DIOUtils;
import com.shtrih.jpos.fiscalprinter.FiscalPrinterImpl;

public class DIOExecuteCommand {

    private final FiscalPrinterImpl service;

    public DIOExecuteCommand(FiscalPrinterImpl service) {
        this.service = service;
    }

    public void execute(int[] data, Object object) throws Exception {
        DIOUtils.checkDataMinLength(data, 1);
        DIOUtils.checkObjectNotNull(object);

        int timeout = data[0];
        byte[] tx = (byte[]) ((Object[]) object)[0];
        RawCommand command = new RawCommand(tx);
        command.setTimeout(timeout);
        service.checkEnabled();
        service.getPrinter().deviceExecute(command);
        service.getPrinter().check(command.getResultCode());
        ((Object[]) object)[1] = command.getRxData();
    }
}
