/*
 * PrintRecItemAdjustmentRequest.java
 *
 * Created on 4 April 2008, 14:38
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

public final class PrintRecItemAdjustmentRequest extends FiscalPrinterRequest {
    private final int adjustmentType;
    private final String description;
    private final long amount;
    private final int vatInfo;

    public PrintRecItemAdjustmentRequest(int adjustmentType,
            String description, long amount, int vatInfo) {
        this.adjustmentType = adjustmentType;
        this.description = description;
        this.amount = amount;
        this.vatInfo = vatInfo;
    }

    public void execute(FiscalPrinterImpl device) throws Exception {
        device.printRecItemAdjustmentAsync(getAdjustmentType(),
                getDescription(), getAmount(), getVatInfo());
    }

    public int getAdjustmentType() {
        return adjustmentType;
    }

    public String getDescription() {
        return description;
    }

    public long getAmount() {
        return amount;
    }

    public int getVatInfo() {
        return vatInfo;
    }
}
