/*
 * PrintRecItemFuelVoidRequest.java
 *
 * Created on 4 April 2008, 14:42
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

public final class PrintRecItemFuelVoidRequest extends FiscalPrinterRequest {
    private final String description;
    private final long price;
    private final int vatInfo;
    private final long specialTax;

    public PrintRecItemFuelVoidRequest(String description, long price,
            int vatInfo, long specialTax) {
        this.description = description;
        this.price = price;
        this.vatInfo = vatInfo;
        this.specialTax = specialTax;
    }

    public void execute(FiscalPrinterImpl device) throws Exception {
        device.printRecItemFuelVoidAsync(description, price, vatInfo,
                specialTax);
    }
}
