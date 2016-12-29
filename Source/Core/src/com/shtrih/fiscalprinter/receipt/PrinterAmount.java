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

    public static long getAmount(long price, long quantity) {
        return (price * quantity + 500) / 1000;
    }
}
