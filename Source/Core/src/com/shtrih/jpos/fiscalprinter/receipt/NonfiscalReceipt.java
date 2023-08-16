/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.jpos.fiscalprinter.receipt;

import com.shtrih.fiscalprinter.SMFiscalPrinter;

/**
 * @author V.Kravtsov
 */

public class NonfiscalReceipt extends CustomReceipt implements FiscalReceipt {

    public NonfiscalReceipt(SMFiscalPrinter printer) {
        super(printer);
    }
}
