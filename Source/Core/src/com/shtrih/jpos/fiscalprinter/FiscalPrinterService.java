/////////////////////////////////////////////////////////////////////
//
// BaseJposService.java - Abstract base class for all JavaPOS services.
//
// Modification history
// ------------------------------------------------------------------
// 2007-07-24 JavaPOS Release 1.0                                  VK
//
// This class intended for logging purpores only.
// This class serves for logging mathods, parameters, errors
//
/////////////////////////////////////////////////////////////////////
package com.shtrih.jpos.fiscalprinter;

import java.util.HashMap;
import jpos.JposException;
import jpos.config.JposEntry;
import jpos.services.EventCallbacks;
import jpos.services.FiscalPrinterService113;
import com.shtrih.util.CompositeLogger;
import com.shtrih.jpos.DeviceService;

public class FiscalPrinterService extends DeviceService implements
        FiscalPrinterService113 {

    private long startTime = 0;
    private final FiscalPrinterImpl impl;
    static CompositeLogger logger = CompositeLogger.getLogger(FiscalPrinterService.class);

    public FiscalPrinterService() throws Exception {
        impl = new FiscalPrinterImpl();
    }

    public void deleteInstance() throws JposException {
    }

    public void handleException(Throwable e) throws JposException {
        startTime = System.currentTimeMillis() - startTime;
        String suffix = String.format(", time = %d ms", startTime);
        JposExceptionHandler.handleException(e, suffix);
    }

    public void methodBegin(String text)
    {
        startTime = System.currentTimeMillis();
        logger.debug(text);
    }

    public void methodEnd(String text)
    {
        startTime = System.currentTimeMillis() - startTime;
        logger.debug(String.format("%s, time = %d ms", text, startTime));
    }

    public synchronized boolean getCapCompareFirmwareVersion() throws JposException {
        methodBegin("getCapCompareFirmwareVersion()");
        boolean result = false;
        try {
            result = impl.getCapCompareFirmwareVersion();
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("getCapCompareFirmwareVersion = " + String.valueOf(result));
        return result;
    }

    public synchronized boolean getCapUpdateFirmware() throws JposException {
        methodBegin("getCapUpdateFirmware()");
        boolean result = false;
        try {
            result = impl.getCapUpdateFirmware();
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("getCapUpdateFirmware = " + String.valueOf(result));
        return result;
    }

    public synchronized void compareFirmwareVersion(String firmwareFileName, int[] result)
            throws JposException {
        methodBegin("compareFirmwareVersion("
                + String.valueOf(firmwareFileName) + ", "
                + String.valueOf(result) + ")");
        try {
            impl.compareFirmwareVersion(firmwareFileName, result);
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("compareFirmwareVersion: OK");
    }

    public synchronized void updateFirmware(String firmwareFileName) throws JposException {
        methodBegin("updateFirmware(" + String.valueOf(firmwareFileName) + ")");
        try {
            impl.updateFirmware(firmwareFileName);
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("updateFirmware: OK");
    }

    public synchronized String getCheckHealthText() throws JposException {
        methodBegin("getCheckHealthText()");
        String result = "";
        try {
            result = impl.getCheckHealthText();
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("getCheckHealthText = " + String.valueOf(result));
        return result;
    }



    public synchronized boolean getClaimed() throws JposException {
        methodBegin("getClaimed()");
        boolean result = false;
        try {
            result = impl.getClaimed();
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("getClaimed = " + String.valueOf(result));
        return result;
    }

    public synchronized boolean getDeviceEnabled() throws JposException {
        methodBegin("getDeviceEnabled()");
        boolean result = false;
        try {
            result = impl.getDeviceEnabled();
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("getDeviceEnabled = " + String.valueOf(result));
        return result;
    }

    public synchronized void setDeviceEnabled(boolean deviceEnabled) throws JposException {
        methodBegin("setDeviceEnabled(" + String.valueOf(deviceEnabled) + ")");
        try {
            impl.setDeviceEnabled(deviceEnabled);
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("setDeviceEnabled: OK");
    }

    public synchronized String getDeviceServiceDescription() throws JposException {
        methodBegin("getDeviceServiceDescription()");
        String result = "";
        try {
            result = impl.getDeviceServiceDescription();
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("getDeviceServiceDescription = " + String.valueOf(result));
        return result;
    }

    public synchronized int getDeviceServiceVersion() throws JposException {
        methodBegin("getDeviceServiceVersion()");
        int result = 0;
        try {
            result = impl.getDeviceServiceVersion();
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("getDeviceServiceVersion = " + String.valueOf(result));
        return result;
    }

    public synchronized boolean getFreezeEvents() throws JposException {
        methodBegin("getFreezeEvents()");
        boolean result = false;
        try {
            result = impl.getFreezeEvents();
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("getFreezeEvents = " + String.valueOf(result));
        return result;
    }

    public synchronized void setFreezeEvents(boolean freezeEvents) throws JposException {
        methodBegin("setFreezeEvents(" + String.valueOf(freezeEvents) + ")");
        try {
            impl.setFreezeEvents(freezeEvents);
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("setFreezeEvents: OK");
    }

    public synchronized String getPhysicalDeviceDescription() throws JposException {
        methodBegin("getPhysicalDeviceDescription()");
        String result = "";
        try {
            result = impl.getPhysicalDeviceDescription();
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("getPhysicalDeviceDescription = " + String.valueOf(result));
        return result;
    }

    public synchronized String getPhysicalDeviceName() throws JposException {
        methodBegin("getPhysicalDeviceName()");
        String result = "";
        try {
            result = impl.getPhysicalDeviceName();
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("getPhysicalDeviceName = " + String.valueOf(result));
        return result;
    }

    public synchronized int getState() throws JposException {
        methodBegin("getState()");
        int result = 0;
        try {
            result = impl.getState();
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("getState = " + String.valueOf(result));
        return result;
    }

    public synchronized void claim(int timeout) throws JposException {
        methodBegin("claim(" + String.valueOf(timeout) + ")");
        try {
            impl.claim(timeout);
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("claim: OK");
    }

    public synchronized void close() throws JposException {
        methodBegin("close()");
        try {
            impl.close();
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("close: OK");
    }

    public synchronized void checkHealth(int level) throws JposException {
        methodBegin("checkHealth(" + String.valueOf(level) + ")");
        try {
            impl.checkHealth(level);
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("checkHealth: OK");
    }

    public synchronized void directIO(int command, int[] data, Object object)
            throws JposException {
        methodBegin("directIO(" + getDirectIOCommandText(command) + ", "
                + String.valueOf(data) + ", " + String.valueOf(object) + ")");
        try {
            impl.directIO(command, data, object);
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("directIO: OK");
    }

    public synchronized void setJposEntry(JposEntry entry) {
        jposEntry = entry;
    }

    public synchronized void open(String logicalName, EventCallbacks cb)
            throws JposException {
        methodBegin("open(" + String.valueOf(logicalName) + ")");
        try {
            impl.setJposEntry(jposEntry);
            impl.open(logicalName, cb);
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("open: OK");
    }

    public synchronized void release() throws JposException {
        methodBegin("release()");
        try {
            impl.release();
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("release: OK");
    }

    public synchronized boolean getCapAdditionalLines() throws JposException {
        methodBegin("getCapAdditionalLines()");
        boolean result = false;
        try {
            result = impl.getCapAdditionalLines();
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("getCapAdditionalLines = " + String.valueOf(result));
        return result;
    }

    public synchronized boolean getCapAmountAdjustment() throws JposException {
        methodBegin("getCapAmountAdjustment()");
        boolean result = false;
        try {
            result = impl.getCapAmountAdjustment();
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("getCapAmountAdjustment = " + String.valueOf(result));
        return result;
    }

    public synchronized boolean getCapAmountNotPaid() throws JposException {
        methodBegin("getCapAmountNotPaid()");
        boolean result = false;
        try {
            result = impl.getCapAmountNotPaid();
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("getCapAmountNotPaid = " + String.valueOf(result));
        return result;
    }

    public synchronized boolean getCapCheckTotal() throws JposException {
        methodBegin("getCapCheckTotal()");
        boolean result = false;
        try {
            result = impl.getCapCheckTotal();
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("getCapCheckTotal = " + String.valueOf(result));
        return result;
    }

    public synchronized boolean getCapCoverSensor() throws JposException {
        methodBegin("getCapCoverSensor()");
        boolean result = false;
        try {
            result = impl.getCapCoverSensor();
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("getCapCoverSensor = " + String.valueOf(result));
        return result;
    }

    public synchronized boolean getCapDoubleWidth() throws JposException {
        methodBegin("getCapDoubleWidth()");
        boolean result = false;
        try {
            result = impl.getCapDoubleWidth();
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("getCapDoubleWidth = " + String.valueOf(result));
        return result;
    }

    public synchronized boolean getCapDuplicateReceipt() throws JposException {
        methodBegin("getCapDuplicateReceipt()");
        boolean result = false;
        try {
            result = impl.getCapDuplicateReceipt();
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("getCapDuplicateReceipt = " + String.valueOf(result));
        return result;
    }

    public synchronized void setDuplicateReceipt(boolean duplicateReceipt)
            throws JposException {
        methodBegin("setDuplicateReceipt(" + String.valueOf(duplicateReceipt)
                + ")");
        try {
            impl.setDuplicateReceipt(duplicateReceipt);
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("setDuplicateReceipt: OK");
    }

    public synchronized boolean getCapFixedOutput() throws JposException {
        methodBegin("getCapFixedOutput()");
        boolean result = false;
        try {
            result = impl.getCapFixedOutput();
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("getCapFixedOutput = " + String.valueOf(result));
        return result;
    }

    public synchronized boolean getCapHasVatTable() throws JposException {
        methodBegin("getCapHasVatTable()");
        boolean result = false;
        try {
            result = impl.getCapHasVatTable();
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("getCapHasVatTable = " + String.valueOf(result));
        return result;
    }

    public synchronized boolean getCapIndependentHeader() throws JposException {
        methodBegin("getCapIndependentHeader()");
        boolean result = false;
        try {
            result = impl.getCapIndependentHeader();
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("getCapIndependentHeader = " + String.valueOf(result));
        return result;
    }

    public synchronized boolean getCapItemList() throws JposException {
        methodBegin("getCapItemList()");
        boolean result = false;
        try {
            result = impl.getCapItemList();
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("getCapItemList = " + String.valueOf(result));
        return result;
    }

    public synchronized boolean getCapJrnEmptySensor() throws JposException {
        methodBegin("getCapJrnEmptySensor()");
        boolean result = false;
        try {
            result = impl.getCapJrnEmptySensor();
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("getCapJrnEmptySensor = " + String.valueOf(result));
        return result;
    }

    public synchronized boolean getCapJrnNearEndSensor() throws JposException {
        methodBegin("getCapJrnNearEndSensor()");
        boolean result = false;
        try {
            result = impl.getCapJrnNearEndSensor();
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("getCapJrnNearEndSensor = " + String.valueOf(result));
        return result;
    }

    public synchronized boolean getCapJrnPresent() throws JposException {
        methodBegin("getCapJrnPresent()");
        boolean result = false;
        try {
            result = impl.getCapJrnPresent();
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("getCapJrnPresent = " + String.valueOf(result));
        return result;
    }

    public synchronized boolean getCapNonFiscalMode() throws JposException {
        methodBegin("getCapNonFiscalMode()");
        boolean result = false;
        try {
            result = impl.getCapNonFiscalMode();
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("getCapNonFiscalMode = " + String.valueOf(result));
        return result;
    }

    public synchronized boolean getCapOrderAdjustmentFirst() throws JposException {
        methodBegin("getCapOrderAdjustmentFirst()");
        boolean result = false;
        try {
            result = impl.getCapOrderAdjustmentFirst();
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("getCapOrderAdjustmentFirst = " + String.valueOf(result));
        return result;
    }

    public synchronized boolean getCapPercentAdjustment() throws JposException {
        methodBegin("getCapPercentAdjustment()");
        boolean result = false;
        try {
            result = impl.getCapPercentAdjustment();
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("getCapPercentAdjustment = " + String.valueOf(result));
        return result;
    }

    public synchronized boolean getCapPositiveAdjustment() throws JposException {
        methodBegin("getCapPositiveAdjustment()");
        boolean result = false;
        try {
            result = impl.getCapPositiveAdjustment();
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("getCapPositiveAdjustment = " + String.valueOf(result));
        return result;
    }

    public synchronized boolean getCapPowerLossReport() throws JposException {
        methodBegin("getCapPowerLossReport()");
        boolean result = false;
        try {
            result = impl.getCapPowerLossReport();
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("getCapPowerLossReport = " + String.valueOf(result));
        return result;
    }

    public synchronized int getCapPowerReporting() throws JposException {
        methodBegin("getCapPowerReporting()");
        int result = 0;
        try {
            result = impl.getCapPowerReporting();
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("getCapPowerReporting = " + String.valueOf(result));
        return result;
    }

    public synchronized boolean getCapPredefinedPaymentLines() throws JposException {
        methodBegin("getCapPredefinedPaymentLines()");
        boolean result = false;
        try {
            result = impl.getCapPredefinedPaymentLines();
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("getCapPredefinedPaymentLines = " + String.valueOf(result));
        return result;
    }

    public synchronized boolean getCapReceiptNotPaid() throws JposException {
        methodBegin("getCapReceiptNotPaid()");
        boolean result = false;
        try {
            result = impl.getCapReceiptNotPaid();
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("getCapReceiptNotPaid = " + String.valueOf(result));
        return result;
    }

    public synchronized boolean getCapRecEmptySensor() throws JposException {
        methodBegin("getCapRecEmptySensor()");
        boolean result = false;
        try {
            result = impl.getCapRecEmptySensor();
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("getCapRecEmptySensor = " + String.valueOf(result));
        return result;
    }

    public synchronized boolean getCapRecNearEndSensor() throws JposException {
        methodBegin("getCapRecNearEndSensor()");
        boolean result = false;
        try {
            result = impl.getCapRecNearEndSensor();
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("getCapRecNearEndSensor = " + String.valueOf(result));
        return result;
    }

    public synchronized boolean getCapRecPresent() throws JposException {
        methodBegin("getCapRecPresent()");
        boolean result = false;
        try {
            result = impl.getCapRecPresent();
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("getCapRecPresent = " + String.valueOf(result));
        return result;
    }

    public synchronized boolean getCapRemainingFiscalMemory() throws JposException {
        methodBegin("getCapRemainingFiscalMemory()");
        boolean result = false;
        try {
            result = impl.getCapRemainingFiscalMemory();
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("getCapRemainingFiscalMemory = " + String.valueOf(result));
        return result;
    }

    public synchronized boolean getCapReservedWord() throws JposException {
        methodBegin("getCapReservedWord()");
        boolean result = false;
        try {
            result = impl.getCapReservedWord();
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("getCapReservedWord = " + String.valueOf(result));
        return result;
    }

    public synchronized boolean getCapSetHeader() throws JposException {
        methodBegin("getCapSetHeader()");
        boolean result = false;
        try {
            result = impl.getCapSetHeader();
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("getCapSetHeader = " + String.valueOf(result));
        return result;
    }

    public synchronized boolean getCapSetPOSID() throws JposException {
        methodBegin("getCapSetPOSID()");
        boolean result = false;
        try {
            result = impl.getCapSetPOSID();
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("getCapSetPOSID = " + String.valueOf(result));
        return result;
    }

    public synchronized boolean getCapSetStoreFiscalID() throws JposException {
        methodBegin("getCapSetStoreFiscalID()");
        boolean result = false;
        try {
            result = impl.getCapSetStoreFiscalID();
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("getCapSetStoreFiscalID = " + String.valueOf(result));
        return result;
    }

    public synchronized boolean getCapSetTrailer() throws JposException {
        methodBegin("getCapSetTrailer()");
        boolean result = false;
        try {
            result = impl.getCapSetTrailer();
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("getCapSetTrailer = " + String.valueOf(result));
        return result;
    }

    public synchronized boolean getCapSetVatTable() throws JposException {
        methodBegin("getCapSetVatTable()");
        boolean result = false;
        try {
            result = impl.getCapSetVatTable();
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("getCapSetVatTable = " + String.valueOf(result));
        return result;
    }

    public synchronized boolean getCapSlpEmptySensor() throws JposException {
        methodBegin("getCapSlpEmptySensor()");
        boolean result = false;
        try {
            result = impl.getCapSlpEmptySensor();
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("getCapSlpEmptySensor = " + String.valueOf(result));
        return result;
    }

    public synchronized boolean getCapSlpFiscalDocument() throws JposException {
        methodBegin("getCapSlpFiscalDocument()");
        boolean result = false;
        try {
            result = impl.getCapSlpFiscalDocument();
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("getCapSlpFiscalDocument = " + String.valueOf(result));
        return result;
    }

    public synchronized boolean getCapSlpFullSlip() throws JposException {
        methodBegin("getCapSlpFullSlip()");
        boolean result = false;
        try {
            result = impl.getCapSlpFullSlip();
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("getCapSlpFullSlip = " + String.valueOf(result));
        return result;
    }

    public synchronized boolean getCapSlpNearEndSensor() throws JposException {
        methodBegin("getCapSlpNearEndSensor()");
        boolean result = false;
        try {
            result = impl.getCapSlpNearEndSensor();
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("getCapSlpNearEndSensor = " + String.valueOf(result));
        return result;
    }

    public synchronized boolean getCapSlpPresent() throws JposException {
        methodBegin("getCapSlpPresent()");
        boolean result = false;
        try {
            result = impl.getCapSlpPresent();
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("getCapSlpPresent = " + String.valueOf(result));
        return result;
    }

    public synchronized boolean getCapSlpValidation() throws JposException {
        methodBegin("getCapSlpValidation()");
        boolean result = false;
        try {
            result = impl.getCapSlpValidation();
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("getCapSlpValidation = " + String.valueOf(result));
        return result;
    }

    public synchronized boolean getCapSubAmountAdjustment() throws JposException {
        methodBegin("getCapSubAmountAdjustment()");
        boolean result = false;
        try {
            result = impl.getCapSubAmountAdjustment();
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("getCapSubAmountAdjustment = " + String.valueOf(result));
        return result;
    }

    public synchronized boolean getCapSubPercentAdjustment() throws JposException {
        methodBegin("getCapSubPercentAdjustment()");
        boolean result = false;
        try {
            result = impl.getCapSubPercentAdjustment();
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("getCapSubPercentAdjustment = " + String.valueOf(result));
        return result;
    }

    public synchronized boolean getCapSubtotal() throws JposException {
        methodBegin("getCapSubtotal()");
        boolean result = false;
        try {
            result = impl.getCapSubtotal();
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("getCapSubtotal = " + String.valueOf(result));
        return result;
    }

    public synchronized boolean getCapTrainingMode() throws JposException {
        methodBegin("getCapTrainingMode()");
        boolean result = false;
        try {
            result = impl.getCapTrainingMode();
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("getCapTrainingMode = " + String.valueOf(result));
        return result;
    }

    public synchronized boolean getCapValidateJournal() throws JposException {
        methodBegin("getCapValidateJournal()");
        boolean result = false;
        try {
            result = impl.getCapValidateJournal();
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("getCapValidateJournal = " + String.valueOf(result));
        return result;
    }

    public synchronized boolean getCapXReport() throws JposException {
        methodBegin("getCapXReport()");
        boolean result = false;
        try {
            result = impl.getCapXReport();
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("getCapXReport = " + String.valueOf(result));
        return result;
    }

    public synchronized int getOutputID() throws JposException {
        methodBegin("getOutputID()");
        int result = 0;
        try {
            result = impl.getOutputID();
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("getOutputID = " + String.valueOf(result));
        return result;
    }

    public synchronized int getPowerNotify() throws JposException {
        methodBegin("getPowerNotify()");
        int result = 0;
        try {
            result = impl.getPowerNotify();
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("getPowerNotify = " + String.valueOf(result));
        return result;
    }

    public synchronized void setPowerNotify(int powerNotify) throws JposException {
        methodBegin("setPowerNotify(" + String.valueOf(powerNotify) + ")");
        try {
            impl.setPowerNotify(powerNotify);
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("setPowerNotify: OK");
    }

    public synchronized int getPowerState() throws JposException {
        methodBegin("getPowerState()");
        int result = 0;
        try {
            result = impl.getPowerState();
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("getPowerState = " + String.valueOf(result));
        return result;
    }

    public synchronized int getAmountDecimalPlace() throws JposException {
        methodBegin("getAmountDecimalPlace()");
        int result = 0;
        try {
            result = impl.getAmountDecimalPlace();
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("getAmountDecimalPlace = " + String.valueOf(result));
        return result;
    }

    public synchronized boolean getAsyncMode() throws JposException {
        methodBegin("getAsyncMode()");
        boolean result = false;
        try {
            result = impl.getAsyncMode();
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("getAsyncMode = " + String.valueOf(result));
        return result;
    }

    public synchronized void setAsyncMode(boolean asyncMode) throws JposException {
        methodBegin("setAsyncMode(" + String.valueOf(asyncMode) + ")");
        try {
            impl.setAsyncMode(asyncMode);
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("setAsyncMode: OK");
    }

    public synchronized boolean getCheckTotal() throws JposException {
        methodBegin("getCheckTotal()");
        boolean result = false;
        try {
            result = impl.getCheckTotal();
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("getCheckTotal = " + String.valueOf(result));
        return result;
    }

    public synchronized void setCheckTotal(boolean checkTotal) throws JposException {
        methodBegin("setCheckTotal(" + String.valueOf(checkTotal) + ")");
        try {
            impl.setCheckTotal(checkTotal);
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("setCheckTotal: OK");
    }

    public synchronized int getCountryCode() throws JposException {
        methodBegin("getCountryCode()");
        int result = 0;
        try {
            result = impl.getCountryCode();
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("getCountryCode = " + String.valueOf(result));
        return result;
    }

    public synchronized boolean getCoverOpen() throws JposException {
        methodBegin("getCoverOpen()");
        boolean result = false;
        try {
            result = impl.getCoverOpen();
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("getCoverOpen = " + String.valueOf(result));
        return result;
    }

    public synchronized boolean getDayOpened() throws JposException {
        methodBegin("getDayOpened()");
        boolean result = false;
        try {
            result = impl.getDayOpened();
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("getDayOpened = " + String.valueOf(result));
        return result;
    }

    public synchronized int getDescriptionLength() throws JposException {
        methodBegin("getDescriptionLength()");
        int result = 0;
        try {
            result = impl.getDescriptionLength();
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("getDescriptionLength = " + String.valueOf(result));
        return result;
    }

    public synchronized boolean getDuplicateReceipt() throws JposException {
        methodBegin("getDuplicateReceipt()");
        boolean result = false;
        try {
            result = impl.getDuplicateReceipt();
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("getDuplicateReceipt = " + String.valueOf(result));
        return result;
    }

    public synchronized int getErrorLevel() throws JposException {
        methodBegin("getErrorLevel()");
        int result = 0;
        try {
            result = impl.getErrorLevel();
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("getErrorLevel = " + String.valueOf(result));
        return result;
    }

    public synchronized int getErrorOutID() throws JposException {
        methodBegin("getErrorOutID()");
        int result = 0;
        try {
            result = impl.getErrorOutID();
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("getErrorOutID = " + String.valueOf(result));
        return result;
    }

    public synchronized int getErrorState() throws JposException {
        methodBegin("getErrorState()");
        int result = 0;
        try {
            result = impl.getErrorState();
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("getErrorState = " + String.valueOf(result));
        return result;
    }

    public synchronized int getErrorStation() throws JposException {
        methodBegin("getErrorStation()");
        int result = 0;
        try {
            result = impl.getErrorStation();
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("getErrorStation = " + String.valueOf(result));
        return result;
    }

    public synchronized String getErrorString() throws JposException {
        methodBegin("getErrorString()");
        String result = "";
        try {
            result = impl.getErrorString();
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("getErrorString = " + String.valueOf(result));
        return result;
    }

    public synchronized boolean getFlagWhenIdle() throws JposException {
        methodBegin("getFlagWhenIdle()");
        boolean result = false;
        try {
            result = impl.getFlagWhenIdle();
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("getFlagWhenIdle = " + String.valueOf(result));
        return result;
    }

    public synchronized void setFlagWhenIdle(boolean flagWhenIdle) throws JposException {
        methodBegin("setFlagWhenIdle(" + String.valueOf(flagWhenIdle) + ")");
        try {
            impl.setFlagWhenIdle(flagWhenIdle);
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("setFlagWhenIdle: OK");
    }

    public synchronized boolean getJrnEmpty() throws JposException {
        methodBegin("getJrnEmpty()");
        boolean result = false;
        try {
            result = impl.getJrnEmpty();
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("getJrnEmpty = " + String.valueOf(result));
        return result;
    }

    public synchronized boolean getJrnNearEnd() throws JposException {
        methodBegin("getJrnNearEnd()");
        boolean result = false;
        try {
            result = impl.getJrnNearEnd();
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("getJrnNearEnd = " + String.valueOf(result));
        return result;
    }

    public synchronized int getMessageLength() throws JposException {
        methodBegin("getMessageLength()");
        int result = 0;
        try {
            result = impl.getMessageLength();
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("getMessageLength = " + String.valueOf(result));
        return result;
    }

    public synchronized int getNumHeaderLines() throws JposException {
        methodBegin("getNumHeaderLines()");
        int result = 0;
        try {
            result = impl.getNumHeaderLines();
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("getNumHeaderLines = " + String.valueOf(result));
        return result;
    }

    public synchronized int getNumTrailerLines() throws JposException {
        methodBegin("getNumTrailerLines()");
        int result = 0;
        try {
            result = impl.getNumTrailerLines();
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("getNumTrailerLines = " + String.valueOf(result));
        return result;
    }

    public synchronized int getNumVatRates() throws JposException {
        methodBegin("getNumVatRates()");
        int result = 0;
        try {
            result = impl.getNumVatRates();
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("getNumVatRates = " + String.valueOf(result));
        return result;
    }

    public synchronized String getPredefinedPaymentLines() throws JposException {
        methodBegin("getPredefinedPaymentLines()");
        String result = "";
        try {
            result = impl.getPredefinedPaymentLines();
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("getPredefinedPaymentLines = " + String.valueOf(result));
        return result;
    }

    public synchronized int getPrinterState() throws JposException {
        methodBegin("getPrinterState()");
        int result = 0;
        try {
            result = impl.getPrinterState();
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("getPrinterState = " + String.valueOf(result));
        return result;
    }

    public synchronized int getQuantityDecimalPlaces() throws JposException {
        methodBegin("getQuantityDecimalPlaces()");
        int result = 0;
        try {
            result = impl.getQuantityDecimalPlaces();
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("getQuantityDecimalPlaces = " + String.valueOf(result));
        return result;
    }

    public synchronized int getQuantityLength() throws JposException {
        methodBegin("getQuantityLength()");
        int result = 0;
        try {
            result = impl.getQuantityLength();
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("getQuantityLength = " + String.valueOf(result));
        return result;
    }

    public synchronized boolean getRecEmpty() throws JposException {
        methodBegin("getRecEmpty()");
        boolean result = false;
        try {
            result = impl.getRecEmpty();
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("getRecEmpty = " + String.valueOf(result));
        return result;
    }

    public synchronized boolean getRecNearEnd() throws JposException {
        methodBegin("getRecNearEnd()");
        boolean result = false;
        try {
            result = impl.getRecNearEnd();
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("getRecNearEnd = " + String.valueOf(result));
        return result;
    }

    public synchronized int getRemainingFiscalMemory() throws JposException {
        methodBegin("getRemainingFiscalMemory()");
        int result = 0;
        try {
            result = impl.getRemainingFiscalMemory();
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("getRemainingFiscalMemory = " + String.valueOf(result));
        return result;
    }

    public synchronized String getReservedWord() throws JposException {
        methodBegin("getReservedWord()");
        String result = "";
        try {
            result = impl.getReservedWord();
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("getReservedWord = " + String.valueOf(result));
        return result;
    }

    public synchronized boolean getSlpEmpty() throws JposException {
        methodBegin("getSlpEmpty()");
        boolean result = false;
        try {
            result = impl.getSlpEmpty();
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("getSlpEmpty = " + String.valueOf(result));
        return result;
    }

    public synchronized boolean getSlpNearEnd() throws JposException {
        methodBegin("getSlpNearEnd()");
        boolean result = false;
        try {
            result = impl.getSlpNearEnd();
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("getSlpNearEnd = " + String.valueOf(result));
        return result;
    }

    public synchronized int getSlipSelection() throws JposException {
        methodBegin("getSlipSelection()");
        int result = 0;
        try {
            result = impl.getSlipSelection();
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("getSlipSelection = " + String.valueOf(result));
        return result;
    }

    public synchronized void setSlipSelection(int slipSelection) throws JposException {
        methodBegin("setSlipSelection(" + String.valueOf(slipSelection) + ")");
        try {
            impl.setSlipSelection(slipSelection);
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("setSlipSelection: OK");
    }

    public synchronized boolean getTrainingModeActive() throws JposException {
        methodBegin("getTrainingModeActive()");
        boolean result = false;
        try {
            result = impl.getTrainingModeActive();
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("getTrainingModeActive = " + String.valueOf(result));
        return result;
    }

    public synchronized void beginFiscalDocument(int documentAmount) throws JposException {
        methodBegin("beginFiscalDocument(" + String.valueOf(documentAmount)
                + ")");
        try {
            impl.beginFiscalDocument(documentAmount);
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("beginFiscalDocument: OK");
    }

    public synchronized void beginFiscalReceipt(boolean printHeader) throws JposException {
        methodBegin(String.format("beginFiscalReceipt(%s)",  String.valueOf(printHeader)));
        try {
            impl.beginFiscalReceipt(printHeader);
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("beginFiscalReceipt: OK");
    }

    public synchronized void beginFixedOutput(int station, int documentType)
            throws JposException {
        methodBegin("beginFixedOutput(" + String.valueOf(station) + ", "
                + String.valueOf(documentType) + ")");
        try {
            impl.beginFixedOutput(station, documentType);
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("beginFixedOutput: OK");
    }

    public synchronized void beginInsertion(int timeout) throws JposException {
        methodBegin("beginInsertion(" + String.valueOf(timeout) + ")");
        try {
            impl.beginInsertion(timeout);
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("beginInsertion: OK");
    }

    public synchronized void beginItemList(int vatID) throws JposException {
        methodBegin("beginItemList(" + String.valueOf(vatID) + ")");
        try {
            impl.beginItemList(vatID);
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("beginItemList: OK");
    }

    public synchronized void beginNonFiscal() throws JposException {
        methodBegin("beginNonFiscal()");
        try {
            impl.beginNonFiscal();
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("beginNonFiscal: OK");
    }

    public synchronized void beginRemoval(int timeout) throws JposException {
        methodBegin("beginRemoval(" + String.valueOf(timeout) + ")");
        try {
            impl.beginRemoval(timeout);
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("beginRemoval: OK");
    }

    public synchronized void beginTraining() throws JposException {
        methodBegin("beginTraining()");
        try {
            impl.beginTraining();
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("beginTraining: OK");
    }

    public synchronized void clearError() throws JposException {
        methodBegin("clearError()");
        try {
            impl.clearError();
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("clearError: OK");
    }

    public synchronized void clearOutput() throws JposException {
        methodBegin("clearOutput()");
        try {
            impl.clearOutput();
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("clearOutput: OK");
    }

    public synchronized void endFiscalDocument() throws JposException {
        methodBegin("endFiscalDocument()");
        try {
            impl.endFiscalDocument();
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("endFiscalDocument: OK");
    }

    public synchronized void endFiscalReceipt(boolean printHeader) throws JposException {
        methodBegin("endFiscalReceipt(" + String.valueOf(printHeader) + ")");
        try {
            impl.endFiscalReceipt(printHeader);
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("endFiscalReceipt: OK");
    }

    public synchronized void endFixedOutput() throws JposException {
        methodBegin("endFixedOutput()");
        try {
            impl.endFixedOutput();
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("endFixedOutput: OK");
    }

    public synchronized void endInsertion() throws JposException {
        methodBegin("endInsertion()");
        try {
            impl.endInsertion();
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("endInsertion: OK");
    }

    public synchronized void endItemList() throws JposException {
        methodBegin("endItemList()");
        try {
            impl.endItemList();
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("endItemList: OK");
    }

    public synchronized void endNonFiscal() throws JposException {
        methodBegin("endNonFiscal()");
        try {
            impl.endNonFiscal();
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("endNonFiscal: OK");
    }

    public synchronized void endRemoval() throws JposException {
        methodBegin("endRemoval()");
        try {
            impl.endRemoval();
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("endRemoval: OK");
    }

    public synchronized void endTraining() throws JposException {
        methodBegin("endTraining()");
        try {
            impl.endTraining();
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("endTraining: OK");
    }

    public synchronized void getData(int dataItem, int[] optArgs, String[] data)
            throws JposException {
        methodBegin("getData(" + String.valueOf(dataItem) + ", "
                + String.valueOf(optArgs) + ", " + String.valueOf(data) + ")");
        try {
            impl.getData(dataItem, optArgs, data);
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("getData: OK");
    }

    public synchronized void getDate(String[] Date) throws JposException {
        methodBegin("getDate(" + String.valueOf(Date) + ")");
        try {
            impl.getDate(Date);
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("getDate: OK");
    }

    public synchronized void getTotalizer(int vatID, int optArgs, String[] data)
            throws JposException {
        methodBegin("getTotalizer(" + String.valueOf(vatID) + ", "
                + String.valueOf(optArgs) + ", " + String.valueOf(data) + ")");
        try {
            impl.getTotalizer(vatID, optArgs, data);
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("getTotalizer: OK");
    }

    public synchronized void getVatEntry(int vatID, int optArgs, int[] vatRate)
            throws JposException {
        methodBegin("getVatEntry(" + String.valueOf(vatID) + ", "
                + String.valueOf(optArgs) + ", " + String.valueOf(vatRate)
                + ")");
        try {
            impl.getVatEntry(vatID, optArgs, vatRate);
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("getVatEntry: OK");
    }

    public synchronized void printDuplicateReceipt() throws JposException {
        methodBegin("printDuplicateReceipt()");
        try {
            impl.printDuplicateReceipt();
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("printDuplicateReceipt: OK");
    }

    public synchronized void printFiscalDocumentLine(String documentLine)
            throws JposException {
        methodBegin("printFiscalDocumentLine(" + String.valueOf(documentLine)
                + ")");
        try {
            impl.printFiscalDocumentLine(documentLine);
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("printFiscalDocumentLine: OK");
    }

    public synchronized void printFixedOutput(int documentType, int lineNumber, String data)
            throws JposException {
        methodBegin("printFixedOutput(" + String.valueOf(documentType) + ", "
                + String.valueOf(lineNumber) + ", " + String.valueOf(data)
                + ")");
        try {
            impl.printFixedOutput(documentType, lineNumber, data);
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("printFixedOutput: OK");
    }

    public synchronized void printNormal(int station, String data) throws JposException {
        methodBegin("printNormal(" + String.valueOf(station) + ", "
                + String.valueOf(data) + ")");
        try {
            impl.printNormal(station, data);
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("printNormal: OK");
    }

    public synchronized void printPeriodicTotalsReport(String date1, String date2)
            throws JposException {
        methodBegin("printPeriodicTotalsReport(" + String.valueOf(date1)
                + ", " + String.valueOf(date2) + ")");
        try {
            impl.printPeriodicTotalsReport(date1, date2);
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("printPeriodicTotalsReport: OK");
    }

    public synchronized void printPowerLossReport() throws JposException {
        methodBegin("printPowerLossReport()");
        try {
            impl.printPowerLossReport();
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("printPowerLossReport: OK");
    }

    public synchronized void printRecItem(String description, long price, int quantity,
            int vatInfo, long unitPrice, String unitName) throws JposException {
        methodBegin("printRecItem(" + String.valueOf(description) + ", "
                + String.valueOf(price) + ", " + String.valueOf(quantity)
                + ", " + String.valueOf(vatInfo) + ", "
                + String.valueOf(unitPrice) + ", " + String.valueOf(unitName)
                + ")");
        try {
            impl.printRecItem(description, price, quantity, vatInfo, unitPrice,
                    unitName);
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("printRecItem: OK");
    }

    public synchronized void printRecItemAdjustment(int adjustmentType, String description,
            long amount, int vatInfo) throws JposException {
        methodBegin("printRecItemAdjustment(" + String.valueOf(adjustmentType)
                + ", " + String.valueOf(description) + ", "
                + String.valueOf(amount) + ", " + String.valueOf(vatInfo) + ")");
        try {
            impl.printRecItemAdjustment(adjustmentType, description, amount,
                    vatInfo);
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("printRecItemAdjustment: OK");
    }

    public synchronized void printRecMessage(String message) throws JposException {
        methodBegin("printRecMessage(" + String.valueOf(message) + ")");
        try {
            impl.printRecMessage(message);
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("printRecMessage: OK");
    }

    public synchronized void printRecNotPaid(String description, long amount)
            throws JposException {
        methodBegin("printRecNotPaid(" + String.valueOf(description) + ", "
                + String.valueOf(amount) + ")");
        try {
            impl.printRecNotPaid(description, amount);
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("printRecNotPaid: OK");
    }

    public synchronized void printRecRefund(String description, long amount, int vatInfo)
            throws JposException {
        methodBegin("printRecRefund(" + String.valueOf(description) + ", "
                + String.valueOf(amount) + ", " + String.valueOf(vatInfo) + ")");
        try {
            impl.printRecRefund(description, amount, vatInfo);
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("printRecRefund: OK");
    }

    public synchronized void printRecItemRefund(String description, long amount,
            int quantity, int vatInfo, long unitAmount, String unitName)
            throws JposException {
        methodBegin("printRecItemRefund(" + String.valueOf(description) + ", "
                + String.valueOf(amount) + ", " + String.valueOf(quantity)
                + ", " + String.valueOf(vatInfo) + ", "
                + String.valueOf(unitAmount) + ", " + String.valueOf(unitName)
                + ")");
        try {
            impl.printRecItemRefund(description, amount, quantity, vatInfo,
                    unitAmount, unitName);
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("printRecItemRefund: OK");
    }

    public synchronized void printRecItemRefundVoid(String description, long amount,
            int quantity, int vatInfo, long unitAmount, String unitName)
            throws JposException {
        methodBegin("printRecItemRefundVoid(" + String.valueOf(description)
                + ", " + String.valueOf(amount) + ", "
                + String.valueOf(quantity) + ", " + String.valueOf(vatInfo)
                + ", " + String.valueOf(unitAmount) + ", "
                + String.valueOf(unitName) + ")");
        try {
            impl.printRecItemRefundVoid(description, amount, quantity, vatInfo,
                    unitAmount, unitName);
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("printRecItemRefundVoid: OK");
    }

    public synchronized void printRecItemAdjustmentVoid(int adjustmentType,
            String description, long amount, int vatInfo) throws JposException {
        methodBegin("printRecItemAdjustmentVoid("
                + String.valueOf(adjustmentType) + ", "
                + String.valueOf(description) + ", " + String.valueOf(amount)
                + ", " + String.valueOf(vatInfo) + ")");
        try {
            impl.printRecItemAdjustmentVoid(adjustmentType, description,
                    amount, vatInfo);
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("printRecItemAdjustmentVoid: OK");
    }

    public synchronized void printRecSubtotal(long amount) throws JposException {
        methodBegin("printRecSubtotal(" + String.valueOf(amount) + ")");
        try {
            impl.printRecSubtotal(amount);
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("printRecSubtotal: OK");
    }

    public synchronized void printRecSubtotalAdjustment(int adjustmentType,
            String description, long amount) throws JposException {
        methodBegin("printRecSubtotalAdjustment("
                + String.valueOf(adjustmentType) + ", "
                + String.valueOf(description) + ", " + String.valueOf(amount)
                + ")");
        try {
            impl.printRecSubtotalAdjustment(adjustmentType, description, amount);
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("printRecSubtotalAdjustment: OK");
    }

    public synchronized void printRecTotal(long total, long payment, String description)
            throws JposException {
        methodBegin("printRecTotal(" + String.valueOf(total) + ", "
                + String.valueOf(payment) + ", " + String.valueOf(description)
                + ")");
        try {
            impl.printRecTotal(total, payment, description);
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("printRecTotal: OK");
    }

    public synchronized void printRecVoid(String description) throws JposException {
        methodBegin("printRecVoid(" + String.valueOf(description) + ")");
        try {
            impl.printRecVoid(description);
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("printRecVoid: OK");
    }

    public synchronized void printRecVoidItem(String description, long amount, int quantity,
            int adjustmentType, long adjustment, int vatInfo)
            throws JposException {
        methodBegin("printRecVoidItem(" + String.valueOf(description) + ", "
                + String.valueOf(amount) + ", " + String.valueOf(quantity)
                + ", " + String.valueOf(adjustmentType) + ", "
                + String.valueOf(adjustment) + ", " + String.valueOf(vatInfo)
                + ")");
        try {
            impl.printRecVoidItem(description, amount, quantity,
                    adjustmentType, adjustment, vatInfo);
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("printRecVoidItem: OK");
    }

    public synchronized void printReport(int reportType, String startNum, String endNum)
            throws JposException {
        methodBegin("printReport(" + String.valueOf(reportType) + ", "
                + String.valueOf(startNum) + ", " + String.valueOf(endNum)
                + ")");
        try {
            impl.printReport(reportType, startNum, endNum);
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("printReport: OK");
    }

    public synchronized void printXReport() throws JposException {
        methodBegin("printXReport()");
        try {
            impl.printXReport();
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("printXReport: OK");
    }

    public synchronized void printZReport() throws JposException {
        methodBegin("printZReport()");
        try {
            impl.printZReport();
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("printZReport: OK");
    }

    public synchronized void resetPrinter() throws JposException {
        methodBegin("resetPrinter()");
        try {
            impl.resetPrinter();
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("resetPrinter: OK");
    }

    public synchronized void setDate(String date) throws JposException {
        methodBegin("setDate(" + String.valueOf(date) + ")");
        try {
            impl.setDate(date);
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("setDate: OK");
    }

    public synchronized void setHeaderLine(int lineNumber, String text, boolean doubleWidth)
            throws JposException {
        methodBegin("setHeaderLine(" + String.valueOf(lineNumber) + ", \""
                + String.valueOf(text) + "\", " + String.valueOf(doubleWidth)
                + ")");
        try {
            impl.setHeaderLine(lineNumber, text, doubleWidth);
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("setHeaderLine: OK");
    }

    public synchronized void setPOSID(String POSID, String cashierID) throws JposException {
        methodBegin("setPOSID(" + String.valueOf(POSID) + ", "
                + String.valueOf(cashierID) + ")");
        try {
            impl.setPOSID(POSID, cashierID);
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("setPOSID: OK");
    }

    public synchronized void setStoreFiscalID(String ID) throws JposException {
        methodBegin("setStoreFiscalID(" + String.valueOf(ID) + ")");
        try {
            impl.setStoreFiscalID(ID);
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("setStoreFiscalID: OK");
    }

    public synchronized void setTrailerLine(int lineNumber, String text, boolean doubleWidth)
            throws JposException {
        methodBegin("setTrailerLine(" + String.valueOf(lineNumber) + ", \""
                + String.valueOf(text) + "\", " + String.valueOf(doubleWidth)
                + ")");
        try {
            impl.setTrailerLine(lineNumber, text, doubleWidth);
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("setTrailerLine: OK");
    }

    public synchronized void setVatTable() throws JposException {
        methodBegin("setVatTable()");
        try {
            impl.setVatTable();
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("setVatTable: OK");
    }

    public synchronized void setVatValue(int vatID, String vatValue) throws JposException {
        methodBegin("setVatValue(" + String.valueOf(vatID) + ", "
                + String.valueOf(vatValue) + ")");
        try {
            impl.setVatValue(vatID, vatValue);
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("setVatValue: OK");
    }

    public synchronized void verifyItem(String itemName, int vatID) throws JposException {
        methodBegin("verifyItem(" + String.valueOf(itemName) + ", "
                + String.valueOf(vatID) + ")");
        try {
            impl.verifyItem(itemName, vatID);
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("verifyItem: OK");
    }

    public synchronized boolean getCapAdditionalHeader() throws JposException {
        methodBegin("getCapAdditionalHeader()");
        boolean result = false;
        try {
            result = impl.getCapAdditionalHeader();
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("getCapAdditionalHeader = " + String.valueOf(result));
        return result;
    }

    public synchronized boolean getCapAdditionalTrailer() throws JposException {
        methodBegin("getCapAdditionalTrailer()");
        boolean result = false;
        try {
            result = impl.getCapAdditionalTrailer();
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("getCapAdditionalTrailer = " + String.valueOf(result));
        return result;
    }

    public synchronized boolean getCapChangeDue() throws JposException {
        methodBegin("getCapChangeDue()");
        boolean result = false;
        try {
            result = impl.getCapChangeDue();
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("getCapChangeDue = " + String.valueOf(result));
        return result;
    }

    public synchronized boolean getCapEmptyReceiptIsVoidable() throws JposException {
        methodBegin("getCapEmptyReceiptIsVoidable()");
        boolean result = false;
        try {
            result = impl.getCapEmptyReceiptIsVoidable();
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("getCapEmptyReceiptIsVoidable = " + String.valueOf(result));
        return result;
    }

    public synchronized boolean getCapFiscalReceiptStation() throws JposException {
        methodBegin("getCapFiscalReceiptStation()");
        boolean result = false;
        try {
            result = impl.getCapFiscalReceiptStation();
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("getCapFiscalReceiptStation = " + String.valueOf(result));
        return result;
    }

    public synchronized boolean getCapFiscalReceiptType() throws JposException {
        methodBegin("getCapFiscalReceiptType()");
        boolean result = false;
        try {
            result = impl.getCapFiscalReceiptType();
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("getCapFiscalReceiptType = " + String.valueOf(result));
        return result;
    }

    public synchronized boolean getCapMultiContractor() throws JposException {
        methodBegin("getCapMultiContractor()");
        boolean result = false;
        try {
            result = impl.getCapMultiContractor();
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("getCapMultiContractor = " + String.valueOf(result));
        return result;
    }

    public synchronized boolean getCapOnlyVoidLastItem() throws JposException {
        methodBegin("getCapOnlyVoidLastItem()");
        boolean result = false;
        try {
            result = impl.getCapOnlyVoidLastItem();
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("getCapOnlyVoidLastItem = " + String.valueOf(result));
        return result;
    }

    public synchronized boolean getCapPackageAdjustment() throws JposException {
        methodBegin("getCapPackageAdjustment()");
        boolean result = false;
        try {
            result = impl.getCapPackageAdjustment();
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("getCapPackageAdjustment = " + String.valueOf(result));
        return result;
    }

    public synchronized boolean getCapPostPreLine() throws JposException {
        methodBegin("getCapPostPreLine()");
        boolean result = false;
        try {
            result = impl.getCapPostPreLine();
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("getCapPostPreLine = " + String.valueOf(result));
        return result;
    }

    public synchronized boolean getCapSetCurrency() throws JposException {
        methodBegin("getCapSetCurrency()");
        boolean result = false;
        try {
            result = impl.getCapSetCurrency();
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("getCapSetCurrency = " + String.valueOf(result));
        return result;
    }

    public synchronized boolean getCapTotalizerType() throws JposException {
        methodBegin("getCapTotalizerType()");
        boolean result = false;
        try {
            result = impl.getCapTotalizerType();
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("getCapTotalizerType = " + String.valueOf(result));
        return result;
    }

    public synchronized int getActualCurrency() throws JposException {
        methodBegin("getActualCurrency()");
        int result = 0;
        try {
            result = impl.getActualCurrency();
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("getActualCurrency = " + String.valueOf(result));
        return result;
    }

    public synchronized String getAdditionalHeader() throws JposException {
        methodBegin("getAdditionalHeader()");
        String result = "";
        try {
            result = impl.getAdditionalHeader();
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("getAdditionalHeader = " + String.valueOf(result));
        return result;
    }

    public synchronized void setAdditionalHeader(String additionalHeader)
            throws JposException {
        methodBegin("setAdditionalHeader(" + String.valueOf(additionalHeader)
                + ")");
        try {
            impl.setAdditionalHeader(additionalHeader);
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("setAdditionalHeader: OK");
    }

    public synchronized String getAdditionalTrailer() throws JposException {
        methodBegin("getAdditionalTrailer()");
        String result = "";
        try {
            result = impl.getAdditionalTrailer();
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("getAdditionalTrailer = " + String.valueOf(result));
        return result;
    }

    public synchronized void setAdditionalTrailer(String additionalTrailer)
            throws JposException {
        methodBegin("setAdditionalTrailer("
                + String.valueOf(additionalTrailer) + ")");
        try {
            impl.setAdditionalTrailer(additionalTrailer);
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("setAdditionalTrailer: OK");
    }

    public synchronized String getChangeDue() throws JposException {
        methodBegin("getChangeDue()");
        String result = "";
        try {
            result = impl.getChangeDue();
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("getChangeDue = " + String.valueOf(result));
        return result;
    }

    public synchronized void setChangeDue(String changeDue) throws JposException {
        methodBegin("setChangeDue(" + String.valueOf(changeDue) + ")");
        try {
            impl.setChangeDue(changeDue);
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("setChangeDue: OK");
    }

    public synchronized int getContractorId() throws JposException {
        methodBegin("getContractorId()");
        int result = 0;
        try {
            result = impl.getContractorId();
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("getContractorId = " + String.valueOf(result));
        return result;
    }

    public synchronized void setContractorId(int contractorId) throws JposException {
        methodBegin("setContractorId(" + String.valueOf(contractorId) + ")");
        try {
            impl.setContractorId(contractorId);
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("setContractorId: OK");
    }

    public synchronized int getDateType() throws JposException {
        methodBegin("getDateType()");
        int result = 0;
        try {
            result = impl.getDateType();
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("getDateType = " + String.valueOf(result));
        return result;
    }

    public synchronized void setDateType(int dateType) throws JposException {
        methodBegin("setDateType(" + String.valueOf(dateType) + ")");
        try {
            impl.setDateType(dateType);
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("setDateType: OK");
    }

    public synchronized int getFiscalReceiptStation() throws JposException {
        methodBegin("getFiscalReceiptStation()");
        int result = 0;
        try {
            result = impl.getFiscalReceiptStation();
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("getFiscalReceiptStation = " + String.valueOf(result));
        return result;
    }

    public synchronized void setFiscalReceiptStation(int fiscalReceiptStation)
            throws JposException {
        methodBegin("setFiscalReceiptStation("
                + String.valueOf(fiscalReceiptStation) + ")");
        try {
            impl.setFiscalReceiptStation(fiscalReceiptStation);
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("setFiscalReceiptStation: OK");
    }

    public synchronized int getFiscalReceiptType() throws JposException {
        methodBegin("getFiscalReceiptType()");
        int result = 0;
        try {
            result = impl.getFiscalReceiptType();
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("getFiscalReceiptType = " + String.valueOf(result));
        return result;
    }

    public synchronized void setFiscalReceiptType(int fiscalReceiptType)
            throws JposException {
        methodBegin("setFiscalReceiptType("
                + String.valueOf(fiscalReceiptType) + ")");
        try {
            impl.setFiscalReceiptType(fiscalReceiptType);
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("setFiscalReceiptType: OK");
    }

    public synchronized int getMessageType() throws JposException {
        methodBegin("getMessageType()");
        int result = 0;
        try {
            result = impl.getMessageType();
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("getMessageType = " + String.valueOf(result));
        return result;
    }

    public synchronized void setMessageType(int messageType) throws JposException {
        methodBegin("setMessageType(" + String.valueOf(messageType) + ")");
        try {
            impl.setMessageType(messageType);
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("setMessageType: OK");
    }

    public synchronized String getPostLine() throws JposException {
        methodBegin("getPostLine()");
        String result = "";
        try {
            result = impl.getPostLine();
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("getPostLine = " + String.valueOf(result));
        return result;
    }

    public synchronized void setPostLine(String postLine) throws JposException {
        methodBegin("setPostLine(" + String.valueOf(postLine) + ")");
        try {
            impl.setPostLine(postLine);
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("setPostLine: OK");
    }

    public synchronized String getPreLine() throws JposException {
        methodBegin("getPreLine()");
        String result = "";
        try {
            result = impl.getPreLine();
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("getPreLine = " + String.valueOf(result));
        return result;
    }

    public synchronized void setPreLine(String preLine) throws JposException {
        methodBegin("setPreLine(" + String.valueOf(preLine) + ")");
        try {
            impl.setPreLine(preLine);
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("setPreLine: OK");
    }

    public synchronized int getTotalizerType() throws JposException {
        methodBegin("getTotalizerType()");
        int result = 0;
        try {
            result = impl.getTotalizerType();
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("getTotalizerType = " + String.valueOf(result));
        return result;
    }

    public synchronized void setTotalizerType(int totalizerType) throws JposException {
        methodBegin("setTotalizerType(" + String.valueOf(totalizerType) + ")");
        try {
            impl.setTotalizerType(totalizerType);
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("setTotalizerType: OK");
    }

    public synchronized void setCurrency(int newCurrency) throws JposException {
        methodBegin("setCurrency(" + String.valueOf(newCurrency) + ")");
        try {
            impl.setCurrency(newCurrency);
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("setCurrency: OK");
    }

    public synchronized void printRecCash(long amount) throws JposException {
        methodBegin("printRecCash(" + String.valueOf(amount) + ")");
        try {
            impl.printRecCash(amount);
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("printRecCash: OK");
    }

    public synchronized void printRecItemFuel(String description, long price, int quantity,
            int vatInfo, long unitPrice, String unitName, long specialTax,
            String specialTaxName) throws JposException {
        methodBegin("printRecItemFuel(" + String.valueOf(description) + ", "
                + String.valueOf(price) + ", " + String.valueOf(quantity)
                + ", " + String.valueOf(vatInfo) + ", "
                + String.valueOf(unitPrice) + ", " + String.valueOf(unitName)
                + ", " + String.valueOf(specialTax) + ", "
                + String.valueOf(specialTaxName) + ")");
        try {
            impl.printRecItemFuel(description, price, quantity, vatInfo,
                    unitPrice, unitName, specialTax, specialTaxName);
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("printRecItemFuel: OK");
    }

    public synchronized void printRecItemFuelVoid(String description, long price,
            int vatInfo, long specialTax) throws JposException {
        methodBegin("printRecItemFuelVoid(" + String.valueOf(description)
                + ", " + String.valueOf(price) + ", " + String.valueOf(vatInfo)
                + ", " + String.valueOf(specialTax) + ")");
        try {
            impl.printRecItemFuelVoid(description, price, vatInfo, specialTax);
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("printRecItemFuelVoid: OK");
    }

    public synchronized void printRecPackageAdjustment(int adjustmentType,
            String description, String vatAdjustment) throws JposException {
        methodBegin("printRecPackageAdjustment("
                + String.valueOf(adjustmentType) + ", "
                + String.valueOf(description) + ", "
                + String.valueOf(vatAdjustment) + ")");
        try {
            impl.printRecPackageAdjustment(adjustmentType, description,
                    vatAdjustment);
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("printRecPackageAdjustment: OK");
    }

    public synchronized void printRecPackageAdjustVoid(int adjustmentType,
            String vatAdjustment) throws JposException {
        methodBegin("printRecPackageAdjustVoid("
                + String.valueOf(adjustmentType) + ", "
                + String.valueOf(vatAdjustment) + ")");
        try {
            impl.printRecPackageAdjustVoid(adjustmentType, vatAdjustment);
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("printRecPackageAdjustVoid: OK");
    }

    public synchronized void printRecRefundVoid(String description, long amount, int vatInfo)
            throws JposException {
        methodBegin("printRecRefundVoid(" + String.valueOf(description) + ", "
                + String.valueOf(amount) + ", " + String.valueOf(vatInfo) + ")");
        try {
            impl.printRecRefundVoid(description, amount, vatInfo);
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("printRecRefundVoid: OK");
    }

    public synchronized void printRecSubtotalAdjustVoid(int adjustmentType, long amount)
            throws JposException {
        methodBegin("printRecSubtotalAdjustVoid("
                + String.valueOf(adjustmentType) + ", "
                + String.valueOf(amount) + ")");
        try {
            impl.printRecSubtotalAdjustVoid(adjustmentType, amount);
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("printRecSubtotalAdjustVoid: OK");
    }

    public synchronized void printRecTaxID(String taxID) throws JposException {
        methodBegin("printRecTaxID(" + String.valueOf(taxID) + ")");
        try {
            impl.printRecTaxID(taxID);
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("printRecTaxID: OK");
    }

    public synchronized int getAmountDecimalPlaces() throws JposException {
        methodBegin("getAmountDecimalPlaces()");
        int result = 0;
        try {
            result = impl.getAmountDecimalPlaces();
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("getAmountDecimalPlaces = " + String.valueOf(result));
        return result;
    }

    public synchronized boolean getCapStatisticsReporting() throws JposException {
        methodBegin("getCapStatisticsReporting()");
        boolean result = false;
        try {
            result = impl.getCapStatisticsReporting();
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("getCapStatisticsReporting = " + String.valueOf(result));
        return result;
    }

    public synchronized boolean getCapUpdateStatistics() throws JposException {
        methodBegin("getCapUpdateStatistics()");
        boolean result = false;
        try {
            result = impl.getCapUpdateStatistics();
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("getCapUpdateStatistics = " + String.valueOf(result));
        return result;
    }

    public synchronized void resetStatistics(String statisticsBuffer) throws JposException {
        methodBegin("resetStatistics(" + String.valueOf(statisticsBuffer)
                + ")");
        try {
            impl.resetStatistics(statisticsBuffer);
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("resetStatistics: OK");
    }

    public synchronized void retrieveStatistics(String[] statisticsBuffer)
            throws JposException {
        methodBegin(String.format("retrieveStatistics(%s)", String.valueOf(statisticsBuffer)));
        try {
            impl.retrieveStatistics(statisticsBuffer);
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("retrieveStatistics: OK");
    }

    public synchronized void updateStatistics(String statisticsBuffer) throws JposException {
        methodBegin(String.format("updateStatistics(%s)", statisticsBuffer));
        try {
            impl.updateStatistics(statisticsBuffer);
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("updateStatistics: OK");
    }

    public synchronized void printRecItemVoid(String description, long price, int quantity,
            int vatInfo, long unitPrice, String unitName) throws JposException
    {
        methodBegin(String.format("printRecItemVoid(\"%s\", %d, %d, %d, %d, \"%s\")",
                description, price, quantity, vatInfo, unitPrice, unitName));
        try {
            impl.printRecItemVoid(description, price, quantity, vatInfo,
                    unitPrice, unitName);

        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd("printRecItemVoid: OK");
    }

    public synchronized boolean getCapPositiveSubtotalAdjustment() throws JposException {
        methodBegin("getCapPositiveSubtotalAdjustment()");
        boolean result = false;
        try {
            result = impl.getCapPositiveSubtotalAdjustment();
        } catch (Throwable e) {
            handleException(e);
        }
        methodEnd(String.format("getCapPositiveSubtotalAdjustment = ", result));
        return result;
    }

    public synchronized String getDirectIOCommandText(int command) {
        HashMap<Integer, String> map = new HashMap<Integer, String>();
        map.put(SmFptrConst.SMFPTR_DIO_COMMAND,
                "SMFPTR_DIO_COMMAND");
        map.put(SmFptrConst.SMFPTR_DIO_PRINT_BARCODE_OBJECT,
                "SMFPTR_DIO_PRINT_BARCODE_OBJECT");
        map.put(SmFptrConst.SMFPTR_DIO_SET_DEPARTMENT,
                "SMFPTR_DIO_SET_DEPARTMENT");
        map.put(SmFptrConst.SMFPTR_DIO_GET_DEPARTMENT,
                "SMFPTR_DIO_GET_DEPARTMENT");
        map.put(SmFptrConst.SMFPTR_DIO_STRCOMMAND,
                "SMFPTR_DIO_GET_DEPARTMENT");
        map.put(SmFptrConst.SMFPTR_DIO_READTABLE,
                "SMFPTR_DIO_READTABLE");
        map.put(SmFptrConst.SMFPTR_DIO_WRITETABLE,
                "SMFPTR_DIO_WRITETABLE");
        map.put(SmFptrConst.SMFPTR_DIO_READ_PAYMENT_NAME,
                "SMFPTR_DIO_READ_PAYMENT_NAME");
        map.put(SmFptrConst.SMFPTR_DIO_WRITE_PAYMENT_NAME,
                "SMFPTR_DIO_WRITE_PAYMENT_NAME");
        map.put(SmFptrConst.SMFPTR_DIO_READ_DAY_END,
                "SMFPTR_DIO_READ_DAY_END");
        map.put(SmFptrConst.SMFPTR_DIO_PRINT_BARCODE,
                "SMFPTR_DIO_PRINT_BARCODE");
        map.put(SmFptrConst.SMFPTR_DIO_LOAD_IMAGE,
                "SMFPTR_DIO_LOAD_IMAGE");
        map.put(SmFptrConst.SMFPTR_DIO_PRINT_IMAGE,
                "SMFPTR_DIO_PRINT_IMAGE");
        map.put(SmFptrConst.SMFPTR_DIO_CLEAR_IMAGES,
                "SMFPTR_DIO_CLEAR_IMAGES");
        map.put(SmFptrConst.SMFPTR_DIO_ADD_LOGO,
                "SMFPTR_DIO_ADD_LOGO");
        map.put(SmFptrConst.SMFPTR_DIO_CLEAR_LOGO,
                "SMFPTR_DIO_CLEAR_LOGO");
        map.put(SmFptrConst.SMFPTR_DIO_PRINT_LINE,
                "SMFPTR_DIO_PRINT_LINE");
        map.put(SmFptrConst.SMFPTR_DIO_GET_DRIVER_PARAMETER,
                "SMFPTR_DIO_GET_DRIVER_PARAMETER");
        map.put(SmFptrConst.SMFPTR_DIO_SET_DRIVER_PARAMETER,
                "SMFPTR_DIO_SET_DRIVER_PARAMETER");
        map.put(SmFptrConst.SMFPTR_DIO_PRINT_TEXT,
                "SMFPTR_DIO_PRINT_TEXT");
        map.put(SmFptrConst.SMFPTR_DIO_WRITE_TABLES,
                "SMFPTR_DIO_WRITE_TABLES");
        map.put(SmFptrConst.SMFPTR_DIO_READ_TABLES,
                "SMFPTR_DIO_READ_TABLES");
        map.put(SmFptrConst.SMFPTR_DIO_READ_SERIAL,
                "SMFPTR_DIO_READ_SERIAL");
        map.put(SmFptrConst.SMFPTR_DIO_READ_EJ_SERIAL,
                "SMFPTR_DIO_READ_EJ_SERIAL");
        map.put(SmFptrConst.SMFPTR_DIO_OPEN_DRAWER,
                "SMFPTR_DIO_OPEN_DRAWER");
        map.put(SmFptrConst.SMFPTR_DIO_READ_DRAWER_STATE,
                "SMFPTR_DIO_READ_DRAWER_STATE");
        map.put(SmFptrConst.SMFPTR_DIO_READ_PRINTER_STATUS,
                "SMFPTR_DIO_READ_PRINTER_STATUS");
        map.put(SmFptrConst.SMFPTR_DIO_READ_CASH_REG,
                "SMFPTR_DIO_READ_CASH_REG");
        map.put(SmFptrConst.SMFPTR_DIO_READ_OPER_REG,
                "SMFPTR_DIO_READ_OPER_REG");
        map.put(SmFptrConst.SMFPTR_DIO_COMMAND_OBJECT,
                "SMFPTR_DIO_COMMAND_OBJECT");
        map.put(SmFptrConst.SMFPTR_DIO_XML_ZREPORT,
                "SMFPTR_DIO_XML_ZREPORT");
        map.put(SmFptrConst.SMFPTR_DIO_CSV_ZREPORT,
                "SMFPTR_DIO_CSV_ZREPORT");
        map.put(SmFptrConst.SMFPTR_DIO_WRITE_DEVICE_PARAMETER,
                "SMFPTR_DIO_WRITE_DEVICE_PARAMETER");
        map.put(SmFptrConst.SMFPTR_DIO_READ_DEVICE_PARAMETER,
                "SMFPTR_DIO_READ_DEVICE_PARAMETER");
        map.put(SmFptrConst.SMFPTR_DIO_LOAD_LOGO,
                "SMFPTR_DIO_LOAD_LOGO");
        map.put(SmFptrConst.SMFPTR_DIO_READ_DAY_STATUS,
                "SMFPTR_DIO_READ_DAY_STATUS");
        map.put(SmFptrConst.SMFPTR_DIO_READ_LICENSE,
                "SMFPTR_DIO_READ_LICENSE");
        map.put(SmFptrConst.SMFPTR_DIO_IS_READY_FISCAL,
                "SMFPTR_DIO_IS_READY_FISCAL");
        map.put(SmFptrConst.SMFPTR_DIO_IS_READY_NONFISCAL,
                "SMFPTR_DIO_IS_READY_NONFISCAL");
        map.put(SmFptrConst.SMFPTR_DIO_READ_MAX_GRAPHICS,
                "SMFPTR_DIO_READ_MAX_GRAPHICS");
        map.put(SmFptrConst.SMFPTR_DIO_GET_HEADER_LINE,
                "SMFPTR_DIO_GET_HEADER_LINE");
        map.put(SmFptrConst.SMFPTR_DIO_GET_TRAILER_LINE,
                "SMFPTR_DIO_GET_TRAILER_LINE");
        map.put(SmFptrConst.SMFPTR_DIO_GET_TEXT_LENGTH,
                "SMFPTR_DIO_GET_TEXT_LENGTH");
        map.put(SmFptrConst.SMFPTR_DIO_READ_CASHIER_NAME, 
                "SMFPTR_DIO_READ_CASHIER_NAME");
        map.put(SmFptrConst.SMFPTR_DIO_WRITE_CASHIER_NAME,
                "SMFPTR_DIO_WRITE_CASHIER_NAME");
        map.put(SmFptrConst.SMFPTR_DIO_CUT_PAPER, 
                "SMFPTR_DIO_CUT_PAPER");
        map.put(SmFptrConst.SMFPTR_DIO_WAIT_PRINT, 
                "SMFPTR_DIO_WAIT_PRINT");
        map.put(SmFptrConst.SMFPTR_DIO_GET_RECEIPT_STATE,
                "SMFPTR_DIO_GET_RECEIPT_STATE");
        map.put(SmFptrConst.SMFPTR_DIO_READ_SHORT_STATUS,
                "SMFPTR_DIO_READ_SHORT_STATUS");
        map.put(SmFptrConst.SMFPTR_DIO_READ_LONG_STATUS, 
                "SMFPTR_DIO_READ_LONG_STATUS");
        map.put(SmFptrConst.SMFPTR_DIO_CANCELIO, 
                "SMFPTR_DIO_CANCELIO");
        map.put(SmFptrConst.SMFPTR_DIO_OPEN_DAY, 
                "SMFPTR_DIO_OPEN_DAY");
        map.put(SmFptrConst.SMFPTR_DIO_FS_WRITE_TAG, 
                "SMFPTR_DIO_FS_WRITE_TAG");
        map.put(SmFptrConst.SMFPTR_DIO_PRINT_DOC_END, 
                "SMFPTR_DIO_PRINT_DOC_END");
        map.put(SmFptrConst.SMFPTR_DIO_FS_WRITE_TLV, 
                "SMFPTR_DIO_FS_WRITE_TLV");
        map.put(SmFptrConst.SMFPTR_DIO_FS_DISABLE_PRINT_ONCE, 
                "SMFPTR_DIO_FS_DISABLE_PRINT_ONCE");
        map.put(SmFptrConst.SMFPTR_DIO_FS_DISABLE_PRINT, 
                "SMFPTR_DIO_FS_DISABLE_PRINT");
        map.put(SmFptrConst.SMFPTR_DIO_FS_ENABLE_PRINT, 
                "SMFPTR_DIO_FS_ENABLE_PRINT");
        map.put(SmFptrConst.SMFPTR_DIO_PRINT_NON_FISCAL, 
                "SMFPTR_DIO_PRINT_NON_FISCAL");
        map.put(SmFptrConst.SMFPTR_DIO_FS_WRITE_CUSTOMER_EMAIL, 
                "SMFPTR_DIO_FS_WRITE_CUSTOMER_EMAIL");
        map.put(SmFptrConst.SMFPTR_DIO_FS_WRITE_CUSTOMER_PHONE, 
                "SMFPTR_DIO_FS_WRITE_CUSTOMER_PHONE");
        map.put(SmFptrConst.SMFPTR_DIO_FS_PRINT_CALC_REPORT, 
                "SMFPTR_DIO_FS_PRINT_CALC_REPORT");
        map.put(SmFptrConst.SMFPTR_DIO_PRINT_JOURNAL, 
                "SMFPTR_DIO_PRINT_JOURNAL");
        map.put(SmFptrConst.SMFPTR_DIO_SET_DISCOUNT_AMOUNT, 
                "SMFPTR_DIO_SET_DISCOUNT_AMOUNT");
        map.put(SmFptrConst.SMFPTR_DIO_READ_FS_PARAMS, 
                "SMFPTR_DIO_READ_FS_PARAMS");
        map.put(SmFptrConst.SMFPTR_DIO_READ_FS_TICKETS, 
                "SMFPTR_DIO_READ_FS_TICKETS");
        map.put(SmFptrConst.SMFPTR_DIO_READ_FS_TICKETS2, 
                "SMFPTR_DIO_READ_FS_TICKETS2");
        map.put(SmFptrConst.SMFPTR_DIO_READ_FS_TICKETS3, 
                "SMFPTR_DIO_READ_FS_TICKETS3");
        map.put(SmFptrConst.SMFPTR_DIO_READ_FS_TICKETS4, 
                "SMFPTR_DIO_READ_FS_TICKETS4");
        map.put(SmFptrConst.SMFPTR_DIO_PRINT_CORRECTION2, 
                "SMFPTR_DIO_PRINT_CORRECTION2");
        map.put(SmFptrConst.SMFPTR_DIO_READ_TOTALS, 
                "SMFPTR_DIO_READ_TOTALS");
        map.put(SmFptrConst.SMFPTR_DIO_PRINT_RAW_GRAPHICS, 
                "SMFPTR_DIO_PRINT_RAW_GRAPHICS");
        map.put(SmFptrConst.SMFPTR_DIO_READ_MAX_GRAPHICS_WIDTH, 
                "SMFPTR_DIO_READ_MAX_GRAPHICS_WIDTH");
        map.put(SmFptrConst.SMFPTR_DIO_PRINT_CORRECTION, 
                "SMFPTR_DIO_PRINT_CORRECTION");
        map.put(SmFptrConst.SMFPTR_DIO_FEED_PAPER, 
                "SMFPTR_DIO_FEED_PAPER");
        map.put(SmFptrConst.SMFPTR_DIO_GET_FS_SERVICE_STATE, 
                "SMFPTR_DIO_GET_FS_SERVICE_STATE");
        map.put(SmFptrConst.SMFPTR_DIO_SET_FS_SERVICE_STATE, 
                "SMFPTR_DIO_SET_FS_SERVICE_STATE");
        map.put(SmFptrConst.SMFPTR_DIO_CONTINUE_PRINT, 
                "SMFPTR_DIO_CONTINUE_PRINT");
        map.put(SmFptrConst.SMFPTR_DIO_FS_READ_DOCUMENT_TLV, 
                "SMFPTR_DIO_FS_READ_DOCUMENT_TLV");
        map.put(SmFptrConst.SMFPTR_DIO_FS_READ_DAY_OPEN, 
                "SMFPTR_DIO_FS_READ_DAY_OPEN");
        map.put(SmFptrConst.SMFPTR_DIO_FS_READ_DAY_CLOSE, 
                "SMFPTR_DIO_FS_READ_DAY_CLOSE");
        map.put(SmFptrConst.SMFPTR_DIO_FS_READ_RECEIPT, 
                "SMFPTR_DIO_FS_READ_RECEIPT");
        map.put(SmFptrConst.SMFPTR_DIO_FS_READ_STATUS, 
                "SMFPTR_DIO_FS_READ_STATUS");
        map.put(SmFptrConst.SMFPTR_DIO_FS_FIND_DOCUMENT, 
                "SMFPTR_DIO_FS_FIND_DOCUMENT");
        map.put(SmFptrConst.SMFPTR_DIO_FS_DISABLE_DOCEND, 
                "SMFPTR_DIO_FS_DISABLE_DOCEND");
        map.put(SmFptrConst.SMFPTR_DIO_FS_READ_FISCALIZATION_TAG, 
                "SMFPTR_DIO_FS_READ_FISCALIZATION_TAG");
        map.put(SmFptrConst.SMFPTR_DIO_FS_READ_FISCALIZATION_TLV, 
                "SMFPTR_DIO_FS_READ_FISCALIZATION_TLV");
        map.put(SmFptrConst.SMFPTR_DIO_READ_DEVICE_METRICS, 
                "SMFPTR_DIO_READ_DEVICE_METRICS");
        map.put(SmFptrConst.SMFPTR_DIO_READ_TOTALIZER, 
                "SMFPTR_DIO_READ_TOTALIZER");
        map.put(SmFptrConst.SMFPTR_DIO_FS_READ_DOCUMENT_TLV_TEXT, 
                "SMFPTR_DIO_FS_READ_DOCUMENT_TLV_TEXT");
        map.put(SmFptrConst.SMFPTR_DIO_FS_WRITE_OPERATION_TLV, 
                "SMFPTR_DIO_FS_WRITE_OPERATION_TLV");
        map.put(SmFptrConst.SMFPTR_DIO_READ_EJ_DOCUMENT, 
                "SMFPTR_DIO_READ_EJ_DOCUMENT");
        map.put(SmFptrConst.SMFPTR_DIO_START_DAY_CLOSE, 
                "SMFPTR_DIO_START_DAY_CLOSE");
        map.put(SmFptrConst.SMFPTR_DIO_START_DAY_OPEN, 
                "SMFPTR_DIO_START_DAY_OPEN");
        map.put(SmFptrConst.SMFPTR_DIO_START_FISCALIZATION, 
                "SMFPTR_DIO_START_FISCALIZATION");
        map.put(SmFptrConst.SMFPTR_DIO_START_CALC_REPORT, 
                "SMFPTR_DIO_START_CALC_REPORT");
        map.put(SmFptrConst.SMFPTR_DIO_START_FISCAL_CLOSE, 
                "SMFPTR_DIO_START_FISCAL_CLOSE");
        map.put(SmFptrConst.SMFPTR_DIO_SEND_ITEM_CODE, 
                "SMFPTR_DIO_SEND_ITEM_CODE");
        map.put(SmFptrConst.SMFPTR_DIO_CHECK_ITEM_CODE, 
                "SMFPTR_DIO_CHECK_ITEM_CODE");
        map.put(SmFptrConst.SMFPTR_DIO_ACCEPT_ITEM_CODE, 
                "SMFPTR_DIO_ACCEPT_ITEM_CODE");
        map.put(SmFptrConst.SMFPTR_DIO_BIND_ITEM_CODE, 
                "SMFPTR_DIO_BIND_ITEM_CODE");
        map.put(SmFptrConst.SMFPTR_DIO_READ_MC_NOTIFICATION_STATUS, 
                "SMFPTR_DIO_READ_KM_SERVER_STATUS");
        map.put(SmFptrConst.SMFPTR_DIO_SET_ITEM_CODE, 
                "SMFPTR_DIO_SET_ITEM_CODE");
        map.put(SmFptrConst.SMFPTR_DIO_GET_RECEIPT_FIELD, 
                "SMFPTR_DIO_GET_RECEIPT_FIELD");
        map.put(SmFptrConst.SMFPTR_DIO_SET_RECEIPT_FIELD, 
                "SMFPTR_DIO_SET_RECEIPT_FIELD");
        map.put(SmFptrConst.SMFPTR_DIO_READ_JOURNAL, 
                "SMFPTR_DIO_READ_JOURNAL");
        
   
        String result = map.get(command);
        if (result == null) {
            result = String.valueOf(command);
        }
        return result;
    }

}
