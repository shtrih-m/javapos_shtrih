/*
 * PrintRecItemVoidRequest.java
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

public final class PrintRecItemVoidRequest extends FiscalPrinterRequest {
    private String description;
    private long price;
    private int quantity;
    private int vatInfo;
    private long unitPrice;
    private String unitName;

    public PrintRecItemVoidRequest(String description, long price,
            int quantity, int vatInfo, long unitPrice, String unitName) {
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.vatInfo = vatInfo;
        this.unitPrice = unitPrice;
        this.unitName = unitName;
    }

    public void execute(FiscalPrinterImpl device) throws Exception {
        device.printRecItemVoidAsync(description, price, quantity, vatInfo,
                unitPrice, unitName);
    }

    public String getDescription() {
        return description;
    }

    public long getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getVatInfo() {
        return vatInfo;
    }

    public long getUnitPrice() {
        return unitPrice;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setVatInfo(int vatInfo) {
        this.vatInfo = vatInfo;
    }

    public void setUnitPrice(long unitPrice) {
        this.unitPrice = unitPrice;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }
}
