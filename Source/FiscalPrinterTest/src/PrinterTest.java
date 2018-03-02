/////////////////////////////////////////////////////////////////////
//
// BaseJposService.java - Abstract base class for all JavaPOS services.
//
// Modification history
// ------------------------------------------------------------------
// 2007-07-24 JavaPOS Release 1.0                                  VK
//
/////////////////////////////////////////////////////////////////////

import java.util.*;
import java.io.FileReader;
import java.io.BufferedReader;

import com.google.zxing.EncodeHintType;
import com.google.zxing.pdf417.encoder.Dimensions;
import com.shtrih.fiscalprinter.TLVWriter;
import jpos.CashDrawer;
import jpos.JposException;
import jpos.FiscalPrinter;
import jpos.FiscalPrinterConst;
import com.shtrih.util.CompositeLogger;
import com.shtrih.fiscalprinter.FontNumber;
import com.shtrih.cashdrawer.ShtrihCashDrawer;
import com.shtrih.jpos.fiscalprinter.SmFptrConst;
import com.shtrih.fiscalprinter.ShtrihFiscalPrinter;
import com.shtrih.printer.model.NCR7167;
import com.shtrih.fiscalprinter.command.*;
import com.shtrih.util.Hex;
import com.shtrih.barcode.PrinterBarcode;
import com.shtrih.util.StringUtils;

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
            //printer.clearLogo();
            //printer.clearImages();
            //printer.loadImage("Logo.bmp");
            //printer.addLogo(0, SmFptrConst.SMFPTR_LOGO_BEFORE_HEADER);
            // 
            printer.setHeaderLine(1, getLoadImageCommand("Logo.bmp"), false);
            printer.setHeaderLine(2, "Header line 2", false);
            printer.setHeaderLine(3, "Header line 3", false);
            printer.setHeaderLine(4, "Header line 4", false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setTrailerLines() {
        try {
            printer.setTrailerLine(1, "Trailer line 1", false);
            printer.setTrailerLine(2, "Trailer line 2", false);
            printer.setTrailerLine(3, "Trailer line 3", false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setHeaderLines3() {
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
        printCode128();
        //printEan13();
        //printQRCode();
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
            long time = System.currentTimeMillis();

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

            time = System.currentTimeMillis() - time;
            printer.printNormal(FPTR_S_RECEIPT, "Time: " + time + " ms.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printCode128_1() {
        try {
            Object[] params = new Object[9];
            params[0] = "001C0FC020617101000278"; // barcode data
            params[1] = "001C0FC020617101000278"; // barcode label
            params[2] = new Integer(SmFptrConst.SMFPTR_BARCODE_CODE128); // barcode type
            params[3] = new Integer(100); // barcode height in pixels
            params[4] = new Integer(1); // print type
            params[5] = new Integer(2); // barcode bar width in pixels
            params[6] = new Integer(0); // text position
            params[7] = new Integer(1); // text font
            params[8] = new Integer(3); // narrow to width ratio, 3 by default
            printer.directIO(SmFptrConst.SMFPTR_DIO_PRINT_BARCODE, null, params);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void printAztecBarcode() {
        try {
            PrinterBarcode barcode = new PrinterBarcode();
            barcode.setAspectRatio(3);
            barcode.setPrintType(SmFptrConst.SMFPTR_PRINTTYPE_DRIVER);
            barcode.setTextFont(1);
            barcode.setTextPosition(SmFptrConst.SMFPTR_TEXTPOS_ABOVE);

            barcode.setBarWidth(4);
            barcode.setText(
                    "https://checkl.fsrar.ru?id=fa07210-0041-4dc6-"
                    + "bbf2-1634282724418amdt=191015161"
                    + "71amcn=0100000062870D0682B61230689D76826FAC92C5DC29955F0E3B5663B4"
                    + "4C63A673C86B0976C0B24495848F6EF157792203A0D275"
                    + "1F525456644096478D256A910EFEABB67");
            barcode.setLabel("Aztec: ");
            barcode.setType(SmFptrConst.SMFPTR_BARCODE_AZTEC);
            printer.printBarcode(barcode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void printCode128() {
        try {
            final int BARCODE_HEIGHT = 100;
            final int BARCODE_WIDTH = 2;
            final int TEXT_FONT = 1;
            final int ASPECT_RATIO = 2;
            final String text = "55111061711";

            PrinterBarcode barcode = new PrinterBarcode();
            barcode.setAspectRatio(ASPECT_RATIO);
            barcode.setBarWidth(BARCODE_WIDTH);
            barcode.setHeight(BARCODE_HEIGHT);
            barcode.setLabel(text);
            barcode.setPrintType(SmFptrConst.SMFPTR_PRINTTYPE_DRIVER);
            barcode.setText(text);
            barcode.setTextFont(TEXT_FONT);
            barcode.setTextPosition(SmFptrConst.SMFPTR_TEXTPOS_ABOVE);
            barcode.setType(SmFptrConst.SMFPTR_BARCODE_CODE128);
            printer.printBarcode(barcode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printEan13() {
        try {
            printer.printBarcode("2223183256141", "2223183256141",
                    2, 45, 1, 3, 1, 1, 3);

            printer.printBarcode("222318325614", "2223183256141",
                    2, 45, 1, 3, 1, 1, 3);
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

    public void printEscBarcodesNormal2() {
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

    public void printEscBarcodesNormal() {
        try {
            printer.beginNonFiscal();

            byte b[] = {0x1B, 0x61, 0x01};
            printer.printNormal(FPTR_S_RECEIPT, new String(b));
            byte b1[] = {0x1D, 0x48, 0x02};
            printer.printNormal(FPTR_S_RECEIPT, new String(b1));
            byte b2[] = {0x1D, 0x68, 0x30};
            printer.printNormal(FPTR_S_RECEIPT, new String(b2));
            byte b3[] = {0x1D, 0x77, 0x02};
            printer.printNormal(FPTR_S_RECEIPT, new String(b3));
            byte b4[] = {0x1D, 0x6B, 0x43, 0x0C};
            printer.printNormal(FPTR_S_RECEIPT, new String(b4) + "207013000741");

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

            //printCheckWithSmallSum();
            //readFSStatus();
            //findFSDocument();
            /*
            readLastDayOpen();
            readLastDayClose();
            readLastReceipt();
            
            String[] lines = new String[1];
            printer.getData(FPTR_GD_PRINTER_ID, null, lines);
            System.out.println("FPTR_GD_PRINTER_ID: " + lines[0]);
            printer.setPOSID("34", "Кравцов В.В.");
            List<String> results = new ArrayList<String>();
            printer.directIO(SmFptrConst.SMFPTR_DIO_READ_FS_PARAMS, null, results);

            FSStatusInfo fsStatus = printer.fsReadStatus();
            FSCommunicationStatus fsCommunicationStatus = printer.fsReadCommStatus();

            System.out.println("FSSerialNumber: " + fsStatus.getFsSerial());
            System.out.println("Rnm: " + results.get(1));
            System.out.println("Unsent documents: " + fsCommunicationStatus.getUnsentDocumentsCount());
            System.out.println("Cash in drawer: " + printer.readCashRegister(241).getValue() / 100.0d);
             */
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void readFSStatus() {
        System.out.println("readFSStatus");
        try {
            int[] data = null;
            String[] lines = new String[9];
            printer.directIO(SmFptrConst.SMFPTR_DIO_FS_READ_STATUS, data, lines);

            System.out.println("Lifecycle code  : " + lines[0]);
            System.out.println("Document type   : " + lines[1]);
            System.out.println("isDocReceived   : " + lines[2]);
            System.out.println("isDayOpened     : " + lines[3]);
            System.out.println("Flags           : " + lines[4]);
            System.out.println("Date            : " + lines[5]);
            System.out.println("Time            : " + lines[6]);
            System.out.println("FS serial       : " + lines[7]);
            System.out.println("Document number : " + lines[8]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void findFSDocument() {
        System.out.println("findFSDocument");
        try {
            int[] data = new int[1];
            data[0] = 1;
            String[] lines = new String[5];
            printer.directIO(SmFptrConst.SMFPTR_DIO_FS_FIND_DOCUMENT, data, lines);

            System.out.println("Document type    : " + lines[0]);
            System.out.println("IsTicketReceived : " + lines[1]);
            System.out.println("Date and time    : " + lines[2]);
            System.out.println("Document number  : " + lines[3]);
            System.out.println("Document sign    : " + lines[4]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void readLastDayOpen() {
        System.out.println("readLastDayOpen");
        try {
            Vector<String> lines = printer.fsReadDayOpen();
            System.out.println("Номер смены      : " + lines.get(0));
            System.out.println("Тип документа    : " + lines.get(1));
            System.out.println("Дата и время     : " + lines.get(2));
            System.out.println("РН ККТ           : " + lines.get(3));
            System.out.println("ЗН ККТ           : " + lines.get(4));
            System.out.println("Номер ФН         : " + lines.get(5));
            System.out.println("Номер ФД         : " + lines.get(6));
            System.out.println("ФПД              : " + lines.get(7));
            System.out.println("Наименование ОФД : " + lines.get(8));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void readLastDayClose() {
        System.out.println("readLastDayClose");
        try {
            Vector<String> lines = printer.fsReadDayClose();
            System.out.println("Номер смены      : " + lines.get(0));
            System.out.println("Тип документа    : " + lines.get(1));
            System.out.println("Дата и время     : " + lines.get(2));
            System.out.println("РН ККТ           : " + lines.get(3));
            System.out.println("ЗН ККТ           : " + lines.get(4));
            System.out.println("Номер ФН         : " + lines.get(5));
            System.out.println("Номер ФД         : " + lines.get(6));
            System.out.println("ФПД              : " + lines.get(7));
            System.out.println("Наименование ОФД : " + lines.get(8));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void readLastReceipt() {
        System.out.println("readLastReceipt");
        try {
            Vector<String> lines = printer.fsReadReceipt();
            System.out.println("Тип документа      : " + lines.get(0));
            System.out.println("Квитанция получена : " + lines.get(1));
            System.out.println("Дата и время       : " + lines.get(2));
            System.out.println("Номер ФД           : " + lines.get(3));
            System.out.println("ФПД                : " + lines.get(4));
            System.out.println("Тип операции       : " + lines.get(5));
            System.out.println("Сумма              : " + lines.get(6));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void PrintCheckWithPassedPositionSum() {
        try {
            // Задаем тип чека SMFPTR_RT_SALE, SMFPTR_RT_RETSALE, SMFPTR_RT_BUY, SMFPTR_RT_RETBUY
            printer.setFiscalReceiptType(SmFptrConst.SMFPTR_RT_SALE);

            // Указываем систему налогообложения
            printer.setParameter(SmFptrConst.SMFPTR_DIO_PARAM_TAX_SYSTEM, 1);

            // Начинаем фискальный документ
            printer.beginFiscalReceipt(true);

            int oldFontNumber = printer.getFontNumber();
            printer.setFontNumber(5);
            try {
                // Запись телефона покупателя
                printer.fsWriteCustomerPhone("+79006009090");
            } finally {
                printer.setFontNumber(oldFontNumber);
            }
            // Печать строки шрифтом 2
            // Печатаем текст
            printer.printRecMessage("же не манж пасижур", 2);

            // Обычная позиция
            printer.printRecItem("ITEM 1", 0, 1234, 0, 1000, "");

            // Позиция с коррекцией на +-1 копейку
            // Сумма позиции будет сброшена драйвером после вызова printRecItem
            //printer.setParameter(SmFptrConst.SMFPTR_DIO_PARAM_ITEM_TOTAL_AMOUNT, 1235);
            //printer.printRecItem("ITEM 2", 0, 1234, 0, 1000, "");

            // Позиция с признаком способа расчета и признаком предмета расчета
            // 1214, признак способа расчета, если не указывать будет 0
            // ВНИМАНИЕ: значение сохраняется после вызова printRecItem
            printer.setParameter(SmFptrConst.SMFPTR_DIO_PARAM_ITEM_PAYMENT_TYPE, 2);
            // 1212, признак предмета расчета, если не указывать будет 0
            // ВНИМАНИЕ: значение сохраняется после вызова printRecItem
            printer.setParameter(SmFptrConst.SMFPTR_DIO_PARAM_ITEM_SUBJECT_TYPE, 3);
            printer.printRecItem("ITEM 3", 0, 1234, 0, 1000, "");

            // Оплата типом оплаты который привязан к jpos.xml к метке "0"
            // <prop name="payType0" type="String" value="0"/>
            printer.printRecTotal(0, 1235 + 1234, "0");

            // Оплата типом оплаты который привязан к jpos.xml к метке "2"
            // <prop name="payType2" type="String" value="2"/>
            //printer.printRecTotal(0, 12350000, "2");

            // Закрыть фискальный документ
            printer.endFiscalReceipt(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void printCheckWithSmallSum() throws Exception {
        // Задаем тип чека SMFPTR_RT_SALE, SMFPTR_RT_RETSALE, SMFPTR_RT_BUY, SMFPTR_RT_RETBUY
        printer.setFiscalReceiptType(SmFptrConst.SMFPTR_RT_SALE);

        // Указываем систему налогообложения
        printer.setParameter(SmFptrConst.SMFPTR_DIO_PARAM_TAX_SYSTEM, 1);

        // Начинаем фискальный документ
        printer.beginFiscalReceipt(true);

        // Обычная позиция
        printer.printRecItem("ITEM 1", 120, 6000, 1, 20, "");

        printer.printRecTotal(120, 120, "0");

        // Закрыть фискальный документ
        printer.endFiscalReceipt(false);
    }

    private void PrintCheckWithPDF417BarCodeAndFeed() throws Exception {
        printer.setParameter(SmFptrConst.SMFPTR_DIO_PARAM_TAX_SYSTEM, 1);
        printer.beginFiscalReceipt(true);
        printer.printRecItem("ITEM 1", 0, 1234, 0, 1000, "");

        printer.setParameter(SmFptrConst.SMFPTR_DIO_PARAM_ITEM_TOTAL_AMOUNT, 1235);
        printer.printRecItem("ITEM 1", 0, 1234, 0, 1000, "");
        printer.printRecMessage("Nonfiscal line 1");

        printer.fsWriteCustomerEmail("nyx@mail.ru");
        //printer.fsWriteCustomerPhone("88006009090");

        printer.printRecSubtotal(0);
        printer.printRecTotal(0, 1234 + 1235, "1");

        PrinterBarcode barcode = new PrinterBarcode();
        barcode.setText("\"4C63A673C86B0976C0B24495848F6EF157792203A0D275\\n\"\n"
                + "                            + \"1F525456644096478D256A910EFEABB67\"");

        barcode.setType(SmFptrConst.SMFPTR_BARCODE_PDF417);
        barcode.setPrintType(SmFptrConst.SMFPTR_PRINTTYPE_DRIVER);

        barcode.setBarWidth(2);
        barcode.setVScale(5);

        Map<EncodeHintType, Object> params = new HashMap<EncodeHintType, Object>();
// Измерения, тут мы задаем количество колонок и столбцов
        params.put(EncodeHintType.PDF417_DIMENSIONS, new Dimensions(3, 3, 2, 60));
// Можно задать уровень коррекции ошибок, по умолчанию он 0
        params.put(EncodeHintType.ERROR_CORRECTION, 1);
        barcode.addParameter(params);

        printer.printBarcode(barcode);

        printer.endFiscalReceipt(false);

        printer.feedPaper(2);
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

    public void printFiscalReceipt() {
        PrintCheckWithPassedPositionSum();
        /*
        printFiscalReceipt145_1(false);
        printNonFiscal(false);
        printEscBarcodesNormal();
         */
    }

    public void disablePrint() {
        try {
            printer.disablePrint();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printFiscalReceipt102(boolean disablePrint) {
        try {
            printer.resetPrinter();
            printer.setFiscalReceiptType(FPTR_RT_SALES);
            printer.beginFiscalReceipt(true);
            if (disablePrint) {
                printer.disablePrint();
            }
            printer.printRecItem("Item 1", 107466, 4497, 1, 23897, "");
            printer.printRecTotal(200000, 200000, "");

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

            printer.printRecMessage("printRecMessage 1", 2);
            printer.printRecMessage("printRecMessage 2", 2);
            printer.printRecMessage("printRecMessage 3", 2);
            printer.printRecMessage("printRecMessage 4", 2);
            printer.printRecMessage("printRecMessage 5", 2);

            printer.endFiscalReceipt(false);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printCorrectionReceipt() {
        try {
            printer.fsPrintCorrectionReceipt(1, 123);
            printer.fsPrintCorrectionReceipt(3, 123);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void printCorrectionReceipt2() {
        try {
            FSStatusInfo fsStatus = printer.fsReadStatus();
            if (fsStatus.getDocType().getValue() == FSDocType.FS_DOCTYPE_CORRECTION_RECEIPT) {
                printer.fsCancelDocument();
            }

            // 1. Начинаем чек коррекции
            printer.fsStartCorrectionReceipt();

            // 2. Записываем составные части тэга 1174
            // 1177, описание коррекции
            printer.fsWriteTag(1177, "Описание коррекции");
            // 1178, дата документа основания для коррекции UnixTime
            printer.fsWriteTag(1178, "24122017");
            // 1179, номер документа основания для коррекции
            printer.fsWriteTag(1179, "1203891203");

            // 3. Закрываем чек коррекции
            // 1173, тип коррекции: «0» – самостоятельная операция, «1» – операция по предписанию.
            int correctionType = 1;
            // 1054, признак расчета: «1» - коррекция прихода, «3» - коррекция расхода.
            int paymentType = 1;
            // 1020, сумма расчета, указанного в чеке
            long total = 500;
            // 1031, сумма по чеку наличными
            long payments0 = 100;
            // 1081, сумма по чеку электронными
            long payments1 = 100;
            // 1215, сумма по чеку предоплатой
            long payments2 = 100;
            // 1216, сумма по чеку постоплатой
            long payments3 = 100;
            // 1217, сумма по чеку встречным предоставлением
            long payments4 = 100;
            // сумма НДС чека по ставке 18%	1102
            long taxTotals0 = 1;
            // сумма НДС чека по ставке 10%	1103
            long taxTotals1 = 2;
            // 1104, сумма расчета по чеку с НДС по ставке 0%
            long taxTotals2 = 3;
            // 1105, сумма расчета по чеку без НДС
            long taxTotals3 = 4;
            // 1106, сумма НДС чека по расч. ставке 18/118
            long taxTotals4 = 5;
            // 1107, сумма НДС чека по расч. ставке 10/110
            long taxTotals5 = 6;
            // 1055, применяемая система налогообложения
            int taxSystem = 1;
            Object[] outParams = new Object[3];

            printer.fsPrintCorrectionReceipt3(
                    correctionType,
                    paymentType,
                    total,
                    payments0,
                    payments1,
                    payments2,
                    payments3,
                    payments4,
                    taxTotals0,
                    taxTotals1,
                    taxTotals2,
                    taxTotals3,
                    taxTotals4,
                    taxTotals5,
                    taxSystem,
                    outParams);
            int receiptNumber = (Integer) outParams[0];
            int documentNumber = (Integer) outParams[1];
            long documentDigest = (Long) outParams[2];

            System.out.println("Номер чека за смену: " + receiptNumber);
            System.out.println("Номер ФД: " + documentNumber);
            System.out.println("Фискальный признак: " + documentDigest);

        } catch (Exception e) {
            e.printStackTrace();
        }
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
            printer.beginFiscalReceipt(false);
            printer.printRecRefund("****      100359344 Item1", 5596, 2);
            printer.printRecSubtotal(5596);
            printer.printRecSubtotalAdjustment(1, "", 1000);
            printer.printRecTotal(5596, 5596, "payTypeName1");
            printer.endFiscalReceipt(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printZeroFiscalReceipt() {
        try {
            printer.resetPrinter();
            printer.setFiscalReceiptType(FPTR_RT_SALES);
            printer.beginFiscalReceipt(false);
            printer.printRecItem("ITEM 1", 0, 1234, 0, 0, "");
            printer.printRecTotal(1000, 1000, "payTypeName1");
            printer.endFiscalReceipt(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printFiscalReceipt14() {
        try {
            printer.resetPrinter();
            printer.setFiscalReceiptType(FPTR_RT_SALES);
            printer.beginFiscalReceipt(false);
            printer.printRecItem("1053 ПИРОЖКИ ПЕЧЕНЫЕ С КА", 1800, 1000, 1, 1, "");
            printer.printRecItem("1053 ПИРОЖКИ ПЕЧЕНЫЕ С КА", 1800, 1000, 1, 1, "");
            printer.printRecItem("1053 ПИРОЖКИ ПЕЧЕНЫЕ С КА", 1800, 1000, 1, 1, "");
            printer.printRecTotal(10000, 10000, "payTypeName1");
            printer.endFiscalReceipt(false);
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

    public void printFiscalReceipt1044() {
        try {
            printer.resetPrinter();

            printer.setFiscalReceiptType(4);
            printer.beginFiscalReceipt(false);

            printer.printRecItem("ПЕЛЬМЕНИ РЯЗАН. 1КГ", 2799000, 1000, 2, 2799000, "");
            printer.printRecItem("ПЕЛЬМЕНИ РЯЗАН. 1КГ", 2519100, 1000, 2, 2799000, "");
            printer.printRecItem("ДЕКОР.ПАННО ЕЛКА", 1259100, 1000, 1, 1399000, "");
            printer.printRecItem("ДЕКОР.ПАННО ЕЛКА", 1259100, 1000, 1, 1399000, "");
            printer.printRecVoidItem("ПЕЛЬМЕНИ РЯЗАН. 1КГ", -2519100, 1000, 0, 0, 2);
            printer.printRecSubtotal(5037400);
            printer.printRecTotal(2518200, 3200, "39");
            printer.printRecTotal(2518200, 3000000, "1");
            printer.endFiscalReceipt(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printFiscalReceipt10445() {
        try {
            printer.resetPrinter();

            printer.setFiscalReceiptType(4);
            printer.beginFiscalReceipt(false);
            /*
            printer.printRecItem("СВЕЧА ПИРАМИДА", 2519100, 1000, 1, 2799000, "");
            printer.printRecItem("ДЕКОР.ПАННО ЕЛКА", 1259100, 1000, 1, 1399000,"");
            printer.printRecItem("ДЕКОР.ПАННО ЕЛКА", 1259100, 1000, 1, 1399000,"");
            printer.printRecItem("КЕФИР МОЛ.ИСТ.1  930", 417500, 1000, 2, 463900, "");
            printer.printRecItem("КЕФИР МОЛ.ИСТ.1  930", 417500, 1000, 2, 463900,"");
            printer.printRecItem(КЕФИР МОЛ.ИСТ.1  930, -46600, 1000, 2, 463900,   )
            printer.printRecSubtotal(4662700)
            printer.printRecTotal(3306600, 1600, 39)
            printer.printRecTotal(3306600, 3500000, 1)
             */

            printer.endFiscalReceipt(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printFiscalReceipt10444() {
        try {
            printer.resetPrinter();

            printer.setFiscalReceiptType(4);
            printer.beginFiscalReceipt(false);
            printer.printRecMessage("12345678901234567890123456789012345678901234567890123456789012345678901234567890");
            printer.printRecMessage("               Кассовый чек               ");
            printer.printRecItem("1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890", 3305, 1000, 1, 3305, "");
            printer.printRecItem("*3358752 КУХ.Печен.КУРАБЬЕ ОРИГИНАЛ.130г", 3755, 1000, 1, 3755, "");
            printer.printRecItem("*3376015 ПОСИД.ПеченьеТВОРОЖсдоб.изюм250", 4505, 1000, 1, 4505, "");
            printer.printRecItem("*3422398 GREENF.Чай ПР.АСС.чер.25х2г", 5900, 1000, 1, 5900, "");
            printer.printRecMessage("                                          ");
            printer.printRecSubtotal(17465);
            printer.printRecSubtotalAdjustment(1, "", 65);
            printer.printRecSubtotal(17400);
            printer.printRecTotal(17400, 17400, "30");
            printEan13();
            printQRCode();

            printer.endFiscalReceipt(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printFiscalReceipt1045() {
        try {
            printer.resetPrinter();

            printer.setFiscalReceiptType(4);
            printer.beginFiscalReceipt(false);

            printer.printRecItem("БИФШТЕКС/ГОВЯД.335Г", 1199000, 1000, 2, 1199000, "");
            printer.printRecItem("БИФШТЕКС/ГОВЯД.335Г", 1199000, 1000, 2, 1199000, "");
            printer.printRecItemAdjustment(1, "", 1199000, 2);
            printer.printRecItem("ПАКЕТ МАЙКА 38 Х 59", 25000, 1000, 1, 25000, "");
            printer.printRecItem("ПАКЕТ МАЙКА 38 Х 59", 25000, 1000, 1, 25000, "");
            printer.printRecVoidItem("БИФШТЕКС/ГОВЯД.335Г", 1199000, 1000, 0, 0, 2);
            printer.printRecItemAdjustment(2, "", 1199000, 2);
            printer.printRecVoidItem("БИФШТЕКС/ГОВЯД.335Г", 1199000, 1000, 0, 0, 2);

            printer.printRecTotal(5000000, 5000000, "1");

            printer.endFiscalReceipt(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printFiscalReceipt1046() {
        try {
            printer.resetPrinter();

            printer.setFiscalReceiptType(4);
            printer.beginFiscalReceipt(false);

            printer.printRecItem("ФУТБОЛКА МУЖ СВ-СЕР", 3399000, 1000, 1, 3399000, "");
            printer.printRecItem("ФУТБОЛКА МУЖ БЕЛАЯ", 3999000, 1000, 1, 3999000, "");
            printer.printRecItem("ПАКЕТ МАЙКА 38 Х 59", 175000, 7000, 1, 25000, "");
            printer.printRecItem("МУКА В/С ГЛОБУС 2КГ", 3174000, 6000, 2, 529000, "");
            printer.printRecItem("КАКТУС", 899000, 1000, 1, 899000, "");
            printer.printRecItem("КАЛЬЦЕОЛЯРИЯ", 2499000, 1000, 1, 2499000, "");
            printer.printRecItem("КАРТОФЕЛЬ МОЛОД ВЕС", 1540700, 3082, 2, 499900, "");
            printer.printRecItem("ЖИДКОСТЬ Д/РОЗЖИГА", 849000, 1000, 1, 849000, "");
            printer.printRecItem("КАРТОФЕЛЬ МОЛОД ВЕС", 1668200, 3337, 2, 499900, "");
            printer.printRecItem("МАСЛО Т.ОЗ.82,5%450Г", 2359000, 1000, 2, 2359000, "");
            printer.printRecItem("ПЕРЧАТКИ ОБЛИВНЫЕ XL", 329900, 1000, 1, 329900, "");
            printer.printRecItem("СПИЧКИ ГАЗОВЫЕ", 599900, 1000, 1, 599900, "");
            printer.printRecItem("КАРТОФЕЛЬ МОЛОД ВЕС", 1498200, 2997, 2, 499900, "");
            printer.printRecItem("ЖИДК.Д/РОЗЖИГА 0,5Л", 579000, 1000, 1, 579000, "");
            printer.printRecItem("ЙОГУРТ ЦАРКА П/М400Г", 499900, 1000, 2, 499900, "");
            printer.printRecItem("ЛУК РЕПЧАТЫЙ", 361000, 1577, 2, 228900, "");
            printer.printRecItem("КОЛБ.ДОКТОРСКАЯ 500Г", 2899000, 1000, 2, 2899000, "");
            printer.printRecItem("КИНЗА          100ГР", 549000, 1000, 2, 549000, "");
            printer.printRecItem("ПЕРЧАТКИ ОБЛИВНЫЕ XL", 329900, 1000, 1, 329900, "");
            printer.printRecItem("ХЛОПЬЯ 7 ЗЛАКОВ400ГР", 499900, 1000, 2, 499900, "");
            printer.printRecItem("ХЛОПЬЯ 7 ЗЛАКОВ400ГР", 499900, 1000, 2, 499900, "");
            printer.printRecItem("ТВОРОГ РОСТАГ.9%180Г", 629000, 1000, 2, 629000, "");
            printer.printRecItem("ТЕРМОМЕТР КОМНАТН.", 3499000, 1000, 1, 3499000, "");
            printer.printRecItem("СИЛИКОНОВАЯ", 999000, 1000, 1, 999000, "");
            printer.printRecItem("ЛУК РЕПЧАТЫ", 588700, 2572, 2, 228900, "");
            printer.printRecItem("МАСЛО Т.ОЗ.82,5%450", 2359000, 1000, 2, 2359000, "");
            printer.printRecItem("ЙОГУРТ ЦАРКА КЛ.400", 499900, 1000, 2, 499900, "");
            printer.printRecItem("ТЕРМОМЕТР УНИВЕРС.", 1699000, 1000, 1, 1699000, "");
            printer.printRecItem("ОГУРЦЫ ПУПЫР 300 Г", 849000, 1000, 2, 849000, "");
            printer.printRecItem("ЙОГУРТ ЦАРКА Ч.400", 499900, 1000, 2, 499900, "");
            printer.printRecItem("БАТ DUR BAS АЛК АА2", 1149000, 1000, 1, 1149000, "");
            printer.printRecItem("ГРЕЧКА МИСТРАЛЬ 2К", 1599000, 1000, 2, 1599000, "");
            printer.printRecItem("ГРЕЧКА МИСТРАЛЬ 2К", 1599000, 1000, 2, 1599000, "");
            printer.printRecItem("СМЕТАНА БЛ 26% 315", 829000, 1000, 2, 829000, "");
            printer.printRecItem("РИС КРАСНОДАРСКИЙ900", 799000, 1000, 2, 799000, "");
            printer.printRecItem("РИС КРАСНОДАРСКИЙ900", 799000, 1000, 2, 799000, "");
            printer.printRecItem("СЕРВЕЛАТ БРАУНШВ С/", 939800, 188, 2, 4999000, "");
            printer.printRecItem("ТВОРОГ ВКУСН.9% 350", 1179000, 1000, 2, 1179000, "");
            printer.printRecItem("ЙОГУРТ ЦАРКА П/М400", 499900, 1000, 2, 499900, "");
            printer.printRecItem("ЙОГУРТ ЦАРКА БР.400", 499900, 1000, 2, 499900, "");
            printer.printRecItem("Й-Т ЦАРКА МАЛИНА400", 499900, 1000, 2, 499900, "");
            printer.printRecItem("ЙОГУРТ ЦАРКА ,5%400", 499900, 1000, 2, 499900, "");
            printer.printRecItem("ЙОГУРТ ЦАРКА Г/Я400", 499900, 1000, 2, 499900, "");
            printer.printRecItem("ЙОГУРТ ЦАРКА Ч.400", 499900, 1000, 2, 499900, "");
            printer.printRecItem("ТВ.БР.-ЛИТОВС.7%200", 1378000, 2000, 2, 689000, "");
            printer.printRecItem("МОРКОВЬ МЫТАЯ ВЕ", 662600, 1657, 2, 399900, "");
            printer.printRecItem("ТВ.РОКИШКИО 9% 400", 1689000, 1000, 2, 1689000, "");
            printer.printRecItem("ЙОГУРТ ЦАРКА КЛ.400", 499900, 1000, 2, 499900, "");
            printer.printRecItem("УКРОП ПАКЕТ 100 Г", 369900, 1000, 2, 369900, "");
            printer.printRecItem("ИВАН ЧАЙ С ДУШИЦ 50", 1169000, 1000, 1, 1169000, "");
            printer.printRecItem("ГУБКА КИВИ ЭКС.ЧЕ", 1249000, 1000, 1, 1249000, "");
            printer.printRecItem("СЫР РОСCИЙСКИЙ 45% ", 1621500, 306, 2, 5299000, "");
            printer.printRecItem("Й-Т ЦАРКА МАЛИНА400", 499900, 1000, 2, 499900, "");
            printer.printRecItem("ЙОГУРТ ЦАРКА Г/Я400", 499900, 1000, 2, 499900, "");
            printer.printRecItem("ЙОГУРТ ЦАРКА БР.400", 499900, 1000, 2, 499900, "");
            printer.printRecItem("ЙОГУРТ ЦАРКА ,5%400", 499900, 1000, 2, 499900, "");
            printer.printRecItem("КОФЕ ЖАРД МОЛ ЭСП250", 1490000, 1000, 1, 1490000, "");
            printer.printRecItem("ДАНОН ТВ.НАТУР.5%170", 448900, 1000, 2, 448900, "");
            printer.printRecItem("ДАНОН ТВ.НАТУР.5%170", 448900, 1000, 2, 448900, "");
            printer.printRecItem("РЕДИ", 331700, 332, 2, 999000, "");
            printer.printRecItem("МОРКОВЬ МЫТАЯ ВЕ", 578700, 1447, 2, 399900, "");
            printer.printRecItem("КАРТОФЕЛЬ МОЛОД ВЕС", 1733200, 3467, 2, 499900, "");
            printer.printRecItem("МОЛОКО ГЛОБУС ОТ.900", 629000, 1000, 2, 629000, "");
            printer.printRecItem("МОЛОКО ГЛОБУС ОТ.900", 629000, 1000, 2, 629000, "");
            printer.printRecItem("КАРМ АРИАН ПЭТ 1,5", 449900, 1000, 1, 449900, "");
            printer.printRecItem("КОНФ БАБ/ФУНДУК ВЕ", 1781600, 440, 1, 4049000, "");
            printer.printRecItem("САЦЕБЕЛИ ЗАСТ 300", 919000, 1000, 1, 919000, "");
            printer.printRecItem("ПЕРЕЦ ЖЕЛТЫ", 983500, 492, 2, 1999000, "");
            printer.printRecItem("СЫР ЛАРЕЦ Т/С 50%ВЕ", 1529800, 225, 2, 6799000, "");
            printer.printRecItem("КОЛБ.КРАКОВС.П/К 400", 2799000, 1000, 2, 2799000, "");
            printer.printRecItem("ТОМАТ", 2462900, 1332, 2, 1849000, "");
            printer.printRecItem("КОЛБ.ДОКТОРСКАЯ 500", 2899000, 1000, 2, 2899000, "");
            printer.printRecItem("Й-Т ЦАРКА КИВИ 400", 499900, 1000, 2, 499900, "");
            printer.printRecItem("Й-Т ЦАРКА КИВИ 400", 499900, 1000, 2, 499900, "");
            printer.printRecItem("НАП СПРАЙТ ОГУР 0,5", 499900, 1000, 1, 499900, "");
            printer.printRecItem("ЯЙЦО ОКСКОЕ С0 30Ш", 2429000, 1000, 2, 2429000, "");
            printer.printRecItem("ЯЙЦО ОКСКОЕ С0 30Ш", 2429000, 1000, 2, 2429000, "");
            printer.printRecItem("ЖУРН САША И МАШ", 384900, 1000, 2, 384900, "");
            printer.printRecItem("ВНЕКЛАССНЫЙ ЖУРНА", 599000, 1000, 2, 599000, "");
            printer.printRecItem("БАТОН НАРЕЗНОЙ 400", 279900, 1000, 2, 279900, "");
            printer.printRecItem("БАТОН АРБАТСКИЙ 350", 79000, 1000, 2, 79000, "");
            printer.printRecItem("ПАКЕТ МАЙКА 38 Х 59", 25000, 1000, 1, 25000, "");
            printer.printRecItem("БУЛОЧ ФИЛИП ИЗЮМ5Х60", 449900, 1000, 2, 449900, "");
            printer.printRecItem("БАТОН АРБАТСКИЙ 350", 79000, 1000, 2, 79000, "");
            printer.printRecItem("ХЛЕБ АРБАТСКИЙ 500Г", 239900, 1000, 2, 239900, "");
            printer.printRecTotal(91969100, 91969100, "8");

            printer.endFiscalReceipt(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printFiscalReceipt1047() {
        try {
            printer.resetPrinter();

            printer.setFiscalReceiptType(4);
            printer.beginFiscalReceipt(false);

            printer.printRecItem("ПАКЕТ МАЙКА 38 Х 59", 25000, 1000, 1, 25000, "");
            printer.printRecItem("ПЕЧ БАМБ МОЛОЧ100Г", 599000, 1000, 1, 599000, "");
            printer.printRecItem("ПЕЧ БАМБ МОЛОЧ100Г", 599000, 1000, 1, 599000, "");
            printer.printRecItemAdjustment(1, "", 299500, 1);
            printer.printRecItemAdjustment(1, "", 299500, 1);
            printer.printRecVoidItem("ПЕЧ БАМБ МОЛОЧ100Г", 599000, 1000, 0, 0, 1);
            printer.printRecItemAdjustment(2, "", 299500, 1);
            printer.printRecItemAdjustment(2, "", 299500, 1);
            printer.printRecVoidItem("ПЕЧ БАМБ МОЛОЧ100Г", 599000, 1000, 0, 0, 1);
            printer.printRecSubtotal(25000);
            printer.printRecTotal(25000, 5000, "39");
            printer.printRecTotal(25000, 20000, "1");

            printer.endFiscalReceipt(false);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printFiscalReceipt1049() {
        try {
            printer.resetPrinter();

            printer.setFiscalReceiptType(4);
            printer.beginFiscalReceipt(false);

            //100 + 100 + 119.9 - 11.99 + 119.9 - 131.89
            printer.printRecItem("МОРОЖ НЕСКВИК ЭСКИМО", 1000000, 1000, 2, 1000000, "");
            printer.printRecItem("МОРОЖ НЕСКВИК ЭСКИМО", 1000000, 1000, 2, 1000000, "");
            printer.printRecItem("БИФШТЕКС/ГОВЯД.335Г", 1199000, 1000, 2, 1199000, "");
            printer.printRecItemAdjustment(1, "", 119900, 2);
            printer.printRecItem("БИФШТЕКС/ГОВЯД.335Г", 1199000, 1000, 2, 1199000, "");
            printer.printRecItemAdjustment(1, "", 1318900, 2);

            printer.printRecTotal(25000000, 25000000, "1");
            printer.endFiscalReceipt(false);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printFiscalReceipt1048() {
        try {
            printer.resetPrinter();

            printer.setFiscalReceiptType(4);
            printer.beginFiscalReceipt(false);

            printer.printRecItem("1 3493909 Бланк игр.ком.ВГЛ4 Спорт(Жил.л", 1, 1000, 3, 1, "");
            printer.printRecItemVoid("2 3493909 Бланк игр.ком.ВГЛ4 Спорт(Жил.л", 1, 1000, 3, 1, "");
            printer.printRecItem("3 3493909 Бланк игр.ком.ВГЛ4 Спорт(Жил.л", 1, 1000, 3, 1, "");
            printer.printRecItemVoid("4 3493909 Бланк игр.ком.ВГЛ4 Спорт(Жил.л", 1, 1000, 3, 1, "");
            printer.printRecItem("5 3493909 Бланк игр.ком.ВГЛ4 Спорт(Жил.л", 10000, 1000, 3, 10000, "");
            printer.printRecSubtotal(10000);
            printer.printRecTotal(10000, 10000, "");
            printer.endFiscalReceipt(false);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printFiscalReceipt23() {
        try {
            int[] recTypes = {
                SmFptrConst.SMFPTR_RT_SALE,
                SmFptrConst.SMFPTR_RT_RETSALE,
                SmFptrConst.SMFPTR_RT_BUY,
                SmFptrConst.SMFPTR_RT_RETBUY
            };
            for (int i = 0; i < recTypes.length; i++) {
                printer.resetPrinter();
                printer.setFiscalReceiptType(recTypes[i]);
                printer.beginFiscalReceipt(false);
                printer.printRecItem("88888 Груши РОША                  1кг", 10697, 3000, 1, 6899, "г.");
                printer.printRecItemAdjustment(1, "", 10000, 1);
                printer.printRecSubtotal(20697);
                printer.printRecSubtotalAdjustment(1, "", 97);
                printer.printRecSubtotal(10600);
                printer.printRecTotal(10600, 10600, "30");
                printer.endFiscalReceipt(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printFiscalReceipt10488() {
        try {
            printer.resetPrinter();
            printer.setFiscalReceiptType(4);
            printer.beginFiscalReceipt(false);
            printer.printRecItem("44444 Сироп Кленовый", 65, 1000, 6, 65, "ST");
            printer.printRecItem("55555 Напиток Coca-Cola ZERO 2 л.", 10100, 1000, 5, 10100, "ST");
            printer.printRecItem("66666 Напиток Coca-Cola газированный 2 л", 10155, 1000, 3, 10155, "ST");
            printer.printRecItem("77777 Напиток Содовая 0,33 л.", 567, 1000, 2, 567, "ST");
            printer.printRecItem("88888 Груши РОША                  1кг", 6899, 1000, 1, 6899, "г.");
            printer.printRecItem("99999 Напиток гранатовый негазированный", 45169, 1000, 4, 45169, "ST");
            printer.printRecSubtotalAdjustment(1, "", 55);
            printer.printRecTotal(72900, 72900, "00");
            printer.endFiscalReceipt(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printFiscalReceipt1048888() {
        try {
            printer.resetPrinter();
            printer.printRecMessage("Кассовый чек 1");
            printer.setFiscalReceiptType(4);
            printer.beginFiscalReceipt(false);
            printer.printRecMessage("Кассовый чек 2");

            printer.printRecItem("Item 1", 396705, 795, 1, 499000, "кг");
            printer.printRecTotal(396705, 396705, "13");

            printer.endFiscalReceipt(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printFiscalReceipt1048889() {
        try {
            printer.resetPrinter();
            printer.setFiscalReceiptType(4);
            printer.beginFiscalReceipt(false);

            printer.printRecItem("*1860 Напиток COCA-COLA газ.ПЭТ  2.0л", 264, 1000, 1, 300, "ST");
            printer.printRecItemAdjustment(1, "", 36, 1);
            printer.printRecItem("202 Томаты 1кг", 19124, 1695, 2, 12800, "г.");
            printer.printRecItemAdjustment(1, "", 2572, 2);
            printer.printRecItem("202 Томаты 1кг", 19123, 1695, 2, 12800, "г.");
            printer.printRecItemAdjustment(1, "", 2573, 2);
            printer.printRecItem("202 Томаты 1кг", 48457, 4295, 2, 12800, "г.");
            printer.printRecItemAdjustment(1, "", 6519, 2);
            printer.printRecSubtotal(98668);
            printer.printRecSubtotalAdjustment(1, "", 68);
            printer.printRecSubtotal(86900);
            printer.printRecTotal(86900, 86900, "10");

            printer.endFiscalReceipt(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printFiscalReceipt105() {
        try {
            printer.resetPrinter();
            printer.setFiscalReceiptType(4);
            printer.beginFiscalReceipt(false);

            printer.printRecItem("****            322 БАНАНЫ", 396700, 795, 1, 499000, "кг");
            printer.printRecItem("****      100204414 БУЛКА ЧЕРКИЗОВСКАЯ1", 265000, 1000, 2, 265000, "шт");
            printer.printRecItem("****  4607032421115 ХЛЕБ РИЖСКИЙ 1/400", 245000, 1000, 2, 245000, "шт");
            printer.printRecItem("**02  4690228030437 ДЕСЕРТ ТВОРОЖНЫЙ ЧУД", 452000, 1000, 2, 452000, "шт");
            printer.printRecItemAdjustment(1, "ckugka", 104000, 2);
            printer.printRecItem("**02  4690228030437 ДЕСЕРТ ТВОРОЖНЫЙ ЧУД", 452000, 1000, 2, 452000, "шт");
            printer.printRecItemAdjustment(1, "ckugka", 104000, 2);
            printer.printRecItem("****            112 МОРКОВЬ МЫТАЯ", 296500, 495, 2, 599000, "кг");
            printer.printRecItem("****  4607072010348 ИКРА МИНТАЯ ПРОБОЙНА", 495000, 1000, 2, 495000, "шт");
            printer.printRecItem("****             87 КАРТОФЕЛЬ УРОЖАЙ 201", 669500, 1525, 2, 439000, "кг");
            printer.printRecSubtotal(3063700);
            printer.printRecSubtotalAdjustment(1, "МАРКИ", 60000);
            printer.printRecSubtotalAdjustment(1, "Округл.сдачи", 3700);
            printer.printRecTotal(3000000, 0, "20");
            printer.printRecTotal(3000000, 10000000, "1");

            printer.endFiscalReceipt(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printFiscalReceipt104888() {
        try {
            printer.resetPrinter();
            printer.setFiscalReceiptType(4);
            printer.beginFiscalReceipt(false);

            printer.printRecItem("*1860 Напиток COCA-COLA газ.ПЭТ  2.0л", 738, 1000, 1, 738, "ST");
            printer.printRecSubtotalAdjustment(1, "", 8);
            printer.printRecTotal(730, 730, "00");

            /*            
            printer.printRecItem("Item 1", 0, 8000, 1, 123, "ST");
            printer.printRecItemAdjustment(1, "", 100, 1);
            printer.printRecItem("Item 2", 0, 12340, 1, 123, "ST");
            printer.printRecItemAdjustment(1, "", 1000, 1);
            printer.printRecTotal(2000000, 2000000, "00");

            
            
            
            printer.printRecItem("17333 Шок.KIND.CH.мол.с мол.нач. 100г", 41920, 5000, 1, 9300, "ST");
            printer.printRecItemAdjustment(1, "", 4580, 1);
            printer.printRecItem("5051 AHM.Чай EARL GREY 25х2г", 160469, 20000, 1, 8900, "ST");
            printer.printRecItemAdjustment(1, "", 17531, 1);
            printer.printRecItem("5051 AHM.Чай EARL GREY 25х2г", 64188, 8000, 1, 8900, "ST");
            printer.printRecItemAdjustment(1, "", 7012, 1);
            printer.printRecItem("5051 AHM.Чай EARL GREY 25х2г", 8023, 1000, 1, 8900, "ST");
            printer.printRecItemAdjustment(1, "", 877, 1);
            printer.printRecSubtotal(304600);
            printer.printRecTotal(274600, 274600, "00");
            
            printer.printRecItem("1245 Шамп.АБ.ДЮР.РОССИЙСК.б.п/сл0.75", 99005, 1000, 1, 100000, "ST");
            printer.printRecItemAdjustment(1, "", 995, 1);
            printer.printRecItem("22233 ФН Сок ябл-персик.б/сах.200мл", 55, 4000, 2, 2590, "ST");
            printer.printRecItemAdjustment(1, "", 10305, 2);
            printer.printRecSubtotal(110360);
            printer.printRecSubtotalAdjustment(1, "", 60);
            printer.printRecSubtotal(99000);
            printer.printRecTotal(99000, 99000, "00");
             */
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

            printer.printRecItem("СЫР КОРОЛЬ АРТУР ВЕС", 2343200, 378, 2, 6199000, "");
            printer.printRecItemAdjustment(1, "", 234300, 2);
            // 2108900
            printer.printRecItem("КАЛЬМАР ТУШКА ВЕС", 1478700, 646, 2, 2289000, "");
            printer.printRecItemAdjustment(1, "", 147900, 2);
            //1478700 - 147900 = 1330800

            printer.printRecItem("ФОРЕЛЬ ЧИЛ. БАЛЫК ХК", 7218000, 602, 2, 11990000, "");
            printer.printRecItemAdjustment(1, "", 721800, 2);
            //7218000 - 721800 = 6496200

            printer.printRecItem("САВОРИН ТУШКА Х/К  В", 1786400, 358, 2, 4990000, "");
            printer.printRecItemAdjustment(1, "", 178600, 2);
            //1786400 - 178600 = 1607800

            printer.printRecItem("МАСЛЯНАЯ ФИЛЕ Х/К", 4277100, 446, 2, 9590000, "");
            printer.printRecItemAdjustment(1, "", 427700, 2);
            //4277100 - 427700 = 3849400

            printer.printRecItem("КАРП ЖИВОЙ ВЕС", 2785700, 1752, 2, 1590000, "");
            printer.printRecItemAdjustment(1, "", 278600, 2);
            // 2785700 - 278600 = 2507100

            printer.printRecItem("СЫРОК ВАНИЛЬ 45Г РАЭ", 1519000, 10000, 2, 151900, "");
            printer.printRecItemAdjustment(1, "", 152000, 2);
            //1519000 - 152000 = 1367000

            printer.printRecItem("КАРБОНАД В/К ВЕС", 5225200, 804, 1, 6499000, "");
            printer.printRecItemAdjustment(1, "", 522500, 1);
            //5225200 - 522500 = 4702700

            printer.printRecItem("КОЛБ ЗЕРНИСТАЯ С/К", 2914300, 286, 2, 10190000, "");
            printer.printRecItemAdjustment(1, "", 291400, 2);
            //2914300 - 291400 = 2622900

            printer.printRecItem("КАЙСА ВЕС", 2009700, 718, 1, 2799000, "");
            printer.printRecItemAdjustment(1, "", 201000, 1);
            //2009700 - 201000 = 1808700

            printer.printRecItem("ВИНО ЛАЗАРЕВКА", 1599000, 1000, 1, 1599000, "");
            printer.printRecItemAdjustment(1, "", 159900, 1);
            // 1599000 - 159900 = 1439100

            printer.printRecItem("ЧИПСЫ ХРУСТ КАРТ 160", 599000, 1000, 1, 599000, "");
            printer.printRecItemAdjustment(1, "", 59900, 1);
            // 599000 - 59900 = 539100

            printer.printRecItem("САЛЯМИ БОГАТЫРЬ С/К", 3331700, 285, 2, 11690000, "");
            printer.printRecItemAdjustment(1, "", 333200, 2);
            // 3331700 - 333200 = 2998500

            printer.printRecItem("ПИВО ВАШ ВЫБОР 1,5Л", 749000, 1000, 1, 749000, "");
            printer.printRecItemAdjustment(1, "", 74900, 1);
            // 749000 - 74900 = 674100

            printer.printRecItem("ЯБЛОКИ СЕЗОННЫЕ  ВЕС", 435800, 794, 1, 548900, "");
            printer.printRecItemAdjustment(1, "", 43600, 1);
            //435800 - 43600 = 392200

            printer.printRecItem("МЫЛО ХОЗ. АИСТ 150Г.", 1399000, 10000, 1, 139900, "");
            printer.printRecItemAdjustment(1, "", 140000, 1);
            //1399000 - 140000 = 1259000

            printer.printRecVoidItem("КАЙСА ВЕС", 2009700, 718, 0, 0, 1);
            printer.printRecItemAdjustment(2, "", 201000, 1);
            // 3000000
            // 2799000 + 201000 = 2598000

            printer.printRecItem("АБРИКОС СУШЕНЫЙ ВЕС", 2867300, 717, 1, 3999000, "");
            printer.printRecItemAdjustment(1, "", 286700, 1);
            printer.printRecItem("МАК СПИРАЛИ 500Г", 400000, 1000, 2, 400000, "");
            printer.printRecItemAdjustment(1, "", 40000, 2);
            printer.printRecItem("МАК СПИРАЛИ 500Г", 400000, 1000, 2, 400000, "");
            printer.printRecItemAdjustment(1, "", 40000, 2);
            printer.printRecItem("ГОЛЕНЬ ИНД БЕСКОСТ", 2292700, 831, 2, 2759000, "");
            printer.printRecItemAdjustment(1, "", 229300, 2);
            printer.printRecItem("МАК ВЕРМИШЕЛЬ 500Г", 400000, 1000, 2, 400000, "");
            printer.printRecItemAdjustment(1, "", 40000, 2);

            printer.printRecSubtotal(39618818);
            printer.printRecTotal(39618800, 8800, "39");
            printer.printRecTotal(39618800, 40000000, "1");

            //4363.82
            printer.endFiscalReceipt(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printFiscalReceipt1051() {
        try {
            printer.resetPrinter();

            printer.beginFiscalReceipt(true);
            printer.printRecItem("****            430 МАНДАРИНЫ", 10772, 770, 1, 13990, "кг");
            printer.printRecItemAdjustment(1, "M за N !!!", 50, 1);
            printer.printRecItem("****            430 МАНДАРИНЫ", 10772, 770, 1, 13990, "кг");
            printer.printRecItemAdjustment(1, "M за N !!!", 50, 1);
            printer.printRecVoidItem("****            430 МАНДАРИНЫ", 13990, 770, 0, 0, 1);
            printer.printRecSubtotal(0);
            printer.printRecSubtotalAdjustment(1, "Округл.сдачи", 22);
            printer.printRecSubtotal(0);
            printer.printRecTotal(10750, 10750, "10");

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

    public void printNullReceipt() {
        try {
            printer.resetPrinter();
            printer.setFiscalReceiptType(4);
            printer.beginFiscalReceipt(true);
            printer.printRecItem("Item1", 0, 0, 1, 0, "");
            printer.printRecTotal(0, 0, "");
            printer.endFiscalReceipt(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printFiscalReceipt1052() {
        try {
            printer.resetPrinter();

            printer.setFiscalReceiptType(4);
            printer.beginFiscalReceipt(true);

            printer.printRecMessage("                      КАССОВЫЙ ЧЕК                       ");
            printer.printRecItem("3162506 МОС.ПР.Майон.КЛАСС.67% 750мл", 9180, 1000, 1, 10200, "ST");
            printer.printRecItemAdjustment(1, "", 1020, 1);
            printer.printRecItem("*8041 Нектар ДОБРЫЙ МУЛЬТИФРУТ     2л", 8500, 1000, 2, 8500, "ST");
            printer.printRecItem("*3334592 ДОБРЫЙ Нектар пер.-ябл.2л", 8500, 1000, 2, 8500, "ST");
            printer.printRecItem("*3443716 GREENR.Огурчики АППЕТ.мар720мл", 7500, 1000, 1, 7500, "ST");
            printer.printRecItem("*3314610 GL.VIL.Томаты ЧЕРРИ мар.720мл", 5900, 1000, 1, 5900, "ST");
            printer.printRecItem("*3345854 МК КЛИНС.Колб.МИНИ-САЛ.с/к 300г", 24600, 1000, 2, 24600, "ST");
            printer.printRecItem("78004333 ОСТ.Свинина д/запекания охл.1кг", 38463, 1299, 2, 32900, "г."
            );
            printer.printRecItemAdjustment(1, "", 4274, 1);
            printer.printRecItem("3074007 Морковь мытая 1кг", 2333, 530, 2, 4890, "г."
            );
            printer.printRecItemAdjustment(1, "", 259, 1);
            printer.printRecItem("*3219701 Огурцы короткоплодные 1кг", 2576, 430, 2, 5990, "г."
            );
            printer.printRecItem("202 Томаты 1кг", 6654, 740, 2, 9990, "г."
            );
            printer.printRecItemAdjustment(1, "", 739, 1);
            printer.printRecItem("*3044397 Чеснок фасованный 1уп", 2890, 1000, 2, 2890, "ST");
            printer.printRecItem("*3256556 Томаты ЧЕРРИ красные 250г", 5990, 1000, 2, 5990, "ST");
            printer.printRecItem("3628395 САРАФ.Кефир дет.1,5% 930г", 5499, 1000, 2, 6110, "ST");
            printer.printRecItemAdjustment(1, "", 611, 1);
            printer.printRecItem("197 Лук репчатый 1кг", 1739, 440, 2, 4390, "г."
            );
            printer.printRecItemAdjustment(1, "", 193, 1);
            printer.printRecItem("*16791 BOND.Горошек зеленый ж/б 400г", 6200, 1000, 1, 6200, "ST");
            printer.printRecItem("*3792 BOND.Кукур.слад.зерн.ж/б 340г", 6200, 1000, 1, 6200, "ST");
            printer.printRecItem("*16791 BOND.Горошек зеленый ж/б 400г", 6200, 1000, 1, 6200, "ST");
            printer.printRecItem("*3417568 ВЕЛК.Колб.САЛЯМ.КОР.п/к ср.370г", 19900, 1000, 2, 19900, "ST");
            printer.printRecItem("*3417568 ВЕЛК.Колб.САЛЯМ.КОР.п/к ср.370г", 19900, 1000, 2, 19900, "ST");
            printer.printRecItem("*3454250 Ч.ЛИН.Мор.пл.кр-бр.рож.12%110г", 6600, 1000, 2, 9900, "ST");
            printer.printRecItemAdjustment(1, "", 3300, 2);
            printer.printRecItem("*3454250 Ч.ЛИН.Мор.пл.кр-бр.рож.12%110г", 6600, 1000, 2, 9900, "ST");
            printer.printRecItemAdjustment(1, "", 3300, 2);
            printer.printRecItem("*3454250 Ч.ЛИН.Мор.пл.кр-бр.рож.12%110г", 6600, 1000, 2, 9900, "ST");
            printer.printRecItemAdjustment(1, "", 3300, 2);
            printer.printRecItem("*3380555 ШАРЛИЗ Суфле СЛИВКИ-ВАНИЛЬ 200г", 4500, 1000, 1, 4500, "ST");
            printer.printRecItem("*3380555 ШАРЛИЗ Суфле СЛИВКИ-ВАНИЛЬ 200г", 4500, 1000, 1, 4500, "ST");
            printer.printRecItem("*3363793 Ч-ЯГОДА Марм.Ябл/Апельс/Дыня250", 3900, 1000, 1, 3900, "ST");
            printer.printRecItem("*3414014 Р.ФР.Марм.ЧУД.ЯГ.м./бр./ч.с.250", 3900, 1000, 1, 3900, "ST");
            printer.printRecItem("*3275468 F.H.Икр.мойв.ар/копч.с/с 180г", 6990, 1000, 2, 6990, "ST");
            printer.printRecItem("*3419749 NESC.Кофе GOL.BARISTA ст/б 85г", 19900, 1000, 1, 19900, "ST");
            printer.printRecItem("3613025 САРАФ.Мол.отб.паст.д3,4-4%930мл", 5517, 1000, 2, 6130, "ST");
            printer.printRecItemAdjustment(1, "", 613, 2);
            printer.printRecItem("3172880 Батон НАРЕЗНОЙ нарезка п/э 380г", 2065, 1000, 2, 2295, "ST");
            printer.printRecItemAdjustment(1, "", 230, 2);
            printer.printRecItem("*3502314 Пиво ZATECKY GUS св.4,6% 0.45л", 5550, 1000, 1, 5550, "ST");
            printer.printRecItem("*26877 BAHL.Печ.LEIBNIZ мин.сл.шок100г", 5900, 1000, 1, 5900, "ST");
            printer.printRecItem("*3492143 BANINI Печенье чайн.с какао125г", 3500, 1000, 1, 3500, "ST");
            printer.printRecItem("*3603447 BANINI Печенье чайн.ван.нач125г", 3500, 1000, 1, 3500, "ST");
            printer.printRecItem("3400865 МЕРИД.Палоч.КРАБ.д/салата 200г", 8005, 1000, 2, 8895, "ST");
            printer.printRecItemAdjustment(1, "", 890, 2);
            printer.printRecItem("3400865 МЕРИД.Палоч.КРАБ.д/салата 200г", 8005, 1000, 2, 8895, "ST");
            printer.printRecItemAdjustment(1, "", 890, 2);
            printer.printRecItem("3182853 Яйца кур.С0 стол.фас.10шт", 4941, 1000, 2, 5490, "ST");
            printer.printRecItemAdjustment(1, "", 549, 2);
            printer.printRecItem("3341462 ЛЕТО Яйцо куриное С1 20шт", 13230, 1000, 2, 14700, "ST");
            printer.printRecItemAdjustment(1, "", 1470, 2);
            printer.printRecItem("49505 Дск Свекла варен.очищ.в/у 500г", 8622, 2000, 1, 4790, "ST");
            printer.printRecItemAdjustment(1, "", 958, 1);
            printer.printRecItem("*3454582 Вино АЛАЗ.ДОЛ.кр.п/сл.ст.0.75л", 21000, 1000, 1, 42000, "ST");
            printer.printRecItemAdjustment(1, "", 21000, 1);
            printer.printRecItem("*3454582 Вино АЛАЗ.ДОЛ.кр.п/сл.ст.0.75л", 21000, 1000, 1, 42000, "ST");
            printer.printRecItemAdjustment(1, "", 21000, 1);
            printer.printRecItem("71029 Зелень Укроп в упаковке 100г", 3591, 1000, 2, 3990, "ST");
            printer.printRecItemAdjustment(1, "", 399, 2);
            printer.printRecItem("*3256556 Томаты ЧЕРРИ красные 250г", 5990, 1000, 2, 5990, "ST");
            printer.printRecItem("3335501 КРАСНАЯ ЦЕНА Сыр ГАУДА фас.1кг", 15809, 352, 2, 49900, "г."
            );
            printer.printRecItemAdjustment(1, "", 1756, 2);
            printer.printRecItem("*3385812 ВЕЛКОМ Колб.ДОКТОР.кус.в/уп500г", 17500, 1000, 2, 35000, "ST");
            printer.printRecItemAdjustment(1, "", 17500, 2);
            printer.printRecItem("*3385812 ВЕЛКОМ Колб.ДОКТОР.кус.в/уп500г", 17500, 1000, 2, 35000, "ST");
            printer.printRecItemAdjustment(1, "", 17500, 2);
            printer.printRecSubtotal(525190);
            printer.printRecSubtotalAdjustment(1, "", 39);
            printer.printRecSubtotal(423400);
            printer.printRecTotal(423400, 423400, "10");

            printer.endFiscalReceipt(false);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printFiscalReceipt1053() {
        try {
            printer.resetPrinter();
            printer.setFiscalReceiptType(4);
            printer.beginFiscalReceipt(true);
            printer.printRecItem("Кафетерий КОТЛЕТА КУРИ", 721900, 382, 1, 1890000, "");
            printer.printRecSubtotal(721900);
            printer.printRecTotal(721900, 721900, "0");
            printer.endFiscalReceipt(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printFiscalReceipt1054() {
        try {
            printer.resetPrinter();
            printer.setFiscalReceiptType(4);
            printer.beginFiscalReceipt(true);
            for (int i = 0; i < 19; i++) {
                printer.printRecItem("4607001777021 КОФЕ ЯКОБС МОНАРХ ВЕ", 28990, 1000, 1, 28990, "шт");
            }
            printer.printRecSubtotalAdjustment(1, "Округл.сдачи", 10);
            printer.printRecTotal(550800, 550800, "1");

            printer.endFiscalReceipt(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printFiscalReceipt666() {
        try {
            printer.resetPrinter();

            printer.clearLogo();
            printer.clearImages();
            printer.loadLogo(SmFptrConst.SMFPTR_LOGO_BEFORE_HEADER, "logo.bmp");
            int numHeaderLines = printer.getNumHeaderLines();
            for (int i = 1; i <= numHeaderLines; i++) {
                printer.setHeaderLine(i, "Header line " + i, false);
            }

            long payment = 0;
            printer.resetPrinter();
            printer.setFiscalReceiptType(jpos.FiscalPrinterConst.FPTR_RT_SALES);
            printer.beginFiscalReceipt(false);

            double unitPrice = 0.1;
            double qty = 1.0;
            printer.printRecItem("Водка БонАква сильногаз 1,0л ПЭТ", 0, (int) (qty * 1000), 4, (long) (unitPrice * 100), "");

            payment += (long) (0.1 * 100);
            printer.printRecItem("Водка БонАква сильногаз 1,0л ПЭТ", 0, (int) (qty * 1000), 4, (long) (unitPrice * 100), "");

            payment += (long) (0.1 * 100);
            //printer.fsWriteTag(1074, "8-913-919-1205"); // Телефон платежного агента
            printer.printRecTotal(payment, payment, "1");
            //printer.directIO(0x39, null, "foo@example.com");
            //printer.fsWriteTLV(new byte[] {-13, 3, 1, 0, -124});            // тег: 1011, длина: 1, значение: 132

            printer.printRecMessage("printRecMessage 1");
            printer.printRecMessage("printRecMessage 2");
            printer.printRecMessage("printRecMessage 3");
            printer.printRecMessage("printRecMessage 4");
            printer.printRecMessage("printRecMessage 5");
            printer.endFiscalReceipt(false);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printNonFiscalReceipt666() {
        try {
            printer.resetPrinter();
            printer.clearLogo();
            printer.clearImages();
            int numHeaderLines = printer.getNumHeaderLines();
            for (int i = 1; i <= numHeaderLines; i++) {
                printer.setHeaderLine(i, "Header line " + i, false);
            }

            int howMuch = 1;

            long payment = 0;
            printer.resetPrinter();
            //printer.setFiscalReceiptType(jpos.FiscalPrinterConst.FPTR_RT_SALES);
            printer.beginNonFiscal();
            for (int i = 0; i < howMuch; i++) {
                long price = 1234;
                payment += price;

                String itemName = "Item 1234";
                printer.printRecItem(itemName, price, 0, 0, 0, "");
            }
            printer.printRecTotal(payment, payment, "1");

            //printer.directIO(0x39, null, "foo@example.com");
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
            printer.printRecItem("АИ-92           N  2", 50000, 14010, 1, 3570, "");
            printer.printRecTotal(100000, 100000, "30");
            printer.endFiscalReceipt(false);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printFiscalReceipt101() {
        try {
            printer.resetPrinter();
            printer.setFiscalReceiptType(FPTR_RT_SALES);
            printer.beginFiscalReceipt(true);
            printer.printRecItem("АИ-95-К5        N  1", 10016, 3130, 1, 3200, "");
            printer.printRecSubtotal(10016);
            printer.printRecSubtotalAdjustment(1, "скидка округления 0.16", 16);
            printer.printRecTotal(10016, 10000, "0");
            printer.endFiscalReceipt(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printFiscalReceipt101_1() {
        try {
            printer.resetPrinter();
            printer.setFiscalReceiptType(FPTR_RT_SALES);
            printer.beginFiscalReceipt(true);
            printer.printRecItem("АИ-95-К5        N  1", 50016, 15630, 1, 3200, "");
            printer.printRecSubtotal(50016);
            printer.printRecSubtotalAdjustment(1, "скидка округления 0.16", 16);
            printer.printRecTotal(50016, 50000, "0");
            printer.endFiscalReceipt(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printFiscalReceipt103() {
        try {
            printer.resetPrinter();
            printer.setFiscalReceiptType(SmFptrConst.SMFPTR_RT_RETSALE);
            printer.beginFiscalReceipt(true);

            printer.printRecMessage("5095 1234/045/008   13.11.17 18:09 AC-00");
            printer.printRecItem("КОНТЕЙНЕР СКЛ. LOGO", 499900, 1000, 1, 499900, "");
            printer.printRecVoid("* * * *** ЧЕК ОТМЕНЕН *** * * *");
            printer.printRecMessage("1 2000152395007 КОНТЕЙНЕР СКЛ. LOGO 1");
            printer.printRecMessage("   49,99 * 1 = 49,99");
            printer.printRecMessage("ПОДИТОГ: 49,99");
            printer.printRecMessage("*** ЧЕК ОТМЕНЕН ***");

            printer.disableDocEnd();
            printer.endFiscalReceipt(false);
            printer.printNormal(FPTR_S_RECEIPT, "Дополнительная строка 1");
            printer.printNormal(FPTR_S_RECEIPT, "Дополнительная строка 2");
            printer.printNormal(FPTR_S_RECEIPT, "Дополнительная строка 3");
            printer.printDocEnd();

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
        //printFiscalReceipt1044();
        printFiscalReceipt10444();
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

    public void printNonFiscal(boolean docEndEnabled) {
        try {
            printer.beginNonFiscal();
            if (!docEndEnabled) {
                printer.directIO(84, null, null);
            }

            printer.printNormal(FPTR_S_RECEIPT, getLine(""));
            printer.printNormal(FPTR_S_RECEIPT, getLine("Nonfiscal receipt"));
            printer.printNormal(FPTR_S_RECEIPT, getLine(""));

            printer.printNormal(FPTR_S_RECEIPT, "Строка1\rСтрока2\nСтрока3\r\nСтрока4\r\rСтрока6");
            printer.printNormal(FPTR_S_RECEIPT, "#*~*#http://check.egais.ru?id=38d02af6-bfd2-409f-8041-b011d8160700&dt=2311161430&cn=030000290346");

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
            printer.printRecCash(100000);
            printer.printRecTotal(0, 100000, "0");
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
            System.out.println("CapHasVatTable: " + printer.getCapHasVatTable());
            System.out.println("CapSetVatTable: " + printer.getCapSetVatTable());

            if (printer.getCapHasVatTable()) {
                int numVatRates = printer.getNumVatRates();
                System.out.println("NumVatRates: " + String.valueOf(numVatRates));
                // get vat rates
                for (int i = 1; i <= numVatRates; i++) {
                    int[] vatRate = new int[1];
                    printer.getVatEntry(i, 0, vatRate);
                    System.out.println("VatRate " + String.valueOf(i)
                            + "  : " + String.valueOf(vatRate[0]));
                }
                // set vat rates
                if (printer.getCapSetVatTable()) {
                    for (int i = 1; i <= numVatRates; i++) {
                        printer.setVatValue(i, String.valueOf(1234 * i));
                    }
                    printer.setVatTable();
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
                        + String.valueOf(reg.getName(i)) + "\r\n";
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
        printCancelledSalesReceipt();
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
            driver.openCashDrawer(0);

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

    public void fsReadTickets() throws Exception {
        System.out.println("fsReadTickets()");
        int[] numbers = new int[10];
        for (int i = 0; i < numbers.length; i++) {
            numbers[i] = i;
        }
        printTickets(printer.fsReadTickets(numbers));
    }

    public void fsReadTickets2() throws Exception {
        System.out.println("fsReadTickets2()");
        int[] numbers = new int[10];
        for (int i = 0; i < numbers.length; i++) {
            numbers[i] = i;
        }
        printTickets(printer.fsReadTickets2(numbers));
    }

    public void fsReadTickets3() throws Exception {
        System.out.println("readFSTickets3(1, 10)");
        printTickets(printer.fsReadTickets3(1, 10));
    }

    public void fsReadTickets4() throws Exception {
        System.out.println("readFSTickets4(1, 10)");
        printTickets(printer.fsReadTickets4(1, 10));
    }

    public void printTickets(String[] lines) throws Exception {
        for (int i = 0; i < lines.length; i++) {
            System.out.println(lines[i]);
        }
    }

    public void printTickets(Vector<FSTicket> tickets) throws Exception {
        for (int i = 0; i < tickets.size(); i++) {
            FSTicket ticket = tickets.get(i);
            String s = String.valueOf(ticket.getResultCode());
            if ((ticket.getResultCode() == 0) && (ticket.getData() != null)) {
                s = ticket.getResultCode() + ": " + Hex.toHex(ticket.getData());
            }
            s = String.valueOf(i) + ". " + s;
            System.out.println(s);

            System.out.println("Date: " + ticket.getDate().toString());
            System.out.println("Time: " + ticket.getTime().toString());
            System.out.println("Document MAC: " + Hex.toHex(ticket.getDocumentMAC()));
            System.out.println("Document number: " + ticket.getDocumentNumber());
            System.out.println("Text: " + ticket.getText());
        }
    }

    public void printFiscalReceipt145() {
        try {
            printer.resetPrinter();
            printer.setFiscalReceiptType(4);
            printer.beginFiscalReceipt(false);
            printer.printRecRefund("Item 1", 300, 1);
            printCode128();
            printer.printRecSubtotal(0);
            printer.printRecTotal(300, 300, "description");
            printCode128();
            printer.endFiscalReceipt(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printFiscalReceipt145_1(boolean printTrailer) {
        try {
            printer.resetPrinter();
            printer.setFiscalReceiptType(4);
            printer.beginFiscalReceipt(false);
            if (!printTrailer) {
                printer.directIO(84, null, null);
            }

            printer.printRecItem("Item 1", 300, 1000, 1, 300, "");
            printer.printRecItemAdjustment(FPTR_AT_AMOUNT_DISCOUNT, "СКИДКА", 50, 1);
            printer.printRecTotal(30, 30, "1");
            printer.printRecTotal(20, 20, "2");
            printer.printRecTotal(300, 300, "0");
            for (int i = 1; i < 20; i++) {
                printer.printRecMessage("Printrecmessage " + i);
            }
            printer.endFiscalReceipt(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
