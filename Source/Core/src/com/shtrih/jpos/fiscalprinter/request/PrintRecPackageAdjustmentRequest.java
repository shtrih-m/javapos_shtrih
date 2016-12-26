/*
 * PrintRecPackageAdjustmentRequest.java
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

public final class PrintRecPackageAdjustmentRequest extends
        FiscalPrinterRequest {
    private final int adjustmentType;
    private final String description;
    private final String vatAdjustment;

    public PrintRecPackageAdjustmentRequest(int adjustmentType,
            String description, String vatAdjustment) {
        this.adjustmentType = adjustmentType;
        this.description = description;
        this.vatAdjustment = vatAdjustment;
    }

    public void execute(FiscalPrinterImpl device) throws Exception {
        device.printRecPackageAdjustmentAsync(adjustmentType, description,
                vatAdjustment);
    }
}
