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

    private final FiscalPrinterImpl impl;
    static CompositeLogger logger = CompositeLogger.getLogger(FiscalPrinterService.class);

    public FiscalPrinterService() throws Exception {
        impl = new FiscalPrinterImpl();
    }

    public void deleteInstance() throws JposException {
    }

    public void handleException(Throwable e) throws JposException {
        impl.handleException(e);
    }

    public boolean getCapCompareFirmwareVersion() throws JposException {
        logger.debug("getCapCompareFirmwareVersion()");
        boolean result = false;
        try {
            result = impl.getCapCompareFirmwareVersion();
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("getCapCompareFirmwareVersion = " + String.valueOf(result));
        return result;
    }

    public boolean getCapUpdateFirmware() throws JposException {
        logger.debug("getCapUpdateFirmware()");
        boolean result = false;
        try {
            result = impl.getCapUpdateFirmware();
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("getCapUpdateFirmware = " + String.valueOf(result));
        return result;
    }

    public void compareFirmwareVersion(String firmwareFileName, int[] result)
            throws JposException {
        logger.debug("compareFirmwareVersion("
                + String.valueOf(firmwareFileName) + ", "
                + String.valueOf(result) + ")");
        try {
            impl.compareFirmwareVersion(firmwareFileName, result);
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("compareFirmwareVersion: OK");
    }

    public void updateFirmware(String firmwareFileName) throws JposException {
        logger.debug("updateFirmware(" + String.valueOf(firmwareFileName) + ")");
        try {
            impl.updateFirmware(firmwareFileName);
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("updateFirmware: OK");
    }

    public String getCheckHealthText() throws JposException {
        logger.debug("getCheckHealthText()");

        String result = "";
        try {
            result = impl.getCheckHealthText();
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("getCheckHealthText = " + String.valueOf(result));
        return result;
    }

    public boolean getClaimed() throws JposException {
        logger.debug("getClaimed()");
        boolean result = false;
        try {
            result = impl.getClaimed();
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("getClaimed = " + String.valueOf(result));
        return result;
    }

    public boolean getDeviceEnabled() throws JposException {
        logger.debug("getDeviceEnabled()");
        boolean result = false;
        try {
            result = impl.getDeviceEnabled();
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("getDeviceEnabled = " + String.valueOf(result));
        return result;
    }

    public void setDeviceEnabled(boolean deviceEnabled) throws JposException {
        logger.debug("setDeviceEnabled(" + String.valueOf(deviceEnabled) + ")");
        try {
            impl.setDeviceEnabled(deviceEnabled);
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("setDeviceEnabled: OK");
    }

    public String getDeviceServiceDescription() throws JposException {
        logger.debug("getDeviceServiceDescription()");
        String result = "";
        try {
            result = impl.getDeviceServiceDescription();
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("getDeviceServiceDescription = " + String.valueOf(result));
        return result;
    }

    public int getDeviceServiceVersion() throws JposException {
        logger.debug("getDeviceServiceVersion()");
        int result = 0;
        try {
            result = impl.getDeviceServiceVersion();
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("getDeviceServiceVersion = " + String.valueOf(result));
        return result;
    }

    public boolean getFreezeEvents() throws JposException {
        logger.debug("getFreezeEvents()");
        boolean result = false;
        try {
            result = impl.getFreezeEvents();
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("getFreezeEvents = " + String.valueOf(result));
        return result;
    }

    public void setFreezeEvents(boolean freezeEvents) throws JposException {
        logger.debug("setFreezeEvents(" + String.valueOf(freezeEvents) + ")");
        try {
            impl.setFreezeEvents(freezeEvents);
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("setFreezeEvents: OK");
    }

    public String getPhysicalDeviceDescription() throws JposException {
        logger.debug("getPhysicalDeviceDescription()");
        String result = "";
        try {
            result = impl.getPhysicalDeviceDescription();
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("getPhysicalDeviceDescription = " + String.valueOf(result));
        return result;
    }

    public String getPhysicalDeviceName() throws JposException {
        logger.debug("getPhysicalDeviceName()");
        String result = "";
        try {
            result = impl.getPhysicalDeviceName();
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("getPhysicalDeviceName = " + String.valueOf(result));
        return result;
    }

    public int getState() throws JposException {
        logger.debug("getState()");
        int result = 0;
        try {
            result = impl.getState();
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("getState = " + String.valueOf(result));
        return result;
    }

    public void claim(int timeout) throws JposException {
        logger.debug("claim(" + String.valueOf(timeout) + ")");
        try {
            impl.claim(timeout);
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("claim: OK");
    }

    public void close() throws JposException {
        logger.debug("close()");
        try {
            impl.close();
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("close: OK");
    }

    public void checkHealth(int level) throws JposException {
        logger.debug("checkHealth(" + String.valueOf(level) + ")");
        try {
            impl.checkHealth(level);
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("checkHealth: OK");
    }

    public void directIO(int command, int[] data, Object object)
            throws JposException {
        logger.debug("directIO(" + getDirectIOCommandText(command) + ", "
                + String.valueOf(data) + ", " + String.valueOf(object) + ")");
        try {
            impl.directIO(command, data, object);
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("directIO: OK");
    }

    public void setJposEntry(JposEntry entry) {
        jposEntry = entry;
    }

    public void open(String logicalName, EventCallbacks cb)
            throws JposException {
        logger.debug("open(" + String.valueOf(logicalName) + ")");
        try {
            impl.setJposEntry(jposEntry);
            impl.open(logicalName, cb);
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("open: OK");
    }

    public void release() throws JposException {
        logger.debug("release()");
        try {
            impl.release();
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("release: OK");
    }

    public boolean getCapAdditionalLines() throws JposException {
        logger.debug("getCapAdditionalLines()");
        boolean result = false;
        try {
            result = impl.getCapAdditionalLines();
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("getCapAdditionalLines = " + String.valueOf(result));
        return result;
    }

    public boolean getCapAmountAdjustment() throws JposException {
        logger.debug("getCapAmountAdjustment()");
        boolean result = false;
        try {
            result = impl.getCapAmountAdjustment();
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("getCapAmountAdjustment = " + String.valueOf(result));
        return result;
    }

    public boolean getCapAmountNotPaid() throws JposException {
        logger.debug("getCapAmountNotPaid()");
        boolean result = false;
        try {
            result = impl.getCapAmountNotPaid();
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("getCapAmountNotPaid = " + String.valueOf(result));
        return result;
    }

    public boolean getCapCheckTotal() throws JposException {
        logger.debug("getCapCheckTotal()");
        boolean result = false;
        try {
            result = impl.getCapCheckTotal();
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("getCapCheckTotal = " + String.valueOf(result));
        return result;
    }

    public boolean getCapCoverSensor() throws JposException {
        logger.debug("getCapCoverSensor()");
        boolean result = false;
        try {
            result = impl.getCapCoverSensor();
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("getCapCoverSensor = " + String.valueOf(result));
        return result;
    }

    public boolean getCapDoubleWidth() throws JposException {
        logger.debug("getCapDoubleWidth()");
        boolean result = false;
        try {
            result = impl.getCapDoubleWidth();
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("getCapDoubleWidth = " + String.valueOf(result));
        return result;
    }

    public boolean getCapDuplicateReceipt() throws JposException {
        logger.debug("getCapDuplicateReceipt()");
        boolean result = false;
        try {
            result = impl.getCapDuplicateReceipt();
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("getCapDuplicateReceipt = " + String.valueOf(result));
        return result;
    }

    public void setDuplicateReceipt(boolean duplicateReceipt)
            throws JposException {
        logger.debug("setDuplicateReceipt(" + String.valueOf(duplicateReceipt)
                + ")");
        try {
            impl.setDuplicateReceipt(duplicateReceipt);
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("setDuplicateReceipt: OK");
    }

    public boolean getCapFixedOutput() throws JposException {
        logger.debug("getCapFixedOutput()");
        boolean result = false;
        try {
            result = impl.getCapFixedOutput();
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("getCapFixedOutput = " + String.valueOf(result));
        return result;
    }

    public boolean getCapHasVatTable() throws JposException {
        logger.debug("getCapHasVatTable()");
        boolean result = false;
        try {
            result = impl.getCapHasVatTable();
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("getCapHasVatTable = " + String.valueOf(result));
        return result;
    }

    public boolean getCapIndependentHeader() throws JposException {
        logger.debug("getCapIndependentHeader()");
        boolean result = false;
        try {
            result = impl.getCapIndependentHeader();
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("getCapIndependentHeader = " + String.valueOf(result));
        return result;
    }

    public boolean getCapItemList() throws JposException {
        logger.debug("getCapItemList()");
        boolean result = false;
        try {
            result = impl.getCapItemList();
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("getCapItemList = " + String.valueOf(result));
        return result;
    }

    public boolean getCapJrnEmptySensor() throws JposException {
        logger.debug("getCapJrnEmptySensor()");
        boolean result = false;
        try {
            result = impl.getCapJrnEmptySensor();
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("getCapJrnEmptySensor = " + String.valueOf(result));
        return result;
    }

    public boolean getCapJrnNearEndSensor() throws JposException {
        logger.debug("getCapJrnNearEndSensor()");
        boolean result = false;
        try {
            result = impl.getCapJrnNearEndSensor();
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("getCapJrnNearEndSensor = " + String.valueOf(result));
        return result;
    }

    public boolean getCapJrnPresent() throws JposException {
        logger.debug("getCapJrnPresent()");
        boolean result = false;
        try {
            result = impl.getCapJrnPresent();
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("getCapJrnPresent = " + String.valueOf(result));
        return result;
    }

    public boolean getCapNonFiscalMode() throws JposException {
        logger.debug("getCapNonFiscalMode()");
        boolean result = false;
        try {
            result = impl.getCapNonFiscalMode();
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("getCapNonFiscalMode = " + String.valueOf(result));
        return result;
    }

    public boolean getCapOrderAdjustmentFirst() throws JposException {
        logger.debug("getCapOrderAdjustmentFirst()");
        boolean result = false;
        try {
            result = impl.getCapOrderAdjustmentFirst();
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("getCapOrderAdjustmentFirst = " + String.valueOf(result));
        return result;
    }

    public boolean getCapPercentAdjustment() throws JposException {
        logger.debug("getCapPercentAdjustment()");
        boolean result = false;
        try {
            result = impl.getCapPercentAdjustment();
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("getCapPercentAdjustment = " + String.valueOf(result));
        return result;
    }

    public boolean getCapPositiveAdjustment() throws JposException {
        logger.debug("getCapPositiveAdjustment()");
        boolean result = false;
        try {
            result = impl.getCapPositiveAdjustment();
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("getCapPositiveAdjustment = " + String.valueOf(result));
        return result;
    }

    public boolean getCapPowerLossReport() throws JposException {
        logger.debug("getCapPowerLossReport()");
        boolean result = false;
        try {
            result = impl.getCapPowerLossReport();
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("getCapPowerLossReport = " + String.valueOf(result));
        return result;
    }

    public int getCapPowerReporting() throws JposException {
        logger.debug("getCapPowerReporting()");
        int result = 0;
        try {
            result = impl.getCapPowerReporting();
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("getCapPowerReporting = " + String.valueOf(result));
        return result;
    }

    public boolean getCapPredefinedPaymentLines() throws JposException {
        logger.debug("getCapPredefinedPaymentLines()");
        boolean result = false;
        try {
            result = impl.getCapPredefinedPaymentLines();
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("getCapPredefinedPaymentLines = " + String.valueOf(result));
        return result;
    }

    public boolean getCapReceiptNotPaid() throws JposException {
        logger.debug("getCapReceiptNotPaid()");
        boolean result = false;
        try {
            result = impl.getCapReceiptNotPaid();
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("getCapReceiptNotPaid = " + String.valueOf(result));
        return result;
    }

    public boolean getCapRecEmptySensor() throws JposException {
        logger.debug("getCapRecEmptySensor()");
        boolean result = false;
        try {
            result = impl.getCapRecEmptySensor();
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("getCapRecEmptySensor = " + String.valueOf(result));
        return result;
    }

    public boolean getCapRecNearEndSensor() throws JposException {
        logger.debug("getCapRecNearEndSensor()");
        boolean result = false;
        try {
            result = impl.getCapRecNearEndSensor();
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("getCapRecNearEndSensor = " + String.valueOf(result));
        return result;
    }

    public boolean getCapRecPresent() throws JposException {
        logger.debug("getCapRecPresent()");
        boolean result = false;
        try {
            result = impl.getCapRecPresent();
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("getCapRecPresent = " + String.valueOf(result));
        return result;
    }

    public boolean getCapRemainingFiscalMemory() throws JposException {
        logger.debug("getCapRemainingFiscalMemory()");
        boolean result = false;
        try {
            result = impl.getCapRemainingFiscalMemory();
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("getCapRemainingFiscalMemory = " + String.valueOf(result));
        return result;
    }

    public boolean getCapReservedWord() throws JposException {
        logger.debug("getCapReservedWord()");
        boolean result = false;
        try {
            result = impl.getCapReservedWord();
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("getCapReservedWord = " + String.valueOf(result));
        return result;
    }

    public boolean getCapSetHeader() throws JposException {
        logger.debug("getCapSetHeader()");
        boolean result = false;
        try {
            result = impl.getCapSetHeader();
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("getCapSetHeader = " + String.valueOf(result));
        return result;
    }

    public boolean getCapSetPOSID() throws JposException {
        logger.debug("getCapSetPOSID()");
        boolean result = false;
        try {
            result = impl.getCapSetPOSID();
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("getCapSetPOSID = " + String.valueOf(result));
        return result;
    }

    public boolean getCapSetStoreFiscalID() throws JposException {
        logger.debug("getCapSetStoreFiscalID()");
        boolean result = false;
        try {
            result = impl.getCapSetStoreFiscalID();
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("getCapSetStoreFiscalID = " + String.valueOf(result));
        return result;
    }

    public boolean getCapSetTrailer() throws JposException {
        logger.debug("getCapSetTrailer()");
        boolean result = false;
        try {
            result = impl.getCapSetTrailer();
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("getCapSetTrailer = " + String.valueOf(result));
        return result;
    }

    public boolean getCapSetVatTable() throws JposException {
        logger.debug("getCapSetVatTable()");
        boolean result = false;
        try {
            result = impl.getCapSetVatTable();
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("getCapSetVatTable = " + String.valueOf(result));
        return result;
    }

    public boolean getCapSlpEmptySensor() throws JposException {
        logger.debug("getCapSlpEmptySensor()");
        boolean result = false;
        try {
            result = impl.getCapSlpEmptySensor();
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("getCapSlpEmptySensor = " + String.valueOf(result));
        return result;
    }

    public boolean getCapSlpFiscalDocument() throws JposException {
        logger.debug("getCapSlpFiscalDocument()");
        boolean result = false;
        try {
            result = impl.getCapSlpFiscalDocument();
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("getCapSlpFiscalDocument = " + String.valueOf(result));
        return result;
    }

    public boolean getCapSlpFullSlip() throws JposException {
        logger.debug("getCapSlpFullSlip()");
        boolean result = false;
        try {
            result = impl.getCapSlpFullSlip();
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("getCapSlpFullSlip = " + String.valueOf(result));
        return result;
    }

    public boolean getCapSlpNearEndSensor() throws JposException {
        logger.debug("getCapSlpNearEndSensor()");
        boolean result = false;
        try {
            result = impl.getCapSlpNearEndSensor();
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("getCapSlpNearEndSensor = " + String.valueOf(result));
        return result;
    }

    public boolean getCapSlpPresent() throws JposException {
        logger.debug("getCapSlpPresent()");
        boolean result = false;
        try {
            result = impl.getCapSlpPresent();
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("getCapSlpPresent = " + String.valueOf(result));
        return result;
    }

    public boolean getCapSlpValidation() throws JposException {
        logger.debug("getCapSlpValidation()");
        boolean result = false;
        try {
            result = impl.getCapSlpValidation();
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("getCapSlpValidation = " + String.valueOf(result));
        return result;
    }

    public boolean getCapSubAmountAdjustment() throws JposException {
        logger.debug("getCapSubAmountAdjustment()");
        boolean result = false;
        try {
            result = impl.getCapSubAmountAdjustment();
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("getCapSubAmountAdjustment = " + String.valueOf(result));
        return result;
    }

    public boolean getCapSubPercentAdjustment() throws JposException {
        logger.debug("getCapSubPercentAdjustment()");
        boolean result = false;
        try {
            result = impl.getCapSubPercentAdjustment();
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("getCapSubPercentAdjustment = " + String.valueOf(result));
        return result;
    }

    public boolean getCapSubtotal() throws JposException {
        logger.debug("getCapSubtotal()");
        boolean result = false;
        try {
            result = impl.getCapSubtotal();
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("getCapSubtotal = " + String.valueOf(result));
        return result;
    }

    public boolean getCapTrainingMode() throws JposException {
        logger.debug("getCapTrainingMode()");
        boolean result = false;
        try {
            result = impl.getCapTrainingMode();
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("getCapTrainingMode = " + String.valueOf(result));
        return result;
    }

    public boolean getCapValidateJournal() throws JposException {
        logger.debug("getCapValidateJournal()");
        boolean result = false;
        try {
            result = impl.getCapValidateJournal();
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("getCapValidateJournal = " + String.valueOf(result));
        return result;
    }

    public boolean getCapXReport() throws JposException {
        logger.debug("getCapXReport()");
        boolean result = false;
        try {
            result = impl.getCapXReport();
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("getCapXReport = " + String.valueOf(result));
        return result;
    }

    public int getOutputID() throws JposException {
        logger.debug("getOutputID()");
        int result = 0;
        try {
            result = impl.getOutputID();
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("getOutputID = " + String.valueOf(result));
        return result;
    }

    public int getPowerNotify() throws JposException {
        logger.debug("getPowerNotify()");
        int result = 0;
        try {
            result = impl.getPowerNotify();
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("getPowerNotify = " + String.valueOf(result));
        return result;
    }

    public void setPowerNotify(int powerNotify) throws JposException {
        logger.debug("setPowerNotify(" + String.valueOf(powerNotify) + ")");
        try {
            impl.setPowerNotify(powerNotify);
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("setPowerNotify: OK");
    }

    public int getPowerState() throws JposException {
        logger.debug("getPowerState()");
        int result = 0;
        try {
            result = impl.getPowerState();
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("getPowerState = " + String.valueOf(result));
        return result;
    }

    public int getAmountDecimalPlace() throws JposException {
        logger.debug("getAmountDecimalPlace()");
        int result = 0;
        try {
            result = impl.getAmountDecimalPlace();
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("getAmountDecimalPlace = " + String.valueOf(result));
        return result;
    }

    public boolean getAsyncMode() throws JposException {
        logger.debug("getAsyncMode()");
        boolean result = false;
        try {
            result = impl.getAsyncMode();
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("getAsyncMode = " + String.valueOf(result));
        return result;
    }

    public void setAsyncMode(boolean asyncMode) throws JposException {
        logger.debug("setAsyncMode(" + String.valueOf(asyncMode) + ")");
        try {
            impl.setAsyncMode(asyncMode);
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("setAsyncMode: OK");
    }

    public boolean getCheckTotal() throws JposException {
        logger.debug("getCheckTotal()");
        boolean result = false;
        try {
            result = impl.getCheckTotal();
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("getCheckTotal = " + String.valueOf(result));
        return result;
    }

    public void setCheckTotal(boolean checkTotal) throws JposException {
        logger.debug("setCheckTotal(" + String.valueOf(checkTotal) + ")");
        try {
            impl.setCheckTotal(checkTotal);
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("setCheckTotal: OK");
    }

    public int getCountryCode() throws JposException {
        logger.debug("getCountryCode()");
        int result = 0;
        try {
            result = impl.getCountryCode();
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("getCountryCode = " + String.valueOf(result));
        return result;
    }

    public boolean getCoverOpen() throws JposException {
        logger.debug("getCoverOpen()");
        boolean result = false;
        try {
            result = impl.getCoverOpen();
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("getCoverOpen = " + String.valueOf(result));
        return result;
    }

    public boolean getDayOpened() throws JposException {
        logger.debug("getDayOpened()");
        boolean result = false;
        try {
            result = impl.getDayOpened();
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("getDayOpened = " + String.valueOf(result));
        return result;
    }

    public int getDescriptionLength() throws JposException {
        logger.debug("getDescriptionLength()");
        int result = 0;
        try {
            result = impl.getDescriptionLength();
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("getDescriptionLength = " + String.valueOf(result));
        return result;
    }

    public boolean getDuplicateReceipt() throws JposException {
        logger.debug("getDuplicateReceipt()");
        boolean result = false;
        try {
            result = impl.getDuplicateReceipt();
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("getDuplicateReceipt = " + String.valueOf(result));
        return result;
    }

    public int getErrorLevel() throws JposException {
        logger.debug("getErrorLevel()");
        int result = 0;
        try {
            result = impl.getErrorLevel();
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("getErrorLevel = " + String.valueOf(result));
        return result;
    }

    public int getErrorOutID() throws JposException {
        logger.debug("getErrorOutID()");
        int result = 0;
        try {
            result = impl.getErrorOutID();
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("getErrorOutID = " + String.valueOf(result));
        return result;
    }

    public int getErrorState() throws JposException {
        logger.debug("getErrorState()");
        int result = 0;
        try {
            result = impl.getErrorState();
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("getErrorState = " + String.valueOf(result));
        return result;
    }

    public int getErrorStation() throws JposException {
        logger.debug("getErrorStation()");
        int result = 0;
        try {
            result = impl.getErrorStation();
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("getErrorStation = " + String.valueOf(result));
        return result;
    }

    public String getErrorString() throws JposException {
        logger.debug("getErrorString()");
        String result = "";
        try {
            result = impl.getErrorString();
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("getErrorString = " + String.valueOf(result));
        return result;
    }

    public boolean getFlagWhenIdle() throws JposException {
        logger.debug("getFlagWhenIdle()");
        boolean result = false;
        try {
            result = impl.getFlagWhenIdle();
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("getFlagWhenIdle = " + String.valueOf(result));
        return result;
    }

    public void setFlagWhenIdle(boolean flagWhenIdle) throws JposException {
        logger.debug("setFlagWhenIdle(" + String.valueOf(flagWhenIdle) + ")");
        try {
            impl.setFlagWhenIdle(flagWhenIdle);
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("setFlagWhenIdle: OK");
    }

    public boolean getJrnEmpty() throws JposException {
        logger.debug("getJrnEmpty()");
        boolean result = false;
        try {
            result = impl.getJrnEmpty();
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("getJrnEmpty = " + String.valueOf(result));
        return result;
    }

    public boolean getJrnNearEnd() throws JposException {
        logger.debug("getJrnNearEnd()");
        boolean result = false;
        try {
            result = impl.getJrnNearEnd();
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("getJrnNearEnd = " + String.valueOf(result));
        return result;
    }

    public int getMessageLength() throws JposException {
        logger.debug("getMessageLength()");
        int result = 0;
        try {
            result = impl.getMessageLength();
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("getMessageLength = " + String.valueOf(result));
        return result;
    }

    public int getNumHeaderLines() throws JposException {
        logger.debug("getNumHeaderLines()");
        int result = 0;
        try {
            result = impl.getNumHeaderLines();
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("getNumHeaderLines = " + String.valueOf(result));
        return result;
    }

    public int getNumTrailerLines() throws JposException {
        logger.debug("getNumTrailerLines()");
        int result = 0;
        try {
            result = impl.getNumTrailerLines();
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("getNumTrailerLines = " + String.valueOf(result));
        return result;
    }

    public int getNumVatRates() throws JposException {
        logger.debug("getNumVatRates()");
        int result = 0;
        try {
            result = impl.getNumVatRates();
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("getNumVatRates = " + String.valueOf(result));
        return result;
    }

    public String getPredefinedPaymentLines() throws JposException {
        logger.debug("getPredefinedPaymentLines()");
        String result = "";
        try {
            result = impl.getPredefinedPaymentLines();
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("getPredefinedPaymentLines = " + String.valueOf(result));
        return result;
    }

    public int getPrinterState() throws JposException {
        logger.debug("getPrinterState()");
        int result = 0;
        try {
            result = impl.getPrinterState();
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("getPrinterState = " + String.valueOf(result));
        return result;
    }

    public int getQuantityDecimalPlaces() throws JposException {
        logger.debug("getQuantityDecimalPlaces()");
        int result = 0;
        try {
            result = impl.getQuantityDecimalPlaces();
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("getQuantityDecimalPlaces = " + String.valueOf(result));
        return result;
    }

    public int getQuantityLength() throws JposException {
        logger.debug("getQuantityLength()");
        int result = 0;
        try {
            result = impl.getQuantityLength();
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("getQuantityLength = " + String.valueOf(result));
        return result;
    }

    public boolean getRecEmpty() throws JposException {
        logger.debug("getRecEmpty()");
        boolean result = false;
        try {
            result = impl.getRecEmpty();
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("getRecEmpty = " + String.valueOf(result));
        return result;
    }

    public boolean getRecNearEnd() throws JposException {
        logger.debug("getRecNearEnd()");
        boolean result = false;
        try {
            result = impl.getRecNearEnd();
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("getRecNearEnd = " + String.valueOf(result));
        return result;
    }

    public int getRemainingFiscalMemory() throws JposException {
        logger.debug("getRemainingFiscalMemory()");
        int result = 0;
        try {
            result = impl.getRemainingFiscalMemory();
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("getRemainingFiscalMemory = " + String.valueOf(result));
        return result;
    }

    public String getReservedWord() throws JposException {
        logger.debug("getReservedWord()");
        String result = "";
        try {
            result = impl.getReservedWord();
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("getReservedWord = " + String.valueOf(result));
        return result;
    }

    public boolean getSlpEmpty() throws JposException {
        logger.debug("getSlpEmpty()");
        boolean result = false;
        try {
            result = impl.getSlpEmpty();
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("getSlpEmpty = " + String.valueOf(result));
        return result;
    }

    public boolean getSlpNearEnd() throws JposException {
        logger.debug("getSlpNearEnd()");
        boolean result = false;
        try {
            result = impl.getSlpNearEnd();
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("getSlpNearEnd = " + String.valueOf(result));
        return result;
    }

    public int getSlipSelection() throws JposException {
        logger.debug("getSlipSelection()");
        int result = 0;
        try {
            result = impl.getSlipSelection();
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("getSlipSelection = " + String.valueOf(result));
        return result;
    }

    public void setSlipSelection(int slipSelection) throws JposException {
        logger.debug("setSlipSelection(" + String.valueOf(slipSelection) + ")");
        try {
            impl.setSlipSelection(slipSelection);
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("setSlipSelection: OK");
    }

    public boolean getTrainingModeActive() throws JposException {
        logger.debug("getTrainingModeActive()");
        boolean result = false;
        try {
            result = impl.getTrainingModeActive();
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("getTrainingModeActive = " + String.valueOf(result));
        return result;
    }

    public void beginFiscalDocument(int documentAmount) throws JposException {
        logger.debug("beginFiscalDocument(" + String.valueOf(documentAmount)
                + ")");
        try {
            impl.beginFiscalDocument(documentAmount);
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("beginFiscalDocument: OK");
    }

    public void beginFiscalReceipt(boolean printHeader) throws JposException {
        logger.debug("beginFiscalReceipt(" + String.valueOf(printHeader) + ")");
        try {
            impl.beginFiscalReceipt(printHeader);
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("beginFiscalReceipt: OK");
    }

    public void beginFixedOutput(int station, int documentType)
            throws JposException {
        logger.debug("beginFixedOutput(" + String.valueOf(station) + ", "
                + String.valueOf(documentType) + ")");
        try {
            impl.beginFixedOutput(station, documentType);
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("beginFixedOutput: OK");
    }

    public void beginInsertion(int timeout) throws JposException {
        logger.debug("beginInsertion(" + String.valueOf(timeout) + ")");
        try {
            impl.beginInsertion(timeout);
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("beginInsertion: OK");
    }

    public void beginItemList(int vatID) throws JposException {
        logger.debug("beginItemList(" + String.valueOf(vatID) + ")");
        try {
            impl.beginItemList(vatID);
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("beginItemList: OK");
    }

    public void beginNonFiscal() throws JposException {
        logger.debug("beginNonFiscal()");
        try {
            impl.beginNonFiscal();
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("beginNonFiscal: OK");
    }

    public void beginRemoval(int timeout) throws JposException {
        logger.debug("beginRemoval(" + String.valueOf(timeout) + ")");
        try {
            impl.beginRemoval(timeout);
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("beginRemoval: OK");
    }

    public void beginTraining() throws JposException {
        logger.debug("beginTraining()");
        try {
            impl.beginTraining();
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("beginTraining: OK");
    }

    public void clearError() throws JposException {
        logger.debug("clearError()");
        try {
            impl.clearError();
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("clearError: OK");
    }

    public void clearOutput() throws JposException {
        logger.debug("clearOutput()");
        try {
            impl.clearOutput();
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("clearOutput: OK");
    }

    public void endFiscalDocument() throws JposException {
        logger.debug("endFiscalDocument()");
        try {
            impl.endFiscalDocument();
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("endFiscalDocument: OK");
    }

    public void endFiscalReceipt(boolean printHeader) throws JposException {
        logger.debug("endFiscalReceipt(" + String.valueOf(printHeader) + ")");
        try {
            impl.endFiscalReceipt(printHeader);
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("endFiscalReceipt: OK");
    }

    public void endFixedOutput() throws JposException {
        logger.debug("endFixedOutput()");
        try {
            impl.endFixedOutput();
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("endFixedOutput: OK");
    }

    public void endInsertion() throws JposException {
        logger.debug("endInsertion()");
        try {
            impl.endInsertion();
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("endInsertion: OK");
    }

    public void endItemList() throws JposException {
        logger.debug("endItemList()");
        try {
            impl.endItemList();
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("endItemList: OK");
    }

    public void endNonFiscal() throws JposException {
        logger.debug("endNonFiscal()");
        try {
            impl.endNonFiscal();
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("endNonFiscal: OK");
    }

    public void endRemoval() throws JposException {
        logger.debug("endRemoval()");
        try {
            impl.endRemoval();
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("endRemoval: OK");
    }

    public void endTraining() throws JposException {
        logger.debug("endTraining()");
        try {
            impl.endTraining();
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("endTraining: OK");
    }

    public void getData(int dataItem, int[] optArgs, String[] data)
            throws JposException {
        logger.debug("getData(" + String.valueOf(dataItem) + ", "
                + String.valueOf(optArgs) + ", " + String.valueOf(data) + ")");
        try {
            impl.getData(dataItem, optArgs, data);
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("getData: OK");
    }

    public void getDate(String[] Date) throws JposException {
        logger.debug("getDate(" + String.valueOf(Date) + ")");
        try {
            impl.getDate(Date);
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("getDate: OK");
    }

    public void getTotalizer(int vatID, int optArgs, String[] data)
            throws JposException {
        logger.debug("getTotalizer(" + String.valueOf(vatID) + ", "
                + String.valueOf(optArgs) + ", " + String.valueOf(data) + ")");
        try {
            impl.getTotalizer(vatID, optArgs, data);
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("getTotalizer: OK");
    }

    public void getVatEntry(int vatID, int optArgs, int[] vatRate)
            throws JposException {
        logger.debug("getVatEntry(" + String.valueOf(vatID) + ", "
                + String.valueOf(optArgs) + ", " + String.valueOf(vatRate)
                + ")");
        try {
            impl.getVatEntry(vatID, optArgs, vatRate);
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("getVatEntry: OK");
    }

    public void printDuplicateReceipt() throws JposException {
        logger.debug("printDuplicateReceipt()");
        try {
            impl.printDuplicateReceipt();
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("printDuplicateReceipt: OK");
    }

    public void printFiscalDocumentLine(String documentLine)
            throws JposException {
        logger.debug("printFiscalDocumentLine(" + String.valueOf(documentLine)
                + ")");
        try {
            impl.printFiscalDocumentLine(documentLine);
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("printFiscalDocumentLine: OK");
    }

    public void printFixedOutput(int documentType, int lineNumber, String data)
            throws JposException {
        logger.debug("printFixedOutput(" + String.valueOf(documentType) + ", "
                + String.valueOf(lineNumber) + ", " + String.valueOf(data)
                + ")");
        try {
            impl.printFixedOutput(documentType, lineNumber, data);
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("printFixedOutput: OK");
    }

    public void printNormal(int station, String data) throws JposException {
        logger.debug("printNormal(" + String.valueOf(station) + ", "
                + String.valueOf(data) + ")");
        try {
            impl.printNormal(station, data);
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("printNormal: OK");
    }

    public void printPeriodicTotalsReport(String date1, String date2)
            throws JposException {
        logger.debug("printPeriodicTotalsReport(" + String.valueOf(date1)
                + ", " + String.valueOf(date2) + ")");
        try {
            impl.printPeriodicTotalsReport(date1, date2);
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("printPeriodicTotalsReport: OK");
    }

    public void printPowerLossReport() throws JposException {
        logger.debug("printPowerLossReport()");
        try {
            impl.printPowerLossReport();
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("printPowerLossReport: OK");
    }

    public void printRecItem(String description, long price, int quantity,
            int vatInfo, long unitPrice, String unitName) throws JposException {
        logger.debug("printRecItem(" + String.valueOf(description) + ", "
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
        logger.debug("printRecItem: OK");
    }

    public void printRecItemAdjustment(int adjustmentType, String description,
            long amount, int vatInfo) throws JposException {
        logger.debug("printRecItemAdjustment(" + String.valueOf(adjustmentType)
                + ", " + String.valueOf(description) + ", "
                + String.valueOf(amount) + ", " + String.valueOf(vatInfo) + ")");
        try {
            impl.printRecItemAdjustment(adjustmentType, description, amount,
                    vatInfo);
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("printRecItemAdjustment: OK");
    }

    public void printRecMessage(String message) throws JposException {
        logger.debug("printRecMessage(" + String.valueOf(message) + ")");
        try {
            impl.printRecMessage(message);
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("printRecMessage: OK");
    }

    public void printRecNotPaid(String description, long amount)
            throws JposException {
        logger.debug("printRecNotPaid(" + String.valueOf(description) + ", "
                + String.valueOf(amount) + ")");
        try {
            impl.printRecNotPaid(description, amount);
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("printRecNotPaid: OK");
    }

    public void printRecRefund(String description, long amount, int vatInfo)
            throws JposException {
        logger.debug("printRecRefund(" + String.valueOf(description) + ", "
                + String.valueOf(amount) + ", " + String.valueOf(vatInfo) + ")");
        try {
            impl.printRecRefund(description, amount, vatInfo);
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("printRecRefund: OK");
    }

    public void printRecItemRefund(String description, long amount,
            int quantity, int vatInfo, long unitAmount, String unitName)
            throws JposException {
        logger.debug("printRecItemRefund(" + String.valueOf(description) + ", "
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
        logger.debug("printRecItemRefund: OK");
    }

    public void printRecItemRefundVoid(String description, long amount,
            int quantity, int vatInfo, long unitAmount, String unitName)
            throws JposException {
        logger.debug("printRecItemRefundVoid(" + String.valueOf(description)
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
        logger.debug("printRecItemRefundVoid: OK");
    }

    public void printRecItemAdjustmentVoid(int adjustmentType,
            String description, long amount, int vatInfo) throws JposException {
        logger.debug("printRecItemAdjustmentVoid("
                + String.valueOf(adjustmentType) + ", "
                + String.valueOf(description) + ", " + String.valueOf(amount)
                + ", " + String.valueOf(vatInfo) + ")");
        try {
            impl.printRecItemAdjustmentVoid(adjustmentType, description,
                    amount, vatInfo);
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("printRecItemAdjustmentVoid: OK");
    }

    public void printRecSubtotal(long amount) throws JposException {
        logger.debug("printRecSubtotal(" + String.valueOf(amount) + ")");
        try {
            impl.printRecSubtotal(amount);
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("printRecSubtotal: OK");
    }

    public void printRecSubtotalAdjustment(int adjustmentType,
            String description, long amount) throws JposException {
        logger.debug("printRecSubtotalAdjustment("
                + String.valueOf(adjustmentType) + ", "
                + String.valueOf(description) + ", " + String.valueOf(amount)
                + ")");
        try {
            impl.printRecSubtotalAdjustment(adjustmentType, description, amount);
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("printRecSubtotalAdjustment: OK");
    }

    public void printRecTotal(long total, long payment, String description)
            throws JposException {
        logger.debug("printRecTotal(" + String.valueOf(total) + ", "
                + String.valueOf(payment) + ", " + String.valueOf(description)
                + ")");
        try {
            impl.printRecTotal(total, payment, description);
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("printRecTotal: OK");
    }

    public void printRecVoid(String description) throws JposException {
        logger.debug("printRecVoid(" + String.valueOf(description) + ")");
        try {
            impl.printRecVoid(description);
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("printRecVoid: OK");
    }

    public void printRecVoidItem(String description, long amount, int quantity,
            int adjustmentType, long adjustment, int vatInfo)
            throws JposException {
        logger.debug("printRecVoidItem(" + String.valueOf(description) + ", "
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
        logger.debug("printRecVoidItem: OK");
    }

    public void printReport(int reportType, String startNum, String endNum)
            throws JposException {
        logger.debug("printReport(" + String.valueOf(reportType) + ", "
                + String.valueOf(startNum) + ", " + String.valueOf(endNum)
                + ")");
        try {
            impl.printReport(reportType, startNum, endNum);
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("printReport: OK");
    }

    public void printXReport() throws JposException {
        logger.debug("printXReport()");
        try {
            impl.printXReport();
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("printXReport: OK");
    }

    public void printZReport() throws JposException {
        logger.debug("printZReport()");
        try {
            impl.printZReport();
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("printZReport: OK");
    }

    public void resetPrinter() throws JposException {
        logger.debug("resetPrinter()");
        try {
            impl.resetPrinter();
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("resetPrinter: OK");
    }

    public void setDate(String date) throws JposException {
        logger.debug("setDate(" + String.valueOf(date) + ")");
        try {
            impl.setDate(date);
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("setDate: OK");
    }

    public void setHeaderLine(int lineNumber, String text, boolean doubleWidth)
            throws JposException {
        logger.debug("setHeaderLine(" + String.valueOf(lineNumber) + ", \""
                + String.valueOf(text) + "\", " + String.valueOf(doubleWidth)
                + ")");
        try {
            impl.setHeaderLine(lineNumber, text, doubleWidth);
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("setHeaderLine: OK");
    }

    public void setPOSID(String POSID, String cashierID) throws JposException {
        logger.debug("setPOSID(" + String.valueOf(POSID) + ", "
                + String.valueOf(cashierID) + ")");
        try {
            impl.setPOSID(POSID, cashierID);
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("setPOSID: OK");
    }

    public void setStoreFiscalID(String ID) throws JposException {
        logger.debug("setStoreFiscalID(" + String.valueOf(ID) + ")");
        try {
            impl.setStoreFiscalID(ID);
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("setStoreFiscalID: OK");
    }

    public void setTrailerLine(int lineNumber, String text, boolean doubleWidth)
            throws JposException {
        logger.debug("setTrailerLine(" + String.valueOf(lineNumber) + ", \""
                + String.valueOf(text) + "\", " + String.valueOf(doubleWidth)
                + ")");
        try {
            impl.setTrailerLine(lineNumber, text, doubleWidth);
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("setTrailerLine: OK");
    }

    public void setVatTable() throws JposException {
        logger.debug("setVatTable()");
        try {
            impl.setVatTable();
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("setVatTable: OK");
    }

    public void setVatValue(int vatID, String vatValue) throws JposException {
        logger.debug("setVatValue(" + String.valueOf(vatID) + ", "
                + String.valueOf(vatValue) + ")");
        try {
            impl.setVatValue(vatID, vatValue);
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("setVatValue: OK");
    }

    public void verifyItem(String itemName, int vatID) throws JposException {
        logger.debug("verifyItem(" + String.valueOf(itemName) + ", "
                + String.valueOf(vatID) + ")");
        try {
            impl.verifyItem(itemName, vatID);
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("verifyItem: OK");
    }

    public boolean getCapAdditionalHeader() throws JposException {
        logger.debug("getCapAdditionalHeader()");
        boolean result = false;
        try {
            result = impl.getCapAdditionalHeader();
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("getCapAdditionalHeader = " + String.valueOf(result));
        return result;
    }

    public boolean getCapAdditionalTrailer() throws JposException {
        logger.debug("getCapAdditionalTrailer()");
        boolean result = false;
        try {
            result = impl.getCapAdditionalTrailer();
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("getCapAdditionalTrailer = " + String.valueOf(result));
        return result;
    }

    public boolean getCapChangeDue() throws JposException {
        logger.debug("getCapChangeDue()");
        boolean result = false;
        try {
            result = impl.getCapChangeDue();
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("getCapChangeDue = " + String.valueOf(result));
        return result;
    }

    public boolean getCapEmptyReceiptIsVoidable() throws JposException {
        logger.debug("getCapEmptyReceiptIsVoidable()");
        boolean result = false;
        try {
            result = impl.getCapEmptyReceiptIsVoidable();
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("getCapEmptyReceiptIsVoidable = " + String.valueOf(result));
        return result;
    }

    public boolean getCapFiscalReceiptStation() throws JposException {
        logger.debug("getCapFiscalReceiptStation()");
        boolean result = false;
        try {
            result = impl.getCapFiscalReceiptStation();
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("getCapFiscalReceiptStation = " + String.valueOf(result));
        return result;
    }

    public boolean getCapFiscalReceiptType() throws JposException {
        logger.debug("getCapFiscalReceiptType()");
        boolean result = false;
        try {
            result = impl.getCapFiscalReceiptType();
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("getCapFiscalReceiptType = " + String.valueOf(result));
        return result;
    }

    public boolean getCapMultiContractor() throws JposException {
        logger.debug("getCapMultiContractor()");
        boolean result = false;
        try {
            result = impl.getCapMultiContractor();
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("getCapMultiContractor = " + String.valueOf(result));
        return result;
    }

    public boolean getCapOnlyVoidLastItem() throws JposException {
        logger.debug("getCapOnlyVoidLastItem()");
        boolean result = false;
        try {
            result = impl.getCapOnlyVoidLastItem();
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("getCapOnlyVoidLastItem = " + String.valueOf(result));
        return result;
    }

    public boolean getCapPackageAdjustment() throws JposException {
        logger.debug("getCapPackageAdjustment()");
        boolean result = false;
        try {
            result = impl.getCapPackageAdjustment();
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("getCapPackageAdjustment = " + String.valueOf(result));
        return result;
    }

    public boolean getCapPostPreLine() throws JposException {
        logger.debug("getCapPostPreLine()");
        boolean result = false;
        try {
            result = impl.getCapPostPreLine();
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("getCapPostPreLine = " + String.valueOf(result));
        return result;
    }

    public boolean getCapSetCurrency() throws JposException {
        logger.debug("getCapSetCurrency()");
        boolean result = false;
        try {
            result = impl.getCapSetCurrency();
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("getCapSetCurrency = " + String.valueOf(result));
        return result;
    }

    public boolean getCapTotalizerType() throws JposException {
        logger.debug("getCapTotalizerType()");
        boolean result = false;
        try {
            result = impl.getCapTotalizerType();
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("getCapTotalizerType = " + String.valueOf(result));
        return result;
    }

    public int getActualCurrency() throws JposException {
        logger.debug("getActualCurrency()");
        int result = 0;
        try {
            result = impl.getActualCurrency();
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("getActualCurrency = " + String.valueOf(result));
        return result;
    }

    public String getAdditionalHeader() throws JposException {
        logger.debug("getAdditionalHeader()");
        String result = "";
        try {
            result = impl.getAdditionalHeader();
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("getAdditionalHeader = " + String.valueOf(result));
        return result;
    }

    public void setAdditionalHeader(String additionalHeader)
            throws JposException {
        logger.debug("setAdditionalHeader(" + String.valueOf(additionalHeader)
                + ")");
        try {
            impl.setAdditionalHeader(additionalHeader);
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("setAdditionalHeader: OK");
    }

    public String getAdditionalTrailer() throws JposException {
        logger.debug("getAdditionalTrailer()");
        String result = "";
        try {
            result = impl.getAdditionalTrailer();
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("getAdditionalTrailer = " + String.valueOf(result));
        return result;
    }

    public void setAdditionalTrailer(String additionalTrailer)
            throws JposException {
        logger.debug("setAdditionalTrailer("
                + String.valueOf(additionalTrailer) + ")");
        try {
            impl.setAdditionalTrailer(additionalTrailer);
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("setAdditionalTrailer: OK");
    }

    public String getChangeDue() throws JposException {
        logger.debug("getChangeDue()");
        String result = "";
        try {
            result = impl.getChangeDue();
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("getChangeDue = " + String.valueOf(result));
        return result;
    }

    public void setChangeDue(String changeDue) throws JposException {
        logger.debug("setChangeDue(" + String.valueOf(changeDue) + ")");
        try {
            impl.setChangeDue(changeDue);
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("setChangeDue: OK");
    }

    public int getContractorId() throws JposException {
        logger.debug("getContractorId()");
        int result = 0;
        try {
            result = impl.getContractorId();
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("getContractorId = " + String.valueOf(result));
        return result;
    }

    public void setContractorId(int contractorId) throws JposException {
        logger.debug("setContractorId(" + String.valueOf(contractorId) + ")");
        try {
            impl.setContractorId(contractorId);
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("setContractorId: OK");
    }

    public int getDateType() throws JposException {
        logger.debug("getDateType()");
        int result = 0;
        try {
            result = impl.getDateType();
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("getDateType = " + String.valueOf(result));
        return result;
    }

    public void setDateType(int dateType) throws JposException {
        logger.debug("setDateType(" + String.valueOf(dateType) + ")");
        try {
            impl.setDateType(dateType);
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("setDateType: OK");
    }

    public int getFiscalReceiptStation() throws JposException {
        logger.debug("getFiscalReceiptStation()");
        int result = 0;
        try {
            result = impl.getFiscalReceiptStation();
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("getFiscalReceiptStation = " + String.valueOf(result));
        return result;
    }

    public void setFiscalReceiptStation(int fiscalReceiptStation)
            throws JposException {
        logger.debug("setFiscalReceiptStation("
                + String.valueOf(fiscalReceiptStation) + ")");
        try {
            impl.setFiscalReceiptStation(fiscalReceiptStation);
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("setFiscalReceiptStation: OK");
    }

    public int getFiscalReceiptType() throws JposException {
        logger.debug("getFiscalReceiptType()");
        int result = 0;
        try {
            result = impl.getFiscalReceiptType();
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("getFiscalReceiptType = " + String.valueOf(result));
        return result;
    }

    public void setFiscalReceiptType(int fiscalReceiptType)
            throws JposException {
        logger.debug("setFiscalReceiptType("
                + String.valueOf(fiscalReceiptType) + ")");
        try {
            impl.setFiscalReceiptType(fiscalReceiptType);
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("setFiscalReceiptType: OK");
    }

    public int getMessageType() throws JposException {
        logger.debug("getMessageType()");
        int result = 0;
        try {
            result = impl.getMessageType();
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("getMessageType = " + String.valueOf(result));
        return result;
    }

    public void setMessageType(int messageType) throws JposException {
        logger.debug("setMessageType(" + String.valueOf(messageType) + ")");
        try {
            impl.setMessageType(messageType);
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("setMessageType: OK");
    }

    public String getPostLine() throws JposException {
        logger.debug("getPostLine()");
        String result = "";
        try {
            result = impl.getPostLine();
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("getPostLine = " + String.valueOf(result));
        return result;
    }

    public void setPostLine(String postLine) throws JposException {
        logger.debug("setPostLine(" + String.valueOf(postLine) + ")");
        try {
            impl.setPostLine(postLine);
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("setPostLine: OK");
    }

    public String getPreLine() throws JposException {
        logger.debug("getPreLine()");
        String result = "";
        try {
            result = impl.getPreLine();
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("getPreLine = " + String.valueOf(result));
        return result;
    }

    public void setPreLine(String preLine) throws JposException {
        logger.debug("setPreLine(" + String.valueOf(preLine) + ")");
        try {
            impl.setPreLine(preLine);
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("setPreLine: OK");
    }

    public int getTotalizerType() throws JposException {
        logger.debug("getTotalizerType()");
        int result = 0;
        try {
            result = impl.getTotalizerType();
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("getTotalizerType = " + String.valueOf(result));
        return result;
    }

    public void setTotalizerType(int totalizerType) throws JposException {
        logger.debug("setTotalizerType(" + String.valueOf(totalizerType) + ")");
        try {
            impl.setTotalizerType(totalizerType);
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("setTotalizerType: OK");
    }

    public void setCurrency(int newCurrency) throws JposException {
        logger.debug("setCurrency(" + String.valueOf(newCurrency) + ")");
        try {
            impl.setCurrency(newCurrency);
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("setCurrency: OK");
    }

    public void printRecCash(long amount) throws JposException {
        logger.debug("printRecCash(" + String.valueOf(amount) + ")");
        try {
            impl.printRecCash(amount);
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("printRecCash: OK");
    }

    public void printRecItemFuel(String description, long price, int quantity,
            int vatInfo, long unitPrice, String unitName, long specialTax,
            String specialTaxName) throws JposException {
        logger.debug("printRecItemFuel(" + String.valueOf(description) + ", "
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
        logger.debug("printRecItemFuel: OK");
    }

    public void printRecItemFuelVoid(String description, long price,
            int vatInfo, long specialTax) throws JposException {
        logger.debug("printRecItemFuelVoid(" + String.valueOf(description)
                + ", " + String.valueOf(price) + ", " + String.valueOf(vatInfo)
                + ", " + String.valueOf(specialTax) + ")");
        try {
            impl.printRecItemFuelVoid(description, price, vatInfo, specialTax);
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("printRecItemFuelVoid: OK");
    }

    public void printRecPackageAdjustment(int adjustmentType,
            String description, String vatAdjustment) throws JposException {
        logger.debug("printRecPackageAdjustment("
                + String.valueOf(adjustmentType) + ", "
                + String.valueOf(description) + ", "
                + String.valueOf(vatAdjustment) + ")");
        try {
            impl.printRecPackageAdjustment(adjustmentType, description,
                    vatAdjustment);
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("printRecPackageAdjustment: OK");
    }

    public void printRecPackageAdjustVoid(int adjustmentType,
            String vatAdjustment) throws JposException {
        logger.debug("printRecPackageAdjustVoid("
                + String.valueOf(adjustmentType) + ", "
                + String.valueOf(vatAdjustment) + ")");
        try {
            impl.printRecPackageAdjustVoid(adjustmentType, vatAdjustment);
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("printRecPackageAdjustVoid: OK");
    }

    public void printRecRefundVoid(String description, long amount, int vatInfo)
            throws JposException {
        logger.debug("printRecRefundVoid(" + String.valueOf(description) + ", "
                + String.valueOf(amount) + ", " + String.valueOf(vatInfo) + ")");
        try {
            impl.printRecRefundVoid(description, amount, vatInfo);
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("printRecRefundVoid: OK");
    }

    public void printRecSubtotalAdjustVoid(int adjustmentType, long amount)
            throws JposException {
        logger.debug("printRecSubtotalAdjustVoid("
                + String.valueOf(adjustmentType) + ", "
                + String.valueOf(amount) + ")");
        try {
            impl.printRecSubtotalAdjustVoid(adjustmentType, amount);
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("printRecSubtotalAdjustVoid: OK");
    }

    public void printRecTaxID(String taxID) throws JposException {
        logger.debug("printRecTaxID(" + String.valueOf(taxID) + ")");
        try {
            impl.printRecTaxID(taxID);
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("printRecTaxID: OK");
    }

    public int getAmountDecimalPlaces() throws JposException {
        logger.debug("getAmountDecimalPlaces()");
        int result = 0;
        try {
            result = impl.getAmountDecimalPlaces();
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("getAmountDecimalPlaces = " + String.valueOf(result));
        return result;
    }

    public boolean getCapStatisticsReporting() throws JposException {
        logger.debug("getCapStatisticsReporting()");
        boolean result = false;
        try {
            result = impl.getCapStatisticsReporting();
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("getCapStatisticsReporting = " + String.valueOf(result));
        return result;
    }

    public boolean getCapUpdateStatistics() throws JposException {
        logger.debug("getCapUpdateStatistics()");
        boolean result = false;
        try {
            result = impl.getCapUpdateStatistics();
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("getCapUpdateStatistics = " + String.valueOf(result));
        return result;
    }

    public void resetStatistics(String statisticsBuffer) throws JposException {
        logger.debug("resetStatistics(" + String.valueOf(statisticsBuffer)
                + ")");
        try {
            impl.resetStatistics(statisticsBuffer);
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("resetStatistics: OK");
    }

    public void retrieveStatistics(String[] statisticsBuffer)
            throws JposException {
        logger.debug("retrieveStatistics(" + String.valueOf(statisticsBuffer)
                + ")");
        try {
            impl.retrieveStatistics(statisticsBuffer);
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("retrieveStatistics: OK");
    }

    public void updateStatistics(String statisticsBuffer) throws JposException {
        logger.debug("updateStatistics(" + String.valueOf(statisticsBuffer)
                + ")");
        try {
            impl.updateStatistics(statisticsBuffer);
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("updateStatistics: OK");
    }

    public void printRecItemVoid(String description, long price, int quantity,
            int vatInfo, long unitPrice, String unitName) throws JposException {
        logger.debug("printRecItemVoid(" + String.valueOf(description) + ", "
                + String.valueOf(price) + ", " + String.valueOf(quantity)
                + ", " + String.valueOf(vatInfo) + ", "
                + String.valueOf(unitPrice) + ", " + String.valueOf(unitName)
                + ")");
        try {
            impl.printRecItemVoid(description, price, quantity, vatInfo,
                    unitPrice, unitName);

        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("printRecItemVoid: OK");
    }

    public boolean getCapPositiveSubtotalAdjustment() throws JposException {
        logger.debug("getCapPositiveSubtotalAdjustment()");
        boolean result = false;
        try {
            result = impl.getCapPositiveSubtotalAdjustment();
        } catch (Throwable e) {
            handleException(e);
        }
        logger.debug("getCapPositiveSubtotalAdjustment = "
                + String.valueOf(result));
        return result;
    }

    public String getDirectIOCommandText(int command) {
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
