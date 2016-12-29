/*
 * FiscalReceipt.java
 *
 * Created on April 3 2008, 20:08
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.shtrih.fiscalprinter.receipt;

/**
 *
 * @author V.Kravtsov
 */
import java.util.Vector;

import com.shtrih.util.CompositeLogger;

import com.shtrih.fiscalprinter.command.AmountItem;
import com.shtrih.fiscalprinter.command.PriceItem;
import com.shtrih.util.MethodParameter;

public class PrinterReceipt {

    private long total = 0; // receipt total
    private long lastAmount = 0; // last item amount
    private boolean isOpened = false;
    private final long[] payments = new long[5]; // payment amounts
    private boolean isCancelled = false; // receipt is isCancelled

    public long[] vatAmount = new long[5];
    public long[] chargeAmount = new long[5];
    private final long[] discountAmount = new long[5];

    private final Vector discounts = new Vector();
    private final ReceiptItems items = new ReceiptItems();
    private static CompositeLogger logger = CompositeLogger.getLogger(PrinterReceipt.class);
    private ReceiptItem lastItem = null;
    private int receiptType = 0;

    /**
     * Creates a new instance of FiscalReceipt
     */
    public PrinterReceipt() {
        reset();
    }

    public long[] getPayments() {
        return payments;
    }

    public long getPayment(int index) {
        return payments[index];
    }

    public void reset() {
        total = 0;
        lastAmount = 0;
        for (int i = 0; i < 5; i++) {
            payments[i] = 0;
            vatAmount[i] = 0;
            chargeAmount[i] = 0;
            discountAmount[i] = 0;
        }
        isOpened = false;
        isCancelled = false;
        items.clear();
        lastItem = null;
        receiptType = 0;
    }

    public void openReceipt(int receiptType) {
        this.receiptType = receiptType;
        this.isOpened = true;
    }

    public void close() {
        this.isOpened = false;
    }

    public boolean hasItems() {
        return items.size() > 0;
    }

    public long getTotal() {
        logger.debug("getTotal: " + String.valueOf(total));
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public boolean isCancelled() {
        return isCancelled;
    }

    public void cancel() {
        isCancelled = true;
    }

    public long getCashlessPayment() {
        return payments[1] + payments[2] + payments[3];
    }

    public long getPaymentAmount() {
        return payments[0] + payments[1] + payments[2] + payments[3];
    }

    public boolean isOpened() {
        return this.isOpened;
    }

    public boolean isPayed() {
        return getPaymentAmount() >= total;
    }

    public void addVatAmount(long amount, int vatInfo) {
        vatAmount[vatInfo] = vatAmount[vatInfo] + amount;
    }

    public void printSale(PriceItem item) {
        lastAmount = item.getAmount();
        addtotal(lastAmount);
        addVatAmount(lastAmount, item.getTax1());
        addReceiptItem(new SaleReceiptItem(item));
    }

    public void addReceiptItem(ReceiptItem item) {
        items.add(item);
        lastItem = item;
    }

    public void printSaleRefund(PriceItem item) {
        lastAmount = item.getAmount();
        addtotal(lastAmount);
        addVatAmount(lastAmount, item.getTax1());
        addReceiptItem(new RefundReceiptItem(item));
    }

    public void printStorno(PriceItem item) {
        lastAmount = item.getAmount();
        addtotal(-lastAmount);
        addVatAmount(-lastAmount, item.getTax1());
        addReceiptItem(new StornoReceiptItem(item));
    }

    public long getItemPercentAdjustmentAmount(long amount) {
        return lastAmount * amount / 10000;
    }

    public void addPayment(long payment, long payType) throws Exception {
        logger.debug("addPayment(" + String.valueOf(payment) + ", "
                + String.valueOf(payType) + ")");

        MethodParameter.checkRange(payType, 0, 3, "payType");
        payments[(int) payType] += payment;
    }

    public void addtotal(long sum) {
        total += sum;
        logger.debug("total: " + total);
    }

    public void printDiscount(AmountItem item) throws Exception {
        if (item.getAmount() > total) {
            throw new Exception("Negative receipt total");
        }
        discounts.add(item);
        discountAmount[item.getTax1()] += item.getAmount();
        addtotal(-item.getAmount());
        items.add(new DiscountItem(item));
        if (lastItem != null) {
            lastItem.addDiscount(item.getAmount());
        }
    }

    public Vector getDiscounts() {
        return discounts;
    }

    public void printCharge(AmountItem item) {
        addtotal(item.getAmount());
        chargeAmount[item.getTax1()] += item.getAmount();
        items.add(new ChargeItem(item));
    }

    public ReceiptItems getItems() {
        return items;
    }

    long roundAmount(double amount) {
        return (long) (amount + 0.5);
    }

    private long[] getTaxTotals(long amount) 
    {
        logger.debug("getTaxTotals(" + amount + ")");
        long[] result = new long[5];
        long noTaxSumm = amount;
        for (int i = 0; i <= 4; i++) {
            result[i] = 0;
            long taxAmount = vatAmount[i] - discountAmount[i] + chargeAmount[i];
            if (taxAmount != 0) {
                result[i] = roundAmount(amount * taxAmount / total);
                noTaxSumm = noTaxSumm - result[i];
            }
        }

        for (int i = 0; i <= 4; i++) {
            long taxAmount = vatAmount[i] - discountAmount[i] + chargeAmount[i];
            if (taxAmount != 0) 
            {
                result[i] = result[i] + noTaxSumm;
                break;
            }
        }
        return result;
    }

    public void subtotalCharge(long amount) throws Exception {
        long[] taxAmounts = getTaxTotals(amount);
        for (int i = 0; i < 4; i++) {
            chargeAmount[i] = chargeAmount[i] + taxAmounts[i];
        }
        total = total + amount;
    }

    public void subtotalDiscount(long amount) throws Exception {
        long[] taxAmounts = getTaxTotals(amount);
        for (int i = 0; i < 4; i++) {
            discountAmount[i] = discountAmount[i] + taxAmounts[i];
        }
        total = total - amount;
    }

    public long getDiscountAmount(int tax) {
        return discountAmount[tax];
    }

    public long getChargeAmount(int tax) {
        return chargeAmount[tax];
    }
}
