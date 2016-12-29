/*
 * PrintRecItemFuelRequest.java
 *
 * Created on 4 April 2008, 14:39
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

public final class PrintRecItemFuelRequest extends FiscalPrinterRequest {
    private final String description;
    private final long price;
    private final int quantity;
    private final int vatInfo;
    private final long unitPrice;
    private final String unitName;
    private final long specialTax;
    private final String specialTaxName;

    public PrintRecItemFuelRequest(String description, long price,
            int quantity, int vatInfo, long unitPrice, String unitName,
            long specialTax, String specialTaxName) {
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.vatInfo = vatInfo;
        this.unitPrice = unitPrice;
        this.unitName = unitName;
        this.specialTax = specialTax;
        this.specialTaxName = specialTaxName;
    }

    public void execute(FiscalPrinterImpl device) throws Exception {
        device.printRecItemFuelAsync(description, price, quantity, vatInfo,
                unitPrice, unitName, specialTax, specialTaxName);
    }
}
