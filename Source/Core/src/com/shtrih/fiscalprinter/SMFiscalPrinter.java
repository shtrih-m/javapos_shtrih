/*
 * SmFiscalPrinterInterface.java
 *
 * Created on 15 October 2010 Рі., 11:26
 *
 * To change this template, choose Tools | Template PrintVoidItemManager
 * and open the template in the editor.
 */
package com.shtrih.fiscalprinter;

/**
 *
 * @author V.Kravtsov
 */
import com.shtrih.ej.EJDate;
import com.shtrih.barcode.PrinterBarcode;
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
import com.shtrih.fiscalprinter.command.PrinterConst;
import com.shtrih.fiscalprinter.command.PrinterDate;
import com.shtrih.fiscalprinter.command.PrinterStatus;
import com.shtrih.fiscalprinter.command.PrinterTime;
import com.shtrih.fiscalprinter.command.ReadCashRegister;
import com.shtrih.fiscalprinter.command.ReadEJActivationReport;
import com.shtrih.fiscalprinter.command.ReadEJStatus;
import com.shtrih.fiscalprinter.command.ReadFMLastRecordDate;
import com.shtrih.fiscalprinter.command.ReadFMTotals;
import com.shtrih.fiscalprinter.command.ReadLongStatus;
import com.shtrih.fiscalprinter.command.ReadTableInfo;
import com.shtrih.fiscalprinter.command.ShortPrinterStatus;
import com.shtrih.fiscalprinter.command.VoidFiscalReceipt;
import com.shtrih.fiscalprinter.command.ReadOperationRegister;
import com.shtrih.fiscalprinter.model.PrinterModel;
import com.shtrih.fiscalprinter.table.PrinterField;
import com.shtrih.fiscalprinter.table.PrinterFields;
import com.shtrih.fiscalprinter.table.PrinterTable;
import com.shtrih.fiscalprinter.table.PrinterTables;
import com.shtrih.jpos.fiscalprinter.FptrParameters;
import com.shtrih.jpos.fiscalprinter.PrinterImages;
import com.shtrih.printer.ncr7167.NCR7167Printer;
import com.shtrih.jpos.fiscalprinter.PrinterImage;
import com.shtrih.jpos.fiscalprinter.PrinterImages;
import com.shtrih.jpos.fiscalprinter.ReceiptImages;

public interface SMFiscalPrinter {

    public boolean getCapFiscalStorage();

    public FptrParameters getParams();

    public PrinterProtocol getDevice();

    public void setDevice(PrinterProtocol device);

    public void addEvents(IPrinterEvents item);

    public void deviceExecute(PrinterCommand command) throws Exception;

    public void connect() throws Exception;

    public void check(int errorCode) throws Exception;

    public void execute(PrinterCommand command) throws Exception;

    public int getSysPassword();

    public int getUsrPassword();

    public int getTaxPassword();

    public boolean failed(int errorCode);

    public boolean succeeded(int errorCode);

    public int executeCommand(PrinterCommand command) throws Exception;

    public void setTaxPassword(int taxPassword);

    public void setUsrPassword(int usrPassword);

    public void setSysPassword(int sysPassword);

    public Beep beep() throws Exception;

    public int activateEJ() throws Exception;

    public int printEJActivationReport() throws Exception;

    public int initEJArchive() throws Exception;

    public int testEJArchive() throws Exception;

    public int closeEJArchive() throws Exception;

    public int cancelEJDocument() throws Exception;

    public int writeEJErrorCode(int errorCode) throws Exception;

    public LongPrinterStatus readLongStatus() throws Exception;

    public LongPrinterStatus getLongStatus() throws Exception;

    public ShortPrinterStatus getShortStatus();

    public ShortPrinterStatus readShortStatus() throws Exception;

    public int printString(int station, String line) throws Exception;

    public int printBoldString(int station, String line) throws Exception;

    public void feedPaper(int station, int lineNumber) throws Exception;

    public int printStringFont(int station, FontNumber font, String line)
            throws Exception;

    public int printLine(int station, String line, FontNumber font)
            throws Exception;

    public String[] splitText(String text, int n, boolean wrap)
            throws Exception;

    public String[] splitText(String text, FontNumber font) throws Exception;

    public void printText(int station, String text, FontNumber font)
            throws Exception;

    public void printText(String text) throws Exception;

    public int updateFieldInfo(int tableNumber, int fieldNumber)
            throws Exception;

    public int writeTable(int tableNumber, int rowNumber, int fieldNumber,
            String fieldValue) throws Exception;

    public String readTable(int tableNumber, int rowNumber, int fieldNumber)
            throws Exception;
    
    public String readTable(String tableName, String fieldName) 
            throws Exception;
    
    public int readTable(int tableNumber, int rowNumber, int fieldNumber,
            String[] fieldValue) throws Exception;

    public int readTableInfo(int tableNumber, Object[] out) throws Exception;

    public ReadTableInfo readTableInfo(int tableNumber) throws Exception;

    public PrintCashIn printCashIn(long sum) throws Exception;

    public PrintCashOut printCashOut(long sum) throws Exception;

    public ContinuePrint continuePrint() throws Exception;

    public BeginTest startTest(int runningPeriod) throws Exception;

    public EndTest stopTest() throws Exception;

    public VoidFiscalReceipt cancelReceipt() throws Exception;

    public VoidFiscalReceipt cancelReceipt(int password) throws Exception;

    public EndFiscalReceipt closeReceipt(CloseRecParams params)
            throws Exception;

    public long getSubtotal() throws Exception;

    public int readOperationRegister(OperationRegister register)
            throws Exception;

    public int readOperationRegister(int number) throws Exception;

    public int readCashRegister(CashRegister register) throws Exception;

    public long readCashRegister(int number) throws Exception;

    public PrintEJDayReportOnDates printEJDayReportOnDates(EJDate date1,
            EJDate date2, int reportType) throws Exception;

    public PrintFMReportDates printFMReportDates(PrinterDate date1,
            PrinterDate date2, int reportType) throws Exception;

    public PrintEJDayReportOnDays printEJReportDays(int day1, int day2,
            int reportType) throws Exception;

    public PrintFMReportDays printFMReportDays(int day1, int day2,
            int reportType) throws Exception;

    public void printSale(PriceItem item) throws Exception;

    public void printVoidSale(PriceItem item) throws Exception;

    public void printRefund(PriceItem item) throws Exception;

    public void printVoidRefund(PriceItem item) throws Exception;

    public PrintVoidItem printVoidItem(PriceItem item) throws Exception;

    public PrintDiscount printDiscount(AmountItem item) throws Exception;

    public PrintVoidDiscount printVoidDiscount(AmountItem item)
            throws Exception;

    public PrintCharge printCharge(AmountItem item) throws Exception;

    public PrintVoidCharge printVoidCharge(AmountItem item) throws Exception;

    public ReadFMLastRecordDate readFMLastRecordDate() throws Exception;

    public PrintXReport printXReport() throws Exception;

    public PrintZReport printZReport() throws Exception;

    public int printDepartmentReport() throws Exception;

    public int printTaxReport() throws Exception;

    public int printTotalizers() throws Exception;

    public int writeDate(PrinterDate date) throws Exception;

    public int confirmDate(PrinterDate date) throws Exception;

    public void writeTime(PrinterTime time) throws Exception;

    public void writePortParams(int portNumber, int baudRate, int timeout)
            throws Exception;

    public void printBarcode(String barcode) throws Exception;

    public void duplicateReceipt() throws Exception;

    public OpenReceipt openReceipt(int receiptType) throws Exception;

    public void loadGraphics(int lineNumber, int lineCount, byte[] data)
            throws Exception;

    public int loadGraphics1(int lineNumber, byte[] data) throws Exception;

    public int loadGraphics2(int lineNumber, byte[] data) throws Exception;

    public int printGraphics1(int line1, int line2) throws Exception;

    public void endDump() throws Exception;

    public int printGraphics2(int line1, int line2) throws Exception;

    public void printGraphics(int line1, int line2) throws Exception;

    public int printGraphicLine(int height, byte[] data) throws Exception;

    public int cutPaper(int cutType) throws Exception;

    public void cutPaper() throws Exception;

    public void openCashDrawer(int drawerNumber) throws Exception;

    public boolean checkEcrMode(int mode) throws Exception;

    public PrinterStatus waitForPrinting() throws Exception;

    public int[] getSupportedBaudRates() throws Exception;

    public boolean tryCancelReceipt(int password) throws Exception;

    public void writeDecimalPoint(int position) throws Exception;

    public void resetFM() throws Exception;

    public void sysAdminCancelReceipt() throws Exception;

    public int getBaudRateIndex(int value) throws Exception;

    public void setBaudRate(int baudRate) throws Exception;

    public boolean connectDevice(int baudRate, int deviceBaudRate,
            int deviceByteTimeout) throws Exception;

    public void checkBaudRate(int value) throws Exception;

    public void closePort() throws Exception;

    public void writeTables(PrinterTables tables) throws Exception;

    public void writeFields(PrinterFields fields) throws Exception;

    public void updateTableInfo(int tableNumber) throws Exception;

    public boolean isValidField(int tableNumber, int rowNumber, int fieldNumber)
            throws Exception;

    public void readTables(PrinterTables tables) throws Exception;

    public PrinterStatus readShortPrinterStatus() throws Exception;

    public PrinterStatus readLongPrinterStatus() throws Exception;

    public PrinterStatus readPrinterStatus() throws Exception;

    public int readDeviceMetrics() throws Exception;

    public DeviceMetrics getDeviceMetrics();

    public PrinterModel getModel() throws Exception;

    public void initialize() throws Exception;

    public boolean getWrapText();

    public void setWrapText(boolean value);

    public void checkPaper(PrinterStatus status) throws Exception;

    public int bufferZReport() throws Exception;

    public int printBufferedZReport() throws Exception;

    public int printTrailer() throws Exception;

    public int printHeader() throws Exception;

    public int initTables() throws Exception;

    public void readTable(PrinterTable table) throws Exception;

    public void writeField(PrinterField field) throws Exception;

    public void readField(PrinterField field) throws Exception;

    public int stopEJPrint() throws Exception;

    public int printEJDocument(int macNumber) throws Exception;

    public int printEJDayReport(int dayNumber) throws Exception;

    public int printEJDayTotal(int dayNumber) throws Exception;

    public int readEJDayReport(int dayNumber) throws Exception;

    public int readEJDayTotals(int dayNumber) throws Exception;

    public void writeParameter(String paramName, int value) throws Exception;

    public void writeParameter(String paramName, boolean value) throws Exception;

    public void writeParameter(String paramName, String value) throws Exception;

    public String readParameter(String paramName) throws Exception;

    public int readIntParameter(String paramName) throws Exception;

    public void printBarcode(PrinterBarcode barcode) throws Exception;

    public void sleep(long millis);

    public PrinterImages getPrinterImages();

    public String processEscCommands(String text)
            throws Exception;

    public int getLineHeight(FontNumber font) throws Exception;

    public int getLineSpacing() throws Exception;

    public void checkImageSize(int firstLine, int imageWidth, int imageHeight)
            throws Exception;

    public int readLicense(String[] license) throws Exception;

    public void printSeparator(int separatorType, int height) throws Exception;

    public int getPrintWidth() throws Exception;

    public FlexCommands getCommands() throws Exception;

    public int getCommandTimeout(int code) throws Exception;

    public int getResultCode();

    public String getResultText();

    public ReadEJActivationReport readEJActivationReport() throws Exception;

    public ReadEJStatus readEJStatus() throws Exception;

    public String[] readEJActivationText(int maxCount) throws Exception;

    public ReadFMTotals readFMTotals(int mode) throws Exception;

    public void setEscPrinter(NCR7167Printer escPrinter);

    public void beginFiscalDay() throws Exception;

    public ReceiptImages getReceiptImages();

    public void printReceiptImage(int position) throws Exception;

    public PrinterImage getPrinterImage(int position) throws Exception;

    public void printImage(PrinterImage image) throws Exception;

    public void loadImage(PrinterImage image, boolean addImage) throws Exception;

    public void printBlankSpace(int height) throws Exception;

    public void waitForFiscalMemory() throws Exception;

    public void waitForElectronicJournal() throws Exception;

    public ReadCashRegister readCashRegister2(int number) throws Exception;

    public ReadOperationRegister readOperationRegister2(int number)
            throws Exception;

    public String getErrorText(int code) throws Exception;

    public void openFiscalDay() throws Exception;

    public int fsWriteTag(int tagId, String tagValue) throws Exception;

    public int fsWriteTLV(byte[] tlv) throws Exception;

    public FSReadStatus fsReadStatus() throws Exception;
    
    public FSReadDayParameters fsReadDayParameters() throws Exception;
    
    public boolean isFiscalized() throws Exception;

    public int readDayNumber() throws Exception;

    public int readDocNumber() throws Exception;

    public void writeCasierName(String name) throws Exception;

    public void writeAdminName(String name) throws Exception;
    
    public void disablePrint() throws Exception;
    
    public FSReadFiscalization fsReadFiscalization() throws Exception;
    
    public FSReadCommStatus fsReadCommStatus() throws Exception;    
    
    public void startSaveCommands();
    
    public void stopSaveCommands();
    
    public void clearReceiptCommands();
    
    public int printReceiptCommands() throws Exception;
    
    public int fsReceiptDiscount(FSReceiptDiscount command) throws Exception;
    
    public String getDepartmentName(int number) throws Exception;
    
    public String getTaxName(int number) throws Exception;
    
    public int getTaxRate(int number) throws Exception;
   
    public int getDiscountMode() throws Exception;
}

