/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.jpos.fiscalprinter.receipt;

/**
 *
 * @author V.Kravtsov
 */
import jpos.JposConst;
import jpos.JposException;

import com.shtrih.fiscalprinter.FontNumber;
import com.shtrih.fiscalprinter.SMFiscalPrinter;

public class NullReceipt implements FiscalReceipt {

    private boolean cancelled = false;

    public NullReceipt() {
    }

    public boolean getCapAutoCut() throws Exception {
        return false;
    }

    public boolean isPayed() {
        return false;
    }

    public boolean isOpened() {
        return false;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void notSupported() throws Exception {
        throw new JposException(JposConst.JPOS_E_ILLEGAL,
                "Receipt method is not supported");
    }

    public void beginFiscalReceipt(boolean printHeader) throws Exception {
        notSupported();
    }

    public void endFiscalReceipt(boolean printHeader) throws Exception {
        notSupported();
    }

    public void printRecItem(String description, long price, int quantity,
            int vatInfo, long unitPrice, String unitName) throws Exception {
        notSupported();
    }

    public void printRecItemAdjustment(int adjustmentType, String description,
            long amount, int vatInfo) throws Exception {
        notSupported();
    }

    public void printRecMessage(int station, FontNumber font, String message)
            throws Exception {
        notSupported();
    }

    public void printRecNotPaid(String description, long amount)
            throws Exception {
        notSupported();
    }

    public void printRecRefund(String description, long amount, int vatInfo)
            throws Exception {
        notSupported();
    }

    public void printRecSubtotal(long amount) throws Exception {
        notSupported();
    }

    public void printRecSubtotalAdjustment(int adjustmentType,
            String description, long amount) throws Exception {
        notSupported();
    }

    public void printRecTotal(long total, long payment, long payType,
            String description) throws Exception {
        notSupported();
    }

    public void printRecVoid(String description) throws Exception {
        cancelled = true;
    }

    public void printRecVoidItem(String description, long amount, int quantity,
            int adjustmentType, long adjustment, int vatInfo) throws Exception {
        notSupported();
    }

    public void printRecCash(long amount) throws Exception {
        notSupported();
    }

    public void printRecItemFuel(String description, long price, int quantity,
            int vatInfo, long unitPrice, String unitName, long specialTax,
            String specialTaxName) throws Exception {
        notSupported();
    }

    public void printRecItemFuelVoid(String description, long price,
            int vatInfo, long specialTax) throws Exception {
        notSupported();
    }

    public void printRecPackageAdjustment(int adjustmentType,
            String description, String vatAdjustment) throws Exception {
        notSupported();
    }

    public void printRecPackageAdjustVoid(int adjustmentType,
            String vatAdjustment) throws Exception {
        notSupported();
    }

    public void printRecRefundVoid(String description, long amount, int vatInfo)
            throws Exception {
        notSupported();
    }

    public void printRecSubtotalAdjustVoid(int adjustmentType, long amount)
            throws Exception {
        notSupported();
    }

    public void printRecTaxID(String taxID) throws Exception {
        notSupported();
    }

    public void printRecItemVoid(String description, long price, int quantity,
            int vatInfo, long unitPrice, String unitName) throws Exception {
        notSupported();
    }

    public void printRecItemAdjustmentVoid(int adjustmentType,
            String description, long amount, int vatInfo) throws Exception {
        notSupported();
    }

    public void printRecItemRefund(String description, long amount,
            int quantity, int vatInfo, long unitAmount, String unitName)
            throws Exception {
        notSupported();
    }

    public void printRecItemRefundVoid(String description, long amount,
            int quantity, int vatInfo, long unitAmount, String unitName)
            throws Exception {
        notSupported();
    }

    public void printNormal(int station, String data) throws Exception {
        notSupported();
    }

    public void fsWriteTLV(byte[] data) throws Exception {
        notSupported();
    }
}
