/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.jpos.fiscalprinter.receipt;

/**
 *
 * @author V.Kravtsov
 */

import com.shtrih.fiscalprinter.FontNumber;
import com.shtrih.fiscalprinter.SMFiscalPrinter;

public interface ReceiptPrinter {

    public void waitForPrinting() throws Exception;

    public void printPreLine() throws Exception;

    public void printPostLine() throws Exception;

    public long getSubtotal() throws Exception;

    public void openReceipt(int receiptType) throws Exception;

    public String printDescription(String description) throws Exception;

    public void printText(String line) throws Exception;

    public void printText(int station, String text, FontNumber font)
            throws Exception;

    public void printStrings(String line1, String line2) throws Exception;

    public SMFiscalPrinter getPrinter() throws Exception;

    public int getTextLength() throws Exception;

    public void printSeparator(int separatorType, int height) throws Exception;

    public int getStation(int station) throws Exception;

    public void checkZeroReceipt() throws Exception;
}
