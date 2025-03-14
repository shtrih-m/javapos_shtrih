package com.shtrih.jpos.fiscalprinter;

import com.shtrih.fiscalprinter.GS1Barcode;
import com.shtrih.barcode.PrinterBarcode;
import com.shtrih.ej.EJActivation;
import com.shtrih.ej.EJReportParser;
import com.shtrih.ej.EJStatus;
import com.shtrih.fiscalprinter.command.*;
import com.shtrih.fiscalprinter.FontNumber;
import com.shtrih.fiscalprinter.PrinterGraphics;
import com.shtrih.fiscalprinter.PrinterProtocol;
import com.shtrih.fiscalprinter.ProtocolFactory;
import com.shtrih.fiscalprinter.SMFiscalPrinter;
import com.shtrih.fiscalprinter.DeviceException;
import com.shtrih.fiscalprinter.SMFiscalPrinterImpl;
import com.shtrih.fiscalprinter.command.DeviceMetrics;
import com.shtrih.fiscalprinter.command.FDOParameters;
import com.shtrih.fiscalprinter.command.FMTotals;
import com.shtrih.fiscalprinter.command.FSPrintCalcReport;
import com.shtrih.fiscalprinter.command.FSReadCommStatus;
import com.shtrih.fiscalprinter.command.FlexCommands;
import com.shtrih.fiscalprinter.command.IPrinterEvents;
import com.shtrih.fiscalprinter.command.LongPrinterStatus;
import com.shtrih.fiscalprinter.command.PrinterCommand;
import com.shtrih.fiscalprinter.command.PrinterConst;
import com.shtrih.fiscalprinter.command.PrinterDate;
import com.shtrih.fiscalprinter.command.PrinterFlags;
import com.shtrih.fiscalprinter.command.PrinterMode;
import com.shtrih.fiscalprinter.command.PrinterModelParameters;
import com.shtrih.fiscalprinter.command.PrinterStatus;
import com.shtrih.fiscalprinter.command.PrinterTime;
import com.shtrih.fiscalprinter.command.ReadEJStatus;
import com.shtrih.fiscalprinter.command.ReadFMLastRecordDate;
import com.shtrih.fiscalprinter.command.ReadShortStatus;
import com.shtrih.fiscalprinter.command.ShortPrinterStatus;
import com.shtrih.fiscalprinter.command.TextDocumentFilter;
import com.shtrih.fiscalprinter.command.TextLine;
import com.shtrih.fiscalprinter.model.PrinterModel;
import com.shtrih.fiscalprinter.port.PrinterPort;
import com.shtrih.fiscalprinter.port.PrinterPortWrapper;
import com.shtrih.fiscalprinter.port.PrinterPortFactory;
import com.shtrih.fiscalprinter.receipt.PrinterReceipt;
import com.shtrih.fiscalprinter.table.CsvTablesReader;
import com.shtrih.fiscalprinter.table.CsvTablesWriter;
import com.shtrih.fiscalprinter.table.PrinterField;
import com.shtrih.fiscalprinter.table.PrinterFields;
import com.shtrih.fiscalprinter.table.PrinterTable;
import com.shtrih.fiscalprinter.table.PrinterTables;
import com.shtrih.jpos.DeviceService;
import com.shtrih.jpos.StatusUpdateEventHelper;
import com.shtrih.jpos.events.ErrorEventRequest;
import com.shtrih.jpos.events.OutputCompleteEventRequest;
import com.shtrih.jpos.events.StatusUpdateEventRequest;
import com.shtrih.jpos.fiscalprinter.directIO.DirectIOHandler;
import com.shtrih.jpos.fiscalprinter.directIO.DirectIOHandler2;
import com.shtrih.jpos.fiscalprinter.receipt.CashInReceipt;
import com.shtrih.jpos.fiscalprinter.receipt.CashOutReceipt;
import com.shtrih.jpos.fiscalprinter.receipt.FSSalesReceipt;
import com.shtrih.jpos.fiscalprinter.receipt.FiscalReceipt;
import com.shtrih.jpos.fiscalprinter.receipt.GlobusSalesReceipt;
import com.shtrih.jpos.fiscalprinter.receipt.NonfiscalReceipt;
import com.shtrih.jpos.fiscalprinter.receipt.NullReceipt;
import com.shtrih.jpos.fiscalprinter.receipt.SalesReceipt;
import com.shtrih.jpos.fiscalprinter.request.FiscalPrinterRequest;
import com.shtrih.jpos.fiscalprinter.request.PrintNormalRequest;
import com.shtrih.jpos.fiscalprinter.request.PrintRecCashRequest;
import com.shtrih.jpos.fiscalprinter.request.PrintRecItemAdjustmentRequest;
import com.shtrih.jpos.fiscalprinter.request.PrintRecItemAdjustmentVoidRequest;
import com.shtrih.jpos.fiscalprinter.request.PrintRecItemRefundRequest;
import com.shtrih.jpos.fiscalprinter.request.PrintRecItemRefundVoidRequest;
import com.shtrih.jpos.fiscalprinter.request.PrintRecItemRequest;
import com.shtrih.jpos.fiscalprinter.request.PrintRecItemVoidRequest;
import com.shtrih.jpos.fiscalprinter.request.PrintRecMessageRequest;
import com.shtrih.jpos.fiscalprinter.request.PrintRecPackageAdjustVoidRequest;
import com.shtrih.jpos.fiscalprinter.request.PrintRecPackageAdjustmentRequest;
import com.shtrih.jpos.fiscalprinter.request.PrintRecRefundRequest;
import com.shtrih.jpos.fiscalprinter.request.PrintRecRefundVoidRequest;
import com.shtrih.jpos.fiscalprinter.request.PrintRecSubtotalAdjustVoidRequest;
import com.shtrih.jpos.fiscalprinter.request.PrintRecSubtotalAdjustmentRequest;
import com.shtrih.jpos.fiscalprinter.request.PrintRecSubtotalRequest;
import com.shtrih.jpos.fiscalprinter.request.PrintRecTaxIDRequest;
import com.shtrih.jpos.fiscalprinter.request.PrintRecTotalRequest;
import com.shtrih.jpos.fiscalprinter.request.PrintRecVoidRequest;
import com.shtrih.jpos.monitoring.MonitoringServerX5;
import com.shtrih.printer.ncr7167.NCR7167Printer;
import com.shtrih.util.CompositeLogger;
import com.shtrih.util.FileUtils;
import com.shtrih.util.FirmwareUpdater;
import com.shtrih.util.Hex;
import com.shtrih.util.Localizer;
import com.shtrih.util.LogWriter;
import com.shtrih.util.ServiceVersion;
import com.shtrih.util.ServiceVersionUtil;
import com.shtrih.util.StringUtils;
import com.shtrih.util.SysUtils;
import com.shtrih.jpos.fiscalprinter.JsonUpdateService;
import com.shtrih.util.Time;
import com.shtrih.fiscalprinter.MCNotifications;
import com.shtrih.fiscalprinter.MCNotificationsReport;
import com.shtrih.jpos.fiscalprinter.receipt.FSTextReceiptItem;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import java.util.List;
import java.util.Calendar;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Set;

import jpos.FiscalPrinterConst;
import jpos.JposConst;
import jpos.JposException;
import jpos.config.JposEntry;
import jpos.config.JposEntryConst;
import jpos.config.simple.SimpleEntry;
import jpos.events.ErrorEvent;
import jpos.events.OutputCompleteEvent;
import jpos.events.StatusUpdateEvent;
import jpos.services.EventCallbacks;

public class FiscalPrinterImpl extends DeviceService implements PrinterConst,
        JposConst, JposEntryConst, FiscalPrinterConst,
        IPrinterEvents {

    private CompositeLogger logger = CompositeLogger.getLogger(FiscalPrinterImpl.class);

    public int logoPosition = SmFptrConst.SMFPTR_LOGO_PRINT;
    private final FptrParameters params;
    private boolean setPrinterFDOMode = true;
    private boolean freezeEvents = true;
    private final FiscalPrinterFilters filters = new FiscalPrinterFilters();
    private Thread asyncThread = null;
    private Thread deviceThread = null;
    private Thread eventThread = null;
    private boolean deviceEnabled = false;
    private EventCallbacks cb = null;
    private final Vector events = new Vector();
    private final FontNumber doubleWidthFont;
    private final FiscalDay fiscalDay = new FiscalDay();
    private final Vector requests = new Vector();
    private PrinterHeader header;
    private HashMap<Integer, Integer> vatEntries = new HashMap<Integer, Integer>();
    private PrinterPort port;
    private PrinterProtocol device = null;
    private SMFiscalPrinter printer;
    private final FiscalPrinterStatistics statistics;
    // --------------------------------------------------------------------------
    // Variables
    // --------------------------------------------------------------------------
    // Instance Data Set in Derived Class
    private boolean claimed = false;
    private String checkHealthText = "";
    private String physicalDeviceDescription = "";
    private String deviceServiceDescription = "";
    private int deviceServiceVersion;
    private String physicalDeviceName = "";
    private int state = JposConst.JPOS_S_CLOSED;
    public final FiscalPrinterState printerState = new FiscalPrinterState();
    // Capabilities
    private boolean capAdditionalLines;
    private boolean capAmountAdjustment;
    private boolean capAmountNotPaid;
    private boolean capCheckTotal;
    private boolean capFixedOutput;
    private boolean capIndependentHeader;
    private boolean capItemList;
    private boolean capNonFiscalMode;
    private boolean capOrderAdjustmentFirst;
    private boolean capPercentAdjustment;
    private boolean capPositiveAdjustment;
    private boolean capPowerLossReport;
    private boolean capPredefinedPaymentLines;
    private boolean capReceiptNotPaid;
    private boolean capRemainingFiscalMemory;
    private boolean capReservedWord;
    private boolean capSetHeader;
    private boolean capSetPOSID;
    private boolean capSetStoreFiscalID;
    private boolean capSetTrailer;
    private boolean capSetVatTable;
    private boolean capSlpFiscalDocument;
    private boolean capSlpFullSlip;
    private boolean capSlpValidation;
    private boolean capSubAmountAdjustment;
    private boolean capSubPercentAdjustment;
    private boolean capSubtotal;
    private boolean capTrainingMode;
    private boolean capValidateJournal;
    private boolean capXReport;
    // 1.6
    public boolean capAdditionalHeader;
    public boolean capAdditionalTrailer;
    public boolean capChangeDue;
    private boolean capEmptyReceiptIsVoidable;
    private boolean capFiscalReceiptStation;
    private boolean capFiscalSlipStation;
    private boolean capFiscalReceiptType;
    private boolean capMultiContractor;
    private boolean capOnlyVoidLastItem;
    private boolean capPackageAdjustment;
    private boolean capPostPreLine;
    private boolean capSetCurrency;
    private boolean capTotalizerType;
    // 1.8
    private boolean capStatisticsReporting;
    private boolean capUpdateStatistics;
    // 1.9
    private boolean capCompareFirmwareVersion;
    private boolean capUpdateFirmware;
    // 1.11
    private boolean capPositiveSubtotalAdjustment;
    // State
    private boolean duplicateReceipt;
    private int outputID;
    private int powerNotify = JPOS_PN_ENABLED;
    private int powerState;
    private int countryCode;
    private int errorLevel;
    private int errorOutID;
    private int errorState;
    private int errorStation;
    private String errorString = "";
    private boolean flagWhenIdle;
    private String predefinedPaymentLines = "";
    private int quantityDecimalPlaces;
    private int quantityLength;
    private boolean recEmpty;
    private boolean recNearEnd;
    private boolean jrnEmpty;
    private boolean jrnNearEnd;
    private boolean slpEmpty;
    private boolean slpNearEnd;
    private int slipSelection;
    private boolean trainingModeActive;
    private boolean coverOpened = false;
    private String reservedWord = "";
    // 1.6
    private int actualCurrency;
    private String additionalHeader = "";
    private String additionalTrailer = "";
    private String changeDue = "";
    private int contractorId;
    private int dateType;
    private int fiscalReceiptStation;
    private int fiscalReceiptType;
    private int messageType;
    private int totalizerType;
    private int graphicsLine = 1;
    private boolean isReceiptOpened = false;
    private String logicalName = "";
    private boolean asyncMode = false;
    private FiscalReceipt receipt = new NullReceipt();
    private boolean connected = false;
    private boolean isLicenseValid = false;
    private final MonitoringServerX5 monitoringServer = new MonitoringServerX5(this);
    private int receiptType = 0;
    private boolean isRecPresent = true;
    private boolean filterEnabled = true;
    private boolean isInReceiptTrailer = false;
    private TextDocumentFilter filter = new TextDocumentFilter();
    private FDOService fdoService;
    private FirmwareUpdaterService firmwareUpdaterService;
    private boolean docEndEnabled = true;
    private JsonUpdateService jsonUpdateService = null;
    private volatile boolean pollStopFlag = false;
    private volatile boolean eventStopFlag = false;
    private TextGenerator textGenerator = null;
    private List<String> documentLines = new Vector<String>();

    public TextDocumentFilter getTextDocumentFilter() {
        return filter;
    }

    public void setFirmwareUpdateObserver(FirmwareUpdateObserver observer) {
        if (firmwareUpdaterService != null) {
            firmwareUpdaterService.setListener(observer);
        }
    }

    class DeviceTarget implements Runnable {

        private final FiscalPrinterImpl fiscalPrinter;

        public DeviceTarget(FiscalPrinterImpl fiscalPrinter) {
            this.fiscalPrinter = fiscalPrinter;
        }

        public void run() {
            fiscalPrinter.deviceProc();
        }
    }

    class EventTarget implements Runnable {

        private final FiscalPrinterImpl fiscalPrinter;

        public EventTarget(FiscalPrinterImpl fiscalPrinter) {
            this.fiscalPrinter = fiscalPrinter;
        }

        public void run() {
            fiscalPrinter.eventProc();
        }
    }

    class AsyncTarget implements Runnable {

        private final FiscalPrinterImpl fiscalPrinter;

        public AsyncTarget(FiscalPrinterImpl fiscalPrinter) {
            this.fiscalPrinter = fiscalPrinter;
        }

        public void run() {
            fiscalPrinter.asyncProc();
        }
    }

    // --------------------------------------------------------------------------
    // Constructor
    // --------------------------------------------------------------------------
    public FiscalPrinterImpl() throws Exception {
        params = new FptrParameters();
        doubleWidthFont = FontNumber.getNormalFont();
        statistics = new FiscalPrinterStatistics();
        statistics.unifiedPOSVersion = "1.13";
        statistics.deviceCategory = "CashDrawer";
        statistics.manufacturerName = "SHTRIH-M";
        statistics.modelName = "Fiscal printer";
        statistics.serialNumber = "";
        statistics.firmwareRevision = "";
        statistics.physicalInterface = RS232_DEVICE_BUS;
        statistics.installationDate = "";
        initializeData();
    }

    private void initializeData() {
        state = JPOS_S_CLOSED;
        if (printer != null && printer.getCapFiscalStorage() && !params.FSDiscountEnabled) {

            capPositiveSubtotalAdjustment = false;
            capAmountAdjustment = false;
            capOrderAdjustmentFirst = false;
            capPercentAdjustment = false;
            capPositiveAdjustment = false;
            capSubAmountAdjustment = false;
            capSubPercentAdjustment = false;
            capPackageAdjustment = false;
        } else {
            capPositiveSubtotalAdjustment = true;
            capAmountAdjustment = true;
            capOrderAdjustmentFirst = false;
            capPercentAdjustment = true;
            capPositiveAdjustment = true;
            capSubAmountAdjustment = true;
            capSubPercentAdjustment = true;
            capPackageAdjustment = true;
        }
        deviceServiceDescription = "Fiscal Printer Service , SHTRIH-M, 2016";
        capAdditionalLines = true;

        capAmountNotPaid = false;
        capCheckTotal = true;
        capFixedOutput = false;
        capIndependentHeader = true;
        capItemList = false;
        capNonFiscalMode = true;

        capPowerLossReport = false;
        capPredefinedPaymentLines = true;
        capReceiptNotPaid = false;
        capRemainingFiscalMemory = true;
        capReservedWord = false;
        capSetHeader = true;
        capSetPOSID = true;
        capSetStoreFiscalID = false;
        capSetTrailer = true;
        capSetVatTable = true;
        capSlpFiscalDocument = false;
        capSlpFullSlip = false;
        capSlpValidation = false;

        capSubtotal = true;
        capTrainingMode = false;
        capValidateJournal = false;
        capXReport = true;
        capAdditionalHeader = true;
        capAdditionalTrailer = true;
        capChangeDue = false;
        capEmptyReceiptIsVoidable = true;
        capFiscalReceiptStation = true;
        capFiscalSlipStation = false;
        capFiscalReceiptType = true;
        capMultiContractor = false;
        capOnlyVoidLastItem = false;

        capPostPreLine = true;
        capSetCurrency = false;
        capTotalizerType = true;
        capCompareFirmwareVersion = true;
        capUpdateFirmware = false;
        // state
        duplicateReceipt = false;
        outputID = 0;
        powerNotify = JPOS_PN_ENABLED;
        powerState = JPOS_PS_UNKNOWN;
        countryCode = FPTR_CC_RUSSIA;
        errorLevel = FPTR_EL_NONE;
        errorOutID = 0;
        errorState = 0;
        errorStation = 0;
        errorString = "";
        flagWhenIdle = false;
        jrnEmpty = false;
        jrnNearEnd = false;
        predefinedPaymentLines = "";
        printerState.setValue(FPTR_PS_MONITOR);
        quantityDecimalPlaces = 6;
        quantityLength = 15;
        recEmpty = false;
        recNearEnd = false;
        reservedWord = "";
        slpEmpty = false;
        slpNearEnd = false;
        slipSelection = FPTR_SS_FULL_LENGTH;
        trainingModeActive = false;
        actualCurrency = FPTR_AC_RUR;
        additionalHeader = "";
        additionalTrailer = "";
        changeDue = "";
        contractorId = FPTR_CID_SINGLE;
        dateType = FPTR_DT_RTC;
        fiscalReceiptStation = FPTR_RS_RECEIPT;
        fiscalReceiptType = FPTR_RT_SALES;
        messageType = FPTR_MT_FREE_TEXT;
        totalizerType = FPTR_TT_DAY;
        capUpdateStatistics = true;
        capStatisticsReporting = true;
        deviceServiceVersion = deviceVersion113 + ServiceVersionUtil.getVersionInt();
    }

    public SMFiscalPrinter getPrinter() throws Exception {
        if (printer == null) {
            throw new Exception("Printer is not initialized");
        }
        return printer;
    }

    public String[] parseText(String text) throws Exception {
        return getPrinter().splitText(text, params.font);
    }

    public void setPrinter(SMFiscalPrinter printer) {
        this.printer = printer;
    }

    public void handleException(Throwable e) throws JposException {
        JposExceptionHandler.handleException(e);
    }

    public PrinterStatus readPrinterStatus() throws Exception {
        return getPrinter().readPrinterStatus();
    }

    public FlexCommands getCommands() throws Exception {
        return printer.getCommands();
    }

    public PrinterModel getModel() throws Exception {
        return getPrinter().getModel();
    }

    // --------------------------------------------------------------------------
    // Properties
    // --------------------------------------------------------------------------
    public String getCheckHealthText() throws Exception {
        checkOpened();
        return encodeText(checkHealthText);
    }

    public boolean getClaimed() throws Exception {
        checkOpened();
        return claimed;
    }

    public boolean getDeviceEnabled() throws Exception {
        return deviceEnabled;
    }

    public void setPowerState(int powerState) {
        if (powerNotify == JPOS_PN_ENABLED) {
            if (powerState != this.powerState) {
                switch (powerState) {
                    case JPOS_PS_ONLINE:
                        statusUpdateEvent(JPOS_SUE_POWER_ONLINE);
                        break;

                    case JPOS_PS_OFF:
                        statusUpdateEvent(JPOS_SUE_POWER_OFF);
                        break;

                    case JPOS_PS_OFFLINE:
                        statusUpdateEvent(JPOS_SUE_POWER_OFFLINE);
                        break;

                    case JPOS_PS_OFF_OFFLINE:
                        statusUpdateEvent(JPOS_SUE_POWER_OFF_OFFLINE);
                        break;
                }
            }
        }
        this.powerState = powerState;
    }

    private void setState(int value) {
        if (value != state) {
            state = value;
            if ((state == JPOS_S_IDLE) && (flagWhenIdle)) {
                statusUpdateEvent(FPTR_SUE_IDLE);
                flagWhenIdle = false;
            }
        }
    }

    private void addEvent(Runnable event) {
        synchronized (events) {
            events.add(event);
            events.notifyAll();
        }
    }

    private void statusUpdateEvent(int value) {
        logger.debug("statusUpdateEvent("
                + StatusUpdateEventHelper.getName(value) + ")");

        addEvent(new StatusUpdateEventRequest(cb, new StatusUpdateEvent(this,
                value)));
    }

    private void outputCompleteEvent(int outputID) {
        logger.debug("outputCompleteEvent(" + String.valueOf(outputID) + ")");

        addEvent(new OutputCompleteEventRequest(cb, new OutputCompleteEvent(
                this, outputID)));
    }

    public void disableDocEnd() throws Exception {
        docEndEnabled = false;
    }

    private void checkPaperStatus(PrinterStatus status) throws Exception {
        if (isRecPresent) {
            isRecPresent = status.getPrinterFlags().isRecPresent();
        }

        if (status.getPrinterFlags().isRecPresent()) {
            if (isInReceiptTrailer) {
                printEndNonFiscal();
                isInReceiptTrailer = false;
                isRecPresent = true;
            }
        }
    }

    public void beforeCommand(PrinterCommand command) throws Exception {
    }

    public void afterCommand(PrinterCommand command) throws Exception {
        if (!filterEnabled) {
            return;
        }
        try {
            filterEnabled = false;
            setPowerState(JPOS_PS_ONLINE);
            if (command.isFailed()) {
                int errorCode = command.getResultCode() + 300;
                logger.debug("ErrorEvent(JPOS_E_EXTENDED, " + errorCode + ")");
                ErrorEvent event = new ErrorEvent(this, JPOS_E_EXTENDED,
                        errorCode, JPOS_EL_OUTPUT, 0);
                addEvent(new ErrorEventRequest(cb, event));
            }

            if (command.isSucceeded()) {
                switch (command.getCode()) {
                    case 0x10: {
                        if (command instanceof ReadShortStatus) {
                            ShortPrinterStatus shortStatus = ((ReadShortStatus) command).getStatus();
                            updateStatus(shortStatus.getPrinterStatus());
                        }
                    }
                    break;
                    case 0x11: {
                        if (command instanceof ReadLongStatus) {
                            LongPrinterStatus longStatus = ((ReadLongStatus) command).getStatus();
                            updateStatus(longStatus.getPrinterStatus());
                        }
                    }
                    break;

                }
            }

            switch (command.getResultCode()) {
                case SMFP_EFPTR_FS_INVALID_STATE:
                    if (getPrinter().getCapFiscalStorage()) {
                        getPrinter().fsReadStatus();
                    }
                    break;

                case SMFP_EFPTR_NO_REC_PAPER:
                    isRecPresent = false;
                    setRecPaperState(true, recNearEnd);
                    break;

                case SMFP_EFPTR_NO_JRN_PAPER:
                    setJrnPaperState(true, jrnNearEnd);
                    break;

                case SMFP_EFPTR_NO_SLP_PAPER:
                    setSlpPaperState(true, slpNearEnd);
                    break;

                case SMFP_EFPTR_WAIT_PRINT_CONTINUE:
                    getPrinter().continuePrint();
                    PrinterStatus status = getPrinter().waitForPrinting();
                    checkPaperStatus(status);
                    command.setRepeatNeeded(true);
                    break;

                case SMFP_EFPTR_FS_DATE_TIME:
                    FSReadStatus fsStatus = printer.fsReadStatus();
                    logger.debug("FS document date: " + fsStatus.getDate().toString());
                    logger.debug("FS document time: " + fsStatus.getTime().toString());
                    break;

                case SMFP_EFPTR_PREVCOMMAND:
                    printer.waitForPrinting();
                    command.setRepeatNeeded(true);
                    break;

                case SMFP_EFPTR_INVALID_MODE:
                case SMFP_EFPTR_INVALID_SUBMODE:
                    status = getPrinter().readPrinterStatus();
                    logger.debug("Mode: " + status.getPrinterMode().getText()
                            + ", submode: " + status.getPrinterSubmode().getText());
                    break;

                case SMFP_EFPTR_FM_CONNECT_ERROR:
                    printer.waitForFiscalMemory();
                    command.setRepeatNeeded(true);
                    break;

                case SMFP_EFPTR_EJ_CONNECT_ERROR:
                case SMFP_EFPTR_EJ_MISSING:
                    if (!printer.getCapFiscalStorage()) {
                        printer.waitForElectronicJournal();
                        command.setRepeatNeeded(true);
                    }
                    break;

                case SMFP_EFPTR_RECBUF_OVERFLOW:
                    boolean recOpened = printer.readPrinterStatus().getPrinterMode().isReceiptOpened();
                    if (!recOpened) {
                        Time.delay(1000);
                        command.setRepeatNeeded(true);
                    }
                    break;

            }
        } finally {
            filterEnabled = true;
        }
    }

    public void setRecPaperState(boolean recEmpty, boolean recNearEnd)
            throws Exception {

        int state = getRecPaperState(recEmpty, recNearEnd);
        int curState = getRecPaperState(this.recEmpty, this.recNearEnd);
        if (state != curState) {
            statusUpdateEvent(state);
        }
        this.recEmpty = recEmpty;
        this.recNearEnd = recNearEnd;
    }

    public int getRecPaperState(boolean recEmpty, boolean recNearEnd)
            throws Exception {
        if (getCapRecPresent()) {
            if (!getCapRecEmptySensor()) {
                recEmpty = false;
            }
            if (!getCapRecNearEndSensor()) {
                recNearEnd = false;
            }
            if (recEmpty) {
                return FiscalPrinterConst.FPTR_SUE_REC_EMPTY;
            }
            if (recNearEnd) {
                return FiscalPrinterConst.FPTR_SUE_REC_NEAREMPTY;
            }
        }
        return FiscalPrinterConst.FPTR_SUE_REC_PAPEROK;
    }

    public int getJrnPaperState(boolean jrnEmpty, boolean jrnNearEnd)
            throws Exception {
        if (getCapJrnPresent()) {
            if (!getCapJrnEmptySensor()) {
                jrnEmpty = false;
            }
            if (!getCapJrnNearEndSensor()) {
                jrnNearEnd = false;
            }
            if (jrnEmpty) {
                return FiscalPrinterConst.FPTR_SUE_JRN_EMPTY;
            }
            if (jrnNearEnd) {
                return FiscalPrinterConst.FPTR_SUE_JRN_NEAREMPTY;
            }
        }
        return FiscalPrinterConst.FPTR_SUE_JRN_PAPEROK;
    }

    public int getSlpPaperState(boolean slpEmpty, boolean slpNearEnd)
            throws Exception {
        if (getCapSlpPresent()) {
            if (!getCapSlpEmptySensor()) {
                slpEmpty = false;
            }
            if (!getCapSlpNearEndSensor()) {
                slpNearEnd = false;
            }
            if (slpEmpty) {
                return FiscalPrinterConst.FPTR_SUE_SLP_EMPTY;
            }
            if (slpNearEnd) {
                return FiscalPrinterConst.FPTR_SUE_SLP_NEAREMPTY;
            }
        }
        return FiscalPrinterConst.FPTR_SUE_SLP_PAPEROK;
    }

    public void setJrnPaperState(boolean jrnEmpty, boolean jrnNearEnd)
            throws Exception {

        int state = getJrnPaperState(jrnEmpty, jrnNearEnd);
        int curState = getJrnPaperState(this.jrnEmpty, this.jrnNearEnd);
        if (state != curState) {
            statusUpdateEvent(state);
        }
        this.jrnEmpty = jrnEmpty;
        this.jrnNearEnd = jrnNearEnd;
    }

    public void setSlpPaperState(boolean slpEmpty, boolean slpNearEnd)
            throws Exception {

        int state = getSlpPaperState(slpEmpty, slpNearEnd);
        int curState = getSlpPaperState(this.slpEmpty, this.slpNearEnd);
        if (state != curState) {
            statusUpdateEvent(state);
        }
        this.slpEmpty = slpEmpty;
        this.slpNearEnd = slpNearEnd;
    }

    private void setCoverState(boolean isCoverOpened) throws Exception {
        if (getCapCoverSensor()) {
            if (isCoverOpened != coverOpened) {
                if (isCoverOpened) {
                    statusUpdateEvent(FPTR_SUE_COVER_OPEN);
                } else {
                    statusUpdateEvent(FPTR_SUE_COVER_OK);
                }
                coverOpened = isCoverOpened;
            }
        }
    }

    public void execute(FiscalPrinterRequest request) throws Exception {
        if (asyncMode) {
            setState(JPOS_S_BUSY);
            synchronized (requests) {
                requests.add(request);
                requests.notifyAll();
                outputID = request.getId();
            }
        } else {
            if (state == JPOS_S_BUSY) {
                throw new JposException(JPOS_E_BUSY);
            }

            checkEnabled();
            synchronized (printer) {
                request.execute(this);
            }
        }
    }

    private void executePrinterRequest(FiscalPrinterRequest request) {
        while (true) {
            try {
                request.execute(this);
                outputCompleteEvent(request.getId());
                break;
            } catch (Exception e) {
                JposException jpose = JposExceptionHandler.getJposException(e);

                setState(JPOS_S_ERROR);
                switch (jpose.getErrorCode()) {
                    case JPOS_E_TIMEOUT:
                        setPowerState(JPOS_PS_OFFLINE);
                        break;
                }

                ErrorEvent event = new ErrorEvent(this, jpose.getErrorCode(),
                        jpose.getErrorCodeExtended(), JPOS_EL_OUTPUT,
                        JPOS_ER_CLEAR);

                /*
                 * if (getFreezeEvents()) { events.add(new ErrorEventRequest(cb,
                 * event)); } else { if (cb != null) cb.fireErrorEvent(event);
                 * if (event.getErrorResponse() != JPOS_ER_RETRY) break; }
                 */
            }
        }
    }

    // event delivery routine
    public void eventProc() {
        try {
            while (!eventStopFlag) {
                synchronized (events) {
                    while (!events.isEmpty()) {
                        ((Runnable) events.remove(0)).run();

                    }
                    events.wait();
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void asyncProc() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                synchronized (requests) {
                    while (!requests.isEmpty()) {
                        setState(JPOS_S_BUSY);
                        executePrinterRequest((FiscalPrinterRequest) requests
                                .remove(0));
                    }
                    requests.wait();
                }
                setState(JPOS_S_IDLE);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void updateStatus(PrinterStatus status) throws Exception {

        PrinterFlags flags = status.getPrinterFlags();
        setRecPaperState(flags.isRecEmpty(), flags.isRecNearEnd());
        setJrnPaperState(flags.isJrnEmpty(), flags.isJrnNearEnd());
        setSlpPaperState(flags.isSlpEmpty(), flags.isSlpNearEnd());
        setCoverState(flags.isCoverOpened());
    }

    private void startPoll() throws Exception {
        if (deviceThread == null) {
            logger.debug("Poll thread starting...");
            pollStopFlag = false;
            deviceThread = new Thread(new DeviceTarget(this));
            deviceThread.start();
        }
    }

    private void stopPoll() throws Exception {
        if (deviceThread != null) {
            logger.debug("Poll thread stopping...");
            pollStopFlag = true;
            deviceThread.interrupt();
            deviceThread.join();
            deviceThread = null;
        }
    }

    public void setDeviceEnabled(boolean deviceEnabled) throws Exception {
        logger.debug("setDeviceEnabled(" + deviceEnabled + ")");
        checkClaimed();
        if (this.deviceEnabled != deviceEnabled) {
            if (deviceEnabled) {
                physicalDeviceDescription = null;
                physicalDeviceName = null;

                LongPrinterStatus longStatus = getPrinter().searchDevice();

                connected = true;
                setPowerState(JPOS_PS_ONLINE);
                setJrnPaperState(true, true);

                updateDeviceMetrics(longStatus);

                getPrinterImages().setMaxSize(getMaxGraphicsHeight());

                cancelReceipt();
                getPrinter().initialize();

                if (!params.fastConnect) {
                    writeTables();
                }

                header = createHeader();
                header.initDevice(); // TODO: make lazy

                if (!params.fastConnect) {
                    loadProperties();
                    updateCommandTimeouts();
                }
                setPrinterFDOMode(PrinterConst.FDO_MODE_PRINTER);
                isTablesRead = false;
                capSetVatTable = getPrinter().getCapSetVatTable();
                capUpdateFirmware = getPrinter().getCapUpdateFirmware();

                // if polling enabled - create device thread
                if (params.pollEnabled) {
                    startPoll();
                }
                try {
                    startFDOService();
                } catch (Exception e) {
                    logger.error("Failed to start FSService", e);
                }
                try {
                    startFirmwareUpdaterService();
                } catch (Exception e) {
                    logger.error("Failed to start FirmwareUpdaterService", e);
                }
                // JSON update service
                try {
                    startJsonUpdateService();
                } catch (Exception e) {
                    logger.error("Failed to start JsonUpdateService", e);
                }

                filter.init(getPrinter());
                readLastDocument();
                getPrinter().addEvents(filter);
                if (params.textReportEnabled || (params.duplicateReceipt == SmFptrConst.DUPLICATE_RECEIPT_DRIVER)) {
                    filter.setEnabled(true);
                }

            } else {
                saveProperties();
                stopPoll();
                stopFDOService();
                stopJsonUpdateService();
                stopFirmwareUpdaterService();
                getPrinter().disconnect();
                getPrinter().closePort();
                connected = false;
                setPowerState(JPOS_PS_UNKNOWN);
            }
            this.deviceEnabled = deviceEnabled;
            logger.debug("setDeviceEnabled: OK");
        }
    }

    public void updateCommandTimeouts() {
        try {
            String[] value = new String[1];
            int rc = getPrinter().readTable(19, 1, 8, value);
            if (getPrinter().succeeded(rc)) {
                // 0xFF61 command timeout
                int kmtimeout = 1000 * Integer.parseInt(value[0]);
                int timeout = getPrinter().getCommandTimeout(0xFF61);
                if (timeout < kmtimeout) {
                    getPrinter().setCommandTimeout(0xFF61, kmtimeout + 1000);
                }
            }
        } catch (Exception e) {
            logger.error(e);
        }
    }

    public void startFDOService() {
        if (fdoService != null) {
            return;
        }

        if (isFDOServiceEnabled()) {
            fdoService = new FDOService(printer);
            fdoService.start();
        }
    }

    public boolean isFDOServiceEnabled() {
        if (!printer.capFDOSupport()) {
            logger.debug("FDO commands not supported, capFDOSupport=false");
            return false;
        }

        if (params.fdoMode != SmFptrConst.FDO_MODE_ENABLED) {
            logger.debug("FDO mode not enabled, fdoMode=" + params.fdoMode);
            return false;
        }
        return true;
    }

    public void startFirmwareUpdaterService() throws Exception {
        if (firmwareUpdaterService != null) {
            return;
        }

        if (!params.capScocUpdateFirmware) {
            return;
        }

        if (!printer.getCapFiscalStorage()) {
            return;
        }

        if (!printer.isDesktop() && !printer.isShtrihNano() && !printer.isCashCore()) {
            logger.debug("FirmwareUpdaterService stopped, unsupported device");
            return;
        }

        firmwareUpdaterService = new FirmwareUpdaterService(printer);
        firmwareUpdaterService.start();
    }

    public void startJsonUpdateService() throws Exception {
        if (jsonUpdateService != null) {
            return;
        }

        if (!params.jsonUpdateEnabled) {
            return;
        }

        if (!printer.getCapFiscalStorage()) {
            return;
        }

        jsonUpdateService = new JsonUpdateService(printer);
        jsonUpdateService.start();
    }

    public void stopJsonUpdateService() {
        if (jsonUpdateService == null) {
            return;
        }

        try {
            jsonUpdateService.stop();
            jsonUpdateService = null;
        } catch (Exception e) {
            logger.error("Failed to stop jsonUpdateService", e);
        }
    }

    public boolean isPPP() {
        int[] data = new int[1];
        data[0] = 0;
        port.directIO(PrinterPort.DIO_REPORT_IS_PPP, data, null);
        return data[0] == 1;
    }

    public void setPrinterFDOMode(int mode) {
        try {
            if (!setPrinterFDOMode) {
                return;
            }

            if (params.fdoMode != SmFptrConst.FDO_MODE_DISABLE_IN_REC) {
                return;
            }

            if (isPPP()) {
                printer.writeTable(21, 1, 2, String.valueOf(mode));
            }
        } catch (Exception e) {
            setPrinterFDOMode = false;
            logger.error("setPrinterFDOMode ", e);
        }
    }

    public void stopFDOService() {
        if (fdoService == null) {
            return;
        }

        try {
            fdoService.stop();
            fdoService = null;
        } catch (Exception e) {
            logger.error("Failed to stop fsSenderService", e);
        }
    }

    public void stopFirmwareUpdaterService() {
        if (firmwareUpdaterService == null) {
            return;
        }

        //printer.removeEvents(firmwareUpdaterService);
        try {
            firmwareUpdaterService.stop();
            firmwareUpdaterService = null;
        } catch (Exception e) {
            logger.error("Failed to stop firmwareUpdaterService", e);
        }
    }

    public boolean isFSServiceRunning() {
        return (fdoService != null) && (fdoService.isStarted());
    }

    public boolean isFirmwareUpdaterServiceRunning() {
        return firmwareUpdaterService != null;
    }

    public void setNumHeaderLines(int numHeaderLines) throws Exception 
    {
        printer.getParams().setNumHeaderLines(numHeaderLines);
    }

    public void setNumTrailerLines(int numTrailerLines) throws Exception {
        printer.getParams().setNumTrailerLines(numTrailerLines);
    }

    public LongPrinterStatus readLongStatus() throws Exception {
        return getPrinter().readLongStatus();
    }

    private void updateDeviceMetrics(LongPrinterStatus longStatus) throws Exception {

        LogWriter.write(longStatus);
        LogWriter.write(getDeviceMetrics());
        LogWriter.writeSeparator();

        physicalDeviceName = getDeviceMetrics().getDeviceName() + ", № "
                + longStatus.getSerial();

        // iceDescription = "%s,  %s, ПО ФР: %s.%d, %s, ПО ФП: %s.%d, %s"
        physicalDeviceDescription = getDeviceMetrics().getDeviceName() + ", "
                + longStatus.getSerial() + ", ПО ФР: "
                + longStatus.getFirmwareVersion() + "."
                + String.valueOf(longStatus.getFirmwareBuild()) + ", "
                + longStatus.getFirmwareDate().toString() + ", ПО ФП: "
                + longStatus.getFMFirmwareVersion() + "."
                + String.valueOf(longStatus.getFMFirmwareBuild()) + ", "
                + longStatus.getFMFirmwareDate().toString();

        logger.debug("PhysicalDeviceName: " + physicalDeviceName);
        logger.debug("PhysicalDeviceDescription: " + physicalDeviceDescription);

        // update device parameters
        statistics.serialNumber = longStatus.getSerial();
        statistics.firmwareRevision = longStatus.getFirmwareRevision();

        checkLicense(longStatus.getSerial());
    }

    private void checkLicense(String serial) throws Exception {

        if ((getModel().getModelID() == PrinterConst.PRINTER_MODEL_SHTRIH_MINI_FRK)
                || (getModel().getModelID() == PrinterConst.PRINTER_MODEL_SHTRIH_MINI_FRK2)) {
            if (isLicenseValid) {
                return;
            }

            logger.debug("Reading license file...");
            try {
                String fileName = SysUtils.getFilesPath() + "shtrihjavapos.lic";
                FileReader fileReader = new FileReader(fileName);
                try {
                    BufferedReader input = new BufferedReader(fileReader);
                    try {
                        String license = input.readLine();
                        if (license != null) {
                            logger.debug("Checking license data...");
                            MessageDigest md = MessageDigest.getInstance("MD5");
                            md.update(serial.getBytes());
                            String digest = bytesToHex(md.digest());
                            isLicenseValid = license.equals(digest);
                            if (!isLicenseValid) {
                                throw new Exception(
                                        "Invalid license file for this device");
                            }
                        }
                    } finally {
                        input.close();
                    }
                } finally {
                    fileReader.close();
                }
            } catch (Exception e) {
                logger.error(e);
            }
        }
    }

    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    public void setPollEnabled(boolean value) throws Exception {
        if (value != params.pollEnabled) {
            if (value) {
                if (deviceEnabled) {
                    startPoll();
                }
            } else {
                stopPoll();
            }
        }
        params.pollEnabled = value;
    }

    public String getDeviceServiceDescription() throws Exception {
        checkOpened();
        return encodeText(deviceServiceDescription);
    }

    public int getDeviceServiceVersion() throws Exception {
        checkOpened();
        return deviceServiceVersion;
    }

    public boolean getFreezeEvents() throws Exception {
        checkOpened();
        return freezeEvents;
    }

    public void setFreezeEvents(boolean freezeEvents) throws Exception {
        checkOpened();
        if (freezeEvents != getFreezeEvents()) {
            this.freezeEvents = freezeEvents;
            if (freezeEvents) {
                stopEventThread();
            } else {
                startEventThread();
            }
        }
    }

    private void stopEventThread() throws Exception {
        if (eventThread != null) {
            synchronized (events) {
                events.notifyAll();
            }
            eventStopFlag = true;
            eventThread.interrupt();
            eventThread.join();
            eventThread = null;
        }
    }

    private void startEventThread() throws Exception {
        if (eventThread == null) {
            eventStopFlag = false;
            eventThread = new Thread(new EventTarget(this));
            eventThread.start();
        }
    }

    public String getPhysicalDeviceDescription() throws Exception {
        checkOpened();

        return encodeText(physicalDeviceDescription);
    }

    public String getPhysicalDeviceName() throws Exception {
        checkOpened();

        return encodeText(physicalDeviceName);
    }

    public int getState() throws Exception {
        return state;
    }

    // --------------------------------------------------------------------------
    // Properties
    // --------------------------------------------------------------------------
    // Capabilities
    public boolean getCapAdditionalLines() throws Exception {
        return capAdditionalLines;
    }

    public boolean getCapAmountAdjustment() throws Exception {
        return capAmountAdjustment;
    }

    public boolean getCapAmountNotPaid() throws Exception {
        return capAmountNotPaid;
    }

    public boolean getCapCheckTotal() throws Exception {
        return capCheckTotal;
    }

    public boolean getCapCoverSensor() throws Exception {
        return getModel().getCapCoverSensor();
    }

    public boolean getCapDoubleWidth() throws Exception {
        return getModel().getCapDoubleWidth();
    }

    public boolean getCapDuplicateReceipt() throws Exception {
        return getModel().getCapDuplicateReceipt();
    }

    public void setDuplicateReceipt(boolean aduplicateReceipt) throws Exception {
        if (!getCapDuplicateReceipt()) {
            throw new JposException(
                    JPOS_E_ILLEGAL,
                    Localizer
                    .getString(Localizer.receiptDuplicationNotSupported));
        }
        duplicateReceipt = aduplicateReceipt;
    }

    public boolean getCapFixedOutput() throws Exception {
        return capFixedOutput;
    }

    public boolean getCapHasVatTable() throws Exception {
        return getModel().getCapHasVatTable();
    }

    public boolean getCapIndependentHeader() throws Exception {
        return capIndependentHeader;
    }

    public boolean getCapItemList() throws Exception {
        return capItemList;
    }

    public boolean getCapJrnEmptySensor() throws Exception {
        return getModel().getCapJrnEmptySensor();
    }

    public boolean getCapJrnNearEndSensor() throws Exception {
        return getModel().getCapJrnNearEndSensor();
    }

    public boolean getCapJrnPresent() throws Exception {
        return params.capJrnPresent && getModel().getCapJrnPresent();
    }

    public boolean getCapNonFiscalMode() throws Exception {
        return capNonFiscalMode;
    }

    public boolean getCapOrderAdjustmentFirst() throws Exception {
        return capOrderAdjustmentFirst;
    }

    public boolean getCapPercentAdjustment() throws Exception {
        return capPercentAdjustment;
    }

    public boolean getCapPositiveAdjustment() throws Exception {
        return capPositiveAdjustment;
    }

    public boolean getCapPowerLossReport() throws Exception {
        return capPowerLossReport;
    }

    public int getCapPowerReporting() throws Exception {
        return JPOS_PR_STANDARD;
    }

    public boolean getCapPredefinedPaymentLines() throws Exception {
        return capPredefinedPaymentLines;
    }

    public boolean getCapReceiptNotPaid() throws Exception {
        return capReceiptNotPaid;
    }

    public boolean getCapRecEmptySensor() throws Exception {
        return getModel().getCapRecEmptySensor();
    }

    public boolean getCapRecNearEndSensor() throws Exception {
        return getModel().getCapRecNearEndSensor();
    }

    public boolean getCapRecPresent() throws Exception {
        return getModel().getCapRecPresent();
    }

    public boolean getCapRemainingFiscalMemory() throws Exception {
        return capRemainingFiscalMemory;
    }

    public boolean getCapReservedWord() throws Exception {
        return capReservedWord;
    }

    public boolean getCapSetHeader() throws Exception {
        return capSetHeader;
    }

    public boolean getCapSetPOSID() throws Exception {
        return capSetPOSID;
    }

    public boolean getCapSetStoreFiscalID() throws Exception {
        return capSetStoreFiscalID;
    }

    public boolean getCapSetTrailer() throws Exception {
        return capSetTrailer;
    }

    public boolean getCapSetVatTable() throws Exception {
        return capSetVatTable;
    }

    public boolean getCapSlpEmptySensor() throws Exception {
        return getModel().getCapSlpEmptySensor();
    }

    public boolean getCapSlpFiscalDocument() throws Exception {
        return capSlpFiscalDocument;
    }

    public boolean getCapSlpFullSlip() throws Exception {
        return capSlpFullSlip;
    }

    public boolean getCapSlpNearEndSensor() throws Exception {
        return getModel().getCapSlpNearEndSensor();
    }

    public boolean getCapSlpPresent() throws Exception {
        return getModel().getCapSlpPresent();
    }

    public boolean getCapSlpValidation() throws Exception {
        return capSlpValidation;
    }

    public boolean getCapSubAmountAdjustment() throws Exception {
        return capSubAmountAdjustment;
    }

    public boolean getCapSubPercentAdjustment() throws Exception {
        return capSubPercentAdjustment;
    }

    public boolean getCapSubtotal() throws Exception {
        return capSubtotal;
    }

    public boolean getCapTrainingMode() throws Exception {
        return capTrainingMode;
    }

    public boolean getCapValidateJournal() throws Exception {
        return capValidateJournal;
    }

    public boolean getCapXReport() throws Exception {
        return capXReport;
    }

    public int getOutputID() throws Exception {
        checkOpened();
        return outputID;
    }

    public int getPowerNotify() throws Exception {
        checkOpened();
        return powerNotify;
    }

    public void setPowerNotify(int powerNotify) throws Exception {
        if (deviceEnabled) {
            throw new JposException(JPOS_E_ILLEGAL,
                    Localizer.getString(Localizer.deviceIsEnabled));
        }
        this.powerNotify = powerNotify;
    }

    public int getPowerState() throws Exception {
        checkOpened();
        return powerState;
    }

    public int getAmountDecimalPlace() throws Exception {
        return getModel().getAmountDecimalPlace();
    }

    public boolean getAsyncMode() throws Exception {
        return asyncMode;
    }

    public void setAsyncMode(boolean asyncMode) throws Exception {
        if (asyncMode != this.asyncMode) {
            if (asyncMode) {
                asyncThread = new Thread(new AsyncTarget(this));
                asyncThread.start();
            } else {
                asyncThread.interrupt();
                asyncThread.join();
                asyncThread = null;
            }
            this.asyncMode = asyncMode;
        }
    }

    public boolean getCheckTotal() throws Exception {
        return params.checkTotal;
    }

    public void setCheckTotal(boolean value) throws Exception {
        params.checkTotal = value;
    }

    public int getCountryCode() throws Exception {
        checkEnabled();
        return countryCode;
    }

    public boolean getCoverOpen() throws Exception {
        checkEnabled();
        if (!getCapCoverSensor()) {
            return false;
        } else {
            return coverOpened;
        }
    }

    public boolean getDayOpened() throws Exception {
        checkEnabled();

        PrinterMode mode = getPrinter().readPrinterStatus().getPrinterMode();

        if (!params.openReceiptOnBegin) {
            // Эта логика должна выполняться, только если beginFiscalReceipt не открывает чек,
            // во всех остальных случаях нужно обращаться к устройству
            if (receipt != null && receipt.isOpened()) {
                return true;
            }
        }

        return mode.isDayOpened() || mode.isReceiptOpened();
    }

    public int getDescriptionLength() throws Exception {
        return getPrinter().getMessageLength();
    }

    public boolean getDuplicateReceipt() throws Exception {
        if (!getCapDuplicateReceipt()) {
            return false;
        }
        return duplicateReceipt;
    }

    public int getErrorLevel() throws Exception {
        return errorLevel;
    }

    public int getErrorOutID() throws Exception {
        checkEnabled();
        return errorOutID;
    }

    public int getErrorState() throws Exception {
        return errorState;
    }

    public int getErrorStation() throws Exception {
        return errorStation;
    }

    public String getErrorString() throws Exception {
        return encodeText(errorString);
    }

    public boolean getFlagWhenIdle() throws Exception {
        return flagWhenIdle;
    }

    public void setFlagWhenIdle(boolean value) throws Exception {
        if (value != flagWhenIdle) {
            flagWhenIdle = value;

            // If the State is already set to S_IDLE
            // when this property is set to true, then a
            // StatusUpdateEvent is enqueued immediately.
            if ((state == JPOS_S_IDLE) && (flagWhenIdle)) {
                statusUpdateEvent(FPTR_SUE_IDLE);

                // This property is automatically reset to false when
                // the status event is delivered.
                // We reset this property when status event is enqueued.
                // I think that it is a feature, not a code bug.
                flagWhenIdle = false;
            }
        }
    }

    public boolean getJrnEmpty() throws Exception {
        checkEnabled();
        return jrnEmpty;
    }

    public boolean getJrnNearEnd() throws Exception {
        checkEnabled();
        return jrnNearEnd;
    }

    public int getMessageLength() throws Exception {
        return getPrinter().getMessageLength();
    }

    public int getNumHeaderLines() throws Exception {
        checkEnabled();
        return getParams().getNumHeaderLines();
    }

    public int getNumTrailerLines() throws Exception {
        checkEnabled();
        return getParams().getNumTrailerLines();
    }

    public int getNumVatRates() throws Exception {
        PrinterTable table = getPrinter().getTable(PrinterConst.SMFP_TABLE_TAX);
        return table.getRowCount();
    }

    public String getPredefinedPaymentLines() throws Exception {
        return encodeText(predefinedPaymentLines);
    }

    public int getPrinterState() throws Exception {
        checkEnabled();
        return printerState.getValue();
    }

    public int getQuantityDecimalPlaces() throws Exception {
        checkEnabled();
        return quantityDecimalPlaces;
    }

    public int getQuantityLength() throws Exception {
        checkEnabled();
        return quantityLength;
    }

    public boolean getRecEmpty() throws Exception {
        checkEnabled();
        if (getCapRecEmptySensor()) {
            return recEmpty;
        } else {
            return false;
        }
    }

    public boolean getRecNearEnd() throws Exception {
        checkEnabled();
        if (getCapRecNearEndSensor()) {
            return recNearEnd;
        }
        return false;
    }

    public int getRemainingFiscalMemory() throws Exception {
        checkEnabled();
        return readLongStatus().getFMFreeRecords();
    }

    public String getReservedWord() throws Exception {
        return encodeText(reservedWord);
    }

    public boolean getSlpEmpty() throws Exception {
        checkEnabled();
        return slpEmpty;
    }

    public boolean getSlpNearEnd() throws Exception {
        checkEnabled();
        return slpNearEnd;
    }

    public int getSlipSelection() throws Exception {
        checkEnabled();
        return slipSelection;
    }

    public void setSlipSelection(int value) throws Exception {
        checkEnabled();
        if (value == FPTR_SS_FULL_LENGTH) {
            slipSelection = value;
        } else {
            throw new JposException(JPOS_E_ILLEGAL,
                    Localizer.getString(Localizer.invalidParameterValue)
                    + "SlipSelection");
        }
    }

    public boolean getTrainingModeActive() throws Exception {
        checkEnabled();
        return trainingModeActive;
    }

    public boolean getCapAdditionalHeader() throws Exception {
        return capAdditionalHeader;
    }

    public boolean getCapAdditionalTrailer() throws Exception {
        return capAdditionalTrailer;
    }

    public boolean getCapChangeDue() throws Exception {
        return capChangeDue;
    }

    public boolean getCapEmptyReceiptIsVoidable() throws Exception {
        return capEmptyReceiptIsVoidable;
    }

    public boolean getCapFiscalReceiptStation() throws Exception {
        switch (fiscalReceiptStation) {
            case FPTR_RS_RECEIPT:
                return capFiscalReceiptStation;

            case FPTR_RS_SLIP:
                return capFiscalSlipStation;

            default:
                throw new JposException(JPOS_E_ILLEGAL,
                        Localizer.getString(Localizer.invalidPropertyValue)
                        + "fiscalReceiptStation");
        }
    }

    public boolean getCapFiscalReceiptType() throws Exception {
        return capFiscalReceiptType;
    }

    public boolean getCapMultiContractor() throws Exception {
        return capMultiContractor;
    }

    public boolean getCapOnlyVoidLastItem() throws Exception {
        return capOnlyVoidLastItem;
    }

    public boolean getCapPackageAdjustment() throws Exception {
        return capPackageAdjustment;
    }

    public boolean getCapPostPreLine() throws Exception {
        return capPostPreLine;
    }

    public boolean getCapSetCurrency() throws Exception {
        return capSetCurrency;
    }

    public boolean getCapTotalizerType() throws Exception {
        return capTotalizerType;
    }

    // Properties
    public int getActualCurrency() throws Exception {
        checkEnabled();
        return actualCurrency;
    }

    public String getAdditionalHeader() throws Exception {
        checkEnabled();
        if (!getCapAdditionalHeader()) {
            throw new JposException(JPOS_E_ILLEGAL,
                    Localizer.getString(Localizer.additionalHeaderNotSupported));
        }
        return encodeText(additionalHeader);
    }

    public void setAdditionalHeader(String value) throws Exception {
        checkEnabled();
        if (!getCapAdditionalHeader()) {
            throw new JposException(JPOS_E_ILLEGAL,
                    Localizer.getString(Localizer.additionalHeaderNotSupported));
        }
        additionalHeader = decodeText(value);
    }

    public String getAdditionalTrailer() throws Exception {
        checkEnabled();
        if (!getCapAdditionalTrailer()) {
            throw new JposException(JPOS_E_ILLEGAL,
                    Localizer
                    .getString(Localizer.additionalTrailerNotSupported));
        }
        return encodeText(additionalTrailer);
    }

    public void setAdditionalTrailer(String value) throws Exception {
        checkEnabled();
        if (!getCapAdditionalTrailer()) {
            throw new JposException(JPOS_E_ILLEGAL,
                    Localizer
                    .getString(Localizer.additionalTrailerNotSupported));
        }
        additionalTrailer = decodeText(value);
    }

    public String getChangeDue() throws Exception {
        if (!getCapChangeDue()) {
            throw new JposException(JPOS_E_ILLEGAL,
                    Localizer.getString(Localizer.changeDueTextNotSupported));
        }
        return encodeText(changeDue);
    }

    public void setChangeDue(String value) throws Exception {
        if (!getCapChangeDue()) {
            throw new JposException(JPOS_E_ILLEGAL,
                    Localizer.getString(Localizer.changeDueTextNotSupported));
        }
        changeDue = decodeText(value);
    }

    public int getContractorId() throws Exception {
        checkEnabled();
        return contractorId;
    }

    public void setContractorId(int value) throws Exception {
        checkEnabled();
        if (!getCapMultiContractor()) {
            throw new JposException(
                    JPOS_E_ILLEGAL,
                    Localizer
                    .getString(Localizer.multipleContractorsNotSupported));
        }
        contractorId = value;
    }

    public int getDateType() throws Exception {
        checkEnabled();
        return dateType;
    }

    public void setDateType(int value) throws Exception {
        checkEnabled();
        switch (value) {
            case FPTR_DT_EOD:
            case FPTR_DT_RTC:

                dateType = value;
                break;
            default:
                throw new JposException(JPOS_E_ILLEGAL,
                        Localizer.getString(Localizer.invalidParameterValue)
                        + "DateType");
        }
    }

    public int getFiscalReceiptStation() throws Exception {
        checkEnabled();
        return fiscalReceiptStation;
    }

    public void setFiscalReceiptStation(int value) throws Exception {
        checkEnabled();
        // Check if the Fiscal Printer is currently in the Monitor State
        checkPrinterState(FPTR_PS_MONITOR);

        switch (value) {
            case FPTR_RS_RECEIPT:

                fiscalReceiptStation = value;
                break;

            case FPTR_RS_SLIP:
                if (!capFiscalSlipStation) {
                    throw new JposException(JPOS_E_ILLEGAL,
                            Localizer.getString(Localizer.slipStationNotPresent));
                }
                fiscalReceiptStation = value;
                break;
            default:
                throw new JposException(JPOS_E_ILLEGAL,
                        Localizer.getString(Localizer.invalidParameterValue));
        }
    }

    public int getFiscalReceiptType() throws Exception {
        checkEnabled();
        return fiscalReceiptType;
    }

    public int getMessageType() throws Exception {
        return messageType;
    }

    public void setMessageType(int value) throws Exception {
        if (value == FPTR_MT_FREE_TEXT) {
            messageType = value;
        } else {
            throw new JposException(JPOS_E_ILLEGAL,
                    Localizer.getString(Localizer.messageTypeNotSupported));
        }
    }

    public String getPostLine() throws Exception {
        checkEnabled();
        return encodeText(params.postLine);
    }

    public void setPostLine(String value) throws Exception {
        checkEnabled();
        params.postLine = params.postLinePrefix + decodeText(value);
    }

    public String getPreLine() throws Exception {
        checkEnabled();
        return encodeText(params.preLine);
    }

    public void setPreLine(String value) throws Exception {
        checkEnabled();
        params.preLine = params.preLinePrefix + decodeText(value);
    }

    public int getTotalizerType() throws Exception {
        checkEnabled();
        return totalizerType;
    }

    public void setTotalizerType(int value) throws Exception {
        checkEnabled();
        switch (value) {
            case FPTR_TT_DOCUMENT: // Document totalizer
            case FPTR_TT_DAY: // Day totalizer
            case FPTR_TT_RECEIPT: // Receipt totalizer
            case FPTR_TT_GRAND: // Grand totalizer
                totalizerType = value;
                break;

            default:
                throw new JposException(JPOS_E_ILLEGAL,
                        Localizer.getString(Localizer.invalidParameterValue));
        }
    }

    private void checkOpened() throws Exception {
        if (state == JPOS_S_CLOSED) {
            throw new JposException(JPOS_E_CLOSED, "Service is closed");
        }
    }

    // write fields if file specified
    private void writeFieldsFile() {
        logger.debug("writeFieldsFile");
        try {
            if (params.fieldsFilesPath.length() != 0) {
                writeFieldsFileFromPath();

            } else if (params.fieldsFileName.length() != 0) {
                writeFieldsFileFromFileName();
            }
        } catch (Exception e) {
            logger.error(e);
        }
    }

    private void writeFieldsFileFromPath() throws Exception {
        logger.debug("writeFieldsFileFromPath");
        File dir = new File(params.fieldsFilesPath);
        if (!dir.exists()) {
            logger.debug("Directory does not exists");
            return;
        }
        File[] files = dir.listFiles();
        if (files == null) {
            logger.debug("dir.listFiles() returns null");
            return;
        }
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            logger.debug("Found file '" + file.getAbsolutePath() + "')");
            if (FileUtils.getExtention(file.getName()).equals(".csv")) {
                if (file.exists()) {
                    PrinterFields fields = new PrinterFields();
                    CsvTablesReader reader = new CsvTablesReader();
                    reader.load(file.getAbsolutePath(), fields);

                    if (fields.validModelName(getPrinter().getDeviceMetrics().getDeviceName())) {
                        logger.debug("Write fields values from file '" + file.getAbsolutePath() + "')");
                        // set font for driver text
                        PrinterField field = fields.find(8, 1, 1);
                        if (field != null) {
                            int fontNumber = Integer.parseInt(field.getValue());
                            logger.debug("Font number changed to " + fontNumber);
                            params.font = new FontNumber(fontNumber);
                        }
                        writeTables(fields);
                        break;
                    }
                }

            }
        }
    }

    private void writeFieldsFileFromFileName() throws Exception {
        File file = new File(SysUtils.getFilesPath() + params.fieldsFileName);
        if (!file.exists()) {
            logger.debug("File not exists: " + params.fieldsFileName);
            return;
        }
        PrinterFields fields = new PrinterFields();
        CsvTablesReader reader = new CsvTablesReader();
        reader.load(file.getAbsolutePath(), fields);

        String deviceModelName = getPrinter().getDeviceMetrics().getDeviceName();
        if (fields.validModelName(deviceModelName)) {
            logger.error("File model name does not match device name");
            logger.error("'" + fields.getModelName() + "' <> '" + deviceModelName + "'");
            return;

        }
        logger.debug("Write fields values from file '" + file.getAbsolutePath() + "')");
        writeTables(fields);
    }

    private void writePaymentNames() throws Exception {
        // payment names
        Vector paymentNames = params.getPaymentNames();
        for (int i = 0; i < paymentNames.size(); i++) {
            FptrPaymentName paymentName = (FptrPaymentName) paymentNames.get(i);
            if (paymentName.getCode() != PrinterConst.SMFP_TABLE_TAX_ROW_CASH) {
                int result = getPrinter().writeTable(
                        PrinterConst.SMFP_TABLE_PAYTYPE, paymentName.getCode(),
                        1, paymentName.getName());
                if (printer.failed(result)) {
                    break;
                }
            }
        }
    }

    private void writeTables() throws Exception {
        writeFieldsFile();
        writePaymentNames();
    }

    public void readTables() {
        try {
            // load tax names
            String[] fieldValue = new String[1];
            for (int i = 0; i < 4; i++) {
                fieldValue[0] = "";
                if (printer.readTable(6, i + 1, 2, fieldValue) == 0) {
                    params.taxNames[i] = fieldValue[0];
                }
            }
        } catch (Exception e) {
            logger.error(e);
        }
    }

    public void claim(int timeout) throws Exception {
        checkOpened();
        if (!claimed) {
            claimed = true;
        }
    }

    public void close() throws Exception {
        checkOpened();
        setEventCallbacks(null);
        setFreezeEvents(true);

        if (claimed) {
            release();
        }
        setState(JPOS_S_CLOSED);
        if (params.statisticEnabled) {
            statistics.save(params.statisticFileName);
        }
        monitoringServer.stop();
    }

    public void checkHealth(int level) throws Exception {
        checkEnabled();
        switch (level) {
            case JPOS_CH_INTERNAL:
                checkHealthText = InternalCheckHelthReport.getReport(printer);
                break;

            case JPOS_CH_EXTERNAL:
                checkHealthText = checkHealthExternal();
                break;

            default:
                throw new JposException(JPOS_E_ILLEGAL,
                        Localizer.getString(Localizer.invalidParameterValue)
                        + ", level");
        }
    }

    public PrinterImages getPrinterImages() throws Exception {
        return params.getPrinterImages();
    }

    public void printImage(int index) throws Exception {
        printImage(getPrinterImages().get(index));
    }

    public void printImage(PrinterImage image) throws Exception {
        if (image == null) {
            return;
        }
        printer.loadImage(image, true);
        if (getModel().getModelID() == PrinterConst.PRINTER_MODEL_SHTRIH_M_FRK) {
            printer.printGraphics(image.getStartPos() + 1, image.getEndPos() + 1);
        } else {
            printer.printGraphics(image.getStartPos(), image.getEndPos());
        }
    }

    /**
     * Write tables from CSV file and don't check *
     */
    public void writeTables(String fileName) throws Exception {
        PrinterFields fields = new PrinterFields();
        CsvTablesReader reader = new CsvTablesReader();
        reader.load(fileName, fields);
        writeTables(fields);
    }

    public void writeTables(PrinterFields fields) throws Exception {
        for (int i = 0; i < fields.size(); i++) {
            PrinterField field = fields.get(i);
            try {
                writeField(field);
            } catch (Exception e) {
                logger.error("WriteField", e);
            }
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Do not write table SMFP_TABLE_TEXT 
    // Header and trailer are set by application with 
    // setHeaderLine and setTrailerLine
    public void writeField(PrinterField field) throws Exception {
        if (field.getTable() == PrinterConst.SMFP_TABLE_TEXT) {
            return;
        }
        getPrinter().writeField(field);
    }

    /**
     * Read tables from CSV file *
     */
    public void readTables(String fileName) throws Exception {
        PrinterTables tables = getPrinter().readTables();
        CsvTablesWriter writer = new CsvTablesWriter();
        writer.save(SysUtils.getFilesPath() + fileName, tables);
    }

    public void directIO(int command, int[] data, Object object)
            throws Exception {
        if (params.compatibilityLevel == SmFptrConst.SMFPTR_COMPAT_LEVEL_NONE) {
            (new DirectIOHandler(this)).directIO(command, data, object);
        } else {
            (new DirectIOHandler2(this)).directIO(command, data, object);
        }
    }

    public void setEventCallbacks(EventCallbacks cb) {
        this.cb = cb;
    }

    public void open(String logicalName, EventCallbacks cb) throws Exception {
        initializeData();
        logicalName = decodeText(logicalName);
        this.logicalName = logicalName;
        setEventCallbacks(cb);
        if (jposEntry != null) {
            params.loadLogEnabled(jposEntry);
            logger.setEnabled(params.logEnabled);

            logger.debug("-----------------------------------------------");
            logger.debug("SHTRIH-M JavaPos FiscalPrinter service");
            logger.debug("DeviceServiceVersion: "
                    + String.valueOf(deviceServiceVersion));
            logger.debug("git version: "
                    + String.valueOf(ServiceVersion.VERSION));
            logger.debug("Java version: " + System.getProperty("java.version"));
            logger.debug("File encoding: " + System.getProperty("file.encoding"));
            logger.debug("OS: " + System.getProperty("os.name"));
            logger.debug("OS ARCH: " + System.getProperty("os.arch"));
            logger.debug("OS Version: " + System.getProperty("os.version"));
            logger.debug("Library path: " + System.getProperty("java.library.path"));
            logger.debug("-----------------------------------------------");

            params.load(jposEntry);
            port = PrinterPortFactory.createInstance(params);
            port.setPortEvents(new PortEventsNotifier());
            device = ProtocolFactory.getProtocol(params, port);
            printer = new SMFiscalPrinterImpl(port, device, params);
            textGenerator = new TextGenerator(printer);
            header = new NullHeader(printer);

            if (params.escCommandsEnabled) {
                printer.setEscPrinter(new NCR7167Printer(this));
            }

            getPrinter().addEvents(this);
            if (params.receiptReportEnabled) {
                getPrinter().addEvents(new ReceiptReportFilter(printer, params));
            }
            getPrinter().setTaxPassword(params.taxPassword);
            getPrinter().setUsrPassword(params.usrPassword);
            getPrinter().setSysPassword(params.sysPassword);

            if (params.statisticEnabled) {
                statistics.load(params.statisticFileName);
            }
            Localizer.init(params.messagesFileName);
            getPrinter().setWrapText(params.wrapText);
            createFilters();

            if (params.monitoringEnabled) {
                monitoringServer.start(params.getMonitoringPort());
            }

            JposExceptionHandler.setStripExceptionDetails(params.stripExceptionDetails);
        }

        receipt = new NullReceipt(printer);

        state = JPOS_S_IDLE;
        setFreezeEvents(false);
    }

    private class PortEventsNotifier implements PrinterPort.IPortEvents {

        public PortEventsNotifier() {
        }

        public void onConnect() {
            setPowerState(JPOS_PS_ONLINE);
        }

        public void onDisconnect() {
            setPowerState(JPOS_PS_OFFLINE);
            if (device != null) {
                device.disconnect();
            }
        }

    }

    private PrinterHeader createHeader() throws Exception {
        switch (params.headerMode) {
            case SmFptrConst.SMFPTR_HEADER_MODE_PRINTER:
                return new DeviceHeader(printer);

            case SmFptrConst.SMFPTR_HEADER_MODE_DRIVER:
                return new DriverHeader(printer);

            case SmFptrConst.SMFPTR_HEADER_MODE_DRIVER2:
                return new DriverHeader2(printer);

            case SmFptrConst.SMFPTR_HEADER_MODE_NULL:
                return new NullHeader(printer);

            default:
                return new DriverHeader(printer);
        }
    }

    private void createFilters() throws Exception {

        filters.clear();

        if (params.zeroPriceFilterEnabled) {
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
            Date time1 = timeFormat.parse(params.zeroPriceFilterTime1);
            Date time2 = timeFormat.parse(params.zeroPriceFilterTime2);

            FiscalPrinterFilter113 filter = new ZeroPriceFilter(
                    params.zeroPriceFilterEnabled, time1, time2,
                    params.zeroPriceFilterErrorText);

            filters.add(filter);
        }
    }

    public void release() throws Exception {
        setDeviceEnabled(false);
        claimed = false;
    }

    // ////////////////////////////////////////////////////////////////////////////
    // Fiscal Document
    // ////////////////////////////////////////////////////////////////////////////
    private void noSlipStationError() throws Exception {
        throw new JposException(JPOS_E_ILLEGAL,
                Localizer.getString(Localizer.slipStationNotPresent));
    }

    public void beginFiscalDocument(int documentAmount) throws Exception {
        checkEnabled();
        noSlipStationError();
    }

    public void printFiscalDocumentLine(String documentLine) throws Exception {
        checkEnabled();
        noSlipStationError();
    }

    public void endFiscalDocument() throws Exception {
        checkEnabled();
        noSlipStationError();
    }

    // ////////////////////////////////////////////////////////////////////////////
    // Fixed Output
    // ////////////////////////////////////////////////////////////////////////////
    public void beginFixedOutput(int station, int documentType)
            throws Exception {
        checkEnabled();
        throw new JposException(JPOS_E_ILLEGAL);
    }

    public void printFixedOutput(int documentType, int lineNumber, String data)
            throws Exception {
        checkEnabled();
        throw new JposException(JPOS_E_ILLEGAL);
    }

    public void endFixedOutput() throws Exception {
        checkEnabled();
        throw new JposException(JPOS_E_ILLEGAL);
    }

    // ////////////////////////////////////////////////////////////////////////////
    // Slip
    // ////////////////////////////////////////////////////////////////////////////
    public void beginInsertion(int timeout) throws Exception {
        checkEnabled();
        throw new JposException(JPOS_E_ILLEGAL);
    }

    public void beginItemList(int vatID) throws Exception {
        checkEnabled();
        throw new JposException(JPOS_E_ILLEGAL);
    }

    public void beginRemoval(int timeout) throws Exception {
        checkEnabled();
        throw new JposException(JPOS_E_ILLEGAL);
    }

    public void endInsertion() throws Exception {
        checkEnabled();
        throw new JposException(JPOS_E_ILLEGAL);
    }

    public void endItemList() throws Exception {
        checkEnabled();
        throw new JposException(JPOS_E_ILLEGAL);
    }

    public void endRemoval() throws Exception {
        checkEnabled();
        throw new JposException(JPOS_E_ILLEGAL);
    }

    // ////////////////////////////////////////////////////////////////////////////
    // NonFiscal
    // ////////////////////////////////////////////////////////////////////////////
    public void beginNonFiscal() throws Exception {
        checkEnabled();
        checkPrinterState(FPTR_PS_MONITOR);
        receipt = new NonfiscalReceipt(printer);
        printDocStart();
        printHeaderDriver();

        setPrinterState(FPTR_PS_NONFISCAL);
    }

    public void printHeaderDriver() throws Exception {
        if (params.nonFiscalHeaderEnabled) {
            printer.printReceiptHeader("Нефискальный документ");
        }
    }

    public void printNormalAsync(int station, String data) throws Exception {
        checkEnabled();
        data = decodeText(data);
        logoPosition = SmFptrConst.SMFPTR_LOGO_PRINT;
        receipt.printNormal(station, data);
    }

    public void printNormal(int station, String data) throws Exception {
        checkEnabled();
        execute(new PrintNormalRequest(station, data));
    }

    private PrinterImage getPrinterImage(int position) throws Exception {
        ReceiptImage image = getReceiptImages().imageByPosition(position);
        if (image != null) {
            int index = image.getImageIndex();
            if (getPrinterImages().validIndex(index)) {
                return getPrinterImages().get(index);
            }
        }
        return null;
    }

    public void printRecLine(String line) throws Exception {
        getPrinter().printLine(SMFP_STATION_REC, line, params.font);
    }

    public void printEndFiscal() {
        try {
            if (getPrinter().getPrintMode() == PrinterConst.PRINT_MODE_DISABLE_ONCE) {
                getPrinter().enablePrint();
                return;
            }

            if (!docEndEnabled) {
                docEndEnabled = true;
                return;
            }

            synchronized (printer) {
                docEndEnabled = true;
                isInReceiptTrailer = true;
                getPrinter().waitForPrinting();
                header.endFiscal(additionalTrailer);
                isInReceiptTrailer = false;
            }
        } catch (Exception e) {
            logger.error(e.toString());
        }
    }

    public void printEndNonFiscal() {
        try {
            if (getPrinter().getPrintMode() == PrinterConst.PRINT_MODE_DISABLE_ONCE) {
                getPrinter().enablePrint();
                return;
            }

            if (!docEndEnabled && params.canDisableNonFiscalEnding) {
                docEndEnabled = true;
                return;
            }

            if (textGenerator != null) {
                try {
                    receipt.accept(textGenerator);
                } catch (Exception e) {
                    logger.error("Failed duplicate receipt", e);
                }
                setDocumentLines(textGenerator.getLines());
            }

            synchronized (printer) {

                docEndEnabled = true;
                isInReceiptTrailer = true;
                getPrinter().waitForPrinting();
                header.endNonFiscal(additionalTrailer);
                isInReceiptTrailer = false;
            }
        } catch (Exception e) {
            logger.error(e.toString());
        }
    }

    public void printDocStart() throws Exception {
        synchronized (printer) {
            isInReceiptTrailer = true;
            header.beginDocument(additionalHeader);
            isInReceiptTrailer = false;
        }
    }

    public void printNonFiscalDoc(String text) throws Exception {
        getPrinter().stopSaveCommands();

        boolean isReceiptOpened = false;
        PrinterStatus status = getPrinter().waitForPrinting();
        isReceiptOpened = status.getPrinterMode().isReceiptOpened();
        if (isReceiptOpened) {
            getPrinter().sysAdminCancelReceipt();
        }
        getPrinter().waitForPrinting();
        printDocStart();
        getPrinter().printText(text);
        printEndNonFiscal();

        // open receipt again
        if (isReceiptOpened) {
            getPrinter().check(getPrinter().printReceiptCommands());
            getPrinter().clearReceiptCommands();
        }
    }

    public void endNonFiscal() throws Exception {
        synchronized (printer) {
            checkEnabled();
            checkPrinterState(FPTR_PS_NONFISCAL);
            setPrinterState(FPTR_PS_MONITOR);
            printEndNonFiscal();
        }
    }

    // ////////////////////////////////////////////////////////////////////////////
    // Training
    // ////////////////////////////////////////////////////////////////////////////
    public void beginTraining() throws Exception {
        checkEnabled();
        checkPrinterState(FPTR_PS_MONITOR);
        trainingModeActive = true;
    }

    public void endTraining() throws Exception {
        checkEnabled();
        if (trainingModeActive) {
            trainingModeActive = false;
        } else {
            throwWrongStateError();
        }
    }

    // ////////////////////////////////////////////////////////////////////////////
    public void clearError() throws Exception {
        checkEnabled();
    }

    public void clearOutput() throws Exception {
        checkClaimed();
        // Clears all buffered output data, including all asynchronous output
        // Also, when possible, halts outputs that are in progress
        synchronized (requests) {
            requests.clear();
        }
        // Any output error events that are enqueued –
        // usually waiting for FreezeEvents to
        // be set to false – are also cleared.
        synchronized (events) {
            for (int i = events.size() - 1; i >= 0; i--) {
                if (events.get(i) instanceof ErrorEventRequest) {
                    events.remove(i);
                }
            }
        }
        setState(JPOS_S_IDLE);
    }

    private String getDataFirmware(int[] optArgs) throws Exception {
        LongPrinterStatus status = readLongStatus();
        String result = "";
        switch (optArgs[0]) {
            // printer firmware version number
            case 0:
                result = status.getFirmwareVersion();
                break;

            // printer firmware build number
            case 1:
                result = Long.toString(status.getFirmwareBuild());
                break;

            // fiscal memory firmware version number
            case 2:
                result = status.getFMFirmwareVersion();
                break;

            // fiscal memory firmware build number
            case 3:
                result = Long.toString(status.getFMFirmwareBuild());
                break;

            default:
                result += "Printer firmware : " + status.getFirmwareVersion();
                result += ", build " + String.valueOf(status.getFirmwareBuild());
                result += " from " + status.getFirmwareDate().toString();
                result += " Fiscal memory firmware: "
                        + status.getFMFirmwareVersion();
                result += ", build " + String.valueOf(status.getFMFirmwareBuild());
                result += " from " + status.getFMFirmwareDate().toString();
        }
        return result;
    }

    public String getTenderData(int optArg) throws Exception {
        switch (optArg) {
            // Cash
            case FPTR_PDL_CASH:
            // Cheque.
            case FPTR_PDL_CHEQUE:
            // Chitty.
            case FPTR_PDL_CHITTY:
            // Coupon.
            case FPTR_PDL_COUPON:
            // Currency.
            case FPTR_PDL_CURRENCY:
            case FPTR_PDL_DRIVEN_OFF:
            // Printer EFT.
            case FPTR_PDL_EFT_IMPRINTER:
            // Terminal EFT.
            case FPTR_PDL_EFT_TERMINAL:
            case FPTR_PDL_TERMINAL_IMPRINTER:
            // Gift.
            case FPTR_PDL_FREE_GIFT:
            // Giro.
            case FPTR_PDL_GIRO:
            // Home.
            case FPTR_PDL_HOME:
            case FPTR_PDL_IMPRINTER_WITH_ISSUER:
            // Local account.
            case FPTR_PDL_LOCAL_ACCOUNT:
            // Local card account.
            case FPTR_PDL_LOCAL_ACCOUNT_CARD:
            // Pay card.
            case FPTR_PDL_PAY_CARD:
            // Manual pay card.
            case FPTR_PDL_PAY_CARD_MANUAL:
            // Prepay.
            case FPTR_PDL_PREPAY:
            // Pump test.
            case FPTR_PDL_PUMP_TEST:
            // Credit.
            case FPTR_PDL_SHORT_CREDIT:
            // Staff.
            case FPTR_PDL_STAFF:
            // Voucher.
            case FPTR_PDL_VOUCHER:
                return "0";

            default:
                throw new JposException(JPOS_E_ILLEGAL,
                        Localizer.getString(Localizer.invalidParameterValue)
                        + ", OptArgs");
        }
    }

    public int getLineCountData(int optArg) throws Exception {
        switch (optArg) {
            // Number of item lines.
            case FPTR_LC_ITEM:
                return 2;
            // Number of voided item lines.
            case FPTR_LC_ITEM_VOID:
                return 2;
            // Number of printItemDiscount lines.
            case FPTR_LC_DISCOUNT:
                return 2;
            // Number of voided printItemDiscount lines.
            case FPTR_LC_DISCOUNT_VOID:
                return 2;
            // Number of surcharge lines.
            case FPTR_LC_SURCHARGE:
                return 2;
            // Number of voided surcharge lines.
            case FPTR_LC_SURCHARGE_VOID:
                return 2;
            // Number of refund lines.
            case FPTR_LC_REFUND:
                return 2;
            // Number of voided refund lines.
            case FPTR_LC_REFUND_VOID:
                return 2;
            // Number of subtotal printItemDiscount lines.
            case FPTR_LC_SUBTOTAL_DISCOUNT:
                return 2;
            // Number of voided subtotal printItemDiscount lines.
            case FPTR_LC_SUBTOTAL_DISCOUNT_VOID:
                return 2;
            // Number of subtotal surcharge lines.
            case FPTR_LC_SUBTOTAL_SURCHARGE:
                return 2;
            // Number of voided subtotal surcharge lines.
            case FPTR_LC_SUBTOTAL_SURCHARGE_VOID:
                return 2;
            // Number of comment lines.
            case FPTR_LC_COMMENT:
                return 0;
            // Number of subtotal lines.
            case FPTR_LC_SUBTOTAL:
                return 1;
            // Number of total lines.
            case FPTR_LC_TOTAL:
                return 2;

            default:
                throw new JposException(JPOS_E_ILLEGAL,
                        Localizer.getString(Localizer.invalidParameterValue)
                        + ", OptArgs");
        }
    }

    public int getDataDescriptionLength(int optArg) throws Exception {
        switch (optArg) {
            // printRecItem method
            case FPTR_DL_ITEM:
            // printRecItemAdjustment method.
            case FPTR_DL_ITEM_ADJUSTMENT:
            // printRecItemFuel method.
            case FPTR_DL_ITEM_FUEL:
            // printRecItemFuelVoid method.
            case FPTR_DL_ITEM_FUEL_VOID:
            // printRecNotPaid method.
            case FPTR_DL_NOT_PAID:
            // printRecPackageAdjustment method.
            case FPTR_DL_PACKAGE_ADJUSTMENT:
            // printRecRefund method.
            case FPTR_DL_REFUND:
            // printRecRefundVoid method.
            case FPTR_DL_REFUND_VOID:
            // printRecSubtotalAdjustment method.
            case FPTR_DL_SUBTOTAL_ADJUSTMENT:
            // printRecTotal method.
            case FPTR_DL_TOTAL:
            // printRecVoid method.
            case FPTR_DL_VOID:
            // printRecItemVoid and printRecItemAdjustmentVoid methods.
            case FPTR_DL_VOID_ITEM:
                return getPrinter().getMessageLength();

            default:
                throw new JposException(JPOS_E_ILLEGAL,
                        Localizer.getString(Localizer.invalidParameterValue)
                        + ", OptArgs");
        }
    }

    public String getDataItemText(int dataItem) {
        switch (dataItem) {
            case FPTR_GD_CURRENT_TOTAL:
                return "FPTR_GD_CURRENT_TOTAL";

            case FPTR_GD_DAILY_TOTAL:
                return "FPTR_GD_DAILY_TOTAL";
            case FPTR_GD_RECEIPT_NUMBER:
                return "FPTR_GD_RECEIPT_NUMBER";
            case FPTR_GD_REFUND:
                return "FPTR_GD_REFUND";
            case FPTR_GD_NOT_PAID:
                return "FPTR_GD_NOT_PAID";
            case FPTR_GD_MID_VOID:
                return "FPTR_GD_MID_VOID";
            case FPTR_GD_Z_REPORT:
                return "FPTR_GD_Z_REPORT";
            case FPTR_GD_GRAND_TOTAL:
                return "FPTR_GD_GRAND_TOTAL";
            case FPTR_GD_PRINTER_ID:
                return "FPTR_GD_PRINTER_ID";
            case FPTR_GD_FIRMWARE:
                return "FPTR_GD_FIRMWARE";
            case FPTR_GD_RESTART:
                return "FPTR_GD_RESTART";
            case FPTR_GD_REFUND_VOID:
                return "FPTR_GD_REFUND_VOID";
            case FPTR_GD_NUMB_CONFIG_BLOCK:
                return "FPTR_GD_NUMB_CONFIG_BLOCK";
            case FPTR_GD_NUMB_CURRENCY_BLOCK:
                return "FPTR_GD_NUMB_CURRENCY_BLOCK";
            case FPTR_GD_NUMB_HDR_BLOCK:
                return "FPTR_GD_NUMB_HDR_BLOCK";
            case FPTR_GD_NUMB_RESET_BLOCK:
                return "FPTR_GD_NUMB_RESET_BLOCK";
            case FPTR_GD_NUMB_VAT_BLOCK:
                return "FPTR_GD_NUMB_VAT_BLOCK";
            case FPTR_GD_FISCAL_DOC:
                return "FPTR_GD_FISCAL_DOC";
            case FPTR_GD_FISCAL_DOC_VOID:
                return "FPTR_GD_FISCAL_DOC_VOID";
            case FPTR_GD_FISCAL_REC:
                return "FPTR_GD_FISCAL_REC";
            case FPTR_GD_FISCAL_REC_VOID:
                return "FPTR_GD_FISCAL_REC_VOID";
            case FPTR_GD_NONFISCAL_DOC:
                return "FPTR_GD_NONFISCAL_DOC";
            case FPTR_GD_NONFISCAL_DOC_VOID:
                return "FPTR_GD_NONFISCAL_DOC_VOID";
            case FPTR_GD_NONFISCAL_REC:
                return "FPTR_GD_NONFISCAL_REC";
            case FPTR_GD_SIMP_INVOICE:
                return "FPTR_GD_SIMP_INVOICE";
            case FPTR_GD_TENDER:
                return "FPTR_GD_TENDER";
            case FPTR_GD_LINECOUNT:
                return "FPTR_GD_LINECOUNT";
            case FPTR_GD_DESCRIPTION_LENGTH:
                return "FPTR_GD_DESCRIPTION_LENGTH";
            default:
                return String.valueOf(dataItem);

        }
    }

    public void getData(int dataItem, int[] optArgs, String[] data)
            throws Exception {
        checkEnabled();
        String result = "";

        long number;
        switch (dataItem) {
            // Get the Fiscal Printer’s firmware release number.
            case FPTR_GD_FIRMWARE:
                result = getDataFirmware(optArgs);
                break;

            // Get the Fiscal Printer’s fiscal ID.
            case FPTR_GD_PRINTER_ID:

                if (params.printerIDMode == PrinterConst.PRINTER_ID_FS_SERIAL) {
                    if (printer.getCapFiscalStorage()) {
                        result = printer.fsReadSerial().getSerial();
                    } else {
                        result = readLongStatus().getSerial();
                    }
                } else if (printer.getCapFiscalStorage()) {
                    result = printer.readFullSerial();
                } else {
                    result = readLongStatus().getSerial();
                }
                break;

            case FPTR_GD_CURRENT_TOTAL:
                result = StringUtils.amountToString(receipt.getSubtotal());
                break;

            // Get the daily total.
            case FPTR_GD_DAILY_TOTAL:
                long amount = 0;
                if ((optArgs == null) || (optArgs.length < 1)) {
                    amount = getDailyTotal(SmFptrConst.SMFPTR_DAILY_TOTAL_ALL);
                } else {
                    amount = getDailyTotal(optArgs[0]);
                }
                result = StringUtils.amountToString(amount);
                break;

            // Get the Fiscal Printer’s grand total.
            case FPTR_GD_GRAND_TOTAL:
                result = readGrandTotal(optArgs);
                break;

            // Get the total number of voided receipts.
            case FPTR_GD_MID_VOID:
                result = Long.toString(printer.readOperationRegister(166));
                break;

            // Get the current total of not paid receipts.
            case FPTR_GD_NOT_PAID:
                result = "0";
                break;

            // Get the number of fiscal receipts printed
            case FPTR_GD_RECEIPT_NUMBER:
                result = Long.toString(readReceiptNumber());
                break;

            // Get the current total of refunds.
            case FPTR_GD_REFUND:
                result = Long.toString(printer.readOperationRegister(146));
                break;

            // Get the current total of voided refunds.
            case FPTR_GD_REFUND_VOID:
                throw new JposException(JPOS_E_ILLEGAL,
                        Localizer.getString(Localizer.invalidParameterValue)
                        + "dataItem");

            // Get the number of daily fiscal documents.
            case FPTR_GD_FISCAL_DOC:
                result = Long.toString(fiscalDay.getFiscalDocNumber());
                break;

            // Get the number of daily voided fiscal documents
            case FPTR_GD_FISCAL_DOC_VOID:
                result = Long.toString(fiscalDay.getFiscalDocVoidNumber());
                break;

            // Get the number of daily fiscal sales receipts
            case FPTR_GD_FISCAL_REC:
                number = getPrinter().readOperationRegister(144)
                        + getPrinter().readOperationRegister(145)
                        + getPrinter().readOperationRegister(146)
                        + getPrinter().readOperationRegister(147);

                result = Long.toString(number);
                break;

            case FPTR_GD_FISCAL_REC_VOID:
                number = getPrinter().readOperationRegister(179)
                        + getPrinter().readOperationRegister(180)
                        + getPrinter().readOperationRegister(181)
                        + getPrinter().readOperationRegister(182);

                result = Long.toString(number);
                break;

            case FPTR_GD_NONFISCAL_DOC:
                result = Long.toString(fiscalDay.getNonFiscalDocNumber());
                break;

            case FPTR_GD_NONFISCAL_DOC_VOID:
                result = Long.toString(fiscalDay.getNonFiscalDocVoidNumber());
                break;

            case FPTR_GD_NONFISCAL_REC:
                result = Long.toString(fiscalDay.getNonFiscalRecNumber());
                break;

            case FPTR_GD_SIMP_INVOICE:
                result = Long.toString(fiscalDay.getSimpInvoiceNumber());
                break;

            case FPTR_GD_Z_REPORT:
                result = Long.toString(printer.readDayNumber());
                break;

            case FPTR_GD_TENDER:
                result = getTenderData(optArgs[0]);
                break;

            case FPTR_GD_LINECOUNT:
                result = String.valueOf(getLineCountData(optArgs[0]));
                break;

            case FPTR_GD_DESCRIPTION_LENGTH:
                result = String.valueOf(getDataDescriptionLength(optArgs[0]));
                break;

            default:
                throw new JposException(JPOS_E_ILLEGAL,
                        Localizer.getString(Localizer.invalidParameterValue)
                        + "dataItem");
        }
        data[0] = encodeText(result);
        logger.debug("getData(" + getDataItemText(dataItem) + ")=" + result);
    }

    public long readReceiptNumber() throws Exception {
        long number = 0;
        switch (params.receiptNumberRequest) {
            case SmFptrConst.SMFPTR_RN_FP_DOCUMENT_NUMBER:
                number = printer.readDocNumber();
                break;

            case SmFptrConst.SMFPTR_RN_FS_DOCUMENT_NUMBER:
                if (printer.getCapFiscalStorage()) {
                    number = printer.fsReadStatus().getDocNumber();
                }
                break;

            case SmFptrConst.SMFPTR_RN_FS_RECEIPT_NUMBER:
                if (printer.getCapFiscalStorage()) {
                    number = printer.fsReadDayParameters().getReceiptNumber();
                }
                break;
        }
        return number;
    }

    public String readGrandTotal(int[] optArgs) throws Exception {
        String result = "0;0;0;0";
        boolean isFiscalized = printer.readLongStatus().isFiscalized();
        if (isFiscalized) {
            int mode = 0;
            if ((optArgs != null) && (optArgs.length > 0)) {
                mode = optArgs[0];
            }
            FMTotals totals = printer.readFPTotals(mode);
            result = String.valueOf(totals.getSalesAmount()) + ";"
                    + String.valueOf(totals.getBuyAmount()) + ";"
                    + String.valueOf(totals.getRetSaleAmount()) + ";"
                    + String.valueOf(totals.getRetBuyAmount());
        }
        return result;
    }

    public long getDailyTotal(int mode) throws Exception {
        long amount = 0;
        switch (mode) {
            case SmFptrConst.SMFPTR_DAILY_TOTAL_ALL:
                for (int i = 0; i < 4; i++) {
                    amount += printer.readCashRegister(193 + i * 4);
                    amount -= printer.readCashRegister(194 + i * 4);
                    amount -= printer.readCashRegister(195 + i * 4);
                    amount += printer.readCashRegister(196 + i * 4);
                }
                amount += printer.readCashRegister(242);
                amount -= printer.readCashRegister(243);
                break;

            case SmFptrConst.SMFPTR_DAILY_TOTAL_CASH:
                amount += printer.readCashRegister(193);
                amount -= printer.readCashRegister(194);
                amount -= printer.readCashRegister(195);
                amount += printer.readCashRegister(196);
                amount += printer.readCashRegister(242);
                amount -= printer.readCashRegister(243);
                break;

            case SmFptrConst.SMFPTR_DAILY_TOTAL_PT2:
                amount += printer.readCashRegister(197);
                amount -= printer.readCashRegister(198);
                amount -= printer.readCashRegister(199);
                amount += printer.readCashRegister(200);
                break;

            case SmFptrConst.SMFPTR_DAILY_TOTAL_PT3:
                amount += printer.readCashRegister(201);
                amount -= printer.readCashRegister(202);
                amount -= printer.readCashRegister(203);
                amount += printer.readCashRegister(204);
                break;

            case SmFptrConst.SMFPTR_DAILY_TOTAL_PT4:
                amount += printer.readCashRegister(205);
                amount -= printer.readCashRegister(206);
                amount -= printer.readCashRegister(207);
                amount += printer.readCashRegister(208);
                break;

            default:
                throw new Exception("Invalid optional parameter");
        }
        return amount;
    }

    public void getDate(String[] Date) throws Exception {
        checkEnabled();
        String result = "";
        if (Date.length < 1) {
            throw new JposException(JPOS_E_ILLEGAL,
                    Localizer.getString(Localizer.invalidParameterValue)
                    + "Date");
        }

        switch (dateType) {
            // Date of last end of day.
            case FPTR_DT_EOD:
                ReadFMLastRecordDate lastFmRecordDate = getPrinter()
                        .readFMLastRecordDate();
                if (lastFmRecordDate.getRecordType() == 1) {
                    PrinterDate date = lastFmRecordDate.getRecordDate();
                    JposFiscalPrinterDate jposDate = new JposFiscalPrinterDate(
                            date.getDay(), date.getMonth(), date.getYear(),
                            0, 0);
                    result = jposDate.toString();
                }
                break;

            // Real time clock of the Fiscal Printer.
            case FPTR_DT_RTC:
                LongPrinterStatus status = readLongStatus();
                PrinterDate printerDate = status.getDate();
                PrinterTime printerTime = status.getTime();

                JposFiscalPrinterDate jposDate = new JposFiscalPrinterDate(
                        printerDate.getDay(), printerDate.getMonth(),
                        printerDate.getYear(), printerTime.getHour(),
                        printerTime.getMin(), printerTime.getSec());
                result = jposDate.toString();
                break;

            default:
                throw new JposException(JPOS_E_ILLEGAL,
                        Localizer.getString(Localizer.invalidParameterValue)
                        + "DateType");
        }
        Date[0] = encodeText(result);
        logger.debug("getDate: " + Date[0]);
    }

    public void getTotalizer(int vatID, int optArgs, String[] data)
            throws Exception {
        checkEnabled();
        String result = "";
        /*
         * switch (totalizerType) { case FPTR_TT_DAY: case FPTR_TT_DOCUMENT:
         * case FPTR_TT_RECEIPT: case FPTR_TT_GRAND: switch (optArgs) { case
         * FPTR_GT_GROSS: data[0] = String.valueOf(printer.getTotal()); break;
         * case FPTR_GT_DISCOUNT: GetDiscountTotalizer(data); break; case
         * FPTR_GT_ITEM: GetItemTotalizer(data); break; case FPTR_GT_REFUND:
         * GetRefundTotalizer(data); break; case FPTR_GT_SURCHARGE:
         * GetSurchargeTotalizer(data); break; default: throw new
         * JposException(JPOS_E_ILLEGAL, "Invalid optArgs parameter value" ); }
         * }
         */
    }

    private boolean isSalesReceipt() {
        switch (fiscalReceiptType) {
            case FPTR_RT_SALES:
            case FPTR_RT_GENERIC:
            case FPTR_RT_SERVICE:
            case FPTR_RT_SIMPLE_INVOICE:
            case FPTR_RT_REFUND:
                return true;
            default:
                return false;
        }
    }

    private void dayEndRequiredError() throws Exception {
        throw new JposException(JPOS_E_EXTENDED, JPOS_EFPTR_DAY_END_REQUIRED,
                "Day end required");
    }

    public void openFiscalDay() throws Exception {
        if (printer.getCapOpenFiscalDay() && printer.isDayClosed()) {
            stopFDOService();
            printDocStart();
            getPrinter().openFiscalDay();
            printEndFiscal();
            startFDOService();
        }
    }

    // ////////////////////////////////////////////////////////////////////////////
    // Fiscal Receipt
    // ////////////////////////////////////////////////////////////////////////////
    public void beginFiscalReceipt(boolean printHeader) throws Exception {
        checkEnabled();
        checkPrinterState(FPTR_PS_MONITOR);
        stopFDOService();
        setPrinterFDOMode(PrinterConst.FDO_MODE_DRIVER);
        logger.debug("DeviceServiceVersion: " + String.valueOf(deviceServiceVersion));
        try {

            Vector<TextLine> messages = null;
            if ((receipt != null) && (receipt instanceof NullReceipt)) {
                messages = ((NullReceipt) receipt).getMessages();
            }
            receipt = createReceipt(fiscalReceiptType);

            PrinterStatus status = getPrinter().waitForPrinting();
            // Cancel receipt if it opened
            if (status.getPrinterMode().isReceiptOpened()) {
                cancelReceipt();
            }

            // check end of day
            if (status.getPrinterMode().isDayEndRequired()) {
                if (params.autoPrintZReport) {
                    printZReport();
                    openFiscalDay();
                } else {
                    dayEndRequiredError();
                }
            } else {
                if (status.getPrinterMode().isDayClosed() && getParams().autoOpenShift) {
                    openFiscalDay();
                }
            }
            setPrinterState(FPTR_PS_FISCAL_RECEIPT);
            getPrinter().startSaveCommands();
            printDocStart();
            receipt.beginFiscalReceipt(printHeader);
            if (messages != null) {
                for (int i = 0; i < messages.size(); i++) {
                    TextLine line = messages.get(i);
                    receipt.printRecMessage(line.getStation(), line.getFont(), line.getLine());
                }
            }

        } catch (Exception e) {
            receipt = new NullReceipt(printer);
            setPrinterState(FPTR_PS_MONITOR);
            throw e;
        }
    }

    public void cancelReceipt2() {
        try {
            getPrinter().waitForPrinting();
            getPrinter().cancelReceipt();
        } catch (Exception e) {
            logger.error(e);
        }
    }

    public void endFiscalReceipt(boolean printHeader) throws Exception {
        logger.debug("endFiscalReceipt");

        if (receipt.isCancelled()) {
            fiscalDay.cancelFiscalRec();
        } else {
            fiscalDay.closeFiscalRec();
        }

        synchronized (printer) {
            checkEnabled();
            checkPrinterState(FPTR_PS_FISCAL_RECEIPT_ENDING);
            if (receipt instanceof FSSalesReceipt) {
                if (textGenerator != null) {
                    try {
                        receipt.accept(textGenerator);
                    } catch (Exception e) {
                        logger.error("Failed duplicate receipt", e);
                    }
                    setDocumentLines(textGenerator.getLines());
                }
            }

            try {

                try {
                    receipt.endFiscalReceipt(printHeader);
                } catch (DeviceException e) {
                    if (e.getErrorCode() > 0) {
                        cancelReceipt2();
                    }
                    throw e;
                }

                try {
                    Time.delay(getParams().recCloseSleepTime);
                    if (!receipt.getCapAutoCut()) {
                        printEndFiscal();
                    }
                } catch (Exception e) {
                    // ignore print errors because cashin is succeeded
                    logger.error("endFiscalReceipt", e);
                }
            } finally {
                endFiscalreceipt2();
            }
        }
    }

    public void endFiscalreceipt2() throws Exception {
        getPrinter().stopSaveCommands();
        setPrinterFDOMode(PrinterConst.FDO_MODE_PRINTER);
        startFDOService();
        setPrinterState(FPTR_PS_MONITOR);
        filter.endFiscalReceipt();

        setDocumentLines(filter.getLines());
        if ((textGenerator != null) && (receipt instanceof FSSalesReceipt)) {
            textGenerator.addFiscalSign();
            setDocumentLines(textGenerator.getLines());
        } else {
            setDocumentLines(filter.getLines());
        }
        params.nonFiscalDocNumber++;
        saveProperties();
    }

    public void printDuplicateReceipt() throws Exception {
        boolean filterEnabled = filter.getEnabled();
        filter.setEnabled(false);
        try {
            checkPrinterState(FPTR_PS_MONITOR);
            if (!getCapDuplicateReceipt()) {
                throw new JposException(JPOS_E_ILLEGAL, Localizer.getString(Localizer.receiptDuplicationNotSupported));
            }
            if (printer.getParams().duplicateReceipt == SmFptrConst.DUPLICATE_RECEIPT_DEVICE) {
                printDocStart();
                getPrinter().duplicateReceipt();
                receipt.printReceiptEnding();
                printEndFiscal();
            } else {
                List<String> lines = documentLines;
                if (lines.size() == 0) {
                    throw new JposException(JPOS_E_ILLEGAL, "There is no documents to print");
                }

                printDocStart();
                for (int i = 0; i < lines.size(); i++) {
                    printer.printText(lines.get(i));
                }
                printEndFiscal();
            }
            duplicateReceipt = false;
        } finally {
            filter.setEnabled(filterEnabled);
        }
    }

    /**
     * ************************************************************************
     * Prints a report of totals for a range of dates on the receipt. This
     * method is always performed synchronously. The dates are strings in the
     * format “ddmmyyyyhhmm”, where: dd day of the month (1 - 31) mm month (1 -
     * 12) yyyy year (1997-) hh hour (0-23) mm minutes (0-59)
     * *************************************************************************
     */
    public void printPeriodicTotalsReport(String date1, String date2)
            throws Exception {
        checkEnabled();
        checkStateBusy();
        checkPrinterState(FPTR_PS_MONITOR);

        date1 = decodeText(date1);
        date2 = decodeText(date2);

        PrinterDate printerDate1 = JposFiscalPrinterDate.parseDateTime(date1)
                .getPrinterDate();
        PrinterDate printerDate2 = JposFiscalPrinterDate.parseDateTime(date2)
                .getPrinterDate();

        printDocStart();

        if (params.reportDevice == SmFptrConst.SMFPTR_REPORT_DEVICE_EJ) {
            getPrinter().printEJDayReportOnDates(printerDate1, printerDate2, params.reportType);
        } else {
            getPrinter().printFMReportDates(printerDate1, printerDate2,
                    params.reportType);
        }
        printEndFiscal();
    }

    public void printPowerLossReport() throws Exception {
        checkEnabled();
        throw new JposException(JPOS_E_ILLEGAL);
    }

    private void checkQuantity(int value) throws Exception {
        if (value < 0) {
            throw new JposException(JPOS_E_EXTENDED,
                    JPOS_EFPTR_BAD_ITEM_QUANTITY);
        }
    }

    private void checkPrice(long value) throws Exception {
        if (value < 0) {
            throw new JposException(JPOS_E_EXTENDED, JPOS_EFPTR_BAD_PRICE);
        }
    }

    private void checkLongParam(long Value, long minValue, long maxValue,
            String propName) throws Exception {
        if (Value < minValue) {
            throw new JposException(JPOS_E_ILLEGAL,
                    Localizer.getString(Localizer.invalidParameterValue)
                    + propName);
        }
        if (Value > maxValue) {
            throw new JposException(JPOS_E_ILLEGAL,
                    Localizer.getString(Localizer.invalidParameterValue)
                    + propName);
        }
    }

    private void checkVatInfo(long value) throws Exception {
        checkLongParam(value, 0, getNumVatRates(), "VatInfo");
    }

    private void checkReceiptStation() throws Exception {
        if (fiscalReceiptStation != FPTR_RS_RECEIPT) {
            throw new JposException(JPOS_E_ILLEGAL,
                    Localizer.getString(Localizer.invalidParameterValue)
                    + "fiscalReceiptStation");
        }
    }

    private long convertAmount(long value) {
        return Math.abs((long) (value * params.amountFactor));
    }

    private double convertQuantity(int value) {
        return value * params.quantityFactor / 1000000.0;
    }

    public void printRecItemAsync(String description, long price, int quantity,
            int vatInfo, long unitPrice, String unitName) throws Exception {
        unitName = decodeText(unitName);
        description = decodeText(description);
        price = convertAmount(price);
        unitPrice = convertAmount(unitPrice);

        checkEnabled();
        checkReceiptStation();
        checkQuantity(quantity);
        checkPrice(price);
        checkPrice(unitPrice);
        checkVatInfo(vatInfo);
        description = updateDescription(description);
        receipt.printRecItem(description, price, convertQuantity(quantity), vatInfo, unitPrice, unitName);
    }

    public String updateDescription(String description) throws Exception {
        if (params.postLineAsItemTextEnabled) {
            String postLine = getPostLine();
            if (!postLine.isEmpty()) {
                params.clearPostLine();
                printRecMessage(description);
                description = postLine;
            }
        }
        return description;
    }

    public void printRecItem(String description, long price, int quantity,
            int vatInfo, long unitPrice, String unitName) throws Exception {
        filters.printRecItem(description, price, quantity, vatInfo, unitPrice,
                unitName);

        checkEnabled();
        checkPrinterState(FPTR_PS_FISCAL_RECEIPT);
        execute(new PrintRecItemRequest(description, price, quantity, vatInfo,
                unitPrice, unitName));
    }

    public void printRecMessageAsync(int station, FontNumber font,
            String message) throws Exception {
        message = decodeText(message);
        receipt.printRecMessage(station, font, message);
    }

    public boolean isReceiptEnding() {
        return printerState.isEnding();
    }

    public void printRecMessageAsync(String message) throws Exception {
        printRecMessageAsync(PrinterConst.SMFP_STATION_RECJRN, getFont(),
                message);
    }

    public void printRecMessage(String message) throws Exception {
        checkEnabled();
        execute(new PrintRecMessageRequest(message));
    }

    private void checkPercents(long amount) throws Exception {
        if ((amount < 0) || (amount > 10000)) {
            throw new JposException(JPOS_E_EXTENDED, JPOS_EFPTR_BAD_ITEM_AMOUNT);
        }
    }

    public void checkAdjustment(int adjustmentType, long amount)
            throws Exception {
        switch (adjustmentType) {
            case FPTR_AT_AMOUNT_DISCOUNT:
            case FPTR_AT_AMOUNT_SURCHARGE:
                break;

            case FPTR_AT_PERCENTAGE_DISCOUNT:
            case FPTR_AT_PERCENTAGE_SURCHARGE:
                checkPercents(amount);
                break;

            default:
                throw new JposException(JPOS_E_ILLEGAL,
                        Localizer.getString(Localizer.invalidParameterValue)
                        + "adjustmentType");
        }
    }

    public void printRecItemAdjustmentAsync(int adjustmentType,
            String description, long amount, int vatInfo) throws Exception {
        description = decodeText(description);
        amount = convertAmount(amount);

        checkEnabled();
        checkVatInfo(vatInfo);
        checkAdjustment(adjustmentType, amount);

        // filter request
        PrintRecItemAdjustmentRequest request = new PrintRecItemAdjustmentRequest(
                adjustmentType, description, amount, vatInfo);
        request = filters.printRecItemAdjustment(request);
        adjustmentType = request.getAdjustmentType();
        description = request.getDescription();
        amount = request.getAmount();
        vatInfo = request.getVatInfo();

        checkPrinterState(FPTR_PS_FISCAL_RECEIPT);
        receipt.printRecItemAdjustment(adjustmentType, description, amount,
                vatInfo);
    }

    public void printRecItemAdjustment(int adjustmentType, String description,
            long amount, int vatInfo) throws Exception {
        checkEnabled();
        execute(new PrintRecItemAdjustmentRequest(adjustmentType, description,
                amount, vatInfo));
    }

    private String formatStrings(String line1, String line2) throws Exception {
        int len;
        String S = "";
        len = getPrinter().getMessageLength() - line2.length();

        for (int i = 0; i < len; i++) {
            if (i < line1.length()) {
                S = S + line1.charAt(i);
            } else {
                S = S + " ";
            }
        }
        return S + line2;
    }

    private void printStrings(String line1, String line2) throws Exception {
        checkEnabled();
        getPrinter().printText(SMFP_STATION_REC, formatStrings(line1, line2),
                getFont());
    }

    public void printRecItemFuelAsync(String description, long price,
            int quantity, int vatInfo, long unitPrice, String unitName,
            long specialTax, String specialTaxName) throws Exception {
    }

    public void printRecItemFuelVoidAsync(String description, long price,
            int vatInfo, long specialTax) throws Exception {
    }

    public void printRecNotPaidAsync(String description, long amount)
            throws Exception {
    }

    public void printRecNotPaid(String description, long amount)
            throws Exception {
        checkEnabled();
        throw new JposException(JPOS_E_ILLEGAL,
                Localizer.getString(Localizer.notPaidReceiptsNotSupported));
    }

    public void printRecRefund(String description, long amount, int vatInfo)
            throws Exception {
        checkEnabled();
        execute(new PrintRecRefundRequest(description, amount, vatInfo));
    }

    public void printRecRefundAsync(String description, long amount, int vatInfo)
            throws Exception {
        description = decodeText(description);
        amount = convertAmount(amount);

        checkEnabled();
        checkVatInfo(vatInfo);

        checkPrinterState(FPTR_PS_FISCAL_RECEIPT);
        receipt.printRecRefund(description, amount, vatInfo);
    }

    public void printRecSubtotal(long amount) throws Exception {
        checkEnabled();
        execute(new PrintRecSubtotalRequest(amount));
    }

    public void printRecSubtotalAsync(long amount) throws Exception {
        amount = convertAmount(amount);

        checkEnabled();
        checkPrinterState(FPTR_PS_FISCAL_RECEIPT);
        receipt.printRecSubtotal(amount);
    }

    public void printRecSubtotalAdjustment(int adjustmentType,
            String description, long amount) throws Exception {
        checkEnabled();
        execute(new PrintRecSubtotalAdjustmentRequest(adjustmentType,
                description, amount));
    }

    public void printRecSubtotalAdjustmentAsync(int adjustmentType,
            String description, long amount) throws Exception {
        description = decodeText(description);
        amount = convertAmount(amount);

        checkEnabled();
        checkAdjustment(adjustmentType, amount);
        checkPrinterState(FPTR_PS_FISCAL_RECEIPT);
        receipt.printRecSubtotalAdjustment(adjustmentType, description, amount);
    }

    public void printRecTotal(long total, long payment, String description)
            throws Exception {
        checkEnabled();
        execute(new PrintRecTotalRequest(total, payment, description));
    }

    public void printRecTotalAsync(long total, long payment, String description)
            throws Exception {
        logger.debug("printRecTotal");

        total = convertAmount(total);
        payment = convertAmount(payment);
        description = decodeText(description);

        checkEnabled();

        if ((printerState.getValue() != FPTR_PS_FISCAL_RECEIPT)
                && (printerState.getValue() != FPTR_PS_FISCAL_RECEIPT_TOTAL)) {
            throwWrongStateError();
        }
        long payType = getPayType(description);
        receipt.printRecTotal(total, payment, payType, description);
        if (receipt.isPayed()) {
            setPrinterState(FPTR_PS_FISCAL_RECEIPT_ENDING);
        } else {
            setPrinterState(FPTR_PS_FISCAL_RECEIPT_TOTAL);
        }
    }

    public void printRecVoidAsync(String description) throws Exception {
        description = decodeText(description);
        if ((printerState.getValue() == FPTR_PS_FISCAL_RECEIPT)
                || (printerState.getValue() == FPTR_PS_FISCAL_RECEIPT_TOTAL)
                || (printerState.isEnding())) {
            receipt.printRecVoid(description);
            setPrinterState(FPTR_PS_FISCAL_RECEIPT_ENDING);
        } else {
            throwWrongStateError();
        }
    }

    public void printRecVoid(String description) throws Exception {
        checkEnabled();
        execute(new PrintRecVoidRequest(description));
    }

    public void printRecVoidItem(String description, long amount, int quantity,
            int adjustmentType, long adjustment, int vatInfo) throws Exception {
        checkEnabled();
        description = decodeText(description);
        amount = convertAmount(amount);

        checkPrinterState(FPTR_PS_FISCAL_RECEIPT);
        checkQuantity(quantity);
        checkVatInfo(vatInfo);

        if (getParams().printRecVoidItemAmount) {
            quantity = 1000;
        }

        receipt.printRecVoidItem(description, amount, convertQuantity(quantity),
                adjustmentType, adjustment, vatInfo);
    }

    /*
     * Remarks: Prints a report of the fiscal EPROM contents on the receipt that
     * occurred between two end points. This method is always performed
     * synchronously
     */
    public void printReport(int reportType, String startNum, String endNum)
            throws Exception {
        checkEnabled();
        startNum = decodeText(startNum);
        endNum = decodeText(endNum);

        checkEnabled();
        checkPrinterState(FPTR_PS_MONITOR);

        int day1 = 0;
        int day2 = 0;
        switch (reportType) {
            case FPTR_RT_ORDINAL:
                // case FPTR_RT_EOD_ORDINAL:
                day1 = stringParamToInt(startNum, "startNum");
                day2 = stringParamToInt(endNum, "endNum");

                printDocStart();

                if (params.reportDevice == SmFptrConst.SMFPTR_REPORT_DEVICE_EJ) {
                    getPrinter().printEJReportDays(day1, day2, params.reportType);
                } else {
                    getPrinter().printFMReportDays(day1, day2, params.reportType);
                }
                printEndFiscal();
                break;

            case FPTR_RT_DATE:
                PrinterDate date1;
                PrinterDate date2;
                // pase dates
                date1 = JposFiscalPrinterDate.parseDateTime(startNum).getPrinterDate();
                date2 = JposFiscalPrinterDate.parseDateTime(endNum).getPrinterDate();

                // print report
                printDocStart();

                if (params.reportDevice == SmFptrConst.SMFPTR_REPORT_DEVICE_EJ) {
                    getPrinter().printEJDayReportOnDates(date1, date2, params.reportType);
                } else {
                    getPrinter().printFMReportDates(date1, date2, params.reportType);
                }
                printEndFiscal();
                break;

            default:
                throw new JposException(JPOS_E_ILLEGAL,
                        Localizer.getString(Localizer.invalidParameterValue)
                        + "reportType");
        }
    }

    public void printXReport() throws Exception {
        checkEnabled();
        checkStateBusy();
        checkPrinterState(FPTR_PS_MONITOR);
        printDocStart();
        getPrinter().printXReport();
        setDocumentLines(filter.getLines());
        filter.printXReport();
        printEndFiscal();
    }

    public void printZReport() throws Exception {
        checkEnabled();
        checkStateBusy();
        checkPrinterState(FPTR_PS_MONITOR);

        sendDocumentsZReport();
        stopFDOService();

        if (getParams().forceOpenShiftOnZReport) {
            openFiscalDay();
        }

        PrinterStatus status = readPrinterStatus();
        if (status.getPrinterMode().canPrintZReport()) {

            saveZReportXml();
            printDocStart();
            getPrinter().printZReport();
            filter.printZReport();
            setDocumentLines(filter.getLines());
            fiscalDay.close();
            try {
                printCalcReport();
                printEndFiscal();
            } catch (Exception e) {
                logger.error("printZReport", e);
            }
            sendDocumentsZReport();
            startFDOService();
        } else {
            throw new JposException(JPOS_E_ILLEGAL, "Day is closed");
        }
    }

    private void sendDocumentsZReport() {
        if (params.fdoMode == SmFptrConst.FDO_MODE_ZREPORT) {
            try {
                getPrinter().sendFDODocuments();
            } catch (Exception e) {
                logger.debug("sendFDODocuments failed", e);
            }
        }
    }

    private void printCalcReport() {
        if (!params.calcReportEnabled) {
            return;
        }

        if (!printer.getCapFiscalStorage()) {
            return;
        }

        try {
            printer.waitForPrinting();
            FSReadCommStatus status = printer.fsReadCommStatus();
            printer.printLines("КОЛИЧЕСТВО СООБЩЕНИЙ ДЛЯ ОФД:", String.valueOf(status.getQueueSize()));
            printer.printLines("НОМЕР ПЕРВОГО ДОКУМЕНТА ДЛЯ ОФД:", String.valueOf(status.getDocumentNumber()));
            String docDate = status.getDocumentDate().toString() + " " + status.getDocumentTime().toString2();
            printer.printLines("ДАТА ПЕРВОГО ДОКУМЕНТА:", docDate);
        } catch (Exception e) {
            logger.error(e);
        }
    }

    private void saveZReportXml() throws Exception {
        if (params.xmlZReportEnabled || params.csvZReportEnabled) {
            try {
                RegisterReport report = new RegisterReport();
                RegisterReportReader.execute(report, printer);
                if (params.xmlZReportEnabled) {
                    try {
                        String fileName = getXmlZReportFileName(report.getDayNumber());
                        XmlRegisterReportWriter.execute(report, fileName);
                    } catch (Exception e) {
                        logger.error("Error saving file", e);
                    }
                }
                if (params.csvZReportEnabled) {
                    try {
                        String fileName = getCsvZReportFileName(report.getDayNumber());
                        CsvRegisterReportWriter.execute(report, fileName);
                    } catch (Exception e) {
                        logger.error("Error saving file", e);
                    }
                }
            } catch (Exception e) {
                logger.error("Error saving file", e);
            }
        }
    }

    private String getXmlZReportFileName(int dayNumber) throws Exception {
        String fileName = params.xmlZReportFileName;
        if (params.ZReportDayNumber) {
            fileName = FileUtils.removeExtention(fileName) + "_"
                    + getDayNumberText(dayNumber)
                    + FileUtils.getExtention(fileName);
        }
        fileName = SysUtils.getFilesPath() + fileName;
        return fileName;
    }

    private String getCsvZReportFileName(int dayNumber) throws Exception {
        String fileName = params.csvZReportFileName;
        if (params.ZReportDayNumber) {
            fileName = FileUtils.removeExtention(fileName) + "_"
                    + getDayNumberText(dayNumber)
                    + FileUtils.getExtention(fileName);
        }
        fileName = SysUtils.getFilesPath() + fileName;
        return fileName;
    }

    private String getDayNumberText(int dayNumber) {
        String result = String.valueOf(dayNumber);
        for (int i = result.length(); i < 4; i++) {
            result = "0" + result;
        }
        return result;
    }

    public void resetPrinter() throws Exception {
        params.cancelIO = false;
        checkEnabled();
        cancelReceipt();
        printer.resetPrinter();
        receiptType = 0;
        isReceiptOpened = false;
        startFDOService();
    }

    public void setDate(String date) throws Exception {
        checkEnabled();
        date = decodeText(date);

        checkEnabled();
        LongPrinterStatus status = getPrinter().readLongStatus();
        if (!status.getPrinterMode().isDayClosed()) {
            dayEndRequiredError();
        }

        JposFiscalPrinterDate jposDate = JposFiscalPrinterDate.parseDateTime(date);
        PrinterDate printerDate = jposDate.getPrinterDate();
        PrinterTime printerTime = jposDate.getPrinterTime();

        if (!printerDate.isEqual(status.getDate())) {
            getPrinter().check(printer.writeDate(printerDate));
            int resultCode = getPrinter().confirmDate(printerDate);
            if (resultCode != 0) {
                // try to set date back
                printerDate = readLongStatus().getDate();
                getPrinter().check(printer.confirmDate(printerDate));

                throw new Exception(
                        Localizer.getString(Localizer.failedConfirmDate)
                        + printer.getErrorText(resultCode));
            }
        }
        getPrinter().writeTime(printerTime);
        // check if date and time was set correctly
        status = getPrinter().readLongStatus();
        if (!printerDate.isEqual(status.getDate())) {
            logger.error("Failed to set printer date: "
                    + printerDate.toText() + " <> "
                    + status.getDate().toText());
        }
        if (!printerTime.isEqual(status.getTime())) {
            logger.error("Failed to set printer time: "
                    + PrinterTime.toString(printerTime) + " <> "
                    + PrinterTime.toString(status.getTime()));
        }
    }

    public String getHeaderLine(int index) throws Exception {
        return getParams().getHeaderLine(index + 1).getText();
    }

    public String getTrailerLine(int index) throws Exception {
        return getParams().getTrailerLine(index + 1).getText();
    }

    public void setHeaderLine(int lineNumber, String text, boolean doubleWidth) throws Exception {
        checkEnabled();
        text = decodeText(text);
        logger.debug("setHeaderLine: " + text);

        checkEnabled();
        // reset graphic info
        if (lineNumber == 1) {
            graphicsLine = 1;
        }
        logoPosition = params.headerImagePosition;
        text = getPrinter().processEscCommands(text);
        if (params.centerHeader) {
            text = StringUtils.centerLine(text.trim(), getMessageLength());
        }
        text = StringUtils.trimRight(text);
        params.setHeaderLine(lineNumber, text, doubleWidth);
        header.setHeaderLine(lineNumber, text, doubleWidth);
        saveProperties();
        logoPosition = SmFptrConst.SMFPTR_LOGO_PRINT;
    }

    public void setPOSID(String POSID, String cashierID) throws Exception {
        checkEnabled();
        if ((POSID != null) && (!POSID.isEmpty())) {
            getPrinter().writeTable(1, 1, 1, POSID);
        }
        if ((cashierID != null) && (!cashierID.isEmpty())) {
            cashierID = decodeText(cashierID);
            getPrinter().writeAdminName(cashierID);
            getPrinter().writeCasierName(cashierID);
        }
    }

    public void setStoreFiscalID(String ID) throws Exception {
        checkEnabled();
        throw new JposException(JPOS_E_ILLEGAL);
    }

    public void setTrailerLine(int lineNumber, String text, boolean doubleWidth) throws Exception {
        checkEnabled();
        text = decodeText(text);
        checkEnabled();
        logoPosition = params.trailerImagePosition;
        text = getPrinter().processEscCommands(text);
        if (params.centerHeader) {
            text = StringUtils.centerLine(text.trim(), getMessageLength());
        }
        text = StringUtils.trimRight(text);
        params.setTrailerLine(lineNumber, text, doubleWidth);
        header.setTrailerLine(lineNumber, text, doubleWidth);
        saveProperties();
        logoPosition = SmFptrConst.SMFPTR_LOGO_PRINT;
    }

    /**
     * ************************************************************************
     * Gets the rate associated with a given VAT identifier. Parameter
     * Description vatID - VAT identifier of the required rate. optArgs - For
     * some countries, this additional argument may be needed. Consult the
     * Fiscal Printer Service vendor's documentation for details. vatRate - The
     * rate associated with the VAT identifier
     */
     
    public void getVatEntry(int vatID, int optArgs, int[] vatRate)
            throws Exception {
        checkEnabled();
        checkCapHasVatTable();
        checkParamValue(vatID, 1, getNumVatRates(), "vatID");

        vatRate[0] = 0;
        Integer vatValue = vatEntries.get(vatID);
        if (vatValue != null){
            vatRate[0] = vatValue;
        }
    }

    public void setVatTable() throws Exception {
        checkEnabled();
        checkCapHasVatTable();
        checkCapSetVatTable();

        Integer[] vatIDs = (Integer[])vatEntries.keySet().toArray();
        for (int i=0; i<vatIDs.length; i++)
        {
            int vatID = vatIDs[i];
            int vatRate = vatEntries.get(vatID);
            int res = printer.writeTable(SMFP_TABLE_TAX, vatID, 1, String.valueOf(vatRate));
            getPrinter().check(res);
        }
    }

    public void checkCapHasVatTable() throws Exception {
        if (!getCapHasVatTable()) {
            throw new JposException(JPOS_E_ILLEGAL, "CapHasVatTable = false, vat table is not supported");
        }
    }

    public void checkCapSetVatTable() throws Exception {
        checkCapHasVatTable();
        if (!capSetVatTable) {
            throw new JposException(JPOS_E_ILLEGAL, "CapSetVatTable = false, setting vat table is not supported");
        }
    }

    public void setVatValue(int vatID, String vatValue) throws Exception {
        checkEnabled();
        checkCapHasVatTable();
        checkCapSetVatTable();

        vatValue = decodeText(vatValue);
        checkParamValue(vatID, 1, getNumVatRates(), "vatID");
        int intVatValue = Integer.parseInt(vatValue);
        checkParamValue(intVatValue, 0, 10000, "vatValue");
        vatEntries.put(vatID, intVatValue);
    }

    public void verifyItem(String itemName, int vatID) throws Exception {
        checkEnabled();
        throw new JposException(JPOS_E_ILLEGAL);
    }

    public void setCurrency(int newCurrency) throws Exception {
        checkEnabled();
        throw new JposException(JPOS_E_ILLEGAL);
    }

    public void printRecCashAsync(long amount) throws Exception {
        amount = convertAmount(amount);
        checkEnabled();
        checkPrinterState(FPTR_PS_FISCAL_RECEIPT);
        receipt.printRecCash(amount);
    }

    public void printRecCash(long amount) throws Exception {
        checkEnabled();
        execute(new PrintRecCashRequest(amount));
    }

    public void printRecItemFuel(String description, long price, int quantity,
            int vatInfo, long unitPrice, String unitName, long specialTax,
            String specialTaxName) throws Exception {
        checkEnabled();
        throw new JposException(JPOS_E_ILLEGAL);
    }

    public void printRecItemFuelVoid(String description, long price,
            int vatInfo, long specialTax) throws Exception {
        checkEnabled();
        throw new JposException(JPOS_E_ILLEGAL);
    }

    public void printRecPackageAdjustment(int adjustmentType,
            String description, String vatAdjustment) throws Exception {
        checkEnabled();
        execute(new PrintRecPackageAdjustmentRequest(adjustmentType,
                description, vatAdjustment));
    }

    public void printRecPackageAdjustmentAsync(int adjustmentType,
            String description, String vatAdjustment) throws Exception {
        description = decodeText(description);
        vatAdjustment = decodeText(vatAdjustment);

        checkEnabled();
        checkPrinterState(FPTR_PS_FISCAL_RECEIPT);
        receipt.printRecPackageAdjustment(adjustmentType, description,
                vatAdjustment);
    }

    public void printRecPackageAdjustVoid(int adjustmentType,
            String vatAdjustment) throws Exception {
        execute(new PrintRecPackageAdjustVoidRequest(adjustmentType,
                vatAdjustment));
    }

    public void printRecPackageAdjustVoidAsync(int adjustmentType,
            String vatAdjustment) throws Exception {
        vatAdjustment = decodeText(vatAdjustment);
        checkEnabled();
        checkPrinterState(FPTR_PS_FISCAL_RECEIPT);
        receipt.printRecPackageAdjustVoid(adjustmentType, vatAdjustment);
    }

    public void printRecRefundVoid(String description, long amount, int vatInfo)
            throws Exception {
        execute(new PrintRecRefundVoidRequest(description, amount, vatInfo));
    }

    public void printRecRefundVoidAsync(String description, long amount,
            int vatInfo) throws Exception {
        description = decodeText(description);
        amount = convertAmount(amount);

        checkEnabled();
        checkVatInfo(vatInfo);
        checkPrinterState(FPTR_PS_FISCAL_RECEIPT);
        receipt.printRecRefundVoid(description, amount, vatInfo);
    }

    public void printRecSubtotalAdjustVoid(int adjustmentType, long amount)
            throws Exception {
        execute(new PrintRecSubtotalAdjustVoidRequest(adjustmentType, amount));
    }

    public void printRecSubtotalAdjustVoidAsync(int adjustmentType, long amount)
            throws Exception {
        amount = convertAmount(amount);
        checkEnabled();
        checkPrinterState(FPTR_PS_FISCAL_RECEIPT);
        receipt.printRecSubtotalAdjustVoid(adjustmentType, amount);
    }

    public void printRecTaxID(String taxID) throws Exception {
        execute(new PrintRecTaxIDRequest(taxID));
    }

    public void printRecTaxIDAsync(String taxID) throws Exception {
        checkEnabled();
        checkPrinterState(FPTR_PS_FISCAL_RECEIPT_ENDING);
        getPrinter().printText(SMFP_STATION_REC, decodeText(taxID), getFont());
    }

    public int getAmountDecimalPlaces() throws Exception {
        checkEnabled();
        return getModel().getAmountDecimalPlace();
    }

    public boolean getCapStatisticsReporting() throws Exception {
        checkOpened();
        return capStatisticsReporting;
    }

    public boolean getCapUpdateStatistics() throws Exception {
        checkOpened();
        return capUpdateStatistics;
    }

    public void resetStatistics(String statisticsBuffer) throws Exception {
        statisticsBuffer = decodeText(statisticsBuffer);
        checkEnabled();
        statistics.reset(statisticsBuffer);
    }

    public void retrieveStatistics(String[] statisticsBuffer) throws Exception {
        checkEnabled();
        statistics.retrieve(statisticsBuffer);
    }

    public void updateStatistics(String statisticsBuffer) throws Exception {
        checkEnabled();
        statistics.update(statisticsBuffer);
    }

    public boolean getCapCompareFirmwareVersion() throws Exception {
        checkOpened();
        return capCompareFirmwareVersion;
    }

    public boolean getCapUpdateFirmware() throws Exception {
        checkOpened();
        return capUpdateFirmware;
    }

    public void compareFirmwareVersion(String firmwareFileName, int[] result)
            throws Exception {
        if (!getCapCompareFirmwareVersion()) {
            throw new JposException(JPOS_E_ILLEGAL);
        }
        checkEnabled();
        int rc = printer.compareFirmwareVersion(firmwareFileName);
        if (rc == 0) {
            result[0] = JposConst.JPOS_CFV_FIRMWARE_SAME;
        }
        if (rc == 1) {
            result[0] = JposConst.JPOS_CFV_FIRMWARE_NEWER;
        }
        if (rc == -1) {
            result[0] = JposConst.JPOS_CFV_FIRMWARE_OLDER;
        }

    }

    public void updateFirmware(String firmwareFileName) throws Exception {
        checkEnabled();
        if (!capUpdateFirmware) {
            throw new JposException(JPOS_E_ILLEGAL);
        }
        printer.updateFirmware(firmwareFileName);
    }

    // 1.11
    // Capabilities
    public boolean getCapPositiveSubtotalAdjustment() throws Exception {
        return capPositiveSubtotalAdjustment;
    }

    // Methods
    public void printRecItemVoidAsync(String description, long price,
            int quantity, int vatInfo, long unitPrice, String unitName)
            throws Exception {
        price = convertAmount(price);
        unitPrice = convertAmount(unitPrice);
        description = decodeText(description);
        unitName = decodeText(unitName);

        checkEnabled();
        checkQuantity(quantity);
        checkVatInfo(vatInfo);

        description = updateDescription(description);
        receipt.printRecItemVoid(description, price, convertQuantity(quantity),
                vatInfo, unitPrice, unitName);
    }

    public void printRecItemVoid(String description, long price, int quantity,
            int vatInfo, long unitPrice, String unitName) throws Exception {
        checkPrinterState(FPTR_PS_FISCAL_RECEIPT);
        PrintRecItemVoidRequest request = new PrintRecItemVoidRequest(
                description, price, quantity, vatInfo, unitPrice, unitName);
        request = filters.printRecItemVoid(request);
        execute(request);
    }

    public void printRecItemAdjustmentVoidAsync(int adjustmentType,
            String description, long amount, int vatInfo) throws Exception {
        description = decodeText(description);
        amount = convertAmount(amount);

        checkEnabled();
        checkVatInfo(vatInfo);
        receipt.printRecItemAdjustmentVoid(adjustmentType, description, amount,
                vatInfo);
    }

    public void printRecItemAdjustmentVoid(int adjustmentType,
            String description, long amount, int vatInfo) throws Exception {
        checkPrinterState(FPTR_PS_FISCAL_RECEIPT);
        execute(new PrintRecItemAdjustmentVoidRequest(adjustmentType,
                description, amount, vatInfo));
    }

    // //////////////////////////
    private void checkPrinterState(int value) throws Exception {
        if (printerState.getValue() != value) {
            throwWrongStateError();
        }
    }

    private void setPrinterState(int newState) {
        printerState.setValue(newState);
    }

    private void checkParamValue(int value, int minValue, int maxValue,
            String paramText) throws Exception {
        if ((value < minValue) || (value > maxValue)) {
            throw new JposException(JPOS_E_ILLEGAL,
                    Localizer.getString(Localizer.invalidParameterValue)
                    + paramText);
        }
    }

    private int stringParamToInt(String s, String paramName) throws Exception {
        try {
            int result = Integer.parseInt(s);
            return result;
        } catch (Exception e) {
            throw new JposException(JPOS_E_ILLEGAL,
                    Localizer.getString(Localizer.invalidParameterValue)
                    + paramName);
        }
    }

    private String checkHealthExternal() throws Exception {
        PrinterStatus status = getPrinter().waitForPrinting();
        if (!status.getPrinterMode().isTestMode()) {
            getPrinter().startTest(1);
            getPrinter().waitForPrinting();
        }
        getPrinter().stopTest();
        getPrinter().waitForPrinting();
        return "External HCheck: OK";
    }

    private void checkStateBusy() throws Exception {
        if (state == JPOS_S_BUSY) {
            logger.error("JPOS_E_BUSY");
            throw new JposException(JPOS_E_BUSY);
        }
    }

    public void cancelReceipt() throws Exception {
        setPrinterState(FPTR_PS_MONITOR);
        PrinterStatus status = getPrinter().waitForPrinting();
        if (status.getPrinterMode().isReceiptOpened()) {
            getPrinter().sysAdminCancelReceipt();
            if (!getPrinter().getCapFiscalStorage()) {
                printEndFiscal();
            }
        }
    }

    public int getPayType(String key) throws Exception {
        // logger.debug("getPayType(" + String.valueOf(userPayType) + ")");

        PayType payType = params.getPayTypes().get(key);
        if (payType == null) {
            return 0;
        } else {
            return payType.getValue();
        }
    }

    public FontNumber getFont(boolean doubleWidth) {
        if (doubleWidth) {
            return doubleWidthFont;
        } else {
            return getFont();
        }
    }

    public String encodeText(String text) {
        String result;
        if (getStringEncoding().length() == 0) {
            result = text;
        } else {
            try {
                result = new String(StringUtils.getBytes(text,
                        getStringEncoding()));
            } catch (UnsupportedEncodingException e) {
                logger.error(e);
                result = text;
            }
        }
        return result;
    }

    public String decodeText(String text) {
        String result;
        if (getStringEncoding().length() == 0) {
            result = text;
        } else {
            try {
                result = new String(text.getBytes(), getStringEncoding());
            } catch (UnsupportedEncodingException e) {
                logger.error(e);
                result = text;
            }
        }
        return result;

    }

    class PackageAdjustment {

        public int vat;
        public long amount;
    }

    class PackageAdjustments extends Vector {

        public PackageAdjustment addItem(int vat, long amount) {
            PackageAdjustment result = new PackageAdjustment();
            result.vat = vat;
            result.amount = amount;
            add(result);
            return result;
        }

        public PackageAdjustment getItem(int Index) {
            return (PackageAdjustment) get(Index);
        }

        public void parse(String s) {
            String[] items = StringUtils.split(s, ';');
            for (int i = 0; i < items.length; i++) {
                String[] fields = StringUtils.split(items[i], ',');
                if (fields.length >= 2) {
                    addItem(Integer.parseInt(fields[0]),
                            Long.parseLong(fields[1]));
                }
            }
        }
    }

    private void checkClaimed() throws Exception {
        if (!claimed) {
            throw new JposException(JPOS_E_NOTCLAIMED, "Service is not claimed");
        }
    }

    public void checkEnabled() throws Exception {
        checkClaimed();
        if (!deviceEnabled) {
            throw new JposException(JPOS_E_DISABLED, "Service is not enabled");
        }
    }

    public void printFiscalDocumentLineAsync(String documentLine)
            throws Exception {
        checkEnabled();
        noSlipStationError();
    }

    public void printFixedOutputAsync(int documentType, int lineNumber,
            String data) throws Exception {
        checkEnabled();
        throw new JposException(JPOS_E_ILLEGAL);
    }

    public int getMaxGraphicsHeight() throws Exception {
        return getModel().getMaxGraphicsHeight();
    }

    public void loadGraphics(int lineNumber, int lineCount, byte[] data)
            throws Exception {
        getPrinter().loadGraphics(lineNumber, lineCount, data);
    }

    public void throwTestError() throws Exception {
        throw new DeviceException(0x71, "Cutter failure");
    }

    public void checkDeviceStatus() throws Exception {
        try {
            synchronized (printer) {
                PrinterStatus status = getPrinter().readPrinterStatus();
                checkPaperStatus(status);
            }
        } catch (DeviceException e) {
            if (e.isConnectionError()) {
                setPowerState(JPOS_PS_OFFLINE);
            }
            logger.error("checkDeviceStatus", e);
        }
    }

    public void deviceProc() {
        logger.debug("Poll thread started");
        try {
            while (!pollStopFlag) {
                checkDeviceStatus();
                Thread.sleep(params.pollInterval);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            logger.error("DeviceProc", e);
        }
        logger.debug("Poll thread stopped");
    }

    public DeviceMetrics getDeviceMetrics() throws Exception {
        return getPrinter().getDeviceMetrics();
    }

    public void printBarcode(PrinterBarcode barcode) throws Exception {
        receipt.printBarcode(barcode);
    }

    public void printRawGraphics(byte[][] data) throws Exception {
        PrinterGraphics graphics = new PrinterGraphics(data);
        receipt.printGraphics(graphics);
    }

    public void printRecItemRefund(String description, long amount,
            int quantity, int vatInfo, long unitAmount, String unitName)
            throws Exception {
        checkEnabled();
        checkPrinterState(FPTR_PS_FISCAL_RECEIPT);

        filters.printRecItemRefund(description, amount, quantity, vatInfo,
                unitAmount, unitName);

        execute(new PrintRecItemRefundRequest(description, amount, quantity,
                vatInfo, unitAmount, unitName));
    }

    public void printRecItemRefundAsync(String description, long amount,
            int quantity, int vatInfo, long unitAmount, String unitName)
            throws Exception {
        unitName = decodeText(unitName);
        description = decodeText(description);
        amount = convertAmount(amount);
        unitAmount = convertAmount(unitAmount);

        checkEnabled();
        checkReceiptStation();
        checkQuantity(quantity);
        checkPrice(amount);
        checkPrice(unitAmount);
        checkVatInfo(vatInfo);

        description = updateDescription(description);
        receipt.printRecItemRefund(description, amount, convertQuantity(quantity),
                vatInfo, unitAmount, unitName);
    }

    public void printRecItemRefundVoid(String description, long amount,
            int quantity, int vatInfo, long unitAmount, String unitName)
            throws Exception {
        checkEnabled();
        checkPrinterState(FPTR_PS_FISCAL_RECEIPT);

        filters.printRecItemRefundVoid(description, amount, quantity, vatInfo,
                unitAmount, unitName);

        execute(new PrintRecItemRefundVoidRequest(description, amount,
                quantity, vatInfo, unitAmount, unitName));
    }

    public void printRecItemRefundVoidAsync(String description, long amount,
            int quantity, int vatInfo, long unitAmount, String unitName)
            throws Exception {
        amount = convertAmount(amount);
        unitAmount = convertAmount(unitAmount);
        description = decodeText(description);
        unitName = decodeText(unitName);

        checkEnabled();
        checkQuantity(quantity);
        checkVatInfo(vatInfo);

        description = updateDescription(description);
        receipt.printRecItemRefundVoid(description, amount,
                convertQuantity(quantity), vatInfo, unitAmount, unitName);
    }

    public void saveXmlZReport(String fileName) throws JposException {
        try {
            RegisterReport report = new RegisterReport();
            RegisterReportReader.execute(report, printer);
            XmlRegisterReportWriter.execute(report, fileName);
        } catch (Exception e) {
            logger.error("Error saving file", e);
            throw new JposException(JPOS_E_FAILURE, e.getMessage());
        }
    }

    public void saveCsvZReport(String fileName) throws JposException {
        try {
            RegisterReport report = new RegisterReport();
            RegisterReportReader.execute(report, printer);
            CsvRegisterReportWriter.execute(report, fileName);
        } catch (Exception e) {
            logger.error("Error saving file", e);
            throw new JposException(JPOS_E_FAILURE, e.getMessage());
        }
    }

    private String getPropsFileName() throws Exception {
        return SysUtils.getFilesPath() + "shtrihjavapos.xml";
    }

    protected JposEntry createJposEntry(String logicalName,
            String factoryClass, String serviceClass, String vendorName,
            String vendorURL, String deviceCategory, String jposVersion,
            String productName, String productDescription, String productURL) {
        JposEntry jposEntry = new SimpleEntry();

        jposEntry.addProperty(JposEntry.LOGICAL_NAME_PROP_NAME, logicalName);
        jposEntry.addProperty(JposEntry.SI_FACTORY_CLASS_PROP_NAME,
                factoryClass);
        jposEntry.addProperty(JposEntry.SERVICE_CLASS_PROP_NAME, serviceClass);
        jposEntry.addProperty(JposEntry.VENDOR_NAME_PROP_NAME, vendorName);
        jposEntry.addProperty(JposEntry.VENDOR_URL_PROP_NAME, vendorURL);
        jposEntry.addProperty(JposEntry.DEVICE_CATEGORY_PROP_NAME,
                deviceCategory);
        jposEntry.addProperty(JposEntry.JPOS_VERSION_PROP_NAME, jposVersion);
        jposEntry.addProperty(JposEntry.PRODUCT_NAME_PROP_NAME, productName);
        jposEntry.addProperty(JposEntry.PRODUCT_DESCRIPTION_PROP_NAME,
                productDescription);
        jposEntry.addProperty(JposEntry.PRODUCT_URL_PROP_NAME, productURL);

        return jposEntry;
    }

    public void saveProperties() {
        try {
            String serial = "FiscalPrinter_" + getPrinter().getFullSerial();
            XmlPropWriter writer = new XmlPropWriter("FiscalPrinter",
                    serial);
            writer.write(params);
            writer.save(getPropsFileName());
        } catch (Exception e) {
            logger.error("saveProperties", e);
        }
    }

    private void loadProperties() throws Exception {

        logger.debug("loadProperties");
        try {
            String serial = "FiscalPrinter_" + getPrinter().readFullSerial();
            String fileName = getPropsFileName();
            File f = new File(fileName);
            if (f.exists()) {
                XmlPropReader reader = new XmlPropReader();
                reader.load("FiscalPrinter", serial, fileName);
                reader.read(params);
                logger.debug("loadProperties: OK");
            } else {
                logger.debug("loadProperties: no file");
            }
        } catch (Exception e) {
            logger.error("Failed to load properties", e);
        }
    }

    private void throwWrongStateError() throws Exception {
        throw new JposException(JPOS_E_EXTENDED, JPOS_EFPTR_WRONG_STATE,
                Localizer.getString(Localizer.wrongPrinterState) + "("
                + String.valueOf(printerState) + ", " + "\""
                + PrinterState.getText(printerState.getValue()) + "\"");
    }

    public void writeParameter(String paramName, int value) throws Exception {
        getPrinter().writeParameter(paramName, value);
    }

    public void writeParameter(String paramName, boolean value) throws Exception {
        getPrinter().writeParameter(paramName, value);
    }

    public void writeParameter(String paramName, String value) throws Exception {
        getPrinter().writeParameter(paramName, value);
    }

    public String readParameter(String paramName) throws Exception {
        return getPrinter().readParameter(paramName);
    }

    public void setFiscalReceiptType(int value) throws Exception {
        fiscalReceiptType = value;
    }

    public FiscalReceipt createReceipt(int fiscalReceiptType) throws Exception {
        checkEnabled();
        checkPrinterState(FPTR_PS_MONITOR);
        switch (fiscalReceiptType) {
            case FPTR_RT_CASH_IN:
                return new CashInReceipt(printer);

            case FPTR_RT_CASH_OUT:
                return new CashOutReceipt(printer);

            case FPTR_RT_GENERIC:
            case FPTR_RT_SALES:
            case FPTR_RT_SERVICE:
            case FPTR_RT_SIMPLE_INVOICE: // case FPTR_RT_REFUND:
                return createSalesReceipt(PrinterConst.SMFP_RECTYPE_SALE);

            case FPTR_RT_REFUND:
                return createSalesReceipt(PrinterConst.SMFP_RECTYPE_RETSALE);

            case SmFptrConst.SMFPTR_RT_SALE:
                return createSalesReceipt(PrinterConst.SMFP_RECTYPE_SALE);

            case SmFptrConst.SMFPTR_RT_BUY:
                return createSalesReceipt(PrinterConst.SMFP_RECTYPE_BUY);

            case SmFptrConst.SMFPTR_RT_RETSALE:
                return createSalesReceipt(PrinterConst.SMFP_RECTYPE_RETSALE);

            case SmFptrConst.SMFPTR_RT_RETBUY:
                return createSalesReceipt(PrinterConst.SMFP_RECTYPE_RETBUY);

            case SmFptrConst.SMFPTR_RT_CORRECTION:
                return createSalesReceipt(PrinterConst.SMFP_RECTYPE_CORRECTION_SALE);

            case SmFptrConst.SMFPTR_RT_CORRECTION_SALE:
                return createSalesReceipt(PrinterConst.SMFP_RECTYPE_CORRECTION_SALE);

            case SmFptrConst.SMFPTR_RT_CORRECTION_BUY:
                return createSalesReceipt(PrinterConst.SMFP_RECTYPE_CORRECTION_BUY);

            case SmFptrConst.SMFPTR_RT_CORRECTION_RETSALE:
                return createSalesReceipt(PrinterConst.SMFP_RECTYPE_CORRECTION_RETSALE);

            case SmFptrConst.SMFPTR_RT_CORRECTION_RETBUY:
                return createSalesReceipt(PrinterConst.SMFP_RECTYPE_CORRECTION_RETBUY);

            default:
                throw new JposException(JPOS_E_ILLEGAL,
                        Localizer.getString(Localizer.invalidParameterValue));
        }
    }

    private boolean isTablesRead = false;

    private FiscalReceipt createSalesReceipt(int receiptType) throws Exception {
        FiscalReceipt result;
        if (printer.getCapFiscalStorage()) {
            result = new FSSalesReceipt(printer, receiptType);
        } else if (params.salesReceiptType == SmFptrConst.SMFPTR_RECEIPT_NORMAL) {
            result = new SalesReceipt(printer, receiptType);
        } else {
            if (!isTablesRead) {
                readTables();
                isTablesRead = true;
            }
            result = new GlobusSalesReceipt(printer, PrinterConst.SMFP_RECTYPE_SALE);
        }
        return result;
    }

    public FptrParameters getParams() {
        return params;
    }

    public FontNumber getFont() {
        return params.getFont();
    }

    public String getStringEncoding() {
        return params.stringEncoding;
    }

    public boolean handleDeviceException(Exception e) throws Exception {
        if (connected
                && (params.searchMode == SmFptrConst.SMFPTR_SEARCH_ON_ERROR)) {
            getPrinter().searchDevice();
            connected = true;
            return true;
        } else {
            return false;
        }
    }

    public int getFontNumber() {
        return params.getFont().getValue();
    }

    public void setFontNumber(int value) throws Exception {
        params.setFont(new FontNumber(value));
    }

    public EJActivation getEJActivation() throws Exception {
        LongPrinterStatus longStatus = readLongStatus();
        if ((longStatus.getRegistrationNumber() > 0)
                && (longStatus.getPrinterFlags().isEJPresent())) {
            String[] lines = printer.readEJActivationText(6);
            return EJReportParser.parseEJActivation(lines);
        } else {
            return new EJActivation();
        }
    }

    public int loadLogo(String fileName) throws Exception {
        return loadLogo(fileName, logoPosition);
    }

    public int loadLogo(String fileName, int logoPosition) throws Exception {
        int imageIndex = -1;
        PrinterImage image = new PrinterImage(fileName);
        if (logoPosition < SmFptrConst.SMFPTR_LOGO_NONE) {
            printer.loadImage(image, true);
            imageIndex = getPrinterImages().getIndex(image);
            ReceiptImage receiptImage = new ReceiptImage(imageIndex, logoPosition);
            getReceiptImages().add(receiptImage);
            saveProperties();
        } else {
            printer.loadImage(image, false);
        }
        if (logoPosition == SmFptrConst.SMFPTR_LOGO_PRINT) {
            printer.printImage(image);
        }
        return imageIndex;
    }

    public ReceiptImages getReceiptImages() {
        return params.getReceiptImages();
    }

    public void fsWriteTLV(byte[] data, boolean print) throws Exception {
        logger.debug("fsWriteTLV: " + Hex.toHex(data));
        receipt.fsWriteTLV(data, print);
    }

    public void fsWriteOperationTLV(byte[] data, boolean print) throws Exception {
        receipt.fsWriteOperationTLV(data, print);
    }

    public void disablePrintOnce() throws Exception {
        getPrinter().disablePrintOnce();
    }

    public void disablePrint() throws Exception {
        getPrinter().disablePrint();
    }

    public void enablePrint() throws Exception {
        getPrinter().enablePrint();
    }

    public void fsPrintCalcReport() throws Exception {
        printDocStart();
        FSPrintCalcReport command = new FSPrintCalcReport(printer.getSysPassword());
        printer.execute(command);

        try {
            printer.waitForPrinting();
            printEndFiscal();
        } catch (Exception e) {
            logger.error("fsPrintCalcReport", e);
        }
    }

    public void setDiscountAmount(int amount) throws Exception {
        if ((printerState.getValue() != FPTR_PS_FISCAL_RECEIPT)
                && (printerState.getValue() != FPTR_PS_FISCAL_RECEIPT_TOTAL)) {
            throwWrongStateError();
        }

        receipt.setDiscountAmount(amount);

        if (receipt.isPayed()) {
            setPrinterState(FPTR_PS_FISCAL_RECEIPT_ENDING);
        } else {
            setPrinterState(FPTR_PS_FISCAL_RECEIPT_TOTAL);
        }
    }

    public String getReceiptName(int receiptType) {
        return "";
    }

    public void addItemCode(ItemCode itemCode) throws Exception {
        receipt.addItemCode(itemCode);
    }

    public void saveMCNotifications(String fileName) throws Exception {
        MCNotifications items = printer.readNotifications();
        MCNotificationsReport report = new MCNotificationsReport();
        report.save(items, fileName);
        printer.confirmNotifications(items);
    }

    public int mcClearBuffer() throws Exception {
        return printer.mcClearBuffer();
    }

    public PrinterPort getPort() {
        return port;
    }

    public void setDocumentLines(List<String> lines) {
        documentLines.clear();
        documentLines.addAll(lines);
        saveLastDocument();
    }

    public static String getLastDocFilePath() {
        return SysUtils.getFilesPath() + "document.txt";
    }

    private void saveLastDocument() {
        try {
            File file = new File(getLastDocFilePath());
            if (file.exists()) {
                file.delete();
            }
            PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
            try {
                for (int i = 0; i < documentLines.size(); i++) {
                    String line = documentLines.get(i);
                    writer.println(line);
                }
                writer.flush();
            } finally {
                writer.close();
            }
        } catch (Exception e) {
            logger.error("Failed to save last document, ", e);
        }
    }

    private void readLastDocument() {
        try {
            documentLines.clear();
            File file = new File(getLastDocFilePath());
            if (!file.exists()) {
                logger.debug("Last document not found");
                return;
            }

            BufferedReader reader = new BufferedReader(new FileReader(file));
            try {
                String line;
                while ((line = reader.readLine()) != null) {
                    documentLines.add(line);
                }
            } finally {
                reader.close();
            }
        } catch (Exception e) {
            logger.error("Failed to read last document, ", e);
        }
    }

    public PrinterHeader getHeader(){
        return header;
    }
    
}
