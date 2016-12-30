/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.jpos.fiscalprinter.receipt;

import com.shtrih.fiscalprinter.receipt.*;
import java.util.Vector;

import com.shtrih.fiscalprinter.SMFiscalPrinter;
import com.shtrih.fiscalprinter.command.PriceItem;
import com.shtrih.fiscalprinter.command.AmountItem;
import com.shtrih.jpos.fiscalprinter.FiscalPrinterImpl;
import com.shtrih.util.CompositeLogger;
import com.shtrih.util.MathUtils;

/**
 * @author V.Kravtsov
 */
public class FSSaleReceiptItem {

    private String preLine = "";
    private String postLine = "";
    private PriceItem item;
    private long voidAmount = 0;
    private final FSDiscounts discounts = new FSDiscounts();
    private CompositeLogger logger = CompositeLogger.getLogger(FiscalPrinterImpl.class);

    public FSSaleReceiptItem(PriceItem item, String preLine, String postline) {
        this.item = item;
        this.preLine = preLine;
        this.postLine = postline;
    }

    public void addVoidAmount(long amount) 
    {
        voidAmount += amount;
    }

    public long getVoidAmount() {
        return voidAmount;
    }

    public long getTotalWithVoids() {
        return getTotal() - voidAmount;
    }

    public long getTotal() {
        long total = item.getAmount() - discounts.getTotal();
        return total;

    }

    public long getDiscountTotal() {
        return discounts.getTotal();
    }

    public void addDiscount(FSDiscount discount) {
        getDiscounts().add(discount);
    }

    /**
     * @return the preLine
     */
    public String getPreLine() {
        return preLine;
    }

    /**
     * @return the postLine
     */
    public String getPostLine() {
        return postLine;
    }

    /**
     * @return the item
     */
    public PriceItem getItem() {
        return item;
    }

    /**
     * @return the discounts
     */
    public FSDiscounts getDiscounts() {
        return discounts;
    }

}
