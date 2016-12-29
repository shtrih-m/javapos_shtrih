/*
 * PrintRecRefundVoidRequest.java
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

public final class PrintRecRefundVoidRequest extends FiscalPrinterRequest {
    private final String description;
    private final long amount;
    private final int vatInfo;

    public PrintRecRefundVoidRequest(String description, long amount,
            int vatInfo) {
        this.description = description;
        this.amount = amount;
        this.vatInfo = vatInfo;
    }

    public void execute(FiscalPrinterImpl device) throws Exception {
        device.printRecRefundVoidAsync(description, amount, vatInfo);
    }
}
