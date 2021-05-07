/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.fiscalprinter.receipt;

/**
 * @author V.Kravtsov
 */
public class PrinterAmount {
    private PrinterAmount() {
    }

    public static long getAmount(long price, double quantity) {
        return Math.round(price * Math.abs(quantity));
    }
}
