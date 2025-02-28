
/*
 * FiscalPrinterImplTest.java
 * JUnit based test
 *
 * Created on 12 Октябрь 2009 г., 12:47
 */

package com.shtrih.jpos.fiscalprinter;

import com.shtrih.fiscalprinter.MockSmFiscalPrinter;
import java.util.Vector;
import jpos.BaseControl;
import jpos.FiscalPrinterConst;
import jpos.JposConst;
import jpos.JposException;
import jpos.events.DataEvent;
import jpos.events.DirectIOEvent;
import jpos.events.ErrorEvent;
import jpos.events.OutputCompleteEvent;
import jpos.events.StatusUpdateEvent;
import jpos.services.EventCallbacks;
import junit.framework.TestCase;
import org.junit.Ignore;

/**
 *
 * @author V.Kravtsov
 */
@Ignore
public class FiscalPrinterImplTest extends TestCase 
{
    
    /*

    protected void setUp() throws Exception {
        
        device = new MockPrinterDevice();
        model = new MockPrinterModel();
        events = new MockEventCallbacks();
        instance = new FiscalPrinterImpl();
        instance.setEventCallbacks(events);
        instance.printer.setDevice(device);
    }
    
    protected void tearDown() throws Exception {
    }

    private class MockEventCallbacks implements EventCallbacks{
        
        private final Vector events = new Vector();
        
        public void waitEvent(long timeout) {
            try {
                synchronized(events) {
                    events.wait(timeout);
                }
            } catch(InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        
        public void addEvent(Object e) {
            synchronized(events) {
                events.add(e);
                events.notifyAll();
            }
        }
        
        public void fireDataEvent(DataEvent e) {
            addEvent(e);
        };
        
        public void fireDirectIOEvent(DirectIOEvent e){
            addEvent(e);
        };
        
        public void fireErrorEvent(ErrorEvent e){
            addEvent(e);
        };
        
        public void fireOutputCompleteEvent(OutputCompleteEvent e){
            addEvent(e);
        };
        
        public void fireStatusUpdateEvent(StatusUpdateEvent e){
            addEvent(e);
        };
        
        public BaseControl getEventSource() {
            return null;
        };
        
        public Vector getEvents(){
            return events;
        }
    }
    
    MockPrinterModel model;
    MockPrinterDevice device;
    MockEventCallbacks events;
    FiscalPrinterImpl instance;
    
    
    /**
     * Test of getRecPaperState method, of class com.shtrih.jpos.fiscalprinter.FiscalPrinterImpl.
     */
    /*
    public void testGetRecPaperState() throws Exception {
        System.out.println("getRecPaperState");
        
        assertTrue("getCapRecPresent", instance.getCapRecPresent());
        assertTrue("getCapRecEmptySensor", instance.getCapRecEmptySensor());
        assertTrue("getCapRecNearEndSensor", instance.getCapRecNearEndSensor());
        
        int result = 0;
        int expResult = 0;
        
        expResult = FiscalPrinterConst.FPTR_SUE_REC_PAPEROK;
        result = instance.getRecPaperState(false, false);
        assertEquals(expResult, result);
        
        expResult = FiscalPrinterConst.FPTR_SUE_REC_EMPTY;
        result = instance.getRecPaperState(true, false);
        assertEquals(expResult, result);
        
        expResult = FiscalPrinterConst.FPTR_SUE_REC_EMPTY;
        result = instance.getRecPaperState(true, true);
        assertEquals(expResult, result);
        
        expResult = FiscalPrinterConst.FPTR_SUE_REC_NEAREMPTY;
        result = instance.getRecPaperState(false, true);
        assertEquals(expResult, result);
    }
    
    public void testSetRecPaperState() throws Exception {
        Object event;
        StatusUpdateEvent sue;
        
        assertEquals(events.getEvents().size(), 0);
        instance.setRecPaperState(false, false);
        assertEquals(events.getEvents().size(), 0);
        instance.setRecPaperState(true, false);
        events.waitEvent(100);
        assertEquals(events.getEvents().size(), 1);
        event = events.getEvents().get(0);
        assertEquals("StatusUpdateEvent", event.getClass().getName());
        sue = (StatusUpdateEvent)event;
        assertEquals("sue.getParameters != FPTR_SUE_REC_EMPTY",
            sue.getParameters(), FiscalPrinterConst.FPTR_SUE_REC_EMPTY);
        
        events.getEvents().clear();
        assertEquals(events.getEvents().size(), 0);
        instance.setRecPaperState(false, true);
        events.waitEvent(100);
        assertEquals(events.getEvents().size(), 1);
        event = events.getEvents().get(0);
        assertEquals("StatusUpdateEvent", event.getClass().getName());
        sue = (StatusUpdateEvent)event;
        assertEquals("sue.getParameters != FPTR_SUE_REC_NEAREMPTY",
            sue.getParameters(), FiscalPrinterConst.FPTR_SUE_REC_NEAREMPTY);
        
        events.getEvents().clear();
        assertEquals(events.getEvents().size(), 0);
        instance.setRecPaperState(false, false);
        events.waitEvent(100);
        assertEquals(events.getEvents().size(), 1);
        event = events.getEvents().get(0);
        assertEquals("StatusUpdateEvent", event.getClass().getName());
        sue = (StatusUpdateEvent)event;
        assertEquals("sue.getParameters != FPTR_SUE_REC_PAPEROK",
            sue.getParameters(), FiscalPrinterConst.FPTR_SUE_REC_PAPEROK);
    }
    
    public void testGetJrnPaperState() throws Exception {
        System.out.println("getJrnPaperState");
        
        assertTrue("getCapJrnPresent", instance.getCapJrnPresent());
        assertTrue("getCapJrnEmptySensor", instance.getCapJrnEmptySensor());
        assertTrue("getCapJrnNearEndSensor", instance.getCapJrnNearEndSensor());
        
        int result = 0;
        int expResult = 0;
        
        expResult = FiscalPrinterConst.FPTR_SUE_JRN_PAPEROK;
        result = instance.getJrnPaperState(false, false);
        assertEquals(expResult, result);
        
        expResult = FiscalPrinterConst.FPTR_SUE_JRN_EMPTY;
        result = instance.getJrnPaperState(true, false);
        assertEquals(expResult, result);
        
        expResult = FiscalPrinterConst.FPTR_SUE_JRN_EMPTY;
        result = instance.getJrnPaperState(true, true);
        assertEquals(expResult, result);
        
        expResult = FiscalPrinterConst.FPTR_SUE_JRN_NEAREMPTY;
        result = instance.getJrnPaperState(false, true);
        assertEquals(expResult, result);
    }
    
    public void testSetJrnPaperState() throws Exception {
        Object event;
        StatusUpdateEvent sue;
        
        assertEquals(events.getEvents().size(), 0);
        instance.setJrnPaperState(false, false);
        assertEquals(events.getEvents().size(), 0);
        instance.setJrnPaperState(true, false);
        events.waitEvent(100);
        assertEquals(events.getEvents().size(), 1);
        event = events.getEvents().get(0);
        assertEquals("StatusUpdateEvent", event.getClass().getName());
        sue = (StatusUpdateEvent)event;
        assertEquals("sue.getParameters != FPTR_SUE_JRN_EMPTY",
            sue.getParameters(), FiscalPrinterConst.FPTR_SUE_JRN_EMPTY);
        
        events.getEvents().clear();
        assertEquals(events.getEvents().size(), 0);
        instance.setJrnPaperState(false, true);
        events.waitEvent(100);
        assertEquals(events.getEvents().size(), 1);
        event = events.getEvents().get(0);
        assertEquals("StatusUpdateEvent", event.getClass().getName());
        sue = (StatusUpdateEvent)event;
        assertEquals("sue.getParameters != FPTR_SUE_JRN_NEAREMPTY",
            sue.getParameters(), FiscalPrinterConst.FPTR_SUE_JRN_NEAREMPTY);
        
        events.getEvents().clear();
        assertEquals(events.getEvents().size(), 0);
        instance.setJrnPaperState(false, false);
        events.waitEvent(100);
        assertEquals(events.getEvents().size(), 1);
        event = events.getEvents().get(0);
        assertEquals("StatusUpdateEvent", event.getClass().getName());
        sue = (StatusUpdateEvent)event;
        assertEquals("sue.getParameters != FPTR_SUE_JRN_PAPEROK",
            sue.getParameters(), FiscalPrinterConst.FPTR_SUE_JRN_PAPEROK);
    }
    
    public void testGetSlpPaperState() throws Exception {
        System.out.println("getSlpPaperState");
        
        assertTrue("getCapSlpPresent", instance.getCapSlpPresent());
        assertTrue("getCapSlpEmptySensor", instance.getCapSlpEmptySensor());
        assertTrue("getCapSlpNearEndSensor", instance.getCapSlpNearEndSensor());
        
        int result = 0;
        int expResult = 0;
        
        expResult = FiscalPrinterConst.FPTR_SUE_SLP_PAPEROK;
        result = instance.getSlpPaperState(false, false);
        assertEquals(expResult, result);
        
        expResult = FiscalPrinterConst.FPTR_SUE_SLP_EMPTY;
        result = instance.getSlpPaperState(true, false);
        assertEquals(expResult, result);
        
        expResult = FiscalPrinterConst.FPTR_SUE_SLP_EMPTY;
        result = instance.getSlpPaperState(true, true);
        assertEquals(expResult, result);
        
        expResult = FiscalPrinterConst.FPTR_SUE_SLP_NEAREMPTY;
        result = instance.getSlpPaperState(false, true);
        assertEquals(expResult, result);
    }
    
    public void testSetSlpPaperState() throws Exception {
        Object event;
        StatusUpdateEvent sue;
        
        assertEquals(events.getEvents().size(), 0);
        instance.setSlpPaperState(false, false);
        assertEquals(events.getEvents().size(), 0);
        instance.setSlpPaperState(true, false);
        events.waitEvent(100);
        assertEquals(events.getEvents().size(), 1);
        event = events.getEvents().get(0);
        assertEquals("StatusUpdateEvent", event.getClass().getName());
        sue = (StatusUpdateEvent)event;
        assertEquals("sue.getParameters != FPTR_SUE_SLP_EMPTY",
            sue.getParameters(), FiscalPrinterConst.FPTR_SUE_SLP_EMPTY);
        
        events.getEvents().clear();
        assertEquals(events.getEvents().size(), 0);
        instance.setSlpPaperState(false, true);
        events.waitEvent(100);
        assertEquals(events.getEvents().size(), 1);
        event = events.getEvents().get(0);
        assertEquals("StatusUpdateEvent", event.getClass().getName());
        sue = (StatusUpdateEvent)event;
        assertEquals("sue.getParameters != FPTR_SUE_SLP_NEAREMPTY",
            sue.getParameters(), FiscalPrinterConst.FPTR_SUE_SLP_NEAREMPTY);
        
        events.getEvents().clear();
        assertEquals(events.getEvents().size(), 0);
        instance.setSlpPaperState(false, false);
        events.waitEvent(100);
        assertEquals(events.getEvents().size(), 1);
        event = events.getEvents().get(0);
        assertEquals("StatusUpdateEvent", event.getClass().getName());
        sue = (StatusUpdateEvent)event;
        assertEquals("sue.getParameters != FPTR_SUE_SLP_PAPEROK",
            sue.getParameters(), FiscalPrinterConst.FPTR_SUE_SLP_PAPEROK);
    }
    
    
    public void testCheckDeviceStatus() 
    {
        instance.params.statusCommand = PrinterConst.SMFP_STATUS_COMMAND_10H;
        instance.checkDeviceStatus();
    }
    
    public void testDeviceProc() 
    {
        
    }
    
    public void testPrintRecItem() throws Exception 
    {
        PriceItem item;
        PrintSale saleCommand;
        PrinterCommand command;
        PrintStringFont printCommand;
        model.capOpenReceipt = false;
        instance.printer.setUsrPassword(10);
    
        // not claimed
        assertEquals("instance.claimed", false, instance.claimed);
        try
        {
            instance.printRecItem("", 0, 0, 0, 0, "");
            fail("Claimed not checked");
        }
        catch(JposException e)
        {
            assertEquals(JposConst.JPOS_E_NOTCLAIMED, e.getErrorCode());
            assertEquals(0, e.getErrorCodeExtended());
            assertEquals("103", e.getMessage());
        }
        
        // not enabled
        instance.claimed = true;
        assertEquals("instance.claimed", true, instance.claimed);
        try
        {
            instance.printRecItem("", 0, 0, 0, 0, "");
            fail("Enabled not checked");
        }
        catch(JposException e)
        {
            assertEquals(JposConst.JPOS_E_DISABLED, e.getErrorCode());
            assertEquals(0, e.getErrorCodeExtended());
            assertEquals("105", e.getMessage());
        }
        instance.deviceEnabled = true;
        assertEquals("instance.deviceEnabled", true, instance.deviceEnabled);
        // check printerState
        assertEquals("instance.printerState", 
            FiscalPrinterConst.FPTR_PS_MONITOR, instance.printerState);
        try
        {
            instance.printRecItem("", 0, 0, 0, 0, "");
            fail("Printer state not checked");
        }
        catch(JposException e)
        {
            assertEquals(JposConst.JPOS_E_EXTENDED, e.getErrorCode());
            assertEquals(FiscalPrinterConst.JPOS_EFPTR_WRONG_STATE, e.getErrorCodeExtended());
        }

        // unitPrice = 0 - quantity chnaged to 1000
        instance.department = 12;
        device.getCommands().clear();
        assertEquals(0, device.getCommands().size());
        instance.printerState = FiscalPrinterConst.FPTR_PS_FISCAL_RECEIPT;
        instance.printRecItem("description", 123, 234, 1, 0, "");
        assertEquals(1, device.getCommands().size());
        command = device.getCommand(0);
        assertEquals("PrintSale", command.getClass().getName());
        saleCommand = (PrintSale) command;
        item = saleCommand.getItem();
        assertEquals("description", item.getText());
        assertEquals(123, item.getPrice());
        assertEquals(1000, item.getQuantity());
        assertEquals(1, item.getTax1());
        assertEquals(0, item.getTax2());
        assertEquals(0, item.getTax3());
        assertEquals(0, item.getTax4());
        assertEquals(12, item.getDepartment());
        
        // unitPrice != 0 -> quantity not changed
        instance.department = 12;
        device.getCommands().clear();
        assertEquals(0, device.getCommands().size());
        instance.printerState = FiscalPrinterConst.FPTR_PS_FISCAL_RECEIPT;
        instance.printRecItem("description", 123, 234, 1, 1234, "");
        assertEquals(1, device.getCommands().size());
        command = device.getCommand(0);
        assertEquals("PrintSale", command.getClass().getName());
        saleCommand = (PrintSale) command;
        item = saleCommand.getItem();
        assertEquals("description", item.getText());
        assertEquals(1234, item.getPrice());
        assertEquals(234, item.getQuantity());
        assertEquals(1, item.getTax1());
        assertEquals(0, item.getTax2());
        assertEquals(0, item.getTax3());
        assertEquals(0, item.getTax4());
        assertEquals(12, item.getDepartment());
       
        
        // unitPrice = 0 - quantity chnaged to 1000
        instance.department = 12;
        //instance.fontNumber = 5;
        model.capPrintStringFont = true;
        instance.setPreLine("PreLine1\r\nPreLine2");
        instance.setPostLine("PostLine1");
        device.getCommands().clear();
        assertEquals(0, device.getCommands().size());
        instance.printerState = FiscalPrinterConst.FPTR_PS_FISCAL_RECEIPT;
        instance.printRecItem("description", 123, 234, 1, 0, "");
        assertEquals(4, device.getCommands().size());
        
        // PreLine1
        command = device.getCommand(0);
        assertEquals("PrintStringFont", command.getClass().getName());
        printCommand = (PrintStringFont) command;
        assertEquals(10, printCommand.getPassword());
        assertEquals(PrinterConst.SMFP_STATION_REC, printCommand.getStation());
        assertEquals(5, printCommand.getFontNumber());
        assertEquals("PreLine1", printCommand.getLine());
        
        // PreLine2
        command = device.getCommand(1);
        assertEquals("PrintStringFont", command.getClass().getName());
        printCommand = (PrintStringFont) command;
        assertEquals(10, printCommand.getPassword());
        assertEquals(PrinterConst.SMFP_STATION_REC, printCommand.getStation());
        assertEquals(5, printCommand.getFontNumber());
        assertEquals("PreLine2", printCommand.getLine());
        
        // PrintSale
        command = device.getCommand(2);
        assertEquals("PrintSale", command.getClass().getName());
        saleCommand = (PrintSale) command;
        item = saleCommand.getItem();
        assertEquals("description", item.getText());
        assertEquals(123, item.getPrice());
        assertEquals(1000, item.getQuantity());
        assertEquals(1, item.getTax1());
        assertEquals(0, item.getTax2());
        assertEquals(0, item.getTax3());
        assertEquals(0, item.getTax4());
        assertEquals(12, item.getDepartment());
        
        // PostLine1
        command = device.getCommand(3);
        assertEquals("PrintStringFont", command.getClass().getName());
        printCommand = (PrintStringFont) command;
        assertEquals(10, printCommand.getPassword());
        assertEquals(PrinterConst.SMFP_STATION_REC, printCommand.getStation());
        assertEquals(5, printCommand.getFontNumber());
        assertEquals("PostLine1", printCommand.getLine());
        // PreLine and PostLine must be cleared
        assertEquals("", instance.getPreLine());
        assertEquals("", instance.getPostLine());
        
        // long description
        instance.department = 12;
        instance.setPreLine("");
        instance.setPostLine("");
        model.textLength[4] = 10;
        device.getCommands().clear();
        assertEquals(0, device.getCommands().size());
        instance.printerState = FiscalPrinterConst.FPTR_PS_FISCAL_RECEIPT;
        instance.printRecItem("Line1\r\n01234567890123456789", 123, 234, 1, 0, "");
        assertEquals(4, device.getCommands().size());
        
        // Line1
        command = device.getCommand(0);
        assertEquals("PrintStringFont", command.getClass().getName());
        printCommand = (PrintStringFont) command;
        assertEquals(10, printCommand.getPassword());
        assertEquals(PrinterConst.SMFP_STATION_REC, printCommand.getStation());
        assertEquals(5, printCommand.getFontNumber());
        assertEquals("Line1", printCommand.getLine());
        
        // 0123456789
        command = device.getCommand(1);
        assertEquals("PrintStringFont", command.getClass().getName());
        printCommand = (PrintStringFont) command;
        assertEquals(10, printCommand.getPassword());
        assertEquals(PrinterConst.SMFP_STATION_REC, printCommand.getStation());
        assertEquals(5, printCommand.getFontNumber());
        assertEquals("0123456789", printCommand.getLine());
        
        // 0123456789
        command = device.getCommand(2);
        assertEquals("PrintStringFont", command.getClass().getName());
        printCommand = (PrintStringFont) command;
        assertEquals(10, printCommand.getPassword());
        assertEquals(PrinterConst.SMFP_STATION_REC, printCommand.getStation());
        assertEquals(5, printCommand.getFontNumber());
        assertEquals("0123456789", printCommand.getLine());
        
        command = device.getCommand(3);
        assertEquals("PrintSale", command.getClass().getName());
        saleCommand = (PrintSale) command;
        item = saleCommand.getItem();
        assertEquals("", item.getText());
        assertEquals(123, item.getPrice());
        assertEquals(1000, item.getQuantity());
        assertEquals(1, item.getTax1());
        assertEquals(0, item.getTax2());
        assertEquals(0, item.getTax3());
        assertEquals(0, item.getTax4());
        assertEquals(12, item.getDepartment());
    }
    
    public void testGetData() throws Exception 
    {
        int dataItem = FiscalPrinterConst.FPTR_GD_RECEIPT_NUMBER;
        int[] optArgs = new int[1];
        String[] data = new String[1];
        int documentNumber = 1234;
        
        MockSmFiscalPrinter printer = new MockSmFiscalPrinter();
        LongPrinterStatus status = new LongPrinterStatus();
        status.setDocumentNumber(documentNumber);
        printer.setLongStatus(status);
        instance.setPrinter(printer);
        instance.claimed = true;
        instance.deviceEnabled = true;
        instance.getTLV(dataItem, optArgs, data);
        assertEquals(documentNumber, Integer.parseInt(data[0]));
        
        status.setMode(PrinterConst.ECRMODE_RECSELL);
        instance.printerState = FiscalPrinterConst.FPTR_PS_MONITOR;
        instance.getTLV(dataItem, optArgs, data);
        assertEquals(documentNumber + 1, Integer.parseInt(data[0]));
        
        documentNumber = 9999;
        status.setDocumentNumber(documentNumber);
        status.setMode(PrinterConst.ECRMODE_RECSELL);
        instance.printerState = FiscalPrinterConst.FPTR_PS_MONITOR;
        instance.getTLV(dataItem, optArgs, data);
        assertEquals(1, Integer.parseInt(data[0]));
    }
    
/*
    public void testHandleException() throws Exception {
    }
 
    public void testGetCommandTimeout() throws Exception {
    }
 
    public void testGetCommands() throws Exception {
    }
 
    public void testOpenReceipt() throws Exception {
    }
 
    public void testPrintSale() throws Exception {
    }
 
    public void testPrintSaleRefund() throws Exception {
    }
 
    public void testPrintStorno() throws Exception {
    }
 
    public void testPrintDiscount() throws Exception {
    }
 
    public void testPrintCharge() throws Exception {
    }
 
    public void testGetCheckHealthText() throws Exception {
    }
 
    public void testGetClaimed() throws Exception {
    }
 
    public void testGetDeviceEnabled() throws Exception {
    }
 
    public void testCommandExecuted() {
    }
 
    public void testExecute() throws Exception {
    }
 
    public void testEventProc() {
    }
 
    public void testAsyncProc() {
    }
 
    public void testUpdateStatus() throws Exception {
    }
 
    public void testSetDeviceEnabled() throws Exception {
    }
 
    public void testSetPollEnabled() throws Exception {
    }
 
    public void testGetDeviceServiceDescription() throws Exception {
    }
 
    public void testGetDeviceServiceVersion() throws Exception {
    }
 
    public void testGetFreezeEvents() throws Exception {
    }
 
    public void testSetFreezeEvents() throws Exception {
    }
 
    public void testGetPhysicalDeviceDescription() throws Exception {
    }
 
    public void testGetPhysicalDeviceName() throws Exception {
    }
 
    public void testGetState() throws Exception {
    }
 
    public void testGetCapAdditionalLines() throws Exception {
    }
 
    public void testGetCapAmountAdjustment() throws Exception {
    }
 
    public void testGetCapAmountNotPaid() throws Exception {
    }
 
    public void testGetCapCheckTotal() throws Exception {
    }
 
    public void testGetCapCoverSensor() throws Exception {
    }
 
    public void testGetCapDoubleWidth() throws Exception {
    }
 
    public void testGetCapDuplicateReceipt() throws Exception {
    }
 
    public void testSetDuplicateReceipt() throws Exception {
    }
 
    public void testGetCapFixedOutput() throws Exception {
    }
 
    public void testGetCapHasVatTable() throws Exception {
    }
 
    public void testGetCapIndependentHeader() throws Exception {
    }
 
    public void testGetCapItemList() throws Exception {
    }
 
    public void testGetCapJrnEmptySensor() throws Exception {
    }
 
    public void testGetCapJrnNearEndSensor() throws Exception {
    }
 
    public void testGetCapJrnPresent() throws Exception {
    }
 
    public void testGetCapNonFiscalMode() throws Exception {
    }
 
    public void testGetCapOrderAdjustmentFirst() throws Exception {
    }
 
    public void testGetCapPercentAdjustment() throws Exception {
    }
 
    public void testGetCapPositiveAdjustment() throws Exception {
    }
 
    public void testGetCapPowerLossReport() throws Exception {
    }
 
    public void testGetCapPowerReporting() throws Exception {
    }
 
    public void testGetCapPredefinedPaymentLines() throws Exception {
    }
 
    public void testGetCapReceiptNotPaid() throws Exception {
    }
 
    public void testGetCapRecEmptySensor() throws Exception {
    }
 
    public void testGetCapRecNearEndSensor() throws Exception {
    }
 
    public void testGetCapRecPresent() throws Exception {
    }
 
    public void testGetCapRemainingFiscalMemory() throws Exception {
    }
 
    public void testGetCapReservedWord() throws Exception {
    }
 
    public void testGetCapSetHeader() throws Exception {
    }
 
    public void testGetCapSetPOSID() throws Exception {
    }
 
    public void testGetCapSetStoreFiscalID() throws Exception {
    }
 
    public void testGetCapSetTrailer() throws Exception {
    }
 
    public void testGetCapSetVatTable() throws Exception {
    }
 
    public void testGetCapSlpEmptySensor() throws Exception {
    }
 
    public void testGetCapSlpFiscalDocument() throws Exception {
    }
 
    public void testGetCapSlpFullSlip() throws Exception {
    }
 
    public void testGetCapSlpNearEndSensor() throws Exception {
    }
 
    public void testGetCapSlpPresent() throws Exception {
    }
 
    public void testGetCapSlpValidation() throws Exception {
    }
 
    public void testGetCapSubAmountAdjustment() throws Exception {
    }
 
    public void testGetCapSubPercentAdjustment() throws Exception {
    }
 
    public void testGetCapSubtotal() throws Exception {
    }
 
    public void testGetCapTrainingMode() throws Exception {
    }
 
    public void testGetCapValidateJournal() throws Exception {
    }
 
    public void testGetCapXReport() throws Exception {
    }
 
    public void testGetOutputID() throws Exception {
    }
 
    public void testGetPowerNotify() throws Exception {
    }
 
    public void testSetPowerNotify() throws Exception {
    }
 
    public void testGetPowerState() throws Exception {
    }
 
    public void testGetAmountDecimalPlace() throws Exception {
    }
 
    public void testGetAsyncMode() throws Exception {
    }
 
    public void testSetAsyncMode() throws Exception {
    }
 
    public void testGetCheckTotal() throws Exception {
    }
 
    public void testSetCheckTotal() throws Exception {
    }
 
    public void testGetCountryCode() throws Exception {
    }
 
    public void testGetCoverOpen() throws Exception {
    }
 
    public void testGetDayOpened() throws Exception {
    }
 
    public void testGetDescriptionLength() throws Exception {
    }
 
    public void testGetDuplicateReceipt() throws Exception {
    }
 
    public void testGetErrorLevel() throws Exception {
    }
 
    public void testGetErrorOutID() throws Exception {
    }
 
    public void testGetErrorState() throws Exception {
    }
 
    public void testGetErrorStation() throws Exception {
    }
 
    public void testGetErrorString() throws Exception {
    }
 
    public void testGetFlagWhenIdle() throws Exception {
    }
 
    public void testSetFlagWhenIdle() throws Exception {
    }
 
    public void testGetJrnEmpty() throws Exception {
    }
 
    public void testGetJrnNearEnd() throws Exception {
    }
 
    public void testGetMessageLength() throws Exception {
    }
 
    public void testGetNumHeaderLines() throws Exception {
    }
 
    public void testGetNumTrailerLines() throws Exception {
    }
 
    public void testGetNumVatRates() throws Exception {
    }
 
    public void testGetPredefinedPaymentLines() throws Exception {
    }
 
    public void testGetPrinterState() throws Exception {
    }
 
    public void testGetQuantityDecimalPlaces() throws Exception {
    }
 
    public void testGetQuantityLength() throws Exception {
    }
 
    public void testGetRecEmpty() throws Exception {
    }
 
    public void testGetRecNearEnd() throws Exception {
    }
 
    public void testGetRemainingFiscalMemory() throws Exception {
    }
 
    public void testGetReservedWord() throws Exception {
    }
 
    public void testGetSlpEmpty() throws Exception {
    }
 
    public void testGetSlpNearEnd() throws Exception {
    }
 
    public void testGetSlipSelection() throws Exception {
    }
 
    public void testSetSlipSelection() throws Exception {
    }
 
    public void testGetTrainingModeActive() throws Exception {
    }
 
    public void testGetCapAdditionalHeader() throws Exception {
    }
 
    public void testGetCapAdditionalTrailer() throws Exception {
    }
 
    public void testGetCapChangeDue() throws Exception {
    }
 
    public void testGetCapEmptyReceiptIsVoidable() throws Exception {
    }
 
    public void testGetCapFiscalReceiptStation() throws Exception {
    }
 
    public void testGetCapFiscalReceiptType() throws Exception {
    }
 
    public void testGetCapMultiContractor() throws Exception {
    }
 
    public void testGetCapOnlyVoidLastItem() throws Exception {
    }
 
    public void testGetCapPackageAdjustment() throws Exception {
    }
 
    public void testGetCapPostPreLine() throws Exception {
    }
 
    public void testGetCapSetCurrency() throws Exception {
    }
 
    public void testGetCapTotalizerType() throws Exception {
    }
 
    public void testGetActualCurrency() throws Exception {
    }
 
    public void testGetAdditionalHeader() throws Exception {
    }
 
    public void testSetAdditionalHeader() throws Exception {
    }
 
    public void testGetAdditionalTrailer() throws Exception {
    }
 
    public void testSetAdditionalTrailer() throws Exception {
    }
 
    public void testGetChangeDue() throws Exception {
    }
 
    public void testSetChangeDue() throws Exception {
    }
 
    public void testGetContractorId() throws Exception {
    }
 
    public void testSetContractorId() throws Exception {
    }
 
    public void testGetDateType() throws Exception {
    }
 
    public void testSetDateType() throws Exception {
    }
 
    public void testGetFiscalReceiptStation() throws Exception {
    }
 
    public void testSetFiscalReceiptStation() throws Exception {
    }
 
    public void testGetFiscalReceiptType() throws Exception {
    }
 
    public void testSetFiscalReceiptType() throws Exception {
    }
 
    public void testGetMessageType() throws Exception {
    }
 
    public void testSetMessageType() throws Exception {
    }
 
    public void testGetPostLine() throws Exception {
    }
 
    public void testSetPostLine() throws Exception {
    }
 
    public void testGetPreLine() throws Exception {
    }
 
    public void testSetPreLine() throws Exception {
    }
 
    public void testGetTotalizerType() throws Exception {
    }
 
    public void testSetTotalizerType() throws Exception {
    }
 
    public void testClaim() throws Exception {
    }
 
    public void testClose() throws Exception {
    }
 
    public void testCheckHealth() throws Exception {
    }
 
    public void testPrintImage() throws Exception {
    }
 
    public void testWriteTables() throws Exception {
    }
 
    public void testReadTables() throws Exception {
    }
 
    public void testDirectIO() throws Exception {
    }
 
    public void testOpen() throws Exception {
    }
 
    public void testRelease() throws Exception {
    }
 
    public void testBeginFiscalDocument() throws Exception {
    }
 
    public void testPrintFiscalDocumentLine() throws Exception {
    }
 
    public void testEndFiscalDocument() throws Exception {
    }
 
    public void testBeginFixedOutput() throws Exception {
    }
 
    public void testPrintFixedOutput() throws Exception {
    }
 
    public void testEndFixedOutput() throws Exception {
    }
 
    public void testBeginInsertion() throws Exception {
    }
 
    public void testBeginItemList() throws Exception {
    }
 
    public void testBeginRemoval() throws Exception {
    }
 
    public void testEndInsertion() throws Exception {
    }
 
    public void testEndItemList() throws Exception {
    }
 
    public void testEndRemoval() throws Exception {
    }
 
    public void testBeginNonFiscal() throws Exception {
    }
 
    public void testPrintNormalAsync() throws Exception {
    }
 
    public void testPrintNormal() throws Exception {
    }
 
    public void testEndNonFiscal() throws Exception {
    }
 
    public void testBeginTraining() throws Exception {
    }
 
    public void testEndTraining() throws Exception {
    }
 
    public void testClearError() throws Exception {
    }
 
    public void testClearOutput() throws Exception {
    }
 
    public void testGetTenderData() throws Exception {
    }
 
    public void testGetLineCountData() throws Exception {
    }
 
    public void testGetDataDescriptionLength() throws Exception {
    }
 
    public void testGetDate() throws Exception {
    }
 
    public void testGetTotalizer() throws Exception {
    }
 
    public void testBeginFiscalReceipt() throws Exception {
    }
 
    public void testPrintCashIn() throws Exception {
    }
 
    public void testPrintCashOut() throws Exception {
    }
 
    public void testPrintSalesReceipt() throws Exception {
    }
 
    public void testEndFiscalReceipt() throws Exception {
    }
 
    public void testPrintDuplicateReceipt() throws Exception {
    }
 
    public void testPrintPeriodicTotalsReport() throws Exception {
    }
 
    public void testPrintPowerLossReport() throws Exception {
    }
 
 
    public void testPrintRecMessageAsync() throws Exception {
    }
 
    public void testPrintRecMessage() throws Exception {
    }
 
    public void testCheckAdjustment() throws Exception {
    }
 
    public void testPrintRecItemAdjustmentAsync() throws Exception {
    }
 
    public void testPrintRecItemAdjustment() throws Exception {
    }
 
    public void testPrintRecItemFuelAsync() throws Exception {
    }
 
    public void testPrintRecItemFuelVoidAsync() throws Exception {
    }
 
    public void testPrintRecNotPaidAsync() throws Exception {
    }
 
    public void testPrintRecNotPaid() throws Exception {
    }
 
    public void testPrintRecRefund() throws Exception {
    }
 
    public void testPrintRecRefundAsync() throws Exception {
    }
 
    public void testPrintRecSubtotal() throws Exception {
    }
 
    public void testPrintRecSubtotalAsync() throws Exception {
    }
 
    public void testPrintRecSubtotalAdjustment() throws Exception {
    }
 
    public void testPrintRecSubtotalAdjustmentAsync() throws Exception {
    }
 
    public void testPrintRecTotal() throws Exception {
    }
 
    public void testPrintRecTotalAsync() throws Exception {
    }
 
    public void testPrintRecVoidAsync() throws Exception {
    }
 
    public void testPrintRecVoid() throws Exception {
    }
 
    public void testPrintRecVoidItem() throws Exception {
    }
 
    public void testPrintReport() throws Exception {
    }
 
    public void testPrintXReport() throws Exception {
    }
 
    public void testPrintZReport() throws Exception {
    }
 
    public void testResetPrinter() throws Exception {
    }
 
    public void testSetDate() throws Exception {
    }
 
    public void testSetHeaderLine() throws Exception {
    }
 
    public void testSetPOSID() throws Exception {
    }
 
    public void testSetStoreFiscalID() throws Exception {
    }
 
    public void testSetTrailerLine() throws Exception {
    }
 
    public void testGetVatEntry() throws Exception {
    }
 
    public void testSetVatTable() throws Exception {
    }
 
    public void testSetVatValue() throws Exception {
    }
 
    public void testVerifyItem() throws Exception {
    }
 
    public void testSetCurrency() throws Exception {
    }
 
    public void testPrintRecCashAsync() throws Exception {
    }
 
    public void testPrintRecCash() throws Exception {
    }
 
    public void testPrintRecItemFuel() throws Exception {
    }
 
    public void testPrintRecItemFuelVoid() throws Exception {
    }
 
    public void testPrintRecPackageAdjustment() throws Exception {
    }
 
    public void testPrintRecPackageAdjustmentAsync() throws Exception {
    }
 
    public void testPrintRecPackageAdjustVoid() throws Exception {
    }
 
    public void testPrintRecPackageAdjustVoidAsync() throws Exception {
    }
 
    public void testPrintRecRefundVoid() throws Exception {
    }
 
    public void testPrintRecRefundVoidAsync() throws Exception {
    }
 
    public void testPrintRecSubtotalAdjustVoid() throws Exception {
    }
 
    public void testPrintRecSubtotalAdjustVoidAsync() throws Exception {
    }
 
    public void testPrintRecTaxID() throws Exception {
    }
 
    public void testPrintRecTaxIDAsync() throws Exception {
    }
 
    public void testGetAmountDecimalPlaces() throws Exception {
    }
 
    public void testGetCapStatisticsReporting() throws Exception {
    }
 
    public void testGetCapUpdateStatistics() throws Exception {
    }
 
    public void testResetStatistics() throws Exception {
    }
 
    public void testRetrieveStatistics() throws Exception {
    }
 
    public void testUpdateStatistics() throws Exception {
    }
 
    public void testGetCapCompareFirmwareVersion() throws Exception {
    }
 
    public void testGetCapUpdateFirmware() throws Exception {
    }
 
    public void testCompareFirmwareVersion() throws Exception {
    }
 
    public void testUpdateFirmware() throws Exception {
    }
 
    public void testGetCapPositiveSubtotalAdjustment() throws Exception {
    }
 
    public void testPrintRecItemVoidAsync() throws Exception {
    }
 
    public void testPrintRecItemVoid() throws Exception {
    }
 
    public void testPrintRecItemAdjustmentVoidAsync() throws Exception {
    }
 
    public void testPrintRecItemAdjustmentVoid() throws Exception {
    }
 
    public void testGetPayType() throws Exception {
    }
 
    public void testCenterLine() throws Exception {
    }
 
    public void testGetFont() {
    }
 
    public void testEncodeText() {
    }
 
    public void testDecodeText() {
    }
 
    public void testPrintFiscalDocumentLineAsync() throws Exception {
    }
 
    public void testPrintFixedOutputAsync() throws Exception {
    }
 
    public void testGetMaxGraphicsHeight() throws Exception {
    }
 
    public void testCheckCapGraphics() throws Exception {
    }
 
    public void testPrintGraphics() throws Exception {
    }
 
    public void testLoadGraphics() throws Exception {
    }
 
 
    public void testGetMaxGraphicsWidth() throws Exception {
    }
 
    public void testPrintBarcode() throws Exception {
    }
 
    public void testPrintRecItemRefund() throws Exception {
    }
 
    public void testPrintRecItemRefundAsync() throws Exception {
    }
 
    public void testPrintRecItemRefundVoid() throws Exception {
    }
 
    public void testPrintRecItemRefundVoidAsync() throws Exception {
    }
 */
}
