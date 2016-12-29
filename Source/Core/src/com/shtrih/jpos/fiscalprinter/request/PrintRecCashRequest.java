/*
 * PrintRecCashRequest.java
 *
 * Created on 4 April 2008, 14:37
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.shtrih.jpos.fiscalprinter.request;

import com.shtrih.jpos.fiscalprinter.FiscalPrinterImpl;

public final class PrintRecCashRequest extends FiscalPrinterRequest {
    private final long amount;

    public PrintRecCashRequest(long amount) {
        this.amount = amount;
    }

    public void execute(FiscalPrinterImpl device) throws Exception {
        device.printRecCashAsync(amount);
    }
}
