/*
 * DIOReadPrinterStatus.java
 *
 * Created on 4 Март 2010 г., 15:30
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.shtrih.jpos.fiscalprinter.directIO;

/**
 *
 * @author V.Kravtsov
 */

import com.shtrih.fiscalprinter.command.PrinterStatus;
import com.shtrih.jpos.DIOUtils;
import com.shtrih.jpos.fiscalprinter.FiscalPrinterImpl;

public class DIOReadPrinterStatus {

    private FiscalPrinterImpl service;

    /**
     * Creates a new instance of DIOPrintBarcode
     */
    public DIOReadPrinterStatus(FiscalPrinterImpl service) {
        this.service = service;
    }

    public void execute(int[] data, Object object) throws Exception {
        DIOUtils.checkDataMinLength(data, 4);
        PrinterStatus status = service.readPrinterStatus();
        data[0] = status.getMode();
        data[1] = status.getSubmode();
        data[2] = status.getFlags();
        data[3] = status.getOperator();
        if (object != null) {
            String[] lines = (String[]) object;
            if (lines.length > 0) {
                lines[0] = status.getText();
            }
        }

    }

}
