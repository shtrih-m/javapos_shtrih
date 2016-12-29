/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.fiscalprinter;

import com.shtrih.barcode.PrinterBarcode;
import com.shtrih.ej.EJDate;
import com.shtrih.fiscalprinter.command.AmountItem;
import com.shtrih.fiscalprinter.command.Beep;
import com.shtrih.fiscalprinter.command.BeginTest;
import com.shtrih.fiscalprinter.command.CashRegister;
import com.shtrih.fiscalprinter.command.CloseRecParams;
import com.shtrih.fiscalprinter.command.ContinuePrint;
import com.shtrih.fiscalprinter.command.DeviceMetrics;
import com.shtrih.fiscalprinter.command.EndFiscalReceipt;
import com.shtrih.fiscalprinter.command.EndTest;
import com.shtrih.fiscalprinter.command.FSReadCommStatus;
import com.shtrih.fiscalprinter.command.FSReadDayParameters;
import com.shtrih.fiscalprinter.command.FSReadFiscalization;
import com.shtrih.fiscalprinter.command.FSReadStatus;
import com.shtrih.fiscalprinter.command.FSReceiptDiscount;
import com.shtrih.fiscalprinter.command.FSWriteTLV;
import com.shtrih.fiscalprinter.command.FlexCommands;
import com.shtrih.fiscalprinter.command.IPrinterEvents;
import com.shtrih.fiscalprinter.command.LongPrinterStatus;
import com.shtrih.fiscalprinter.command.OpenReceipt;
import com.shtrih.fiscalprinter.command.OperationRegister;
import com.shtrih.fiscalprinter.command.PriceItem;
import com.shtrih.fiscalprinter.command.PrintCashIn;
import com.shtrih.fiscalprinter.command.PrintCashOut;
import com.shtrih.fiscalprinter.command.PrintCharge;
import com.shtrih.fiscalprinter.command.PrintDiscount;
import com.shtrih.fiscalprinter.command.PrintEJDayReportOnDates;
import com.shtrih.fiscalprinter.command.PrintEJDayReportOnDays;
import com.shtrih.fiscalprinter.command.PrintFMReportDates;
import com.shtrih.fiscalprinter.command.PrintFMReportDays;
import com.shtrih.fiscalprinter.command.PrintVoidCharge;
import com.shtrih.fiscalprinter.command.PrintVoidDiscount;
import com.shtrih.fiscalprinter.command.PrintVoidItem;
import com.shtrih.fiscalprinter.command.PrintXReport;
import com.shtrih.fiscalprinter.command.PrintZReport;
import com.shtrih.fiscalprinter.command.PrinterCommand;
import com.shtrih.fiscalprinter.command.PrinterDate;
import com.shtrih.fiscalprinter.command.PrinterStatus;
import com.shtrih.fiscalprinter.command.PrinterTime;
import com.shtrih.fiscalprinter.command.ReadEJActivationReport;
import com.shtrih.fiscalprinter.command.ReadEJStatus;
import com.shtrih.fiscalprinter.command.ReadFMLastRecordDate;
import com.shtrih.fiscalprinter.command.ReadFMTotals;
import com.shtrih.fiscalprinter.command.ReadFieldInfo;
import com.shtrih.fiscalprinter.command.ReadLongStatus;
import com.shtrih.fiscalprinter.command.ReadTableInfo;
import com.shtrih.fiscalprinter.command.ShortPrinterStatus;
import com.shtrih.fiscalprinter.command.VoidFiscalReceipt;
import com.shtrih.fiscalprinter.model.PrinterModel;
import com.shtrih.fiscalprinter.model.PrinterModels;
import com.shtrih.fiscalprinter.port.PrinterPort;
import com.shtrih.fiscalprinter.table.PrinterField;
import com.shtrih.fiscalprinter.table.PrinterFields;
import com.shtrih.fiscalprinter.table.PrinterTable;
import com.shtrih.fiscalprinter.table.PrinterTables;
import com.shtrih.jpos.fiscalprinter.FptrParameters;
import com.shtrih.jpos.fiscalprinter.PrinterImages;
import com.shtrih.printer.ncr7167.NCR7167Printer;
import java.util.Vector;
import com.shtrih.util.CompositeLogger;
import com.shtrih.fiscalprinter.command.PrinterConst;
import com.shtrih.fiscalprinter.command.ReadCashRegister;
import com.shtrih.fiscalprinter.command.ReadOperationRegister;
import com.shtrih.jpos.fiscalprinter.PrinterImage;
import com.shtrih.jpos.fiscalprinter.ReceiptImages;
import com.shtrih.util.BitUtils;

/**
 *
 * @author Kravtsov
 */
public class SMFiscalPrinterNull implements SMFiscalPrinter {

    public PrinterProtocol device;
    private PrinterModel model = null;
    private final PrinterModels models = new PrinterModels();
    private DeviceMetrics deviceMetrics = new DeviceMetrics();
    private PrinterStatus printerStatus = new PrinterStatus();
    private LongPrinterStatus longStatus = new LongPrinterStatus();
    private ShortPrinterStatus shortStatus = new ShortPrinterStatus();
    public ReadTableInfo tableInfo = new ReadTableInfo();
    public ReadFieldInfo fieldInfo = new ReadFieldInfo();
    public static CompositeLogger logger = CompositeLogger.getLogger(SMFiscalPrinterNull.class);
    private final PrinterPort port;
    private final FptrParameters params;
    private final PrinterImages printerImages = new PrinterImages();
    private int resultCode = 0;

    public SMFiscalPrinterNull(
            PrinterPort port,
            PrinterProtocol device,
            FptrParameters params) {
        this.port = port;
        this.device = device;
        this.params = params;
        models.load();
        try {
            model = models.itemByID(PrinterConst.SMFP_MODELID_DEFAULT);
        } catch (Exception e) {
            logger.error(e);
        }
        // 0000 0011 1111 1111
        int flags = 0x03FF;
        printerStatus.setFlags(flags);
        shortStatus.setFlags(flags);
        longStatus.setFlags(flags);

        int mode = PrinterConst.ECRMODE_24NOTOVER;
        printerStatus.setMode(mode);
        longStatus.setMode(mode);
        shortStatus.setMode(mode);

        int submode = 0;
        printerStatus.setSubmode(submode);
        shortStatus.setSubmode(submode);
        longStatus.setSubmode(submode);

    }

    public boolean getCapFiscalStorage() {
        return true;
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

    public void addEvents(IPrinterEvents item) {
    }

    public void deviceExecute(PrinterCommand command) throws Exception {
    }

    public void connect() throws Exception {
    }

    public void check(int errorCode) throws Exception {
    }

    public void execute(PrinterCommand command) throws Exception {
    }

    public int getSysPassword() {
        return 30;
    }

    public int getUsrPassword() {
        return 1;
    }

    public int getTaxPassword() {
        return 0;
    }

    public boolean failed(int errorCode) {
        return errorCode != 0;
    }

    public boolean succeeded(int errorCode) {
        return errorCode == 0;
    }

    public int executeCommand(PrinterCommand command) throws Exception {
        return 0;
    }

    public void setTaxPassword(int taxPassword) {
    }

    public void setUsrPassword(int usrPassword) {
    }

    public void setSysPassword(int sysPassword) {
    }

    public Beep beep() throws Exception {
        return new Beep();
    }

    public int activateEJ() throws Exception {
        return 0;
    }

    public int printEJActivationReport() throws Exception {
        return 0;
    }

    public int initEJArchive() throws Exception {
        return 0;
    }

    public int testEJArchive() throws Exception {
        return 0;
    }

    public int closeEJArchive() throws Exception {
        return 0;
    }

    public int cancelEJDocument() throws Exception {
        return 0;
    }

    public int writeEJErrorCode(int errorCode) throws Exception {
        return 0;
    }

    public LongPrinterStatus readLongStatus() throws Exception {
        return longStatus;
    }

    public LongPrinterStatus getLongStatus() {
        return longStatus;
    }

    public ShortPrinterStatus getShortStatus() {
        return shortStatus;
    }

    public ShortPrinterStatus readShortStatus() throws Exception {
        return shortStatus;
    }

    public int printString(int station, String line) throws Exception {
        return 0;
    }

    public int printBoldString(int station, String line) throws Exception {
        return 0;
    }

    public void feedPaper(int station, int lineNumber) throws Exception {
    }

    public int printStringFont(int station, FontNumber font, String line)
            throws Exception {
        return 0;
    }

    public int printLine(int station, String line, FontNumber font)
            throws Exception {
        return 0;
    }

    public String[] splitText(String text, int n, boolean wrap)
            throws Exception {
        return new String[0];
    }

    public String[] splitText(String text, FontNumber font) throws Exception {
        return new String[0];
    }

    public void printText(int station, String text, FontNumber font)
            throws Exception {
    }

    public int updateFieldInfo(int tableNumber, int fieldNumber)
            throws Exception {
        return 0;
    }

    public int writeTable(int tableNumber, int rowNumber, int fieldNumber,
            String fieldValue) throws Exception {
        return 0;
    }

    public int readTable(int tableNumber, int rowNumber, int fieldNumber,
            String[] fieldValue) throws Exception {
        return 0;
    }

    public int readTableInfo(int tableNumber, Object[] out) throws Exception {
        return 0;
    }

    public ReadTableInfo readTableInfo(int tableNumber) throws Exception {
        return new ReadTableInfo();
    }

    public PrintCashIn printCashIn(long sum) throws Exception {
        return new PrintCashIn();
    }

    public PrintCashOut printCashOut(long sum) throws Exception {
        return new PrintCashOut();
    }

    public ContinuePrint continuePrint() throws Exception {
        return new ContinuePrint();
    }

    public BeginTest startTest(int runningPeriod) throws Exception {
        return new BeginTest();
    }

    public EndTest stopTest() throws Exception {
        return new EndTest();
    }

    public VoidFiscalReceipt cancelReceipt() throws Exception {
        return new VoidFiscalReceipt();
    }

    public VoidFiscalReceipt cancelReceipt(int password) throws Exception {
        return new VoidFiscalReceipt();
    }

    public EndFiscalReceipt closeReceipt(CloseRecParams params)
            throws Exception {
        return new EndFiscalReceipt();
    }

    public long getSubtotal() throws Exception {
        return 0;
    }

    public int readOperationRegister(OperationRegister register)
            throws Exception {
        return 0;
    }

    public int readOperationRegister(int number) throws Exception {
        return 0;
    }

    public int readCashRegister(CashRegister register) throws Exception {
        return 0;
    }

    public long readCashRegister(int number) throws Exception {
        return 0;
    }

    public PrintEJDayReportOnDates printEJDayReportOnDates(EJDate date1,
            EJDate date2, int reportType) throws Exception {
        return new PrintEJDayReportOnDates();
    }

    public PrintFMReportDates printFMReportDates(PrinterDate date1,
            PrinterDate date2, int reportType) throws Exception {
        return new PrintFMReportDates();
    }

    public PrintEJDayReportOnDays printEJReportDays(int day1, int day2,
            int reportType) throws Exception {
        return new PrintEJDayReportOnDays();
    }

    public PrintFMReportDays printFMReportDays(int day1, int day2,
            int reportType) throws Exception {
        return new PrintFMReportDays();
    }

    public void printSale(PriceItem item) throws Exception {
    }

    public void printVoidSale(PriceItem item) throws Exception {
    }

    public void printRefund(PriceItem item) throws Exception {
    }

    public void printVoidRefund(PriceItem item) throws Exception {
    }

    public PrintVoidItem printVoidItem(PriceItem item) throws Exception {
        return new PrintVoidItem();
    }

    public PrintDiscount printDiscount(AmountItem item) throws Exception {
        return new PrintDiscount();
    }

    public PrintVoidDiscount printVoidDiscount(AmountItem item)
            throws Exception {
        return new PrintVoidDiscount();
    }

    public PrintCharge printCharge(AmountItem item) throws Exception {
        return new PrintCharge();
    }

    public PrintVoidCharge printVoidCharge(AmountItem item) throws Exception {
        return new PrintVoidCharge();
    }

    public ReadFMLastRecordDate readFMLastRecordDate() throws Exception {
        return new ReadFMLastRecordDate();
    }

    public PrintXReport printXReport() throws Exception {
        return new PrintXReport();
    }

    public PrintZReport printZReport() throws Exception {
        return new PrintZReport();
    }

    public int printDepartmentReport() throws Exception {
        return 0;
    }

    public int printTaxReport() throws Exception {
        return 0;
    }

    public int printTotalizers() throws Exception {
        return 0;
    }

    public int writeDate(PrinterDate date) throws Exception {
        return 0;
    }

    public int confirmDate(PrinterDate date) throws Exception {
        return 0;
    }

    public void writeTime(PrinterTime time) throws Exception {
    }

    public void writePortParams(int portNumber, int baudRate, int timeout)
            throws Exception {
    }

    public void printBarcode(String barcode) throws Exception {
    }

    public void duplicateReceipt() throws Exception {
    }

    public OpenReceipt openReceipt(int receiptType) throws Exception {
        return new OpenReceipt();
    }

    public void loadGraphics(int lineNumber, byte[] data)
            throws Exception {
    }

    public void loadGraphics(int lineNumber, int lineCount, byte[] data)
            throws Exception {
    }

    public int loadGraphics1(int lineNumber, byte[] data) throws Exception {
        return 0;
    }

    public int loadGraphics2(int lineNumber, byte[] data) throws Exception {
        return 0;
    }

    public int printGraphics1(int line1, int line2) throws Exception {
        return 0;
    }

    public void endDump() throws Exception {
    }

    public int printGraphics2(int line1, int line2) throws Exception {
        return 0;
    }

    public int printGraphicLine(int height, byte[] data) throws Exception {
        return 0;
    }

    public int cutPaper(int cutType) throws Exception {
        return 0;
    }

    public void cutPaper() throws Exception {
    }

    public void openCashDrawer(int drawerNumber) throws Exception {
    }

    public boolean checkEcrMode(int mode) throws Exception {
        return true;
    }

    public PrinterStatus waitForPrinting() throws Exception {
        return printerStatus;
    }

    public int[] getSupportedBaudRates() throws Exception {
        return getModel().getSupportedBaudRates();
    }

    public boolean tryCancelReceipt(int password) throws Exception {
        return true;
    }

    public void writeDecimalPoint(int position) throws Exception {
    }

    public void resetFM() throws Exception {
    }

    public void sysAdminCancelReceipt() throws Exception {
    }

    public int getBaudRateIndex(int value) throws Exception {
        return 0;
    }

    public void setBaudRate(int baudRate) throws Exception {
    }

    public boolean connectDevice(int baudRate, int deviceBaudRate,
            int deviceByteTimeout) throws Exception {
        return true;
    }

    public void checkBaudRate(int value) throws Exception {
    }

    public void closePort() throws Exception {
    }

    public void writeTables(PrinterTables tables) throws Exception {
    }

    public void writeFields(PrinterFields fields) throws Exception {
    }

    public void updateTableInfo(int tableNumber) throws Exception {
    }

    public boolean isValidField(int tableNumber, int rowNumber, int fieldNumber)
            throws Exception {
        return true;
    }

    public void readTables(PrinterTables tables) throws Exception {
    }

    public PrinterStatus readShortPrinterStatus() throws Exception {
        return printerStatus;
    }

    public PrinterStatus readLongPrinterStatus() throws Exception {
        return printerStatus;
    }

    public PrinterStatus readPrinterStatus() throws Exception {
        return printerStatus;

    }

    public int readDeviceMetrics() throws Exception {
        return 0;
    }

    public DeviceMetrics getDeviceMetrics() {
        return deviceMetrics;
    }

    public PrinterModel getModel() throws Exception {
        return model;
    }

    public void initialize() throws Exception {
    }

    public boolean getWrapText() {
        return true;
    }

    public void setWrapText(boolean value) {
    }

    public void checkPaper(PrinterStatus status) throws Exception {
    }

    public int bufferZReport() throws Exception {
        return 0;
    }

    public int printBufferedZReport() throws Exception {
        return 0;
    }

    public int printTrailer() throws Exception {
        return 0;
    }

    public int printHeader() throws Exception {
        return 0;
    }

    public int initTables() throws Exception {
        return 0;
    }

    public void readTable(PrinterTable table) throws Exception {
    }

    public void writeField(PrinterField field) throws Exception {
    }

    public void readField(PrinterField field) throws Exception {
    }

    public void printGraphics(int line1, int line2) throws Exception {
    }

    public int stopEJPrint() throws Exception {
        return 0;
    }

    public int printEJDocument(int macNumber) throws Exception {
        return 0;
    }

    public int printEJDayReport(int dayNumber) throws Exception {
        return 0;
    }

    public int printEJDayTotal(int dayNumber) throws Exception {
        return 0;
    }

    public int readEJDayReport(int dayNumber) throws Exception {
        return 0;
    }

    public int readEJDayTotals(int dayNumber) throws Exception {
        return 0;
    }

    public void writeParameter(String paramName, int value) throws Exception {
    }

    public void writeParameter(String paramName, boolean value) throws Exception {
    }

    public void writeParameter(String paramName, String value) throws Exception {
    }

    public String readParameter(String paramName) throws Exception {
        return "";
    }

    public int readIntParameter(String paramName) throws Exception {
        return 0;
    }

    public void printBarcode(PrinterBarcode barcode) throws Exception {
    }

    public void sleep(long millis) {
    }

    public PrinterImages getPrinterImages() {
        return new PrinterImages();
    }

    public String processEscCommands(String text)
            throws Exception {
        return "";
    }

    public int getLineHeight(FontNumber font) throws Exception {
        return 0;
    }

    public int getLineSpacing() throws Exception {
        return 0;
    }

    public void checkImageSize(int firstLine, int imageWidth, int imageHeight)
            throws Exception {
    }

    public int readLicense(String[] license) throws Exception {
        return 0;
    }

    public void printSeparator(int separatorType, int height) throws Exception {
    }

    public int getPrintWidth() throws Exception {
        return 0;
    }

    public FlexCommands getCommands() throws Exception {
        return new FlexCommands();
    }

    public int getCommandTimeout(int code) throws Exception {
        return 0;
    }

    public int getResultCode() {
        return 0;
    }

    public String getResultText() {
        return "";
    }

    public ReadEJActivationReport readEJActivationReport() throws Exception {
        return new ReadEJActivationReport();
    }

    public ReadEJStatus readEJStatus() throws Exception {
        return new ReadEJStatus();
    }

    public String[] readEJActivationText(int maxCount) throws Exception {
        return new String[0];
    }

    public ReadFMTotals readFMTotals(int mode) throws Exception {
        return new ReadFMTotals();
    }

    public void setEscPrinter(NCR7167Printer escPrinter) {
    }

    public void beginFiscalDay() throws Exception {
    }

    public ReceiptImages getReceiptImages() {
        return null;
    }

    public void printImage(PrinterImage image) throws Exception {
    }

    public void printReceiptImage(int position) throws Exception {
    }

    public PrinterImage getPrinterImage(int position) throws Exception {
        return null;
    }

    public void loadImage(PrinterImage image, boolean addImage) throws Exception {
    }

    public void printBlankSpace(int height) throws Exception {
    }

    public void printText(String text) throws Exception {
    }

    public void waitForFiscalMemory() throws Exception {
    }

    public void waitForElectronicJournal() throws Exception {
    }

    public ReadCashRegister readCashRegister2(int number) throws Exception {
        return null;
    }

    public ReadOperationRegister readOperationRegister2(int number)
            throws Exception {
        return null;
    }

    public byte[] fsReadBlockData() throws Exception {
        return null;
    }

    public void fsWriteBlockData(byte[] data) throws Exception {
    }

    public String getErrorText(int code) throws Exception {
        return "";
    }

    public void openFiscalDay() throws Exception {
    }

    public int fsWriteTag(int tagId, String tagValue) throws Exception {
        return 0;
    }

    public int fsWriteTLV(byte[] tlv) throws Exception {
        return 0;
    }

    public FSReadStatus fsReadStatus() throws Exception {
        return null;
    }

    public FSReadDayParameters fsReadDayParameters() throws Exception {
        return null;
    }

    public boolean isFiscalized() throws Exception {
        return false;
    }

    public int readDayNumber() throws Exception {
        return 0;
    }

    public int readDocNumber() throws Exception {
        return 0;
    }

    public void writeCasierName(String name) throws Exception {
    }

    public void writeAdminName(String name) throws Exception {
    }

    public void disablePrint() throws Exception {
    }

    public FSReadFiscalization fsReadFiscalization() throws Exception {
        return null;
    }

    public FSReadCommStatus fsReadCommStatus() throws Exception {
        return null;
    }

    public void startSaveCommands() {
    }

    public void stopSaveCommands() {
    }

    public void clearReceiptCommands() {
    }

    public int printReceiptCommands() throws Exception {
        return 0;
    }
    
    public int fsReceiptDiscount(FSReceiptDiscount command) throws Exception{
        return 0;
    }

    public String getDepartmentName(int number) throws Exception{
        return "";
    }
    
    public String getTaxName(int number) throws Exception{
        return "";
    }
    
    public int getTaxRate(int number) throws Exception{
        return 0;
    }

    public int getDiscountMode() throws Exception{
        return 0;
    }
    
    public String readTable(int tableNumber, int rowNumber, int fieldNumber)
            throws Exception{
        return "";
    }
    
    public String readTable(String tableName, String fieldName) 
            throws Exception{
        return "";
    }
    
}
