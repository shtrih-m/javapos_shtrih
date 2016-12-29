/*
 * PrintRecSubtotalAdjustmentRequest.java
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

public final class PrintRecSubtotalAdjustmentRequest extends
        FiscalPrinterRequest {
    private final int adjustmentType;
    private final String description;
    private final long amount;

    public PrintRecSubtotalAdjustmentRequest(int adjustmentType,
            String description, long amount) {
        this.adjustmentType = adjustmentType;
        this.description = description;
        this.amount = amount;
    }

    public void execute(FiscalPrinterImpl device) throws Exception {
        device.printRecSubtotalAdjustmentAsync(adjustmentType, description,
                amount);
    }
}
