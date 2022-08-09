package com.shtrih.jpos.fiscalprinter.directIO;

import com.shtrih.fiscalprinter.SMFiscalPrinter;
import com.shtrih.fiscalprinter.command.DeviceMetrics;
import com.shtrih.fiscalprinter.command.PrinterModelParameters;
import com.shtrih.jpos.DIOUtils;
import com.shtrih.jpos.fiscalprinter.FiscalPrinterImpl;

public class DIOSetFFDVersion extends DIOItem {

    public DIOSetFFDVersion(FiscalPrinterImpl service) {
        super(service);
    }

    public void execute(int[] data, Object object) throws Exception {
        DIOUtils.checkDataMinLength(data, 1);

        SMFiscalPrinter printer = getPrinter();
        PrinterModelParameters model = printer.readPrinterModelParameters();

        String value = String.valueOf(data[0]);
        if (model.capFFDTableAndColumnNumber()) {

            int rc = printer.writeTable(model.getFFDTableNumber(), 1, model.getFFDColumnNumber(), value);
            printer.check(rc);
        } else {
            if (isDesktop()) {
                int rc = printer.writeTable(17, 1, 17, value);
                printer.check(rc);
            } else {
                int rc = printer.writeTable(10, 1, 4, value);
                printer.check(rc);
            }
        }
    }

    private boolean isDesktop() throws Exception {
        DeviceMetrics metrics = getPrinter().getDeviceMetrics();
        return metrics.isDesktop() || metrics.isShtrihNano();
    }
}
