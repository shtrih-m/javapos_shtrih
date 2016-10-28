/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.jpos.fiscalprinter;

/**
 *
 * @author V.Kravtsov
 */
import com.shtrih.fiscalprinter.receipt.PrinterAmount;
import com.shtrih.fiscalprinter.receipt.PrinterReceipt;
import com.shtrih.fiscalprinter.receipt.ReceiptItem;
import com.shtrih.fiscalprinter.receipt.ReceiptItems;
import com.shtrih.jpos.fiscalprinter.request.PrintRecItemAdjustmentRequest;
import com.shtrih.jpos.fiscalprinter.request.PrintRecItemVoidRequest;

public class DiscountFilter extends FiscalPrinterFilter implements
        FiscalPrinterFilter113 {

    private final FiscalPrinterImpl printer;

    /** Creates a new instance of DiscountFilter */
    public DiscountFilter(FiscalPrinterImpl printer) {
        this.printer = printer;
    }

    
    public PrintRecItemAdjustmentRequest printRecItemAdjustment(
            PrintRecItemAdjustmentRequest request) throws Exception {
        return request;
    }

    public long getItemAmount(long price, int quantity, long unitPrice) {
        if (unitPrice == 0) {
            quantity = 1000;
        } else {
            if (quantity == 0) {
                quantity = 1000;
            }
            price = unitPrice;
        }
        return PrinterAmount.getAmount(price, quantity);
    }

    
    public PrintRecItemVoidRequest printRecItemVoid(
            PrintRecItemVoidRequest request) throws Exception {
        long amount = getItemAmount(request.getPrice(), request.getQuantity(),
                request.getUnitPrice());
        PrinterReceipt receipt = printer.getReceipt();
        String description = request.getDescription();
        ReceiptItems items = receipt.getItems();

        for (int i = 0; i < items.size(); i++) {
            ReceiptItem item = items.get(i);
            if (item.getId() == ReceiptItem.RECEIPT_ITEM_SALE) {
                if (item.getDescription().equalsIgnoreCase(description)) {
                    if (item.getDiscount() > 0) {
                        if (item.getAmount() == amount) {
                            request.setQuantity(1000);
                            request.setPrice(amount - item.getDiscount());
                        }
                    }
                }
            }
        }
        return request;
    }
}
