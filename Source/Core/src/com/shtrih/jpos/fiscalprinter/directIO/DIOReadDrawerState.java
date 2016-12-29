/*
 * DIOReadDrawerState.java
 *
 * Created on 4 Март 2010 г., 15:09
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.shtrih.jpos.fiscalprinter.directIO;

/**
 *
 * @author V.Kravtsov
 */

import com.shtrih.fiscalprinter.command.PrinterFlags;
import com.shtrih.jpos.DIOUtils;
import com.shtrih.jpos.fiscalprinter.FiscalPrinterImpl;

public class DIOReadDrawerState {

    private FiscalPrinterImpl service;

    /**
     * Creates a new instance of DIOPrintBarcode
     */
    public DIOReadDrawerState(FiscalPrinterImpl service) {
        this.service = service;
    }

    public void execute(int[] data, Object object) throws Exception {
        DIOUtils.checkDataMinLength(data, 1);
        data[0] = 0;
        PrinterFlags flags = service.readPrinterStatus().getPrinterFlags();
        if (flags.isDrawerOpened()) {
            data[0] = 1;
        }
    }
}
