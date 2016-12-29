/*
 * FiscalDay.java
 *
 * Created on 18 April 2008, 12:48
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.shtrih.jpos.fiscalprinter;

/**
 * @author V.Kravtsov
 */

public class FiscalDay {
    private boolean isOpened;
    private long fiscalDocNumber; // Get the number of daily fiscal documents.
    private long fiscalDocVoidNumber; // Get the number of daily voided fiscal
                                      // documents.
    private long fiscalRecNumber; // Get the number of daily fiscal sales
                                  // receipts.
    private long fiscalRecVoidNumber; // Get the number of daily voided fiscal
                                      // sales receipts
    private long nonFiscalDocNumber; // Get the number of daily non fiscal
                                     // documents.
    private long nonFiscalDocVoidNumber; // Get the number of daily voided non
                                         // fiscal documents.
    private long nonFiscalRecNumber; // Get the number of daily non fiscal
                                     // receipts.
    private long simpInvoiceNumber; // Get the number of daily simplified
                                    // invoices.

    public FiscalDay() {
        close();
    }

    public void open() {
        isOpened = true;
    }

    public void close() {
        isOpened = false;
        fiscalRecNumber = 0;
        fiscalDocNumber = 0;
        fiscalRecVoidNumber = 0;
        fiscalDocVoidNumber = 0;
        nonFiscalDocNumber = 0;
        nonFiscalDocVoidNumber = 0;
        nonFiscalRecNumber = 0;
        simpInvoiceNumber = 0;
    }

    public boolean getOpened() {
        return isOpened;
    }

    public long getFiscalRecNumber() {
        return fiscalRecNumber;
    }

    public long getFiscalDocNumber() {
        return fiscalDocNumber;
    }

    public long getFiscalDocVoidNumber() {
        return fiscalDocVoidNumber;
    }

    public long getFiscalRecVoidNumber() {
        return fiscalRecVoidNumber;
    }

    public long getNonFiscalDocNumber() {
        return nonFiscalDocNumber;
    }

    public long getNonFiscalDocVoidNumber() {
        return nonFiscalDocVoidNumber;
    }

    public long getNonFiscalRecNumber() {
        return nonFiscalRecNumber;
    }

    public long getSimpInvoiceNumber() {
        return simpInvoiceNumber;
    }

    public void closeFiscalDoc() {
        fiscalDocNumber++;
    }

    public void cancelFiscalDoc() {
        fiscalDocVoidNumber++;
    }

    public void closeFiscalRec() {
        fiscalRecNumber++;
    }

    public void cancelFiscalRec() {
        fiscalRecVoidNumber++;
    }

    public void closeNonFiscalDoc() {
        nonFiscalDocNumber++;
    }

    public void cancelNonFiscalDoc() {
        nonFiscalDocVoidNumber++;
    }

    public void closeNonFiscalRec() {
        nonFiscalRecNumber++;
    }

    public void closeSimpInvoice() {
        simpInvoiceNumber++;
    }
}
