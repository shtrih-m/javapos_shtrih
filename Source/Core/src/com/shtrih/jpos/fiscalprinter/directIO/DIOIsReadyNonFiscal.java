/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.jpos.fiscalprinter.directIO;

/**
 *
 * @author V.Kravtsov
 */

import com.shtrih.fiscalprinter.command.PrinterConst;
import com.shtrih.fiscalprinter.command.PrinterStatus;
import com.shtrih.jpos.DIOUtils;
import com.shtrih.jpos.fiscalprinter.FiscalPrinterImpl;

public class DIOIsReadyNonFiscal {

    private final FiscalPrinterImpl service;

    /**
     * Creates a new instance of DIOPrintBarcode
     */
    public DIOIsReadyNonFiscal(FiscalPrinterImpl service) {
        this.service = service;
    }

    public void execute(int[] data, Object object) throws Exception {
        String text = "";
        int isReady = 0;
        String[] lines = (String[]) object;
        DIOUtils.checkDataMinLength(data, 1);
        DIOUtils.checkObjectMinLength(lines, 1);
        PrinterStatus status = service.getPrinter().readPrinterStatus();
        text = status.getPrinterMode().getText();
        isReady = 0;
        if (status.getSubmode() == PrinterConst.ECR_SUBMODE_IDLE) {
            switch (status.getPrinterMode().getLoMode()) {
                case PrinterConst.MODE_REC:
                case PrinterConst.MODE_CLOSED:
                case PrinterConst.MODE_24OVER:
                case PrinterConst.MODE_24NOTOVER:
                    isReady = 1;
                    break;
            }
        }

        data[0] = isReady;
        lines[0] = text;
    }
}
