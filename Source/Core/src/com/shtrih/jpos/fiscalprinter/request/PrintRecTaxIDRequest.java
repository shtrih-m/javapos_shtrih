/*
 * PrintRecTaxIDRequest.java
 *
 * Created on 4 April 2008, 14:45
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.shtrih.jpos.fiscalprinter.request;

/**
 *
 * @author V.Kravtsov
 */

import com.shtrih.jpos.fiscalprinter.FiscalPrinterImpl;

public final class PrintRecTaxIDRequest extends FiscalPrinterRequest {
    private final String taxID;

    public PrintRecTaxIDRequest(String taxID) {
        this.taxID = taxID;
    }

    
    public void execute(FiscalPrinterImpl device) throws Exception {
        device.printRecTaxIDAsync(taxID);
    }
}
