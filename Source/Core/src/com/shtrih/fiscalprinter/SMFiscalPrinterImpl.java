/*
 * SmPrinterDevice.java
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

import com.shtrih.barcode.*;
import com.shtrih.ej.EJDate;
import com.shtrih.fiscalprinter.command.*;
import com.shtrih.fiscalprinter.model.PrinterModel;
import com.shtrih.fiscalprinter.model.PrinterModels;
import com.shtrih.fiscalprinter.model.XmlModelsWriter;
import com.shtrih.fiscalprinter.port.PrinterPort;
import com.shtrih.fiscalprinter.table.PrinterField;
import com.shtrih.fiscalprinter.table.PrinterFields;
import com.shtrih.fiscalprinter.table.PrinterTable;
import com.shtrih.fiscalprinter.table.PrinterTables;
import com.shtrih.jpos.fiscalprinter.*;
import com.shtrih.printer.ncr7167.NCR7167Printer;
import com.shtrih.util.*;
import jpos.JposConst;

import java.io.ByteArrayOutputStream;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class SMFiscalPrinterImpl implements SMFiscalPrinter, PrinterConst {

    enum Boolean {

        NOTDEFINED, TRUE, FALSE
    }

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
    public PrinterTable table = new PrinterTable(0, "", 0, 0);
    public FieldInfo fieldInfo = new FieldInfo(0, 0, 0, 0, 0, 0, "");
    public static CompositeLogger logger = CompositeLogger.getLogger(SMFiscalPrinterImpl.class);
    private final List<IPrinterEvents> events = new ArrayList<IPrinterEvents>();
    private final PrinterPort port;
    private final FptrParameters params;
    private final PrinterImages printerImages = new PrinterImages();
    private final ReceiptImages receiptImages = new ReceiptImages();
    private int resultCode = 0;
    private NCR7167Printer escPrinter = new NCR7167Printer(null);
    private final PrinterFields fields = new PrinterFields();
    private final FiscalPrinterImpl service;
    private boolean capDiscount = true;
    private boolean capDisableDiscountText = false;
    private Boolean capLoadGraphics1 = Boolean.NOTDEFINED;
    private Boolean capLoadGraphics2 = Boolean.NOTDEFINED;
    private Boolean capLoadGraphics3 = Boolean.NOTDEFINED;
    private boolean capGraphics3Scale = false;
    private Boolean capPrintGraphics1 = Boolean.NOTDEFINED;
    private Boolean capPrintGraphics2 = Boolean.NOTDEFINED;
    private Boolean capPrintGraphics3 = Boolean.NOTDEFINED;
    private Boolean capPrintScaled = Boolean.NOTDEFINED;
    private Boolean capPrintGraphicsLine = Boolean.NOTDEFINED;
    private Boolean capPrintBarcode2 = Boolean.NOTDEFINED;
    private Boolean capPrintBarcode3 = Boolean.NOTDEFINED;
    private boolean capFSPrintItem = true;
    private boolean capCutPaper = true;
    private String fsUser = "";
    private String fsAddress = "";

    private boolean capOpenFiscalDay = true;
    private boolean capFiscalStorage = false;
    private int discountMode = PrinterConst.SMFP_DM_NOT_CHANGE_SUBTOTAL_SMALLDSC;
    private boolean saveCommands = false;
    private Vector receiptCommands = new Vector();
    private boolean capOpenReceipt = true;
    private boolean capFSCloseReceipt = false;
    private boolean capFSTotals = true;
    private boolean capFooterFlag = false;
    private boolean subtotalInHeader = false;
    private boolean discountInHeader = false;
    private int headerHeigth = 0;
    private boolean isFooter = false;
    private PrinterModelParameters modelParameters = null;
    private String serial  = "";

    public SMFiscalPrinterImpl(PrinterPort port, PrinterProtocol device,
            FptrParameters params, FiscalPrinterImpl service) {
        this.port = port;
        this.device = device;
        this.params = params;
        this.service = service;

        models.load();
        try {
            model = models.itemByID(SMFP_MODELID_DEFAULT);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
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

    public PrinterImages getPrinterImages() {
        return printerImages;
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

    public void deviceExecute(PrinterCommand command) throws Exception {
        synchronized (port.getSyncObject()) {
            beforeCommand(command);
            try {
                device.send(command);

                if (command.getResultCode() != 0) {
                    String text = getErrorText(command.getResultCode());
                    logger.error(text + ", " + command.getParametersText(commands));
                }
                afterCommand(command);
            } catch (Exception e) {
                service.setPowerState(JposConst.JPOS_PS_OFF_OFFLINE);
                throw e;
            }
        }
    }

    public void connect() throws Exception {
        synchronized (port.getSyncObject()) {
            device.connect();
            check(readDeviceMetrics());
            model = selectPrinterModel(getDeviceMetrics());
        }
    }

    private void beforeCommand(PrinterCommand command) {
        for (IPrinterEvents printerEvents : events) {
            try {
                printerEvents.beforeCommand(command);
            } catch (Exception e) {
                logger.error(e);
            }
        }
    }

    private void afterCommand(PrinterCommand command) {
        for (IPrinterEvents printerEvents : events) {
            try {
                printerEvents.afterCommand(command);
            } catch (Exception e) {
                logger.error(e);
            }
        }
    }

    public void check(int errorCode) throws Exception {
        if (errorCode != 0) {
            String text = getErrorText(errorCode);
            throw new SmFiscalPrinterException(errorCode, text);
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

    private static final int maxCmdRepeatCount = 5;

    public int executeCommand(PrinterCommand command) throws Exception {
        String text = Hex.toHex((byte) command.getCode()) + "h, "
                + command.getText();
        logger.debug(text);

        int timeout = getCommandTimeout(command.getCode());
        command.setTimeout(timeout);

        for (int i = 0; i < maxCmdRepeatCount; i++) {
            command.setRepeatNeeded(false);
            deviceExecute(command);
            resultCode = command.getResultCode();

            if (!command.getRepeatNeeded()) {
                break;
            }

            if (command.getResultCode() == SMFP_EFPTR_PREVCOMMAND) {
                // Do not count as an attempt, added to fix SHTRIH-MOBILE-F bug
                SysUtils.sleep(SMFP_EFPTR_PREVCOMMAND_TimeToSleep);
                i--;
            }
        }
        if (saveCommands && succeeded(resultCode) && isReceiptCommand(command)) {
            BinaryCommand cmd = new BinaryCommand(command.getCode(),
                    command.getText(), command.getTxData());
            receiptCommands.add(cmd);
        }
        logger.debug(text + " = " + resultCode);
        return resultCode;
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
        logger.debug("printReceiptCommands.0");
        int result = 0;
        for (int i = 0; i < receiptCommands.size(); i++) {
            PrinterCommand command = (PrinterCommand) receiptCommands.get(i);
            result = executeCommand(command);
            if (failed(result)) {
                return result;
            }

            waitForPrinting();
        }
        logger.debug("printReceiptCommands.1");
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
        return command.getStatus();
    }

    public PrinterModelParameters readPrinterModelParameters() throws Exception {
        logger.debug("readPrinterModelParameters");
        ReadPrinterModelParameters command = new ReadPrinterModelParameters();
        execute(command);
        return command.getParameters();
    }

    public ShortPrinterStatus getShortStatus() {
        return shortStatus;
    }

    public ShortPrinterStatus readShortStatus() throws Exception {
        logger.debug("readShortStatus");
        ReadShortStatus command = new ReadShortStatus(usrPassword);
        execute(command);
        shortStatus = command.getStatus();

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
        PrintString command = new PrintString(usrPassword, station, line);
        execute(command);
        return command.getOperator();
    }

    public int printBoldString(int station, String line) throws Exception {
        logger.debug("printBoldString(" + String.valueOf(station) + ", '"
                + line + "')");

        station = getPrintStation(station);
        PrintBoldString command = new PrintBoldString();
        command.setPassword(usrPassword);
        command.setStation(station);
        command.setText(line);
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
        if (isFooter) {
            station = station + SMFP_STATION_FOOTER;
        }
        return station;
    }

    public int printStringFont(int station, FontNumber font, String line)
            throws Exception {
        logger.debug("printStringFont(" + String.valueOf(station) + ", '"
                + String.valueOf(font.getValue()) + ", '" + line + "')");

        station = getPrintStation(station);
        PrintStringFont command = new PrintStringFont(usrPassword, station,
                font, line);

        execute(command);
        return command.getOperator();
    }

    // line is truncated to maximum print width
    public int printLine(int station, String line, FontNumber font)
            throws Exception {
        logger.debug("printLine(" + String.valueOf(station) + ", " + "'" + line
                + "', " + String.valueOf(font.getValue()) + ")");

        if (line.length() == 0) {
            line = " ";
        }
        int len = Math.min(line.length(), getMessageLength(font));
        line = line.substring(0, len);
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

    public int updateFieldInfo(int table, int field)
            throws Exception {
        int result = 0;
        if (!fieldInfo.isEqual(table, field)) {
            ReadFieldInfo command = new ReadFieldInfo();
            command.setPassword(sysPassword);
            command.setTable(table);
            command.setField(field);
            result = executeCommand(command);
            if (succeeded(result)) {
                fieldInfo = command.getFieldInfo();
            }
        }
        return result;
    }

    public int writeTable(int tableNumber, int rowNumber, int fieldNumber,
            String fieldValue) throws Exception {
        int result = 0;
        logger.debug("writeTable(" + String.valueOf(tableNumber) + ", "
                + String.valueOf(rowNumber) + ", "
                + String.valueOf(fieldNumber) + ", " + "'" + fieldValue + "')");

        // update field info
        result = updateFieldInfo(tableNumber, fieldNumber);
        if (failed(result)) {
            return result;
        }

        byte[] fieldData = fieldInfo.fieldToBytes(fieldValue, charsetName);
        PrinterCommand command2 = new WriteTable(sysPassword, tableNumber,
                rowNumber, fieldNumber, fieldData);
        result = executeCommand(command2);
        if (result == 0) {
            PrinterField field = fields.find(tableNumber, rowNumber, fieldNumber);
            if (field == null) {
                field = new PrinterField(fieldInfo, rowNumber);
                fields.add(field);
            }
            field.setValue(fieldValue);
        }
        return result;
    }

    public void updateTables() throws Exception {
        if (tables.size() > 0) {
            return;
        }

        int tableNumber = 1;
        while (true) {
            ReadTableInfo command = new ReadTableInfo();
            command.setPassword(sysPassword);
            command.setTableNumber(tableNumber);
            int result = executeCommand(command);
            if (result == SMFP_EFPTR_INVALID_TABLE) {
                break;
            }
            check(result);

            table = command.getTable();
            tables.add(table);

            for (int fieldNumber = 1; fieldNumber <= table.getFieldCount(); fieldNumber++) {
                updateFieldInfo(tableNumber, fieldNumber);
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

    public int readTable(int tableNumber, int rowNumber, int fieldNumber, String[] fieldValue) throws Exception {
        int result = 0;
        logger.debug("readTable(" + String.valueOf(tableNumber) + ", "
                + String.valueOf(rowNumber) + ", "
                + String.valueOf(fieldNumber) + ")");

        PrinterField field = fields.find(tableNumber, rowNumber, fieldNumber);
        if (field == null) {
            result = updateFieldInfo(tableNumber, fieldNumber);
            if (failed(result)) {
                return result;
            }
        } else {
            fieldValue[0] = field.getValue();
            return result;
        }

        ReadTable commandReadTable = new ReadTable(sysPassword, tableNumber,
                rowNumber, fieldNumber);
        result = executeCommand(commandReadTable);
        if (failed(result)) {
            return result;
        }

        String value = fieldInfo.bytesToField(commandReadTable.fieldValue,
                charsetName);

        field = new PrinterField(fieldInfo, rowNumber);
        field.setValue(value);
        fields.add(field);

        fieldValue[0] = value;
        return result;
    }

    public int readTableInfo(int tableNumber, Object[] out) throws Exception {
        logger.debug("readTableInfo");
        ReadTableInfo command = new ReadTableInfo();
        command.setPassword(sysPassword);
        command.setTableNumber(tableNumber);

        out[0] = command;
        return executeCommand(command);
    }

    public ReadTableInfo readTableInfo(int tableNumber) throws Exception {
        Object[] out = new Object[1];
        check(readTableInfo(tableNumber, out));
        return (ReadTableInfo) out[0];
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

    public ContinuePrint continuePrint() throws Exception {
        logger.debug("continuePrint");
        ContinuePrint command = new ContinuePrint();
        command.setPassword(usrPassword);
        execute(command);
        return command;
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
        logger.debug("closeReceipt");
        EndFiscalReceipt command = new EndFiscalReceipt();
        command.setPassword(usrPassword);
        command.setParams(params);
        int resultCode = executeCommand(command);
        if (resultCode == 168) {
            ReadEJStatus command2 = new ReadEJStatus();
            command2.setPassword(sysPassword);
            int resultCode2 = executeCommand(command2);
            if (resultCode2 == 0) {
                logger.debug("EJ date: "
                        + command2.getStatus().getDocDate().toString());
                logger.debug("EJ time: "
                        + command2.getStatus().getDocTime().toString());
            }
        }
        check(resultCode);
        return command;
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

    public PrintEJDayReportOnDates printEJDayReportOnDates(EJDate date1,
            EJDate date2, int reportType) throws Exception {
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
        return result;
    }

    public int fsPrintRecItem2(int operation, PriceItem item) throws Exception {
        FSReceiptItem fsReceiptItem = new FSReceiptItem();
        fsReceiptItem.setOperation(operation);
        fsReceiptItem.setQuantity(item.getQuantity() * 1000);
        fsReceiptItem.setPrice(item.getPrice());
        fsReceiptItem.setAmount(item.getTotalAmount() == null ? 0xFFFFFFFFFFL : item.getTotalAmount());
        fsReceiptItem.setTaxAmount(0xFFFFFFFFFFL);
        fsReceiptItem.setTax(getTaxBits(item.getTax1()));
        fsReceiptItem.setDepartment(item.getDepartment());
        fsReceiptItem.setPaymentType(item.getPaymentType() == null ? 0 : item.getPaymentType());
        fsReceiptItem.setPaymentItem(item.getSubjectType() == null ? 0 : item.getSubjectType());
        fsReceiptItem.setText(item.getText());
        int rc = fsPrintRecItem(fsReceiptItem);
        capFSPrintItem = isCommandSupported(rc);
        return rc;
    }

    public void printSale(PriceItem item) throws Exception {
        logger.debug("printSale");
        String text = getRecItemText(item.getText());
        item.setText(text);

        if (capFSPrintItem) {
            int rc = fsPrintRecItem2(1, item);
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
            int rc = fsPrintRecItem2(2, item);
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
            int rc = fsPrintRecItem2(3, item);
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
            int rc = fsPrintRecItem2(4, item);
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
        item.setText(text);

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

    public PrintZReport printZReport() throws Exception {
        logger.debug("printZReport");

        PrintZReport command = new PrintZReport();
        command.setPassword(sysPassword);
        execute(command);
        printCalcReport();
        return command;
    }

    public void printCalcReport() throws Exception {
        if (!params.calcReportEnabled) {
            return;
        }

        if (!getCapFiscalStorage()) {
            return;
        }

        try {
            waitForPrinting();
            FSReadCommStatus status = fsReadCommStatus();
            printLines("КОЛИЧЕСТВО СООБЩЕНИЙ ДЛЯ ОФД:", String.valueOf(status.getQueueSize()));
            printLines("НОМЕР ПЕРВОГО ДОКУМЕНТА ДЛЯ ОФД:", String.valueOf(status.getDocumentNumber()));
            String docDate = status.getDocumentDate().toString() + " " + status.getDocumentTime().toString2();
            printLines("ДАТА ПЕРВОГО ДОКУМЕНТА:", docDate);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
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

    public void writeTime(PrinterTime time) throws Exception {
        logger.debug("setTime");

        WriteTime command = new WriteTime();
        command.setPassword(sysPassword);
        command.setTime(time);
        execute(command);
    }

    public void writePortParams(int portNumber, int baudRate, int timeout)
            throws Exception {
        logger.debug("writePortParams(" + String.valueOf(portNumber) + ", "
                + String.valueOf(baudRate) + ", " + String.valueOf(timeout)
                + ")");

        MethodParameter.checkByte(portNumber, "portNumber");
        MethodParameter.checkByte(baudRate, "baudRate");
        WritePortParams command = new WritePortParams();
        command.setPassword(sysPassword);
        command.setPortNumber(portNumber);
        command.setBaudRate(baudRate);
        command.setTimeout(timeout);
        execute(command);
        SysUtils.sleep(300);
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

        PrintReceiptCopy command = new PrintReceiptCopy(usrPassword);
        execute(command);
    }

    public void openReceipt(int receiptType) throws Exception {
        logger.debug("openReceipt");

        openFiscalDay();
        PrinterStatus status = waitForPrinting();
        if (!status.getPrinterMode().isReceiptOpened()) {
            OpenReceipt command = new OpenReceipt();
            command.setPassword(usrPassword);
            command.setReceiptType(receiptType);
            int rc = executeCommand(command);
            capOpenReceipt = isCommandSupported(rc);
            check(rc);
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
        command.setFlags(2);
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
        sleep(params.getGraphicsLineDelay());
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

    public boolean checkEcrMode(int mode) throws Exception {
        logger.debug("checkEcrMode");
        switch (mode) {
            case MODE_FULLREPORT:
            case MODE_EJREPORT:
            case MODE_SLPPRINT:
                SysUtils.sleep(TimeToSleep);
                break;

            default:
                return true;
        }
        return false;
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
            if (command.getResultCode() == 0) {
                return;
            }
            if (i == maxCount) {
                check(command.getResultCode());
            }
        }
    }

    public PrinterStatus waitForPrinting() throws Exception {
        logger.debug("waitForPrinting");
        PrinterStatus status = null;
        try {
            for (;;) {
                status = readPrinterStatus();
                switch (status.getSubmode()) {
                    case ECR_SUBMODE_IDLE: {
                        if (checkEcrMode(status.getMode())) {
                            return status;
                        }
                        break;
                    }

                    case ECR_SUBMODE_PASSIVE:
                    case ECR_SUBMODE_ACTIVE: {
                        checkPaper(status);
                        break;
                    }

                    case ECR_SUBMODE_AFTER: {
                        continuePrint();
                        break;
                    }

                    case ECR_SUBMODE_REPORT:
                    case ECR_SUBMODE_PRINT: {
                        SysUtils.sleep(TimeToSleep);
                        break;
                    }

                    default: {
                        logger.debug("Unknown submode");
                        return status;
                    }
                }
            }
        } catch (InterruptedException e) {
            // Restore the interrupted status
            logger.error("InterruptedException", e);
            Thread.currentThread().interrupt();
        }
        return status;
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

    public int beginFiscalDay() throws Exception {
        BeginFiscalDay command = new BeginFiscalDay();
        command.setPassword(usrPassword);
        int rc = executeCommand(command);
        capOpenFiscalDay = isCommandSupported(rc);
        return rc;
    }

    public void resetFM() throws Exception {
        ResetFM command = new ResetFM();
        execute(command);
    }

    public void sysAdminCancelReceipt() throws Exception {
        logger.debug("sysAdminCancelReceipt");
        String[] passwordString = new String[0];
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
        for (int i = 0; i < tables.size(); i++) {
            PrinterTable table = tables.get(i);
            PrinterFields fields = table.getFields();
            for (int j = 0; j < fields.size(); j++) {
                PrinterField field = fields.get(j);
                check(writeTable(field.getTable(), field.getRow(),
                        field.getField(), field.getValue()));
            }
        }
    }

    public void writeFields(PrinterFields fields) throws Exception {
        for (int i = 0; i < fields.size(); i++) {
            writeField2(fields.get(i));
        }
    }

    public PrinterTable getTable(int tableNumber) throws Exception {
        if (tableNumber != table.getNumber()) {
            table = readTableInfo(tableNumber).getTable();
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
                String[] fieldValue = new String[1];
                check(readTable(table.getNumber(), rowNumber, fieldNumber,
                        fieldValue));
                PrinterField field = new PrinterField(fieldInfo, rowNumber);
                field.setValue(fieldValue[0]);
                table.getFields().add(field);
            }
        }
    }

    public PrinterField readField(PrinterField field) throws Exception {
        String[] fieldValue = new String[1];
        check(readTable(field.getTable(), field.getRow(), field.getField(),
                fieldValue));
        field.setValue(fieldValue[0]);
        return field;
    }

    public int writeField(PrinterField field) throws Exception {
        return writeTable(field.getTable(), field.getRow(), field.getField(),
                field.getValue());
    }

    // Write field only if field is valid and value differs
    public void writeField2(PrinterField field) throws Exception {
        if (isValidField(field)) {
            PrinterField field2 = readField(field.getCopy());

            if (!field.isEqualValue(field2)) {
                writeField(field);
            }
        }
    }

    public void readTables(PrinterTables tables) throws Exception {
        tables.clear();
        int tableNumber = 1;
        while (true) {
            ReadTableInfo command = new ReadTableInfo();
            command.setPassword(sysPassword);
            command.setTableNumber(tableNumber);
            int result = executeCommand(command);
            if (result == SMFP_EFPTR_INVALID_TABLE) {
                break;
            }
            check(result);

            table = command.getTable();
            tables.add(table);

            for (int fieldNumber = 1; fieldNumber <= table.getFieldCount(); fieldNumber++) {
                for (int rowNumber = 1; rowNumber <= table.getRowCount(); rowNumber++) {
                    String[] fieldValue = new String[1];
                    check(readTable(tableNumber, rowNumber, fieldNumber,
                            fieldValue));
                    PrinterField field = new PrinterField(fieldInfo, rowNumber);
                    field.setValue(fieldValue[0]);
                    table.getFields().add(field);
                }
            }
            tableNumber++;
        }
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
        for (IPrinterEvents event : events) {
            event.printerStatusRead(status);
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

    public PrinterModel getModel() throws Exception {
        return model;
    }

    public boolean getCapLoadGraphics1() throws Exception {
        if (capLoadGraphics1 == Boolean.NOTDEFINED) {
            byte[] data = new byte[40];
            for (int i = 0; i < data.length; i++) {
                data[i] = 0;
            }
            if (isCommandSupported(loadGraphics1(1, data))) {
                capLoadGraphics1 = Boolean.TRUE;
            } else {
                capLoadGraphics1 = Boolean.FALSE;
            }
        }
        return capLoadGraphics1 == Boolean.TRUE;
    }

    public boolean getCapLoadGraphics2() throws Exception {
        if (capLoadGraphics2 == Boolean.NOTDEFINED) {
            byte[] data = new byte[40];
            for (int i = 0; i < data.length; i++) {
                data[i] = 0;
            }
            if (isCommandSupported(loadGraphics2(1, data))) {
                capLoadGraphics2 = Boolean.TRUE;
            } else {
                capLoadGraphics2 = Boolean.FALSE;
            }
        }
        return capLoadGraphics2 == Boolean.TRUE;
    }

    public boolean capGraphicsFlags(){
        return (capModelParameters() && (modelParameters.isCapGraphicsFlags()));
    }
            
    public boolean capModelParameters(){
        return modelParameters != null;
    }
    
    public boolean getCapLoadGraphics3() throws Exception {
        if (capLoadGraphics3 == Boolean.NOTDEFINED) 
        {
            if (capModelParameters()) {
                if (modelParameters.isGraphics512Supported()) {
                    capLoadGraphics3 = Boolean.TRUE;
                } else {
                    capLoadGraphics3 = Boolean.FALSE;
                }
            } else {
                byte[] data = new byte[40];
                for (int i = 0; i < data.length; i++) {
                    data[i] = 0;
                }
                if (isCommandSupported(loadGraphics3(1, data))) {
                    capLoadGraphics3 = Boolean.TRUE;
                } else {
                    capLoadGraphics3 = Boolean.FALSE;
                }
            }
        }
        return capLoadGraphics3 == Boolean.TRUE;
    }

    public boolean getCapPrintGraphics1() throws Exception {
        if (capPrintGraphics1 == Boolean.NOTDEFINED) {
            if (isCommandSupported(printGraphics1(1, 2))) {
                capPrintGraphics1 = Boolean.TRUE;
            } else {
                capPrintGraphics1 = Boolean.FALSE;
            }
        }
        return capPrintGraphics1 == Boolean.TRUE;
    }

    public boolean getCapPrintGraphics2() throws Exception {
        if (capPrintGraphics2 == Boolean.NOTDEFINED) {
            if (isCommandSupported(printGraphics2(1, 2))) {
                capPrintGraphics2 = Boolean.TRUE;
            } else {
                capPrintGraphics2 = Boolean.FALSE;
            }
        }
        return capPrintGraphics2 == Boolean.TRUE;
    }

    public boolean getCapPrintGraphics3() throws Exception {
        if (capPrintGraphics3 == Boolean.NOTDEFINED) {
            if (capModelParameters()) {
                if (modelParameters.isGraphics512Supported()) {
                    capPrintGraphics3 = Boolean.TRUE;
                } else {
                    capPrintGraphics3 = Boolean.FALSE;
                }
            } else if (isCommandSupported(printGraphics3(1, 2))) {
                capPrintGraphics3 = Boolean.TRUE;
            } else {
                capPrintGraphics3 = Boolean.FALSE;
            }
        }
        return capPrintGraphics3 == Boolean.TRUE;
    }

    public boolean getCapPrintScaled() throws Exception {
        if (capPrintScaled == Boolean.NOTDEFINED) {
            if (isCommandSupported(printScaled(1, 2, 1, 1))) {
                capPrintScaled = Boolean.TRUE;
            } else {
                capPrintScaled = Boolean.FALSE;
            }
        }
        return capPrintScaled == Boolean.TRUE;
    }

    public boolean getCapPrintGraphicsLine() throws Exception {
        if (capPrintGraphicsLine == Boolean.NOTDEFINED) {
            byte[] data = new byte[40];
            for (int i = 0; i < data.length; i++) {
                data[i] = 0;
            }

            if (isCommandSupported(printGraphicLine(SMFP_STATION_REC, 1, data))) {
                capPrintGraphicsLine = Boolean.TRUE;
            } else {
                capPrintGraphicsLine = Boolean.FALSE;
            }
        }
        return capPrintGraphicsLine == Boolean.TRUE;
    }

    public boolean getCapPrintBarcode2() throws Exception {
        if (capPrintBarcode2 == Boolean.NOTDEFINED) {
            PrinterBarcode barcode = new PrinterBarcode();
            barcode.setHeight(1);
            barcode.setText("123456789012");
            barcode.setType(SmFptrConst.SMFPTR_BARCODE_EAN13);
            if (isCommandSupported(printBarcode2(barcode))) {
                capPrintBarcode2 = Boolean.TRUE;
            } else {
                capPrintBarcode2 = Boolean.FALSE;
            }
        }
        return capPrintBarcode2 == Boolean.TRUE;
    }

    public boolean getCapPrintBarcode3() throws Exception {
        if (capPrintBarcode3 == Boolean.NOTDEFINED) {
            byte[] data = {0x00};
            LoadBarcode3 command = new LoadBarcode3();
            command.setPassword(usrPassword);
            command.setBlockType(0);
            command.setBlockNumber(0);
            command.setBlockData(data);
            int rc = executeCommand(command);
            if (isCommandSupported(rc)) {
                capPrintBarcode3 = Boolean.TRUE;
            } else {
                capPrintBarcode3 = Boolean.FALSE;
            }
        }
        return capPrintBarcode3 == Boolean.TRUE;
    }

    public boolean getCapDisableDiscountText() {
        return capDisableDiscountText;
    }

    public boolean getCapDiscount() {
        return capDiscount;
    }

    // 17,1,18,1,0,0,3,"Rus компактный заголовок","0"
    public void initialize() throws Exception {
        logger.debug("initialize()");

        readFonts();

        ReadPrinterModelParameters command = new ReadPrinterModelParameters();
        if (executeCommand(command) == 0) {
            modelParameters = command.getParameters();
        }

        headerHeigth = getModel().getHeaderHeight();
        capFiscalStorage = readCapFiscalStorage();
        capFooterFlag = capFiscalStorage;
        if (capFiscalStorage) {
            boolean isCompactHeader = false;
            String[] fieldValue = new String[1];
            int rc = readTable(17, 1, 18, fieldValue);
            if (succeeded(rc)) {
                isCompactHeader = fieldValue[0].equalsIgnoreCase("3");
            }

            subtotalInHeader = isCompactHeader;
            discountInHeader = isCompactHeader;

            capDisableDiscountText = readCapDisableDiscountText();
            capFSCloseReceipt = readCapFSCloseReceipt();

            discountMode = 2;
            rc = readTable(17, 1, 3, fieldValue);
            if (succeeded(rc)) {
                discountMode = Integer.parseInt(fieldValue[0]);
            }
            rc = readTable(18, 1, 7, fieldValue);
            if (succeeded(rc)) {
                fsUser = fieldValue[0];
            }
            rc = readTable(18, 1, 9, fieldValue);
            if (succeeded(rc)) {
                fsAddress = fieldValue[0];
            }

            rc = readTable(10, 1, 1, fieldValue);
            if (succeeded(rc)) {
                headerHeigth = Integer.valueOf(fieldValue[0]);
            }
        }
        capDiscount = true;
        if (isShtrihMobile()) {
            capDiscount = false;
            capGraphics3Scale = true;
        } else {
            capDiscount = discountMode == 0;
        }

        if (capFiscalStorage) {
            if (isShtrihMobile()) {
                getModel().addParameter("fdoName", "", 14, 1, 10);
            } else {
                getModel().addParameter("fdoName", "", 18, 1, 10);
            }
        }
    }

    private boolean readCapDisableDiscountText() throws Exception {
        PrinterDate date1 = new PrinterDate(10, 04, 17);
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
            throw new SmFiscalPrinterException(resultCode,
                    getErrorText(resultCode));
        }
        if (getModel().getCapJrnPresent()
                && status.getPrinterFlags().isJrnEmpty()) {
            resultCode = SMFP_EFPTR_NO_JRN_PAPER;
            throw new SmFiscalPrinterException(resultCode,
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

        String[] fieldValue = new String[1];
        check(readTable(parameter.getTableNumber(), parameter.getRowNumber(),
                parameter.getFieldNumber(), fieldValue));
        return fieldValue[0];
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
        waitForPrinting();
        sleep(getParams().barcodeDelay);
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
        String data = centerLine(barcode.getLabel());
        doPrintText(SMFP_STATION_REC, data, params.font);
    }

    public void printText(String text)
            throws Exception {
        printText(SMFP_STATION_REC, text, params.font);
    }

    public void printText(int station, String text, FontNumber font)
            throws Exception {
        logger.debug("printText(" + station + ", " + text + ")");
        if (text.length() == 0) {
            text = " ";
        }
        String data = processEscCommands(text);
        if (data.length() > 0) {
            doPrintText(station, data, font);
        }
    }

    public void doPrintText(int station, String text, FontNumber font)
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
        waitForPrinting();
        if (barcode.isTextBelow()) {
            printBarcodeLabel(barcode);
        }
    }

    public int getImageFirstLine() {
        return getPrinterImages().getTotalSize() + 3;
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
                getMaxGraphicsWidth(), barcode.getBarWidth(), barcode.getHeight());
        SmBarcode bc = encoder.encode(barcode);
        if (bc == null) {
            throw new Exception("Barcode type is not supported");
        }

        if (getCapPrintGraphicsLine() && getParams().graphicsLineEnabled) {
            int width = getModel().getFonts().get(0).getPaperWidth();
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
            bc.centerBarcode(getMaxGraphicsWidth());
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

    public int printBarcode3(PrinterBarcode barcode) throws Exception {
        logger.debug("printBarcode3");
        // Load barcode data
        String text = barcode.getText();
        int blockSize = LoadBarcode3.MAX_BLOCK_SIZE;
        int blockCount = (text.length() + blockSize - 1) / blockSize;
        for (int i = 0; i < blockCount; i++) {
            int sIndex = i * blockSize;
            int eIndex = (i + 1) * blockSize;
            if (eIndex > (text.length())) {
                eIndex = text.length();
            }
            String blockData = text.substring(sIndex, eIndex);

            LoadBarcode3 command = new LoadBarcode3();
            command.setPassword(usrPassword);
            command.setBlockType(0);
            command.setBlockNumber(i);
            command.setBlockData(blockData.getBytes());
            int result = executeCommand(command);
            if (result != 0) {
                return result;
            }
        }
        // Print barcode
        PrintBarcode3 command = new PrintBarcode3();
        command.setPassword(usrPassword);
        command.setBarcodeType(PrintBarcode3.QRCODE);
        command.setDataLength(text.length());
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

    public void sleep(long millis) {
        try {
            SysUtils.sleep(millis);
        } catch (InterruptedException e) {
            // Restore the interrupted status
            logger.error("InterruptedException", e);
            Thread.currentThread().interrupt();
        }
    }

    public int getMaxGraphicsWidth() throws Exception {
        if (capModelParameters()) {
            if (getCapLoadGraphics3()) {
                return modelParameters.getGraphics512Width();
            } else {
                return modelParameters.getGraphicsWidth();
            }
        } else if (getCapLoadGraphics3()) {
            return 512;
        } else {
            return 320;
        }
    }

    public int getMaxGraphicsLineWidth() throws Exception {
        if (capModelParameters()) {
            return modelParameters.getGraphicsWidth();
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

    public int getCommandTimeout(int code) throws Exception {
        int timeout;
        FlexCommand command = getCommands().itemByCode(code);
        if (command != null) {
            timeout = command.getTimeout();
        } else {
            timeout = PrinterCommand.getDefaultTimeout(code);
        }
        return timeout;
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

        FSReadTotals command = new FSReadTotals();
        int rc = executeCommand(command);
        capFSTotals = isCommandSupported(rc);
        if (succeeded(rc)) {
            return command.getTotals();
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
                    lines.add(command2.getData());
                }
            }
            check(cancelEJDocument());
        }
        return (String[]) (lines.toArray(new String[0]));
    }

    public void readFonts() throws Exception {
        PrinterFonts fonts = new PrinterFonts();
        int fontNumber = 1;
        int fontCount = 15;
        while (fontNumber <= fontCount) {
            ReadFontMetrics command = new ReadFontMetrics();
            command.setPassword(sysPassword);
            command.setFont(fontNumber);
            if (executeCommand(command) != 0) {
                break;
            }
            fonts.add(
                    fontNumber,
                    command.getCharWidth(),
                    command.getCharHeight(),
                    command.getPaperWidth());
            fontNumber++;
            fontCount = command.getFontCount();
        }
        if (fonts.size() > 0) {
            getModel().setFonts(fonts);
        }
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
            PrinterTables tables = new PrinterTables();
            readTables(tables);
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
            logger.error(e.getMessage());
        }
    }

    public ReceiptImages getReceiptImages() {
        return receiptImages;
    }

    public void printReceiptImage(int position) throws Exception {
        for (int i = 0; i < receiptImages.size(); i++) {
            ReceiptImage image = receiptImages.get(i);
            if (image.valid(position)) {
                PrinterImage printerImage = printerImages.get(image
                        .getImageIndex());
                printImage(printerImage);
            }
        }
    }

    public PrinterImage getPrinterImage(int position) throws Exception {
        ReceiptImage image = receiptImages.imageByPosition(position);
        if (image != null) {
            int index = image.getImageIndex();
            if (getPrinterImages().validIndex(index)) {
                return getPrinterImages().get(index);
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

        image.readFile();
        image.setStartPos(getPrinterImages().getTotalSize() + 1);
        loadImage2(image);
        if (addImage) {
            getPrinterImages().add(image);
            service.saveProperties();
        }
    }

    public void loadImage2(PrinterImage image) throws Exception {
        logger.debug("loadImage2");
        if (image.getIsLoaded()) {
            logger.debug("Image already loaded");
            return;
        }

        if (getParams().centerImage) {
            image.centerImage(getMaxGraphicsWidth());
        }
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
    }

    public int loadRawGraphics(byte[][] data) throws Exception {
        int startPos = getPrinterImages().getTotalSize() + 1;

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

    public void cutPaper() throws Exception {
        if (capCutPaper && (params.cutMode == SmFptrConst.SMFPTR_CUT_MODE_AUTO)) {
            if (params.cutPaperDelay != 0) {
                SysUtils.sleep(params.cutPaperDelay);
            }
            int rc = cutPaper(params.cutType);
            capCutPaper = isCommandSupported(rc);
        }
    }

    public void printBlankSpace(int height) throws Exception {
        FontNumber font = FontNumber.getNormalFont();
        int lineHeight = getLineHeight(font);
        int lineCount = (height + lineHeight - 1) / lineHeight;
        for (int i = 0; i < lineCount; i++) {
            printLine(SMFP_STATION_REC, " ", font);
        }
        waitForPrinting();
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
    public String readFullSerial() throws Exception {
        if (serial.isEmpty()) {
            int tableNumber = 18;
            if (isShtrihMobile()) {
                tableNumber = 14;
            }
            serial = readTable(tableNumber, 1, 1).trim();
        }
        return serial;
    }

    @Override
    public String readRnm() throws Exception {
        int tableNumber = 18;
        if (isShtrihMobile()) {
            tableNumber = 14;
        }

        return readTable(tableNumber, 1, 3).trim();
    }

    private boolean isShtrihMobile() throws Exception {
        return getModel().getModelID() == 19;
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

    public int fsWriteTLV(byte[] tlv) throws Exception {
        FSWriteTLV command = new FSWriteTLV();
        command.setSysPassword(sysPassword);
        command.setTlv(tlv);
        execute(command);
        return command.getResultCode();
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
        String key = "PrinterError" + Hex.toHex((byte) code);
        if ((capFiscalStorage) && (code < 0x20)) {
            key = "FSPrinterError" + Hex.toHex((byte) code);
        }
        String result = Localizer.getString(key);
        if (result.equals(key)) {
            result = Localizer.UnknownPrinterError;
        }
        return String.valueOf(code) + ", " + result;
    }

    public void openFiscalDay() throws Exception {
        logger.debug("openFiscalDay");
        if (!capOpenFiscalDay) {
            return;
        }
        PrinterStatus status = waitForPrinting();
        if (status.getPrinterMode().isDayClosed()) {
            beginFiscalDay();
            waitForPrinting();
        }
    }

    public byte[] getTLVData(int tagId, String tagValue) throws Exception {
        TLVList list = new TLVList();
        switch (tagId) {
            case 15001:
            case 15002: {
                list.add(tagId, Integer.valueOf(tagValue), 4);
                break;
            }
            default:
                list.add(tagId, tagValue);
                break;

        }
        return list.getData();
    }

    public int fsWriteTag(int tagId, String tagValue) throws Exception {
        return fsWriteTLV(getTLVData(tagId, tagValue));
    }

    ///////////////////////////////////////////////////////////////////////////
    // Данные в массиве представлены в виде строки, в которой: 
    // первые 4 байта – код справочника
    // последующие 8 байт – код группы товаров
    // последние 20 байт – код идентификации товара
    ///////////////////////////////////////////////////////////////////////////
    public int fsWriteTag1162(int catId, long groupId, String itemId)
            throws Exception {
        TLVWriter writer = new TLVWriter();
        writer.add(catId, 4);
        writer.add(groupId, 8);
        writer.add(itemId, 20);
        byte[] data = writer.getBytes();

        writer.clear();
        writer.add(1162, data);
        return fsWriteTLV(writer.getBytes());
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
        writeTable(17, 1, 7, "1");
    }

    public int fsReceiptDiscount(FSReceiptDiscount command) throws Exception {
        return executeCommand(command);
    }

    public String getDepartmentName(int number) throws Exception {
        return readTable(PrinterConst.SMFP_TABLE_DEPARTMENT, number, 1);
    }

    public String getTaxName(int number) throws Exception {
        return readTable(PrinterConst.SMFP_TABLE_TAX, number, 2);
    }

    public int getTaxRate(int number) throws Exception {
        String s = readTable(PrinterConst.SMFP_TABLE_TAX, number, 1);
        return Integer.parseInt(s);
    }

    public int getDiscountMode() throws Exception {
        return discountMode;
    }

    public int printDocHeader(String title, int number) throws Exception {
        PrintDocHeader command = new PrintDocHeader();
        command.setPassword(usrPassword);
        command.setTitle(title);
        command.setNumber(number);
        return executeCommand(command);
    }

    public Vector<String> fsReadDocumentTLVAsText(int docNumber) throws Exception {
        logger.debug("fsReadDocumentTLVAsText");
        byte[] ba = fsReadDocumentTLV(docNumber);
        TLVParser reader = new TLVParser();
        reader.read(ba);
        Vector<String> lines = reader.getPrintText();
        for (int i = 0; i < lines.size(); i++) {
            logger.debug(lines.get(i));
        }
        return lines;
    }

    public byte[] fsReadDocumentTLV(int docNumber) throws Exception {
        FSReadDocument readDocument = fsRequestDocumentTLV(docNumber);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try {
            while (stream.size() < readDocument.getDocSize()) {
                byte[] tlvBlock = fsReadDocumentTLVBlock();
                stream.write(tlvBlock);
            }
        } finally {
            stream.close();
        }
        return stream.toByteArray();
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

    public void printLines(String line1, String line2) throws Exception {
        String text = StringUtils.alignLines(line1, line2, getMessageLength());
        printText(text);
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

    public void printItems(Vector<PrintItem> items) throws Exception {
        for (int i = 0; i < items.size(); i++) {
            PrintItem item = items.get(i);
            try {
                item.print(this);
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }
        items.clear();
    }

    public void printFSHeader() throws Exception {
        if (getCapFiscalStorage() && getParams().fsHeaderEnabled) {
            printText(fsUser);
            printText(fsAddress);
        }
    }

    public boolean getCapOpenReceipt() {
        return capOpenReceipt;
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
            line2 = String.format("#%04d", status.getDocumentNumber());
            printLines2(line1, line2);
            // 3
            line1 = docName;
            line2 = String.format("ИНН %010d", status.getFiscalID());
            printLines(line1, line2);
            // 4
            line1 = "РН ККТ " + readTable(18, 1, 3).trim();
            line2 = "ФН " + fsReadSerial().getSerial();
            printLines2(line1, line2);
            // 5
            line1 = "Сайт ФНС:";
            line2 = readTable(18, 1, 13).trim();
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
        if (getCapFiscalStorage()) {
            return "КАССОВЫЙ ЧЕК/" + fsDocNames[receiptType];
        } else {
            return docNames[receiptType];
        }
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

    public boolean capReadFSBuffer() throws Exception {
        FSReadBufferStatus command = new FSReadBufferStatus(sysPassword);
        return isCommandSupported(executeCommand(command));
    }

    public byte[] fsReadBlockData() throws Exception {
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

    public void fsWriteBlockData(byte[] data) throws Exception {
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

    public boolean getCapFSCloseReceipt() {
        return capFSCloseReceipt;
    }

    public Vector<FSTicket> fsReadTickets(int[] fsDocumentNumbers) throws Exception {
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

    public Vector<FSTicket> fsReadTickets(int firstFSDocumentNumber,
            int documentCount) throws Exception {
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

    public int fsPrintCorrectionReceipt(FSPrintCorrectionReceipt command)
            throws Exception {
        openFiscalDay();
        return executeCommand(command);
    }

    public int fsPrintCorrectionReceipt2(FSPrintCorrectionReceipt2 command)
            throws Exception {
        openFiscalDay();
        return executeCommand(command);
    }

    public boolean isSubtotalInHeader() {
        return subtotalInHeader;
    }

    public boolean isDiscountInHeader() {
        return discountInHeader;
    }

    public int reboot() throws Exception {
        Reboot command = new Reboot();
        command.setPassword(sysPassword);
        return executeCommand(command);
    }

    /*
        
     // Таблица 15, Параметры офд
     // Номер таблицы,Ряд,Поле,Размер поля,Тип поля,Мин. значение, Макс.значение, Название,Значение
     15,1,1,16,1,0,65535,'Ip адрес сервера офд','91.107.67.212'
     15,1,2,2,0,0,65535,'Tcp порт сервера офд','7779'
     15,1,3,2,0,0,65535,'Тайм-аут опроса офд, 1 с','5'
    
     */
    public FDOParameters readFDOParameters() throws Exception {
        if (capFiscalStorage) {

            final int tableNumber = isShtrihMobile() ? 15 : 19;

            //final int tableNumber = c().OFDTable;
            final int hostField = 1;
            final int portField = 2;
            final int pollPeriodField = 3;

            String host = readTable(tableNumber, 1, hostField);

            int portValue = readTableIntValueFromStringOrInt(tableNumber, portField);
            int pollPeriod = isShtrihMobile() ? readTableIntValueFromStringOrInt(tableNumber, pollPeriodField) : 5;

            return new FDOParameters(host, portValue, pollPeriod);
        }

        return null;
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

    public int getHeaderHeight() throws Exception {
        return headerHeigth;
    }

    public int fsPrintRecItem(FSReceiptItem item) throws Exception {
        FSPrintRecItem command = new FSPrintRecItem();
        command.setPassword(usrPassword);
        command.setItem(item);
        return executeCommand(command);
    }

    public int getTaxBits(int tax) {
        int result = 0;
        if ((tax >= 1) && (tax <= 6)) {
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

}
