/*
 * PrintRecPackageAdjustVoidRequest.java
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

public final class PrintRecPackageAdjustVoidRequest extends
        FiscalPrinterRequest {
    private int adjustmentType;
    private String vatAdjustment;

    public PrintRecPackageAdjustVoidRequest(int adjustmentType,
            String vatAdjustment) {
        this.adjustmentType = adjustmentType;
        this.vatAdjustment = vatAdjustment;
    }

    public void execute(FiscalPrinterImpl device) throws Exception {
        device.printRecPackageAdjustVoidAsync(adjustmentType, vatAdjustment);
    }
}
