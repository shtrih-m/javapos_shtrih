/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.fiscalprinter.command;

import com.shtrih.jpos.fiscalprinter.receipt.ReceiptVisitor;
import com.shtrih.barcode.PrinterBarcode;
import com.shtrih.fiscalprinter.FontNumber;
import com.shtrih.fiscalprinter.SMFiscalPrinter;
import static com.shtrih.fiscalprinter.command.TextDocumentFilter.quantityToStr;
import com.shtrih.fiscalprinter.skl.FileSKLStorage;
import com.shtrih.fiscalprinter.skl.SKLStorage;
import com.shtrih.jpos.fiscalprinter.PrintItem;
import com.shtrih.jpos.fiscalprinter.SmFptrConst;
import com.shtrih.jpos.fiscalprinter.receipt.CashInReceipt;
import com.shtrih.jpos.fiscalprinter.receipt.CashOutReceipt;
import com.shtrih.jpos.fiscalprinter.receipt.FSSalesReceipt;
import com.shtrih.jpos.fiscalprinter.receipt.FSTextReceiptItem;
import com.shtrih.jpos.fiscalprinter.receipt.FSSaleReceiptItem;
import com.shtrih.util.CompositeLogger;
import com.shtrih.util.StringUtils;
import com.shtrih.util.SysUtils;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Vector;
import java.util.Map;
import java.util.HashMap;

/**
 *
 * @author Виталий
 */
public class TextGenerator implements ReceiptVisitor {

    private int lineLength = 0;
    private int operatorNumber = 1;
    private boolean connected = false;
    private String deviceName = "ККМ";
    private final SMFiscalPrinter printer;
    private final Map<Integer, String> operators = new HashMap<Integer, String>();
    private List<String> lines = new Vector<String>();
    private static CompositeLogger logger = CompositeLogger.getLogger(TextGenerator.class);

    public TextGenerator(SMFiscalPrinter printer) throws Exception {
        this.printer = printer;
    }
    
    public void visitCustomReceipt(Object element){
    }
    
    public void visitSalesReceipt(Object element)
    {
        if (!(element instanceof FSSalesReceipt)) return;
        FSSalesReceipt receipt = (FSSalesReceipt)element;
        try
        {
            connect();
            lines.clear();
            printReceiptHeader();
            for (int i = 0; i < receipt.items.size(); i++) {
                process((Object) receipt.items.get(i));
            }
            
            /*
            if (receipt.discounts.getTotal() > 0)
            {
                add(STotalText, summToStr(receipt.getSubtotal()));
                // ОКРУГЛЕНИЕ
                //add(SRoundingText, summToStr(command.getDiscount()));
            }
            add(SReceiptTotal, summToStr(receipt.getTotal()));
            receipt.getPaymentAmount()
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
       */     
            
        }
        catch(Exception e){
            logger.error("Failed duplicate receipt", e);
        }
    }

    public void addFiscalSign() 
    {
        try{
            printer.waitForPrinting();
            long docNumber = printer.fsReadStatus().getDocNumber();
            long docSign = printer.fsFindDocument(docNumber).getDocument().getDocSign();
            add(String.format("ФД:%d ФП:%10d", docNumber, docSign));
        }
        catch(Exception e){
            logger.error("addFiscalSign", e);
        }
    }
    
    private void connect() throws Exception {
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
            connected = true;
        }
    }
    
    private String getOperatorName() throws Exception {
        String operatorName = operators.get(operatorNumber);
        if (operatorName == null) 
        {
            String[] fieldValue = new String[1];
            printer.check(printer.readTable(PrinterConst.SMFP_TABLE_CASHIER, operatorNumber, 2, fieldValue));
            operatorName = fieldValue[0];
            operators.put(operatorNumber, operatorName);
        }
        return operatorName;
    }    
    
    private void printReceiptHeader() throws Exception {
        LongPrinterStatus status = printer.readLongStatus();
        int documentNumber = status.getDocumentNumber() + 1;
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
    
    private void process(Object item) throws Exception {
        try {
            if (item instanceof FSSaleReceiptItem) {
                process((FSSaleReceiptItem) item);
            }
            if (item instanceof FSTextReceiptItem) {
                process((FSTextReceiptItem) item);
            }
            if (item instanceof PrinterBarcode) {
                process((PrinterBarcode) item);
            }

            if (item instanceof AmountItem) {
                process((AmountItem) item);
            }

            if (item instanceof PrintItem) {
                process((PrintItem) item);
            }
        } catch (Exception e) {
            logger.error(e);
        }
    }

    private String amountToStr(long value) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
        symbols.setDecimalSeparator('.');
        DecimalFormat formatter = new DecimalFormat("0.00", symbols);
        return formatter.format(value / 100.0);
    }
    
    private String summToStr(long price, double quantity) {
        return summToStr(Math.round(quantity * price));
    }
    
    private String summToStr(long amount) {
        return "=" + amountToStr(amount);
    }
    
    private String getTaxData(int tax1, int tax2, int tax3, int tax4) throws Exception {
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
    
    private void process(FSSaleReceiptItem item) throws Exception {
        String line = "";
        add(item.getText());

        line = String.format("%s X %s", 
                quantityToStr(item.getQuantity() / 1000000.0), 
                amountToStr(item.getPrice()));
        add("", line);

        line = summToStr(item.getPrice(), item.getQuantity() / 1000000.0)
                + getTaxData(item.getTax1(), 0, 0, 0);
        add(String.format("%02d", item.getDepartment()), line);
    }

    private void process(FSTextReceiptItem item) throws Exception {
        add(item.preLine);
        add(item.text);
        add(item.postLine);
    }
    
    private void process(PrinterBarcode item) throws Exception {
        add("BARCODE");
        add(item.getLabel());
        add(item.getText());
    }
    
    private void process(AmountItem item) throws Exception 
    {
        if (item.getAmount() > 0)
        {
            if (item.getText().isEmpty())
            {
                add("СКИДКА", "=" + amountToStr(item.getAmount()));
            } else
            {
                add("СКИДКА");
                add(item.getText(), "=" + amountToStr(item.getAmount()));
            }
        } else{
            if (item.getText().isEmpty())
            {
                add("НАДБАВКА", "=" + amountToStr(Math.abs(item.getAmount())));
            } else
            {
                add("НАДБАВКА");
                add(item.getText(), "=" + amountToStr(Math.abs(item.getAmount())));
            }
        }
    }
    
    private int getLineLength() throws Exception{
        if (lineLength == 0) {
            lineLength = printer.getMessageLength();
        }
        return lineLength;
    }

    private void add(String s1, String s2) throws Exception {
        int len = getLineLength() - s1.length() - s2.length();
        String line = s1 + StringUtils.stringOfChar(' ', len) + s2;
        add(line);

    }

    public List<String> getLines(){
        return lines;
    }
    
    private void add(String line) throws Exception {
        if (line == null) {
            return;
        }
        lines.add(line);
    }

}