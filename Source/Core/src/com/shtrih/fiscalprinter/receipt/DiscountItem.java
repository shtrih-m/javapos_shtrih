/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.fiscalprinter.receipt;

import com.shtrih.fiscalprinter.SMFiscalPrinter;
import com.shtrih.fiscalprinter.command.AmountItem;

/**
 * @author V.Kravtsov
 */
public class DiscountItem implements ReceiptItem {

    private final AmountItem item;

    public DiscountItem(AmountItem item) {
        this.item = item;
    }

    public void print(SMFiscalPrinter printer) throws Exception {
        printer.printDiscount(item);
    }

    public int getId() {
        return RECEIPT_ITEM_DISCOUNT;
    }

    public long getAmount() {
        return item.getAmount();
    }

    public String getDescription() {
        return item.getText();
    }

    public long getDiscount() {
        return 0;
    }

    public void addDiscount(long amount) {
    }
}
