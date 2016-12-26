/*
 * PrintRecSubtotalRequest.java
 *
 * Created on 4 April 2008, 14:44
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

public final class PrintRecSubtotalRequest extends FiscalPrinterRequest {
    private final long amount;

    public PrintRecSubtotalRequest(long amount) {
        this.amount = amount;
    }

    public void execute(FiscalPrinterImpl device) throws Exception {
        device.printRecSubtotalAsync(amount);
    }
}
