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
public class ChargeItem implements ReceiptItem {

    private final AmountItem item;

    public ChargeItem(AmountItem item) {
        this.item = item;
    }

    public long getAmount() {
        return item.getAmount();
    }

    public String getText() {
        return item.getText();
    }

    public void print(SMFiscalPrinter printer) throws Exception {
        printer.printCharge(item);
    }

    public int getId() {
        return RECEIPT_ITEM_CHARGE;
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
