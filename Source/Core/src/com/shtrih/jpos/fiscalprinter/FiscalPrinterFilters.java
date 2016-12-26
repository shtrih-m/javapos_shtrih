/*
 * FiscalPrinterFilters.java
 *
 * Created on 13 Январь 2011 г., 12:43
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.shtrih.jpos.fiscalprinter;

/**
 *
 * @author V.Kravtsov
 */
import java.util.Vector;

import com.shtrih.jpos.fiscalprinter.request.PrintRecItemAdjustmentRequest;
import com.shtrih.jpos.fiscalprinter.request.PrintRecItemVoidRequest;

public class FiscalPrinterFilters implements FiscalPrinterFilter113 {

    private final Vector items = new Vector();

    /** Creates a new instance of FiscalPrinterFilters */
    public FiscalPrinterFilters() {
    }

    public void add(FiscalPrinterFilter113 filter) {
        items.add(filter);
    }

    public int size() {
        return items.size();
    }

    public void clear() {
        items.clear();
    }

    public FiscalPrinterFilter113 get(int index) {
        return (FiscalPrinterFilter113) items.get(index);
    }

    public void beginFiscalDocument(int documentAmount) throws Exception {
        for (int i = 0; i < size(); i++) {
            get(i).beginFiscalDocument(documentAmount);
        }
    }

    public void beginFiscalReceipt(boolean printHeader) throws Exception {
        for (int i = 0; i < size(); i++) {
            get(i).beginFiscalReceipt(printHeader);
        }
    }

    public void beginFixedOutput(int station, int documentType)
            throws Exception {
        for (int i = 0; i < size(); i++) {
            get(i).beginFixedOutput(station, documentType);
        }
    }

    public void beginInsertion(int timeout) throws Exception {
        for (int i = 0; i < size(); i++) {
            get(i).beginInsertion(timeout);
        }
    }

    public void beginItemList(int vatID) throws Exception {
        for (int i = 0; i < size(); i++) {
            get(i).beginItemList(vatID);
        }
    }

    public void beginNonFiscal() throws Exception {
        for (int i = 0; i < size(); i++) {
            get(i).beginNonFiscal();
        }
    }

    public void beginRemoval(int timeout) throws Exception {
        for (int i = 0; i < size(); i++) {
            get(i).beginRemoval(timeout);
        }
    }

    public void beginTraining() throws Exception {
        for (int i = 0; i < size(); i++) {
            get(i).beginTraining();
        }
    }

    public void clearError() throws Exception {
        for (int i = 0; i < size(); i++) {
            get(i).clearError();
        }
    }

    public void clearOutput() throws Exception {
        for (int i = 0; i < size(); i++) {
            get(i).clearOutput();
        }
    }

    public void endFiscalDocument() throws Exception {
        for (int i = 0; i < size(); i++) {
            get(i).endFiscalDocument();
        }
    }

    public void endFiscalReceipt(boolean printHeader) throws Exception {
        for (int i = 0; i < size(); i++) {
            get(i).endFiscalReceipt(printHeader);
        }
    }

    public void endFixedOutput() throws Exception {
        for (int i = 0; i < size(); i++) {
            get(i).endFixedOutput();
        }
    }

    public void endInsertion() throws Exception {
        for (int i = 0; i < size(); i++) {
            get(i).endInsertion();
        }
    }

    public void endItemList() throws Exception {
        for (int i = 0; i < size(); i++) {
            get(i).endItemList();
        }
    }

    public void endNonFiscal() throws Exception {
        for (int i = 0; i < size(); i++) {
            get(i).endNonFiscal();
        }
    }

    public void endRemoval() throws Exception {
        for (int i = 0; i < size(); i++) {
            get(i).endRemoval();
        }
    }

    public void endTraining() throws Exception {
        for (int i = 0; i < size(); i++) {
            get(i).endTraining();
        }
    }

    public void getData(int dataItem, int[] optArgs, String[] data)
            throws Exception {
        for (int i = 0; i < size(); i++) {
            get(i).getData(dataItem, optArgs, data);
        }
    }

    public void getDate(String[] Date) throws Exception {
        for (int i = 0; i < size(); i++) {
            get(i).getDate(Date);
        }
    }

    public void getTotalizer(int vatID, int optArgs, String[] data)
            throws Exception {
        for (int i = 0; i < size(); i++) {
            get(i).getTotalizer(vatID, optArgs, data);
        }
    }

    public void getVatEntry(int vatID, int optArgs, int[] vatRate)
            throws Exception {
        for (int i = 0; i < size(); i++) {
            get(i).getVatEntry(vatID, optArgs, vatRate);
        }
    }

    public void printDuplicateReceipt() throws Exception {
        for (int i = 0; i < size(); i++) {
            get(i).printDuplicateReceipt();
        }
    }

    public void printFiscalDocumentLine(String documentLine) throws Exception {
        for (int i = 0; i < size(); i++) {
            get(i).printFiscalDocumentLine(documentLine);
        }
    }

    public void printFixedOutput(int documentType, int lineNumber, String data)
            throws Exception {
        for (int i = 0; i < size(); i++) {
            get(i).printFixedOutput(documentType, lineNumber, data);
        }
    }

    public void printNormal(int station, String data) throws Exception {
        for (int i = 0; i < size(); i++) {
            get(i).printNormal(station, data);
        }
    }

    public void printPeriodicTotalsReport(String date1, String date2)
            throws Exception {
        for (int i = 0; i < size(); i++) {
            get(i).printPeriodicTotalsReport(date1, date2);
        }
    }

    public void printPowerLossReport() throws Exception {
        for (int i = 0; i < size(); i++) {
            get(i).printPowerLossReport();
        }
    }

    public void printRecItem(String description, long price, int quantity,
            int vatInfo, long unitPrice, String unitName) throws Exception {
        for (int i = 0; i < size(); i++) {
            get(i).printRecItem(description, price, quantity, vatInfo,
                    unitPrice, unitName);
        }
    }

    public PrintRecItemAdjustmentRequest printRecItemAdjustment(
            PrintRecItemAdjustmentRequest request) throws Exception {
        for (int i = 0; i < size(); i++) {
            request = get(i).printRecItemAdjustment(request);
        }
        return request;
    }

    public void printRecMessage(String message) throws Exception {
        for (int i = 0; i < size(); i++) {
            get(i).printRecMessage(message);
        }
    }

    public void printRecNotPaid(String description, long amount)
            throws Exception {
        for (int i = 0; i < size(); i++) {
            get(i).printRecNotPaid(description, amount);
        }
    }

    public void printRecRefund(String description, long amount, int vatInfo)
            throws Exception {
        for (int i = 0; i < size(); i++) {
            get(i).printRecRefund(description, amount, vatInfo);
        }
    }

    public void printRecSubtotal(long amount) throws Exception {
        for (int i = 0; i < size(); i++) {
            get(i).printRecSubtotal(amount);
        }
    }

    public void printRecSubtotalAdjustment(int adjustmentType,
            String description, long amount) throws Exception {
        for (int i = 0; i < size(); i++) {
            get(i).printRecSubtotalAdjustment(adjustmentType, description,
                    amount);
        }
    }

    public void printRecTotal(long total, long payment, String description)
            throws Exception {
        for (int i = 0; i < size(); i++) {
            get(i).printRecTotal(total, payment, description);
        }
    }

    public void printRecVoid(String description) throws Exception {
        for (int i = 0; i < size(); i++) {
            get(i).printRecVoid(description);
        }
    }

    public void printRecVoidItem(String description, long amount, int quantity,
            int adjustmentType, long adjustment, int vatInfo) throws Exception {
        for (int i = 0; i < size(); i++) {
            get(i).printRecVoidItem(description, amount, quantity,
                    adjustmentType, adjustment, vatInfo);
        }
    }

    public void printReport(int reportType, String startNum, String endNum)
            throws Exception {
        for (int i = 0; i < size(); i++) {
            get(i).printReport(reportType, startNum, endNum);
        }
    }

    public void printXReport() throws Exception {
        for (int i = 0; i < size(); i++) {
            get(i).printXReport();
        }
    }

    public void printZReport() throws Exception {
        for (int i = 0; i < size(); i++) {
            get(i).printZReport();
        }
    }

    public void resetPrinter() throws Exception {
        for (int i = 0; i < size(); i++) {
            get(i).resetPrinter();
        }
    }

    public void setDate(String date) throws Exception {
        for (int i = 0; i < size(); i++) {
            get(i).setDate(date);
        }
    }

    public void setHeaderLine(int lineNumber, String text, boolean doubleWidth)
            throws Exception {
        for (int i = 0; i < size(); i++) {
            get(i).setHeaderLine(lineNumber, text, doubleWidth);
        }
    }

    public void setPOSID(String POSID, String cashierID) throws Exception {
        for (int i = 0; i < size(); i++) {
            get(i).setPOSID(POSID, cashierID);
        }
    }

    public void setStoreFiscalID(String ID) throws Exception {
        for (int i = 0; i < size(); i++) {
            get(i).setStoreFiscalID(ID);
        }
    }

    public void setTrailerLine(int lineNumber, String text, boolean doubleWidth)
            throws Exception {
        for (int i = 0; i < size(); i++) {
            get(i).setTrailerLine(lineNumber, text, doubleWidth);
        }
    }

    public void setVatTable() throws Exception {
        for (int i = 0; i < size(); i++) {
            get(i).setVatTable();
        }
    }

    public void setVatValue(int vatID, String vatValue) throws Exception {
        for (int i = 0; i < size(); i++) {
            get(i).setVatValue(vatID, vatValue);
        }
    }

    public void verifyItem(String itemName, int vatID) throws Exception {
        for (int i = 0; i < size(); i++) {
            get(i).verifyItem(itemName, vatID);
        }
    }

    public void setCurrency(int newCurrency) throws Exception {
        for (int i = 0; i < size(); i++) {
            get(i).setCurrency(newCurrency);
        }
    }

    public void printRecCash(long amount) throws Exception {
        for (int i = 0; i < size(); i++) {
            get(i).printRecCash(amount);
        }
    }

    public void printRecItemFuel(String description, long price, int quantity,
            int vatInfo, long unitPrice, String unitName, long specialTax,
            String specialTaxName) throws Exception {
        for (int i = 0; i < size(); i++) {
            get(i).printRecItemFuel(description, price, quantity, vatInfo,
                    unitPrice, unitName, specialTax, specialTaxName);
        }
    }

    public void printRecItemFuelVoid(String description, long price,
            int vatInfo, long specialTax) throws Exception {
        for (int i = 0; i < size(); i++) {
            get(i).printRecItemFuelVoid(description, price, vatInfo, specialTax);
        }
    }

    public void printRecPackageAdjustment(int adjustmentType,
            String description, String vatAdjustment) throws Exception {
        for (int i = 0; i < size(); i++) {
            get(i).printRecPackageAdjustment(adjustmentType, description,
                    vatAdjustment);
        }
    }

    public void printRecPackageAdjustVoid(int adjustmentType,
            String vatAdjustment) throws Exception {
        for (int i = 0; i < size(); i++) {
            get(i).printRecPackageAdjustVoid(adjustmentType, vatAdjustment);
        }
    }

    public void printRecRefundVoid(String description, long amount, int vatInfo)
            throws Exception {
        for (int i = 0; i < size(); i++) {
            get(i).printRecRefundVoid(description, amount, vatInfo);
        }
    }

    public void printRecSubtotalAdjustVoid(int adjustmentType, long amount)
            throws Exception {
        for (int i = 0; i < size(); i++) {
            get(i).printRecSubtotalAdjustVoid(adjustmentType, amount);
        }
    }

    public void printRecTaxID(String taxID) throws Exception {
        for (int i = 0; i < size(); i++) {
            get(i).printRecTaxID(taxID);
        }
    }

    public void resetStatistics(String statisticsBuffer) throws Exception {
        for (int i = 0; i < size(); i++) {
            get(i).resetStatistics(statisticsBuffer);
        }
    }

    public void retrieveStatistics(String[] statisticsBuffer) throws Exception {
        for (int i = 0; i < size(); i++) {
            get(i).retrieveStatistics(statisticsBuffer);
        }
    }

    public void updateStatistics(String statisticsBuffer) throws Exception {
        for (int i = 0; i < size(); i++) {
            get(i).updateStatistics(statisticsBuffer);
        }
    }

    public void compareFirmwareVersion(String firmwareFileName, int[] result)
            throws Exception {
        for (int i = 0; i < size(); i++) {
            get(i).compareFirmwareVersion(firmwareFileName, result);
        }
    }

    public void updateFirmware(String firmwareFileName) throws Exception {
        for (int i = 0; i < size(); i++) {
            get(i).updateFirmware(firmwareFileName);
        }
    }

    public PrintRecItemVoidRequest printRecItemVoid(
            PrintRecItemVoidRequest request) throws Exception {
        for (int i = 0; i < size(); i++) {
            request = get(i).printRecItemVoid(request);
        }
        return request;
    }

    public void printRecItemAdjustmentVoid(int adjustmentType,
            String description, long amount, int vatInfo) throws Exception {
        for (int i = 0; i < size(); i++) {
            get(i).printRecItemAdjustmentVoid(adjustmentType, description,
                    amount, vatInfo);
        }
    }

    public void printRecItemRefund(String description, long amount,
            int quantity, int vatInfo, long unitAmount, String unitName)
            throws Exception {
        for (int i = 0; i < size(); i++) {
            get(i).printRecItemRefund(description, amount, quantity, vatInfo,
                    unitAmount, unitName);
        }
    }

    public void printRecItemRefundVoid(String description, long amount,
            int quantity, int vatInfo, long unitAmount, String unitName)
            throws Exception {
        for (int i = 0; i < size(); i++) {
            get(i).printRecItemRefundVoid(description, amount, quantity,
                    vatInfo, unitAmount, unitName);
        }
    }
}
