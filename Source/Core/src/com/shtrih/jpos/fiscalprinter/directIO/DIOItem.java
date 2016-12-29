/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.jpos.fiscalprinter.directIO;

/**
 *
 * @author V.Kravtsov
 */

import com.shtrih.fiscalprinter.SMFiscalPrinter;
import com.shtrih.jpos.fiscalprinter.FiscalPrinterImpl;
import com.shtrih.jpos.fiscalprinter.FptrParameters;

public class DIOItem {

    public final FiscalPrinterImpl service;

    /**
     * Creates a new instance of DIOPrintBarcode
     */
    public DIOItem(FiscalPrinterImpl service) {
        this.service = service;
    }

    public SMFiscalPrinter getPrinter() {
        return service.getPrinter();
    }

    public FptrParameters getParams() {
        return service.getParams();
    }
}
