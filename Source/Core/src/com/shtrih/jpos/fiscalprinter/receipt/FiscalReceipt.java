/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.jpos.fiscalprinter.receipt;

/**
 * @author V.Kravtsov
 */

import com.shtrih.barcode.PrinterBarcode;
import com.shtrih.fiscalprinter.FontNumber;
import com.shtrih.fiscalprinter.PrinterGraphics;
import com.shtrih.fiscalprinter.command.TextLine;
import java.util.Vector;

public interface FiscalReceipt {

    public boolean getCapAutoCut() throws Exception;

    public boolean isPayed() throws Exception;

    public boolean isOpened();

    public boolean isCancelled();

    public void beginFiscalReceipt(boolean printHeader) throws Exception;

    public void endFiscalReceipt(boolean printHeader) throws Exception;

    public void printRecItem(String description, long price, int quantity,
                             int vatInfo, long unitPrice, String unitName) throws Exception;

    public void printRecItemAdjustment(int adjustmentType, String description,
                                       long amount, int vatInfo) throws Exception;

    public void printRecMessage(int station, FontNumber font, String message)
            throws Exception;

    public void printRecNotPaid(String description, long amount)
            throws Exception;

    public void printRecRefund(String description, long amount, int vatInfo)
            throws Exception;

    public void printRecSubtotal(long amount) throws Exception;

    public void printRecSubtotalAdjustment(int adjustmentType,
                                           String description, long amount) throws Exception;

    public void printRecTotal(long total, long payment, long payType,
                              String description) throws Exception;

    public void printRecVoid(String description) throws Exception;

    public void printRecVoidItem(String description, long amount, int quantity,
                                 int adjustmentType, long adjustment, int vatInfo) throws Exception;

    public void printRecCash(long amount) throws Exception;

    public void printRecItemFuel(String description, long price, int quantity,
                                 int vatInfo, long unitPrice, String unitName, long specialTax,
                                 String specialTaxName) throws Exception;

    public void printRecItemFuelVoid(String description, long price,
                                     int vatInfo, long specialTax) throws Exception;

    public void printRecPackageAdjustment(int adjustmentType,
                                          String description, String vatAdjustment) throws Exception;

    public void printRecPackageAdjustVoid(int adjustmentType,
                                          String vatAdjustment) throws Exception;

    public void printRecRefundVoid(String description, long amount, int vatInfo)
            throws Exception;

    public void printRecSubtotalAdjustVoid(int adjustmentType, long amount)
            throws Exception;

    public void printRecTaxID(String taxID) throws Exception;

    public void printRecItemVoid(String description, long price, int quantity,
                                 int vatInfo, long unitPrice, String unitName) throws Exception;

    public void printRecItemAdjustmentVoid(int adjustmentType,
                                           String description, long amount, int vatInfo) throws Exception;

    public void printRecItemRefund(String description, long amount,
                                   int quantity, int vatInfo, long unitAmount, String unitName)
            throws Exception;

    public void printRecItemRefundVoid(String description, long amount,
                                       int quantity, int vatInfo, long unitAmount, String unitName)
            throws Exception;

    public void printNormal(int station, String data) throws Exception;

    public void fsWriteTLV(byte[] data) throws Exception;

    public void fsWriteTag(int tagId, String tagValue) throws Exception;

    public void disablePrint() throws Exception;

    public boolean getDisablePrint();

    public void fsWriteCustomerEmail(String text) throws Exception;

    public void fsWriteCustomerPhone(String text) throws Exception;

    public void setDiscountAmount(int amount) throws Exception;

    public void printBarcode(PrinterBarcode barcode) throws Exception;

    void printGraphics(PrinterGraphics graphics) throws Exception;
    
}