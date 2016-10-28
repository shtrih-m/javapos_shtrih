/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.fiscalprinter.receipt;

/**
 *
 * @author V.Kravtsov
 */

import com.shtrih.fiscalprinter.SMFiscalPrinter;

public interface ReceiptItem 
{
    public static final int RECEIPT_ITEM_SALE = 0;
    public static final int RECEIPT_ITEM_VOID_SALE = 1;
    public static final int RECEIPT_ITEM_REFUND = 2;
    public static final int RECEIPT_ITEM_VOID_REFUND = 3;
    public static final int RECEIPT_ITEM_DISCOUNT = 4;
    public static final int RECEIPT_ITEM_CHARGE = 5;

    public int getId();

    public long getAmount();

    public long getDiscount();

    public String getDescription();

    public void addDiscount(long amount);

    public void print(SMFiscalPrinter printer) throws Exception;
}
