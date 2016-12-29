/*
 * PrintRecItemRefundVoidRequest.java
 *
 * Created on 26 Апрель 2010 г., 11:53
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

public final class PrintRecItemRefundVoidRequest extends FiscalPrinterRequest {

    private final String description;
    private final long amount;
    private final int quantity;
    private final int vatInfo;
    private final long unitAmount;
    private final String unitName;

    /** Creates a new instance of PrintRecItemRefundVoidRequest */
    public PrintRecItemRefundVoidRequest(String description, long amount,
            int quantity, int vatInfo, long unitAmount, String unitName) {
        this.description = description;
        this.amount = amount;
        this.quantity = quantity;
        this.vatInfo = vatInfo;
        this.unitAmount = unitAmount;
        this.unitName = unitName;
    }

    public void execute(FiscalPrinterImpl device) throws Exception {
        device.printRecItemRefundVoidAsync(description, amount, quantity,
                vatInfo, unitAmount, unitName);
    }

}
