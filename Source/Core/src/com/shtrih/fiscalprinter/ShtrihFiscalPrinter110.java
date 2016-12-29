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
 *
 * @author V.Kravtsov
 */
import jpos.BaseControl;
import jpos.FiscalPrinter;
import jpos.FiscalPrinterConst;
import jpos.FiscalPrinterControl110;
import jpos.JposConst;
import jpos.JposException;
import jpos.events.DirectIOListener;
import jpos.events.ErrorListener;
import jpos.events.OutputCompleteListener;
import jpos.events.StatusUpdateListener;

import com.shtrih.fiscalprinter.command.PrinterStatus;
import com.shtrih.jpos.fiscalprinter.SmFptrConst;

/**
 * Wrapper class to help using directIO codes *
 */
public class ShtrihFiscalPrinter110 implements BaseControl,
        FiscalPrinterControl110, JposConst {

    private final String encoding;
    private final FiscalPrinterControl110 printer;

    /**
     * Creates a new instance of ShtrihFiscalPrinter
     */
    public ShtrihFiscalPrinter110(FiscalPrinterControl110 printer,
            String encoding) {
        this.printer = printer;
        this.encoding = encoding;
    }

    public ShtrihFiscalPrinter110(FiscalPrinterControl110 printer) {
        this.printer = printer;
        this.encoding = "";
    }

    public ShtrihFiscalPrinter110(String encoding) {
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
        directIO(SmFptrConst.SMFPTR_DIO_PRINT_TEXT, data, text);
    }

    public void printText(String text, FontNumber font) throws JposException {
        int data[] = new int[1];
        data[0] = font.getValue();
        directIO(SmFptrConst.SMFPTR_DIO_PRINT_TEXT, data, text);
    }

    public void setParameter(int paramType, int paramValue)
            throws JposException {
        int data[] = new int[1];
        int object[] = new int[1];
        data[0] = paramType;
        object[0] = paramValue;
        directIO(SmFptrConst.SMFPTR_DIO_SET_DRIVER_PARAMETER, data, object);
    }

    public int getParameter(int paramType) throws JposException {
        int data[] = new int[1];
        int object[] = new int[1];
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
        return getParameter(SmFptrConst.SMFPTR_DIO_PARAM_REPORT_DEVICE);
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
        return getParameter(SmFptrConst.SMFPTR_DIO_PARAM_REPORT_TYPE);
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
        int[] data = new int[3];
        data[0] = tableNumber;
        data[1] = rowNumber;
        data[2] = fieldNumber;
        String[] fieldValue = new String[1];
        directIO(SmFptrConst.SMFPTR_DIO_READTABLE, data, fieldValue);
        return fieldValue[0];
    }

    /**
     * Write table value *
     */
    public void writeTable(int tableNumber, int rowNumber, int fieldNumber,
            String fieldValue) throws JposException {
        int[] data = new int[3];
        data[0] = tableNumber;
        data[1] = rowNumber;
        data[2] = fieldNumber;
        String[] value = new String[1];
        value[0] = fieldValue;
        directIO(SmFptrConst.SMFPTR_DIO_WRITETABLE, data, value);
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
        int data[] = new int[7];
        String[] command = new String[2];

        command[0] = barcode; // barcode data
        command[1] = label; // barcode label
        data[0] = barcodeType; // barcode type
        data[1] = barcodeHeight; // barcode height in pixels
        data[2] = printType; // print type
        data[3] = barWidth; // barcode bar width in pixels
        data[4] = textPosition; // text position
        data[5] = textFont; // text font
        data[6] = aspectRatio; // narrow to width ratio, 3 by default
        directIO(SmFptrConst.SMFPTR_DIO_PRINT_BARCODE, data, command);
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
        directIO(SmFptrConst.SMFPTR_DIO_ADD_LOGO, data, null);
    }

    public void clearLogo() throws JposException {
        int[] data = new int[0];
        directIO(SmFptrConst.SMFPTR_DIO_CLEAR_LOGO, data, null);
    }

    public void printLine(int lineType, int lineHeight) throws JposException {
        int[] data = new int[2];
        data[0] = lineHeight;
        data[1] = lineType;
        directIO(SmFptrConst.SMFPTR_DIO_PRINT_LINE, data, null);
    }

    public String readSerial() throws JposException {
        String[] serial = new String[1];
        directIO(SmFptrConst.SMFPTR_DIO_READ_SERIAL, null, serial);
        return serial[0];
    }

    public String readLicense() throws JposException {
        String[] data = new String[1];
        directIO(SmFptrConst.SMFPTR_DIO_READ_LICENSE, null, data);
        return data[0];
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
        int[] data = new int[4];
        directIO(SmFptrConst.SMFPTR_DIO_READ_PRINTER_STATUS, data, null);
        PrinterStatus printerStatus = new PrinterStatus();
        printerStatus.setMode(data[0]);
        printerStatus.setSubmode(data[1]);
        printerStatus.setFlags(data[2]);
        printerStatus.setOperator(data[3]);
        return printerStatus;
    }

    public class CashRegister {

        private final long value;
        private final String text;

        public CashRegister(long value, String text) {
            this.value = value;
            this.text = text;
        }

        public long getValue() {
            return value;
        }

        public String getText() {
            return text;
        }
    }

    public class OperRegister {

        private final long value;
        private final String text;

        public OperRegister(long value, String text) {
            this.value = value;
            this.text = text;
        }

        public long getValue() {
            return value;
        }

        public String getText() {
            return text;
        }
    }

    public CashRegister readCashRegister(int number) throws JposException {
        int[] data = new int[1];
        String[] lines = new String[2];
        data[0] = number;
        directIO(SmFptrConst.SMFPTR_DIO_READ_CASH_REG, data, lines);
        return new CashRegister(Long.parseLong(lines[0]), lines[1]);
    }

    public OperRegister readOperRegister(int number) throws JposException {
        int[] data = new int[1];
        String[] lines = new String[2];
        data[0] = number;
        directIO(SmFptrConst.SMFPTR_DIO_READ_OPER_REG, data, lines);
        return new OperRegister(Long.parseLong(lines[0]), lines[1]);
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
        int[] data = new int[2];
        data[0] = code;
        data[1] = timeout;
        String[] lines = new String[2];
        lines[0] = inParams;
        directIO(SmFptrConst.SMFPTR_DIO_STRCOMMAND, data, lines);
        String outParams = lines[1];
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
        Object[] object = new Object[0];
        directIO(SmFptrConst.SMFPTR_DIO_READ_DAY_STATUS, data, object);
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
        int[] data = new int[2];
        data[0] = fontNumber;
        directIO(SmFptrConst.SMFPTR_DIO_GET_TEXT_LENGTH, data, null);
        return data[1];
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
        return getParameter(SmFptrConst.SMFPTR_DIO_PARAM_FONT_NUMBER);
    }
}
