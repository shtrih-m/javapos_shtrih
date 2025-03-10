/*
 *  mPrinterDevice.java
 *
 * Created on July 31 2007, 16:46
 *f
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
/**
 * @author V.Kravtsov
 */
/*
 *  mPrinterDevice.java
 *
 * Created on July 31 2007, 16:46
 *f
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
/**
 * @author V.Kravtsov
 */
package com.shtrih.fiscalprinter;

import java.io.OutputStream;
import java.util.Map;
import java.util.HashMap;
import java.util.Scanner;
import java.io.ByteArrayInputStream;

import com.shtrih.fiscalprinter.port.ClosedConnectionException;
import com.shtrih.util.Time;
import com.shtrih.barcode.PrinterBarcode;
import com.shtrih.barcode.SmBarcode;
import com.shtrih.barcode.SmBarcodeEncoder;
import com.shtrih.barcode.ZXingEncoder;
import com.shtrih.fiscalprinter.CashCoreVersion;
import com.shtrih.fiscalprinter.command.*;
import com.shtrih.fiscalprinter.request.*;
import com.shtrih.fiscalprinter.model.PrinterModel;
import com.shtrih.fiscalprinter.model.PrinterModels;
import com.shtrih.fiscalprinter.model.XmlModelsWriter;
import com.shtrih.fiscalprinter.port.PrinterPort;
import com.shtrih.fiscalprinter.table.PrinterField;
import com.shtrih.fiscalprinter.table.PrinterFields;
import com.shtrih.fiscalprinter.table.PrinterTable;
import com.shtrih.fiscalprinter.table.PrinterTables;
import com.shtrih.jpos.fiscalprinter.FptrParameters;
import com.shtrih.jpos.fiscalprinter.PrintItem;
import com.shtrih.jpos.fiscalprinter.PrinterImage;
import com.shtrih.jpos.fiscalprinter.PrinterImages;
import com.shtrih.jpos.fiscalprinter.ReceiptImage;
import com.shtrih.jpos.fiscalprinter.ReceiptImages;
import com.shtrih.jpos.fiscalprinter.SmFptrConst;
import com.shtrih.printer.ncr7167.NCR7167Printer;
import com.shtrih.util.BitUtils;
import com.shtrih.util.CompositeLogger;
import com.shtrih.util.FileUtils;
import com.shtrih.util.FirmwareUpdater;
import com.shtrih.util.Hex;
import com.shtrih.util.Localizer;
import com.shtrih.util.MathUtils;
import com.shtrih.util.MethodParameter;
import com.shtrih.util.StringUtils;
import com.shtrih.util.SysUtils;
import com.shtrih.util.ArrayUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import jpos.JposConst;
import jpos.JposException;

import static jpos.JposConst.JPOS_E_EXTENDED;

public class SMFiscalPrinterImpl implements SMFiscalPrinter, PrinterConst {

    private int resultCode = 0;
    public PrinterProtocol device;
    // delay on wait
    public static final int TimeToSleep = 500;
    public static final int SMFP_EFPTR_PREVCOMMAND_TimeToSleep = 333;
    public static String charsetName = "Cp1251"; // device charset name
    // tax officer password
    public int taxPassword = 0;
    // operator password
    public int usrPassword = 1;
    // system administrator password
    public int sysPassword = 30;
    public boolean wrapText = true;
    private PrinterModel model = null;
    private final PrinterModels models = new PrinterModels();
    private FlexCommands commands = null;
    private DeviceMetrics deviceMetrics = new DeviceMetrics();
    private LongPrinterStatus longStatus = null;
    private ShortPrinterStatus shortStatus = new ShortPrinterStatus();
    /**
     * Cashed field info *
     */
    public PrinterTables tables = new PrinterTables();
    public static CompositeLogger logger = CompositeLogger.getLogger(SMFiscalPrinterImpl.class);
    private final List<IPrinterEvents> events = new ArrayList<IPrinterEvents>();
    private final List<byte[]> tlvItems = new ArrayList<byte[]>();
    private final PrinterPort port;
    private final FptrParameters params;
    private NCR7167Printer escPrinter = new NCR7167Printer(null);
    private final FieldInfoMap fields = new FieldInfoMap();
    private boolean capDiscount = true;
    private boolean capDisableDiscountText = false;
    private Boolean capLoadGraphics1 = null;
    private Boolean capLoadGraphics2 = null;
    private Boolean capLoadGraphics3 = null;
    private boolean capGraphics3Scale = false;
    private Boolean capPrintGraphics1 = null;
    private Boolean capPrintGraphics2 = null;
    private Boolean capPrintGraphics3 = null;
    private Boolean capPrintScaled = null;
    private Boolean capPrintGraphicsLine = null;
    private Boolean capPrintBarcode2 = null;
    private Boolean capPrintBarcode3 = null;
    private boolean capFSPrintItem = true;
    private boolean capCutter = true;
    private String fsUser = "";
    private String fsAddress = "";

    private boolean capOpenFiscalDay = true;
    private boolean capFiscalStorage = false;
    private int discountMode = PrinterConst.SMFP_DM_NOT_CHANGE_SUBTOTAL_SMALLDSC;
    private boolean saveCommands = false;
    private Vector receiptCommands = new Vector();
    private boolean capOpenReceipt = true;
    private boolean capFSTotals = true;
    private boolean capFooterFlag = false;
    private int headerHeigth = 0;
    private boolean isFooter = false;
    private PrinterModelParameters modelParameters = null;
    private String serial = "";
    private long lastDocNum = 0;
    private long lastDocMAC = 0;
    private PrinterDate lastDocDate = new PrinterDate();
    private PrinterTime lastDocTime = new PrinterTime();
    private long lastDocTotal = 0;
    private volatile boolean stopFlag = false;
    private volatile boolean interrupted = false;
    private Integer fdVersion = null;
    private boolean capLastErrorText = true;
    private boolean capErrorTextByCode = true;
    private FDOParameters fdoParameters = null;
    public boolean isTableTextCleared = false;
    private final Map<Integer, Integer> taxRates = new HashMap<Integer, Integer>();
    private int printMode = PrinterConst.PRINT_MODE_ENABLED;
    private boolean connected = false;
    private int operatorNumber = 1;
    private Boolean vvaSupported = null;

    public SMFiscalPrinterImpl(PrinterPort port, PrinterProtocol device,
            FptrParameters params) {
        this.port = port;
        this.device = device;
        this.params = params;

        models.load();
        model = models.itemByID(SMFP_MODELID_DEFAULT);
    }

    public boolean getCapFiscalStorage() {
        return capFiscalStorage;
    }

    public int getResultCode() {
        return resultCode;
    }

    public String getResultText() {
        try {
            return getErrorText(resultCode);
        } catch (Exception e) {
            return "";
        }
    }

    public void setEscPrinter(NCR7167Printer escPrinter) {
        this.escPrinter = escPrinter;
    }

    public FptrParameters getParams() {
        return params;
    }

    public void setDevice(PrinterProtocol device) {
        this.device = device;
    }

    public PrinterProtocol getDevice() {
        return device;
    }

    public static String getCharsetName() {
        return charsetName;
    }

    public void addEvents(IPrinterEvents item) {
        events.add(item);
    }

    public void removeEvents(IPrinterEvents item) {
        events.remove(item);
    }

    public Object getSyncObject() throws Exception {
        return port.getSyncObject();
    }

    public void deviceExecute(PrinterCommand command) throws Exception {
        synchronized (port.getSyncObject()) {
            Time.delay(params.commandDelayInMs);
            beforeCommand(command);
            correctDateTime(command);

            if (interrupted || Thread.currentThread().isInterrupted()) {
                throw new InterruptedException();
            }

            try {
                if (connected) {
                    port.open(getParams().portOpenTimeout);
                }
                device.send(command);
                resultCode = command.getResultCode();

            } catch (ClosedConnectionException e) {
                if (!command.getIsRepeatable()) {
                    throw new DeviceException(PrinterConst.SMFPTR_E_NOCONNECTION, e.getMessage());
                }
                command.setRepeatNeeded(true);
                return;
            } catch (Exception e) {
                port.close();
                throw new DeviceException(PrinterConst.SMFPTR_E_NOCONNECTION, e.getMessage());
            }

            if (command.isSucceeded()) {
                // After receipt close
                if (command.getCode() == 0xFF45) {
                    Thread.sleep(3000);
                }
                commandSucceeded(command);
            } else {
                if (command.getCode() != 0x6B) {
                    if (capLastErrorText) {
                        ReadLastErrorText command2 = readLastErrorText();
                        if (command2.isSucceeded()) {
                            logger.error(command2.getErrorText());
                        }
                    } else {
                        String text = getErrorText(command.getResultCode());
                        logger.error(text + ", " + command.getParametersText(commands));
                    }
                } else {
                    String text = getDriverErrorText(command.getResultCode());
                    logger.error(text + ", " + command.getParametersText(commands));
                }
            }
            afterCommand(command);
        }
    }

    private void commandSucceeded(PrinterCommand command) {
        try {
            switch (command.getCode()) {
                case 0x1E: {
                    if (isRegionalTable17Exists()) {
                        CommandInputStream in = new CommandInputStream(charsetName);
                        in.setData(command.getTxData());
                        in.readInt(); // password
                        int table = in.readByte();
                        int row = in.readShort();
                        int field = in.readByte();
                        if ((table == 17) && (row == 1) && (field == 7)) {
                            printMode = in.readByte();
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error(e);
        }
    }

    private boolean isRegionalTable17Exists() throws Exception {
        PrinterTable printerTable = getTable(17);
        return ((printerTable != null) && (printerTable.getName().equalsIgnoreCase("Региональные настройки")));
    }

    public void correctDateTime(PrinterCommand command) {
        // all models
        switch (command.getCode()) {
            case 0xFF41: // start open fiscal day 
            case 0xE0:   // open fiscal day
            case 0x8D:   // open receipt
            case 0xFF05: // start registration report
            case 0xFF06: // print registration report
            case 0xFF0B: // start fiscal day in FS
            case 0xFF34: // print reregistration report
            case 0xFF35: // start correction receipt
            case 0xFF36: // print correction receipt
            case 0xFF37: // start payments report
            case 0xFF38: // print payments report
            case 0xFF42: // start day close
            case 0xFF43: // close day in FS
            case 0xFF4A: // print correction receipt 2
                setCurrentDateTime();
        }
    }

    // correct date
    public void setCurrentDateTime() {
        logger.debug("setCurrentDateTime");
        if (params.validTimeDiffInSecs <= 0) {
            return;
        }
        if (!getCapFiscalStorage()) {
            return;
        }
        try {

            synchronized (port.getSyncObject()) {
                // wait to complete print operations
                LongPrinterStatus status = waitForPrintingLong();
                // write current date
                // if fiscal mode opened, last doc date must be less then date
                PrinterDate currentDate = new PrinterDate();
                PrinterTime currentTime = new PrinterTime();
                FSReadStatus fsStatus = fsReadStatus();

                if (!status.getDate().isEqual(currentDate)) {
                    if (fsStatus.getDate().beforeOrEqual(currentDate)) {
                        check(writeDate(currentDate));
                        check(confirmDate(currentDate));
                    }
                }
                // write time
                int timeDiffInSecs = status.getTime().getDiff(currentTime);
                if (timeDiffInSecs > params.validTimeDiffInSecs) {
                    logger.debug(String.format("FP time: %s, system time: %s",
                            status.getTime().toString(), currentTime.toString()));
                    check(writeTime(currentTime));
                }
            }
        } catch (Exception e) {
            logger.error("Correct date failed ", e);

        }
    }

    public LongPrinterStatus connect() throws Exception {
        logger.debug("connect");
        synchronized (port.getSyncObject()) {
            ReadPrinterModelParameters command = new ReadPrinterModelParameters();
            if (executeCommand(command) == 0) {
                modelParameters = command.getParameters();
                capLastErrorText = modelParameters.isCapCommand6B();
                capErrorTextByCode = modelParameters.isCapCommand6B();
                capCutter = modelParameters.isCapCutter();
            } else {
                modelParameters = null;
            }
            check(readDeviceMetrics());
            model = selectPrinterModel(getDeviceMetrics());
            readFullSerial();
            connected = true;
            return checkEcrMode();
        }
    }

    public void disconnect() {
        connected = false;
    }

    private void beforeCommand(PrinterCommand command) throws Exception {
        for (IPrinterEvents printerEvents : events) {
            try {
                printerEvents.beforeCommand(command);
            } catch (Exception e) {
                logger.error("beforeCommand", e);
            }
        }
    }

    private void afterCommand(PrinterCommand command) throws Exception {
        for (IPrinterEvents printerEvents : events) {
            try {
                printerEvents.afterCommand(command);
            } catch (Exception e) {
                logger.error("afterCommand", e);
            }
        }
    }

    public void check(int errorCode) throws Exception {
        if (errorCode != 0) {
            String text = getErrorText(errorCode);
            throw new DeviceException(errorCode, text);
        }
    }

    public void execute(PrinterCommand command) throws Exception {
        check(executeCommand(command));
    }

    public int getSysPassword() {
        return sysPassword;
    }

    public int getUsrPassword() {
        return usrPassword;
    }

    public int getTaxPassword() {
        return taxPassword;
    }

    public boolean failed(int errorCode) {
        return errorCode != 0;
    }

    public boolean succeeded(int errorCode) {
        return errorCode == 0;
    }

    public int executeCommand(PrinterCommand command) throws Exception {
        long startedAt = System.currentTimeMillis();

        String text = renderCommandCode(command.getCode()) + "h, " + command.getText();

        logger.debug(text);

        int timeout = getCommandTimeout(command.getCode());
        command.setTimeout(timeout);

        for (int i = 0; i < params.maxRepeatCount; i++) {
            command.setRepeatNeeded(false);
            deviceExecute(command);

            if (command.isSucceeded()) {
                break;
            }
            if (!command.getRepeatNeeded()) {
                break;
            }

            if (command.getResultCode() == SMFP_EFPTR_PREVCOMMAND) {
                // Do not count as an attempt, added to fix SHTRIH-MOBILE-F bug
                Time.delay(SMFP_EFPTR_PREVCOMMAND_TimeToSleep);
                i--;
            }
        }
        if (saveCommands && command.isSucceeded() && isReceiptCommand(command)) {
            BinaryCommand cmd = new BinaryCommand(command.getCode(),
                    command.getText(), command.getTxData());
            receiptCommands.add(cmd);
        }

        long doneAt = System.currentTimeMillis();

        logger.debug(text + " = " + command.getResultCode() + ", " + (doneAt - startedAt) + " ms");
        return command.getResultCode();
    }

    private String renderCommandCode(int commandCode) {
        return commandCode > 0xFF
                ? Hex.toHex((short) commandCode)
                : Hex.toHex((byte) commandCode);
    }

    public void startSaveCommands() {
        saveCommands = true;
        receiptCommands.clear();
        logger.debug("startSaveCommands");
    }

    public void stopSaveCommands() {
        saveCommands = false;
        logger.debug("stopSaveCommands");
    }

    public void clearReceiptCommands() {
        receiptCommands.clear();
    }

    public int printReceiptCommands() throws Exception {
        logger.debug("printReceiptCommands");
        int result = 0;
        for (int i = 0; i < receiptCommands.size(); i++) {
            PrinterCommand command = (PrinterCommand) receiptCommands.get(i);
            result = executeCommand(command);
            if (failed(result)) {
                return result;
            }
            waitForPrinting();
        }
        return result;
    }

    private boolean isReceiptCommand(PrinterCommand command) throws Exception {
        int[] codes = {
            0x80, 0x81, 0x82, 0x83, 0x84, 0x85, 0x86, 0x87, 0x88, 0x89,
            0x8A, 0x8B, 0x8C, 0x8D, 0x8E, 0x8F,
            0xFF0C, 0xFF0D, 0x12, 0x17, 0x2F
        };
        for (int i = 0; i < codes.length; i++) {
            if (command.getCode() == codes[i]) {
                return true;
            }
        }
        return false;
    }

    public void setTaxPassword(int taxPassword) {
        this.taxPassword = taxPassword;
    }

    public void setUsrPassword(int usrPassword) {
        this.usrPassword = usrPassword;
    }

    public void setSysPassword(int sysPassword) {
        this.sysPassword = sysPassword;
    }

    public Beep beep() throws Exception {
        logger.debug("beep");
        Beep command = new Beep();
        command.setPassword(usrPassword);
        execute(command);
        return command;
    }

    public ReadEJStatus readEJStatus() throws Exception {
        logger.debug("readEJStatus");
        ReadEJStatus command = new ReadEJStatus();
        command.setPassword(sysPassword);
        executeCommand(command);
        return command;
    }

    public int activateEJ() throws Exception {
        logger.debug("activateEJ");
        ActivateEJCommand command = new ActivateEJCommand();
        command.setPassword(sysPassword);
        return executeCommand(command);
    }

    public int closeEJArchive() throws Exception {
        logger.debug("closeEJArchive");
        CloseEJArhive command = new CloseEJArhive();
        command.setPassword(sysPassword);
        return executeCommand(command);
    }

    public int initEJArchive() throws Exception {
        logger.debug("initEJArchive");
        InitEJArchive command = new InitEJArchive();
        command.setPassword(sysPassword);
        return executeCommand(command);
    }

    public int testEJArchive() throws Exception {
        logger.debug("testEJArchive");
        TestEJArchive command = new TestEJArchive();
        command.setPassword(sysPassword);
        return executeCommand(command);
    }

    public int stopEJPrint() throws Exception {
        logger.debug("stopEJPrint");
        StopEJPrint command = new StopEJPrint(sysPassword);
        return executeCommand(command);
    }

    public int readEJDayReport(int dayNumber) throws Exception {
        logger.debug("readEJDayReport");
        ReadEJDayReport command = new ReadEJDayReport(sysPassword, dayNumber);
        return executeCommand(command);
    }

    public int readEJDayTotals(int dayNumber) throws Exception {
        logger.debug("readEJDayTotals");
        ReadEJDayTotals command = new ReadEJDayTotals(sysPassword, dayNumber);
        return executeCommand(command);
    }

    public int printEJDayReport(int dayNumber) throws Exception {
        logger.debug("printEJDayReport");
        PrintEJDayReport command = new PrintEJDayReport(sysPassword, dayNumber);
        return executeCommand(command);
    }

    public int printEJDayTotal(int dayNumber) throws Exception {
        logger.debug("printEJDayTotal");
        PrintEJDayTotal command = new PrintEJDayTotal(sysPassword, dayNumber);
        return executeCommand(command);
    }

    public int printEJDocument(int macNumber) throws Exception {
        logger.debug("PrintEJDocument");
        PrintEJDocument command = new PrintEJDocument(sysPassword, macNumber);
        return executeCommand(command);
    }

    public int printEJActivationReport() throws Exception {
        logger.debug("printEJActivationReport");
        PrintEJActivationReport command = new PrintEJActivationReport(
                sysPassword);
        return executeCommand(command);
    }

    public int cancelEJDocument() throws Exception {
        logger.debug("cancelEJDocument");
        CancelEJDocument command = new CancelEJDocument();
        command.setPassword(sysPassword);
        return executeCommand(command);
    }

    public int writeEJErrorCode(int errorCode) throws Exception {
        logger.debug("writeEJErrorCode");
        WriteEJErrorCode command = new WriteEJErrorCode(sysPassword, errorCode);
        return executeCommand(command);
    }

    public LongPrinterStatus readLongStatus() throws Exception {
        logger.debug("readLongStatus");
        ReadLongStatus command = new ReadLongStatus();
        command.setPassword(usrPassword);
        execute(command);
        longStatus = command.getStatus();
        operatorNumber = longStatus.getOperatorNumber();
        return command.getStatus();
    }

    public PrinterModelParameters readPrinterModelParameters() throws Exception {
        if (modelParameters != null) {
            return modelParameters;
        }

        logger.debug("readPrinterModelParameters");
        ReadPrinterModelParameters command = new ReadPrinterModelParameters();
        execute(command);
        modelParameters = command.getParameters();

        return modelParameters;
    }

    @Override
    public FSRequestFiscalizationTag fsRequestFiscalizationTag(int fiscId, int tagId) throws Exception {
        logger.debug("fsRequestFiscalizationTag(" + fiscId + ", " + tagId + ")");
        FSRequestFiscalizationTag command = new FSRequestFiscalizationTag(
                getSysPassword(), fiscId, tagId);
        execute(command);
        return command;
    }

    public byte[] fsReadFiscalizationTag(int fiscId, int tagId) throws Exception {
        synchronized (getSyncObject()) {
            FSRequestFiscalizationTag readDocument = fsRequestFiscalizationTag(fiscId, tagId);
            byte[] tagData = readDocument.getTagData();
            if (tagId == 0xFFFF) {
                tagData = fsReadDocumentTLVToEnd();
            }
            return tagData;
        }
    }

    public ShortPrinterStatus getShortStatus() {
        return shortStatus;
    }

    public ShortPrinterStatus readShortStatus() throws Exception {
        logger.debug("readShortStatus");
        ReadShortStatus command = new ReadShortStatus(usrPassword);
        execute(command);
        shortStatus = command.getStatus();
        operatorNumber = longStatus.getOperatorNumber();

        /*
         NumberFormat formatter = new DecimalFormat("#0.00");
         String text = formatter.format(command.getParameters().getPowerVoltage());
         logger.debug("Power voltage: " + text + " V");
         */
        return shortStatus;
    }

    public int printString(int station, String line) throws Exception {
        logger.debug("printString(" + String.valueOf(station) + ", '" + line
                + "')");

        if (line == null) {
            throw new InvalidParameterException("printString, line = null");
        }

        station = getPrintStation(station);
        PrintString command = new PrintString(usrPassword, station, getText(line));

        execute(command);
        Time.delay(getParams().printStringDelayInMs);
        return command.getOperator();
    }

    public int printBoldString(int station, String line) throws Exception {
        logger.debug("printBoldString(" + String.valueOf(station) + ", '"
                + line + "')");

        station = getPrintStation(station);
        PrintBoldString command = new PrintBoldString();
        command.setPassword(usrPassword);
        command.setStation(station);
        command.setLine(getTextBytes(line, 20));
        execute(command);
        return command.getOperator();
    }

    public void feedPaper(int station, int lineNumber) throws Exception {
        logger.debug("feedPaper");
        FeedPaper command = new FeedPaper();
        command.setPassword(usrPassword);
        command.setStations(station);
        command.setLineNumber(lineNumber);
        execute(command);
    }

    public int getPrintStation(int station) {
        if (capFooterFlag && isFooter) {
            station = station + SMFP_STATION_FOOTER;
        }
        return station;
    }

    public int printStringFont(int station, FontNumber font, String line) throws Exception {
        logger.debug("printStringFont(" + station + ", " + font.getValue() + ", '" + line + "')");

        station = getPrintStation(station);
        PrintStringFont command = new PrintStringFont(usrPassword, station,
                font, line);

        execute(command);
        Time.delay(getParams().printStringDelayInMs);
        return command.getOperator();
    }

    // line is truncated to maximum print width
    public int printLine(int station, String line, FontNumber font)
            throws Exception {

        if (line.length() == 0) {
            line = " ";
        }

        int len = Math.min(line.length(), getMessageLength(font));
        if (line.length() != len) {
            line = line.substring(0, len);
        }

        if (getModel().getCapPrintStringFont()) {
            return printStringFont(station, font, line);
        } else if (font.getValue() == PrinterConst.FONT_NUMBER_DOUBLE) {
            return printBoldString(station, line);
        } else {
            return printString(station, line);
        }
    }

    public String[] splitText(String text, int n, boolean wrap) {
        boolean isLineEnd = false;
        String line = "";
        Vector lines = new Vector();
        if (text.length() == 0) {
            lines.add("");
        } else {
            for (int i = 0; i < text.length(); i++) {
                switch (text.charAt(i)) {
                    case '\r':
                        break;

                    case '\n':
                        if (!isLineEnd) {
                            lines.add(line);
                            line = "";
                            isLineEnd = true;
                        }
                        break;

                    default:
                        isLineEnd = false;
                        line = line + text.charAt(i);
                        if (wrap) {
                            if (line.length() == n) {
                                lines.add(line);
                                line = "";
                                isLineEnd = true;
                            }
                        }
                }
            }
            if (line.length() != 0) {
                lines.add(line);
            }
        }
        return (String[]) lines.toArray(new String[0]);
    }

    public String[] splitText(String text, FontNumber font) throws Exception {
        int len = getMessageLength(font);
        return splitText(text, len, wrapText);
    }

    public int writeTable(int tableNumber, int rowNumber, int fieldNumber,
            String fieldValue) throws Exception {
        int result = 0;
        logger.debug("writeTable(" + String.valueOf(tableNumber) + ", "
                + String.valueOf(rowNumber) + ", "
                + String.valueOf(fieldNumber) + ", " + "'" + fieldValue + "')");

        FieldInfo fieldInfo = fields.find(tableNumber, fieldNumber);
        if (fieldInfo == null) {
            ReadFieldInfo command = new ReadFieldInfo();
            command.setPassword(sysPassword);
            command.setTable(tableNumber);
            command.setField(fieldNumber);
            result = executeCommand(command);
            if (failed(result)) {
                return result;
            }
            fieldInfo = command.getFieldInfo();
        }
        byte[] fieldBytes = fieldInfo.fieldToBytes(fieldValue, charsetName);
        PrinterCommand command2 = new WriteTable(sysPassword, tableNumber,
                rowNumber, fieldNumber, fieldBytes);
        result = executeCommand(command2);
        return result;
    }

    public void updateTables() throws Exception {
        if (tables.size() > 0) {
            return;
        }

        int tableNumber = SMFP_TABLE_SETUP;
        while (true) {
            ReadTableInfo command = new ReadTableInfo();
            command.setPassword(sysPassword);
            command.setTableNumber(tableNumber);
            int result = executeCommand(command);
            if (result == SMFP_EFPTR_INVALID_TABLE) {
                break;
            }
            check(result);

            PrinterTable table = command.getTable();
            tables.add(table);

            for (int fieldNumber = 1; fieldNumber <= table.getFieldCount(); fieldNumber++) {
                FieldInfo fieldInfo = fields.find(tableNumber, fieldNumber);
                if (fieldInfo == null) {
                    fieldInfo = readFieldInfo(tableNumber, fieldNumber);
                }
                PrinterField field = new PrinterField(fieldInfo, 1);
                table.getFields().add(field);
            }
            tableNumber++;
        }
    }

    public String readTable(String tableName, String fieldName) throws Exception {
        updateTables();
        PrinterTable table = tables.itemByName(tableName);
        PrinterField field = table.getFields().itemByName(fieldName);
        readField(field);
        return field.getValue();
    }

    public String readTable(int tableNumber, int rowNumber, int fieldNumber)
            throws Exception {
        String[] result = new String[1];
        check(readTable(tableNumber, rowNumber, fieldNumber, result));
        return result[0];
    }

    public int readTable(int tableNumber, int rowNumber, int fieldNumber,
            String[] fieldValue) throws Exception {
        int result = 0;
        logger.debug("readTable(" + String.valueOf(tableNumber) + ", "
                + String.valueOf(rowNumber) + ", "
                + String.valueOf(fieldNumber) + ")");

        FieldInfo fieldInfo = fields.find(tableNumber, fieldNumber);
        if (fieldInfo == null) {
            ReadFieldInfo command = new ReadFieldInfo();
            command.setPassword(sysPassword);
            command.setTable(tableNumber);
            command.setField(fieldNumber);
            result = executeCommand(command);
            if (failed(result)) {
                return result;
            }
            fieldInfo = command.getFieldInfo();
        }

        ReadTable commandReadTable = new ReadTable(sysPassword, tableNumber,
                rowNumber, fieldNumber);
        result = executeCommand(commandReadTable);
        if (failed(result)) {
            return result;
        }
        fieldValue[0] = fieldInfo.bytesToField(commandReadTable.fieldValue, charsetName);
        return result;
    }

    public String readTable2(int tableNumber, int rowNumber, int fieldNumber) throws Exception {
        int result = 0;
        logger.debug("readTable2(" + String.valueOf(tableNumber) + ", "
                + String.valueOf(rowNumber) + ", "
                + String.valueOf(fieldNumber) + ")");

        PrinterField field = getPrinterField(tableNumber, rowNumber, fieldNumber);
        if (!field.hasValue()) {
            readField(field);
        }
        return field.getValue();
    }

    private PrinterField getPrinterField(int tableNumber, int rowNumber,
            int fieldNumber) throws Exception {
        PrinterTable table = getTable(tableNumber);
        PrinterField field = table.getFields().find(tableNumber, rowNumber, fieldNumber);
        if (field == null) {
            FieldInfo fieldInfo = readFieldInfo2(tableNumber, fieldNumber);
            field = new PrinterField(fieldInfo, rowNumber);
            table.getFields().add(field);
        }
        return field;
    }

    private FieldInfo readFieldInfo2(int tableNumber, int fieldNumber) throws Exception {
        FieldInfo fieldInfo = fields.find(tableNumber, fieldNumber);
        if (fieldInfo == null) {
            fieldInfo = readFieldInfo(tableNumber, fieldNumber);
            fields.add(fieldInfo);
        }
        return fieldInfo;
    }

    public PrinterField readField(int tableNumber, int rowNumber, int fieldNumber) throws Exception {
        logger.debug("readTable(" + String.valueOf(tableNumber) + ", "
                + String.valueOf(rowNumber) + ", "
                + String.valueOf(fieldNumber) + ")");

        FieldInfo fieldInfo = fields.find(tableNumber, fieldNumber);
        if (fieldInfo == null) {
            fieldInfo = readFieldInfo(tableNumber, fieldNumber);
        }
        PrinterField field = new PrinterField(fieldInfo, rowNumber);
        readField(field);
        return field;
    }

    public ReadTableInfo readTableInfo(int tableNumber) throws Exception {
        logger.debug("readTableInfo");
        ReadTableInfo command = new ReadTableInfo();
        command.setPassword(sysPassword);
        command.setTableNumber(tableNumber);
        executeCommand(command);
        return command;
    }

    public PrintCashIn printCashIn(long amount) throws Exception {
        logger.debug("printCashIn");
        PrintCashIn command = new PrintCashIn();
        command.setPassword(usrPassword);
        command.setAmount(amount);
        execute(command);
        return command;
    }

    public PrintCashOut printCashOut(long sum) throws Exception {
        logger.debug("printCashOut");
        PrintCashOut command = new PrintCashOut();
        command.setPassword(usrPassword);
        command.setAmount(sum);
        execute(command);
        return command;
    }

    public int continuePrint() throws Exception {
        logger.debug("continuePrint");
        ContinuePrint command = new ContinuePrint();
        command.setPassword(usrPassword);
        return executeCommand(command);
    }

    public BeginTest startTest(int periodInMinutes) throws Exception {
        logger.debug("startTest");
        BeginTest command = new BeginTest();
        command.setPassword(usrPassword);
        command.setPeriodInMinutes(periodInMinutes);
        execute(command);
        return command;
    }

    public EndTest stopTest() throws Exception {
        logger.debug("stopTest");
        EndTest command = new EndTest();
        command.setPassword(usrPassword);
        execute(command);
        return command;
    }

    public VoidFiscalReceipt cancelReceipt() throws Exception {
        logger.debug("cancelReceipt");
        VoidFiscalReceipt command = new VoidFiscalReceipt();
        command.setPassword(usrPassword);
        execute(command);
        return command;
    }

    public VoidFiscalReceipt cancelReceipt(int password) throws Exception {
        logger.debug("cancelReceipt");
        VoidFiscalReceipt command = new VoidFiscalReceipt();
        command.setPassword(password);
        execute(command);
        return command;
    }

    public EndFiscalReceipt closeReceipt(CloseRecParams params)
            throws Exception {
        params.setText(getText(params.getText()));
        EndFiscalReceipt command = new EndFiscalReceipt();
        command.setPassword(usrPassword);
        command.setParams(params);

        check(closeReceipt(command));
        return command;
    }

    public int closeReceipt(EndFiscalReceipt command)
            throws Exception {
        logger.debug("closeReceipt");
        writeTLVItems();

        int rc = executeCommand(command);
        if (rc == 168) {
            ReadEJStatus command2 = new ReadEJStatus();
            command2.setPassword(sysPassword);
            int rc2 = executeCommand(command2);
            if (rc2 == 0) {
                logger.debug("EJ date: " + command2.getStatus().getDocDate().toString());
                logger.debug("EJ time: " + command2.getStatus().getDocTime().toString());
            }
        }
        return rc;
    }

    public int fsCloseReceipt(FSCloseReceipt command) throws Exception {
        writeTLVItems();

        lastDocNum = 0;
        lastDocMAC = 0;
        lastDocTotal = getSubtotal();
        int rc = executeCommand(command);
        if (succeeded(rc)) {
            lastDocNum = command.getDocNum();
            lastDocMAC = command.getDocMAC();
            lastDocDate = command.getDocDate();
            lastDocTime = command.getDocTime();
            if (lastDocDate == null) {
                lastDocDate = new PrinterDate();
                lastDocTime = new PrinterTime();
                try {
                    ReadLongStatus sCommand = new ReadLongStatus();
                    sCommand.setPassword(usrPassword);
                    executeCommand(sCommand);
                    if (sCommand.isSucceeded()) {
                        LongPrinterStatus status = sCommand.getStatus();
                        lastDocDate = status.getDate();
                        lastDocTime = status.getTime();
                    }
                } catch (Exception e) {
                }
            }
        }
        return rc;
    }

    public long getSubtotal() throws Exception {
        logger.debug("getSubtotal");
        ReadSubtotal command = new ReadSubtotal(usrPassword);
        execute(command);
        return command.getSum();
    }

    public ReadOperationRegister readOperationRegister2(int number)
            throws Exception {
        logger.debug("readOperationRegister2");
        ReadOperationRegister command = new ReadOperationRegister(usrPassword,
                number);
        executeCommand(command);
        return command;
    }

    public int readOperationRegister(OperationRegister register)
            throws Exception {
        logger.debug("readOperationRegister");
        ReadOperationRegister command = new ReadOperationRegister(usrPassword,
                register.getNumber());
        int result = executeCommand(command);
        if (result == 0) {
            register.setValue(command.getValue());
        }
        return result;
    }

    public int readOperationRegister(int number) throws Exception {
        logger.debug("readOperationRegister");
        ReadOperationRegister command = new ReadOperationRegister(usrPassword,
                number);
        execute(command);
        return command.getValue();
    }

    public int readCashRegister(CashRegister register) throws Exception {
        logger.debug("readCashRegister");
        ReadCashRegister command = new ReadCashRegister(usrPassword, register.getNumber());
        int result = executeCommand(command);
        if (result == 0) {
            register.setValue(command.getValue());
        }
        return result;
    }

    public long readCashRegister(int number) throws Exception {
        logger.debug("readCashRegister");
        ReadCashRegister command = readCashRegister2(number);
        execute(command);
        return command.getValue();
    }

    public ReadCashRegister readCashRegister2(int number) throws Exception {
        logger.debug("readCashRegister");
        ReadCashRegister command = new ReadCashRegister(usrPassword, number);
        execute(command);
        return command;
    }

    /*
     185.Накопление скидок с продаж в смене
     186.Накопление скидок с покупок в смене
     187.Накопление скидок с возврата продаж в смене
     188.Накопление скидок с возврата покупок в смене
     */
    public boolean isDayDiscountRegister(int number) throws Exception {
        return (number >= 185) && (number <= 188);
    }

    public long readCashRegisterCorrection(int number) throws Exception {
        logger.debug("readCashRegister");
        ReadCashRegister command = readCashRegister2(number);
        execute(command);
        if (getCapFiscalStorage() && isDayDiscountRegister(number)) {
            int recType = number - 185;
            long amount = readDayTotals(recType) - readDayPayments(recType);
            command.setValue(amount);
        }
        return command.getValue();
    }

    public long readDayPayments(int recType) throws Exception {
        long result = 0;
        // Payment types 1..4
        for (int i = 0; i <= 3; i++) {
            result += readCashRegister(193 + recType + i * 4);
        }
        // Payment types 1..4
        for (int i = 0; i <= 11; i++) {
            result += readCashRegister(4144 + recType + i * 4);
        }
        return result;
    }

    public long readDayTotals(int recType) throws Exception {
        long result = 0;
        for (int i = 0; i <= 15; i++) {
            result += readCashRegister(121 + recType + i * 4);
        }
        return result;
    }

    public PrintEJDayReportOnDates printEJDayReportOnDates(PrinterDate date1,
            PrinterDate date2, int reportType) throws Exception {
        logger.debug("printEJDayReportOnDates");
        PrintEJDayReportOnDates command = new PrintEJDayReportOnDates();
        command.setPassword(sysPassword);
        command.setReportType(reportType);
        command.setDate1(date1);
        command.setDate2(date2);
        execute(command);
        return command;
    }

    public PrintFMReportDates printFMReportDates(PrinterDate date1,
            PrinterDate date2, int reportType) throws Exception {
        logger.debug("printFMReportDates");
        PrintFMReportDates command = new PrintFMReportDates();
        command.setPassword(taxPassword);
        command.setReportType(reportType);
        command.setDate1(date1);
        command.setDate2(date2);
        execute(command);
        return command;
    }

    public PrintEJDayReportOnDays printEJReportDays(int day1, int day2,
            int reportType) throws Exception {
        logger.debug("printEJReportDays");
        PrintEJDayReportOnDays command = new PrintEJDayReportOnDays();
        command.setPassword(sysPassword);
        command.setReportType(reportType);
        command.setDayNumber1(day1);
        command.setDayNumber2(day2);
        execute(command);
        return command;
    }

    public PrintFMReportDays printFMReportDays(int day1, int day2,
            int reportType) throws Exception {
        logger.debug("printFMReportDays");
        PrintFMReportDays command = new PrintFMReportDays();
        command.setPassword(taxPassword);
        command.setReportType(reportType);
        command.setSession1(day1);
        command.setSession2(day2);
        execute(command);
        return command;
    }

    public String getRecFormatText(String text, String param) throws Exception {
        String result = text;

        if (getCapParameter(SMFP_PARAMID_RECFORMAT_ENABLED)
                && getCapParameter(param)) {
            if (readBoolParameter(SMFP_PARAMID_RECFORMAT_ENABLED)) {
                int len = readIntParameter(param);
                len = Math.min(len, result.length());
                result = result.substring(0, len);
            }
        }
        return result;
    }

    public String getRecItemText(String text) throws Exception {
        String result = text;
        if (getCapParameter(SMFP_PARAMID_RECFORMAT_ENABLED)
                && getCapParameter(SMFP_PARAMID_ITM_NAME_LEN)) {
            if (readBoolParameter(SMFP_PARAMID_RECFORMAT_ENABLED)) {
                int len = readIntParameter(SMFP_PARAMID_ITM_NAME_LEN);
                len = Math.min(len, result.length());
                result = result.substring(0, len);
            }
        }
        return getText(result);
    }

    public int fsPrintRecItem2(int operation, PriceItem item) throws Exception {
        FSReceiptItem fsReceiptItem = new FSReceiptItem();
        fsReceiptItem.setOperation(operation);
        fsReceiptItem.setQuantity(Math.round(item.getQuantity() * 1000000.0));
        fsReceiptItem.setPrice(item.getPrice());
        fsReceiptItem.setAmount(item.getTotalAmount() == null ? 0xFFFFFFFFFFL : item.getTotalAmount());
        fsReceiptItem.setTaxAmount(item.getTaxAmount() == null ? 0L : item.getTaxAmount());
        fsReceiptItem.setTax(getTaxBits(item.getTax1()));
        fsReceiptItem.setDepartment(item.getDepartment());
        fsReceiptItem.setPaymentType(item.getPaymentType());
        fsReceiptItem.setPaymentItem(item.getSubjectType());
        fsReceiptItem.setText(item.getText());
        int rc = fsPrintRecItem(fsReceiptItem);
        capFSPrintItem = isCommandSupported(rc);
        return rc;
    }

    public boolean isPeaceQuantity(double quantity) {
        return (long) ((quantity - (long) quantity) * 1000000) == 0;
    }

    public void checkItemCode(CheckCodeRequest request) throws Exception {
        if (request.getData() == null) {
            return;
        }
        if (getFDVersion() != PrinterConst.FS_FORMAT_FFD_1_2) {
            return;
        }

        boolean isPeace = isPeaceQuantity(request.getQuantity());
        int itemStatus = FSCheckMC.FS_ITEM_STATUS_NOCHANGE;
        if (request.isSale()) {
            if (isPeace) {
                itemStatus = FSCheckMC.FS_ITEM_STATUS_PIECE_SELL;
            } else {
                itemStatus = FSCheckMC.FS_ITEM_STATUS_WEIGHT_SELL;
            }
        } else if (isPeace) {
            itemStatus = FSCheckMC.FS_ITEM_STATUS_PIECE_RETURN;
        } else {
            itemStatus = FSCheckMC.FS_ITEM_STATUS_WEIGHT_RETURN;
        }
        byte[] tlv = new byte[0];
        if (!isPeace) {
            TLVItems items = new TLVItems();
            items.add(new TLVItem(2108, String.valueOf(request.getUnit())));
            items.add(new TLVItem(1023, String.valueOf(request.getQuantity())));
            TLVItem item = new TLVItem(1291);
            items.add(item);
            item.getItems().add(new TLVItem(1293, String.valueOf(request.getNumerator())));
            item.getItems().add(new TLVItem(1294, String.valueOf(request.getDenominator())));

            TLVWriter writer = new TLVWriter();
            writer.add(items);
            tlv = writer.getBytes();
        }

        FSCheckMC command = new FSCheckMC();
        command.password = sysPassword;
        command.itemStatus = itemStatus;
        command.checkMode = 0;
        command.mcData = request.getData();
        command.tlv = tlv;

        execute(command);
        if (command.localCheckStatus == 0) {
            if (command.localErrorCode == FSCheckMC.FS_LEC_FS_HAS_NO_KEY) {
                throw new Exception("Fiscal storage has no key for this MC type");
            }
            if (command.localErrorCode == FSCheckMC.FS_LEC_MC_FORMAT_ERROR) {
                throw new Exception("MC format error");
            }
            if (command.localErrorCode == FSCheckMC.FS_LEC_CHECK_FAILED) {
                throw new Exception("MC check failed");
            }
        }
        if (command.localCheckStatus == 1) {
            throw new Exception("MC check return negative status");
        }
        if (command.serverCheckStatus == 0x20) {
            throw new Exception(command.getServerErrorCodeText());
        }
        checkTag2106(command.serverErrorCode);
        // accept
        FSAcceptMC acceptCommand = new FSAcceptMC();
        acceptCommand.setPassword(sysPassword);
        acceptCommand.setAction(1);
        execute(acceptCommand);
        checkTag2106(acceptCommand.getErrorCode());
    }

    private void checkTag2106(int errorCode) throws Exception {
        boolean isMCChecked = BitUtils.testBit(errorCode, 0);
        boolean isMCCRCChecked = BitUtils.testBit(errorCode, 1);
        boolean isMSChecked = BitUtils.testBit(errorCode, 2);
        boolean isMSItemStatusOK = BitUtils.testBit(errorCode, 3);
        if (isMCChecked && (!isMCCRCChecked)) {
            throw new Exception("Результат проверки КП КМ отрицательный");
        }
        if (isMSChecked && (!isMSItemStatusOK)) {
            throw new Exception("Планируемый статус товара некорректен");
        }
    }

    public void printSale(PriceItem item) throws Exception {
        logger.debug("printSale");
        String text = getRecItemText(item.getText());
        item.setText(text);

        if (capFSPrintItem) {
            int rc = fsPrintRecItem2(PrinterConst.SMFP_OPERATION_SALE, item);
            if (isCommandSupported(rc)) {
                check(rc);
                return;
            }
        }
        PrintSale command = new PrintSale(usrPassword, item);
        execute(command);
    }

    public void printVoidSale(PriceItem item) throws Exception {
        logger.debug("printVoidSale");
        String text = getRecItemText(item.getText());
        item.setText(text);

        if (capFSPrintItem) {
            int rc = fsPrintRecItem2(PrinterConst.SMFP_OPERATION_RETSALE, item);
            if (isCommandSupported(rc)) {
                check(rc);
                return;
            }
        }

        PrintVoidSale command = new PrintVoidSale(usrPassword, item);
        execute(command);
    }

    public void printRefund(PriceItem item) throws Exception {
        logger.debug("printRefund");

        String text = getRecItemText(item.getText());
        item.setText(text);

        if (capFSPrintItem) {
            int rc = fsPrintRecItem2(PrinterConst.SMFP_OPERATION_BUY, item);
            if (isCommandSupported(rc)) {
                check(rc);
                return;
            }
        }
        PrintRefund command = new PrintRefund(usrPassword, item);
        execute(command);
    }

    public void printVoidRefund(PriceItem item) throws Exception {
        logger.debug("printVoidRefund");

        String text = getRecItemText(item.getText());
        item.setText(text);

        if (capFSPrintItem) {
            int rc = fsPrintRecItem2(PrinterConst.SMFP_OPERATION_RETBUY, item);
            if (isCommandSupported(rc)) {
                check(rc);
                return;
            }
        }
        PrintVoidRefund command = new PrintVoidRefund(usrPassword, item);
        execute(command);
    }

    public PrintVoidItem printVoidItem(PriceItem item) throws Exception {
        logger.debug("printVoidItem");

        String text = getRecItemText(item.getText());
        item.setText(text);

        PrintVoidItem command = new PrintVoidItem();
        command.setPassword(usrPassword);
        command.setItem(item);
        execute(command);
        return command;
    }

    public PrintDiscount printDiscount(AmountItem item) throws Exception {
        logger.debug("printDiscount");

        String text = getRecFormatText(item.getText(), SMFP_PARAMID_DSC_TEXT_LEN);
        if (text.equalsIgnoreCase("СКИДКА")) {
            text = "";
        }
        item.setText(getText(text));

        PrintDiscount command = new PrintDiscount();
        command.setPassword(usrPassword);
        command.setItem(item);
        execute(command);
        return command;
    }

    public PrintVoidDiscount printVoidDiscount(AmountItem item)
            throws Exception {
        logger.debug("printVoidDiscount");

        String text = getRecFormatText(item.getText(), SMFP_PARAMID_DSCVOID_TEXT_LEN);
        item.setText(text);

        PrintVoidDiscount command = new PrintVoidDiscount();
        command.setPassword(usrPassword);
        command.setItem(item);
        execute(command);
        return command;
    }

    public PrintCharge printCharge(AmountItem item) throws Exception {
        logger.debug("printCharge");

        String text = getRecFormatText(item.getText(), SMFP_PARAMID_CHR_TEXT_LEN);
        item.setText(text);

        PrintCharge command = new PrintCharge();
        command.setPassword(usrPassword);
        command.setItem(item);
        execute(command);
        return command;
    }

    public PrintVoidCharge printVoidCharge(AmountItem item) throws Exception {
        logger.debug("printVoidCharge");

        String text = getRecFormatText(item.getText(), SMFP_PARAMID_CHRVOID_TEXT_LEN);
        item.setText(text);

        PrintVoidCharge command = new PrintVoidCharge();
        command.setPassword(usrPassword);
        command.setItem(item);
        execute(command);
        return command;
    }

    public ReadFMLastRecordDate readFMLastRecordDate() throws Exception {
        logger.debug("getLastFmRecordDate");

        ReadFMLastRecordDate command = new ReadFMLastRecordDate();
        command.setPassword(sysPassword);
        execute(command);
        return command;
    }

    public PrintXReport printXReport() throws Exception {
        logger.debug("printXReport");

        PrintXReport command = new PrintXReport();
        command.setPassword(sysPassword);
        execute(command);
        return command;
    }

    public int fsStartDayClose() throws Exception {
        FSStartDayClose command = new FSStartDayClose();
        command.setSysPassword(sysPassword);
        return executeCommand(command);
    }

    public int fsStartDayOpen() throws Exception {
        FSStartDayOpen command = new FSStartDayOpen();
        command.setSysPassword(sysPassword);
        return executeCommand(command);
    }

    public void resetPrinter() throws Exception {
        tlvItems.clear();
        interrupted = false;
    }

    public void writeTLVItems() throws Exception {
        if (tlvItems.size() == 0) {
            return;
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        for (int i = 0; i < tlvItems.size(); i++) {
            baos.write(tlvItems.get(i));
        }
        TLVReader reader = new TLVReader();
        TLVItems src = reader.read(baos.toByteArray());
        TLVItems dst = new TLVItems();

        filterTLV(src, dst, getFDVersion());
        TLVWriter writer = new TLVWriter();
        for (int i = 0; i < dst.size(); i++) {
            writer.clear();
            writer.add(dst.get(i));
            byte[] tlv = writer.getBytes();
            if (tlv.length != 0) {
                FSWriteTLV command = new FSWriteTLV();
                command.setSysPassword(sysPassword);
                command.setTlv(tlv);
                execute(command);
            }
        }
        tlvItems.clear();
    }

    public PrintZReport printZReport() throws Exception {
        if (!tlvItems.isEmpty()) {
            if (fsReadStatus().getDocType().isDocClosed()) {
                check(fsStartDayClose());
            }
            writeTLVItems();
        }

        logger.debug("printZReport");
        PrintZReport command = new PrintZReport();
        command.setPassword(sysPassword);
        execute(command);
        return command;
    }

    public int printDepartmentReport() throws Exception {
        logger.debug("printDepartmentReport");
        PrintDepartmentReport command = new PrintDepartmentReport();
        command.setPassword(sysPassword);
        return executeCommand(command);
    }

    public int printTaxReport() throws Exception {
        logger.debug("printTaxReport");
        PrintTaxReport command = new PrintTaxReport(sysPassword);
        return executeCommand(command);
    }

    public int printTotalizers() throws Exception {
        logger.debug("printTotalizers");
        PrintTotalizers command = new PrintTotalizers(sysPassword);
        return executeCommand(command);
    }

    public int bufferZReport() throws Exception {
        logger.debug("bufferZReport");
        BufferZReport command = new BufferZReport();
        command.setPassword(sysPassword);
        return executeCommand(command);
    }

    public int printBufferedZReport() throws Exception {
        logger.debug("printBufferedZReport");
        PrintBufferedZReport command = new PrintBufferedZReport();
        command.setPassword(sysPassword);
        return executeCommand(command);
    }

    public int printHeader() throws Exception {
        logger.debug("printHeader");
        PrintHeader command = new PrintHeader(sysPassword);
        return executeCommand(command);
    }

    public int printTrailer() throws Exception {
        logger.debug("printTrailer");
        PrintTrailer command = new PrintTrailer(sysPassword);
        return executeCommand(command);
    }

    public int writeDate(PrinterDate date) throws Exception {
        logger.debug("writeDate");

        SetDateCommand command = new SetDateCommand(sysPassword, date);
        return executeCommand(command);
    }

    public int confirmDate(PrinterDate date) throws Exception {
        logger.debug("confirmDate");

        ConfirmDate command = new ConfirmDate();
        command.setPassword(sysPassword);
        command.setDate(date);
        return executeCommand(command);
    }

    public int writeTime(PrinterTime time) throws Exception {
        logger.debug("setTime");

        WriteTime command = new WriteTime();
        command.setPassword(sysPassword);
        command.setTime(time);
        return executeCommand(command);
    }

    public int writePortParams(int portNumber, int baudRate, int timeout)
            throws Exception {
        logger.debug("writePortParams(" + String.valueOf(portNumber) + ", "
                + String.valueOf(baudRate) + ", " + String.valueOf(timeout)
                + ")");

        // For ports >= 3, fiscal printer return error
        if (portNumber >= 3) {
            return 0;
        }
        MethodParameter.checkByte(portNumber, "portNumber");
        MethodParameter.checkByte(baudRate, "baudRate");
        WritePortParams command = new WritePortParams();
        command.setPassword(sysPassword);
        command.setPortNumber(portNumber);
        command.setBaudRate(baudRate);
        command.setTimeout(timeout);
        int rc = executeCommand(command);
        Time.delay(300);
        return rc;
    }

    public void printBarcode(String barcode) throws Exception {
        logger.debug("printBarcode");

        PrintBarcode command = new PrintBarcode();
        command.setPassword(usrPassword);
        command.setBarcode(barcode);
        execute(command);
    }

    public void duplicateReceipt() throws Exception {
        logger.debug("duplicateReceipt");

        PrintReceiptCopy command = new PrintReceiptCopy(sysPassword);
        execute(command);
    }

    public void reportRemoteRSSI() {
        try {
            int[] data = new int[1];
            port.directIO(PrinterPort.DIO_REPORT_RSSI, data, null);
        } catch (Exception e) {
            logger.error("reportRemoteRSSI", e);
        }
    }

    public void openReceipt(int receiptType) throws Exception {
        logger.debug("openReceipt");

        reportRemoteRSSI();
        if (capOpenReceipt) {
            OpenReceipt command = new OpenReceipt();
            command.setPassword(usrPassword);
            command.setReceiptType(receiptType);
            int rc = executeCommand(command);
            if (command.isSucceeded()) {
                operatorNumber = command.getOperator();
            }

            capOpenReceipt = isCommandSupported(rc);
            if (capOpenReceipt) {
                check(rc);
                writeTLVItems();
            }
        }
    }

    public int loadGraphics1(int lineNumber, byte[] data) throws Exception {
        logger.debug("loadGraphics, " + String.valueOf(lineNumber));
        MethodParameter.checkRange(lineNumber, 0, 255, "lineNumber");

        LoadGraphics command = new LoadGraphics();
        command.setPassword(usrPassword);
        command.setLineNumber(lineNumber);
        command.setData(data);
        return executeCommand(command);
    }

    public int loadGraphics2(int lineNumber, byte[] data) throws Exception {
        logger.debug("loadGraphics2, " + String.valueOf(lineNumber));
        MethodParameter.checkRange(lineNumber, 0, 65535, "lineNumber");

        LoadGraphics2 command = new LoadGraphics2();
        command.setPassword(usrPassword);
        command.setLineNumber(lineNumber);
        command.setData(data);
        return executeCommand(command);
    }

    public int loadGraphics3(int lineNumber, byte[] data) throws Exception {
        logger.debug("loadGraphics3, " + String.valueOf(lineNumber));
        LoadGraphics3 command = new LoadGraphics3();
        command.setPassword(usrPassword);
        command.setLineLength(data.length);
        command.setLineNumber(lineNumber);
        command.setLineCount(1);
        command.setBufferType(1);
        command.setData(data);
        return executeCommand(command);
    }

    public int printGraphics1(int line1, int line2) throws Exception {
        logger.debug("printGraphics(" + String.valueOf(line1) + ", "
                + String.valueOf(line2) + ")");

        PrintGraphics command = new PrintGraphics(usrPassword, line1, line2);
        return executeCommand(command);
    }

    public int printGraphics3(int line1, int line2) throws Exception {
        logger.debug("printGraphics3(" + String.valueOf(line1) + ", "
                + String.valueOf(line2) + ")");

        PrintGraphics3 command = new PrintGraphics3();
        command.setPassword(usrPassword);
        command.setLine1(line1);
        command.setLine2(line2);
        command.setVscale(1);
        command.setHscale(1);
        command.setFlags(getPrintStation(SMFP_STATION_REC));
        return executeCommand(command);
    }

    public int printGraphics2(int line1, int line2) throws Exception {
        logger.debug("printGraphics2(" + String.valueOf(line1) + ", "
                + String.valueOf(line2) + ")");

        PrintGraphics2 command = new PrintGraphics2(usrPassword, line1, line2);
        return executeCommand(command);
    }

    public void endDump() throws Exception {
        logger.debug("endDump");
        EndDump command = new EndDump();
        command.setPassword(usrPassword);
        execute(command);
    }

    public int printGraphicLine(int station, int height, byte[] data) throws Exception {
        logger.debug("printGraphicLine");

        station = getPrintStation(station);
        PrintGraphicLine command = new PrintGraphicLine();
        command.setPassword(usrPassword);
        command.setHeight(height);
        command.setFlags(station);
        command.setCapFlags(capGraphicsFlags());
        command.setData(data);
        Time.delay(params.getGraphicsLineDelay());
        return executeCommand(command);
    }

    // CutPaper
    public int cutPaper(int cutType) throws Exception {
        logger.debug("cutPaper");
        CutPaper command = new CutPaper();
        command.setPassword(usrPassword);
        command.setCutType(cutType);
        return executeCommand(command);
    }

    public void openCashDrawer(int drawerNumber) throws Exception {
        logger.debug("openDrawer");
        OpenCashDrawer command = new OpenCashDrawer();
        command.setPassword(usrPassword);
        command.setDrawerNumber(drawerNumber);
        execute(command);
    }

    public void waitForFiscalMemory() throws Exception {
        int resultCode = 0;
        for (int i = 0; i < 3; i++) {
            ReadLongStatus command = new ReadLongStatus();
            command.setPassword(getUsrPassword());
            resultCode = executeCommand(command);
            if (resultCode == 0) {
                break;
            }
        }
        check(resultCode);
    }

    public void waitForElectronicJournal() throws Exception {
        int maxCount = 3;
        for (int i = 1; i <= maxCount; i++) {
            ReadEJStatus command = readEJStatus();
            if (command.isSucceeded()) {
                return;
            }
            if (i == maxCount) {
                check(command.getResultCode());
            }
        }
    }

    public PrinterStatus waitForPrinting() throws Exception {
        logger.debug("waitForPrinting");

        synchronized (port.getSyncObject()) {
            for (;;) {
                PrinterStatus status = readPrinterStatus();
                if (checkPrinterStatus(status)) {
                    return status;
                }
            }
        }
    }

    public LongPrinterStatus waitForPrintingLong() throws Exception {
        logger.debug("waitForPrintingLong");
        synchronized (port.getSyncObject()) {
            for (;;) {
                LongPrinterStatus status = readLongStatus();
                if (checkPrinterStatus(status.getPrinterStatus())) {
                    return status;
                }
            }
        }
    }

    private boolean checkPrinterStatus(PrinterStatus status) throws Exception {
        switch (status.getSubmode()) {
            case ECR_SUBMODE_IDLE: {
                if (checkEcrMode(status.getMode())) {
                    return true;
                }
                break;
            }

            case ECR_SUBMODE_PASSIVE:
            case ECR_SUBMODE_ACTIVE: {
                checkPaper(status);
                // Flags can be ok, but status not
                throw new DeviceException(SMFP_EFPTR_PAPER_OR_COVER,
                        getErrorText(SMFP_EFPTR_PAPER_OR_COVER));
            }

            case ECR_SUBMODE_AFTER: {
                continuePrint();
                break;
            }

            case ECR_SUBMODE_REPORT:
            case ECR_SUBMODE_PRINT: {
                Time.delay(TimeToSleep);
                break;
            }

            default: {
                logger.debug("Unknown submode");
                return true;
            }
        }
        return false;
    }

    public int[] getSupportedBaudRates() throws Exception {
        return getModel().getSupportedBaudRates();
    }

    public boolean tryCancelReceipt(int password) throws Exception {
        VoidFiscalReceipt command = new VoidFiscalReceipt();
        command.setPassword(password);
        if (executeCommand(command) == 0x59) {
            return false;
        } else {
            check(command.getResultCode());
            return true;
        }
    }

    public void writeDecimalPoint(int position) throws Exception {
        WriteDecimalPoint command = new WriteDecimalPoint(sysPassword, position);
        execute(command);
    }

    private int beginFiscalDay() throws Exception {
        synchronized (port.getSyncObject()) {

            if (!capOpenFiscalDay) {
                return 0;
            }

            if (!tlvItems.isEmpty()) {
                if (fsReadStatus().getDocType().isDocClosed()) {
                    check(fsStartDayOpen());
                }
                writeTLVItems();
            }

            BeginFiscalDay command = new BeginFiscalDay();
            command.setPassword(usrPassword);
            int rc = executeCommand(command);
            capOpenFiscalDay = isCommandSupported(rc);

            if (capOpenFiscalDay) {
                check(rc);
            }
            return rc;
        }
    }

    public int hardReset() throws Exception {
        HardReset command = new HardReset();
        return executeCommand(command);
    }

    public void sysAdminCancelReceipt() throws Exception {
        logger.debug("sysAdminCancelReceipt");
        String[] passwordString = new String[1];
        // try use known passwords
        if (tryCancelReceipt(usrPassword)) {
            return;
        }
        if (tryCancelReceipt(sysPassword)) {
            return;
        }
        // reading passwords from tables
        for (int i = 1; i < 30; i++) {
            check(readTable(2, i, 1, passwordString));
            int password = Integer.parseInt(passwordString[0]);
            if (tryCancelReceipt(password)) {
                return;
            }
        }
        throw new Exception(Localizer.getString(Localizer.FailedCancelReceipt));
    }

    public int getBaudRateIndex(int value) throws Exception {
        int[] deviceBaudRates = getSupportedBaudRates();
        for (byte i = 0; i < deviceBaudRates.length; i++) {
            if (value == deviceBaudRates[i]) {
                return i;
            }
        }
        return deviceBaudRates.length - 1;
    }

    public void setBaudRate(int baudRate) throws Exception {
        port.setBaudRate(baudRate);
    }

    public boolean connectDevice(int baudRate, int deviceBaudRate,
            int deviceByteTimeout) throws Exception {
        setBaudRate(baudRate);
        LongPrinterStatus status = readLongStatus();
        writePortParams(status.getPortNumber(),
                getBaudRateIndex(deviceBaudRate), deviceByteTimeout);
        setBaudRate(baudRate);
        return true;
    }

    public void checkBaudRate(int value) throws Exception {
        int[] deviceBaudRates = getSupportedBaudRates();
        for (int i = 0; i < deviceBaudRates.length; i++) {
            if (value == deviceBaudRates[i]) {
                return;
            }
        }
        throw new Exception(Localizer.getString(Localizer.BaudrateNotSupported));
    }

    public void closePort() throws Exception {
        port.close();
    }

    public void writeTables(PrinterTables tables) throws Exception {
        logger.debug("writeTables");
        for (int i = 0; i < tables.size(); i++) {
            PrinterTable table = tables.get(i);
            PrinterFields fields = table.getFields();
            for (int j = 0; j < fields.size(); j++) {
                writeField2(fields.get(j));
            }
        }
        logger.debug("writeTables: OK");
    }

    public void writeFields(PrinterFields fields) throws Exception {
        for (int i = 0; i < fields.size(); i++) {
            writeField2(fields.get(i));
        }
    }

    public PrinterTable getTable(int tableNumber) throws Exception {
        PrinterTable table = tables.find(tableNumber);
        if (table == null) {
            ReadTableInfo command = readTableInfo(tableNumber);
            if (command.isSucceeded()) {
                table = command.getTable();
                tables.add(table);
            }
        }
        return table;
    }

    public boolean isValidField(PrinterField field)
            throws Exception {
        return getTable(field.getTable()).isValid(field);
    }

    public void readTable(PrinterTable table) throws Exception {
        for (int rowNumber = 1; rowNumber <= table.getRowCount(); rowNumber++) {
            for (int fieldNumber = 1; fieldNumber <= table.getFieldCount(); fieldNumber++) {
                PrinterField field = readField(table.getNumber(), rowNumber, fieldNumber);
                table.getFields().add(field);
            }
        }
    }

    public PrinterField readField(PrinterField field) throws Exception {
        ReadTable command = new ReadTable(sysPassword, field.getTable(),
                field.getRow(), field.getField());
        execute(command);
        field.setBytes(command.fieldValue);
        return field;
    }

    public int writeField(PrinterField field) throws Exception {
        logger.debug("writeField(" + field.getTable() + ", " + field.getRow()
                + ", " + field.getField() + ", " + field.getValue() + ")");
        PrinterCommand command = new WriteTable(sysPassword, field.getTable(),
                field.getRow(), field.getField(), field.getBytes());
        return executeCommand(command);
    }

    // Write field only if field is valid and value differs
    public void writeField2(PrinterField field) throws Exception {
        PrinterField field2 = readField(field.getCopy());
        if (!field.isEqualValue(field2)) {
            writeField(field);
        }
    }

    public PrinterTables readTables() throws Exception {
        logger.debug("readTables");
        PrinterTables tables = new PrinterTables();
        int tableNumber = SMFP_TABLE_SETUP;
        while (true) {
            ReadTableInfo command = new ReadTableInfo();
            command.setPassword(sysPassword);
            command.setTableNumber(tableNumber);
            int result = executeCommand(command);
            if (result == SMFP_EFPTR_INVALID_TABLE) {
                break;
            }
            check(result);

            PrinterTable table = command.getTable();
            tables.add(table);

            for (int fieldNumber = 1; fieldNumber <= table.getFieldCount(); fieldNumber++) {
                for (int rowNumber = 1; rowNumber <= table.getRowCount(); rowNumber++) {
                    PrinterField field = readField(tableNumber, rowNumber, fieldNumber);
                    table.getFields().add(field);
                }
            }
            tableNumber++;
        }
        logger.debug("readTables: OK");
        return tables;
    }

    public PrinterStatus readShortPrinterStatus() throws Exception {
        return readShortStatus().getPrinterStatus();
    }

    public PrinterStatus readLongPrinterStatus() throws Exception {
        return readLongStatus().getPrinterStatus();
    }

    private void loggerDebugMode(int mode, int subMode) {
        logger.debug("Mode: 0x" + Hex.toHex(mode) + ", "
                + PrinterMode.getText(mode) + ". Flags: 0x"
                + Hex.toHex(subMode));

        logger.debug("Submode: " + String.valueOf(subMode) + ", "
                + PrinterSubmode.getText(subMode));
    }

    public PrinterStatus readPrinterStatus() throws Exception {
        PrinterStatus status;
        switch (params.getStatusCommand()) {
            case SMFP_STATUS_COMMAND_DS:
                if (getModel().getCapShortStatus()) {
                    status = readShortPrinterStatus();
                } else {
                    status = readLongPrinterStatus();
                }
                break;

            case SMFP_STATUS_COMMAND_10H:
                status = readShortPrinterStatus();
                break;

            default:
                status = readLongPrinterStatus();
                break;
        }
        return status;
    }

    public DeviceMetrics getDeviceMetrics() {
        return deviceMetrics;
    }

    public int readDeviceMetrics() throws Exception {
        ReadDeviceMetrics command = new ReadDeviceMetrics();
        int result = executeCommand(command);
        if (result == 0) {
            deviceMetrics = command.getDeviceMetrics();
        }
        return result;
    }

    public PrinterModel getModel() {
        return model;
    }

    public boolean getCapLoadGraphics1() throws Exception {
        if (capLoadGraphics1 == null) {
            byte[] data = new byte[40];
            for (int i = 0; i < data.length; i++) {
                data[i] = 0;
            }
            capLoadGraphics1 = isCommandSupported(loadGraphics1(1, data));
        }
        return capLoadGraphics1;
    }

    public boolean getCapLoadGraphics2() throws Exception {
        if (capLoadGraphics2 == null) {
            byte[] data = new byte[40];
            for (int i = 0; i < data.length; i++) {
                data[i] = 0;
            }
            capLoadGraphics2 = isCommandSupported(loadGraphics2(1, data));
        }
        return capLoadGraphics2;
    }

    public boolean capGraphicsFlags() {
        return (capModelParameters() && (modelParameters.isCapGraphicsFlags()));
    }

    public boolean capModelParameters() {
        return modelParameters != null;
    }

    public boolean getCapLoadGraphics3() throws Exception {
        if (capLoadGraphics3 == null) {
            if (capModelParameters()) {
                capLoadGraphics3 = modelParameters.isGraphics512Supported();
            } else {
                byte[] data = new byte[40];
                for (int i = 0; i < data.length; i++) {
                    data[i] = 0;
                }
                capLoadGraphics3 = isCommandSupported(loadGraphics3(1, data));
            }
        }
        return capLoadGraphics3;
    }

    public boolean getCapPrintGraphics1() throws Exception {
        if (capPrintGraphics1 == null) {
            capPrintGraphics1 = isCommandSupported(printGraphics1(1, 2));
        }
        return capPrintGraphics1;
    }

    public boolean getCapPrintGraphics2() throws Exception {
        if (capPrintGraphics2 == null) {
            capPrintGraphics2 = isCommandSupported(printGraphics2(1, 2));
        }
        return capPrintGraphics2;
    }

    public boolean getCapPrintGraphics3() throws Exception {
        if (capPrintGraphics3 == null) {
            if (capModelParameters()) {
                capPrintGraphics3 = modelParameters.isGraphics512Supported();
            } else {
                capPrintGraphics3 = isCommandSupported(printGraphics3(1, 2));
            }
        }
        return capPrintGraphics3;
    }

    public boolean getCapPrintScaled() throws Exception {
        if (capPrintScaled == null) {
            capPrintScaled = isCommandSupported(printScaled(1, 2, 1, 1));
        }
        return capPrintScaled;
    }

    public boolean getCapPrintGraphicsLine() throws Exception {
        if (capPrintGraphicsLine == null) {
            byte[] data = new byte[40];
            for (int i = 0; i < data.length; i++) {
                data[i] = 0;
            }

            capPrintGraphicsLine = isCommandSupported(printGraphicLine(SMFP_STATION_REC, 1, data));
        }
        return capPrintGraphicsLine;
    }

    public boolean getCapPrintBarcode2() throws Exception {
        if (capPrintBarcode2 == null) {
            PrinterBarcode barcode = new PrinterBarcode();
            barcode.setHeight(1);
            barcode.setText("123456789012");
            barcode.setType(SmFptrConst.SMFPTR_BARCODE_EAN13);
            capPrintBarcode2 = isCommandSupported(printBarcode2(barcode));
        }
        return capPrintBarcode2;
    }

    public boolean getCapPrintBarcode3() throws Exception {
        if (capPrintBarcode3 == null) {
            byte[] data = {0x00};
            LoadBarcode3 command = new LoadBarcode3();
            command.setPassword(usrPassword);
            command.setBlockType(0);
            command.setBlockNumber(0);
            command.setBlockData(data);
            int rc = executeCommand(command);
            capPrintBarcode3 = isCommandSupported(rc);
        }
        return capPrintBarcode3;
    }

    private boolean isCapDisableDiscountTextInitialized = false;

    public boolean getCapDisableDiscountText() throws Exception {
        if (!isCapDisableDiscountTextInitialized) {
            capDisableDiscountText = readCapDisableDiscountText();
            isCapDisableDiscountTextInitialized = true;
        }

        return capDisableDiscountText;
    }

    private boolean isCapDiscountInitialized;

    public int getDiscountMode() throws Exception {
        if (!isCapDiscountInitialized) {
            initializeCapDiscount();
        }

        return discountMode;
    }

    public boolean getCapDiscount() throws Exception {
        if (!isCapDiscountInitialized) {
            initializeCapDiscount();
        }

        return capDiscount;
    }

    private void initializeCapDiscount() throws Exception {

        discountMode = 0;

        if (capFiscalStorage) {
            discountMode = 2;
            if (isDesktop() || isShtrihNano()) {
                String[] fieldValue = new String[1];
                int rc = readTable(17, 1, 3, fieldValue);
                if (succeeded(rc)) {
                    discountMode = Integer.parseInt(fieldValue[0]);
                }
            }
        }
        capDiscount = !isShtrihMobile() && discountMode == 0;
        isCapDiscountInitialized = true;
    }

    // 17,1,18,1,0,0,3,"Rus компактный заголовок","0"
    public void initialize() throws Exception {
        logger.debug("initialize()");

        fields.clear();
        tables.clear();
        capOpenReceipt = true;
        isCapDisableDiscountTextInitialized = false;
        isHeaderHeightInitialized = false;
        isFsHeaderDataInitialized = false;
        isCapDiscountInitialized = false;
        capFiscalStorage = false;

        getModel().setFonts(new PrinterFonts(this));

        if (capModelParameters()) {
            capFiscalStorage = modelParameters.getCapFiscalStorage();
        }

        if (!capFiscalStorage) {
            capFiscalStorage = readCapFiscalStorage();
        }

        capFooterFlag = capModelParameters()
                && modelParameters.isCapGraphicsFlags() && params.footerFlagEnabled;

        if (capFiscalStorage) {
            int fsTableNumber = getFsTableNumber();
            getModel().addParameter("fdoName", "", fsTableNumber, 1, 10);
            getModel().addParameter("CompressedFontEnabled", "", 1, 1, 31);
        }

        if (isShtrihMobile()) {
            capGraphics3Scale = true;
        }
        updateFirmware();
        setCurrentDateTime();
        if (isRegionalTable17Exists()) {
            String[] printModeText = new String[1];
            if (readTable(17, 1, 7, printModeText) == 0) {
                printMode = Integer.parseInt(printModeText[0]);
            }
        }
    }

    /**
     * Устройство все прочие Штрих-устройства выпускаемые как стационарные,
     * например Retail-01Ф
     */
    public boolean isDesktop() {
        return deviceMetrics.isDesktop();
    }

    /**
     * Устройство Кассовое ядро
     */
    public boolean isCashCore() {
        boolean cashCore;
        if (capModelParameters()) {
            cashCore = modelParameters.isCapCashCore();
        } else {
            cashCore = deviceMetrics.isCashCore();
        }
        return cashCore;
    }

    /**
     * Устройство Штрих-MOBILE-Ф
     */
    public boolean isShtrihMobile() {
        return deviceMetrics.isShtrihMobile();
    }

    /**
     * Устройство Штрих-NANO-Ф
     */
    public boolean isShtrihNano() {
        return deviceMetrics.isShtrihNano();
    }

    private boolean readCapDisableDiscountText() throws Exception {
        PrinterDate date1 = new PrinterDate(10, 4, 2017);
        PrinterDate date2 = readLongStatus().getFirmwareDate();
        return date2.isEqualOrOlder(date1);
    }

    private boolean readCapFSCloseReceipt() throws Exception {
        FSCloseReceipt closeReceipt = new FSCloseReceipt();
        closeReceipt.setSysPassword(sysPassword);
        int rc = executeCommand(closeReceipt);
        return isCommandSupported(rc);
    }

    private boolean readCapFiscalStorage() throws Exception {
        FSReadStatus command = new FSReadStatus();
        command.setSysPassword(sysPassword);
        return succeeded(executeCommand(command));
    }

    private boolean isCommandSupported(int rc) {
        return rc != SMFP_EFPTR_NOT_SUPPORTED;
    }

    public PrinterModel selectPrinterModel(DeviceMetrics metrics)
            throws Exception {
        PrinterModel result = models.find(metrics.getModel(),
                metrics.getProtocolVersion(), metrics.getProtocolSubVersion());
        if (result == null) {
            result = models.itemByID(SMFP_MODELID_DEFAULT);
        }
        if (result == null) {
            throw new Exception("Printer model not found");
        }
        logger.debug("Selected model: " + result.getName());
        return result;
    }

    public boolean getWrapText() {
        return wrapText;
    }

    public void setWrapText(boolean value) {
        wrapText = value;
    }

    public void checkPaper(PrinterStatus status) throws Exception {
        int resultCode;
        if (getModel().getCapRecPresent()
                && status.getPrinterFlags().isRecEmpty()) {
            resultCode = SMFP_EFPTR_NO_REC_PAPER;
            throw new DeviceException(resultCode,
                    getErrorText(resultCode));
        }
        if (getModel().getCapJrnPresent()
                && status.getPrinterFlags().isJrnEmpty()) {
            resultCode = SMFP_EFPTR_NO_JRN_PAPER;
            throw new DeviceException(resultCode,
                    getErrorText(resultCode));
        }
        if (getModel().getCapCoverSensor() && status.getPrinterFlags().isCoverOpened()) {
            resultCode = SMFP_EFPTR_COVER_OPENED;
            throw new DeviceException(resultCode,
                    getErrorText(resultCode));
        }
    }

    public int initTables() throws Exception {
        InitTables command = new InitTables();
        command.setPassword(sysPassword);
        return executeCommand(command);
    }

    private void loadGraphics(int lineNumber, byte[] data) throws Exception {
        if (getCapLoadGraphics2()) {
            check(loadGraphics2(lineNumber, data));
        } else if (getCapLoadGraphics1()) {
            check(loadGraphics1(lineNumber, data));
        }
    }

    public void loadGraphics(int lineNumber, int lineCount, byte[] data)
            throws Exception {
        loadGraphics(lineNumber, data);
    }

    public void printImage(PrinterImage image) throws Exception {
        if (image == null) {
            return;
        }
        loadImage(image, true);
        int line1 = image.getStartPos();
        int line2 = image.getEndPos();

        if (getModel().getModelID() == PrinterConst.PRINTER_MODEL_SHTRIH_M_FRK) {
            line1 = line1 + 1;
            line2 = line2 + 1;
        }
        printGraphics(line1, line2);
    }

    public void printGraphics(int line1, int line2) throws Exception {
        if (getCapPrintGraphics3()) {
            printGraphics3(line1, line2);
            return;
        }
        if (getCapPrintGraphics2()) {
            printGraphics2(line1, line2);
            return;
        }
        if (getCapPrintGraphics1()) {
            printGraphics1(line1, line2);
            return;
        }
        if (getCapPrintScaled()) {
            printScaled(line1, line2, 1, 1);
            return;
        }
        throw new Exception("Graphics commands are not supported");
    }

    public static int boolToInt(boolean value) {
        if (value) {
            return 1;
        } else {
            return 0;
        }
    }

    public void writeParameter(String paramName, boolean value) throws Exception {
        writeParameter(paramName, boolToInt(value));
    }

    public void writeParameter(String paramName, String value) throws Exception {
        logger.debug("writeParameter(" + paramName + ", "
                + String.valueOf(value) + ")");

        PrinterParameter parameter = getModel().findParameter(paramName);
        if (parameter == null) {
            return;
        }

        check(writeTable(parameter.getTableNumber(), parameter.getRowNumber(),
                parameter.getFieldNumber(), value));
    }

    public void writeParameter(String paramName, int value) throws Exception {
        logger.debug("writeParameter(" + paramName + ", "
                + String.valueOf(value) + ")");

        PrinterParameter parameter = getModel().findParameter(paramName);
        if (parameter == null) {
            logger.debug("Parameter not found, NAME=" + paramName);
            return;
        }
        int fieldValue = parameter.getFieldValue(value);
        check(writeTable(parameter.getTableNumber(), parameter.getRowNumber(),
                parameter.getFieldNumber(), String.valueOf(fieldValue)));
    }

    public String readParameter(String paramName) throws Exception {
        PrinterParameter parameter = getModel().findParameter(paramName);
        if (parameter == null) {
            logger.debug("Parameter not found, NAME=" + paramName);
            return "";
        }

        if (parameter.isValueEmpty()) {
            String[] fieldValue = new String[1];
            check(readTable(parameter.getTableNumber(), parameter.getRowNumber(),
                    parameter.getFieldNumber(), fieldValue));
            parameter.setValue(fieldValue[0]);
        }
        return parameter.getValue();
    }

    public boolean readBoolParameter(String paramName) throws Exception {
        return readParameter(paramName).equals("1");
    }

    public int readIntParameter(String paramName) throws Exception {
        int result = Integer.parseInt(readParameter(paramName));
        return result;
    }

    public void printBarcode(PrinterBarcode barcode) throws Exception {
        logger.debug("printBarcode");
        if (barcode.getHeight() <= 0) {
            throw new Exception(
                    Localizer.getString(Localizer.InvalidBarcodeHeight));
        }

        switch (barcode.getPrintType()) {
            case SmFptrConst.SMFPTR_PRINTTYPE_AUTO:
                if (getModel().getCapBarcodeSupported(barcode.getType())) {
                    printBarcodeDevice(barcode);
                } else {
                    printBarcodeDriver(barcode);
                }
                break;

            case SmFptrConst.SMFPTR_PRINTTYPE_DEVICE:
                printBarcodeDevice(barcode);
                break;

            case SmFptrConst.SMFPTR_PRINTTYPE_DRIVER:
                printBarcodeDriver(barcode);
                break;

            default:
                printBarcodeDriver(barcode);
        }

        if (getParams().waitForBarcodePrinting) {
            waitForPrinting();
            Time.delay(getParams().barcodeDelay);
        }
    }

    private void printBarcodeDevice(PrinterBarcode barcode) throws Exception {
        if (getModel().getCapPrintBarcode2()) {
            check(printBarcode2(barcode));
        } else {
            // only EAN-13 barcode
            if (barcode.getType() != SmFptrConst.SMFPTR_BARCODE_EAN13) {
                throw new Exception(
                        Localizer
                                .getString(Localizer.PrinterSupportesEAN13Only));
            }

            if (barcode.isTextAbove()) {
                printBarcodeLabel(barcode);
            }
            printBarcode(barcode.getText());
        }
    }

    public int printBarcode2(PrinterBarcode barcode) throws Exception {
        PrintBarcode2 command = new PrintBarcode2();
        command.setOperatorPassword(usrPassword);
        command.setBarcodeHeight(barcode.getHeight());
        command.setBarWidth(barcode.getBarWidth());
        command.setBarcodeHRIPosition(barcode.getTextPosition());
        command.setBarcodeHRIPitch(barcode.getTextFont());
        command.setBarcodeType(barcode.getType());
        command.setBarcodeData(barcode.getText());
        return executeCommand(command);
    }

    private void printBarcodeLabel(PrinterBarcode barcode) throws Exception {
        if (barcode.getLabel().isEmpty()) {
            return;
        }
        String data = centerLine(barcode.getLabel());
        doPrintText(SMFP_STATION_REC, data, params.font);
    }

    public void printText(String text)
            throws Exception {
        printText(SMFP_STATION_REC, text, params.font);
    }

    public void printText(int station, String text, FontNumber font)
            throws Exception {

        if (text.length() == 0) {
            text = " ";
        }
        String data = processEscCommands(text);
        if (data.length() > 0) {
            doPrintText(station, data, font);
        }
    }

    private void doPrintText(int station, String text, FontNumber font)
            throws Exception {
        if ((!params.barcodePrefix.isEmpty()) && text.startsWith(params.barcodePrefix)) {
            int prefixLength = params.barcodePrefix.length();
            String barcodeText = text.substring(prefixLength);
            PrinterBarcode barcode = new PrinterBarcode();
            barcode.setText(barcodeText);
            barcode.setLabel("");
            barcode.setType(params.barcodeType);
            barcode.setBarWidth(params.barcodeBarWidth);
            barcode.setHeight(params.barcodeHeight);
            barcode.setTextPosition(params.barcodeTextPosition);
            barcode.setTextFont(params.barcodeTextFont);
            barcode.setAspectRatio(params.barcodeAspectRatio);
            printBarcode(barcode);
        } else {
            String[] lines = splitText(text, font);
            for (int i = 0; i < lines.length; i++) {
                printLine(station, lines[i], font);
            }
        }
    }

    public String processEscCommands(String text)
            throws Exception {
        String result = text;
        if (params.escCommandsEnabled) {
            result = escPrinter.parse(text, params.stringEncoding);
            escPrinter.execute();
        }
        return result;
    }

    public String centerLine(String data) throws Exception {
        return StringUtils.centerLine(data, getMessageLength());
    }

    private void printBarcodeDriver(PrinterBarcode barcode) throws Exception {
        if (barcode.isTextAbove()) {
            printBarcodeLabel(barcode);
        }
        if (barcode.isLinear()) {
            printBarcode1D(barcode);
        } else {
            printBarcode2D(barcode);
        }

        if (getParams().waitForBarcodePrinting) {
            waitForPrinting();
        }

        if (barcode.isTextBelow()) {
            printBarcodeLabel(barcode);
        }
    }

    public int getImageFirstLine() {
        return getParams().getPrinterImages().getTotalSize() + 3;
    }

    public int getPrintWidth() throws Exception {
        return getModel().getPrintWidth();
    }

    public boolean getSwapGraphicsLine() throws Exception {
        switch (params.swapGraphicsLine) {
            case SWAP_LINE_AUTO: {
                if (getDeviceMetrics().getDeviceName().equals("ШТРИХ-М-01Ф")) {
                    return true;
                } else {
                    return getModel().getSwapGraphicsLine();
                }
            }

            case SWAP_LINE_TRUE:
                return true;

            case SWAP_LINE_FALSE:
                return false;

            default:
                return getModel().getSwapGraphicsLine();
        }
    }

    public void printBarcode1D(PrinterBarcode barcode) throws Exception {
        SmBarcodeEncoder encoder = new ZXingEncoder(
                getMaxGraphicsLineWidth(), barcode.getBarWidth(), barcode.getHeight());
        SmBarcode bc = encoder.encode(barcode);
        if (bc == null) {
            throw new Exception("Barcode type is not supported");
        }

        if (getParams().graphicsLineEnabled) {
            int width = getMaxGraphicsLineWidth();
            bc.setHScale(1);
            bc.setVScale(1);
            bc.centerBarcode(width);
            byte[] data = bc.getRow(0);
            if (getSwapGraphicsLine()) {
                data = BitUtils.swapBits(data);
            }
            printGraphicLine(SMFP_STATION_REC, barcode.getHeight(), data);
        } else {
            bc.setFirstLine(getImageFirstLine());
            bc.setHScale(1);
            bc.setVScale(1);
            bc.centerBarcode(getMaxGraphicsLineWidth());
            printSmBarcode(bc);
        }
    }

    public void printBarcode2D(PrinterBarcode barcode) throws Exception {
        if ((getCapPrintBarcode3()) && (barcode.getType() == SmFptrConst.SMFPTR_BARCODE_QR_CODE)) {
            check(printBarcode3(barcode));
        } else {
            int hScale = barcode.getBarWidth();
            int vScale = barcode.getVScale();
            int loadHScale = hScale;
            int loadVScale = vScale;
            if (getCapLoadGraphics3()) {
                if (capGraphics3Scale) {
                    loadHScale = 1;
                    loadVScale = 1;
                } else {
                    hScale = 1;
                    vScale = 1;
                }
            } else if (getCapPrintScaled()) {
                hScale = 1;
                loadVScale = 1;
            } else {
                hScale = 1;
                vScale = 1;
            }
            SmBarcodeEncoder encoder = new ZXingEncoder(
                    getMaxGraphicsWidth(), loadHScale, loadVScale);
            SmBarcode bc = encoder.encode(barcode);
            bc.setFirstLine(getImageFirstLine());
            bc.setVScale(vScale);
            bc.setHScale(hScale);
            bc.centerBarcode(getMaxGraphicsWidth());
            printSmBarcode(bc);
        }
    }

    public int loadDataBlock(int blockType, byte[] data) throws Exception {
        logger.debug("loadDataBlock");
        // Load barcode data
        byte[] block = new byte[LoadBarcode3.MAX_BLOCK_SIZE];
        ByteArrayInputStream stream = new ByteArrayInputStream(data);
        int blockNumber = 0;
        int result = 0;
        while (stream.available() > 0) {
            int len = stream.read(block);
            if (len <= 0) {
                throw new Exception("Read failed");
            }
            if (len < block.length) {
                System.arraycopy(block, 0, block, 0, len);
            }
            LoadBarcode3 command = new LoadBarcode3();
            command.setPassword(usrPassword);
            command.setBlockType(blockType);
            command.setBlockNumber(blockNumber);
            command.setBlockData(block);
            result = executeCommand(command);
            if (result != 0) {
                break;
            }
            blockNumber++;
        }
        return result;
    }

    public int printBarcode3(PrinterBarcode barcode) throws Exception {
        logger.debug("printBarcode3");

        loadDataBlock(0, barcode.getText().getBytes());
        // Print barcode
        PrintBarcode3 command = new PrintBarcode3();
        command.setPassword(usrPassword);
        int barcodeType = PrintBarcode3.QRCODE;
        if (capFooterFlag && isFooter) {
            barcodeType += 0x40;
        }
        command.setBarcodeType(barcodeType);
        command.setDataLength(barcode.getText().length());
        command.setBlockNumber(0);
        command.setInParameter1(0);
        command.setInParameter2(0);
        command.setInParameter3(barcode.getBarWidth());
        command.setInParameter4(0);
        command.setInParameter5(0);
        command.setAlignment(PrintBarcode3.ALIGN_CENTER);
        return executeCommand(command);
    }

    private void printSmBarcode(SmBarcode barcode) throws Exception {
        if (getCapLoadGraphics3()) {
            loadBarcode512(barcode);

            int flags = getPrintStation(SMFP_STATION_REC);
            PrintGraphics3 command = new PrintGraphics3();
            command.setPassword(usrPassword);
            command.setLine1(barcode.getFirstLine());
            command.setLine2(barcode.getFirstLine() + barcode.getHeight() - 1);
            command.setVscale(barcode.getVScale());
            command.setHscale(barcode.getHScale());
            command.setFlags(flags);
            execute(command);
        } else {
            for (int i = 0; i < barcode.getHeight(); i++) {
                loadGraphics(barcode.getFirstLine() + i, barcode.getRow(i));
            }
            int line1 = barcode.getFirstLine();
            int line2 = barcode.getFirstLine() + barcode.getHeight() - 1;
            if (getCapPrintScaled()) {
                printScaled(line1, line2, barcode.getVScale(), barcode.getHScale());
            } else {
                printGraphics(line1, line2);
            }
        }
    }

    public void loadBarcode512(SmBarcode barcode) throws Exception {
        int bytesPerCommand = 240;
        int lineLength = barcode.getRow(0).length;
        int rowsPerCommand = bytesPerCommand / lineLength;
        int commandCount = (barcode.getHeight() + rowsPerCommand - 1) / rowsPerCommand;
        int startLine = barcode.getFirstLine();

        int row = 0;
        for (int i = 0; i < commandCount; i++) {
            int rowCount = 0;
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            for (int j = 0; j < rowsPerCommand; j++) {
                byte[] lineData = barcode.getRow(row);

                os.write(lineData);
                row++;
                rowCount++;
                if (row >= barcode.getHeight()) {
                    break;
                }
            }
            LoadGraphics3 command = new LoadGraphics3();
            command.setPassword(usrPassword);
            command.setLineLength(lineLength);
            command.setLineNumber(startLine);
            command.setLineCount(rowCount);
            command.setBufferType(1);
            command.setData(os.toByteArray());
            execute(command);
            startLine = startLine + rowCount;
        }
    }

    public int printScaled(int line1, int line2, int vScale, int hScale)
            throws Exception {
        PrintScaled command = new PrintScaled();
        command.setPassword(usrPassword);
        command.setLine1(line1);
        command.setLine2(line2);
        command.setvScale(vScale);
        command.sethScale(hScale);
        return executeCommand(command);
    }

    public int getMessageLength() throws Exception {
        return getMessageLength(params.font);
    }

    public int getMessageLength(FontNumber font) throws Exception {
        return getModel().getTextLength(font);
    }

    public int getMaxGraphicsHeight() throws Exception {
        int height = 0;
        if (capModelParameters()) {
            height = modelParameters.getMaxGraphics512Height();
        } else {
            height = getModel().getMaxGraphicsHeight();
            if (height > getModel().getMaxGraphics512Height()) {
                height = getModel().getMaxGraphics512Height();
            }
        }
        return height - 1;
    }

    public int getMaxGraphicsWidth() throws Exception {
        int width = Math.min(getPaperWidth(), getMaxGraphicCommandWidth());
        return width;
    }

    public int getMaxGraphicCommandWidth() throws Exception {
        if (capModelParameters()) {
            return modelParameters.getGraphics512Width();
        } else if (getCapLoadGraphics3()) {
            return 512;
        } else {
            return 320;
        }
    }

    public int getPaperWidth() throws Exception {
        return getModel().getFonts().itemByNumber(FontNumber.getNormalFont()).getPaperWidth();
    }

    public int getMaxGraphicsLineWidth() throws Exception {
        if (capModelParameters()) {
            return modelParameters.getGraphicsLineWidthInDots();
        } else if (getCapLoadGraphics3()) {
            return 512;
        } else {
            return 320;
        }
    }

    public int readIntParameter(PrinterParameter parameter) throws Exception {
        String[] fieldValue = new String[1];
        check(readTable(parameter.getTableNumber(), parameter.getRowNumber(),
                parameter.getFieldNumber(), fieldValue));
        return Integer.parseInt(fieldValue[0]);
    }

    private boolean getCapParameter(String paramName) throws Exception {
        return getModel().getCapParameter(paramName);
    }

    public int getLineSpacing() throws Exception {
        if (getCapParameter(SMFP_PARAMID_LINE_SPACING)) {
            PrinterParameter parameter = getModel().getParameter(
                    SMFP_PARAMID_LINE_SPACING);
            return readIntParameter(parameter);
        } else {
            return getModel().getLineSpacing();
        }
    }

    public int getLineHeight(FontNumber font) throws Exception {
        return getModel().getFontHeight(font) + getLineSpacing();
    }

    public void checkImageSize(int firstLine, int imageWidth, int imageHeight)
            throws Exception {
        // check max image width
        int maxGraphicsWidth = getMaxGraphicsWidth();
        if (imageWidth > maxGraphicsWidth) {
            throw new Exception(
                    Localizer.getString(Localizer.InvalidImageWidth) + ", "
                    + String.valueOf(imageWidth) + " > "
                    + String.valueOf(maxGraphicsWidth));
        }
    }

    public int readLicense(String[] license) throws Exception {
        ReadLicense command = new ReadLicense(sysPassword);
        int result = executeCommand(command);
        if (result == 0) {
            license[0] = String.valueOf(command.getLicense());
        }
        return result;
    }

    public byte[] getSeparatorData(int separatorType) throws Exception {
        byte[] SEPARATOR_PATTERN_BLACK = {(byte) 0xFF};
        byte[] SEPARATOR_PATTERN_WHITE = {0x00};
        byte[] SEPARATOR_PATTERN_DOTTED_1 = {(byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, 0x00, 0x00, 0x00};
        byte[] SEPARATOR_PATTERN_DOTTED_2 = {(byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, 0x00, 0x00, 0x00, 0x00};
        byte[] pattern;

        switch (separatorType) {
            case SmFptrConst.SMFPTR_SEPARATOR_BLACK:
                pattern = SEPARATOR_PATTERN_BLACK;
                break;

            case SmFptrConst.SMFPTR_SEPARATOR_WHITE:
                pattern = SEPARATOR_PATTERN_WHITE;
                break;

            case SmFptrConst.SMFPTR_SEPARATOR_DOTTED_1:
                pattern = SEPARATOR_PATTERN_DOTTED_1;
                break;

            case SmFptrConst.SMFPTR_SEPARATOR_DOTTED_2:
                pattern = SEPARATOR_PATTERN_DOTTED_2;
                break;

            default:
                pattern = SEPARATOR_PATTERN_BLACK;
        }

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        int len = getModel().getPrintWidth() / 8;
        while (stream.size() < len) {
            stream.write(pattern);
        }
        return stream.toByteArray();
    }

    public FlexCommands getCommands() throws Exception {
        if (commands == null) {
            commands = new FlexCommands();
            FlexCommandsReader reader = new FlexCommandsReader();
            reader.load(commands);
        }
        return commands;
    }

    public ReadEJActivationReport readEJActivationReport() throws Exception {
        ReadEJActivationReport command = new ReadEJActivationReport();
        command.setPassword(sysPassword);
        executeCommand(command);
        return command;
    }

    public FMTotals readFMTotals(int mode) throws Exception {
        ReadFMTotals command = new ReadFMTotals();
        command.setPassword(sysPassword);
        command.setMode((byte) mode);
        execute(command);
        return command.getFMTotals();
    }

    public FMTotals readFSTotals() throws Exception {
        if (!capFSTotals) {
            return new FMTotals();
        }

        long[] totalizers = new long[4];
        int rc = readTotalizers(0, totalizers);
        capFSTotals = isCommandSupported(rc);
        if (succeeded(rc)) {
            long saleAmount = totalizers[0];
            long retSaleAmount = totalizers[1];
            long buyAmount = totalizers[2];
            long retBuyAmount = totalizers[3];
            FMTotals totals = new FMTotals(saleAmount, buyAmount, retSaleAmount, retBuyAmount);
            return totals;
        } else {
            return new FMTotals();
        }
    }

    public FMTotals readFPTotals(int mode) throws Exception {
        if (capFiscalStorage) {
            return readFSTotals();
        } else if (isFiscalized()) {
            return readFMTotals(mode);
        } else {
            long saleTotals = readCashRegister(244);
            FMTotals totals = new FMTotals(saleTotals, 0, 0, 0);
            return totals;
        }
    }

    public ReadEJDocumentLine readEJDocumentLine() throws Exception {
        ReadEJDocumentLine command = new ReadEJDocumentLine();
        command.setPassword(sysPassword);
        executeCommand(command);
        return command;
    }

    public String[] readEJActivationText(int maxCount) throws Exception {
        Vector lines = new Vector();
        check(cancelEJDocument());
        if (resultCode == 0) {
            ReadEJActivationReport command1 = readEJActivationReport();
            if (command1.getResultCode() == 0) {
                lines.add(command1.getEcrModel());
                for (int i = 0; i < maxCount; i++) {
                    ReadEJDocumentLine command2 = readEJDocumentLine();
                    if (command2.isFailed()) {
                        break;
                    }
                    lines.add(command2.getLine());
                }
            }
            check(cancelEJDocument());
        }
        return (String[]) (lines.toArray(new String[0]));
    }

    public PrinterFont readFont(int fontNumber) throws Exception {
        ReadFontMetrics command = new ReadFontMetrics();
        command.setPassword(sysPassword);
        command.setFont(fontNumber);
        check(executeCommand(command));
        return new PrinterFont(
                new FontNumber(fontNumber),
                command.getCharWidth(),
                command.getCharHeight(),
                command.getPaperWidth());
    }

    public void updateModels() {
        try {
            PrinterModel model = getModel();
            if (model == null) {
                throw new Exception("model == null");
            }
            PrinterModel defmodel = models.itemByID(PrinterConst.SMFP_MODELID_DEFAULT);
            if (defmodel == null) {
                throw new Exception("defmodel == null");
            }
            PrinterTables tables = readTables();
            for (int i = 0; i < tables.size(); i++) {
                PrinterTable table = tables.get(i);
                PrinterFields fields = table.getFields();
                for (int j = 0; j < fields.size(); j++) {
                    PrinterField field = fields.get(j);
                    PrinterParameter param = model.getParameters().itemByText(field.getName());
                    if (param == null) {
                        param = defmodel.getParameters().itemByText(field.getName());
                        if (param == null) {
                            param = new PrinterParameter("1", field.getName(),
                                    field.getTable(), field.getRow(), field.getField());
                            defmodel.getParameters().add(param);
                        } else {
                            param = new PrinterParameter(param.getName(), field.getName(),
                                    field.getTable(), field.getRow(), field.getField());
                            model.getParameters().add(param);
                        }
                    }
                }
            }
            XmlModelsWriter writer = new XmlModelsWriter(models);
            writer.save(SysUtils.getFilesPath() + "models2.xml");

        } catch (Exception e) {
            logger.error("updateModels ", e);
        }
    }

    public void printReceiptImage(int position) throws Exception {
        for (int i = 0; i < params.getReceiptImages().size(); i++) {
            ReceiptImage image = params.getReceiptImages().get(i);
            if (image.valid(position)) {
                PrinterImage printerImage = params.getPrinterImages().get(
                        image.getImageIndex());
                printImage(printerImage);
            }
        }
    }

    public PrinterImage getPrinterImage(int position) throws Exception {
        ReceiptImage image = params.getReceiptImages().imageByPosition(position);
        if (image != null) {
            int index = image.getImageIndex();
            if (params.getPrinterImages().validIndex(index)) {
                return params.getPrinterImages().get(index);
            }
        }
        return null;
    }

    public void loadImage(PrinterImage image, boolean addImage) throws Exception {
        logger.debug("loadImage");
        if (image.getIsLoaded()) {
            logger.debug("Image already loaded");
            return;
        }
        if (image.getFileName().equals("")) {
            logger.debug("Empty file name");
            return;
        }

        image.render(getMaxGraphicsWidth(), getMaxGraphicsHeight(), getParams().centerImage);
        image.setStartPos(params.getPrinterImages().getTotalSize() + 1);
        // check max image width
        if (image.getWidth() > getMaxGraphicsWidth()) {
            throw new Exception(
                    Localizer.getString(Localizer.InvalidImageWidth) + ", "
                    + String.valueOf(image.getWidth()) + " > "
                    + String.valueOf(getMaxGraphicsWidth()));
        }
        // write image to device
        if (getCapLoadGraphics3()) {
            loadImage3(image);
        } else {
            for (int i = 0; i < image.getHeight(); i++) {
                loadGraphics(image.getStartPos() + i + 1, image.getHeight(), image.lines[i]);
            }
        }
        image.setIsLoaded(true);

        if (addImage) {
            params.getPrinterImages().add(image);
        }
    }

    public int loadRawGraphics(byte[][] data) throws Exception {
        int startPos = params.getPrinterImages().getTotalSize() + 1;

        if (getCapLoadGraphics3()) {
            loadGraphics3(startPos, data);
        } else if (getCapLoadGraphics2()) {
            loadGraphics2(startPos, data);
        } else if (getCapLoadGraphics1()) {
            loadGraphics1(startPos, data);
        }

        return startPos;
    }

    public void loadGraphics1(int startPos, byte[][] data) throws Exception {
        for (int i = 0; i < data.length; i++) {
            check(loadGraphics1(startPos + i, data[i]));
        }
    }

    public void loadGraphics2(int startPos, byte[][] data) throws Exception {
        for (int i = 0; i < data.length; i++) {
            check(loadGraphics2(startPos + i, data[i]));
        }
    }

    public void loadGraphics3(int startPos, byte[][] data) throws Exception {
        int bytesPerCommand = 240;
        int lineLength = data[0].length;
        int rowsPerCommand = bytesPerCommand / lineLength;
        int commandCount = (data.length + rowsPerCommand - 1) / rowsPerCommand;
        int startLine = startPos;

        int row = 0;
        for (int i = 0; i < commandCount; i++) {
            int rowCount = 0;
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            for (int j = 0; j < rowsPerCommand; j++) {
                byte[] lineData = data[row];

                os.write(lineData);
                row++;
                rowCount++;
                if (row >= data.length) {
                    break;
                }
            }
            LoadGraphics3 command = new LoadGraphics3();
            command.setPassword(usrPassword);
            command.setLineLength(lineLength);
            command.setLineNumber(startLine);
            command.setLineCount(rowCount);
            command.setBufferType(1);
            command.setData(os.toByteArray());
            execute(command);
            startLine = startLine + rowCount;
        }
    }

    public void loadImage3(PrinterImage image) throws Exception {
        int bytesPerCommand = 240;
        int lineLength = image.lines[0].length;
        int rowsPerCommand = bytesPerCommand / lineLength;
        int commandCount = (image.getHeight() + rowsPerCommand - 1) / rowsPerCommand;
        int startLine = image.getStartPos();

        int row = 0;
        for (int i = 0; i < commandCount; i++) {
            int rowCount = 0;
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            for (int j = 0; j < rowsPerCommand; j++) {
                byte[] lineData = image.lines[row];

                os.write(lineData);
                row++;
                rowCount++;
                if (row >= image.getHeight()) {
                    break;
                }
            }
            LoadGraphics3 command = new LoadGraphics3();
            command.setPassword(usrPassword);
            command.setLineLength(lineLength);
            command.setLineNumber(startLine);
            command.setLineCount(rowCount);
            command.setBufferType(1);
            command.setData(os.toByteArray());
            execute(command);
            startLine = startLine + rowCount;
        }
    }

    public boolean getCapCutter() {
        return (capCutter && (params.cutMode == SmFptrConst.SMFPTR_CUT_MODE_AUTO));
    }

    public void cutPaper() throws Exception {
        if (getCapCutter()) {
            if (params.cutPaperDelay != 0) {
                Time.delay(params.cutPaperDelay);
            }
            int rc = cutPaper(params.cutType);
            capCutter = isCommandSupported(rc);
        }
    }

    public FSReadStatus fsReadStatus() throws Exception {
        FSReadStatus command = new FSReadStatus();
        command.setSysPassword(sysPassword);
        execute(command);
        return command;
    }

    public FSReadDayParameters fsReadDayParameters() throws Exception {
        FSReadDayParameters command = new FSReadDayParameters();
        command.setSysPassword(sysPassword);
        execute(command);
        return command;
    }

    public FSReadSerial fsReadSerial() throws Exception {
        FSReadSerial command = new FSReadSerial();
        command.setSysPassword(sysPassword);
        execute(command);
        return command;
    }

    @Override
    public String getFullSerial() {
        return serial;
    }

    @Override
    public String readFullSerial() throws Exception {
        if (serial.isEmpty()) {
            if (getDeviceMetrics().isElves()) {
                serial = getLongStatus().getSerial();
            } else {
                if (capFiscalStorage) {
                    serial = readTable(getFsTableNumber(), 1, 1).trim();
                } else {
                    serial = getLongStatus().getSerial();
                }
            }
        }
        return serial;
    }

    @Override
    public String readRnm() throws Exception {
        if (getDeviceMetrics().isElves()) {
            return readTable(11, 1, 2).trim();
        }
        return readTable(getFsTableNumber(), 1, 3).trim();
    }

    public FSReadExpDate fsReadExpDate() throws Exception {
        FSReadExpDate command = new FSReadExpDate();
        command.setSysPassword(sysPassword);
        execute(command);
        return command;
    }

    public FSReadVersion fsReadVersion() throws Exception {
        FSReadVersion command = new FSReadVersion();
        command.setSysPassword(sysPassword);
        execute(command);
        return command;
    }

    public FSResetState fsResetState(int code) throws Exception {
        FSResetState command = new FSResetState();
        command.setSysPassword(sysPassword);
        command.setCode(code);
        execute(command);
        return command;
    }

    public FSCancelDoc fsCancelDoc() throws Exception {
        FSCancelDoc command = new FSCancelDoc();
        command.setSysPassword(sysPassword);
        execute(command);
        return command;
    }

    public FSReadFiscalization fsReadFiscalization() throws Exception {
        FSReadFiscalization command = new FSReadFiscalization();
        command.setSysPassword(sysPassword);
        execute(command);
        return command;
    }

    public FSFindDocument fsFindDocument(long docNumber) throws Exception {
        FSFindDocument command = new FSFindDocument(sysPassword, docNumber);
        execute(command);
        return command;
    }

    public FSOpenDay fsOpenDay() throws Exception {
        FSOpenDay command = new FSOpenDay();
        command.setSysPassword(sysPassword);
        execute(command);
        return command;
    }

    public boolean isFSDocumentOpened() throws Exception {
        return (getCapFiscalStorage()
                && (fsReadStatus().getDocType().isDocOpen()));
    }

    public void processTLVBeforeReceipt(byte[] tlv) throws Exception {
        if (params.userExtendedTagPrintMode != SmFptrConst.USER_EXTENDED_TAG_PRINT_MODE_DRIVER) {
            return;
        }

        TLVReader reader = new TLVReader();
        TLVItems items = reader.read(tlv);
        processTLVBeforeReceipt2(items);
    }

    public void processTLVBeforeReceipt2(TLVItems items) throws Exception {
        for (int i = items.size() - 1; i >= 0; i--) {
            TLVItem item = items.get(i);
            int tagId = item.getId();
            if (tagId == 1085) {
                String text = item.getText().replace("\r\n", "  ");
                writeTable(17, 1, 13, text);
            }
            processTLVBeforeReceipt2(item.getItems());
        }
    }

    public void fsWriteTLV(byte[] tlv) throws Exception {
        tlvItems.add(tlv);
    }

    public byte[] filterTLV(byte[] tlv) throws Exception {
        TLVReader reader = new TLVReader();
        TLVItems src = reader.read(tlv);
        TLVItems dst = new TLVItems();

        filterTLV(src, dst, getFDVersion());

        TLVWriter writer = new TLVWriter();
        writer.add(dst);
        return writer.getBytes();
    }

    public void filterTLV(TLVItems src, TLVItems dst, int ffd) throws Exception {
        if (params.userExtendedTagPrintMode
                == SmFptrConst.USER_EXTENDED_TAG_PRINT_MODE_DRIVER) {
            filterTLVItemsDriver(src);
        } else {
            filterTLVItemsPrinter(src);
        }
        src.removeEmptySTLV();
        if (params.processTag1256) {
            TLVFilter.filter(src, dst, ffd);
        } else {
            dst.add(src);
        }
    }

    public void filterTLVItemsDriver(TLVItems items) throws Exception {
        for (int i = items.size() - 1; i >= 0; i--) {
            TLVItem item = items.get(i);
            if (item.getId() == 1085) {
                printText(item.getText());
                items.remove(item);
            }
            if (item.getId() == 1086) {
                printText(item.getText());
                String text = item.getText().replace("\r\n", "  ");
                fsWriteTLVData(15000, text);
                items.remove(item);
            }
            filterTLVItemsDriver(item.getItems());
        }
    }

    public void filterTLVItemsPrinter(TLVItems items) throws Exception {
        for (int i = items.size() - 1; i >= 0; i--) {
            TLVItem item = items.get(i);
            int tagId = item.getTag().getId();
            if ((tagId == 1085) || (tagId == 1086)) {
                String text = item.getText().replace("\r\n", "  ");
                item.setText(text);
            }
            filterTLVItemsPrinter(item.getItems());
        }
    }

    public int fsWriteOperationTLV(byte[] tlv) throws Exception {
        FSWriteOperationTLV command = new FSWriteOperationTLV();
        command.setSysPassword(sysPassword);
        command.setTlv(tlv);
        return executeCommand(command);
    }

    public FSReadBufferStatus fsReadBufferStatus() throws Exception {
        FSReadBufferStatus command = new FSReadBufferStatus(sysPassword);
        execute(command);
        return command;
    }

    public FSReadCommStatus fsReadCommStatus() throws Exception {
        FSReadCommStatus command = new FSReadCommStatus();
        command.setSysPassword(sysPassword);
        execute(command);
        return command;
    }

    public String getErrorText(int code) throws Exception {
        if (capErrorTextByCode) {
            ReadErrorTextByCode command = readErrorText(code);
            if (command.isSucceeded()) {
                return String.valueOf(code) + ", " + command.getErrorDescription();
            }
        }
        return getDriverErrorText(code);
    }

    public String getDriverErrorText(int code) throws Exception {
        String key = "PrinterError";
        if ((code >= 0x00) && (code <= 0xFF)) {
            key += Hex.toHex((byte) code);
        } else {
            key += Hex.toHex((short) code);
        }
        if (code != 0) {
            if ((capFiscalStorage) && ((code < 0x20) || ((code >= 0xA0) && (code <= 0xB2)))) {
                key = "FSPrinterError" + Hex.toHex((byte) code);
            }
        }
        String result = Localizer.getString(key);
        if (result.equals(key)) {
            result = Localizer.UnknownPrinterError;
        }
        return String.valueOf(code) + ", " + result;
    }

    private ReadErrorTextByCode readErrorText(int code) throws Exception {
        ReadErrorTextByCode command = new ReadErrorTextByCode(code);
        executeCommand(command);
        capErrorTextByCode = command.isSucceeded();
        return command;
    }

    private ReadLastErrorText readLastErrorText() throws Exception {
        ReadLastErrorText command = new ReadLastErrorText();
        executeCommand(command);
        capLastErrorText = command.isSucceeded();
        return command;
    }

    public void openFiscalDay() throws Exception {
        logger.debug("openFiscalDay");
        if (!capOpenFiscalDay) {
            return;
        }
        PrinterStatus status = waitForPrinting();
        if (status.getPrinterMode().isDayClosed()) {
            beginFiscalDay();

            try {
                waitForPrinting();
            } catch (Exception e) {
                logger.error("openFiscalDay wait for printing failed", e);
            }
        }
    }

    public byte[] getTLVData(int tagId, String tagValue) throws Exception {
        TLVWriter writer = new TLVWriter();
        switch (tagId) {
            case 15001:
            case 15002: {
                writer.add(tagId, Integer.valueOf(tagValue), 4);
                break;
            }
            default:
                writer.addTag(tagId, tagValue);
        }
        return writer.getBytes();
    }

    public void fsWriteTag(int tagId, String tagValue) throws Exception {
        fsWriteTLV(getTLVData(tagId, tagValue));
    }

    public LongPrinterStatus getLongStatus() throws Exception {
        if (longStatus == null) {
            longStatus = readLongStatus();
        }
        return longStatus;
    }

    public boolean isFiscalized() throws Exception {
        return getCapFiscalStorage() || getLongStatus().isFiscalized();
    }

    public int readDayNumber() throws Exception {
        if (isFiscalized()) {
            return readLongStatus().getDayNumber();
        } else {
            return readOperationRegister(159);
        }
    }

    public int readDocNumber() throws Exception {
        LongPrinterStatus status = readLongStatus();
        int documentNumber = status.getDocumentNumber();
        if (getCapFiscalStorage() && status.getPrinterMode().isDayClosed()) {
            documentNumber += 1;
        }
        return documentNumber;
    }

    public void writeCasierName(String name) throws Exception {
        int operator = readPrinterStatus().getOperator();
        writeTable(SMFP_TABLE_CASHIER, operator, 2, name);

    }

    public void writeAdminName(String name) throws Exception {
        ReadShortStatus command = new ReadShortStatus(sysPassword);
        execute(command);
        int operator = command.getStatus().getOperatorNumber();
        writeTable(SMFP_TABLE_CASHIER, operator, 2, name);
    }

    public void disablePrint() throws Exception {
        // !!! write table and by name
        writeTable(17, 1, 7, "2");
    }

    public void disablePrintOnce() throws Exception {
        // !!! write table and by name
        writeTable(17, 1, 7, "1");
    }

    public void enablePrint() throws Exception {
        // !!! write table and by name
        writeTable(17, 1, 7, "0");
    }

    public int fsReceiptDiscount(FSReceiptDiscount command) throws Exception {
        return executeCommand(command);
    }

    public String getDepartmentName(int number) throws Exception {
        return readTable(PrinterConst.SMFP_TABLE_DEPARTMENT, number, 1);
    }

    public String getTaxName(int number) throws Exception {
        return readTable2(PrinterConst.SMFP_TABLE_TAX, number, 2);
    }

    public int getTaxRate(int number) throws Exception {
        Integer taxRate = taxRates.get(number);
        if (taxRate == null) {
            taxRate = Integer.parseInt(readTable2(PrinterConst.SMFP_TABLE_TAX, number, 1));
            taxRates.put(number, taxRate);
        }
        return taxRate;
    }

    public long getTaxAmount(int tax, long amount) throws Exception {
        double taxRate = getTaxRate(tax) / 10000.0;
        return Math.round(amount * taxRate / (1 + taxRate));
    }

    public int printDocHeader(String title, int number) throws Exception {
        PrintDocHeader command = new PrintDocHeader();
        command.setPassword(usrPassword);
        command.setTitle(getTextBytes(title, 30));
        command.setNumber(number);
        return executeCommand(command);
    }

    public Vector<String> fsReadDocumentTLVAsText(int docNumber) throws Exception {
        logger.debug("fsReadDocumentTLVAsText");
        byte[] ba = fsReadDocumentTLV(docNumber).getTLV();
        TLVReader reader = new TLVReader();
        TLVTextWriter textWriter = new TLVTextWriter(reader.read(ba));
        Vector<String> lines = new Vector<String>();
        textWriter.getDocumentText(lines);
        for (int i = 0; i < lines.size(); i++) {
            logger.debug(lines.get(i));
        }
        return lines;
    }

    public DocumentTLV fsReadDocumentTLV(int docNumber) throws Exception {
        synchronized (getSyncObject()) {
            FSReadDocument readDocument = fsRequestDocumentTLV(docNumber);
            byte[] docData = fsReadDocumentTLVToEnd();
            return new DocumentTLV(docNumber, readDocument.getDocType(), docData);
        }
    }

    public byte[] fsReadDocumentTLVToEnd() throws Exception {
        FSReadDocumentBlock command = new FSReadDocumentBlock(sysPassword);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try {
            while (true) {
                executeCommand(command);
                if (command.getResultCode() == 8) {
                    break;
                }
                check(command.getResultCode());
                if (command.getData().length == 0) {
                    break;
                }
                stream.write(command.getData());
            }
            return stream.toByteArray();
        } finally {
            stream.close();
        }
    }

    public FSReadDocument fsRequestDocumentTLV(int documentNumber) throws Exception {
        FSReadDocument readDocument = new FSReadDocument(sysPassword, documentNumber);
        execute(readDocument);
        return readDocument;
    }

    public byte[] fsReadDocumentTLVBlock() throws Exception {
        FSReadDocumentBlock command = new FSReadDocumentBlock(sysPassword);
        execute(command);
        return command.getData();
    }

    public void printLines(String line1, String line2, FontNumber font) throws Exception {
        String text = StringUtils.alignLines(line1, line2, getMessageLength(font));
        printText(SMFP_STATION_REC, text, font);
    }

    public void printLines(String line1, String line2) throws Exception {
        printLines(line1, line2, params.font);
    }

    public void printLines2(String line1, String line2) throws Exception {
        if ((line1.length() + line2.length()) > getMessageLength()) {
            printText(line1);
            printText(line2);
        } else {
            String text = StringUtils.alignLines(line1, line2, getMessageLength());
            printText(text);
        }
    }

    public void printItems(List<PrintItem> items) throws Exception {
        for (int i = 0; i < items.size(); i++) {
            PrintItem item = items.get(i);
            try {
                item.print(this);
            } catch (Exception e) {
                logger.error("printItems", e);
            }
        }
        items.clear();
    }

    private boolean isFsHeaderDataInitialized = false;

    public void printFSHeader() throws Exception {
        if (getCapFiscalStorage() && getParams().fsHeaderEnabled) {
            if (!isFsHeaderDataInitialized) {
                int fsTableNumber = getFsTableNumber();

                String[] fieldValue = new String[1];

                int rc = readTable(fsTableNumber, 1, 7, fieldValue);
                if (succeeded(rc)) {
                    fsUser = fieldValue[0];
                }
                rc = readTable(fsTableNumber, 1, 9, fieldValue);
                if (succeeded(rc)) {
                    fsAddress = fieldValue[0];
                }

                isFsHeaderDataInitialized = true;
            }

            printText(fsUser);
            printText(fsAddress);
        }
    }

    public void checkDiscountMode(int mode) throws Exception {
        if (getDiscountMode() != mode) {
            throw new Exception("Incorrect fiscal printer discount mode");
        }
    }

    public void printReceiptHeader(String docName) throws Exception {
        if (getCapFiscalStorage()) {
            // 1
            LongPrinterStatus status = readLongStatus();
            String line1 = "ККТ " + readFullSerial();
            String line2 = status.getDate().toStringShort() + " "
                    + status.getTime().toString2();
            printLines2(line1, line2);
            // 2
            line1 = readTable(2, status.getOperatorNumber(), 2);
            line2 = String.format("#%04d", status.getDocumentNumber() + 1);
            printLines2(line1, line2);
            // 3
            line1 = docName;
            line2 = String.format("ИНН %010d", status.getFiscalID());
            printLines(line1, line2);
            // 4
            line1 = "РН ККТ " + readRnm().trim();
            line2 = "ФН " + fsReadSerial().getSerial();
            printLines2(line1, line2);
            // 5
            line1 = "Сайт ФНС:";
            line2 = readTable(getFsTableNumber(), 1, 13).trim();
            printLines2(line1, line2);
            waitForPrinting();
        } else {
            printDocHeader("Нефискальный документ", params.nonFiscalDocNumber);
            waitForPrinting();
        }
    }

    private static String[] docNames = {
        "ПРОДАЖА", "ПОКУПКА", "ВОЗВРАТ ПРОДАЖИ", "ВОЗВРАТ ПОКУПКИ"
    };

    private static String[] fsDocNames = {
        "ПРИХОД", "РАСХОД", "ВОЗВРАТ ПРИХОДА", "ВОЗВРАТ РАСХОДА"
    };

    public String getReceiptName(int receiptType) {
        String receiptName = "";
        boolean isCorrection = (receiptType & 0xF0) == 0x80;
        receiptType = receiptType & 0x0F;

        if ((receiptType < 0) || (receiptType > 3)) {
            return receiptName;
        }

        if (getCapFiscalStorage()) {
            receiptName = "КАССОВЫЙ ЧЕК";
            if (isCorrection) {
                receiptName = "ЧЕК КОРРЕКЦИИ";
            }
            receiptName = receiptName + "/" + fsDocNames[receiptType];
        } else {
            receiptName = docNames[receiptType];
        }
        return receiptName;
    }

    public FSReadBlock fsReadBlock(int offset, int size) throws Exception {
        FSReadBlock command = new FSReadBlock(sysPassword, offset, size);
        execute(command);
        return command;
    }

    public FSStartWriteBlock fsStartWriteBlock(int size) throws Exception {
        FSStartWriteBlock command = new FSStartWriteBlock();
        command.setSysPassword(sysPassword);
        command.setSize(size);
        execute(command);
        return command;
    }

    public FSWriteBlock fsWriteBlock(int offset, byte[] data) throws Exception {
        FSWriteBlock command = new FSWriteBlock();
        command.setSysPassword(sysPassword);
        command.setOffset(offset);
        command.setData(data);
        execute(command);
        return command;
    }

    /*
     ШТРИХ-МОБАЙЛ-Ф
     // Таблица 10, Служебная
     // Номер таблицы,Ряд,Поле,Размер поля,Тип поля,Мин. значение, Макс.значение, Название,Значение
     10,1,1,1,0,1,1,'Поддержка eod','1'

     ШТРИХ-НАНО-Ф
     // Таблица 21, Сетевые интерфейсы
     // Номер таблицы,Ряд,Поле,Размер поля,Тип поля,Мин. значение, Макс.значение, Название,Значение
     21,1,2,1,0,0,1,'Режим обмена с офд','0'
     */
    public boolean capFDOSupport() {
        return capModelParameters() && modelParameters.isCapEoD() && (!getDeviceMetrics().isElves());
    }

    public byte[] fsReadBlockData() throws Exception {
        synchronized (getSyncObject()) {
            byte[] result = new byte[0];
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            FSReadBufferStatus status = fsReadBufferStatus();

            if (status.getDataSize() == 0) {
                return result;
            }
            if (status.getBlockSize() == 0) {
                return result;
            }

            int count = (status.getDataSize() + status.getBlockSize() - 1)
                    / status.getBlockSize();
            for (int i = 0; i < count; i++) {
                int offset = i * status.getBlockSize();
                int dataSize = status.getDataSize() - offset;
                if (dataSize > status.getBlockSize()) {
                    dataSize = status.getBlockSize();
                }
                FSReadBlock block = fsReadBlock(offset, dataSize);

                stream.write(block.getData());
            }
            return stream.toByteArray();
        }
    }

    public void fsWriteBlockData(byte[] data) throws Exception {
        synchronized (getSyncObject()) {
            FSStartWriteBlock command = fsStartWriteBlock(data.length);

            int blockSize = command.getBlockSize();
            if (blockSize == 0) {
                throw new Exception("blockSize = 0");
            }
            int count = (data.length + blockSize - 1) / blockSize;
            for (int i = 0; i < count; i++) {
                int offset = i * blockSize;
                int dataSize = data.length - i * blockSize;
                if (dataSize > blockSize) {
                    dataSize = blockSize;
                }
                byte[] blockData = new byte[dataSize];
                System.arraycopy(data, offset, blockData, 0, dataSize);
                fsWriteBlock(offset, blockData);
            }
        }
    }

    public List<FSTicket> fsReadTickets(int[] fsDocumentNumbers) throws Exception {
        synchronized (getSyncObject()) {
            Vector<FSTicket> tickets = new Vector<FSTicket>();
            for (int i = 0; i < fsDocumentNumbers.length; i++) {
                FSReadDocTicket command = new FSReadDocTicket();
                command.setSysPassword(getSysPassword());
                command.setDocNumber(fsDocumentNumbers[i]);
                int rc = executeCommand(command);
                byte[] ticket = command.getTicket();
                tickets.add(new FSTicket(rc, ticket));
                if (rc != 0) {
                    break;
                }
            }
            return tickets;
        }
    }

    public List<FSTicket> fsReadTickets(int firstFSDocumentNumber,
            int documentCount) throws Exception {
        synchronized (getSyncObject()) {
            Vector<FSTicket> tickets = new Vector<FSTicket>();
            FSReadStatus status = fsReadStatus();
            long lastFSDocumentNumber = status.getDocNumber();
            if ((documentCount > 0) && (firstFSDocumentNumber + documentCount < lastFSDocumentNumber)) {
                lastFSDocumentNumber = firstFSDocumentNumber + documentCount;
            }

            for (int i = firstFSDocumentNumber; i <= lastFSDocumentNumber; i++) {
                FSReadDocTicket command = new FSReadDocTicket();
                command.setSysPassword(getSysPassword());
                command.setDocNumber(i);
                int rc = executeCommand(command);
                byte[] ticket = command.getTicket();
                tickets.add(new FSTicket(rc, ticket));
                if (rc != 0) {
                    break;
                }
            }
            return tickets;
        }
    }

    public boolean isDiscountInHeader() throws Exception {
        // TODO: cache?

        boolean isCompactHeader = false;
        if (getCapFiscalStorage() && (isDesktop() || isShtrihNano())) {
            String[] fieldValue = new String[1];
            int rc = readTable(17, 1, 18, fieldValue);
            if (succeeded(rc)) {
                isCompactHeader = fieldValue[0].equalsIgnoreCase("3");
            }
        }

        return isCompactHeader;
    }

    public int reboot() throws Exception {
        ServiceCommand command = new ServiceCommand();
        command.setFunctionCode(ServiceCommand.CODE_REBOOT);
        command.setIntData(0);
        command.hasAnswer = false; // do not wait answer
        int rc = executeCommand(command);
        if (succeeded(rc)) {
            port.close();
        }
        return rc;
    }

    public void interrupt() {
        interrupted = true;
    }

    public void cancelWait() {

        stopFlag = true;

    }

    public void rebootAndWait() throws Exception {
        logger.debug("rebootAndWait");

        stopFlag = false;
        check(reboot());
        Time.delay(10 * 1000);
        for (int i = 0; i < 10; i++) {
            try {
                if (stopFlag) {
                    return;
                }

                connect();
                break;
            } catch (Exception e) {
                Time.delay(5 * 1000);
            }
        }
        connect();
        logger.debug("rebootAndWait: OK");
    }

    public int rebootToDFU() throws Exception {
        ServiceCommand command = new ServiceCommand();
        command.setFunctionCode(ServiceCommand.CODE_DFU_REBOOT);
        command.setIntData(0);
        int rc = executeCommand(command);
        if (succeeded(rc)) {
            port.close();
        }
        return rc;
    }

    /*

     // Таблица 15, Параметры офд
     // Номер таблицы,Ряд,Поле,Размер поля,Тип поля,Мин. значение, Макс.значение, Название,Значение
     15,1,1,16,1,0,65535,'Ip адрес сервера офд','91.107.67.212'
     15,1,2,2,0,0,65535,'Tcp порт сервера офд','7779'
     15,1,3,2,0,0,65535,'Тайм-аут опроса офд, 1 с','5'

     */
    public FDOParameters readFDOParameters() throws Exception {
        final int tableNumber = getFDOTableNumber();
        String host = readTable(tableNumber, 1, 1);
        int portValue = Integer.valueOf(readTable(tableNumber, 1, 2));
        int timeoutInSec = Integer.valueOf(readTable(tableNumber, 1, 3));
        return new FDOParameters(host, portValue, timeoutInSec);
    }

    public FDOParameters getFDOParameters() throws Exception {
        if (fdoParameters == null) {
            fdoParameters = readFDOParameters();
        }
        return fdoParameters;
    }

    private int getFDOTableNumber() throws Exception {
        int result = 19;
        if (isShtrihMobile()) {
            result = 15;
        }
        if (capModelParameters()) {
            if (modelParameters.capFDOTableNumber()) {
                result = modelParameters.getFDOTableNumber();
            }
        }
        return result;
    }

    private int getFsTableNumber() throws Exception {
        int result = 18;
        if (isShtrihMobile()) {
            result = 14;
        }
        if (capModelParameters()) {
            if (modelParameters.capFsTableNumber()) {
                result = modelParameters.getFsTableNumber();
            }
        }
        return result;
    }

    private int readTableIntValueFromStringOrInt(int tableNumber, int portField) throws Exception {
        String port = readTable(tableNumber, 1, portField);

        FieldInfo fieldInfo = readFieldInfo(tableNumber, portField);
        int portValue;
        if (fieldInfo.isInteger()) {
            byte[] bytes = fieldInfo.fieldToBytes(port, charsetName);
            portValue = FieldInfo.bytesToInt(bytes, bytes.length);
        } else {
            portValue = Integer.valueOf(port);
        }
        return portValue;
    }

    public FieldInfo readFieldInfo(int table, int field) throws Exception {
        ReadFieldInfo command = new ReadFieldInfo();
        command.setPassword(sysPassword);
        command.setTable(table);
        command.setField(field);
        int result = executeCommand(command);
        check(result);

        return command.getFieldInfo();
    }

    public void printSeparator(int separatorType, int height) throws Exception {
        printGraphicLine(SMFP_STATION_REC, height, getSeparatorData(separatorType));
        waitForPrinting();
    }

    private boolean isHeaderHeightInitialized = false;

    public int getHeaderHeight() throws Exception {
        if (isHeaderHeightInitialized) {
            return headerHeigth;
        }

        headerHeigth = getModel().getHeaderHeight();

        if (capFiscalStorage) {
            String[] fieldValue = new String[1];
            int rc = readTable(10, 1, 1, fieldValue);
            if (succeeded(rc)) {
                headerHeigth = Integer.valueOf(fieldValue[0]);
            }
        }

        isHeaderHeightInitialized = true;
        return headerHeigth;
    }

    public int fsPrintRecItem(FSReceiptItem item) throws Exception {
        item.setText(getText(item.getText()));
        FSPrintRecItem command = new FSPrintRecItem();
        command.setPassword(usrPassword);
        command.setItem(item);
        return executeCommand(command);
    }

    public int getTaxBits(int tax) {
        int result = 0;
        if ((tax >= 1) && (tax <= 8)) {
            result = (int) BitUtils.setBit(tax - 1);
        }
        return result;
    }

    public void setIsFooter(boolean value) {
        isFooter = value;
    }

    public boolean isCapFooterFlag() {
        return capFooterFlag;
    }

    public FSDocument fsFindLastDocument(int docType) throws Exception {
        FSReadStatus fsStatus = fsReadStatus();
        long docNumber = fsStatus.getDocNumber();
        while (true) {
            if (docNumber == 0) {
                break;
            }
            FSFindDocument fs = fsFindDocument(docNumber);
            if (fs.getDocument().getDocType() == docType) {
                return fs.getDocument();
            }
            docNumber--;
        }
        return null;
    }

    public boolean getCapSetVatTable() {
        return !getCapFiscalStorage();
    }

    public void clearTableText() throws Exception {
        if (params.isTableTextCleared) {
            return;
        }

        ReadTableInfo tableStructure = readTableInfo(PrinterConst.SMFP_TABLE_TEXT);
        int rowCount = tableStructure.getRowCount();
        for (int row = 1; row <= rowCount; row++) {
            int result = writeTable(PrinterConst.SMFP_TABLE_TEXT, row, 1, "");
            if (failed(result)) {
                break;
            }
        }
        params.isTableTextCleared = true;
    }

    public void updateTableText() throws Exception {
        String[] fieldValue = new String[1];
        ReadTableInfo tableStructure = readTableInfo(PrinterConst.SMFP_TABLE_TEXT);
        int rowCount = tableStructure.getRowCount();
        for (int row = 1; row <= rowCount; row++) {
            int result = readTable(PrinterConst.SMFP_TABLE_TEXT, row, 1, fieldValue);
            if (failed(result)) {
                break;
            }
            if (fieldValue[0].length() == 0) {
                result = writeTable(PrinterConst.SMFP_TABLE_TEXT, row,
                        1, " ");
                if (failed(result)) {
                    break;
                }
            }
        }
    }

    public int readLoaderVersion() throws Exception {
        int result = 0;
        ServiceCommand command = new ServiceCommand();
        command.setFunctionCode(ServiceCommand.CODE_GET_BL_VER);
        command.setIntData(0);
        executeCommand(command);
        if (command.isSucceeded()) {
            CommandInputStream stream = new CommandInputStream(charsetName);
            stream.setData(command.getAnswer());
            result = stream.readInt();
        }
        return result;
    }

    // upd_app_20180220.bin
    // upd_ldr_145.bin
    public int compareFirmwareVersion(String firmwareFileName) throws Exception {
        int index1 = firmwareFileName.lastIndexOf("_");
        if (index1 == -1) {
            throw new JposException(JPOS_E_EXTENDED, JposConst.JPOS_EFIRMWARE_BAD_FILE);
        }
        int index2 = firmwareFileName.lastIndexOf(".");
        if (index2 == -1) {
            throw new JposException(JPOS_E_EXTENDED, JposConst.JPOS_EFIRMWARE_BAD_FILE);
        }
        String suffix = firmwareFileName.substring(index1 + 1, index2);
        int loaderVersion = 0;
        try {
            loaderVersion = Integer.parseInt(suffix);
        } catch (Exception e) {
        }
        PrinterDate firmwareDate = null;
        try {
            int year = StringUtils.stringToInt(suffix, 0, 4, "year");
            int month = StringUtils.stringToInt(suffix, 4, 2, "month");
            int day = StringUtils.stringToInt(suffix, 6, 2, "day");
            firmwareDate = new PrinterDate(day, month, year);
        } catch (Exception e) {
        }
        if ((loaderVersion == 0) && (firmwareDate == null)) {
            throw new JposException(JPOS_E_EXTENDED, JposConst.JPOS_EFIRMWARE_BAD_FILE);
        }
        if (firmwareDate != null) {
            PrinterDate deviceFirmwareDate = readLongStatus().getFirmwareDate();
            return firmwareDate.compare(deviceFirmwareDate);
        }
        if (loaderVersion != 0) {
            int deviceLoaderVersion = readLoaderVersion();
            return MathUtils.compare(loaderVersion, deviceLoaderVersion);
        }
        return 0;
    }

    public boolean getCapUpdateFirmware() throws Exception {
        return (params.capUpdateFirmware && capFiscalStorage && (!isShtrihMobile()));
    }

    private int firmwareUpdateDelay = 10000;

    public void updateFirmware() throws Exception {
        logger.debug("updateFirmware()");
        try {
            if ((!capFiscalStorage) || isShtrihMobile() || isCashCore()) {
                logger.debug("Firmware update is not supported");
                return;
            }
            if (!params.capUpdateFirmware) {
                logger.debug("Firmware update disabled in driver parameters");
                return;
            }

            File dir = new File(params.firmwarePath);
            if (!dir.exists()) {
                logger.debug("Firmware directory does not exists");
                return;
            }
            File[] files = dir.listFiles();
            if (files == null) {
                logger.debug("dir.listFiles() returns null");
                return;
            }
            logger.debug("files.length = " + files.length);
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                logger.debug("Found file '" + file.getAbsolutePath() + "')");
                if (FileUtils.getExtention(file.getName()).equals(".bin")) {
                    if (file.exists()) {
                        if (compareFirmwareVersion(file.getName()) == 1) {
                            updateFirmware(file.getName());
                        }
                    }
                }
            }
            logger.debug("updateFirmware(): OK");
        } catch (Exception e) {
            logger.error("updateFirmware ", e);
        }
    }

    public void updateFirmware(String firmwareFileName) throws Exception {
        logger.debug("updateFirmware(" + firmwareFileName + ")");
        if (!getCapUpdateFirmware()) {
            throw new Exception("Firmware update not supported");
        }

        LongPrinterStatus status = readLongStatus();
        if (status.getPrinterMode().isDayOpened()) {
            throw new Exception("Day end required");
        }

        PrinterTables tables = null;
        synchronized (port.getSyncObject()) {
            if (isSDCardPresent()) {
                tables = readTables();
                updateFirmwareSDCard(firmwareFileName);
                reboot();
                waitFirmwareUpdate();
                connect();
                writeTables(tables);
                return;
            }

            if (FirmwareUpdater.capDFUUpdate() && (status.getPortNumber() == PrinterConst.SMFP_IF_PC_USB)
                    || ((status.getPortNumber() == PrinterConst.SMFP_IF_PC_TCP_SERVER) && isRNDISEnabled())) {
                tables = readTables();
                updateFirmwareDFU(firmwareFileName);
                waitFirmwareUpdate();
                connect();
                writeTables(tables);
                return;
            }

            if ((params.getPortType() == SmFptrConst.PORT_TYPE_SERIAL)
                    && FirmwareUpdater.capXModemUpdate() && (status.getPortNumber() == PrinterConst.SMFP_IF_PC_RS232)) {
                tables = readTables();
                updateFirmwareXModem(firmwareFileName);
                waitFirmwareUpdate();
                searchByBaudRates(params.portName, 4800);
                writeTables(tables);
                return;
            }
            throw new Exception("Cannot update firmware: no valid interface");
        }
    }

    private boolean isRNDISEnabled() throws Exception {
        if (isShtrihMobile()) {
            return false;
        }
        return readTable(21, 1, 9).equalsIgnoreCase("1");
    }

    private void waitFirmwareUpdate() throws Exception {
        logger.debug("waitFirmwareUpdate");
        Time.delay(firmwareUpdateDelay);
        logger.debug("waitFirmwareUpdate: OK");
    }

    public boolean isSDCardPresent() throws Exception {
        if (isShtrihMobile()) {
            return false;
        } else {
            return Integer.parseInt(readTable(14, 1, 1)) == 0;
        }
    }

    private static final int LoaderFileSize = 28672;
    private static final int FirmwareFileSize = 491520;

    public void updateFirmwareSDCard(String firmwareFileName) throws Exception {
        int fileType = 0;
        int blockNumber = 0;
        byte[] block = new byte[128];
        FileInputStream stream = new FileInputStream(new File(firmwareFileName));
        // 0- loader, 1 – firmware
        if (stream.available() >= FirmwareFileSize) {
            fileType = 1;
        }

        while (stream.available() > 0) {
            stream.read(block, 0, 128);
            writeFirmwareBlockToSDCard(fileType, blockNumber, block);
            blockNumber++;
        }
    }

    public void writeFirmwareBlockToSDCard(int fileType, int blockNumber, byte[] block) throws Exception {
        WriteSDCardBlock command = new WriteSDCardBlock();
        command.setPassword(sysPassword);
        command.setBlockNumber(blockNumber);
        command.setFileType(fileType);
        command.setBlock(block);
        execute(command);
    }

    public void updateFirmwareXModem(String firmwareFileName) throws Exception {
        logger.debug("updateFirmwareXModem");
        reboot();

        FirmwareUpdater.updateFirmwareXModem(params.portName, firmwareFileName);
        logger.debug("updateFirmwareXModem: OK");
    }

    public void updateFirmwareDFU(String firmwareFileName) throws Exception {
        rebootToDFU();
        FirmwareUpdater.updateFirmwareDFU(firmwareFileName);
    }

    private static final int MaxStateCount = 3;

    private LongPrinterStatus checkEcrMode() throws Exception {
        synchronized (port.getSyncObject()) {
            for (;;) {
                ReadLongStatus command = new ReadLongStatus();
                command.setPassword(getUsrPassword());
                int rc = executeCommand(command);

                if ((rc == 0x74) || (rc == 0x78) || (rc == 0x79)) {
                    deviceReset();
                    continue;
                } else {
                    check(rc);
                }

                LongPrinterStatus status = command.getStatus();
                if (checkPrinterStatus(status.getPrinterStatus())) {
                    return status;
                }
            }
        }
    }

    public boolean checkEcrMode(int mode) throws Exception {
        switch (mode) {
            case MODE_DUMPMODE:
                try {
                    endDump();
                } catch (DeviceException ignored) {
                    // При чтении докмента из ФН десктопные ФР переходят в режим 1, при этом
                    // прервать этот режим старым методом нельзя только дочитать документ до
                    // конца
                    fsReadDocumentTLVToEnd();
                }
                break;

            case MODE_LOCKED:
                throw new Exception(
                        Localizer.getString(Localizer.LockedTaxPassword));

            case MODE_WAITDATE:
                PrinterDate date = readLongStatus().getDate();
                confirmDate(date);
                break;

            case MODE_POINTPOS:
                writeDecimalPoint(SMFP_POINT_POSITION_2);
                break;

            case MODE_TECH:
                deviceReset();
                break;

            case MODE_TEST:
                stopTest();
                break;

            case MODE_FULLREPORT:
            case MODE_EJREPORT:
            case MODE_SLPPRINT:
                Time.delay(TimeToSleep);
                break;

            default:
                return true;
        }
        return false;
    }

    private void deviceReset() throws Exception {

        PrinterDate date = new PrinterDate();
        PrinterTime time = new PrinterTime();

        hardReset();
        check(writeDate(date));
        check(confirmDate(date));
        check(writeTime(time));
    }

    private boolean searchByBaudRates(String portName, int startBaudRate)
            throws Exception {
        if (connectDevice(portName, startBaudRate)) {
            return true;
        }
        if (port.isSearchByBaudRateEnabled()) {
            int[] deviceBaudRates = {4800, 9600, 19200, 38400, 57600, 115200, 2400};
            for (int i = 0; i < deviceBaudRates.length; i++) {
                int baudRate = deviceBaudRates[i];
                if (baudRate != startBaudRate) {
                    if (connectDevice(portName, baudRate)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    // try to connect to device
    private boolean connectDevice(String searchPortName, int searchBaudRate) throws Exception {
        logger.debug("connectDevice(" + searchPortName + ", " + searchBaudRate + ")");
        try {
            port.setPortName(searchPortName);
            port.setBaudRate(searchBaudRate);
            port.open(0);
            LongPrinterStatus status = connect();

            // always set port parameters to update byte
            // receive timeout in fiscal printer
            int baudRateIndex = getBaudRateIndex(params.getBaudRate());
            writePortParams(status.getPortNumber(), baudRateIndex, params.getDeviceByteTimeout());
            params.setBaudRate(getModel().getSupportedBaudRates()[baudRateIndex]);

            // if baudrate changed - reopen port
            if (searchBaudRate != params.getBaudRate()) {
                port.setPortName(searchPortName);
                port.setBaudRate(params.getBaudRate());
                port.open(0);
            }
            return true;
        } catch (Exception e) {
            logger.error("connectDevice ", e);
            return false;
        }
    }

    // search device on ports and baudrates
    private void searchSerialDevice() throws Exception {
        if (params.searchByPortEnabled) {
            String[] ports = port.getPortNames();

            logger.debug("Searching " + ports.length + " port names");

            for (String portName : ports) {
                if (params.searchByBaudRateEnabled) {
                    if (searchByBaudRates(portName, params.getBaudRate())) {
                        return;
                    }
                } else if (connectDevice(portName, params.getBaudRate())) {
                    return;
                }
            }
        }

        if (params.searchByBaudRateEnabled) {
            if (searchByBaudRates(params.portName, params.getBaudRate())) {
                return;
            }
        } else if (connectDevice(params.portName, params.getBaudRate())) {
            return;
        }
    }

    public LongPrinterStatus searchDevice() throws Exception {
        logger.debug("searchDevice");
        synchronized (port.getSyncObject()) {
            if (params.getPortType() == SmFptrConst.PORT_TYPE_SERIAL) {
                if (params.searchByBaudRateEnabled || params.searchByPortEnabled) {
                    searchSerialDevice();
                    return readLongStatus();
                } else {
                    port.setPortName(params.portName);
                    port.setBaudRate(params.getBaudRate());
                    port.open(params.portOpenTimeout);
                    LongPrinterStatus status = connect();
                    // always set port parameters to update byte
                    // receive timeout in fiscal printer
                    //int baudRateIndex = getBaudRateIndex(params.getBaudRate());
                    //writePortParams(status.getPortNumber(), baudRateIndex, params.getDeviceByteTimeout());
                    return status;
                }
            } else {
                port.setPortName(params.portName);
                port.setBaudRate(params.getBaudRate());
                port.open(params.portOpenTimeout);
                return connect();
            }
        }
    }

    /*
   
     Второй параметр отвечает за тип запрашиваемых сумм
     •   FE F4 00 00 00 00 - возвращает 4 8-ми байтных числа (приход, возврат прихода, расход, возврат расхода). Это НС без деталировки по типам оплаты
     •   FE F4 01 00 00 00 - возвращает 16 8-ми байтных числа (приход). Это НС с деталировкой по 16-ти типам оплаты
     •   FE F4 02 00 00 00 - возвращает 16 8-ми байтных числа (возврат прихода). Это НС с деталировкой по 16-ти типам оплаты
     •   FE F4 03 00 00 00 - возвращает 16 8-ми байтных числа (расход). Это НС с деталировкой по 16-ти типам оплаты
     •   FE F4 04 00 00 00 - возвращает 16 8-ми байтных числа (возврат расхода). Это НС с деталировкой по 16-ти типам оплаты
     •   FE F4 05 00 00 00 - возвращает 4 8-ми байтных числа (коррекция прихода, коррекция возврата прихода, коррекция расхода, коррекция возврата расхода)
    
     */
    public int readTotalizers(int recType, long[] totalizers) throws Exception {
        logger.debug("readPaymentTotalizers");
        if ((recType < 0) || (recType > 5)) {
            return 0x33;
        }

        ServiceCommand command = new ServiceCommand();
        command.setFunctionCode(ServiceCommand.CODE_GLOBALSUMM_GET);
        command.setIntData(recType);
        executeCommand(command);
        if (command.isSucceeded()) {
            CommandInputStream stream = new CommandInputStream(charsetName);
            stream.setData(command.getAnswer());
            int count = stream.size() / 8;
            if (count < totalizers.length) {
                // Workaround, fiscal printer does not check parameter
                // Invalid parameter works as parameter 0.
                return 0x33; // Invalid command parameters
            }

            for (int i = 0; i < totalizers.length; i++) {
                totalizers[i] = stream.readLong(8);
            }
        }
        return command.getResultCode();
    }

    public long[] readTotalizers(int recType) throws Exception {
        int count = 0;
        switch (recType) {
            case 0:
            case 5:
                count = 4;
                break;
            case 1:
            case 2:
            case 3:
            case 4:
                count = 16;
                break;
            default:
                check(0x33);
        }
        long[] totalizers = new long[count];
        check(readTotalizers(recType, totalizers));
        return totalizers;
    }

    public String[] readEJDocument(int documentNumber) throws Exception {
        if (documentNumber <= 0) {
            ReadEJStatus status = readEJStatus();
            check(status.getResultCode());
            documentNumber = (int) status.getStatus().getDocMACNumber();
        }
        ReadEJDocument readEJDocument = new ReadEJDocument(sysPassword, documentNumber);
        execute(readEJDocument);
        Vector<String> lines = new Vector<String>();
        lines.add(readEJDocument.getLine());
        int rc = 0;
        while (rc == 0) {
            ReadEJDocumentLine command = new ReadEJDocumentLine();
            command.setPassword(sysPassword);
            executeCommand(command);
            if (command.isFailed()) {
                break;
            }
            lines.add(command.getLine());
        }
        return (String[]) lines.toArray(new String[0]);
    }

    public int getHeaderTableRow() throws Exception {
        int row = getModel().getHeaderTableRow();
        if (readBoolParameter("CompressedFontEnabled")) {
            row = row - (getNumHeaderLines() - getModel().getNumHeaderLines());
        }
        return row;
    }

    public int getNumHeaderLines() throws Exception {
        int numHeaderLines = getModel().getNumHeaderLines();
        if (readBoolParameter("CompressedFontEnabled")) {
            numHeaderLines = (int) (numHeaderLines * 1.5);
        }
        return numHeaderLines;
    }

    public int getNumTrailerLines() throws Exception {
        return getModel().getNumTrailerLines();
    }

    public int printDocEnd() throws Exception {
        PrintDocEnd command = new PrintDocEnd();
        command.setPassword(usrPassword);
        return executeCommand(command);
    }

    public int fsStartFiscalization(int reportType) throws Exception {
        FSStartFiscalization command = new FSStartFiscalization(getSysPassword(), reportType);
        return executeCommand(command);
    }

    public int fsStartCorrectionReceipt() throws Exception {
        FSStartCorrectionReceipt command = new FSStartCorrectionReceipt();
        command.setSysPassword(getSysPassword());
        return executeCommand(command);
    }

    public int fsStartCalcReport() throws Exception {
        FSStartCalcReport command = new FSStartCalcReport(getSysPassword());
        return executeCommand(command);
    }

    public int fsStartFiscalClose() throws Exception {
        FSStartFiscalClose command = new FSStartFiscalClose();
        command.setSysPassword(getSysPassword());
        return executeCommand(command);
    }

    public int fsPrintCorrectionReceipt(FSPrintCorrectionReceipt command)
            throws Exception {
        if (!tlvItems.isEmpty()) {
            if (fsReadStatus().getDocType().isDocClosed()) {
                check(fsStartCorrectionReceipt());
            }
            writeTLVItems();
        }
        return executeCommand(command);
    }

    public int fsPrintCorrectionReceipt2(FSPrintCorrectionReceipt2 command) throws Exception {
        if (!tlvItems.isEmpty()) {
            if (fsReadStatus().getDocType().isDocClosed()) {
                check(fsStartCorrectionReceipt());
            }
            writeTLVItems();
        }
        return executeCommand(command);
    }

    public boolean getCapOpenFiscalDay() {
        return capOpenFiscalDay;
    }

    public boolean isDayClosed() throws Exception {
        PrinterStatus status = waitForPrinting();
        return status.getPrinterMode().isDayClosed();
    }

    public int fsCheckMC(FSCheckMC command) throws Exception {
        command.password = sysPassword;
        return executeCommand(command);
    }

    public int sendItemCode(ItemCode itemCode) throws Exception {
        if (itemCode == null) {
            return 0;
        }

        if ((getFDVersion() == PrinterConst.FS_FORMAT_FFD_1_2)
                || (params.markingType == SmFptrConst.MARKING_TYPE_PRINTER)) {
            FSBindMC command = new FSBindMC();
            command.data = itemCode.getData();
            command.volumeAccounting = itemCode.isVolumeAccounting();
            return fsBindMC(command);
        } else {
            byte[] tagValue = barcodeTo1162Tag(itemCode.getData());
            return fsWriteOperationTLV(tagValue);
        }
    }

    public static boolean checkEANChecksum(String barcode) throws Exception {
        String s = barcode.substring(0, barcode.length() - 1);
        int crc = ZXingEncoder.getStandardUPCEANChecksum(s);
        return (crc == Integer.parseInt(barcode.substring(barcode.length() - 1)));
    }

    public static byte[] barcodeTo1162Value(byte[] data) throws Exception {
        String barcode = new String(data);
        byte[] barcodeData = data;
        int barcodeLength = barcode.length();
        int barcodeType = SmFptrConst.KTN_UNKNOWN;

        GS1Barcode gs1Barcode = GS1Barcode.parse(barcode);
        if ((gs1Barcode.isValid()) && gs1Barcode.hasItem("01") && gs1Barcode.hasItem("21")) {
            barcodeType = SmFptrConst.KTN_DM;

            String gtin = gs1Barcode.getItem("01");
            if (gtin.length() > 24) {
                gtin = gtin.substring(0, 24);
            }
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            byte[] ba = ArrayUtils.longToBytes(Long.parseLong(gtin), 6);
            os.write(ba);

            String serial = gs1Barcode.getItem("21");
            os.write(serial.getBytes());

            if (gs1Barcode.hasItem("8005")) {
                String unitPrice = gs1Barcode.getItem("8005");
                os.write(unitPrice.getBytes());
            }
            barcodeData = os.toByteArray();
        } else {
            switch (barcodeLength) {
                case 8:
                    if (barcode.matches("\\d+") && checkEANChecksum(barcode)) {
                        barcodeType = SmFptrConst.KTN_EAN8;
                        barcodeData = ArrayUtils.longToBytes(Long.parseLong(barcode), 6);
                    }
                    break;

                case 10:
                    if (barcode.matches("\\d+")) {
                        barcodeType = SmFptrConst.KTN_FUEL;
                        barcodeData = ArrayUtils.longToBytes(Long.parseLong(barcode), 6);
                    }
                    break;

                case 13:
                    if (barcode.matches("\\d+") && checkEANChecksum(barcode)) {
                        barcodeType = SmFptrConst.KTN_EAN13;
                        barcodeData = ArrayUtils.longToBytes(Long.parseLong(barcode), 6);
                    }
                    break;

                case 14:
                    if (barcode.matches("\\d+")) {
                        barcodeType = SmFptrConst.KTN_ITF14;
                        barcodeData = ArrayUtils.longToBytes(Long.parseLong(barcode), 6);
                    }
                    break;

                case 21:
                    // Проверка на формат "СС-ЦЦЦЦЦЦ-ССССССССССС"
                    if (barcode.matches("\\w{2}-\\d{6}-\\w{11}")) {
                        barcodeType = SmFptrConst.KTN_RF;
                    }
                    break;

                case 29:
                    barcodeType = SmFptrConst.KTN_DM;
                    String gtin = barcode.substring(0, 14);
                    ByteArrayOutputStream os = new ByteArrayOutputStream();
                    byte[] ba = ArrayUtils.longToBytes(Long.parseLong(gtin), 6);
                    os.write(ba);
                    String serial = barcode.substring(14, 25) + "  ";
                    os.write(serial.getBytes());
                    barcodeData = os.toByteArray();
                    break;

                case 68:
                    barcodeType = SmFptrConst.KTN_EGAIS2;
                    barcodeData = barcode.substring(8, 31).getBytes();
                    break;

                case 150:
                    barcodeType = SmFptrConst.KTN_EGAIS3;
                    barcodeData = barcode.substring(0, 14).getBytes();
                    break;

            }
        }
        if (barcodeType == SmFptrConst.KTN_UNKNOWN) {
            if (barcode.length() > 30) {
                barcodeData = barcode.substring(0, 29).getBytes();
            }
        }

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        os.write((barcodeType >>> 8) & 0xFF);
        os.write((barcodeType >>> 0) & 0xFF);
        os.write(barcodeData);
        return os.toByteArray();
    }

    public byte[] barcodeTo1162Tag(byte[] data) throws Exception {
        TLVWriter writer = new TLVWriter();
        writer.add(1162, barcodeTo1162Value(data));
        return writer.getBytes();
    }

    public int fsBindMC(FSBindMC command) throws Exception {
        command.password = usrPassword;
        return executeCommand(command);
    }

    public int fsAcceptMC(FSAcceptMC command) throws Exception {
        command.setPassword(usrPassword);
        return executeCommand(command);
    }

    public int fsReadKMServerStatus(FSReadMCNotificationStatus command) throws Exception {
        command.password = sysPassword;
        return executeCommand(command);
    }

    public int fsWriteTLVData(int tagId, String tagValue) throws Exception {
        byte[] data = getTLVData(tagId, tagValue);
        int rc = loadDataBlock(1, data);
        if (failed(rc)) {
            return rc;
        }

        FSWriteTLVBuffer command = new FSWriteTLVBuffer();
        return fsWriteTLVBuffer(command);
    }

    public boolean getCapOperationTagsFirst() {
        return deviceMetrics.isCashCore() || deviceMetrics.isShtrihMobile();
    }

    public long getLastDocNum() {
        return lastDocNum;
    }

    public long getLastDocMac() {
        return lastDocMAC;
    }

    public PrinterDate getLastDocDate() {
        return lastDocDate;
    }

    public PrinterTime getLastDocTime() {
        return lastDocTime;
    }

    public long getLastDocTotal() {
        return lastDocTotal;
    }

    private String formatStrings(String line1, String line2) throws Exception {
        int len;
        String S = "";
        len = getMessageLength() - line2.length();

        for (int i = 0; i < len; i++) {
            if (i < line1.length()) {
                S = S + line1.charAt(i);
            } else {
                S = S + " ";
            }
        }
        return S + line2;
    }

    public int getFDVersion() throws Exception {
        if (fdVersion == null) {
            try {
                if (isCashCore()) {
                    PrinterTable printerTable = getTable(10);
                    if (printerTable.getFieldCount() >= 38) {
                        fdVersion = Integer.parseInt(readTable(10, 1, 38));
                    } else {
                        fdVersion = Integer.parseInt(readTable(10, 1, 29));
                    }
                } else {
                    fdVersion = Integer.parseInt(readTable(17, 1, 17));
                }
            } catch (Exception e) {
                fdVersion = PrinterConst.FS_FORMAT_FFD_1_2;
                logger.error("getFDVersion", e);
            }
        }
        return fdVersion;
    }

    public void checkFDVersion(int version) throws Exception {
        if (getFDVersion() < version) {
            throw new Exception("Not supported in FD version");
        }
    }

    public int startReadMCNotifications(FSStartReadMCNotifications command) throws Exception {
        command.password = sysPassword;
        return executeCommand(command);
    }

    public int readMCNotification(FSReadMCNotification command) throws Exception {
        command.password = sysPassword;
        return executeCommand(command);
    }

    public MCNotifications readNotifications() throws Exception {
        MCNotifications items = new MCNotifications();
        FSStartReadMCNotifications startCommand = new FSStartReadMCNotifications();
        check(startReadMCNotifications(startCommand));
        int count = startCommand.count;
        for (int i = 0; i < count; i++) {
            if (!readNotification(items)) {
                break;
            }
        }
        return items;
    }

    public boolean readNotification(MCNotifications items) throws Exception {
        int number = 0;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        for (;;) {
            FSReadMCNotification command = new FSReadMCNotification();
            readMCNotification(command);

            if (command.getResultCode() == 8) {
                break;
            }
            check(command.getResultCode());
            number = command.number;
            stream.write(command.blockData);
            if (stream.size() >= command.size) {
                break;
            }
        }
        if (stream.size() == 0) {
            return false;
        }

        MCNotification item = new MCNotification(
                number, stream.toByteArray());
        items.put(item);
        return true;
    }

    public void confirmNotifications(MCNotifications items) throws Exception {
        FSConfirmMCNotification command = new FSConfirmMCNotification();
        int count = items.size();
        for (int i = 0; i < count; i++) {
            MCNotification item = items.get(i);
            command.password = sysPassword;
            command.number = item.getNumber();
            command.crc = item.getCrc();
            execute(command);
        }
    }

    public int fsSyncRegisters(FSSyncRegisters command) throws Exception {
        command.password = sysPassword;
        return executeCommand(command);
    }

    public int fsReadMemorySize(FSReadMemorySize command) throws Exception {
        command.password = sysPassword;
        return executeCommand(command);
    }

    public int fsWriteTLVBuffer(FSWriteTLVBuffer command) throws Exception {
        command.setSysPassword(sysPassword);
        return executeCommand(command);
    }

    public int fsReadRandomData(FSReadRandomData command) throws Exception {
        command.password = sysPassword;
        return executeCommand(command);
    }

    public int fsAuthorize(FSAuthorize command) throws Exception {
        command.password = sysPassword;
        return executeCommand(command);
    }

    public int fsReadMCStatus(FSReadMCStatus command) throws Exception {
        command.setPassword(sysPassword);
        return executeCommand(command);
    }

    public int mcClearBuffer() throws Exception {
        FSAcceptMC command = new FSAcceptMC();
        command.setAction(FSAcceptMC.ActionClearBuffer);
        return fsAcceptMC(command);
    }

    public int getCommandTimeout(int code) {
        Integer timeout = getParams().commandTimeouts.get(code);
        if (timeout != null) {
            return timeout;
        }
        return PrinterCommand.getDefaultTimeout(code);
    }

    public void setCommandTimeout(int code, int timeout) {
        getParams().commandTimeouts.put(code, timeout);
    }

    public int getPrintMode() {
        return printMode;
    }

    public void setPrintMode(int value) {
        this.printMode = value;
    }

    public synchronized void sendFDODocuments() throws Exception {
        if (!capFDOSupport()) {
            logger.debug("FDO commands not supported, capFDOSupport=false");
            return;
        }

        checkFDOConnection();

        while (true) {
            byte[] data = fsReadBlockData();
            if (data.length == 0) {
                return;
            }

            // P-protocol version 0x0102 -> 0x0120
            if ((data.length >= 30) && (data[6] == 0x01)
                    && (data[28] == 0) && (data[29] == 0)) {
                if (data[7] == 0x01) {
                    data[7] = 0x10;
                }
                if (data[7] == 0x02) {
                    data[7] = 0x20;
                }
            }

            byte[] answer = sendFDOData(data);
            if (answer.length == 0) {
                return;
            }
            fsWriteBlockData(answer);
        }
    }

    public void checkFDOConnection() throws Exception {
        try {
            logger.debug("testOFDConnection");
            FDOParameters parameters = getFDOParameters();
            Socket socket = new Socket();
            socket.setTcpNoDelay(true);
            socket.setSoTimeout(parameters.getTimeoutInMSec());
            socket.connect(new InetSocketAddress(parameters.getHost(), parameters.getPort()));
            socket.close();
            logger.debug("testOFDConnection: OK");
        } catch (Exception e) {
            throw new DeviceException(PrinterConst.SMFPTR_E_FDO_CONNECTION,
                    e.getMessage());
        }
    }

    private byte[] sendFDOData(byte[] data) throws Exception {
        try {
            FDOParameters parameters = getFDOParameters();
            logger.debug(String.format("FDO %s:%d, connection timeout %d ms",
                    parameters.getHost(), parameters.getPort(),
                    parameters.getTimeoutInMSec()));

            Socket socket = new Socket();
            socket.setTcpNoDelay(true);
            socket.setSoTimeout(parameters.getTimeoutInMSec());
            socket.connect(new InetSocketAddress(parameters.getHost(), parameters.getPort()));
            OutputStream os = socket.getOutputStream();
            os.write(data);
            InputStream in = socket.getInputStream();

            int headerSize = 30;
            byte[] header = new byte[headerSize];

            readInputStream(in, header, 0, headerSize);

            int size = ((header[25] << 8)) | (header[24] & 0xFF);

            byte[] answer = new byte[headerSize + size];
            System.arraycopy(header, 0, answer, 0, headerSize);
            if (size > 0) {
                readInputStream(in, answer, headerSize, size);
            }
            socket.close();
            return answer;
        } catch (Exception e) {
            throw new DeviceException(PrinterConst.SMFPTR_E_FDO_CONNECTION,
                    e.getMessage());
        }
    }

    private void readInputStream(InputStream in, byte[] buffer, int offset, int count) throws IOException {
        int readCount = 0;
        while (readCount < count) {
            int newBytes = in.read(buffer, offset + readCount, count - readCount);

            if (newBytes < 0) {
                throw new IOException("Connection reset by FDO");
            }

            readCount += newBytes;
        }
    }

    public byte[] getTextBytes(String text, int minLen) throws Exception {
        text = StringUtils.rtrim(text);
        byte[] data = text.getBytes(charsetName);
        byte[] result = data;
        if (params.fillTextToMinLength) {
            int len = Math.max(minLen, text.length());
            result = new byte[len];
            Arrays.fill(result, (byte) 0);
            for (int i = 0; i < data.length; i++) {
                result[i] = data[i];
            }
        }
        return result;
    }

    public String getText(String text) {
        text = StringUtils.rtrim(text);
        if (params.fillTextToMinLength) {
            text = StringUtils.appendRight(text, PrinterConst.MIN_TEXT_LENGTH);
        }
        return text;
    }

    public int getOperatorNumber() {
        return operatorNumber;
    }

    public boolean isVVASupported() {
        if (vvaSupported == null) {
            vvaSupported = readVVASupported();
        }
        return vvaSupported;
    }

    public boolean readVVASupported() {

        boolean rc = false;
        try {
            if (isCashCore()) {
                CashCoreVersion minVersion = new CashCoreVersion("1.16.43098");
                CashCoreVersion version = new CashCoreVersion(readTable(3, 1, 25));
                rc = version.compare(minVersion) >= 0;
            } else {
                //  31.05.2023 версия 003 - added VVA flag
                PrinterDate PrinterDate = new PrinterDate(31, 05, 23);
                LongPrinterStatus status = getLongStatus();
                rc = status.getFirmwareDate().compare(PrinterDate) >= 0;
            }
        } catch (Exception e) {
            logger.error("Failed read if VVA supported", e);
        }
        return rc;
    }
}
