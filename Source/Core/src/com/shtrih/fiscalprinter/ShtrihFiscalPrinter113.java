package com.shtrih.fiscalprinter;

/*
 * ShtrihFiscalPrinter.java
 *
 * Created on 23 Ноябрь 2009 г., 22:32
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
/**
 * @author V.Kravtsov
 */

import jpos.*;

import java.util.Vector;

import jpos.events.ErrorListener;
import jpos.events.DirectIOListener;
import jpos.events.StatusUpdateListener;
import com.shtrih.barcode.PrinterBarcode;
import com.shtrih.fiscalprinter.command.*;
import jpos.events.OutputCompleteListener;
import com.shtrih.jpos.fiscalprinter.SmFptrConst;

/**
 * Wrapper class to help using directIO codes *
 */
public class ShtrihFiscalPrinter113 implements BaseControl,
        FiscalPrinterControl113, JposConst {

    private final String encoding;
    private final FiscalPrinterControl113 printer;

    /**
     * Creates a new instance of ShtrihFiscalPrinter
     */
    public ShtrihFiscalPrinter113(FiscalPrinterControl113 printer,
                                  String encoding) {
        this.printer = printer;
        this.encoding = encoding;
    }

    public ShtrihFiscalPrinter113(FiscalPrinterControl113 printer) {
        this.printer = printer;
        this.encoding = "";
    }

    public ShtrihFiscalPrinter113(String encoding) {
        this.printer = new FiscalPrinter();
        this.encoding = encoding;
    }

    private String encodeString(String text) throws JposException {
        try {
            if (encoding.equals("")) {
                return text;
            } else {
                return new String(text.getBytes(encoding));
            }
        } catch (Exception e) {
            throw new JposException(JPOS_E_FAILURE, e.getMessage());
        }
    }

    private String decodeString(String text) throws JposException {
        try {
            if (encoding.equals("")) {
                return text;
            } else {
                return new String(text.getBytes(), encoding);
            }
        } catch (Exception e) {
            throw new JposException(JPOS_E_FAILURE, e.getMessage());
        }
    }

    public boolean getCapCompareFirmwareVersion() throws JposException {
        return printer.getCapCompareFirmwareVersion();
    }

    public boolean getCapUpdateFirmware() throws JposException {
        return printer.getCapUpdateFirmware();
    }

    public void compareFirmwareVersion(String firmwareFileName, int[] result)
            throws JposException {
        printer.compareFirmwareVersion(encodeString(firmwareFileName), result);
    }

    public void updateFirmware(String firmwareFileName) throws JposException {
        printer.updateFirmware(encodeString(firmwareFileName));
    }

    // FiscalPrinterControl18
    public boolean getCapStatisticsReporting() throws JposException {
        return printer.getCapStatisticsReporting();
    }

    public boolean getCapUpdateStatistics() throws JposException {
        return printer.getCapUpdateStatistics();
    }

    public void resetStatistics(String statisticsBuffer) throws JposException {
        printer.resetStatistics(encodeString(statisticsBuffer));
    }

    public void retrieveStatistics(String[] statisticsBuffer)
            throws JposException {
        printer.retrieveStatistics(statisticsBuffer);
    }

    public void updateStatistics(String statisticsBuffer) throws JposException {
        printer.updateStatistics(encodeString(statisticsBuffer));
    }

    // FiscalPrinterControl17
    public int getAmountDecimalPlaces() throws JposException {
        return printer.getAmountDecimalPlaces();
    }

    // FiscalPrinterControl16
    public boolean getCapAdditionalHeader() throws JposException {
        return printer.getCapAdditionalHeader();
    }

    public boolean getCapAdditionalTrailer() throws JposException {
        return printer.getCapAdditionalTrailer();
    }

    public boolean getCapChangeDue() throws JposException {
        return printer.getCapChangeDue();
    }

    public boolean getCapEmptyReceiptIsVoidable() throws JposException {
        return printer.getCapEmptyReceiptIsVoidable();
    }

    public boolean getCapFiscalReceiptStation() throws JposException {
        return printer.getCapFiscalReceiptStation();
    }

    public boolean getCapFiscalReceiptType() throws JposException {
        return printer.getCapFiscalReceiptType();
    }

    public boolean getCapMultiContractor() throws JposException {
        return printer.getCapMultiContractor();
    }

    public boolean getCapOnlyVoidLastItem() throws JposException {
        return printer.getCapOnlyVoidLastItem();
    }

    public boolean getCapPackageAdjustment() throws JposException {
        return printer.getCapPackageAdjustment();
    }

    public boolean getCapPostPreLine() throws JposException {
        return printer.getCapPostPreLine();
    }

    public boolean getCapSetCurrency() throws JposException {
        return printer.getCapSetCurrency();
    }

    public boolean getCapTotalizerType() throws JposException {
        return printer.getCapTotalizerType();
    }

    public int getActualCurrency() throws JposException {
        return printer.getActualCurrency();
    }

    public String getAdditionalHeader() throws JposException {
        return decodeString(printer.getAdditionalHeader());
    }

    public void setAdditionalHeader(String additionalHeader)
            throws JposException {
        printer.setAdditionalHeader(encodeString(additionalHeader));
    }

    public String getAdditionalTrailer() throws JposException {
        return decodeString(printer.getAdditionalTrailer());
    }

    public void setAdditionalTrailer(String additionalTrailer)
            throws JposException {
        printer.setAdditionalTrailer(encodeString(additionalTrailer));
    }

    public String getChangeDue() throws JposException {
        return decodeString(printer.getChangeDue());
    }

    public void setChangeDue(String changeDue) throws JposException {
        printer.setChangeDue(encodeString(changeDue));
    }

    public int getContractorId() throws JposException {
        return printer.getContractorId();
    }

    public void setContractorId(int contractorId) throws JposException {
        printer.setContractorId(contractorId);
    }

    public int getDateType() throws JposException {
        return printer.getDateType();
    }

    public void setDateType(int dateType) throws JposException {
        printer.setDateType(dateType);
    }

    public int getFiscalReceiptStation() throws JposException {
        return printer.getFiscalReceiptStation();
    }

    public void setFiscalReceiptStation(int fiscalReceiptStation)
            throws JposException {
        printer.setFiscalReceiptStation(fiscalReceiptStation);
    }

    public int getFiscalReceiptType() throws JposException {
        return printer.getFiscalReceiptType();
    }

    public void setFiscalReceiptType(int fiscalReceiptType)
            throws JposException {
        printer.setFiscalReceiptType(fiscalReceiptType);
    }

    public int getMessageType() throws JposException {
        return printer.getMessageType();
    }

    public void setMessageType(int messageType) throws JposException {
        printer.setMessageType(messageType);
    }

    public String getPostLine() throws JposException {
        return decodeString(printer.getPostLine());
    }

    public void setPostLine(String postLine) throws JposException {
        printer.setPostLine(encodeString(postLine));
    }

    public String getPreLine() throws JposException {
        return decodeString(printer.getPreLine());
    }

    public void setPreLine(String preLine) throws JposException {
        printer.setPreLine(encodeString(preLine));
    }

    public int getTotalizerType() throws JposException {
        return printer.getTotalizerType();
    }

    public void setTotalizerType(int totalizerType) throws JposException {
        printer.setTotalizerType(totalizerType);
    }

    public void setCurrency(int newCurrency) throws JposException {
        printer.setCurrency(newCurrency);
    }

    public void printRecCash(long amount) throws JposException {
        printer.printRecCash(amount);
    }

    public void printRecItemFuel(String description, long price, int quantity,
                                 int vatInfo, long unitPrice, String unitName, long specialTax,
                                 String specialTaxName) throws JposException {
        printer.printRecItemFuel(encodeString(description), price, quantity,
                vatInfo, unitPrice, encodeString(unitName), specialTax,
                encodeString(specialTaxName));
    }

    public void printRecItemFuelVoid(String description, long price,
                                     int vatInfo, long specialTax) throws JposException {
        printer.printRecItemFuelVoid(encodeString(description), price, vatInfo,
                specialTax);
    }

    public void printRecPackageAdjustment(int adjustmentType,
                                          String description, String vatAdjustment) throws JposException {
        printer.printRecPackageAdjustment(adjustmentType,
                encodeString(description), encodeString(vatAdjustment));
    }

    public void printRecPackageAdjustVoid(int adjustmentType,
                                          String vatAdjustment) throws JposException {
        printer.printRecPackageAdjustVoid(adjustmentType,
                encodeString(vatAdjustment));
    }

    public void printRecRefundVoid(String description, long amount, int vatInfo)
            throws JposException {
        printer.printRecRefundVoid(encodeString(description), amount, vatInfo);
    }

    public void printRecSubtotalAdjustVoid(int adjustmentType, long amount)
            throws JposException {
        printer.printRecSubtotalAdjustVoid(adjustmentType, amount);
    }

    public void printRecTaxID(String taxID) throws JposException {
        printer.printRecTaxID(encodeString(taxID));
    }

    // FiscalPrinterControl13
    public boolean getCapAdditionalLines() throws JposException {
        return printer.getCapAdditionalLines();
    }

    public boolean getCapAmountAdjustment() throws JposException {
        return printer.getCapAmountAdjustment();
    }

    public boolean getCapAmountNotPaid() throws JposException {
        return printer.getCapAmountNotPaid();
    }

    public boolean getCapCheckTotal() throws JposException {
        return printer.getCapCheckTotal();
    }

    public boolean getCapCoverSensor() throws JposException {
        return printer.getCapCoverSensor();
    }

    public boolean getCapDoubleWidth() throws JposException {
        return printer.getCapDoubleWidth();
    }

    public boolean getCapDuplicateReceipt() throws JposException {
        return printer.getCapDuplicateReceipt();
    }

    public boolean getCapFixedOutput() throws JposException {
        return printer.getCapFixedOutput();
    }

    public boolean getCapHasVatTable() throws JposException {
        return printer.getCapHasVatTable();
    }

    public boolean getCapIndependentHeader() throws JposException {
        return printer.getCapIndependentHeader();
    }

    public boolean getCapItemList() throws JposException {
        return printer.getCapItemList();
    }

    public boolean getCapJrnEmptySensor() throws JposException {
        return printer.getCapJrnEmptySensor();
    }

    public boolean getCapJrnNearEndSensor() throws JposException {
        return printer.getCapJrnNearEndSensor();
    }

    public boolean getCapJrnPresent() throws JposException {
        return printer.getCapJrnPresent();
    }

    public boolean getCapNonFiscalMode() throws JposException {
        return printer.getCapNonFiscalMode();
    }

    public boolean getCapOrderAdjustmentFirst() throws JposException {
        return printer.getCapOrderAdjustmentFirst();
    }

    public boolean getCapPercentAdjustment() throws JposException {
        return printer.getCapPercentAdjustment();
    }

    public boolean getCapPositiveAdjustment() throws JposException {
        return printer.getCapPositiveAdjustment();
    }

    public boolean getCapPowerLossReport() throws JposException {
        return printer.getCapPowerLossReport();
    }

    public int getCapPowerReporting() throws JposException {
        return printer.getCapPowerReporting();
    }

    public boolean getCapPredefinedPaymentLines() throws JposException {
        return printer.getCapPredefinedPaymentLines();
    }

    public boolean getCapReceiptNotPaid() throws JposException {
        return printer.getCapReceiptNotPaid();
    }

    public boolean getCapRecEmptySensor() throws JposException {
        return printer.getCapRecEmptySensor();
    }

    public boolean getCapRecNearEndSensor() throws JposException {
        return printer.getCapRecNearEndSensor();
    }

    public boolean getCapRecPresent() throws JposException {
        return printer.getCapRecPresent();
    }

    public boolean getCapRemainingFiscalMemory() throws JposException {
        return printer.getCapRemainingFiscalMemory();
    }

    public boolean getCapReservedWord() throws JposException {
        return printer.getCapReservedWord();
    }

    public boolean getCapSetHeader() throws JposException {
        return printer.getCapSetHeader();
    }

    public boolean getCapSetPOSID() throws JposException {
        return printer.getCapSetPOSID();
    }

    public boolean getCapSetStoreFiscalID() throws JposException {
        return printer.getCapSetStoreFiscalID();
    }

    public boolean getCapSetTrailer() throws JposException {
        return printer.getCapSetTrailer();
    }

    public boolean getCapSetVatTable() throws JposException {
        return printer.getCapSetVatTable();
    }

    public boolean getCapSlpEmptySensor() throws JposException {
        return printer.getCapSlpEmptySensor();
    }

    public boolean getCapSlpFiscalDocument() throws JposException {
        return printer.getCapSlpFiscalDocument();
    }

    public boolean getCapSlpFullSlip() throws JposException {
        return printer.getCapSlpFullSlip();
    }

    public boolean getCapSlpNearEndSensor() throws JposException {
        return printer.getCapSlpNearEndSensor();
    }

    public boolean getCapSlpPresent() throws JposException {
        return printer.getCapSlpPresent();
    }

    public boolean getCapSlpValidation() throws JposException {
        return printer.getCapSlpValidation();
    }

    public boolean getCapSubAmountAdjustment() throws JposException {
        return printer.getCapSubAmountAdjustment();
    }

    public boolean getCapSubPercentAdjustment() throws JposException {
        return printer.getCapSubPercentAdjustment();
    }

    public boolean getCapSubtotal() throws JposException {
        return printer.getCapSubtotal();
    }

    public boolean getCapTrainingMode() throws JposException {
        return printer.getCapTrainingMode();
    }

    public boolean getCapValidateJournal() throws JposException {
        return printer.getCapValidateJournal();
    }

    public boolean getCapXReport() throws JposException {
        return printer.getCapXReport();
    }

    // Properties
    public int getAmountDecimalPlace() throws JposException {
        return printer.getAmountDecimalPlace();
    }

    public boolean getAsyncMode() throws JposException {
        return printer.getAsyncMode();
    }

    public void setAsyncMode(boolean asyncMode) throws JposException {
        printer.setAsyncMode(asyncMode);
    }

    public boolean getCheckTotal() throws JposException {
        return printer.getCheckTotal();
    }

    public void setCheckTotal(boolean checkTotal) throws JposException {
        printer.setCheckTotal(checkTotal);
    }

    public int getCountryCode() throws JposException {
        return printer.getCountryCode();
    }

    public boolean getCoverOpen() throws JposException {
        return printer.getCoverOpen();
    }

    public boolean getDayOpened() throws JposException {
        return printer.getDayOpened();
    }

    public int getDescriptionLength() throws JposException {
        return printer.getDescriptionLength();
    }

    public boolean getDuplicateReceipt() throws JposException {
        return printer.getDuplicateReceipt();
    }

    public void setDuplicateReceipt(boolean duplicateReceipt)
            throws JposException {
        printer.setDuplicateReceipt(duplicateReceipt);
    }

    public int getErrorLevel() throws JposException {
        return printer.getErrorLevel();
    }

    public int getErrorOutID() throws JposException {
        return printer.getErrorOutID();
    }

    public int getErrorState() throws JposException {
        return printer.getErrorState();
    }

    public int getErrorStation() throws JposException {
        return printer.getErrorStation();
    }

    public String getErrorString() throws JposException {
        return decodeString(printer.getErrorString());
    }

    public boolean getFlagWhenIdle() throws JposException {
        return printer.getFlagWhenIdle();
    }

    public void setFlagWhenIdle(boolean flagWhenIdle) throws JposException {
        printer.setFlagWhenIdle(flagWhenIdle);
    }

    public boolean getJrnEmpty() throws JposException {
        return printer.getJrnEmpty();
    }

    public boolean getJrnNearEnd() throws JposException {
        return printer.getJrnNearEnd();
    }

    public int getMessageLength() throws JposException {
        return printer.getMessageLength();
    }

    public int getNumHeaderLines() throws JposException {
        return printer.getNumHeaderLines();
    }

    public int getNumTrailerLines() throws JposException {
        return printer.getNumTrailerLines();
    }

    public int getNumVatRates() throws JposException {
        return printer.getNumVatRates();
    }

    public int getOutputID() throws JposException {
        return printer.getOutputID();
    }

    public int getPowerNotify() throws JposException {
        return printer.getPowerNotify();
    }

    public void setPowerNotify(int powerNotify) throws JposException {
        printer.setPowerNotify(powerNotify);
    }

    public int getPowerState() throws JposException {
        return printer.getPowerState();
    }

    public String getPredefinedPaymentLines() throws JposException {
        return decodeString(printer.getPredefinedPaymentLines());
    }

    public int getPrinterState() throws JposException {
        return printer.getPrinterState();
    }

    public int getQuantityDecimalPlaces() throws JposException {
        return printer.getQuantityDecimalPlaces();
    }

    public int getQuantityLength() throws JposException {
        return printer.getQuantityLength();
    }

    public boolean getRecEmpty() throws JposException {
        return printer.getRecEmpty();
    }

    public boolean getRecNearEnd() throws JposException {
        return printer.getRecNearEnd();
    }

    public int getRemainingFiscalMemory() throws JposException {
        return printer.getRemainingFiscalMemory();
    }

    public String getReservedWord() throws JposException {
        return decodeString(printer.getReservedWord());
    }

    public boolean getSlpEmpty() throws JposException {
        return printer.getSlpEmpty();
    }

    public boolean getSlpNearEnd() throws JposException {
        return printer.getSlpNearEnd();
    }

    public int getSlipSelection() throws JposException {
        return printer.getSlipSelection();
    }

    public void setSlipSelection(int slipSelection) throws JposException {
        printer.setSlipSelection(slipSelection);
    }

    public boolean getTrainingModeActive() throws JposException {
        return printer.getTrainingModeActive();
    }

    public void beginFiscalDocument(int documentAmount) throws JposException {
        printer.beginFiscalDocument(documentAmount);
    }

    public void beginFiscalReceipt(boolean printHeader) throws JposException {
        printer.beginFiscalReceipt(printHeader);
    }

    public void beginFixedOutput(int station, int documentType)
            throws JposException {
        printer.beginFixedOutput(station, documentType);
    }

    public void beginInsertion(int timeout) throws JposException {
        printer.beginInsertion(timeout);
    }

    public void beginItemList(int vatID) throws JposException {
        printer.beginItemList(vatID);
    }

    public void beginNonFiscal() throws JposException {
        printer.beginNonFiscal();
    }

    public void beginRemoval(int timeout) throws JposException {
        printer.beginRemoval(timeout);
    }

    public void beginTraining() throws JposException {
        printer.beginTraining();
    }

    public void clearError() throws JposException {
        printer.clearError();
    }

    public void clearOutput() throws JposException {
        printer.clearOutput();
    }

    public void endFiscalDocument() throws JposException {
        printer.endFiscalDocument();
    }

    public void endFiscalReceipt(boolean printHeader) throws JposException {
        printer.endFiscalReceipt(printHeader);
    }

    public void endFixedOutput() throws JposException {
        printer.endFixedOutput();
    }

    public void endInsertion() throws JposException {
        printer.endInsertion();
    }

    public void endItemList() throws JposException {
        printer.endItemList();
    }

    public void endNonFiscal() throws JposException {
        printer.endNonFiscal();
    }

    public void endRemoval() throws JposException {
        printer.endRemoval();
    }

    public void endTraining() throws JposException {
        printer.endTraining();
    }

    public void getData(int dataItem, int[] optArgs, String[] data)
            throws JposException {
        printer.getData(dataItem, optArgs, data);
    }

    public void getDate(String[] Date) throws JposException {
        printer.getDate(Date);
    }

    public void getTotalizer(int vatID, int optArgs, String[] data)
            throws JposException {
        printer.getTotalizer(vatID, optArgs, data);
    }

    public void getVatEntry(int vatID, int optArgs, int[] vatRate)
            throws JposException {
        printer.getVatEntry(vatID, optArgs, vatRate);
    }

    public void printDuplicateReceipt() throws JposException {
        printer.printDuplicateReceipt();
    }

    public void printFiscalDocumentLine(String documentLine)
            throws JposException {
        printer.printFiscalDocumentLine(encodeString(documentLine));
    }

    public void printFixedOutput(int documentType, int lineNumber, String data)
            throws JposException {
        printer.printFixedOutput(documentType, lineNumber, encodeString(data));
    }

    public void printNormal(int station, String data) throws JposException {
        printer.printNormal(station, encodeString(data));
    }

    public void printPeriodicTotalsReport(String date1, String date2)
            throws JposException {
        printer.printPeriodicTotalsReport(encodeString(date1),
                encodeString(date2));
    }

    public void printPowerLossReport() throws JposException {
        printer.printPowerLossReport();
    }

    public void printRecItem(String description, long price, int quantity,
                             int vatInfo, long unitPrice, String unitName) throws JposException {
        printer.printRecItem(encodeString(description), price, quantity,
                vatInfo, unitPrice, encodeString(unitName));
    }

    public void printRecItemAdjustment(int adjustmentType, String description,
                                       long amount, int vatInfo) throws JposException {
        printer.printRecItemAdjustment(adjustmentType,
                encodeString(description), amount, vatInfo);
    }

    public void printRecMessage(String message) throws JposException {
        printer.printRecMessage(encodeString(message));
    }

    public void printRecNotPaid(String description, long amount)
            throws JposException {
        printer.printRecNotPaid(encodeString(description), amount);
    }

    public void printRecRefund(String description, long amount, int vatInfo)
            throws JposException {
        printer.printRecRefund(encodeString(description), amount, vatInfo);
    }

    public void printRecSubtotal(long amount) throws JposException {
        printer.printRecSubtotal(amount);
    }

    public void printRecSubtotalAdjustment(int adjustmentType,
                                           String description, long amount) throws JposException {
        printer.printRecSubtotalAdjustment(adjustmentType,
                encodeString(description), amount);
    }

    public void printRecTotal(long total, long payment, String description)
            throws JposException {
        printer.printRecTotal(total, payment, encodeString(description));
    }

    public void printRecVoid(String description) throws JposException {
        printer.printRecVoid(encodeString(description));
    }

    public void printRecVoidItem(String description, long amount, int quantity,
                                 int adjustmentType, long adjustment, int vatInfo)
            throws JposException {
        printer.printRecVoidItem(encodeString(description), amount, quantity,
                adjustmentType, adjustment, vatInfo);
    }

    public void printReport(int reportType, String startNum, String endNum)
            throws JposException {
        printer.printReport(reportType, encodeString(startNum), endNum);
    }

    public void printXReport() throws JposException {
        printer.printXReport();
    }

    public void printZReport() throws JposException {
        printer.printZReport();
    }

    public void resetPrinter() throws JposException {
        printer.resetPrinter();
    }

    public void setDate(String date) throws JposException {
        printer.setDate(encodeString(date));
    }

    public void cancelIO() throws JposException {
        directIO(SmFptrConst.SMFPTR_DIO_CANCELIO, null, null);
    }

    public String getHeaderLine(int lineNumber) throws JposException {
        int[] data = new int[1];
        String[] lines = new String[1];
        data[0] = lineNumber;
        directIO(SmFptrConst.SMFPTR_DIO_GET_HEADER_LINE, data, lines);
        return lines[0];
    }

    public String getTrailerLine(int lineNumber) throws JposException {
        int[] data = new int[1];
        String[] lines = new String[1];
        data[0] = lineNumber;
        directIO(SmFptrConst.SMFPTR_DIO_GET_TRAILER_LINE, data, lines);
        return lines[0];
    }

    public void setHeaderLine(int lineNumber, String text, boolean doubleWidth)
            throws JposException {
        printer.setHeaderLine(lineNumber, encodeString(text), doubleWidth);
    }

    public void setPOSID(String POSID, String cashierID) throws JposException {
        printer.setPOSID(encodeString(POSID), encodeString(cashierID));
    }

    public void setStoreFiscalID(String ID) throws JposException {
        printer.setStoreFiscalID(encodeString(ID));
    }

    public void setTrailerLine(int lineNumber, String text, boolean doubleWidth)
            throws JposException {
        printer.setTrailerLine(lineNumber, encodeString(text), doubleWidth);
    }

    public void setVatTable() throws JposException {
        printer.setVatTable();
    }

    public void setVatValue(int vatID, String vatValue) throws JposException {
        printer.setVatValue(vatID, encodeString(vatValue));
    }

    public void verifyItem(String itemName, int vatID) throws JposException {
        printer.verifyItem(encodeString(itemName), vatID);
    }

    public void addDirectIOListener(DirectIOListener l) {
        printer.addDirectIOListener(l);
    }

    public void removeDirectIOListener(DirectIOListener l) {
        printer.removeDirectIOListener(l);
    }

    public void addErrorListener(ErrorListener l) {
        printer.addErrorListener(l);
    }

    public void removeErrorListener(ErrorListener l) {
        printer.removeErrorListener(l);
    }

    public void addOutputCompleteListener(OutputCompleteListener l) {
        printer.addOutputCompleteListener(l);
    }

    public void removeOutputCompleteListener(OutputCompleteListener l) {
        printer.removeOutputCompleteListener(l);
    }

    public void addStatusUpdateListener(StatusUpdateListener l) {
        printer.addStatusUpdateListener(l);
    }

    public void removeStatusUpdateListener(StatusUpdateListener l) {
        printer.removeStatusUpdateListener(l);
    }

    // BaseControl
    public String getCheckHealthText() throws JposException {
        return decodeString(printer.getCheckHealthText());
    }

    public boolean getClaimed() throws JposException {
        return printer.getClaimed();
    }

    public String getDeviceControlDescription() {
        return printer.getDeviceControlDescription();
    }

    public int getDeviceControlVersion() {
        return printer.getDeviceControlVersion();
    }

    public boolean getDeviceEnabled() throws JposException {
        return printer.getDeviceEnabled();
    }

    public void setDeviceEnabled(boolean deviceEnabled) throws JposException {
        printer.setDeviceEnabled(deviceEnabled);
    }

    public String getDeviceServiceDescription() throws JposException {
        return decodeString(printer.getDeviceServiceDescription());
    }

    public int getDeviceServiceVersion() throws JposException {
        return printer.getDeviceServiceVersion();
    }

    public boolean getFreezeEvents() throws JposException {
        return printer.getFreezeEvents();
    }

    public void setFreezeEvents(boolean freezeEvents) throws JposException {
        printer.setFreezeEvents(freezeEvents);
    }

    public String getPhysicalDeviceDescription() throws JposException {
        return decodeString(printer.getPhysicalDeviceDescription());
    }

    public String getPhysicalDeviceName() throws JposException {
        return decodeString(printer.getPhysicalDeviceName());
    }

    public int getState() {
        return printer.getState();
    }

    public boolean getCapPositiveSubtotalAdjustment() throws JposException {
        return printer.getCapPositiveSubtotalAdjustment();
    }

    // Methods
    public void claim(int timeout) throws JposException {
        printer.claim(timeout);
    }

    public void close() throws JposException {
        printer.close();
    }

    public void checkHealth(int level) throws JposException {
        printer.checkHealth(level);
    }

    public void directIO(int command, int[] data, Object object)
            throws JposException {
        printer.directIO(command, data, object);
    }

    public void open(String logicalDeviceName) throws JposException {
        printer.open(encodeString(logicalDeviceName));
    }

    public void release() throws JposException {
        printer.release();
    }

    public void printText(String text) throws JposException {
        int data[] = new int[1];
        data[0] = PrinterConst.FONT_NUMBER_NORMAL;
        directIO(SmFptrConst.SMFPTR_DIO_PRINT_TEXT, data, text);
    }

    public void printText(String text, FontNumber font) throws JposException {
        int data[] = new int[1];
        data[0] = font.getValue();
        directIO(SmFptrConst.SMFPTR_DIO_PRINT_TEXT, data, text);
    }

    public void printText(String text, FontNumber font, int station) throws JposException {
        int data[] = new int[2];
        data[0] = font.getValue();
        data[1] = station;
        directIO(SmFptrConst.SMFPTR_DIO_PRINT_TEXT, data, text);
    }

    public void setParameter(int paramType, int paramValue)
            throws JposException {
        int data[] = new int[1];
        String object[] = new String[1];
        data[0] = paramType;
        object[0] = String.valueOf(paramValue);
        directIO(SmFptrConst.SMFPTR_DIO_SET_DRIVER_PARAMETER, data, object);
    }

    public String getParameter(int paramType) throws JposException {
        int data[] = new int[1];
        String object[] = new String[1];
        data[0] = paramType;
        directIO(SmFptrConst.SMFPTR_DIO_GET_DRIVER_PARAMETER, data, object);
        return object[0];
    }

    /**
     * Set number of header lines *
     */
    public void setNumHeaderLines(int value) throws JposException {
        setParameter(SmFptrConst.SMFPTR_DIO_PARAM_NUMHEADERLINES, value);
    }

    /**
     * Set number of trailer lines *
     */
    public void setNumTrailerLines(int value) throws JposException {
        setParameter(SmFptrConst.SMFPTR_DIO_PARAM_NUMTRAILERLINES, value);
    }

    /**
     * Select device (FM or EJ) for printReport method *
     */
    public void setReportDevice(int value) throws JposException {
        setParameter(SmFptrConst.SMFPTR_DIO_PARAM_REPORT_DEVICE, value);
    }

    /**
     * Get device type (FM or EJ) for printReport method *
     */
    public int getReportDevice() throws JposException {
        String value = getParameter(SmFptrConst.SMFPTR_DIO_PARAM_REPORT_DEVICE);
        return Integer.parseInt(value);
    }

    /**
     * Set report type (full or short) for printReport method *
     */
    public void setReportType(int value) throws JposException {
        setParameter(SmFptrConst.SMFPTR_DIO_PARAM_REPORT_TYPE, value);
    }

    /**
     * Set report type (full or short) for printReport method *
     */
    public int getReportType() throws JposException {
        String value = getParameter(SmFptrConst.SMFPTR_DIO_PARAM_REPORT_TYPE);
        return Integer.parseInt(value);
    }

    /**
     * Write tables from file "fileName". File is in UTF-8 *
     */
    public void writeTables(String fileName) throws JposException {
        int[] data = new int[1];
        directIO(SmFptrConst.SMFPTR_DIO_WRITE_TABLES, data, fileName);
    }

    /**
     * Read tables to file "fileName". File is in UTF-8 *
     */
    public void readTables(String fileName) throws JposException {
        int[] data = new int[1];
        directIO(SmFptrConst.SMFPTR_DIO_READ_TABLES, data, fileName);
    }

    /**
     * Get department number *
     */
    public int getDepartment() throws JposException {
        int[] data = new int[1];
        int[] value = new int[1];
        directIO(SmFptrConst.SMFPTR_DIO_GET_DEPARTMENT, data, value);
        return value[0];
    }

    /**
     * Set department number *
     */
    public void setDepartment(int department) throws JposException {
        int[] data = new int[1];
        int[] value = new int[1];
        value[0] = department;
        directIO(SmFptrConst.SMFPTR_DIO_SET_DEPARTMENT, data, value);
    }

    /**
     * Read table value *
     */
    public String readTable(int tableNumber, int rowNumber, int fieldNumber)
            throws JposException {
        String[] params = new String[4];
        params[0] = String.valueOf(tableNumber);
        params[1] = String.valueOf(rowNumber);
        params[2] = String.valueOf(fieldNumber);
        directIO(SmFptrConst.SMFPTR_DIO_READTABLE, null, params);
        return params[3];
    }

    /**
     * Write table value *
     */
    public void writeTable(int tableNumber, int rowNumber, int fieldNumber,
                           String fieldValue) throws JposException {
        String[] params = new String[4];
        params[0] = String.valueOf(tableNumber);
        params[1] = String.valueOf(rowNumber);
        params[2] = String.valueOf(fieldNumber);
        params[3] = fieldValue;
        directIO(SmFptrConst.SMFPTR_DIO_WRITETABLE, null, params);
    }

    /**
     * Read payment name, number = 1..4 *
     */
    public String readPaymentName(int number) throws JposException {
        int[] data = new int[1];
        data[0] = number;
        String[] fieldValue = new String[1];
        directIO(SmFptrConst.SMFPTR_DIO_READ_PAYMENT_NAME, data, fieldValue);
        return fieldValue[0];
    }

    /**
     * Write payment name, number = 1..4 *
     */
    public void writePaymentName(int number, String value) throws JposException {
        int[] data = new int[1];
        data[0] = number;
        String[] fieldValue = new String[1];
        fieldValue[0] = value;
        directIO(SmFptrConst.SMFPTR_DIO_WRITE_PAYMENT_NAME, data, fieldValue);
    }

    /**
     * Read day end required value *
     */
    public boolean readDayEnd() throws JposException {
        int[] data = new int[1];
        directIO(SmFptrConst.SMFPTR_DIO_READ_DAY_END, data, null);
        return data[0] != 0;
    }

    /**
     * Print barcode *
     */
    public void printBarcode(String barcode, String label, int barcodeType,
                             int barcodeHeight, int printType, int barWidth, int textPosition,
                             int textFont, int aspectRatio) throws JposException {
        Object[] params = new Object[9];
        params[0] = barcode; // barcode data
        params[1] = label; // barcode label
        params[2] = new Integer(barcodeType); // barcode type
        params[3] = new Integer(barcodeHeight); // barcode height in pixels
        params[4] = new Integer(printType); // print type
        params[5] = new Integer(barWidth); // barcode bar width in pixels
        params[6] = new Integer(textPosition); // text position
        params[7] = new Integer(textFont); // text font
        params[8] = new Integer(aspectRatio); // narrow to width ratio, 3 by default
        directIO(SmFptrConst.SMFPTR_DIO_PRINT_BARCODE, null, params);
    }

    /**
     * Load image *
     */
    public int loadImage(String fileName) throws JposException {
        int[] data = new int[1];
        String[] command = new String[1];
        command[0] = fileName;
        directIO(SmFptrConst.SMFPTR_DIO_LOAD_IMAGE, data, command);
        return data[0];
    }

    public void printImage(int imageIndex) throws JposException {
        int[] data = new int[1];
        data[0] = imageIndex;
        directIO(SmFptrConst.SMFPTR_DIO_PRINT_IMAGE, data, null);
    }

    public void clearImages() throws JposException {
        int[] data = new int[1];
        directIO(SmFptrConst.SMFPTR_DIO_CLEAR_IMAGES, data, null);
    }

    public void addLogo(int imageIndex, int logoPosition) throws JposException {
        int[] data = new int[2];
        data[0] = imageIndex;
        data[1] = logoPosition;
        directIO(SmFptrConst.SMFPTR_DIO_ADD_LOGO, null, data);
    }

    public int loadLogo(int logoPosition, String fileName) throws JposException {
        int[] data = new int[1];
        data[0] = logoPosition;
        directIO(SmFptrConst.SMFPTR_DIO_LOAD_LOGO, data, fileName);
        return data[0];
    }

    public void clearLogo() throws JposException {
        int[] data = new int[0];
        directIO(SmFptrConst.SMFPTR_DIO_CLEAR_LOGO, data, null);
    }

    public void printLine(int lineType, int lineHeight) throws JposException {
        int[] data = new int[2];
        data[0] = lineHeight;
        data[1] = lineType;
        directIO(SmFptrConst.SMFPTR_DIO_PRINT_LINE, null, data);
    }

    public String readSerial() throws JposException {
        String[] serial = new String[1];
        directIO(SmFptrConst.SMFPTR_DIO_READ_SERIAL, null, serial);
        return serial[0];
    }

    public String readLicense() throws JposException {
        int[] data = new int[1];
        String[] object = new String[1];
        directIO(SmFptrConst.SMFPTR_DIO_READ_LICENSE, data, object);
        return object[0];
    }

    public String readEJSerial() throws JposException {
        String[] serial = new String[1];
        directIO(SmFptrConst.SMFPTR_DIO_READ_EJ_SERIAL, null, serial);
        return serial[0];
    }

    public void openCashDrawer(int drawerNumber) throws JposException {
        int[] data = new int[1];
        data[0] = drawerNumber;
        directIO(SmFptrConst.SMFPTR_DIO_OPEN_DRAWER, data, null);
    }

    public boolean readDrawerState() throws JposException {
        int[] data = new int[1];
        data[0] = 0;
        directIO(SmFptrConst.SMFPTR_DIO_READ_DRAWER_STATE, data, null);
        return data[0] != 0;
    }

    public PrinterStatus readPrinterStatus() throws JposException {
        int[] data = new int[1];
        Object[] object = new Object[1];
        directIO(SmFptrConst.SMFPTR_DIO_READ_PRINTER_STATUS, data, object);
        return (PrinterStatus) object[0];
    }

    public ShortPrinterStatus readShortPrinterStatus() throws JposException {
        Object[] object = new Object[1];
        directIO(SmFptrConst.SMFPTR_DIO_READ_SHORT_STATUS, null, object);
        return (ShortPrinterStatus) object[0];
    }

    public LongPrinterStatus readLongPrinterStatus() throws JposException {
        Object[] object = new Object[1];
        directIO(SmFptrConst.SMFPTR_DIO_READ_LONG_STATUS, null, object);
        return (LongPrinterStatus) object[0];
    }

    public CashRegister readCashRegister(int number) throws JposException {
        int[] data = new int[1];
        Object[] object = new Object[1];
        data[0] = number;
        directIO(SmFptrConst.SMFPTR_DIO_READ_CASH_REG, data, object);
        return (CashRegister) object[0];
    }

    public OperationRegister readOperRegister(int number) throws JposException {
        int[] data = new int[1];
        Object[] object = new Object[1];
        data[0] = number;
        directIO(SmFptrConst.SMFPTR_DIO_READ_OPER_REG, data, object);
        return (OperationRegister) object[0];
    }

    public long getReceiptNumber() throws JposException {
        String[] data = new String[1];
        getData(FiscalPrinterConst.FPTR_GD_RECEIPT_NUMBER, null, data);
        return Long.parseLong(data[0]);
    }

    public void saveXmlZReport(String fileName) throws JposException {
        int[] data = new int[1];
        directIO(SmFptrConst.SMFPTR_DIO_XML_ZREPORT, data, fileName);
    }

    public void saveCsvZReport(String fileName) throws JposException {
        int[] data = new int[1];
        directIO(SmFptrConst.SMFPTR_DIO_CSV_ZREPORT, data, fileName);
    }

    public void executeCommand(PrinterCommand command) throws JposException {
        int[] data = new int[1];
        directIO(SmFptrConst.SMFPTR_DIO_COMMAND_OBJECT, data, command);
    }

    public byte[] executeCommand(byte[] tx, int timeout) throws JposException {
        int[] data = new int[1];
        Object[] object = new Object[2];
        data[0] = timeout;
        object[0] = tx;
        directIO(SmFptrConst.SMFPTR_DIO_COMMAND, data, object);
        byte[] rx = (byte[]) object[1];
        return rx;
    }

    public String executeCommand(int code, int timeout, String inParams)
            throws JposException {
        int[] data = new int[1];
        data[0] = code;
        String[] lines = new String[3];
        lines[0] = String.valueOf(timeout);
        lines[1] = inParams;
        directIO(SmFptrConst.SMFPTR_DIO_STRCOMMAND, data, lines);
        String outParams = lines[2];
        return outParams;
    }

    public void waitForPrinting() throws JposException {
        directIO(SmFptrConst.SMFPTR_DIO_WAIT_PRINT, null, null);
    }

    public void readStatus1() throws JposException {
        byte[] tx = {0x11, 0x1E, 0x00, 0x00, 0x00};
        byte[] rx = executeCommand(tx, 10000);
    }

    public void readStatus2() throws JposException {
        String inParams = "30";
        String outParams = "";
        outParams = executeCommand(0x11, 10000, inParams);
    }

    public int readDayStatus() throws JposException {
        int[] data = new int[1];
        directIO(SmFptrConst.SMFPTR_DIO_READ_DAY_STATUS, data, null);
        return data[0];
    }

    public boolean isReadyFiscal(String[] text) throws JposException {
        int[] data = new int[1];
        directIO(SmFptrConst.SMFPTR_DIO_IS_READY_FISCAL, data, text);
        return data[0] != 0;
    }

    public boolean isReadyNonfiscal(String[] text) throws JposException {
        int[] data = new int[1];
        directIO(SmFptrConst.SMFPTR_DIO_IS_READY_NONFISCAL, data, text);
        return data[0] != 0;
    }

    public int readMaxGraphics() throws JposException {
        int[] data = new int[1];
        directIO(SmFptrConst.SMFPTR_DIO_READ_MAX_GRAPHICS, data, null);
        return data[0];
    }

    /**
     * @return max graphics width in pixels
     */
    public int readMaxGraphicsWidth() throws JposException {
        int[] data = new int[1];
        directIO(SmFptrConst.SMFPTR_DIO_READ_MAX_GRAPHICS_WIDTH, data, null);
        return data[0];
    }

    public String getDailyTotal() throws JposException {
        String[] data = new String[1];
        data[0] = "";
        getData(FiscalPrinterConst.FPTR_GD_DAILY_TOTAL, null, data);
        return data[0];

    }

    public String getDailyTotal(int mode) throws JposException {
        int[] optArgs = new int[1];
        optArgs[0] = mode;
        String[] data = new String[1];
        data[0] = "";
        getData(FiscalPrinterConst.FPTR_GD_DAILY_TOTAL, optArgs, data);
        return data[0];
    }

    public String getGrandTotal(int mode) throws JposException {
        int[] optArgs = new int[1];
        optArgs[0] = mode;
        String[] data = new String[1];
        data[0] = "";
        getData(FiscalPrinterConst.FPTR_GD_GRAND_TOTAL, optArgs, data);
        return data[0];
    }

    public int getTextLength(int fontNumber) throws JposException {
        int[] data = new int[1];
        data[0] = fontNumber;
        directIO(SmFptrConst.SMFPTR_DIO_GET_TEXT_LENGTH, data, null);
        return data[0];
    }

    public String readCashierName() throws JposException {
        String[] lines = new String[1];
        directIO(SmFptrConst.SMFPTR_DIO_READ_CASHIER_NAME, null, lines);
        return lines[0];
    }

    public void writeCashierName(String value) throws JposException {
        String[] lines = new String[1];
        lines[0] = value;
        directIO(SmFptrConst.SMFPTR_DIO_WRITE_CASHIER_NAME, null, lines);
    }

    // cutMode: 0 – full cut, 1 – partial cut
    public void cutPaper(int cutMode) throws JposException {
        int[] data = new int[1];
        data[0] = cutMode;
        directIO(SmFptrConst.SMFPTR_DIO_CUT_PAPER, data, null);
    }

    public void setFontNumber(int value) throws JposException {
        setParameter(SmFptrConst.SMFPTR_DIO_PARAM_FONT_NUMBER, value);
    }

    public int getFontNumber() throws JposException {
        String value = getParameter(SmFptrConst.SMFPTR_DIO_PARAM_FONT_NUMBER);
        return Integer.parseInt(value);
    }

    public void printRecItemVoid(String description, long price, int quantity,
                                 int vatInfo, long unitPrice, String unitName) throws JposException {
        printer.printRecItemVoid(description, price, quantity, vatInfo,
                unitPrice, unitName);
    }

    public void printRecItemAdjustmentVoid(int adjustmentType,
                                           String description, long amount, int vatInfo) throws JposException {
        printer.printRecItemAdjustmentVoid(adjustmentType, description, amount,
                vatInfo);
    }

    public void printRecItemRefund(String description, long amount,
                                   int quantity, int vatInfo, long unitAmount, String unitName)
            throws JposException {
        printer.printRecItemRefund(description, amount, quantity, vatInfo,
                unitAmount, unitName);
    }

    public void printRecItemRefundVoid(String description, long amount,
                                       int quantity, int vatInfo, long unitAmount, String unitName)
            throws JposException {
        printer.printRecItemRefundVoid(description, amount, quantity, vatInfo,
                unitAmount, unitName);
    }

    public void printBarcode(PrinterBarcode barcode) throws JposException {
        printer.directIO(SmFptrConst.SMFPTR_DIO_PRINT_BARCODE_OBJECT,
                null, barcode);
    }

    public void openFiscalDay() throws JposException {
        printer.directIO(SmFptrConst.SMFPTR_DIO_OPEN_DAY, null, null);
    }

    public int getSysPassword() throws JposException {
        String s = getParameter(SmFptrConst.SMFPTR_DIO_PARAM_SYS_PASSWORD);
        return Integer.decode(s).intValue();
    }

    public int getUsrPassword() throws JposException {
        String s = getParameter(SmFptrConst.SMFPTR_DIO_PARAM_USR_PASSWORD);
        return Integer.decode(s).intValue();
    }

    public int getTaxPassword() throws JposException {
        String s = getParameter(SmFptrConst.SMFPTR_DIO_PARAM_TAX_PASSWORD);
        return Integer.decode(s).intValue();
    }

    public void fsReadStatus(FSReadStatus command) throws JposException {
        executeCommand(command);
    }

    public FSStatusInfo fsReadStatus() throws JposException {
        FSReadStatus command = new FSReadStatus();
        command.setSysPassword(getSysPassword());
        executeCommand(command);

        return new FSStatusInfo(
                command.getStatus(),
                command.getDocType(),
                command.isIsDocReceived(),
                command.isIsDayOpened(),
                command.getFlags(),
                new FSDateTime(command.getDate(), command.getTime()),
                command.getFsSerial(),
                command.getDocNumber());
    }

    public void fsReadSerial(FSReadSerial command) throws JposException {
        executeCommand(command);
    }

    public void fsReadExpDate(FSReadExpDate command) throws JposException {
        executeCommand(command);
    }

    public void fsReadVersion(FSReadVersion command) throws JposException {
        executeCommand(command);
    }

    public void fsStartFiscalization(int reportType) throws Exception {
        FSStartFiscalization command = new FSStartFiscalization(getSysPassword(), reportType);
        executeCommand(command);
    }

    public void fsFiscalization(String inn, String rnm, int taxSystemCode, int operationMode) throws JposException {
        FSFiscalization command = new FSFiscalization(getSysPassword(), inn, rnm, taxSystemCode, operationMode);
        executeCommand(command);
    }

    public void fsReFiscalization(String inn, String rnm, int taxSystemCode, int operationMode, int reasonCode) throws JposException {
        FSReFiscalization command = new FSReFiscalization(getSysPassword(), inn, rnm, taxSystemCode, operationMode, reasonCode);
        executeCommand(command);
    }

    public void fsResetState(FSResetState command) throws JposException {
        executeCommand(command);
    }

    public void fsCancelDoc(FSCancelDoc command) throws JposException {
        executeCommand(command);
    }

    public void fsReadFiscalization(FSReadFiscalization command) throws JposException {
        executeCommand(command);
    }

    public void fsFindDocument(FSFindDocument command) throws JposException {
        executeCommand(command);
    }

    public void fsOpenDay(FSOpenDay command) throws JposException {
        executeCommand(command);
    }

    public void fsStartCorrectionReceipt(FSStartCorrectionReceipt command) throws JposException {
        executeCommand(command);
    }

    public void fsPrintCorrectionReceipt(FSPrintCorrectionReceipt command) throws JposException {
        executeCommand(command);
    }

    public void fsPrintCorrectionReceipt(int operation, long amount) throws JposException {
        Object[] params = new Object[2];
        params[0] = new Integer(operation);
        params[1] = new Long(amount);
        directIO(SmFptrConst.SMFPTR_DIO_PRINT_CORRECTION, null, params);
    }

    public void fsPrintCorrectionReceipt2(FSPrintCorrectionReceipt2 command) throws JposException {
        executeCommand(command);
    }

    public void fsPrintCorrectionReceipt3(
            int correctionType,
            int paymentType,
            long total,
            long payments0,
            long payments1,
            long payments2,
            long payments3,
            long payments4,
            long taxTotals0,
            long taxTotals1,
            long taxTotals2,
            long taxTotals3,
            long taxTotals4,
            long taxTotals5,
            int taxSystem,
            Object[] outParams) throws JposException {
        Object[] params = new Object[18];
        params[0] = correctionType;
        params[1] = paymentType;
        params[2] = total;
        params[3] = payments0;
        params[4] = payments1;
        params[5] = payments2;
        params[6] = payments3;
        params[7] = payments4;
        params[8] = taxTotals0;
        params[9] = taxTotals1;
        params[10] = taxTotals2;
        params[11] = taxTotals3;
        params[12] = taxTotals4;
        params[13] = taxTotals5;
        params[14] = taxSystem;
        directIO(SmFptrConst.SMFPTR_DIO_PRINT_CORRECTION2, null, params);
        int receiptNumber = (Integer) params[15];
        int documentNumber = (Integer) params[16];
        long documentDigest = (Long) params[17];
        outParams[0] = receiptNumber;
        outParams[1] = documentNumber;
        outParams[2] = documentDigest;
    }

    public void fsPrintCorrectionReceipt3(
            int correctionType,
            int paymentType,
            long total,
            long payments0,
            long payments1,
            long payments2,
            long payments3,
            long payments4,
            long taxTotals0,
            long taxTotals1,
            long taxTotals2,
            long taxTotals3,
            long taxTotals4,
            long taxTotals5,
            int taxSystem) throws JposException {
        Object[] params = new Object[15];
        params[0] = correctionType;
        params[1] = paymentType;
        params[2] = total;
        params[3] = payments0;
        params[4] = payments1;
        params[5] = payments2;
        params[6] = payments3;
        params[7] = payments4;
        params[8] = taxTotals0;
        params[9] = taxTotals1;
        params[10] = taxTotals2;
        params[11] = taxTotals3;
        params[12] = taxTotals4;
        params[13] = taxTotals5;
        params[14] = taxSystem;
        directIO(SmFptrConst.SMFPTR_DIO_PRINT_CORRECTION2, null, params);
    }

    public void fsStartCalcReport(FSStartCalcReport command) throws JposException {
        executeCommand(command);
    }

    public void fsPrintCalcReport(FSPrintCalcReport command) throws JposException {
        executeCommand(command);
    }

    public void fsPrintCalcReport() throws JposException {
        directIO(SmFptrConst.SMFPTR_DIO_FS_PRINT_CALC_REPORT, null, null);
    }

    public void fsReadCommStatus(FSReadCommStatus command) throws JposException {
        executeCommand(command);
    }

    public FSCommunicationStatus fsReadCommStatus() throws JposException {

        FSReadCommStatus command = new FSReadCommStatus();
        command.setSysPassword(getSysPassword());
        executeCommand(command);

        return new FSCommunicationStatus(
                command.getQueueSize(),
                command.getDocumentNumber(),
                new FSDateTime(command.getDocumentDate(), command.getDocumentTime()),
                command.getStatus(),
                command.getReadStatus());
    }

    public void fsReadDocTicket(FSReadDocTicket command) throws JposException {
        executeCommand(command);
    }

    public void fsStartFiscalClose(FSStartFiscalClose command) throws JposException {
        executeCommand(command);
    }

    public void fsPrintFiscalClose(FSPrintFiscalClose command) throws JposException {
        executeCommand(command);
    }

    public void fsReadDocCount(FSReadDocCount command) throws JposException {
        executeCommand(command);
    }

    public void fsReadDayParameters(FSReadDayParameters command) throws JposException {
        executeCommand(command);
    }

    public void fsStartDayOpen(FSStartDayOpen command) throws JposException {
        executeCommand(command);
    }

    public void fsStartDayClose(FSStartDayClose command) throws JposException {
        executeCommand(command);
    }

    public void fsDayClose(FSDayClose command) throws JposException {
        executeCommand(command);
    }

    public void fsSale(FSSale command) throws JposException {
        executeCommand(command);
    }

    public void fsSale2(FSSale2 command) throws JposException {
        executeCommand(command);
    }

    public void fsCloseReceipt(FSCloseReceipt command) throws JposException {
        executeCommand(command);
    }

    public void fsWriteCustomerEmail(String data) throws JposException {
        directIO(SmFptrConst.SMFPTR_DIO_FS_WRITE_CUSTOMER_EMAIL, null, data);
    }

    public void fsWriteCustomerPhone(String data) throws JposException {
        directIO(SmFptrConst.SMFPTR_DIO_FS_WRITE_CUSTOMER_PHONE, null, data);
    }

    public void fsWriteTLV(byte[] data) throws JposException {
        directIO(SmFptrConst.SMFPTR_DIO_FS_WRITE_TLV, null, data);
    }

    public void fsWriteTag(int tagId, String tagValue) throws Exception {
        int[] data = new int[1];
        data[0] = tagId;
        directIO(SmFptrConst.SMFPTR_DIO_FS_WRITE_TAG, data, tagValue);
    }

    ///////////////////////////////////////////////////////////////////////////
    // 1162	код товарной номенклатуры	
    // Данные в массиве представлены в виде строки, в которой: 
    // первые 4 байта –код справочника; 
    // последующие 8 байт – код группы товаров; 
    // последние 20 байт – код идентификации товара    
    public void fsWriteTag1162(int catId, long groupId, String itemId) throws Exception {
        String[] params = new String[3];
        params[0] = String.valueOf(catId);
        params[1] = String.valueOf(groupId);
        params[2] = itemId;
        directIO(SmFptrConst.SMFPTR_DIO_FS_WRITE_TAG_1162, null, params);
    }

    public void printDocEnd() throws JposException {
        directIO(SmFptrConst.SMFPTR_DIO_PRINT_DOC_END, null, null);
    }

    public void printNonFiscalDoc(String text) throws JposException {
        directIO(SmFptrConst.SMFPTR_DIO_PRINT_NON_FISCAL, null, text);
    }

    public void disablePrint() throws JposException {
        directIO(SmFptrConst.SMFPTR_DIO_FS_DISABLE_PRINT, null, null);
    }

    /**
     * Печать СКЛ за текущую смену
     */
    public void printJournalCurrentDay() throws JposException {
        int[] params = new int[1];
        params[0] = SmFptrConst.SMFPTR_JRN_REPORT_CURRENT_DAY;
        directIO(SmFptrConst.SMFPTR_DIO_PRINT_JOURNAL, null, params);
    }

    /**
     * Печать СКЛ за указанную смену
     */
    public void printJournalDayNumber(int dayNumber) throws JposException {
        int[] params = new int[2];
        params[0] = SmFptrConst.SMFPTR_JRN_REPORT_DAY_NUMBER;
        params[1] = dayNumber;
        directIO(SmFptrConst.SMFPTR_DIO_PRINT_JOURNAL, null, params);
    }

    /**
     * Печать документа из СКЛ с указанным номером
     */
    public void printJournalDocNumber(int docNumber) throws JposException {
        int[] params = new int[2];
        params[0] = SmFptrConst.SMFPTR_JRN_REPORT_DOC_NUMBER;
        params[1] = docNumber;
        directIO(SmFptrConst.SMFPTR_DIO_PRINT_JOURNAL, null, params);
    }

    /**
     * Печать документа из СКЛ с указанным номером
     */
    public void printJournalDocRange(int docNumber1, int docNumber2) throws JposException {
        int[] params = new int[3];
        params[0] = SmFptrConst.SMFPTR_JRN_REPORT_DOC_RANGE;
        params[1] = docNumber1;
        params[2] = docNumber2;
        directIO(SmFptrConst.SMFPTR_DIO_PRINT_JOURNAL, null, params);
    }

    public void setDiscountAmount(int amount) throws Exception {
        directIO(SmFptrConst.SMFPTR_DIO_SET_DISCOUNT_AMOUNT, null,
                new Integer(amount));
    }

    public String[] fsReadTickets(int[] numbers) throws Exception {
        Object[] object = new Object[1];
        directIO(SmFptrConst.SMFPTR_DIO_READ_FS_TICKETS, numbers, object);
        return (String[]) object[0];
    }

    public Vector<FSTicket> fsReadTickets2(int[] numbers) throws Exception {
        Object[] object = new Object[1];
        object[0] = null;
        directIO(SmFptrConst.SMFPTR_DIO_READ_FS_TICKETS2, numbers, object);
        return (Vector<FSTicket>) object[0];
    }

    public String[] fsReadTickets3(int number1) throws Exception {
        Object[] object = new Object[1];
        int[] data = new int[1];
        data[0] = number1;
        directIO(SmFptrConst.SMFPTR_DIO_READ_FS_TICKETS3, data, object);
        return (String[]) object[0];
    }

    public String[] fsReadTickets3(int number1, int count) throws Exception {
        Object[] object = new Object[1];
        int[] data = new int[2];
        data[0] = number1;
        data[1] = count;
        directIO(SmFptrConst.SMFPTR_DIO_READ_FS_TICKETS3, data, object);
        return (String[]) object[0];
    }

    public Vector<FSTicket> fsReadTickets4(int number1) throws Exception {
        int[] data = new int[1];
        data[0] = number1;
        Object[] object = new Object[1];
        object[0] = null;
        directIO(SmFptrConst.SMFPTR_DIO_READ_FS_TICKETS4, data, object);
        return (Vector<FSTicket>) object[0];
    }

    public Vector<FSTicket> fsReadTickets4(int number1, int count) throws Exception {
        int[] data = new int[2];
        data[0] = number1;
        data[1] = count;
        Object[] object = new Object[1];
        object[0] = null;
        directIO(SmFptrConst.SMFPTR_DIO_READ_FS_TICKETS4, data, object);
        return (Vector<FSTicket>) object[0];
    }

    /**
     * Техническое обнуление
     */
    public void technicalReset() throws JposException {
        ResetFM command = new ResetFM();
        executeCommand(command);
    }

    /**
     * Установка даты
     */
    public void writeDate(PrinterDate date) throws Exception {
        SetDateCommand cmd = new SetDateCommand(getSysPassword(), date);
        executeCommand(cmd);
    }

    /**
     * Подтверждение даты
     */
    public void confirmDate(PrinterDate date) throws Exception {
        ConfirmDate cmd = new ConfirmDate();
        cmd.setPassword(getSysPassword());
        cmd.setDate(date);
        executeCommand(cmd);
    }

    /**
     * Установка времени
     */
    public void writeTime(PrinterTime time) throws Exception {
        WriteTime cmd = new WriteTime();
        cmd.setPassword(getSysPassword());
        cmd.setTime(time);
        executeCommand(cmd);
    }

    public void readTotals(long[] totals) throws Exception {
        int[] data = new int[1];
        data[0] = SmFptrConst.FMTOTALS_ALL_FISCALIZATIONS;
        Long[] object = new Long[4];
        directIO(SmFptrConst.SMFPTR_DIO_READ_TOTALS, data, object);

        totals[0] = (Long) object[0];
        totals[1] = (Long) object[1];
        totals[2] = (Long) object[2];
        totals[3] = (Long) object[3];
    }

    public FSDocumentInfo fsFindDocument(int documentNumber) throws Exception {
        FSFindDocument cmd = new FSFindDocument(getSysPassword(), documentNumber);
        executeCommand(cmd);
        return cmd.getDocument();
    }

    public void printRawGraphics(byte[][] data) throws Exception {
        directIO(SmFptrConst.SMFPTR_DIO_PRINT_RAW_GRAPHICS, new int[0], data);
    }

    public void feedPaper(int linesNumber) throws Exception {
        directIO(SmFptrConst.SMFPTR_DIO_FEED_PAPER, new int[]{linesNumber}, null);
    }


    public boolean isFSServiceStarted() throws Exception {
        Boolean[] object = new Boolean[1];
        directIO(SmFptrConst.SMFPTR_DIO_GET_FS_SERVICE_STATE, null, object);

        return object[0];
    }

    public void startFSService() throws Exception {

        int[] data = new int[]{1};

        directIO(SmFptrConst.SMFPTR_DIO_SET_FS_SERVICE_STATE, data, null);
    }

    public void stopFSService() throws Exception {

        int[] data = new int[]{0};

        directIO(SmFptrConst.SMFPTR_DIO_SET_FS_SERVICE_STATE, data, null);
    }
}
