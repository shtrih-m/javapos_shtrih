/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.jpos.fiscalprinter.receipt;

/**
 * @author V.Kravtsov
 */

public class NonfiscalReceipt extends CustomReceipt implements FiscalReceipt {

    public NonfiscalReceipt(ReceiptContext context) {
        super(context);
    }
}
