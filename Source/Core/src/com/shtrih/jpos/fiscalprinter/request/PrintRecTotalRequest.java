/*
 * PrintRecTotalRequest.java
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

public final class PrintRecTotalRequest extends FiscalPrinterRequest {
    private final long total;
    private final long payment;
    private final String description;

    public PrintRecTotalRequest(long total, long payment, String description) {
        this.total = total;
        this.payment = payment;
        this.description = description;
    }

    public void execute(FiscalPrinterImpl device) throws Exception {
        device.printRecTotalAsync(total, payment, description);
    }
}
