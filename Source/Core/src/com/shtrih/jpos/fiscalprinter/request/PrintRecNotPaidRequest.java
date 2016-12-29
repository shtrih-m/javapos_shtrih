/*
 * PrintRecNotPaidRequest.java
 *
 * Created on 4 April 2008, 14:43
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

public final class PrintRecNotPaidRequest extends FiscalPrinterRequest {
    private final String description;
    private final long amount;

    public PrintRecNotPaidRequest(String description, long amount) {
        this.description = description;
        this.amount = amount;
    }

    public void execute(FiscalPrinterImpl device) throws Exception {
        device.printRecNotPaidAsync(description, amount);
    }
}
