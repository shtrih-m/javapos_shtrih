/*
 * PrintRecItemRequest.java
 *
 * Created on 4 April 2008, 14:37
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

public final class PrintRecItemRequest extends FiscalPrinterRequest {
    private final String description;
    private final long price;
    private final int quantity;
    private final int vatInfo;
    private final long unitPrice;
    private final String unitName;

    public PrintRecItemRequest(String description, long price, int quantity,
            int vatInfo, long unitPrice, String unitName) {
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.vatInfo = vatInfo;
        this.unitPrice = unitPrice;
        this.unitName = unitName;
    }

    public void execute(FiscalPrinterImpl device) throws Exception {
        device.printRecItemAsync(description, price, quantity, vatInfo,
                unitPrice, unitName);
    }
}
