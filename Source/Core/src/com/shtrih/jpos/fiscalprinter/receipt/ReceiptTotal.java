/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.jpos.fiscalprinter.receipt;

/**
 *
 * @author Kravtsov
 */
public class ReceiptTotal {

    private long total = 0; // receipt total
    private long[] totals = new long[10];

    public ReceiptTotal() {
    }

    public ReceiptTotal(ReceiptTotal src) {
        total = src.getTotal();
        for (int i = 0; i < totals.length; i++) {
            totals[i] = src.getTotal(i);
        }
    }

    public long getTotal() {
        return total;
    }

    public long getTotal(int tax) {
        return totals[tax];
    }

    public void add(long amount, int tax) {
        total += amount;
        totals[tax] += amount;
    }

    public void clear() {
        total = 0;
        for (int i = 0; i < totals.length; i++) {
            totals[i] = 0;
        }
    }
}
