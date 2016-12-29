/*
 * FiscalPrinterFilter.java
 *
 * Created on 13 Январь 2011 г., 12:49
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.shtrih.jpos.fiscalprinter;

/**
 *
 * @author V.Kravtsov
 */

import com.shtrih.jpos.fiscalprinter.request.PrintRecItemAdjustmentRequest;
import com.shtrih.jpos.fiscalprinter.request.PrintRecItemVoidRequest;

public class FiscalPrinterFilter implements FiscalPrinterFilter113 {

    /** Creates a new instance of FiscalPrinterFilter */
    public FiscalPrinterFilter() {
    }

    public void beginFiscalDocument(int documentAmount) throws Exception {
    }

    public void beginFiscalReceipt(boolean printHeader) throws Exception {
    }

    public void beginFixedOutput(int station, int documentType)
            throws Exception {
    }

    public void beginInsertion(int timeout) throws Exception {
    }

    public void beginItemList(int vatID) throws Exception {
    }

    public void beginNonFiscal() throws Exception {
    }

    public void beginRemoval(int timeout) throws Exception {
    }

    public void beginTraining() throws Exception {
    }

    public void clearError() throws Exception {
    }

    public void clearOutput() throws Exception {
    }

    public void endFiscalDocument() throws Exception {
    }

    public void endFiscalReceipt(boolean printHeader) throws Exception {
    }

    public void endFixedOutput() throws Exception {
    }

    public void endInsertion() throws Exception {
    }

    public void endItemList() throws Exception {
    }

    public void endNonFiscal() throws Exception {
    }

    public void endRemoval() throws Exception {
    }

    public void endTraining() throws Exception {
    }

    public void getData(int dataItem, int[] optArgs, String[] data)
            throws Exception {
    }

    public void getDate(String[] Date) throws Exception {
    }

    public void getTotalizer(int vatID, int optArgs, String[] data)
            throws Exception {
    }

    public void getVatEntry(int vatID, int optArgs, int[] vatRate)
            throws Exception {
    }

    public void printDuplicateReceipt() throws Exception {
    }

    public void printFiscalDocumentLine(String documentLine) throws Exception {
    }

    public void printFixedOutput(int documentType, int lineNumber, String data)
            throws Exception {
    }

    public void printNormal(int station, String data) throws Exception {
    }

    public void printPeriodicTotalsReport(String date1, String date2)
            throws Exception {
    }

    public void printPowerLossReport() throws Exception {
    }

    public void printRecItem(String description, long price, int quantity,
            int vatInfo, long unitPrice, String unitName) throws Exception {
    }

    public PrintRecItemAdjustmentRequest printRecItemAdjustment(
            PrintRecItemAdjustmentRequest request) throws Exception {
        return request;
    }

    public void printRecMessage(String message) throws Exception {
    }

    public void printRecNotPaid(String description, long amount)
            throws Exception {
    }

    public void printRecRefund(String description, long amount, int vatInfo)
            throws Exception {
    }

    public void printRecSubtotal(long amount) throws Exception {
    }

    public void printRecSubtotalAdjustment(int adjustmentType,
            String description, long amount) throws Exception {
    }

    public void printRecTotal(long total, long payment, String description)
            throws Exception {
    }

    public void printRecVoid(String description) throws Exception {
    }

    public void printRecVoidItem(String description, long amount, int quantity,
            int adjustmentType, long adjustment, int vatInfo) throws Exception {
    }

    public void printReport(int reportType, String startNum, String endNum)
            throws Exception {
    }

    public void printXReport() throws Exception {
    }

    public void printZReport() throws Exception {
    }

    public void resetPrinter() throws Exception {
    }

    public void setDate(String date) throws Exception {
    }

    public void setHeaderLine(int lineNumber, String text, boolean doubleWidth)
            throws Exception {
    }

    public void setPOSID(String POSID, String cashierID) throws Exception {
    }

    public void setStoreFiscalID(String ID) throws Exception {
    }

    public void setTrailerLine(int lineNumber, String text, boolean doubleWidth)
            throws Exception {
    }

    public void setVatTable() throws Exception {
    }

    public void setVatValue(int vatID, String vatValue) throws Exception {
    }

    public void verifyItem(String itemName, int vatID) throws Exception {
    }

    public void setCurrency(int newCurrency) throws Exception {
    }

    public void printRecCash(long amount) throws Exception {
    }

    public void printRecItemFuel(String description, long price, int quantity,
            int vatInfo, long unitPrice, String unitName, long specialTax,
            String specialTaxName) throws Exception {
    }

    public void printRecItemFuelVoid(String description, long price,
            int vatInfo, long specialTax) throws Exception {
    }

    public void printRecPackageAdjustment(int adjustmentType,
            String description, String vatAdjustment) throws Exception {
    }

    public void printRecPackageAdjustVoid(int adjustmentType,
            String vatAdjustment) throws Exception {
    }

    public void printRecRefundVoid(String description, long amount, int vatInfo)
            throws Exception {
    }

    public void printRecSubtotalAdjustVoid(int adjustmentType, long amount)
            throws Exception {
    }

    public void printRecTaxID(String taxID) throws Exception {
    }

    public void resetStatistics(String statisticsBuffer) throws Exception {
    }

    public void retrieveStatistics(String[] statisticsBuffer) throws Exception {
    }

    public void updateStatistics(String statisticsBuffer) throws Exception {
    }

    public void compareFirmwareVersion(String firmwareFileName, int[] result)
            throws Exception {
    }

    public void updateFirmware(String firmwareFileName) throws Exception {
    }

    public PrintRecItemVoidRequest printRecItemVoid(
            PrintRecItemVoidRequest request) throws Exception {
        return request;
    }

    public void printRecItemAdjustmentVoid(int adjustmentType,
            String description, long amount, int vatInfo) throws Exception {
    }

    public void printRecItemRefund(String description, long amount,
            int quantity, int vatInfo, long unitAmount, String unitName)
            throws Exception {
    }

    public void printRecItemRefundVoid(String description, long amount,
            int quantity, int vatInfo, long unitAmount, String unitName)
            throws Exception {
    }
}
