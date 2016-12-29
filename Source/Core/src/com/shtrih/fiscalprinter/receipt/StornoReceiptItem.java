/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.fiscalprinter.receipt;

import java.util.Vector;

import com.shtrih.fiscalprinter.SMFiscalPrinter;
import com.shtrih.fiscalprinter.command.PriceItem;
import com.shtrih.fiscalprinter.command.AmountItem;


/**
 * @author V.Kravtsov
 */
public class StornoReceiptItem implements ReceiptItem {

    private PriceItem item;
    private long discount = 0;

    public StornoReceiptItem(PriceItem item) {
        this.item = item;
    }

    public PriceItem getItem() {
        return item;
    }

    public int getTax1() {
        return getItem().getTax1();
    }

    public int getId() {
        return RECEIPT_ITEM_SALE;
    }

    public String getDescription() {
        return getItem().getText();
    }
    
    public long getDiscount() {
        return discount;
    }

    public void setDiscount(long value) {
        discount = value;
    }
    
    public long getAmount() {
        return PrinterAmount.getAmount(getItem().getPrice(), getItem()
                .getQuantity());
    }

    public void addDiscount(long amount) {
        setDiscount(getDiscount() + amount);
    }

    public void print(SMFiscalPrinter printer) throws Exception {
        printer.printSale(getItem());
    }

    /**
     * @param item
     *            the item to set
     */
    public void setItem(PriceItem item) {
        this.item = item;
    }
}
