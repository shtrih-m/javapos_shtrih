package com.shtrih.jpos.fiscalprinter.directIO;

import com.shtrih.fiscalprinter.command.FSPrintCorrectionReceipt;
import com.shtrih.jpos.fiscalprinter.FiscalPrinterImpl;

public class DIOPrintCorrectionReceipt extends DIOItem {

    public DIOPrintCorrectionReceipt(FiscalPrinterImpl service) {
        super(service);
    }

    public void execute(int[] data, Object object) throws Exception {
        Object[] params = (Object[]) object;
        FSPrintCorrectionReceipt command = new FSPrintCorrectionReceipt();
        command.setSysPassword(service.getPrinter().getSysPassword());
        command.setOperationType((Integer) params[0]);
        command.setTotal((Long) params[1]);

        if(getParams().autoOpenShift)
            getPrinter().openFiscalDay();

        getPrinter().check(getPrinter().fsPrintCorrectionReceipt(command));
        service.printEndFiscal();
    }

}
