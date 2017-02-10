/////////////////////////////////////////////////////////////////////
//
// BaseJposService.java - Abstract base class for all JavaPOS services.
//
// Modification history
// ------------------------------------------------------------------
// 2007-07-24 JavaPOS Release 1.0                                  VK
//
/////////////////////////////////////////////////////////////////////

import java.util.Vector;
import java.io.FileReader;
import java.io.BufferedReader;

import jpos.CashDrawer;
import jpos.JposException;
import jpos.FiscalPrinter;
import jpos.FiscalPrinterConst;
import jpos.events.DirectIOListener;
import jpos.events.ErrorListener;
import jpos.events.OutputCompleteListener;
import jpos.events.StatusUpdateListener;
import com.shtrih.util.CompositeLogger;
import com.shtrih.fiscalprinter.FontNumber;
import com.shtrih.cashdrawer.ShtrihCashDrawer;
import com.shtrih.jpos.fiscalprinter.SmFptrConst;
import com.shtrih.fiscalprinter.ShtrihFiscalPrinter;
import com.shtrih.printer.model.NCR7167;
import com.shtrih.fiscalprinter.ShtrihFiscalPrinter113;
import jpos.events.DirectIOEvent;
import com.shtrih.fiscalprinter.command.*;
import com.shtrih.util.Hex;
import com.shtrih.barcode.PrinterBarcode;

class PrinterTest implements FiscalPrinterConst {

    private static String encoding = "";
    private static CompositeLogger logger = CompositeLogger.getLogger(PrinterTest.class);
    private final ShtrihFiscalPrinter printer;

    public PrinterTest(ShtrihFiscalPrinter printer) {
        this.printer = printer;
    }

    public String centerLine(String data, int lineLength) {
        int len = Math.min(data.length(), lineLength);
        String s = data.substring(0, len);
        len = (lineLength - len) / 2;
        for (int i = 0; i < len; i++) {
            s = " " + s;
        }

        len = lineLength - s.length();
        for (int i = 0; i < len; i++) {
            s += " ";
        }
        return s;
    }

    public String getLine(String data)
            throws JposException {
        String result = centerLine(data, printer.getMessageLength() - 6);
        result = "***" + result + "***";
        return result;
    }

    private String getHeaderLine(int lineNumber)
            throws JposException {
        return getLine("Строка клише " + String.valueOf(lineNumber));
    }

    private String getTrailerLine(int lineNumber)
            throws JposException {
        return getLine("Строка подвала " + String.valueOf(lineNumber));
    }

    public void setHeaderLines() {
        try {
            printer.setHeaderLine(1, getLoadImageCommand("Logo.bmp") + "ООО \"ГИПЕРГЛОБУС\"", false);
            printer.setHeaderLine(2, "                       г.Влaдимир Суздaльский проспект 28", false);
            printer.setHeaderLine(3, "                           т(4922)37-68-66", false);
            printer.setHeaderLine(4, "               www.globus.ru", false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setTrailerLines() {
        try {
            printer.setTrailerLine(1, getLoadImageCommand("qrcode_110.bmp") + "Trailer line 1", false);
            printer.setTrailerLine(2, "Trailer line 2", false);
            printer.setTrailerLine(3, "Trailer line 3", false);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setHeaderLines2() {
        try {
            //String text = getHeaderLine(1) + getLoadImageCommand("Logo.bmp");
            //printer.setHeaderLine(1, text, false);
            int count = printer.getNumHeaderLines();
            for (int i = 1; i <= count; i++) {
                printer.setHeaderLine(i, getHeaderLine(i), false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setTrailerLines2() {
        try {
            int count = printer.getNumTrailerLines();
            for (int i = 1; i <= count; i++) {
                printer.setTrailerLine(i, getTrailerLine(i), false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void printBarcodeDIO(int barcodeType, String label, String barcode)
            throws Exception {
        printer.printBarcode(barcode, label + barcode, barcodeType, 100,
                SmFptrConst.SMFPTR_PRINTTYPE_DRIVER, 2,
                SmFptrConst.SMFPTR_TEXTPOS_BELOW, 1, 3);
    }

    private void printBarcode(int barcodeType, String label, String barcode)
            throws Exception {
        try {
            printer.printNormal(FPTR_S_RECEIPT, label + barcode);
            printBarcodeDIO(barcodeType, label, barcode);
        } catch (JposException e) {
            printer.printNormal(FPTR_S_RECEIPT, "[ERROR] " + e.getMessage());
        }
    }

    public String executeCommand() {
        try {
            int timeout = 10000;
            String out = printer.executeCommand(0xFF01, timeout, "30");
            FSReadSerial command = new FSReadSerial();
            command.setSysPassword(printer.getSysPassword());
            printer.fsReadSerial(command);
            return "";

        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    public String executeCommand2() {
        String result;
        try {
            byte[] tx = {0x10, 0x1E, 0x00, 0x00, 0x00};
            byte[] rx = printer.executeCommand(tx, 1000);

            result = "tx: " + Hex.toHex(tx) + " "
                    + "rx: " + Hex.toHex(rx);

        } catch (Exception e) {
            e.printStackTrace();
            result = e.getMessage();
        }
        return result;
    }

    public void printBarcodes() {
        //printQRCode();
        printEan13();
        //printAllBarcodes();
    }

    public void printJournalCurrentDay() {
        try {
            printer.printJournalCurrentDay();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printJournalDocRange(int docNumber1, int docNumber2) {
        try {
            printer.printJournalDocRange(docNumber1, docNumber2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printQRCode() {
        try {
            String text1 = "http://check.egais.ru?id=fb8c9153-9d3d-40b9-a1a7-1cf35d637976&dt=2101171104&cn=020000272834";
            String text2 = "26544400044402170";
            String text = text1;

            PrinterBarcode barcode = new PrinterBarcode();
            barcode.setTextFont(1);
            barcode.setTextPosition(SmFptrConst.SMFPTR_TEXTPOS_NOTPRINTED);
            barcode.setBarWidth(5);
            barcode.setHeight(100);
            barcode.setPrintType(SmFptrConst.SMFPTR_PRINTTYPE_DRIVER);
            barcode.setLabel("QR code: " + text);
            barcode.setText(text);
            barcode.setType(SmFptrConst.SMFPTR_BARCODE_QR_CODE);
            printer.printBarcode(barcode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printCode128() {
        try {
            PrinterBarcode barcode = new PrinterBarcode();
            barcode.setTextFont(1);
            barcode.setTextPosition(SmFptrConst.SMFPTR_TEXTPOS_NOTPRINTED);
            barcode.setBarWidth(2);
            barcode.setHeight(100);
            barcode.setPrintType(SmFptrConst.SMFPTR_PRINTTYPE_DRIVER);
            barcode.setLabel("CODE128: 0010004211016101000026");
            barcode.setText("0010004211016101000026");
            barcode.setType(SmFptrConst.SMFPTR_BARCODE_CODE128);
            printer.printBarcode(barcode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printEan13() {
        try {
            PrinterBarcode barcode = new PrinterBarcode();
            barcode.setTextFont(1);
            barcode.setTextPosition(SmFptrConst.SMFPTR_TEXTPOS_ABOVE);
            barcode.setBarWidth(2);
            barcode.setHeight(100);
            barcode.setPrintType(SmFptrConst.SMFPTR_PRINTTYPE_DRIVER);
            barcode.setLabel("EAN13: 2223432423409");
            barcode.setText("2223432423409");
            barcode.setType(SmFptrConst.SMFPTR_BARCODE_EAN13);
            printer.printBarcode(barcode);

            barcode.setBarWidth(3);
            printer.printBarcode(barcode);

            barcode.setBarWidth(4);
            printer.printBarcode(barcode);

            barcode.setBarWidth(5);
            printer.printBarcode(barcode);

            barcode.setBarWidth(6);
            printer.printBarcode(barcode);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printAllBarcodes() {
        try {
            PrinterBarcode barcode = new PrinterBarcode();

            barcode.setTextPosition(SmFptrConst.SMFPTR_TEXTPOS_ABOVE);
            barcode.setTextFont(1);
            barcode.setBarWidth(2);
            barcode.setHeight(100);
            barcode.setType(SmFptrConst.SMFPTR_PRINTTYPE_DRIVER);

            printer.resetPrinter();
            printer.beginNonFiscal();
            // UPCA
            barcode.setText("12345678901");
            barcode.setLabel("UPC-A: ");
            barcode.setType(SmFptrConst.SMFPTR_BARCODE_UPCA);
            printBarcode(barcode);
            // UPCE
            barcode.setText("01234567");
            barcode.setLabel("UPC-E: ");
            barcode.setType(SmFptrConst.SMFPTR_BARCODE_UPCE);
            printBarcode(barcode);
            // EAN13
            barcode.setText("123456789012");
            barcode.setLabel("EAN13: ");
            barcode.setType(SmFptrConst.SMFPTR_BARCODE_EAN13);
            printBarcode(barcode);
            // EAN8
            barcode.setText("12345670");
            barcode.setLabel("EAN8: ");
            barcode.setType(SmFptrConst.SMFPTR_BARCODE_EAN8);
            printBarcode(barcode);
            // CODE39
            barcode.setText("123456789012");
            barcode.setLabel("CODE39: ");
            barcode.setType(SmFptrConst.SMFPTR_BARCODE_CODE39);
            printBarcode(barcode);
            // ITF25
            barcode.setText("123456789012");
            barcode.setLabel("ITF25: ");
            barcode.setType(SmFptrConst.SMFPTR_BARCODE_ITF);
            printBarcode(barcode);
            // CODABAR
            barcode.setText("123456789012");
            barcode.setLabel("CODABAR: ");
            barcode.setType(SmFptrConst.SMFPTR_BARCODE_CODABAR);
            printBarcode(barcode);
            // CODE93
            barcode.setText("123456789012");
            barcode.setLabel("CODE93: ");
            barcode.setType(SmFptrConst.SMFPTR_BARCODE_CODE93);
            printBarcode(barcode);
            // CODE128
            barcode.setText("123456789012");
            barcode.setLabel("CODE128: ");
            barcode.setType(SmFptrConst.SMFPTR_BARCODE_CODE128);
            printBarcode(barcode);

            barcode.setBarWidth(4);
            barcode.setText(
                    "https://checkl.fsrar.ru?id=fa07210-0041-4dc6-"
                    + "bbf2-1634282724418amdt=191015161"
                    + "71amcn=0100000062870D0682B61230689D76826FAC92C5DC29955F0E3B5663B4"
                    + "4C63A673C86B0976C0B24495848F6EF157792203A0D275"
                    + "1F525456644096478D256A910EFEABB67");
            // Aztec
            barcode.setLabel("Aztec: ");
            barcode.setType(SmFptrConst.SMFPTR_BARCODE_AZTEC);
            printBarcode(barcode);
            // Data matrix
            barcode.setLabel("Data matrix: ");
            barcode.setType(SmFptrConst.SMFPTR_BARCODE_DATA_MATRIX);
            printBarcode(barcode);
            // QR code
            barcode.setLabel("QR code: ");
            barcode.setType(SmFptrConst.SMFPTR_BARCODE_QR_CODE);
            printBarcode(barcode);
            // PDF417
            barcode.setBarWidth(2);
            barcode.setText(
                    "4C63A673C86B0976C0B24495848F6EF157792203A0D275\n"
                    + "1F525456644096478D256A910EFEABB67");
            barcode.setLabel("PDF417: ");
            barcode.setType(SmFptrConst.SMFPTR_BARCODE_PDF417);
            printBarcode(barcode);

            printer.endNonFiscal();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printBarcodeNF(PrinterBarcode barcode) {
        try {
            barcode.setLabel(barcode.getLabel() + barcode.getText());
            printer.resetPrinter();
            printer.beginNonFiscal();
            printBarcode(barcode);
            printer.endNonFiscal();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printBarcode(PrinterBarcode barcode)
            throws Exception {
        try {
            printer.printNormal(FPTR_S_RECEIPT, "------------------------------------------");
            printer.printNormal(FPTR_S_RECEIPT, barcode.getLabel() + barcode.getText());
            printer.printBarcode(barcode);
        } catch (Exception e) {
            printer.printNormal(FPTR_S_RECEIPT, "ERROR: " + e.getMessage());
        }
    }

    private String getLoadImageCommand(String fileName) {
        byte[] ss = {0x1B, 0x62};
        byte[] es = {0x0A};

        String data = new String(ss) + fileName + new String(es);
        return data;
    }

    // %L:Logo.bmp%
    private String getLoadImageCommand2(String fileName) {
        byte[] ss = {0x25, 0x4C, 0x3A};
        byte[] es = {0x25};

        String data = new String(ss) + fileName + new String(es);
        return data;
    }

    public String getBarcode(
            int barcodeType,
            String text)
            throws JposException {
        // barcodeHeight
        byte b[] = {0x1D, 0x6B, 0, 0};
        b[2] = (byte) barcodeType;
        b[3] = (byte) text.length();

        String result = new String(b);
        result += text;
        return result;
    }

    private void printNormalBarcode(String label, int code, String text)
            throws Exception {
        try {
            printer.printNormal(FPTR_S_RECEIPT, label + text);
            printer.printNormal(FPTR_S_RECEIPT, getBarcode(code, text));
        } catch (JposException e) {
            printer.printNormal(FPTR_S_RECEIPT, "[ERROR] " + e.getMessage());
        }
    }

    private void printRecMessageBarcode(String label, int code, String text)
            throws Exception {
        try {
            printer.printRecMessage(label + text);
            printer.printRecMessage(getBarcode(code, text));
        } catch (JposException e) {
            printer.printRecMessage("[ERROR] " + e.getMessage());
        }
    }

    public void printEscBarcodesNormal() {
        try {
            printer.beginNonFiscal();

            printer.printNormal(FPTR_S_RECEIPT, getBarcodeParameters());
            printNormalBarcode("UPC-A: ", 65, "01234567890");
            printNormalBarcode("UPC-E: ", 66, "0123456");
            printNormalBarcode("EAN13: ", 67, "012345678912");
            printNormalBarcode("EAN8: ", 68, "0123456");
            printNormalBarcode("CODE39: ", 69, "012345678912");
            printNormalBarcode("ITF25: ", 70, "012345678912");
            printNormalBarcode("CODABAR: ", 71, "012345678912");
            printNormalBarcode("CODE93: ", 72, "012345678912");
            printNormalBarcode("CODE128: ", 73, "012345678912");
            printNormalBarcode("PDF417: ", 74, "012345678912");

            printer.endNonFiscal();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getBarcodeParameters() {
        try {
            EscCommand command = new EscCommand();
            command.selectHRIPitch(NCR7167.NCR7167_HRI_PITCH_STANDARD);
            command.selectHRIPosition(NCR7167.NCR7167_HRI_NOTPRINTED);
            command.selectBarcodeWidth(2);
            command.selectBarcodeHeight(100);
            command.EOL();
            return new String(command.getBytes());
        } catch (Exception e) {
            return "[ERROR] " + e.getMessage();
        }

    }

    public void printEscBarcodesRecMessage() {
        try {
            printer.setFiscalReceiptType(FPTR_RT_SALES);
            printer.beginFiscalReceipt(true);
            printer.printRecMessage(getBarcodeParameters());
            printRecMessageBarcode("UPC-A: ", 65, "01234567890");
            printRecMessageBarcode("UPC-E: ", 66, "0123456");
            printRecMessageBarcode("EAN13: ", 67, "012345678912");
            printRecMessageBarcode("EAN8: ", 68, "0123456");
            printRecMessageBarcode("CODE39: ", 69, "012345678912");
            printRecMessageBarcode("ITF25: ", 70, "012345678912");
            printRecMessageBarcode("CODABAR: ", 71, "012345678912");
            printRecMessageBarcode("CODE93: ", 72, "012345678912");
            printRecMessageBarcode("CODE128: ", 73, "012345678912");
            printRecMessageBarcode("PDF417: ", 74, "012345678912");

            printer.printRecVoid("");
            printer.endFiscalReceipt(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void printCode39(String label, String barcode, int dots, int ratio)
            throws Exception {
        try {
            printer.printBarcode(
                    barcode,
                    label + barcode,
                    SmFptrConst.SMFPTR_BARCODE_CODE39,
                    100,
                    SmFptrConst.SMFPTR_PRINTTYPE_DRIVER,
                    dots,
                    SmFptrConst.SMFPTR_TEXTPOS_BELOW,
                    1,
                    ratio);
        } catch (JposException e) {
            printer.printNormal(FPTR_S_RECEIPT, "[ERROR] " + e.getMessage());
        }
    }

    public void printCode39Barcodes() {
        try {
            printer.beginNonFiscal();

            String barcode = "001.0578.270210.0001";
            printer.printBarcode(
                    barcode, barcode,
                    SmFptrConst.SMFPTR_BARCODE_CODE39,
                    100,
                    SmFptrConst.SMFPTR_PRINTTYPE_DRIVER,
                    1,
                    SmFptrConst.SMFPTR_TEXTPOS_BOTH,
                    1,
                    2);

            printer.printBarcode(
                    barcode, barcode,
                    SmFptrConst.SMFPTR_BARCODE_CODE39,
                    100,
                    SmFptrConst.SMFPTR_PRINTTYPE_DRIVER,
                    1,
                    SmFptrConst.SMFPTR_TEXTPOS_BOTH,
                    1,
                    3);

            printer.endNonFiscal();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void testDirectIO()
            throws Exception {
        System.out.println("FPTR_DIO_READTABLE");
        int table;
        int rowCount;
        int fieldCount;

        // FPTR_DIO_WRITETABLE
        // payment names
        table = 5;
        rowCount = 4;
        fieldCount = 1;
        for (int row = 2; row <= rowCount; row++) {
            for (int field = 1; field <= fieldCount; field++) {
                printer.writeTable(table, row, field, "PAYTYPE" + String.valueOf(row));
            }
        }

        table = 1;
        rowCount = 1;
        fieldCount = 20;
        for (int row = 1; row <= rowCount; row++) {
            for (int field = 1; field <= fieldCount; field++) {
                String fieldValue = printer.readTable(table, row, field);
                System.out.println(
                        "Table " + String.valueOf(table) + ", "
                        + "Row " + String.valueOf(row) + ", "
                        + "Field " + String.valueOf(field) + ": "
                        + fieldValue);
            }
        }
        // payment names
        table = 5;
        rowCount = 4;
        fieldCount = 1;
        for (int row = 1; row <= rowCount; row++) {
            for (int field = 1; field <= fieldCount; field++) {
                String fieldValue = printer.readTable(table, row, field);
                System.out.println(
                        "Table " + String.valueOf(table) + ", "
                        + "Row " + String.valueOf(row) + ", "
                        + "Field " + String.valueOf(field) + ": "
                        + fieldValue);
            }
        }
        // FPTR_DIO_WRITE_PAYMENT_NAME
        for (int i = 2; i <= 4; i++) {
            printer.writePaymentName(i, "Тип оплаты " + String.valueOf(i));
        }
        // FPTR_DIO_READ_PAYMENT_NAME
        for (int i = 1; i <= 4; i++) {
            String paymentName = printer.readPaymentName(i);
            System.out.println(
                    "Payment " + String.valueOf(i) + ": " + paymentName);
        }
        // FPTR_DIO_READ_DAY_END
        System.out.println("FPTR_DIO_READ_DAY_END");
        if (printer.readDayEnd()) {
            System.out.println("Day end required");
        } else {
            System.out.println("24 hours is not over");
        }
    }

    public void open(String DeviceName) {
        try {
            printer.open(DeviceName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            printer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void claim() {
        try {
            printer.claim(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void release() {
        try {
            printer.release();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void enableDevice() {
        try {
            printer.setDeviceEnabled(true);
            printer.setPOSID("1", "Кравцов В.В.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void disableDevice() {
        try {
            printer.setDeviceEnabled(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void resetPrinter() {
        try {
            printer.resetPrinter();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printEmptyFiscalReceipt() {
        try {
            printer.beginFiscalReceipt(true);
            printer.printRecMessage("Nonfiscal line 1");
            printer.printRecSubtotal(0);
            printer.printRecTotal(0, 0, "1");
            printer.endFiscalReceipt(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void writeCashierName() {
        try {
            printer.writeCashierName("Кассир: Иванов И.И.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printFiscalReceipt() 
    {
        printFiscalReceipt11();
        
        //printSpeedTest2();
        //printFiscalReceipt106();
        
        //printCancelledSalesReceipt();
        //printFiscalReceipt11();
        //printNCRFiscalReceipt();
        //printFiscalReceipt11();
        //printFiscalReceipt1();

        //printFiscalReceipt107();
        //printFiscalReceipt1();
        //writeCashierName();
        //printFiscalReceipt104();
        //printFiscalReceipt101();
        //printCancelledReceipt();
        //printNCRFiscalReceipt();
        //printQRCode2();
        //printFiscalReceipt136();
        //printFiscalReceipt137();
        //printFiscalReceipt138();
        //printFiscalReceipt1002();
        //printFiscalReceipt101();
        //printFiscalReceipt102();
        //printFiscalReceipt111();
        /*
        printFiscalReceipt123();
        printRefundReceipt();
        printCancelledReceipt();
         */
    }

    public void printPaperReport() {
        try {
            System.out.println("CapRecPresent: " + printer.getCapRecPresent());
            System.out.println("CapRecEmptySensor: " + printer.getCapRecEmptySensor());
            System.out.println("CapRecNearEndSensor: " + printer.getCapRecNearEndSensor());
            System.out.println("RecEmpty: " + printer.getRecEmpty());
            System.out.println("RecNearEnd: " + printer.getRecNearEnd());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printFiscalReceipt11() {
        try {
            printer.resetPrinter();
            printer.setFiscalReceiptType(FPTR_RT_SALES);
            printer.beginFiscalReceipt(true);
            printer.printRecRefund("****      100359344 Item1", 5596000, 2);
            printer.printRecSubtotal(5596000);
            printer.printRecSubtotalAdjustment(1, "", 1000);
            printer.printRecTotal(5596000, 5596000, "payTypeName1");
            printer.endFiscalReceipt(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printFiscalReceipt1001() {
        try {
            printer.resetPrinter();

            printer.setFiscalReceiptType(FPTR_RT_SALES);
            printer.beginFiscalReceipt(true);
            printer.printRecItemRefund("1* 10003 PODR.Приправа VEGETA        75г",
                    100, 1000, 1, 100, "");
            printer.printRecItemAdjustment(1, "СКИДКА", 50, 1);

            printer.printRecTotal(50, 50, "0");
            printer.endFiscalReceipt(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printFiscalReceipt1002() {
        try {
            printer.resetPrinter();

            printer.setFiscalReceiptType(FPTR_RT_SALES);
            printer.beginFiscalReceipt(true);
            printer.printRecRefund("1", 100, 1);
            printer.printRecRefund("2", 1, 2);
            printer.printRecSubtotalAdjustment(1, "СКИДКА", 1);
            printer.printRecTotal(100, 100, "0");
            printer.endFiscalReceipt(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printFiscalReceipt1() {
        try {
            printer.resetPrinter();

            printer.setFiscalReceiptType(FPTR_RT_SALES);
            printer.beginFiscalReceipt(true);
            printer.printText("Before line 1");
            printer.setPreLine("PreLine1");
            printer.setPostLine("PostLine1");
            printer.printRecItem("Тестовый товар", 100, 1000, 0, 100, "");
            printer.printText("After line 1");

            printer.printText("Before line 2");
            printer.setPreLine("PreLine2");
            printer.setPostLine("PostLine2");
            printer.printRecItem("Батарейка Alkaline Stam", 20, 0, 1, 20, "");
            printer.printText("After line 2");

            String text = "Slip document, line 1\r\nSlip document, line 2\r\nSlip document, line 3";
            printer.printNonFiscalDoc(text);

            printer.printRecItemVoid("Батарейка Alkaline Stam", 20, 0, 1, 20, "");
            printer.printRecItem("Тестовый товар", 100, 1000, 0, 100, "");
            printer.printRecItemAdjustment(FPTR_AT_AMOUNT_DISCOUNT, "СКИДКА", 50, 1);
            printer.printRecSubtotalAdjustment(FPTR_AT_AMOUNT_DISCOUNT, "", 100);
            printer.printRecSubtotalAdjustment(FPTR_AT_AMOUNT_DISCOUNT, "", 25);
            printer.printRecSubtotal(25);
            printer.printRecTotal(25, 25, "0");
            printer.endFiscalReceipt(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printFiscalReceipt136() {
        try {
            printer.resetPrinter();
            printer.beginFiscalReceipt(true);
            printer.setPostLine("Марковь Элитная Мыта");
            printer.setPreLine("22");
            printer.printRecItem("Марковь весовая", 24995400, 2000, 1, 12497700, "");
            printer.printRecSubtotal(24995400);
            for (int i = 0; i < 3; i++) {
                printer.printRecSubtotalAdjustment(1, "Скидка по акции", 749900);
                printer.printRecSubtotal(24245500);
            }
            printer.printRecTotal(24245500, 24245500, "30");
            printer.printRecMessage("                     ");

            TLVList list = new TLVList();
            list.add(1008, "+79168191324");
            list.add(1008, "kravtsov@shtrih-m.ru");
            printer.fsWriteTLV(list.getData());

            printer.endFiscalReceipt(true);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printFiscalReceipt137() {
        try {
            printer.resetPrinter();

            printer.beginFiscalReceipt(true);
            printer.printRecItem("0627  4606272002283 НЕСКАФЕ ГОЛД 95Г", 2929000, 1000, 1, 2929000, "");
            printer.printRecSubtotalAdjustment(1, "Округл.сдачи", 4000);
            printer.printRecTotal(2925000, 2925000, "1");
            printer.printRecMessage("");
            printer.printRecMessage("ТОВАРОВ           1                     ");
            printer.printRecMessage("                                        ");
            printer.printRecMessage(" ****************************************");
            printer.printRecMessage("           Уважаемый покупатель!          ");
            printer.printRecMessage("      На товары, участвующие в акции,     ");
            printer.printRecMessage("  баллы на сумму покупки не начисляются!  ");
            printer.printRecMessage("     ****** СПАСИБО ЗА ПОКУПКУ ******     ");
            printer.printRecMessage("------------------------------------------");
            printer.printRecMessage(" КАССИР        001   Тупоногова Екатерина ");
            printer.printRecMessage("*0099 0085/002/001    24.11.16 11:40 AC-00");
            printer.printRecMessage("------------------------------------------");

            TLVList list = new TLVList();
            list.add(1008, "+79168191324");
            list.add(1117, "kravtsov@shtrih-m.ru");
            list.add(1117, "иванов@иванов.рф");
            printer.fsWriteTLV(list.getData());

            printer.endFiscalReceipt(false);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printFiscalReceipt138() {
        try {
            printer.resetPrinter();

            printer.setFontNumber(1);

            printer.beginFiscalReceipt(false);
            printer.printRecItem("1 3300573 Пакет ПЯТЕРОЧКА 65х40см", 445, 1000, 1, 445, "");
            printer.printRecItem("2* 3394146 Напиток ПЕПСИ-ЛАЙТ ПЭТ 1.75л", 8980, 1000, 1, 8980, "");
            printer.printRecItem("3* 3394144 Напиток ПЕПСИ-КОЛА ПЭТ 1.75л", 8980, 1000, 1, 8980, "");
            printer.printRecItem("4 3095747 Хурма сахарная импортная 1кг", 16, 1, 1, 16900, "");
            printer.printRecItem("5* 3182856 Яйца кур.С1 стол.фас.10шт", 5590, 1000, 2, 5590, "");
            printer.printRecItem("6* 2128455 ВИТАМИН Смесь ЛЕТНЯЯ с/м   40", 6990, 1000, 1, 6990, "");
            printer.printRecItem("7 76309 Вода АКВА МИН.пит.газ.пл/бут.2л", 5495, 1000, 1, 5495, "");
            printer.printRecItem("8 3605774 ФУФИНА Пельмени ДОМ.500г", 11800, 1000, 2, 11800, "");
            printer.printRecItem("9* 809 Бананы 1кг", 3950, 990, 1, 3990, "");
            printer.printRecItem("10 3406624 С.ПРОД.Сыр ГОЛЛАН.мдж 45% 210", 15900, 1000, 2, 15900, "");
            printer.printRecSubtotalAdjustment(1, "Скидка. 2 по цене 1", 8980);
            printer.printRecSubtotalAdjustment(1, "", 66);
            printer.printRecSubtotal(59100);
            printer.printRecTotal(59100, 59100, "30");
            printer.endFiscalReceipt(false);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printFiscalReceipt139() {
        try {
            printer.resetPrinter();

            printer.beginFiscalReceipt(false);
            printer.printRecItem("**02  2314523000002 ШЕЙКА СВИНАЯ", 2849100, 950, 2, 2999000, "кг");
            printer.printRecItemAdjustment(1, "ckugka", 284900, 2);
            printer.printRecTotal(2564200, 2564200, "12");

            printer.endFiscalReceipt(false);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void fsReadParameters() throws Exception {
        Vector<String> list = new Vector<String>();
        printer.directIO(SmFptrConst.SMFPTR_DIO_READ_FS_PARAMS, null, list);
        System.out.println("List size: " + list.size());
        for (int i = 0; i < list.size(); i++) {
            System.out.println(String.valueOf(i) + ". " + list.get(i));
        }
    }

    public void printFiscalReceipt103() {
        try {
            printer.resetPrinter();

            printer.setFiscalReceiptType(4);
            printer.beginFiscalReceipt(false);
            printer.printRecItem("1 3496906 KIT.Корм говяд.жел.д/взр.85г", 13585, 11000, 1, 1300, "");
            printer.printRecItemAdjustment(1, "", 715, 1);
            printer.printRecSubtotalAdjustment(1, "", 85);
            printer.printRecSubtotal(13500);
            printer.printRecTotal(13500, 13500, "30");
            printer.endFiscalReceipt(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printFiscalReceipt104() {
        try {
            printer.resetPrinter();

            printer.setFiscalReceiptType(4);
            printer.beginFiscalReceipt(false);
            for (int i = 0; i <= 20; i++) {
                printer.printRecItem("Receipt Item " + i, 1, 1000, 1, 1, "");
            }
            printer.printRecTotal(50000, 50000, "0");
            printer.endFiscalReceipt(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printFiscalReceipt105() {
        try {
            printer.resetPrinter();

            printer.beginFiscalReceipt(true);
            printer.printRecItem("****            430 МАНДАРИНЫ", 10772, 770, 1, 13990, "кг");
            printer.printRecItem("****            430 МАНДАРИНЫ", 10772, 770, 1, 13990, "кг");
            printer.printRecVoidItem("****            430 МАНДАРИНЫ", 13990, 770, 0, 0, 1);
            printer.printRecSubtotalAdjustment(1, "Округл.сдачи", 22);
            printer.printRecTotal(10750, 10750, "1");
            printer.printRecMessage(" ТОВАРОВ           1                      ");
            printer.printRecMessage(" **************************************** ");
            printer.printRecMessage("           Уважаемый покупатель!          ");
            printer.printRecMessage("      На товары, участвующие в акции,     ");
            printer.printRecMessage("  баллы на сумму покупки не начисляются!  ");
            printer.printRecMessage("     ****** СПАСИБО ЗА ПОКУПКУ ******     ");
            printer.endFiscalReceipt(false);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    String additionalHeader = "Ваш кассир сегодня:\r\n"
            + "ИВАНИЛОВА Г.Л.\r\n"
            + "*0562 1007/008/011    18.06.14 14:04 AC-00\r\n"
            + "------------------------------------------";

    public void printFiscalReceipt106() {
        try {
            printer.resetPrinter();
            printer.setAdditionalHeader(additionalHeader);
            printer.setFiscalReceiptType(FPTR_RT_SALES);
            printer.beginFiscalReceipt(true);
            printer.printRecItem("Item 1", 100, 1000, 1, 100, "кг");
            printer.printRecTotal(100, 100, "0");
            printer.printRecMessage(" ТОВАРОВ           1                      ");
            printer.printRecMessage(" **************************************** ");
            printer.printRecMessage("           Уважаемый покупатель!          ");
            printer.printRecMessage("      На товары, участвующие в акции,     ");
            printer.printRecMessage("  баллы на сумму покупки не начисляются!  ");
            printer.printRecMessage("     ****** СПАСИБО ЗА ПОКУПКУ ******     ");
            printer.endFiscalReceipt(true);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printCancelledSalesReceipt() {
        try {
            printer.setFiscalReceiptType(FPTR_RT_SALES);
            printer.beginFiscalReceipt(true);
            printer.printRecItem("Item 1", 0, 770, 1, 13990, "кг");
            printer.printRecVoid("Receipt cancelled");
            printer.endFiscalReceipt(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printFiscalReceipt107() {
        try {
            printer.resetPrinter();

            printer.setAdditionalHeader(additionalHeader);
            printer.beginFiscalReceipt(false);
            printer.printRecItem("9866 NESC.Кофе CLAS.раст.ж/б 100г", 16900, 1000, 1, 16900, "");
            printer.printRecItem("5051 AHM.Чай EARL GREY 25х2г", 6197, 1000, 1, 9295, "");
            printer.printRecItemAdjustment(1, "Тест M за N !!NEW!!", 3098, 1);
            printer.printRecItem("5051 AHM.Чай EARL GREY 25х2г", 6197, 1000, 1, 9295, "");
            printer.printRecItemAdjustment(1, "Тест M за N !!NEW!!", 3098, 0);
            printer.printRecItem("5051 AHM.Чай EARL GREY 25х2г", 6196, 1000, 1, 9295, "");
            printer.printRecItemAdjustment(1, "Тест M за N !!NEW!!", 3099, 1);
            printer.printRecMessage("СКИДКА:                              92.95");
            printer.printRecSubtotal(35490);
            printer.printRecSubtotalAdjustment(1, "", 90);
            printer.printRecSubtotal(35400);
            printer.printRecTotal(35400, 35400, "30");
            printer.endFiscalReceipt(false);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printFiscalReceipt101() {
        try {
            printer.resetPrinter();

            printer.printRecMessage("Кассовый чек");
            printer.printRecMessage("printRecMessage1");
            printer.printNormal(FPTR_S_RECEIPT, "printNormal1");

            printer.setFiscalReceiptType(FPTR_RT_SALES);
            printer.beginFiscalReceipt(true);

            printer.printRecMessage("printRecMessage2");
            printer.printNormal(FPTR_S_RECEIPT, "printNormal2");

            printer.printRecItem("Тестовый товар", 10000, 1000, 0, 10000, "");
            printer.printRecSubtotalAdjustment(FPTR_AT_AMOUNT_DISCOUNT, "", 600);
            printer.printRecItem("Батарейка Alkaline Stam", 20, 0, 1, 20, "");
            printer.printRecItemVoid("Батарейка Alkaline Stam", 20, 0, 1, 20, "");
            printer.printRecSubtotal(9500);

            printer.printRecMessage("printRecMessage3");
            printer.printNormal(FPTR_S_RECEIPT, "printNormal3");

            printer.setDiscountAmount(99);
            printer.printRecTotal(9500, 9500, "0");

            printer.printRecMessage("printRecMessage4");
            printer.printNormal(FPTR_S_RECEIPT, "printNormal4");

            printer.printBarcode("1234567890123", "", SmFptrConst.SMFPTR_BARCODE_EAN13,
                    100, SmFptrConst.SMFPTR_PRINTTYPE_DRIVER, 2, 0, 1, 0);

            printQRCode();

            printer.endFiscalReceipt(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printFiscalReceipt102() {
        try {
            printer.resetPrinter();
            printer.setCheckTotal(true);
            printer.setFiscalReceiptType(FPTR_RT_SALES);
            printer.beginFiscalReceipt(true);
            printer.printRecItem("1 1860 Напиток COCA-COLA газ.ПЭТ  2.0л", 29385, 3000, 1, 9795, "");
            printer.printRecItemAdjustment(1, "", 10000, 0);
            printer.printRecSubtotal(19385);
            printer.printRecSubtotalAdjustment(1, "", 85);
            printer.printRecTotal(19300, 19300, "");

            printer.printRecMessage("На артикул не предоставляется скидка");
            printer.printRecMessage(" ");
            printer.printRecMessage("Покупатель: 7789004000000079");
            printer.printRecMessage("     ");
            printer.printRecMessage("................................................");
            printer.printRecMessage(" ");
            printer.printRecMessage("         ***ЧЕК ПОКУПАТЕЛЯ ***          ");
            printer.printRecMessage(" ");
            printer.printRecMessage("----------------------------------------");
            printer.printRecMessage(" ");
            printer.printRecMessage("Списано баллов: 1000                    ");
            printer.printRecMessage("Со счета списано, баллов: 1000          ");
            printer.printRecMessage("Баланс, баллов: 892                     ");

            printer.endFiscalReceipt(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printFiscalReceipt123() {
        try {
            printer.resetPrinter();

            int[] optArgs = new int[1];
            String[] data = new String[1];
            printer.getData(FPTR_GD_PRINTER_ID, optArgs, data);
            logger.debug("Serial number: " + data[0]);

            printer.setFiscalReceiptType(FPTR_RT_SALES);
            printer.beginFiscalReceipt(true);
            printer.setPreLine("PreLine1");
            printer.printRecItem("Receipt item 1", 2000, 1000, 0, 10890, "");
            printer.setPostLine("PostLine1");

            //printer.printRecVoidItem("Receipt item 1", 2000, 1000, 0, 0, 0);
            printer.printRecItem("Receipt item 1", 2000, 1000, 0, 0, "");

            printer.printRecItemAdjustment(FPTR_AT_AMOUNT_DISCOUNT,
                    "Скидка суммой 123", 123, 0);
            printer.printRecItemAdjustment(FPTR_AT_PERCENTAGE_DISCOUNT,
                    "Процентная скидка 10%", 1000, 0);
            printer.printRecItemAdjustment(FPTR_AT_AMOUNT_SURCHARGE,
                    "Надбавка суммой 123", 123, 0);
            printer.printRecItemAdjustment(FPTR_AT_PERCENTAGE_SURCHARGE,
                    "Процентная надбавка 10%", 1000, 0);

            printer.setPreLine("PreLine2");
            printer.printRecItem("Receipt item 2", 3000, 1000, 0, 10, "");
            printer.setPostLine("PostLine2");

            printer.printRecSubtotalAdjustment(FPTR_AT_AMOUNT_DISCOUNT, "printRecSubtotalAdjustment", 10900);

            printer.printRecSubtotal(2000);
            printer.printRecTotal(2000, 5000, "payTypeName1");
            printer.printRecMessage("Телефон покупателя: +79168191324");
            printer.endFiscalReceipt(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printFiscalReceipt22() {
        try {
            printer.resetPrinter();
            printer.setFiscalReceiptType(FPTR_RT_SALES);
            printer.beginFiscalReceipt(true);
            printer.setPreLine("4607085440095");
            printer.printRecItem("НЕСКАФЕ КЛАССИК 120Г", 10890, 1000, 1, 10890, "");
            printer.printRecItem("Receipt item 2", 10, 1000, 1, 10, "");
            printer.printNormal(FPTR_S_RECEIPT, "printNormal " + getLoadImageCommand("Logo.bmp"));
            printer.printRecSubtotal(0);
            printer.printRecTotal(10900, 4000, "39");
            printer.printRecTotal(10900, 6900, "1");
            printer.printRecMessage("printRecMessage " + getLoadImageCommand("qrcode_110.bmp"));
            printer.printRecMessage("*****     Спaсибо зa покупку!      *****");
            printer.printRecMessage("****************************************");
            printer.printRecMessage("\"Глобус\" г.Владимир приглашает на работу");
            printer.printRecMessage(" Продавец-кассир(Напитки); Повар;");
            printer.printRecMessage(" Помошник повара(ресторан);");
            printer.printRecMessage(" Продавец-консультант(обувь); Водитель");
            printer.printRecMessage(" погрузчика; Продавец(отдел продаж)");
            printer.printRecMessage("          Тел. (4922) 37-68-54");
            printer.printRecMessage("        Найди работу уже сегодня!");

            printer.endFiscalReceipt(true);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printFiscalReceipt31() {
        try {
            printer.setFiscalReceiptType(FPTR_RT_SALES);
            printer.beginFiscalReceipt(true);
            printer.printRecItem("ГЛОБУС МИН ПИТ ГАЗ", 107900, 1000, 1, 107900, "");
            printer.printRecItem("ГЛОБУС МИН ПИТ ГАЗ", 107900, 1000, 1, 107900, "");
            printer.printRecItem("ГЛОБУС МИН ПИТ ГАЗ", 107900, 1000, 1, 107900, "");
            printer.printRecItem("ГЛОБУС МИН ПИТ ГАЗ", 107900, 1000, 1, 107900, "");

            printer.printRecVoidItem("ГЛОБУС МИН ПИТ ГАЗ", 107900, 1000, 0, 0, 1);
            printer.printRecVoidItem("ГЛОБУС МИН ПИТ ГАЗ", 107900, 1000, 0, 0, 1);
            printer.printRecVoidItem("ГЛОБУС МИН ПИТ ГАЗ", 107900, 1000, 0, 0, 1);

            printer.printRecSubtotal(110600);
            printer.printRecTotal(107900, 2900, "39");
            printer.printRecTotal(107900, 200000, "1");

            printer.printRecMessage(getLoadImageCommand("qrcode_1101.bmp"));

            printer.endFiscalReceipt(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printFiscalReceipt3() {
        try {
            for (int i = 0; i < 100; i++) {
                ReadLongStatus command = new ReadLongStatus();
                command.setPassword(30);
                printer.executeCommand(command);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printFiscalReceipt4() {
        try {
            printer.setFiscalReceiptType(FPTR_RT_SALES);
            printer.beginFiscalReceipt(true);
            printer.printRecItem("Item 1", 1350000, 1000, 1, 1350000, "");
            printer.printRecTotal(1350000, 10000, "11");
            printer.printRecTotal(1350000, 10000, "12");
            printer.printRecTotal(1350000, 10000, "21");
            printer.printRecTotal(1350000, 10000, "31");
            printer.printRecTotal(1350000, 10000, "32");
            printer.printRecTotal(1350000, 1500000, "1");
            printer.endFiscalReceipt(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printFiscalReceipt5() {
        try {
            printer.setFiscalReceiptType(FPTR_RT_SALES);
            printer.beginFiscalReceipt(true);
            printer.printRecItem("8032711221681", 75000000, 1, 0, 0, "");
            printer.printRecMessage("DDP CALZ. BABY  SKISKOLAER + BIKE B  ARGENTO  26");
            printer.printRecTotal(75000000, 50000000, "2");
            printer.endFiscalReceipt(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printFiscalReceipt6() {
        try {
            printer.setFiscalReceiptType(FPTR_RT_SALES);
            printer.beginFiscalReceipt(true);
            printer.printRecItem("Товар номер 1", 7565, 1000, 1, 0, "Unit1");
            printer.printRecItemAdjustment(FPTR_AT_AMOUNT_DISCOUNT,
                    "Скидка на товар 1", -123, 1);
            printer.printRecItem("Товар номер 2", 1234, 1234, 2, 0, "Unit2");
            printer.printRecItemAdjustment(FPTR_AT_AMOUNT_DISCOUNT,
                    "Скидка на товар 2", -234, 2);

            printer.printRecTotal(75000000, 50000000, "2");
            printer.printRecMessage("Подвал чека, строка 1");
            printer.endFiscalReceipt(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printFiscalReceipt7() {
        printSalesReceipt7();
    }

    public static int min(int i1, int i2) {
        if (i1 < i2) {
            return i1;
        } else {
            return i2;
        }
    }

    public void printFiscalReceipt8() {
        String[] headerLines = {
            " ",
            "               ООО \"ГИПЕРГЛОБУС\"         ",
            "       г. Щелково, Пролетарский пр-т 18  ",
            "             тел.: (495) 221-85-00       ",
            "                 www.globus.ru           ",
            " "};

        String[] trailerLines = {
            " ",
            "              Спасибо за покупку!        ",
            "          Будем рады Вас видеть снова!   ",
            " "};

        String additionalHeader = "Ваш кассир сегодня:\r\n"
                + "ИВАНИЛОВА Г.Л.\r\n"
                + "*0562 1007/008/011          18.06.14 14:04 AC-00\r\n"
                + "------------------------------------------------";

        try {
            setHeaderLines(headerLines);
            setTrailerLines(trailerLines);

            printer.resetPrinter();

            printer.setFiscalReceiptStation(FPTR_RS_RECEIPT);
            printer.setFiscalReceiptType(FPTR_RT_SALES);
            printer.setAdditionalHeader(additionalHeader);
            printer.setAdditionalTrailer("");
            printer.beginFiscalReceipt(false);
            // Item 1
            printer.printNormal(2, "  4605246004278");
            printer.printRecItem("Чай чер. бергам. 82736827648726", 0, 123, 0, 90, "");
            // Item 2
            printer.printNormal(2, "  4605246004275");
            printer.printRecItem("Чай черный золот. 374683756873465", 5290, 1000, 0, 0, "");
            // Item 3
            printer.printNormal(2, "  4605246004261");
            printer.printRecItem("Ахмад Грей пб/я 394587938457", 60290, 1000, 0, 0, "");
            // Item 4
            printer.printNormal(2, "  4605246004262");
            printer.printRecItem("Брук Бонд чай 3945793857938457", 690, 1000, 0, 0, "");
            // Subtotal
            printer.printRecTotal(66281, 1, "0");
            printer.printRecTotal(66281, 2, "1");
            printer.printRecTotal(66281, 3, "2");
            printer.printRecTotal(66281, 4, "3");
            printer.printRecTotal(66281, 66281, "0");
            printer.endFiscalReceipt(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printSalesReceipt7() {
        try {
            printer.setFiscalReceiptType(FPTR_RT_SALES);
            printer.beginFiscalReceipt(true);
            printer.printRecItem("Weight item", 18900, 1000, 0, 18900, "kg.");
            printer.printRecTotal(18900, 18900, "0");
            printer.endFiscalReceipt(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void zeroFiscalReceipt() {
        try {
            printer.setFiscalReceiptType(FPTR_RT_SALES);
            printer.beginFiscalReceipt(true);
            printer.printRecItem("Zero receipt", 0, 0, 0, 0, "kg.");
            printer.printRecTotal(0, 0, "1");
            printer.endFiscalReceipt(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printSpeedTest() {
        try {
            printer.beginNonFiscal();
            for (int i = 1; i <= 200; i++) {
                printer.printNormal(FPTR_S_RECEIPT, "Line " + i);
            }
            printer.endNonFiscal();
        } catch (JposException e) {
            System.out.println("JposException");
            System.out.println("ErrorCode: " + String.valueOf(e.getErrorCode()));
            System.out.println("ErrorCodeExtended: " + String.valueOf(e.getErrorCodeExtended()));
            System.out.println("Text: " + e.getMessage());

            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printSpeedTest2() {
        try {
            printer.resetPrinter();
            printer.setFiscalReceiptType(FPTR_RT_SALES);
            printer.beginFiscalReceipt(true);
            for (int i = 1; i <= 200; i++) {
                printer.printRecItem("Item " + i, 1, 1000, 0, 0, "");
            }
            printer.printRecTotal(10000, 10000, "1");
            printer.endFiscalReceipt(true);
        } catch (JposException e) {
            System.out.println("JposException");
            System.out.println("ErrorCode: " + String.valueOf(e.getErrorCode()));
            System.out.println("ErrorCodeExtended: " + String.valueOf(e.getErrorCodeExtended()));
            System.out.println("Text: " + e.getMessage());

            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printNonFiscal() {
        try {
            printer.beginNonFiscal();
            printer.printNormal(FPTR_S_RECEIPT, getLine(""));
            printer.printNormal(FPTR_S_RECEIPT, getLine("Nonfiscal receipt"));
            printer.printNormal(FPTR_S_RECEIPT, getLine(""));

            printer.printNormal(FPTR_S_RECEIPT, "Строка1\rСтрока2\nСтрока3\r\nСтрока4\r\rСтрока6");
            printer.printNormal(FPTR_S_RECEIPT, "#*~*#http://check.egais.ru?id=38d02af6-bfd2-409f-8041-b011d8160700&dt=2311161430&cn=030000290346");

            printer.endNonFiscal();
            //ShortPrinterStatus sStatus = printer.readShortPrinterStatus();
            System.out.println("PrinterTest.printNonFiscal()");
        } catch (JposException e) {
            System.out.println("JposException");
            System.out.println("ErrorCode: " + String.valueOf(e.getErrorCode()));
            System.out.println("ErrorCodeExtended: " + String.valueOf(e.getErrorCodeExtended()));
            System.out.println("Text: " + e.getMessage());

            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printImage(String fileName) {
        try {
            printer.beginNonFiscal();
            printer.printNormal(FPTR_S_RECEIPT, getLoadImageCommand2(fileName));
            printer.endNonFiscal();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printFSDiscountTestReceipt() {
        try {
            printer.resetPrinter();

            printer.setFiscalReceiptType(FPTR_RT_SALES);
            printer.beginFiscalReceipt(true);
            printer.printRecItem("Чай чер. бергам. 2*25п", 50, 1, 2, 0, "");
            printer.printRecItemAdjustment(1, "Тест М за N", 10, 1);
//            printer.printRecItemAdjustment(1, "Тест М за N2!", 5, 1);
            printer.printRecItem("Чай чер. бергам. 11*25п", 50, 1, 2, 0, "");
            printer.printRecItemAdjustment(1, "Тест М за N", 10, 1);
            printer.printRecItemAdjustment(1, "Тест М за N2!", 5, 1);
            printer.printRecSubtotal(100);
            printer.printRecTotal(100, 100, "0");
            printer.endFiscalReceipt(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printCashInReceipt() {
        try {
            printer.setFiscalReceiptType(FPTR_RT_CASH_IN);
            printer.beginFiscalReceipt(true);
            printer.printRecCash(100);
            printer.printRecTotal(0, 100, "0");
            printer.printNormal(FiscalPrinterConst.FPTR_S_RECEIPT, "PrintNormal");
            printer.printRecMessage("printRecMessage");
            printer.endFiscalReceipt(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printCancelledCashInReceipt()
            throws Exception {
        printer.setFiscalReceiptType(FPTR_RT_CASH_IN);
        printer.beginFiscalReceipt(true);
        printer.printRecVoid("Receipt cancelled");
        printer.endFiscalReceipt(true);
    }

    public void printCancelledCashInReceipt2()
            throws Exception {
        printer.setFiscalReceiptType(FPTR_RT_CASH_IN);
        printer.beginFiscalReceipt(true);
        printer.printRecCash(100);
        printer.printRecVoid("Receipt cancelled");
        printer.endFiscalReceipt(true);
    }

    public void printCancelledCashInReceipt3()
            throws Exception {
        printer.setFiscalReceiptType(FPTR_RT_CASH_IN);
        printer.beginFiscalReceipt(true);
        printer.printRecCash(100);
        printer.printRecTotal(0, 100, "0");
        printer.printRecVoid("Receipt cancelled");
        printer.endFiscalReceipt(true);

    }

    public void printCashOutReceipt() {
        try {
            printer.setFiscalReceiptType(FPTR_RT_CASH_OUT);
            printer.beginFiscalReceipt(true);
            printer.printRecCash(100);
            printer.printRecTotal(0, 100, "0");
            printer.printNormal(FiscalPrinterConst.FPTR_S_RECEIPT, "PrintNormal");
            printer.printRecMessage("printRecMessage");
            printer.endFiscalReceipt(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printCancelledCashOutReceipt()
            throws Exception {
        printer.setFiscalReceiptType(FPTR_RT_CASH_OUT);
        printer.beginFiscalReceipt(true);
        printer.printRecVoid("Receipt cancelled");
        printer.endFiscalReceipt(true);
    }

    public void fiscalReceipts() {
        try {
            printCashInReceipt();
            printCancelledCashInReceipt();
            printCashOutReceipt();
            printCancelledCashOutReceipt();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printTextFile(String fileName) {
        try {
            printer.beginNonFiscal();

            String line = "";
            FileReader reader = new FileReader(fileName);
            try {
                BufferedReader br = new BufferedReader(reader);
                while (br.ready()) {
                    line = br.readLine();
                    printer.printNormal(FPTR_S_RECEIPT, line);
                }
                printer.endNonFiscal();
            } finally {
                reader.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printZReport() {
        try {
            printer.printZReport();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printDuplicateReceipt() {
        try {
            printer.printDuplicateReceipt();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printXReport() {
        try {
            printer.printXReport();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void VatTest() {
        try {
            if (printer.getCapHasVatTable()) {
                int numVatRates = printer.getNumVatRates();
                System.out.println("NumVatRates: " + String.valueOf(numVatRates));
                // set vat rates
                for (int i = 1; i <= numVatRates; i++) {
                    printer.setVatValue(i, String.valueOf(1234 * i));
                }
                printer.setVatTable();
                // get vat rates
                for (int i = 1; i <= numVatRates; i++) {
                    int[] vatRate = new int[1];
                    printer.getVatEntry(i, 0, vatRate);
                    System.out.println("VatRate " + String.valueOf(i)
                            + "  : " + String.valueOf(vatRate[0]));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clearImages() {
        try {
            printer.clearImages();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadImages() {
        try {
            printer.beginNonFiscal();
            printer.printNormal(FPTR_S_RECEIPT, "Load image receipt");

            // delete all images
            printer.printNormal(FPTR_S_RECEIPT, "Delete all images...");
            try {
                printer.clearImages();
                printer.printNormal(FPTR_S_RECEIPT, "OK");
            } catch (JposException e) {
                printer.printNormal(FPTR_S_RECEIPT, e.getMessage());
            }

            // load images from files
            for (int i = 0; i < 10; i++) {
                long startTime = System.currentTimeMillis();
                String fileName = "Logo" + String.valueOf(i) + ".bmp";
                printer.printNormal(FPTR_S_RECEIPT,
                        "Loading IMAGE" + String.valueOf(i) + " from file "
                        + fileName + " ...");
                try {
                    printer.loadImage(fileName);
                    printer.printNormal(FPTR_S_RECEIPT, "OK, "
                            + String.valueOf(System.currentTimeMillis() - startTime)
                            + " ms.");
                } catch (JposException e) {
                    printer.printNormal(FPTR_S_RECEIPT, e.getMessage());
                }
            }
            printer.endNonFiscal();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printImages() {
        try {
            printer.beginNonFiscal();
            printer.printNormal(FPTR_S_RECEIPT, "Print image receipt");
            for (int i = 0; i < 10; i++) {
                printer.printNormal(FPTR_S_RECEIPT, "IMAGE "
                        + String.valueOf(i) + ":");
                try {
                    printer.printImage(i);
                } catch (JposException e) {
                    printer.printNormal(FPTR_S_RECEIPT, e.getMessage());
                }
            }
            printer.endNonFiscal();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printNonfiscalReceipt3() {
        try {
            printer.beginNonFiscal();
            String text = "Line 1\r\n" + getLoadImageCommand("Logo.bmp") + "Line2";
            printer.printNormal(FPTR_S_RECEIPT, text);
            printer.endNonFiscal();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printNonfiscalReceipt4() {
        try {
            printer.printText("ЗИМНЕЕ УТРО", FontNumber.getBoldFont());
            printer.printText(" ", FontNumber.getItalicFont());
            printer.printText(" ", FontNumber.getItalicFont());
            printer.printText(" ", FontNumber.getItalicFont());
            printer.printText(" ", FontNumber.getItalicFont());
            printer.printText("Мороз и солнце; день чудесный!", FontNumber.getItalicFont());
            printer.printText("Еще ты дремлешь, друг прелестный - ", FontNumber.getItalicFont());
            printer.printText("Пора, красавица, проснись:", FontNumber.getItalicFont());
            printer.printText("Открой сомкнуты негой взоры", FontNumber.getItalicFont());
            printer.printText("Навстречу северной Авроры,", FontNumber.getItalicFont());
            printer.printText("Звездою севера явись!", FontNumber.getItalicFont());
            printer.printText("#*~*#1723716538475683723712", FontNumber.getItalicFont());
            printer.printText(" ", FontNumber.getItalicFont());
            printer.printText(" ", FontNumber.getItalicFont());
            printer.printText(" ", FontNumber.getItalicFont());
            printer.printText(" ", FontNumber.getItalicFont());
            printer.cutPaper(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        printPaperReport();
    }

    public void printLines() {
        try {
            printer.beginNonFiscal();
            printer.printNormal(FPTR_S_RECEIPT, "Print black line receipt");
            for (int i = 0; i < 3; i++) {
                int lineHeight = 1 + i * 2;
                printer.printNormal(FPTR_S_RECEIPT, "Line height: "
                        + String.valueOf(lineHeight));
                try {
                    printer.printLine(lineHeight,
                            SmFptrConst.SMFPTR_LINE_TYPE_BLACK);
                    printer.printNormal(FPTR_S_RECEIPT, "OK");
                } catch (JposException e) {
                    printer.printNormal(FPTR_S_RECEIPT, e.getMessage());
                }
            }
            printer.endNonFiscal();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clearReceiptLogo() {
        try {
            // clear all receipt logo
            printer.clearLogo();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setReceiptLogo() {
        try {
            // clear all receipt logo
            printer.clearLogo();
            // set receipt logo0
            printer.addLogo(0, SmFptrConst.SMFPTR_LOGO_AFTER_HEADER);
            // set receipt logo1
            printer.addLogo(1, SmFptrConst.SMFPTR_LOGO_BEFORE_TRAILER);
            // set receipt logo2
            printer.addLogo(2, SmFptrConst.SMFPTR_LOGO_AFTER_ADDTRAILER);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printFonts() {
        try {
            printer.beginNonFiscal();
            printer.printNormal(FPTR_S_RECEIPT, "Print lines with fonts");
            for (int i = 1; i < 8; i++) {
                String line = "Text line, font " + String.valueOf(i);
                printer.printText(line, new FontNumber(i));
            }
            printer.endNonFiscal();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printQRCode2() {
        try {
            printer.beginNonFiscal();
            printer.printNormal(FPTR_S_RECEIPT, "#*~*#http://check.egais.ru?id=38d02af6-bfd2-409f-8041-b011d8160700&dt=2311161430&cn=030000290346");
            printer.endNonFiscal();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void openCashDrawer() {
        try {
            printer.openCashDrawer(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isDrawerOpened() {
        try {
            return printer.readDrawerState();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String readStatus() {
        String result = "";
        try {
            result = "";

            result = result
                    + "Receipt number: " + String.valueOf(printer.getReceiptNumber()) + "\n";

            result = result
                    + "Printer serial: " + printer.readSerial() + "\n";

            result = result
                    + "Electronic journal serial: " + printer.readEJSerial() + "\n";

            PrinterStatus status = printer.readPrinterStatus();
            result = result
                    + "Printer mode: " + String.valueOf(status.getMode())
                    + ", " + status.getPrinterMode().getText() + "\n";

            result = result
                    + "Printer submode: " + String.valueOf(status.getSubmode())
                    + ", " + PrinterSubmode.getText(status.getSubmode()) + "\n";

            result = result
                    + "Printer flags: " + String.valueOf(status.getFlags()) + "\n";

            PrinterFlags flags = status.getPrinterFlags();

            result = result
                    + "Journal paper is near end: "
                    + String.valueOf(flags.isJrnNearEnd()) + "\n";

            result = result
                    + "Receipt paper is near end: "
                    + String.valueOf(flags.isRecNearEnd()) + "\n";

            result = result
                    + "Paper on top slip station sensor: "
                    + String.valueOf(flags.isSlpEmpty()) + "\n";

            result = result
                    + "Paper on bottom slip station sensor: "
                    + String.valueOf(flags.isSlpNearEnd()) + "\n";

            result = result
                    + "Amount point position: "
                    + String.valueOf(flags.getAmountPointPosition()) + "\n";

            result = result
                    + "Electronic journal is present: "
                    + String.valueOf(flags.isEJPresent()) + "\n";

            result = result
                    + "Journal paper is empty: "
                    + String.valueOf(flags.isJrnEmpty()) + "\n";

            result = result
                    + "Receipt paper is empty: "
                    + String.valueOf(flags.isRecEmpty()) + "\n";

            result = result
                    + "Journal lever is up: "
                    + String.valueOf(flags.isJrnLeverUp()) + "\n";

            result = result
                    + "Receipt lever is up: "
                    + String.valueOf(flags.isRecLeverUp()) + "\n";

            result = result
                    + "Cover is opened: "
                    + String.valueOf(flags.isCoverOpened()) + "\n";

            result = result
                    + "Cash drawer is opened: "
                    + String.valueOf(flags.isDrawerOpened()) + "\n";

            result = result
                    + "Left printer sensor failure: "
                    + String.valueOf(flags.isLSensorFailure()) + "\n";

            result = result
                    + "Right printer sensor failure: "
                    + String.valueOf(flags.isRSensorFailure()) + "\n";

            result = result
                    + "Electronic journal is near end: "
                    + String.valueOf(flags.isJrnNearEnd()) + "\n";

            result = result
                    + "Extended quantity: "
                    + String.valueOf(flags.isExtQuantity()) + "\n";

            result = result
                    + "Operator number: "
                    + String.valueOf(status.getOperator()) + "\n";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public String readCashRegisters() {
        String result = "";
        try {
            result = "";
            for (int i = 0; i < 253; i++) {
                CashRegister reg = printer.readCashRegister(i);
                result = result + String.valueOf(i) + ". "
                        + String.valueOf(reg.getValue()) + ", "
                        + String.valueOf(reg.getName(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public String getDailyTotal() {
        String result = "";
        try {
            result += "Daily total            : "
                    + printer.getDailyTotal() + "\r\n";

            result += "Cash daily total       : " + printer.getDailyTotal(
                    SmFptrConst.SMFPTR_DAILY_TOTAL_CASH) + "\r\n";

            result += "Pay type 2 daily total : " + printer.getDailyTotal(
                    SmFptrConst.SMFPTR_DAILY_TOTAL_PT2) + "\r\n";

            result += "Pay type 3 daily total : " + printer.getDailyTotal(
                    SmFptrConst.SMFPTR_DAILY_TOTAL_PT3) + "\r\n";

            result += "Pay type 4 daily total : " + printer.getDailyTotal(
                    SmFptrConst.SMFPTR_DAILY_TOTAL_PT4) + "\r\n";

            return result;

        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    public String getGrandTotal() {
        String result = "";
        try {
            result += "Grand total from first fiscalization:\r\n";
            result += printer.getGrandTotal(0) + "\r\n";
            result += "Grand total from last fiscalization: \r\n";
            result += printer.getGrandTotal(1) + "\r\n";
            return result;

        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    public String readMaxGraphics() {
        String result = "";
        try {
            result = "Max graphics height: "
                    + String.valueOf(printer.readMaxGraphics());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public String readOperRegisters() {
        String result = "";
        try {
            result = "";
            for (int i = 0; i < 253; i++) {
                OperationRegister reg = printer.readOperRegister(i);
                result = result + String.valueOf(i) + ". "
                        + String.valueOf(reg.getValue()) + ", "
                        + String.valueOf(reg.getName(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public void saveXmlZReport() {
        try {
            printer.saveXmlZReport("ZReport.xml");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveCsvZReport() {
        try {
            printer.saveCsvZReport("ZReport.csv");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void readStatus1() {
        try {
            printer.readStatus1();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void readStatus2() {
        try {
            printer.readStatus2();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void testSynchronization(String fptrDeviceName,
            String cashDeviceName) {
        try {
            logger.debug("testSynchronization");
            int i = 0;
            Thread thread = null;
            int maxThreadCount = 1;
            Thread[] threads = new Thread[maxThreadCount * 2];
            for (i = 0; i < maxThreadCount; i++) {
                FiscalPrinterTest test1 = new FiscalPrinterTest(fptrDeviceName, encoding);
                thread = new Thread(test1);
                thread.start();
                threads[i] = thread;
            }

            for (i = 0; i < maxThreadCount; i++) {
                CashDrawerTest test2 = new CashDrawerTest(cashDeviceName, encoding);
                thread = new Thread(test2);
                thread.start();
                threads[maxThreadCount + i] = thread;
            }

            for (i = 0; i < threads.length; i++) {
                threads[i].join();
            }

            logger.debug("testSynchronization: OK");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("testSynchronization", e);
        }
    }

    public class FiscalPrinterTest implements Runnable {

        private String resultText = "";
        private final String deviceName;
        private final String encoding;
        private CompositeLogger logger = CompositeLogger.getLogger(FiscalPrinterTest.class);

        public FiscalPrinterTest(String deviceName, String encoding) {
            this.deviceName = deviceName;
            this.encoding = encoding;
        }

        public String getResultText() {
            return resultText;
        }

        public void run() {
            try {
                logger.debug("run");
                ShtrihFiscalPrinter printer = new ShtrihFiscalPrinter(
                        new FiscalPrinter(), encoding);
                printer.open(deviceName);
                printer.setPowerNotify(printer.JPOS_PN_ENABLED);
                printer.claim(0);
                printer.setDeviceEnabled(true);
                //printer.close();

                logger.debug("OK");
            } catch (Exception e) {
                logger.error("Failed", e);
                resultText = "Failed: " + e.getMessage();
            }
        }
    }

    public class CashDrawerTest implements Runnable {

        private String resultText = "";
        private final String deviceName;
        private final String encoding;
        private CompositeLogger logger = CompositeLogger.getLogger(CashDrawerTest.class);

        public CashDrawerTest(String deviceName, String encoding) {
            this.deviceName = deviceName;
            this.encoding = encoding;
        }

        public String getResultText() {
            return resultText;
        }

        public void run() {
            try {
                logger.debug("run");
                CashDrawer drawer = new CashDrawer();
                drawer.open(deviceName);
                drawer.claim(0);
                drawer.setDeviceEnabled(true);
                //drawer.close();

                logger.debug("OK");
            } catch (Exception e) {
                logger.error("Failed", e);
                resultText = "Failed: " + e.getMessage();
            }
        }
    }

    public void printNCRFiscalReceipt() {
        String[] headerLines = {
            "                                                ",
            "             34400, Rostov-Na-Donu              ",
            "             RIKHARDA ZORGE UL. 33              ",
            "                ???. 79081735904                "
        };

        String[] trailerLines = {
            "              Спасибо за покупку!        ",
            "          Будем рады Вас видеть снова!   "
        };

        try {
            printer.clearLogo();
            printer.clearImages();
            String fileName = "Logo.bmp";
            int imageIndex = printer.loadImage(fileName);
            printer.addLogo(imageIndex, SmFptrConst.SMFPTR_LOGO_BEFORE_HEADER);

            printer.setAdditionalHeader("");
            printer.setAdditionalTrailer("");
            setHeaderLines(headerLines);
            //setTrailerLines(trailerLines);
            printer.setFiscalReceiptType(FPTR_RT_SALES);
            printer.beginFiscalReceipt(false);
            // Item 1 
            printer.setFontNumber(3);
            printer.printNormal(2, "  4605246004278");
            printer.setFontNumber(1);
            printer.printRecItem("Чай чер. бергам. 2*25п", 1000290, 1, 0, 0, "");
            // Item 2
            printer.setFontNumber(3);
            printer.printNormal(2, "  4605246004275");
            printer.setFontNumber(1);
            printer.printRecItem("Чай черный золотой цейлон", 5290, 1, 0, 0, "");
            // Item 3
            printer.setFontNumber(3);
            printer.printNormal(2, "  4605246004261");
            printer.setFontNumber(1);
            printer.printRecItem("Ахмад Грей пб/я 40x2", 60290, 1, 0, 0, "");
            // Item 4
            printer.setFontNumber(3);
            printer.printNormal(2, "  4605246004262");
            printer.setFontNumber(1);
            printer.printRecItem("Брук Бонд чай 50пак", 690, 15, 0, 0, "");
            // Discount
            printer.printRecSubtotalAdjustment(1, "СКИДКА ОКРУГЛЕНИЯ:", 10);
            // Subtotal
            printer.printRecSubtotal(1070000);
            printer.printRecTotal(1070000, 1070000, "0");
            printer.endFiscalReceipt(true);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printCancelledReceipt() {
        try {
            printer.setAdditionalHeader("");
            printer.setAdditionalTrailer("");
            printSeparator();
            printer.setFiscalReceiptType(FPTR_RT_SALES);
            printer.beginFiscalReceipt(true);
            // Item 1 
            printer.setFontNumber(3);
            printer.printNormal(2, "  4605246004278");
            printer.setFontNumber(1);
            printer.printRecItem("Чай чер. бергам. 2*25п", 1000290, 1, 0, 0, "");
            printer.endFiscalReceipt(false);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printRoundTest() {
        try {
            printer.setAdditionalHeader("");
            printer.setAdditionalTrailer("");
            printer.setFiscalReceiptType(FPTR_RT_SALES);
            printer.beginFiscalReceipt(true);
            // Item 1 
            printer.printRecItem("Item 1", 0, 1, 1, 3595, "");

            printer.printRecTotal(3, 3, "0");
            printer.printRecTotal(1, 1, "0");
            printer.endFiscalReceipt(false);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setHeaderLines(String[] lines)
            throws Exception {
        printer.setNumHeaderLines(lines.length);
        int count = printer.getNumHeaderLines();
        count = Math.min(count, lines.length);
        for (int i = 1; i <= count; i++) {
            printer.setHeaderLine(i, lines[i - 1], false);
        }
    }

    public void setTrailerLines(String[] lines)
            throws Exception {
        printer.setNumTrailerLines(lines.length);
        int count = printer.getNumTrailerLines();
        count = Math.min(count, lines.length);
        for (int i = 1; i <= count; i++) {
            printer.setTrailerLine(i, lines[i - 1], false);
        }
    }

    public void printSeparator()
            throws Exception {
        printer.printLine(SmFptrConst.SMFPTR_LINE_TYPE_WHITE, 5);
        printer.printLine(SmFptrConst.SMFPTR_LINE_TYPE_BLACK, 3);
        printer.printLine(SmFptrConst.SMFPTR_LINE_TYPE_WHITE, 5);
    }

    public void printBlackSeparator()
            throws Exception {
        printer.printText("------------------------------------------");
        //printer.printLine(SmFptrConst.SMFPTR_LINE_TYPE_BLACK, 3);
    }

    public void testCashDrawer(ShtrihCashDrawer driver) {
        try {
            driver.open("ShtrihCashDrawer");
            driver.claim(0);
            driver.setDeviceEnabled(true);
            driver.setDrawerNumber(5);
            if (driver.getDrawerNumber() != 5) {
                throw new Exception("getDrawerNumber");
            }
            driver.readDrawerState();
            driver.openCashDrawer(2);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printRefundReceipt() {
        try {
            printer.setFiscalReceiptType(FiscalPrinterConst.FPTR_RT_REFUND);
            printer.beginFiscalReceipt(false);
            printer.printRecMessage(" ");
            printer.printRecMessage("                  Кассовый чек                  ");
            printer.printRecItemRefund("Coca-cola 2l", 13302, 1000, 1, 13302, "");
            printer.printRecItemRefund("Coca-cola 2l", 13302, 1000, 1, 13302, "");
            printer.printRecRefundVoid("Coca-cola 2l", 13302, 1);
            printer.printRecSubtotal(13302);
            printer.printRecSubtotalAdjustment(FiscalPrinterConst.FPTR_AT_AMOUNT_DISCOUNT, "", 002);
            printer.printRecSubtotal(13300);
            printer.printRecTotal(13300, 13300, "00");
            printer.printRecMessage("*На артикул не предоставляется скидка");
            printer.printRecMessage(" ");
            printer.printRecMessage("Касса:1                  Кассир:GK Administrator");
            printer.endFiscalReceipt(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printFiscalReceipt2() {
        try {
            printer.resetPrinter();

            long startAmount = printer.readCashRegister(242).getValue();
            while (true) {
                printer.setFiscalReceiptType(FiscalPrinterConst.FPTR_RT_CASH_IN);
                printer.beginFiscalReceipt(false);
                printer.printRecCash(200000);
                printer.printRecTotal(200000, 200000, "0");
                printer.endFiscalReceipt(false);

                long amount = printer.readCashRegister(242).getValue();
                if (amount != 0) {
                    break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public long amount(double value) {
        return (long) (value * 100);
    }

    public int quantity(double value) {
        return (int) (value * 1000);
    }

    public void printSaleWithFullDiscount() throws JposException {
        if (!printer.getDayOpened() && printer.getCapSetPOSID()) {
            printer.setPOSID("5", "Cashier5");
        }

        printer.setFiscalReceiptType(FiscalPrinterConst.FPTR_RT_SALES);
        printer.beginFiscalReceipt(true);

        printer.printRecItem("1 31380 KLEENEX ?????? ???.??????. 10??", amount(7.95), quantity(1), 1, amount(7.95), "");
        printer.printRecItem("2 506 PAUL.???? PRESIDENT ???.250?", amount(304.00), quantity(1), 1, amount(304.00), "");

        printer.printRecSubtotalAdjustment(2, "", amount(0.01));
        printer.printRecSubtotalAdjustment(1, "", amount(311.95));
        printer.printRecSubtotal(amount(0.01));

        printer.printRecTotal(amount(0.01), amount(0.01), "00");

        printer.endFiscalReceipt(true);
    }

    public void printSaleWithFullDiscount2() throws JposException {
        logger.debug("FP start state: " + printer.getPrinterState());

        if (!printer.getDayOpened() && printer.getCapSetPOSID()) {
            printer.setPOSID("5", "Cashier5");
        }

        printer.setFiscalReceiptType(FiscalPrinterConst.FPTR_RT_SALES);
        printer.beginFiscalReceipt(true);

        printer.printRecItem("1 31380 KLEENEX ?????? ???.??????. 10??", amount(7.95), quantity(1), 1, amount(7.95), "");
        printer.printRecItemAdjustment(1, "Kundegruppe rabbat - 100% rabbat", amount(7.95), 1);

        printer.printRecSubtotalAdjustment(2, "", amount(0.01));

        printer.printRecItem("2 506 PAUL.???? PRESIDENT ???.250?", amount(304.00), quantity(1), 1, amount(304.00), "");
        printer.printRecItemAdjustment(1, "Kundegruppe rabbat - 100% rabbat", amount(304.00), 1);

        printer.printRecSubtotal(amount(0.00));
        printer.printRecSubtotal(amount(0.01));

        printer.printRecTotal(amount(0.01), amount(0.01), "00");

        printer.endFiscalReceipt(true);

        logger.debug("FP start state: " + printer.getPrinterState());
    }

    public void printSaleWithFullDiscount3() throws JposException {
        if (!printer.getDayOpened() && printer.getCapSetPOSID()) {
            printer.setPOSID("5", "Cashier5");
        }

        printer.setFiscalReceiptType(FiscalPrinterConst.FPTR_RT_REFUND);
        printer.beginFiscalReceipt(true);

        printer.printRecItemRefund("1 31380 KLEENEX ?????? ???.??????. 10??", amount(304.00), quantity(1), 1, amount(304.00), "");

        printer.printRecItemRefund("2 506 PAUL.???? PRESIDENT ???.250?", amount(304.00), quantity(1), 1, amount(304.00), "");

        printer.printRecSubtotalAdjustment(2, "", amount(0.01));
        printer.printRecSubtotalAdjustment(1, "", amount(608.00));
        printer.printRecSubtotal(amount(0.01));

        printer.printRecTotal(amount(0.01), amount(0.01), "00");

        printer.endFiscalReceipt(true);
    }

    public void printSaleWithFullDiscount4() throws JposException {
        if (!printer.getDayOpened() && printer.getCapSetPOSID()) {
            printer.setPOSID("5", "Cashier5");
        }

        printer.setFiscalReceiptType(FiscalPrinterConst.FPTR_RT_REFUND);
        printer.beginFiscalReceipt(true);

        printer.printRecItemRefund("1 506 PAUL.???? PRESIDENT ???.250?", amount(304.00), quantity(1), 1, amount(304.00), "");
        printer.printRecItemAdjustment(1, "Kundegruppe rabbat - 100% rabbat", amount(304.00), 1);

        printer.printRecItemRefund("2 506 PAUL.???? PRESIDENT ???.250?", amount(304.00), quantity(1), 1, amount(304.00), "");
        printer.printRecItemAdjustment(1, "Kundegruppe rabbat - 100% rabbat", amount(304.00), 1);

        printer.printRecSubtotal(amount(0.00));
        printer.printRecSubtotalAdjustment(2, "", amount(0.01));
        printer.printRecSubtotal(amount(0.01));

        printer.printRecTotal(amount(0.01), amount(0.01), "00");

        printer.endFiscalReceipt(true);
    }
}
