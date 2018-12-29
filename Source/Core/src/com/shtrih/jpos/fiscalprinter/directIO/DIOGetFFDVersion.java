package com.shtrih.jpos.fiscalprinter.directIO;

import com.shtrih.fiscalprinter.SMFiscalPrinter;
import com.shtrih.fiscalprinter.command.DeviceMetrics;
import com.shtrih.fiscalprinter.command.PrinterModelParameters;
import com.shtrih.jpos.DIOUtils;
import com.shtrih.jpos.fiscalprinter.FiscalPrinterImpl;

public class DIOGetFFDVersion extends DIOItem {

    public DIOGetFFDVersion(FiscalPrinterImpl service) {
        super(service);
    }

    public void execute(int[] data, Object object) throws Exception {
        DIOUtils.checkDataMinLength(data, 1);

        SMFiscalPrinter printer = getPrinter();
        PrinterModelParameters model = printer.readPrinterModelParameters();

        if (model.capFFDTableAndColumnNumber()) {

            data[0] = Integer.parseInt(printer.readTable(model.getFFDTableNumber(), 1, model.getFFDColumnNumber()));
        } else {
            if (isDesktop()) {
                data[0] = Integer.parseInt(printer.readTable(17, 1, 17));
            } else {
                data[0] = Integer.parseInt(printer.readTable(10, 1, 4));
            }
        }
    }

    private boolean isDesktop() {
        DeviceMetrics metrics = getPrinter().getDeviceMetrics();
        return metrics.isDesktop() || metrics.isShtrihNano();
    }

}

