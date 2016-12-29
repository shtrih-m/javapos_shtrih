/*
 * PrintRecSubtotalAdjustVoidRequest.java
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

public final class PrintRecSubtotalAdjustVoidRequest extends
        FiscalPrinterRequest {
    private final int adjustmentType;
    private final long amount;

    public PrintRecSubtotalAdjustVoidRequest(int adjustmentType, long amount) {
        this.adjustmentType = adjustmentType;
        this.amount = amount;
    }

    public void execute(FiscalPrinterImpl device) throws Exception {
        device.printRecSubtotalAdjustVoidAsync(adjustmentType, amount);
    }
}
