/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.fiscalprinter;

import com.shtrih.barcode.PrinterBarcode;
import com.shtrih.fiscalprinter.command.*;
import com.shtrih.fiscalprinter.request.*;
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
import com.shtrih.util.CompositeLogger;
import com.shtrih.jpos.fiscalprinter.PrinterImage;
import com.shtrih.jpos.fiscalprinter.ReceiptImages;
import com.shtrih.util.BitUtils;
import com.shtrih.jpos.fiscalprinter.PrintItem;
import com.shtrih.jpos.fiscalprinter.receipt.FSSaleReceiptItem;

import java.util.List;
import java.util.Vector;

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

    @Override
    public void removeEvents(IPrinterEvents item) {

    }

    public void deviceExecute(PrinterCommand command) throws Exception {
    }

    public LongPrinterStatus connect() throws Exception {
        return null;
    }

    public void disconnect() {
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

    public int continuePrint() throws Exception {
        return 0;
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

    public int closeReceipt(EndFiscalReceipt command)
            throws Exception {
        return 0;
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

    public long readCashRegisterCorrection(int number) throws Exception {
        return 0;
    }

    public PrintEJDayReportOnDates printEJDayReportOnDates(PrinterDate date1,
            PrinterDate date2, int reportType) throws Exception {
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

    public int writeTime(PrinterTime time) throws Exception {
        return 0;
    }

    public int writePortParams(int portNumber, int baudRate, int timeout)
            throws Exception {
        return 0;
    }

    public void printBarcode(String barcode) throws Exception {
    }

    public void duplicateReceipt() throws Exception {
    }

    public void openReceipt(int receiptType) throws Exception {
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

    @Override
    public int getMaxGraphicsWidth() throws Exception {
        return 0;
    }

    @Override
    public int loadRawGraphics(byte[][] data) throws Exception {
        return 0;
    }

    public int printGraphicLine(int station, int height, byte[] data) throws Exception {
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

    public int hardReset() throws Exception {
        return 0;
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

    public PrinterTable getTable(int tableNumber) throws Exception {
        return null;
    }

    public boolean isValidField(PrinterField field)
            throws Exception {
        return true;
    }

    public PrinterTables readTables() throws Exception {
        return new PrinterTables();
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

    public int writeField(PrinterField field) throws Exception {
        return 0;
    }

    public void writeField2(PrinterField field) throws Exception {
    }

    public PrinterField readField(PrinterField field) throws Exception {
        return field;
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

    public FMTotals readFMTotals(int mode) throws Exception {
        return new FMTotals();
    }

    public FMTotals readFPTotals(int mode) throws Exception {
        return new FMTotals();
    }

    public FMTotals readFSTotals() throws Exception {
        return new FMTotals();
    }

    public void setEscPrinter(NCR7167Printer escPrinter) {
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

    @Override
    public String readRnm() throws Exception {
        return null;
    }

    public String getErrorText(int code) throws Exception {
        return "";
    }

    public void openFiscalDay() throws Exception {
    }

    public void fsWriteTag(int tagId, String tagValue) throws Exception {

    }

    public void fsWriteTLV(byte[] tlv) throws Exception {

    }

    public int fsWriteOperationTLV(byte[] tlv) throws Exception {
        return 0;
    }

    public byte[] getTLVData(int tagId, String tagValue) throws Exception {
        return null;
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

    public void disablePrintOnce() throws Exception {
    }
    
    public void enablePrint() throws Exception {
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

    public int fsReceiptDiscount(FSReceiptDiscount command) throws Exception {
        return 0;
    }

    public String getDepartmentName(int number) throws Exception {
        return "";
    }

    public String getTaxName(int number) throws Exception {
        return "";
    }

    public int getTaxRate(int number) throws Exception {
        return 0;
    }

    public int getDiscountMode() throws Exception {
        return 0;
    }

    public String readTable(int tableNumber, int rowNumber, int fieldNumber)
            throws Exception {
        return "";
    }

    public String readTable(String tableName, String fieldName)
            throws Exception {
        return "";
    }

    public int printDocHeader(String title, int number) throws Exception {
        return 0;
    }

    public void printLines(String line1, String line2) throws Exception {
    }

    public void printLines(String line1, String line2, FontNumber font) throws Exception {
    }

    public void printItems(List<PrintItem> items) throws Exception {
    }

    public void printFSHeader() throws Exception {
    }

    public boolean getCapOpenReceipt() throws Exception {
        return true;
    }

    public FSReadSerial fsReadSerial() throws Exception {
        return null;
    }

    @Override
    public String getFullSerial(){
        return null;
    }

    @Override
    public String readFullSerial() throws Exception {
        return null;
    }

    public FSReadExpDate fsReadExpDate() throws Exception {
        return null;
    }

    public void checkDiscountMode(int mode) throws Exception {
    }

    public void printReceiptHeader(String docName) throws Exception {
    }

    public String getReceiptName(int receiptType) {
        return "";
    }

    public boolean getCapFSCloseReceipt() {
        return false;
    }

    public List<FSTicket> fsReadTickets(int[] numbers) throws Exception {
        return null;
    }

    public List<FSTicket> fsReadTickets(int firstFSDocumentNumber, int documentCount) throws Exception {
        return null;
    }

    public boolean getCapDisableDiscountText() {
        return false;
    }

    public int getMessageLength() throws Exception {
        return 36;
    }

    public int getMessageLength(FontNumber font) throws Exception {
        return 36;
    }

    public boolean isSubtotalInHeader() {
        return false;
    }

    public boolean isDiscountInHeader() {
        return false;
    }

    public int reboot() throws Exception {
        return 0;
    }

    public FDOParameters readFDOParameters() throws Exception {
        return null;
    }

    public FDOParameters getFDOParameters() throws Exception {
        return null;
    }

    public int getHeaderHeight() throws Exception {
        return 88;
    }

    public boolean getCapDiscount() {
        return true;
    }

    @Override
    public FSFindDocument fsFindDocument(long docNumber) throws Exception {
        return null;
    }

    @Override
    public boolean capFDOSupport(){
        return false;
    }

    @Override
    public int getMaxGraphicsLineWidth() throws Exception {
        return 0;
    }

    public void setIsFooter(boolean value) {
    }

    public boolean isCapFooterFlag() {
        return false;
    }

    @Override
    public FSReadDocument fsRequestDocumentTLV(int documentNumber) throws Exception {
        return null;
    }

    @Override
    public byte[] fsReadDocumentTLVBlock() throws Exception {
        return new byte[0];
    }

    public DocumentTLV fsReadDocumentTLV(int docNumber) throws Exception {
        return null;
    }

    public Vector<String> fsReadDocumentTLVAsText(int docNumber) throws Exception {
        return null;
    }

    @Override
    public PrinterModelParameters readPrinterModelParameters() throws Exception {
        return null;
    }

    @Override
    public FSRequestFiscalizationTag fsRequestFiscalizationTag(int fiscId, int tagId) throws Exception {
        return null;
    }

    @Override
    public byte[] fsReadFiscalizationTag(int fiscId, int tagId) throws Exception {
        return null;
    }

    public FSDocument fsFindLastDocument(int docType) throws Exception {
        return null;
    }

    public boolean getCapSetVatTable() {
        return true;
    }

    public void clearTableText() throws Exception {
    }

    public void updateTableText() throws Exception{
    }

    public void updateFirmware(String firmwareFileName) throws Exception {
    }

    public boolean getCapUpdateFirmware() throws Exception {
        return true;
    }

    public LongPrinterStatus searchDevice() throws Exception {
        return null;
    }

    public int compareFirmwareVersion(String firmwareFileName) throws Exception {
        return 0;
    }

    public int readTotalizers(int recType, long[] totalizers) throws Exception {
        return 0;
    }

    public long[] readTotalizers(int recType) throws Exception {
        return null;
    }

    public String[] readEJDocument(int documentNumber) throws Exception {
        return null;
    }

    @Override
    public void writeFirmwareBlockToSDCard(int fileType, int blockNumber, byte[] block) throws Exception {

    }

    @Override
    public boolean isDesktop() {
        return false;
    }

    @Override
    public boolean isCashCore() {
        return false;
    }

    @Override
    public boolean isShtrihMobile() {
        return false;
    }

    @Override
    public boolean isShtrihNano() {
        return false;
    }

    @Override
    public boolean isSDCardPresent() throws Exception {
        return false;
    }

    public int getNumHeaderLines() throws Exception {
        return 3;
    }

    public int getNumTrailerLines() throws Exception {
        return 3;
    }

    public int getHeaderTableRow() throws Exception {
        return 12;
    }

    public int printDocEnd() throws Exception {
        return 0;
    }

    public int fsStartDayClose() throws Exception {
        return 0;
    }

    public int fsStartDayOpen() throws Exception {
        return 0;
    }

    public int fsStartFiscalization(int reportType) throws Exception {
        return 0;
    }

    public int fsStartCorrectionReceipt() throws Exception {
        return 0;
    }

    public int fsStartCalcReport() throws Exception {
        return 0;
    }

    public int fsStartFiscalClose() throws Exception {
        return 0;
    }

    public int fsPrintCorrectionReceipt(FSPrintCorrectionReceipt command) throws Exception {
        return 0;
    }

    public int fsPrintCorrectionReceipt2(FSPrintCorrectionReceipt2 command) throws Exception {
        return 0;
    }

    public boolean isDayClosed() throws Exception {
        return false;
    }

    public boolean getCapOpenFiscalDay() {
        return false;
    }

    public int sendItemCode(byte[] data) throws Exception {
        return 0;
    }

    public void checkItemCode(CheckCodeRequest request) throws Exception {
    }

    public int fsBindMC(FSBindMC command) throws Exception {
        return 0;
    }

    public FSAcceptMC fsAcceptItemCode(int action) throws Exception {
        return null;
    }

    public int fsReadKMServerStatus(FSReadMCNotificationStatus command) throws Exception {
        return 0;
    }

    public FSBindMC bindItemCode(String barcode) throws Exception {
        return null;
    }

    public int fsCloseReceipt(FSCloseReceipt command) throws Exception {
        return 0;
    }

    public void processTLVBeforeReceipt(byte[] tlv) throws Exception {
    }

    public void resetPrinter() throws Exception {
    }

    public boolean getCapOperationTagsFirst() {
        return false;
    }

    public long getLastDocNum() {
        return 0;
    }

    public long getLastDocMac() {
        return 0;
    }

    public PrinterDate getLastDocDate() {
        return null;
    }

    public PrinterTime getLastDocTime() {
        return null;
    }

    public long getLastDocTotal() {
        return 0;
    }

    public void cancelWait() {
    }

    public void rebootAndWait() throws Exception {
    }

    public void confirmNotifications(MCNotifications items) throws Exception {
    }

    public MCNotifications readNotifications() throws Exception {
        return null;
    }

    public int startReadMCNotifications(StartReadMCNotifications command) throws Exception {
        return 0;
    }

    public int readMCNotification(ReadMCNotification command) throws Exception {
        return 0;
    }

    public int fsCheckMC(FSCheckMC command) throws Exception {
        return 0;
    }

    public int fsAcceptMC(FSAcceptMC command) throws Exception {
        return 0;
    }

    public int fsSyncRegisters(FSSyncRegisters command) throws Exception {
        return 0;
    }

    public int fsReadMemorySize(FSReadMemorySize command) throws Exception {
        return 0;
    }

    public int fsWriteTLVBuffer(FSWriteTLVBuffer command) throws Exception {
        return 0;
    }

    public int fsReadRandomData(FSReadRandomData command) throws Exception {
        return 0;
    }

    public int fsAuthorize(FSAuthorize command) throws Exception {
        return 0;
    }

    public int fsReadMCStatus(FSReadMCStatus command) throws Exception {
        return 0;
    }

    public int mcClearBuffer() throws Exception {
        return 0;
    }

    public byte[] fsReadDocumentTLVToEnd() throws Exception {
        return null;
    }

    public Object getSyncObject() throws Exception {
        return null;
    }

    public int getCommandTimeout(int code){
        return 0;
    }
    
    public void setCommandTimeout(int code, int timeout){
    }
    
    public boolean getCapCutter(){
        return true;
    }

    public long getTaxAmount(int tax, long amount) throws Exception{
        return 0;
    }
    
    public int getPrintMode(){
        return 0;
    }
    
    public void setPrintMode(int value){
    }
    
    public void sendFDODocuments() throws Exception{
    }
    
    public void writeTLVItems() throws Exception{
    }
}
