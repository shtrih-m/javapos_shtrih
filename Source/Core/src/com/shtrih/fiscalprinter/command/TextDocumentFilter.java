package com.shtrih.fiscalprinter.command;

import com.shtrih.fiscalprinter.FontNumber;
import com.shtrih.fiscalprinter.SMFiscalPrinter;
import com.shtrih.fiscalprinter.skl.FileSKLStorage;
import com.shtrih.fiscalprinter.skl.SKLStorage;
import com.shtrih.fiscalprinter.skl.SKLWriter;
import com.shtrih.jpos.fiscalprinter.PrinterHeader;
import com.shtrih.util.BitUtils;
import com.shtrih.util.CompositeLogger;
import com.shtrih.util.MathUtils;
import com.shtrih.util.StringUtils;
import com.shtrih.util.SysUtils;
import java.io.BufferedReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.io.File;
import java.io.FileReader;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class TextDocumentFilter implements IPrinterEvents {
    
    private static CompositeLogger logger = CompositeLogger.getLogger(TextDocumentFilter.class);
    
    private int lineLength;
    private boolean enabled = false;
    private String deviceName = "ККМ";
    private int operatorNumber = 1;
    private LongPrinterStatus status = null;
    private long receiptTotal = 0;
    private boolean receiptOpened = false;
    private boolean isFiscal = false;
    private boolean isEJPresent = false;
    private boolean isDocumentPrinted = false;
    private boolean connected = false;
    private SMFiscalPrinter printer;
    private boolean saveToStorage;
    private XReport report = new XReport();
    private final String[] paymentNames = new String[16];
    private final List<Operator> operators = new ArrayList<Operator>();
    private SKLStorage storage;
    private SKLStorage lastDoc;
    
    private static String SFiscalSign = "ФП";
    private static String SSaleText = "ПРОДАЖА";
    private static String SBuyText = "ПОКУПКА";
    private static String SRetSaleText = "ВОЗВРАТ ПРОДАЖИ";
    private static String SRetBuyText = "ВОЗВРАТ ПОКУПКИ";
    private static String SCashInText = "ВНЕСЕНИЕ";
    private static String SCashOutText = "ВЫПЛАТА";
    private static String SStornoText = "СТОРНО";
    private static String STotalText = "ВСЕГО";
    private static String SRoundingText = "ОКРУГЛЕНИЕ";
    private static String SDiscountText = "СКИДКА";
    private static String SChargeText = "НАДБАВКА";
    private static String SReceiptTotal = "ИТОГ";
    private static String SChangeText = "СДАЧА";
    private static String SReceiptCancelled = "ЧЕК АННУЛИРОВАН";
    private static String SDiscountStornoText = "СТОРНО СКИДКИ";
    private static String SChargeStornoText = "СТОРНО НАДБАВКИ";
    private static String SXReportText = "СУТОЧНЫЙ ОТЧЕТ БЕЗ ГАШЕНИЯ";
    public static final String SZReportText = "СУТОЧНЫЙ ОТЧЕТ С ГАШЕНИЕМ";
    private static String SDayClosed = "СМЕНА ЗАКРЫТА";
    public static final String SINN = "ИНН";
    private static String[] docNames = {SSaleText, SBuyText, SRetSaleText, SRetBuyText};
    private boolean documentActive = false;
    private final List<String> lines = new ArrayList<String>();
    
    
    public TextDocumentFilter() {
    }

    public void init(SMFiscalPrinter printer) throws Exception
    {
        this.printer = printer;
        saveToStorage = printer.getParams().textReportEnabled;
        lineLength = printer.getMessageLength(FontNumber.getNormalFont());
    }

    public boolean getEnabled() {
        return enabled;
    }
    
    public void setEnabled(boolean value) {
        this.enabled = value;
    }
    
    @Override
    public void beforeCommand(PrinterCommand command) throws Exception {
        if (printer == null) {
            return;
        }

        if (!enabled) {
            return;
        }
        if (command.isFailed()) {
            return;
        }
        
        try {
            enabled = false;
            connect();
            switch (command.getCode()) {
                case 0x85:
                case 0xFF45:
                    receiptTotal = printer.getSubtotal();
                    break;
                
                case 0x40:
                case 0x41:
                    report = readXReport();
                    break;
                
            }
        } finally {
            enabled = true;
        }
    }
    
    @Override
    public void afterCommand(PrinterCommand command) throws Exception {
        if (printer == null) {
            return;
        }

        if (!enabled) {
            return;
        }
        if (command.isFailed()) {
            return;
        }
        try {
            enabled = false;
            switch (command.getCode()) 
            {
                case 0x17:
                    if (documentActive){
                        add(((PrintString) command).getLine());
                    }
                    break;
                    
                case 0x2F:
                    if (documentActive){
                    add(((PrintStringFont) command).getLine());
                    }
                    break;
                    
                case 0x40:
                    printXReport((PrintXReport) command);
                    break;
                
                case 0x41:
                    printZReport((PrintZReport) command);
                    break;
                
                case 0x50:
                    printCashIn((PrintCashIn) command);
                    break;
                
                case 0x51:
                    printCashOut((PrintCashOut) command);
                    break;
                
                case 0xFF46:
                    printSale((FSPrintRecItem) command);
                    break;
                
                case 0x80:
                    printSale((PrintSale) command);
                    break;
                
                case 0x81:
                    printRefund((PrintRefund) command);
                    break;
                
                case 0x82:
                    printVoidSale((PrintVoidSale) command);
                    break;
                
                case 0x83:
                    printVoidRefund((PrintVoidRefund) command);
                    break;
                
                case 0x84:
                    printVoidItem((PrintVoidItem) command);
                    break;
                
                case 0x85:
                    endFiscalReceipt((EndFiscalReceipt) command);
                    break;
                
                case 0xFF45:
                    endFiscalReceipt((FSCloseReceipt) command);
                    break;
                
                case 0x86:
                    printDiscount((PrintDiscount) command);
                    break;
                
                case 0x87:
                    printCharge((PrintCharge) command);
                    break;
                
                case 0x88:
                    cancelReceipt();
                    break;
                
                case 0x8A:
                    printVoidCharge((PrintVoidCharge) command);
                    break;
                
                case 0x8B:
                    printVoidDiscount((PrintVoidDiscount) command);
                    break;
                
                case 0x8D:
                    openReceipt((OpenReceipt) command);
                    break;
            }
        } finally 
        {
            enabled = true;
        }
    }
    
    private void openReceipt(OpenReceipt command) throws Exception {
        openReceipt2(command.getReceiptType());
    }
    
    private void openReceipt2(int receiptType) throws Exception {
        if (!receiptOpened) {
            receiptOpened = true;
            isDocumentPrinted = false;
            beginDocument();
            long recNumber = getNextRecNumber(getRecNumber(receiptType));
            String docName = printer.getReceiptName(receiptType);
            add(docName, String.format("№%04d", recNumber));
        }
    }
    
    private long getNextRecNumber(long recNumber) throws Exception {
        recNumber = recNumber + 1;
        if (recNumber > 9999) {
            recNumber = 1;
        }
        return recNumber;
    }
    
    private long getRecNumber(int docType) throws Exception {
        switch (docType) {
            case PrinterConst.SMFP_RECTYPE_SALE:
                return printer.readOperationRegister(148);
            
            case PrinterConst.SMFP_RECTYPE_BUY:
                return printer.readOperationRegister(149);
            
            case PrinterConst.SMFP_RECTYPE_RETSALE:
                return printer.readOperationRegister(150);
            
            case PrinterConst.SMFP_RECTYPE_RETBUY:
                return printer.readOperationRegister(151);
            default: {
                return 0;
            }
        }
    }
    
    private void cancelReceipt() throws Exception {
        receiptOpened = false;
        add(SReceiptCancelled);
        endDocument();
    }
    
    private void printDiscount(PrintDiscount command) throws Exception {
        add(command.getItem().getText());
        add(SDiscountText, summToStr(command.getItem().getAmount()));
    }
    
    private void printVoidDiscount(PrintVoidDiscount command) throws Exception {
        add(command.getItem().getText());
        add(SDiscountStornoText, summToStr(command.getItem().getAmount()));
    }
    
    private void printCharge(PrintCharge command) throws Exception {
        add(command.getItem().getText());
        add(SChargeText, summToStr(command.getItem().getAmount()));
    }
    
    private void printVoidCharge(PrintVoidCharge command) throws Exception {
        add(command.getItem().getText());
        add(SChargeStornoText, summToStr(command.getItem().getAmount()));
    }
    
    private void endFiscalReceipt(EndFiscalReceipt command) throws Exception {
        CloseRecParams params = command.getParams();
        receiptOpened = false;
        
        add(params.getText());
        
        if (params.getDiscount() > 0) {
            // ВСЕГО
            add(STotalText, summToStr(receiptTotal));
            // СКИДКА
            long discountAmount = Math.round(receiptTotal * params.getDiscount() / 100.0);
            String line = String.format("%s %s %%", SDiscountText, amountToStr(params.getDiscount()));
            add(line, summToStr(discountAmount)
                    + getTaxData(params.getTax1(), params.getTax2(), params.getTax3(), params.getTax4()));
            
            receiptTotal = receiptTotal - discountAmount;
        }
        // TOTAL =123.34
        add(SReceiptTotal, summToStr(receiptTotal));
        // Payments
        if (params.getSum1() > 0) {
            add(getPaymentName(0), summToStr(params.getSum1()));
        }
        if (params.getSum2() > 0) {
            add(getPaymentName(1), summToStr(params.getSum2()));
        }
        if (params.getSum3() > 0) {
            add(getPaymentName(2), summToStr(params.getSum3()));
        }
        if (params.getSum4() > 0) {
            add(getPaymentName(3), summToStr(params.getSum4()));
        }
        // Change
        if (command.getChange() > 0) {
            add(SChangeText, summToStr(command.getChange()));
        }
        
        addFiscalSign();
        readEJReport(true);
        endDocument();
    }
    
    private void endFiscalReceipt(FSCloseReceipt command) throws Exception {
        FSCloseReceipt params = command;
        receiptOpened = false;
        
        add(params.getPrintableText());
        
        if (params.getDiscount() > 0) {
            // ВСЕГО
            add(STotalText, summToStr(receiptTotal));
            // ОКРУГЛЕНИЕ
            add(SRoundingText, summToStr(command.getDiscount()));
            
            receiptTotal = receiptTotal - command.getDiscount();
        }

        // TOTAL =123.34
        add(SReceiptTotal, summToStr(receiptTotal));

        // Payments
        long[] payments = params.getPayments();
        for (int i = 0; i < payments.length; i++) {
            if (payments[i] > 0) {
                add(getPaymentName(i), summToStr(payments[i]));
            }
        }

        // Change
        if (command.getChange() > 0) {
            add(SChangeText, summToStr(command.getChange()));
        }
        
        addFiscalSign();
        readEJReport(true);
        endDocument();
    }
    
    private String getPaymentName(int paymentIndex) throws Exception
    {
        String paymentName = paymentNames[paymentIndex];
        if (paymentName == null)
        {
            paymentName = "";
            String[] fieldValue = new String[1];
            if (printer.readTable(PrinterConst.SMFP_TABLE_PAYTYPE, paymentIndex + 1, 1, fieldValue) == 0)
            {
                paymentName = fieldValue[0];
            }
            paymentNames[paymentIndex] = paymentName;
        }
        return paymentName;
    }
    
    private void printSale(PrintSale command) throws Exception {
        operatorNumber = command.getOperator();
        openReceipt2(PrinterConst.SMFP_RECTYPE_SALE);
        printReceiptItem(command.getItem());
    }
    
    private void printSale(FSPrintRecItem command) throws Exception {
        printReceiptItem(command.getItem());
    }
    
    private void printRefund(PrintRefund command) throws Exception {
        operatorNumber = command.getOperator();
        openReceipt2(PrinterConst.SMFP_RECTYPE_BUY);
        printReceiptItem(command.getItem());
    }
    
    private void printVoidSale(PrintVoidSale command) throws Exception {
        operatorNumber = command.getOperator();
        openReceipt2(PrinterConst.SMFP_RECTYPE_RETSALE);
        printReceiptItem(command.getItem());
    }
    
    private void printVoidRefund(PrintVoidRefund command) throws Exception {
        operatorNumber = command.getOperator();
        openReceipt2(PrinterConst.SMFP_RECTYPE_RETBUY);
        printReceiptItem(command.getItem());
    }
    
    private void printVoidItem(PrintVoidItem command) throws Exception {
        operatorNumber = command.getOperator();
        PriceItem item = command.getItem();
        // Line 1
        add(item.getText());
        // Line 2
        String line = String.format("%s X %s", quantityToStr(item.getQuantity()), amountToStr(item.getAmount()));
        add(SStornoText, line);
        // Line 3
        line = summToStr(item.getAmount(), item.getQuantity());
        add(String.format("%02d", item.getDepartment()), line);
    }
    
    private String amountToStr(long value) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
        symbols.setDecimalSeparator('.');
        DecimalFormat formatter = new DecimalFormat("0.00", symbols);
        return formatter.format(value / 100.0);
    }
    
    public static String quantityToStr(double value) 
    {
        DecimalFormat formatter;
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
        symbols.setDecimalSeparator('.');
        
        double fpart = value * 1000.0 - Math.floor(value * 1000.0);
        if (fpart > 0.001)
        {
            formatter = new DecimalFormat("0.000000", symbols);
        } else{
            formatter = new DecimalFormat("0.000", symbols);
        }
        return formatter.format(value);
    }
    
    public String getTaxData(int tax1, int tax2, int tax3, int tax4) throws Exception {
        String result = "";
        String taxLetters = " АБВГДЕ";
        if (tax1 > 0) {
            result += taxLetters.charAt(tax1);
        }
        if (tax2 > 0) {
            result += taxLetters.charAt(tax2);
        }
        if (tax3 > 0) {
            result += taxLetters.charAt(tax3);
        }
        if (tax4 > 0) {
            result += taxLetters.charAt(tax4);
        }
        if (result.length() > 0) {
            result = "_" + result;
        }
        return result;
    }
    
    private String summToStr(long price, double quantity) {
        return summToStr(Math.round(quantity * price));
    }
    
    private String summToStr(long amount) {
        return "=" + amountToStr(amount);
    }
    
    private void printReceiptItem(PriceItem item) throws Exception {
        String line = "";
        add(item.getText());
        if (item.getQuantity() != 1.0) {
            line = String.format("%s X %s", quantityToStr(item.getQuantity()), amountToStr(item.getPrice()));
            add("", line);
            
            line = summToStr(item.getPrice(), item.getQuantity())
                    + getTaxData(item.getTax1(), item.getTax2(), item.getTax3(), item.getTax4());
            add(String.format("%02d", item.getDepartment()), line);
        } else {
            long amount = Math.round(item.getQuantity() * item.getPrice());
            line = "=" + amountToStr(amount)
                    + getTaxData(item.getTax1(), item.getTax2(), item.getTax3(), item.getTax4());
            add(String.format("%02d", item.getDepartment()), line);
        }
    }
    
    private void printReceiptItem(FSReceiptItem item) throws Exception {
        String line = "";
        add(item.getText());
        
        line = String.format("%s X %s", quantityToStr(item.getQuantity() / 1000000.0), amountToStr(item.getPrice()));
        add("", line);
        
        line = summToStr(item.getPrice(), item.getQuantity() / 1000000.0)
                + getTaxData(taxBitsToInt(item.getTax()), 0, 0, 0);
        add(String.format("%02d", item.getDepartment()), line);
    }
    
    private int taxBitsToInt(int tax) {
        for (int i = 0; i < 6; i++) {
            if (BitUtils.testBit(tax, i)) {
                return i + 1;
            }
        }
        
        return 0;
    }
    
    public class Operator {
        
        private final int number;
        private final String name;
        
        public Operator(int number, String name) {
            this.number = number;
            this.name = name;
        }
        
        public int getNumber() {
            return number;
        }
        
        public String getName() {
            return name;
        }
        
    }
    
    public class Payment {
        
        public long amount;
        public String text;
    }
    
    public class Cash {
        
        public long number;
        public long count;
        public long total;
    }
    
    public class Receipt {
        
        public Receipt() {
            for (int i = 0; i < 4; i++) {
                payments.add(new Payment());
            }
        }
        
        public long number;
        public long count;
        public long total;
        public List<Payment> payments = new ArrayList<Payment>();
    }
    
    public class VoidReceipt {
        
        public long count;
        public long total;
    }
    
    public class XReport {
        
        public XReport() {
            for (int i = 0; i < 4; i++) {
                receipts.add(new Receipt());
            }
            for (int i = 0; i < 4; i++) {
                voidedReceipts.add(new VoidReceipt());
            }
        }
        
        public long salesAmountBefore;
        public long buyAmountBefore;
        public long xReportNumber;
        public long zReportNumber;
        public List<Receipt> receipts = new ArrayList<Receipt>();
        public List<VoidReceipt> voidedReceipts = new ArrayList<VoidReceipt>();
        public Cash cashIn = new Cash();
        public Cash cashOut = new Cash();
        public long voidCount;
        public long dayVoidCount;
        public long cashInDrawer;
        public long income;
        public long salesAmount;
        public long buyAmount;
    }
    
    public void connect() throws Exception {
        if (!connected) {
            printer.check(printer.readDeviceMetrics()); //!
            deviceName = printer.getDeviceMetrics().getDeviceName();
            if (deviceName.equals("ШТРИХ-МОБАЙЛ-Ф")) {  // TODO: move deviceName to model.xml
                deviceName = "ККТ";
            } else if (deviceName.contains("ПТК")) {
                deviceName = "ПТК";
            } else {
                deviceName = "ККМ";
            }
            status = printer.readLongStatus();
            operatorNumber = status.getOperatorNumber();
            isFiscal = (status.getRegistrationNumber() > 0);
            isEJPresent = status.getPrinterFlags().isEJPresent();
            connected = true;
        }
    }
    
    public XReport readXReport() throws Exception {
        XReport report = new XReport();
        FMTotals totals = printer.readFPTotals(1);
        report.salesAmountBefore = totals.getSalesAmount();
        report.buyAmountBefore = totals.getBuyAmount();
        report.xReportNumber = printer.readOperationRegister(158) + 1;
        report.zReportNumber = printer.readLongStatus().getCurrentShiftNumber();
        for (int i = 0; i <= 3; i++) {
            Receipt receipt = report.receipts.get(i);
            receipt.number = printer.readOperationRegister(148 + i);
            receipt.count = printer.readOperationRegister(144 + i);
            long total = 0;
            for (int j = 0; j <= 3; j++) {
                long amount = printer.readCashRegister(193 + i + j * 4);
                total = total + amount;
                receipt.payments.get(j).amount = amount;
                receipt.payments.get(j).text = paymentNames[j];
            }
            receipt.total = total;
        }
        report.cashIn.number = printer.readOperationRegister(155);
        report.cashIn.count = printer.readOperationRegister(153);
        report.cashIn.total = printer.readCashRegister(242);
        report.cashOut.number = printer.readOperationRegister(156);
        report.cashOut.count = printer.readOperationRegister(154);
        report.cashOut.total = printer.readCashRegister(243);
        report.voidCount = printer.readOperationRegister(166);
        report.dayVoidCount = printer.readOperationRegister(157);
        report.cashInDrawer = printer.readCashRegister(241);
        report.income = report.receipts.get(0).total - report.receipts.get(1).total - report.receipts.get(2).total
                + report.receipts.get(3).total;
        report.salesAmount = report.salesAmountBefore + report.receipts.get(0).total;
        report.buyAmount = report.buyAmountBefore + report.receipts.get(1).total;
        for (int i = 0; i <= 3; i++) {
            report.voidedReceipts.get(i).count = printer.readOperationRegister(179 + i);
            report.voidedReceipts.get(i).total = printer.readCashRegister(249 + i);
        }
        return report;
    }
    
    public void printCashIn(PrintCashIn command) throws Exception {
        isDocumentPrinted = true;
        operatorNumber = command.getOperator();
        long docNumber = printer.readOperationRegister(155);
        printer.waitForPrinting();
        
        beginDocument();
        add(SCashInText, getDocumentNumber(docNumber));
        add("", amountToStr(command.getAmount()));
        endDocument();
    }
    
    public void printCashOut(PrintCashOut command) throws Exception {
        isDocumentPrinted = true;
        operatorNumber = command.getOperator();
        long docNumber = printer.readOperationRegister(156);
        printer.waitForPrinting();
        
        beginDocument();
        add(SCashOutText, getDocumentNumber(docNumber));
        add("", amountToStr(command.getAmount()));
        endDocument();
    }
    
    public String getDocumentNumber(long value) {
        return String.format("%04d", value);
    }
    
    public void add(String text, long value) throws Exception {
        add(text, amountToStr(value));
    }
    
    private void beginDocument() throws Exception {
        printReceiptHeader();
        documentActive = true;
        lines.clear();
    }
    
    private void endDocument() throws Exception {
        documentActive = false;
    }
    
    private void printReceiptHeader() throws Exception {
        status = printer.readLongStatus();
        int documentNumber = status.getDocumentNumber();
        if (!isDocumentPrinted) {
            documentNumber += 1;
        }

        // ККМ
        String s1 = String.format("%s %s", deviceName, status.getSerial());
        String s2 = String.format("ИНН %s №%04d", status.getFiscalIDText(), documentNumber);
        add(s1, s2);
        // Кассир
        PrinterDate date = status.getDate();
        PrinterTime time = status.getTime();
        String s = String.format("%02d.%02d.%02d %02d:%02d", date.getDay(), date.getMonth(), date.getYear(),
                time.getHour(), time.getMin());
        add(s, getOperatorName());
    }
    
    public String getOperatorName() throws Exception {
        for (int i = 0; i < operators.size(); i++) {
            Operator operator = operators.get(i);
            if (operator.getNumber() == operatorNumber) {
                return operator.getName();
            }
        }
        if ((operatorNumber < 1) || (operatorNumber > 30)) {
            operatorNumber = 1;
        }
        String[] fieldValue = new String[1];
        printer.check(printer.readTable(PrinterConst.SMFP_TABLE_CASHIER, operatorNumber, 2, fieldValue));
        Operator operator = new Operator(operatorNumber, fieldValue[0]);
        operators.add(operator);
        return operator.getName();
    }
    
    public void printXReport(PrintXReport command) throws Exception {
        isDocumentPrinted = true;
        operatorNumber = command.getOperator();
        printer.waitForPrinting();
        
        beginDocument();
        add(SXReportText, String.format("№%04d", report.xReportNumber));
        printXZReport();
        endDocument();
    }
    
    public void printZReport(PrintZReport command) throws Exception {
        isDocumentPrinted = true;
        operatorNumber = command.getOperator();
        printer.waitForPrinting();
        
        beginDocument();
        add(SZReportText, String.format("№%04d", report.zReportNumber));
        printXZReport();
        addFiscalSign();
        addCenter('*', SDayClosed);
        readEJReport(false);
        endDocument();
    }
    
    public void printXZReport() throws Exception {
        String[] ReceiptText = {"ЧЕКОВ ПРОДАЖ", "ЧЕКОВ ПОКУПОК", "ЧЕКОВ ВОЗВРАТОВ ПРОДАЖ", "ЧЕКОВ ВОЗВРАТОВ ПОКУПОК"};
        String[] voidedReceiptText = {"ПРОДАЖ", "ПОКУПОК", "ВОЗВР.ПРОДАЖ", "ВОЗВР.ПОКУПОК"};
        
        if (!printer.getCapFiscalStorage()) {
            add("НЕОБНУЛ.СУММА ПРОДАЖ НА НАЧ.СМЕНЫ", report.salesAmountBefore);
            add("НЕОБНУЛ.СУММА ПОКУПОК НА НАЧ.СМЕНЫ", report.buyAmountBefore);
        }
        
        for (int i = 0; i <= 3; i++) {
            Receipt receipt = report.receipts.get(i);
            add(ReceiptText[i], String.format("%04d", receipt.number));
            add(String.format("%04d", receipt.count), receipt.total);
            add(receipt.payments.get(0).text, receipt.payments.get(0).amount);
            add(receipt.payments.get(1).text, receipt.payments.get(1).amount);
            add(receipt.payments.get(2).text, receipt.payments.get(2).amount);
            add(receipt.payments.get(3).text, receipt.payments.get(3).amount);
        }
        add("ВНЕСЕНИЙ", String.format("%04d", report.cashIn.number));
        add(String.format("%04d", report.cashIn.count), report.cashIn.total);
        add("ВЫПЛАТ", String.format("%04d", report.cashOut.number));
        add(String.format("%04d", report.cashOut.count), report.cashOut.total);
        add("АННУЛИРОВАННЫХ ЧЕКОВ", String.format("%04d", report.voidCount));
        add(String.format("%04d", report.dayVoidCount));
        for (int i = 0; i <= 3; i++) {
            String line = String.format("%04d %s", report.voidedReceipts.get(i).count, voidedReceiptText[i]);
            add(line, report.voidedReceipts.get(i).total);
        }
        add("НАЛ. В КАССЕ", report.cashInDrawer);
        add("ВЫРУЧКА", report.income);
        
        if (!printer.getCapFiscalStorage()) {
            add("НЕОБНУЛ. СУММА ПРОДАЖ", report.salesAmount);
            add("НЕОБ. СУММА ПОКУПОК", report.buyAmount);
        }
    }
    
    public void addFiscalSign() throws Exception {
        if (isFiscal) {
            addCenter('-', SFiscalSign);
        }
    }
    
    public void readEJReport(boolean isReceipt) throws Exception {
        if (printer.getCapFiscalStorage()) {
            ReadFiscalStorage();
            return;
        }
        
        if (!(isFiscal && isEJPresent)) {
            return;
        }
        
        List<String> lines = new ArrayList<String>();
        long docMACNumber = printer.readEJStatus().getStatus().getDocMACNumber();
        ReadEJDocument command = new ReadEJDocument(printer.getSysPassword(), (int) docMACNumber);
        printer.execute(command);
        lines.add(command.getLine());
        
        ReadEJDocumentLine command2 = new ReadEJDocumentLine();
        command2.setPassword(printer.getSysPassword());
        while (true) {
            printer.executeCommand(command2);
            if (command2.isFailed()) {
                break;
            }
            String line = command2.getLine();
            lines.add(line);
            if ((line.length() > 0) && (line.contains("#"))) {
                printer.cancelEJDocument();
                break;
            }
        }
        
        if (isReceipt) {
            // 2 last lines
            if (lines.size() > 2) {
                addEJLine(lines.get(2));
                addEJLine(lines.get(lines.size() - 1));
            }
        } else {
            for (int i = 0; i < lines.size(); i++) {
                addEJLine(lines.get(i));
            }
        }
    }
    
    private void ReadFiscalStorage() throws Exception 
    {
        printer.waitForPrinting();
        long docNumber = printer.fsReadStatus().getDocNumber();
        long docSign = printer.fsFindDocument(docNumber).getDocument().getDocSign();
        add(String.format("ФД:%d ФП:%10d", docNumber, docSign));
    }
    
    private void addEJLine(String s) throws Exception {
        add("        " + s);
    }
    
    public void add(String s1, String s2) throws Exception 
    {
        if ((s1 == null)||(s2 == null)) return;
        
        if (s1.length() > lineLength && s2.length() < lineLength && lineLength - (s1.length() % lineLength) - s2.length() > 0) {
            
            int len = lineLength - (s1.length() % lineLength) - s2.length();
            String line = s1 + StringUtils.stringOfChar(' ', len) + s2;
            add(line);
            return;
        }
        
        if (s1.length() + s2.length() + 1 > lineLength) {
            addPaddedLeft(s1);
            addPaddedRight(s2);
            
            return;
        }
        
        int len = lineLength - s1.length() - s2.length();
        String line = s1 + StringUtils.stringOfChar(' ', len) + s2;
        writeLn(line);
    }
    
    private void addPaddedLeft(String s1) throws Exception 
    {
        if (s1 == null) return;
        if (s1.length() > lineLength) {
            add(s1);
            return;
        }
        
        if (s1.length() == lineLength) {
            writeLn(s1);
            return;
        }
        
        int len = lineLength - s1.length();
        String line = s1 + StringUtils.stringOfChar(' ', len);
        writeLn(line);
    }
    
    private void addPaddedRight(String s2) throws Exception {
        if (s2 == null) return;
        if (s2.length() > lineLength) {
            add(s2);
            return;
        }
        
        if (s2.length() == lineLength) {
            writeLn(s2);
            return;
        }
        
        int len = lineLength - s2.length();
        String line = StringUtils.stringOfChar(' ', len) + s2;
        writeLn(line);
    }
    
    public void addCenter(char c, String text) throws Exception {
        if (text == null) return;
        
        int l = (lineLength - text.length()) / 2;
        String line = StringUtils.stringOfChar(c, l) + text;
        line = line + StringUtils.stringOfChar(c, lineLength - line.length());
        writeLn(line);
    }
    
    public void add(String line) throws Exception 
    {
        if (line == null) return;
        
        if (line.length() <= lineLength) {
            addPaddedLeft(line);
            return;
        }
        
        int lineNumber = 0;
        
        while (true) {
            int beginIndex = lineNumber * lineLength;
            if (line.length() <= beginIndex) {
                break;
            }
            
            int endIndex = (lineNumber + 1) * lineLength;
            
            if (endIndex > line.length()) {
                endIndex = line.length();
            }
            
            String substring = line.substring(beginIndex, endIndex);
            
            addPaddedLeft(substring);
            
            lineNumber++;
        }
    }
    
    private SKLStorage getStorage() throws Exception {
        if (storage == null) {
            String filePath = SysUtils.getFilesPath() + printer.getParams().textReportFileName;
            storage = new FileSKLStorage(filePath);
        }
        return storage;
    }
    
    public List<String> getLines(){
        return lines;
    }
            
    private void writeLn(String line) throws Exception 
    {
        lines.add(line);
        if (saveToStorage) {
            getStorage().writeLine(line);
        }
    }
    
    public void printZReport() {
    }
    
    public void printXReport() {
    }
    
    public void endFiscalReceipt() {
    }
}
