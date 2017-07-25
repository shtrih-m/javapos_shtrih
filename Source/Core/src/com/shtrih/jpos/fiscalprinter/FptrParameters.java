/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.jpos.fiscalprinter;

/**
 *
 * @author V.Kravtsov
 */
import java.util.Enumeration;
import java.util.Vector;

import jpos.config.JposEntry;
import jpos.config.RS232Const;

import com.shtrih.util.StringUtils;
import com.shtrih.fiscalprinter.FontNumber;
import com.shtrih.fiscalprinter.command.PrinterConst;
import com.shtrih.jpos.JposPropertyReader;
import static com.shtrih.jpos.fiscalprinter.SmFptrConst.SMFPTR_HEADER_MODE_DRIVER;
import static com.shtrih.jpos.fiscalprinter.SmFptrConst.SMFPTR_HEADER_MODE_DRIVER2;
import com.shtrih.util.CompositeLogger;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class FptrParameters {

    public static final int defaultGraphicsLineDelay = 200;

    private int byteTimeout = 3000;
    private int deviceByteTimeout = 3000;
    private int statusCommand = PrinterConst.SMFP_STATUS_COMMAND_11H;
    private int graphicsLineDelay = defaultGraphicsLineDelay;
    public int barcodeDelay = 0;
    private int portType = SmFptrConst.PORT_TYPE_SERIAL;
    private int baudRate = 4800;
    public String portClass = "com.shtrih.fiscalprinter.port.SerialPrinterPort";
    public String portName = "";
    public boolean searchByPortEnabled = false;
    public boolean searchByBaudRateEnabled = true;
    public FontNumber font;
    public String stringEncoding = "";
    public boolean escCommandsEnabled = false;
    public int department = 1;
    public int recCloseSleepTime = 0;
    public String closeReceiptText = "";
    public String subtotalText = "SUBTOTAL";
    public FontNumber subtotalFont;
    public FontNumber discountFont;
    public String receiptVoidText = "ЧЕК ОТМЕНЕН";
    public int taxPassword = 0;
    public int usrPassword = 1;
    public int sysPassword = 30;
    public String statisticFileName = "ShtrihFiscalPrinter.xml";
    public int pollInterval = 500;
    public boolean pollEnabled = false;
    public double amountFactor = 1;
    public double quantityFactor = 1;
    public boolean centerImage = true; // center BMP image
    public String fieldsFileName = "";
    public String fieldsFilesPath = "";
    public int numHeaderLines;
    public int numTrailerLines;
    public String[] taxNames = {"А", "Б", "В", "Г"};
    public final Vector paymentNames = new Vector();
    public int reportDevice = SmFptrConst.SMFPTR_REPORT_DEVICE_EJ;
    public int reportType = PrinterConst.SMFP_REPORT_TYPE_FULL;
    public String messagesFileName = "";
    public boolean wrapText = true;
    public int cutType = PrinterConst.SMFP_CUT_PARTIAL;
    public int maxEnqNumber = 10;
    public int maxNakCommandNumber = 3;
    public int maxNakAnswerNumber = 3;
    public int maxAckNumber = 3;
    public int maxRepeatCount = 3;
    public boolean xmlZReportEnabled = false;
    public boolean csvZReportEnabled = false;
    public String xmlZReportFileName = "ZReport.xml";
    public String csvZReportFileName = "ZReport.csv";
    public static final boolean defaultAutoOpenDrawer = false;
    public boolean autoOpenDrawer = defaultAutoOpenDrawer;
    public int tableMode = SmFptrConst.SMFPTR_TABLE_MODE_AUTO;
    public int cutMode = SmFptrConst.SMFPTR_CUT_MODE_AUTO;
    public int logoMode = SmFptrConst.SMFPTR_LOGO_MODE_SPLIT_IMAGE;
    public int searchMode = SmFptrConst.SMFPTR_SEARCH_NONE;
    public String preLine = "";
    public String postLine = "";
    public boolean zeroPriceFilterEnabled = false;
    public String zeroPriceFilterTime1 = "21:00";
    public String zeroPriceFilterTime2 = "11:00";
    public String zeroPriceFilterErrorText = "";
    public boolean discountFilterEnabled = false;

    public int RFAmountLength = 8;
    public int RFQuantityLength = 10;
    public int RFSeparatorLine = SmFptrConst.SMFPTR_SEPARATOR_LINE_DASHES;
    private final PayTypes payTypes = new PayTypes();
    public int salesReceiptType = SmFptrConst.SMFPTR_RECEIPT_NORMAL;
    public int cutPaperDelay = 0;
    private int monitoringPort = 50000;
    public boolean monitoringEnabled = false;
    public boolean receiptReportEnabled = false;
    public String receiptReportFileName = "ZCheckReport.xml";
    public int protocolType = SmFptrConst.SMFPTR_PROTOCOL_1;
    public int openReceiptType = SmFptrConst.SMFPTR_OPEN_RECEIPT_ITEM;
    public boolean ZReportDayNumber = false;
    public int headerMode = SmFptrConst.SMFPTR_HEADER_MODE_DRIVER;
    private static CompositeLogger logger = CompositeLogger.getLogger(FptrParameters.class);
    public int headerImagePosition = SmFptrConst.SMFPTR_LOGO_AFTER_HEADER;
    public int trailerImagePosition = SmFptrConst.SMFPTR_LOGO_AFTER_TRAILER;
    public boolean centerHeader = false;
    public boolean cancelIO = false;
    public boolean logEnabled = false;
    public boolean stripExceptionDetails = true;
    public boolean sendENQ = true;
    private boolean taxLettersEnabled = true;
    public boolean zeroReceiptEnabled = true;
    public String barcodePrefix = "BARCODE:";
    public int barcodeType = SmFptrConst.SMFPTR_BARCODE_QR_CODE;
    public int barcodeBarWidth = 2;
    public int barcodeHeight = 100;
    public int barcodeTextPosition = SmFptrConst.SMFPTR_TEXTPOS_BELOW;
    public int barcodeTextFont = 1;
    public int barcodeAspectRatio = 3;
    public boolean FSServiceEnabled = false;
    public String FSHost = "k-server.test-naofd.ru";
    public int FSPort = 7779;
    public int FSConnectTimeout = 3000; // 3000 ms
    public int FSPollPeriod = 3000; // 3000 ms - poll period
    public int compatibilityLevel = SmFptrConst.SMFPTR_COMPAT_LEVEL_NONE;
    public boolean checkTotal = false;
    public boolean checkTotalEnabled = false;
    public int receiptNumberRequest = SmFptrConst.SMFPTR_RN_FP_DOCUMENT_NUMBER;
    public boolean FSDiscountEnabled = true;
    public boolean FSCombineItemAdjustments = true;
    public boolean textReportEnabled = false;
    public boolean readDiscountMode = true;
    public String textReportFileName = "documents.txt";
    public boolean FSPrintTags = true;
    public boolean textReportEmptyLinesEnabled = true;
    public boolean ReceiptTemplateEnabled = false;
    public String ItemTableHeader = null;
    public String ItemTableTrailer = null;
    public String ItemRowFormat = null;
    public String discountFormat = null;
    public String chargeFormat = null;
    public boolean capJrnPresent = true;
    public boolean nonFiscalHeaderEnabled = false;
    public boolean fsHeaderEnabled = false;
    public int nonFiscalDocNumber = 1;
    public String fieldSeparator = "\n";
    public int printerIDMode = PrinterConst.PRINTER_ID_SERIAL;
    public int swapGraphicsLine = PrinterConst.SWAP_LINE_AUTO;
    public int subAdjustmentOrder = PrinterConst.ADJUSTMENT_ORDER_CORRECT;
    public boolean subtotalTextEnabled = true;
    public String weightUnitName = "г.";
    public String firmwarePath = "firmware";
    public boolean graphicsLineEnabled = true;
    public String preLinePrefix = "";
    public String postLinePrefix = "";
    public boolean combineReceiptItems = false;
    public boolean printRecVoidItemAmount = false;
    public boolean FSReceiptItemDiscountEnabled = false;
    public boolean quantityCorrectionEnabled = true;
    public long taxValue[] = new long[6];
    public int taxSystem = 0;
    public Integer itemTotalAmount = null;

    public FptrParameters() throws Exception {
        font = new FontNumber(PrinterConst.FONT_NUMBER_NORMAL);
        subtotalFont = new FontNumber(PrinterConst.FONT_NUMBER_NORMAL);
        discountFont = new FontNumber(PrinterConst.FONT_NUMBER_NORMAL);
        taxValue[0] = 0;
        taxValue[1] = 0;
        taxValue[2] = 0;
        taxValue[3] = 0;
        taxValue[4] = 0;
        taxValue[5] = 0;
        taxSystem = 0;
    }

    public void setDefaults() throws Exception {
        setPortType(SmFptrConst.PORT_TYPE_FROMCLASS);
        setBaudRate(4800);
        FSPrintTags = true;
    }

    public void load(JposEntry entry) throws Exception {
        cancelIO = false;
        if (entry == null) {
            return;
        }
        loadPayTypes(entry);

        JposPropertyReader reader = new JposPropertyReader(entry);

        if (entry.hasPropertyWithName("portClass")) {
            portClass = (String) entry.getPropertyValue("portClass");
        }
        setPortType(reader.readInteger("portType", SmFptrConst.PORT_TYPE_FROMCLASS));
        portName = reader.readString(RS232Const.RS232_PORT_NAME_PROP_NAME,
                "");
        setBaudRate(reader.readInteger(RS232Const.RS232_BAUD_RATE_PROP_NAME,
                4800));
        department = reader.readInteger("department", 1);
        font = new FontNumber(reader.readInteger("fontNumber", 1));
        closeReceiptText = reader.readString("closeReceiptText", "");
        subtotalText = reader.readString("subtotalText", "SUBTOTAL");
        //setByteTimeout(reader.readInteger("byteTimeout", 1000));
        taxPassword = reader.readInteger("taxPassword", 0);
        usrPassword = reader.readInteger("operatorPassword", 1);
        sysPassword = reader.readInteger("sysAdminPassword", 30);
        searchByPortEnabled = reader.readBoolean("searchByPortEnabled",
                false);
        searchByBaudRateEnabled = reader.readBoolean(
                "searchByBaudRateEnabled", true);
        pollInterval = reader.readInteger("pollInterval", 500);
        pollEnabled = reader.readBoolean("pollEnabled", false);
        amountFactor = reader.readDouble("amountFactor", 1);
        quantityFactor = reader.readDouble("quantityFactor", 1);
        stringEncoding = reader
                .readString("stringEncoding", stringEncoding);
        if (stringEncoding.equals("")) {
            stringEncoding = System.getProperty("file.encoding");
        }
        if (stringEncoding == null) {
            stringEncoding = "UTF-8";
        }
        if (stringEncoding.equals("")) {
            stringEncoding = "UTF-8";
        }
        logger.debug("stringEncoding: \"" + stringEncoding + "\"");

        escCommandsEnabled = reader
                .readBoolean("escCommandsEnabled", false);
        statisticFileName = reader.readString("statisticFileName",
                "ShtrihFiscalPrinter.xml");
        fieldsFileName = reader.readString("fieldsFileName", "");
        fieldsFilesPath = reader.readString("fieldsFilesPath", "");
        setGraphicsLineDelay(reader.readInteger("graphicsLineDelay",
                defaultGraphicsLineDelay));
        numHeaderLines = reader.readInteger("numHeaderLines", 5);
        numTrailerLines = reader.readInteger("numTrailerLines", 0);
        reportDevice = reader.readInteger("reportDevice",
                SmFptrConst.SMFPTR_REPORT_DEVICE_EJ);
        reportType = reader.readInteger("reportType",
                PrinterConst.SMFP_REPORT_TYPE_FULL);
        statusCommand = reader.readInteger("statusCommand",
                PrinterConst.SMFP_STATUS_COMMAND_DS);
        messagesFileName = reader.readString("messagesFileName", "shtrihjavapos_en.properties");
        if (messagesFileName.equals("")) {
            messagesFileName = "shtrihjavapos_en.properties";
        }

        wrapText = reader.readBoolean("wrapText", true);
        recCloseSleepTime = reader.readInteger("recCloseSleepTime", 0);
        cutType = reader.readInteger("cutType",
                PrinterConst.SMFP_CUT_PARTIAL);
        maxEnqNumber = reader.readInteger("maxEnqNumber", 10);
        maxNakCommandNumber = reader.readInteger("maxNakCommandNumber", 3);
        maxNakAnswerNumber = reader.readInteger("maxNakAnswerNumber", 3);
        maxAckNumber = reader.readInteger("maxAckNumber", 3);
        maxRepeatCount = reader.readInteger("maxRepeatCount", 3);
        xmlZReportEnabled = reader.readBoolean("XmlZReportEnabled", false);
        xmlZReportFileName = reader.readString("XmlZReportFileName",
                "ZReport.xml");
        csvZReportEnabled = reader.readBoolean("CsvZReportEnabled", false);
        csvZReportFileName = reader.readString("CsvZReportFileName",
                "ZReport.csv");
        autoOpenDrawer = reader.readBoolean("autoOpenDrawer",
                defaultAutoOpenDrawer);
        tableMode = reader.readInteger("tableMode",
                SmFptrConst.SMFPTR_TABLE_MODE_AUTO);
        cutMode = reader.readInteger("cutMode",
                SmFptrConst.SMFPTR_CUT_MODE_AUTO);
        logoMode = reader.readInteger("logoMode",
                SmFptrConst.SMFPTR_LOGO_MODE_SPLIT_IMAGE);
        searchMode = reader.readInteger("searchMode",
                SmFptrConst.SMFPTR_SEARCH_NONE);

        zeroPriceFilterEnabled = reader.readBoolean(
                "ZeroPriceFilterEnabled", false);
        zeroPriceFilterTime1 = reader.readString("ZeroPriceFilterTime1",
                "21:00");
        zeroPriceFilterTime2 = reader.readString("ZeroPriceFilterTime2",
                "11:00");
        zeroPriceFilterErrorText = reader.readString(
                "ZeroPriceFilterErrorText", "");
        discountFilterEnabled = reader.readBoolean("DiscountFilterEnabled",
                false);
        salesReceiptType = reader.readInteger("salesReceiptType",
                SmFptrConst.SMFPTR_RECEIPT_NORMAL);
        cutPaperDelay = reader.readInteger("cutPaperDelay", 0);
        RFAmountLength = reader.readInteger("RFAmountLength", 8);
        RFQuantityLength = reader.readInteger("RFQuantityLength", 10);
        monitoringPort = reader.readInteger("MonitoringPort", 50000);
        monitoringEnabled = reader.readBoolean("MonitoringEnabled", false);
        receiptReportEnabled = reader.readBoolean("receiptReportEnabled",
                false);
        receiptReportFileName = reader.readString("receiptReportFileName",
                "ZCheckReport.xml");
        protocolType = reader.readInteger("protocolType",
                SmFptrConst.SMFPTR_PROTOCOL_1);
        openReceiptType = reader.readInteger("openReceiptType",
                SmFptrConst.SMFPTR_OPEN_RECEIPT_ITEM);
        ZReportDayNumber = reader.readBoolean("ZReportDayNumber", false);

        headerMode = reader.readInteger("headerMode",
                SmFptrConst.SMFPTR_HEADER_MODE_DRIVER);
        headerImagePosition = reader.readInteger("headerImagePosition",
                SmFptrConst.SMFPTR_LOGO_AFTER_HEADER);
        trailerImagePosition = reader.readInteger("trailerImagePosition",
                SmFptrConst.SMFPTR_LOGO_AFTER_TRAILER);
        centerHeader = reader.readBoolean("centerHeader", false);
        logEnabled = reader.readBoolean("logEnabled", false);
        stripExceptionDetails = reader.readBoolean("stripExceptionDetails", true);
        sendENQ = reader.readBoolean("sendENQ", true);
        taxLettersEnabled = reader.readBoolean("taxLettersEnabled", true);
        zeroReceiptEnabled = reader.readBoolean("zeroReceiptEnabled", true);
        barcodePrefix = reader.readString("barcodePrefix", "BARCODE:");
        barcodeType = reader.readInteger("barcodeType", SmFptrConst.SMFPTR_BARCODE_QR_CODE);
        barcodeBarWidth = reader.readInteger("barcodeBarWidth", 2);
        barcodeHeight = reader.readInteger("barcodeHeight", 100);
        barcodeTextPosition = reader.readInteger("barcodeTextPosition", SmFptrConst.SMFPTR_TEXTPOS_BELOW);
        barcodeTextFont = reader.readInteger("barcodeTextFont", 1);
        barcodeAspectRatio = reader.readInteger("barcodeAspectRatio", 3);
        FSServiceEnabled = reader.readBoolean("FSServiceEnabled", false);
        FSHost = reader.readString("FSHost", "k-server.test-naofd.ru");
        FSPort = reader.readInteger("FSPort", 7779);
        FSConnectTimeout = reader.readInteger("FSConnectTimeout", 3000);
        FSPollPeriod = reader.readInteger("FSPollPeriod", 3000);
        compatibilityLevel = reader.readInteger("compatibilityLevel",
                SmFptrConst.SMFPTR_COMPAT_LEVEL_NONE);
        receiptVoidText = reader.readString("receiptVoidText",
                "ЧЕК ОТМЕНЕН");
        checkTotalEnabled = reader.readBoolean("checkTotalEnabled", false);
        receiptNumberRequest = reader.readInteger("receiptNumberRequest",
                SmFptrConst.SMFPTR_RN_FP_DOCUMENT_NUMBER);

        FSDiscountEnabled = reader.readBoolean("FSDiscountEnabled", true);
        FSCombineItemAdjustments = reader.readBoolean("FSCombineItemAdjustments", true);
        readDiscountMode = reader.readBoolean("readDiscountMode", true);
        FSPrintTags = reader.readBoolean("FSPrintTags", true);

        textReportEnabled = reader.readBoolean("textReportEnabled", false);
        textReportFileName = reader.readString("textReportFileName", "");
        ReceiptTemplateEnabled = reader.readBoolean("ReceiptTemplateEnabled", false);
        subtotalFont = new FontNumber(reader.readInteger("subtotalFont", PrinterConst.FONT_NUMBER_NORMAL));
        discountFont = new FontNumber(reader.readInteger("discountFont", PrinterConst.FONT_NUMBER_NORMAL));
        ItemTableHeader = reader.readString("ItemTableHeader", "");
        ItemTableTrailer = reader.readString("ItemTableTrailer", "");

        ItemRowFormat = reader.readString("ItemRowFormat", "%TITLE% %QUAN% X %PRICE%");
        ItemRowFormat = StringUtils.rtrim(ItemRowFormat);

        discountFormat = reader.readString("DiscountFormat", "");
        discountFormat = StringUtils.rtrim(discountFormat);

        chargeFormat = reader.readString("ChargeFormat", "");
        chargeFormat = StringUtils.rtrim(chargeFormat);

        capJrnPresent = reader.readBoolean("capJrnPresent", true);
        nonFiscalHeaderEnabled = reader.readBoolean("nonFiscalHeaderEnabled", false);
        fsHeaderEnabled = reader.readBoolean("fsHeaderEnabled", false);
        fieldSeparator = reader.readString("FieldSeparator", "\n");
        printerIDMode = reader.readInteger("PrinterIDMode", PrinterConst.PRINTER_ID_SERIAL);
        swapGraphicsLine = reader.readInteger("swapGraphicsLine", PrinterConst.SWAP_LINE_AUTO);
        subAdjustmentOrder = reader.readInteger("subAdjustmentOrder", PrinterConst.ADJUSTMENT_ORDER_CORRECT);
        subtotalTextEnabled = reader.readBoolean("subtotalTextEnabled", true);
        weightUnitName = reader.readString("weightUnitName", "г.");
        firmwarePath = reader.readString("firmwarePath", "firmware");
        graphicsLineEnabled = reader.readBoolean("graphicsLineEnabled", true);
        barcodeDelay = reader.readInteger("barcodeDelay", 0);
        preLinePrefix = reader.readString("preLinePrefix", "");
        postLinePrefix = reader.readString("postLinePrefix", "");
        combineReceiptItems = reader.readBoolean("combineReceiptItems", false);
        printRecVoidItemAmount = reader.readBoolean("printRecVoidItemAmount", false);
        FSReceiptItemDiscountEnabled = reader.readBoolean("FSReceiptItemDiscountEnabled", false);
        quantityCorrectionEnabled = reader.readBoolean("quantityCorrectionEnabled", true);
        
        
        // paymentNames
        String paymentName;
        String propertyName;
        paymentNames.clear();
        for (int i = 1; i <= 4; i++) {
            propertyName = "paymentName" + String.valueOf(i);
            if (entry.hasPropertyWithName(propertyName)) {
                paymentName = (String) entry.getPropertyValue(propertyName);
                paymentNames.add(new FptrPaymentName(i, paymentName));
            }
        }
    }

    public void loadLogEnabled(JposEntry entry) throws Exception {
        if (entry == null) {
            return;
        }
        JposPropertyReader reader = new JposPropertyReader(entry);
        logEnabled = reader.readBoolean("logEnabled", false);
    }

    public Vector getPaymentNames() {
        return paymentNames;
    }

    public void loadPayTypes(JposEntry jposEntry) throws Exception {
        try {
            payTypes.clear();
            String payTypeID = "";
            String payTypeValue = "";
            String propertyName = "";

            Enumeration props = jposEntry.getPropertyNames();
            while (props.hasMoreElements()) {
                propertyName = (String) props.nextElement();
                if (propertyName.indexOf("payType") != -1) {
                    try {
                        payTypeValue = (String) jposEntry.getPropertyValue(propertyName);
                        payTypeID = propertyName.substring(propertyName.indexOf("payType") + 7);
                        int payTypeValueInt = Integer.parseInt(payTypeValue);
                        PayType payType = new PayType(payTypeValueInt);
                        payTypes.put(payTypeID, payType);
                    } catch (Exception e) {
                        logger.error(e);
                    }
                }
            }
        } catch (Exception e) {
            logger.error(e);
        }
    }

    public void setFont(FontNumber font) {
        this.font = font;
    }

    public FontNumber getFont() {
        return font;
    }

    public PayTypes getPayTypes() {
        return payTypes;
    }

    public int getMonitoringPort() {
        return monitoringPort;
    }

    public int getByteTimeout() {
        return byteTimeout;
    }

    public int getDeviceByteTimeout() {
        return deviceByteTimeout;
    }

    public int getStatusCommand() {
        return statusCommand;
    }

    private static final int minByteTimeout = 100;      // 100 ms
    private static final int maxByteTimeout = 100000;    // 100 sec

    public void setByteTimeout(int value) {
        if ((value >= minByteTimeout) && (value <= maxByteTimeout)) {
            byteTimeout = value;
        }
    }

    public void setDeviceByteTimeout(int value) {
        if ((value >= minByteTimeout) && (value <= maxByteTimeout)) {
            deviceByteTimeout = value;
        }
    }

    public void setStatusCommand(int value) {
        if ((value >= PrinterConst.SMFP_STATUS_COMMAND_MIN) && (value <= PrinterConst.SMFP_STATUS_COMMAND_MAX)) {
            statusCommand = value;
        }
    }

    public int getGraphicsLineDelay() {
        return graphicsLineDelay;
    }

    public void setGraphicsLineDelay(int value) {
        if ((value >= 0) && (value < 10000)) {
            graphicsLineDelay = value;
        }
    }

    public int getPortType() {
        return portType;
    }

    public void setPortType(int value) {
        if ((value >= SmFptrConst.PORT_TYPE_MIN) && (value <= SmFptrConst.PORT_TYPE_MAX)) {
            portType = value;
        }
    }

    public int getBaudRate() {
        return baudRate;
    }

    public void setBaudRate(int value) {
        if (value >= 1200) {
            baudRate = value;
        }
    }

    public boolean getTaxLettersEnabled() {
        return taxLettersEnabled;
    }

    public boolean getZeroReceiptEnabled() {
        return zeroReceiptEnabled;
    }

    public void clearPrePostLine() {
        preLine = "";
        postLine = "";
    }

    public void clearPreLine() {
        preLine = "";
    }

    public void clearPostLine() {
        postLine = "";
    }

    public String quantityToStr(long value, String unitName) throws Exception 
    {
        value = Math.abs(value);
        
        String result;
        if (((value % 1000) == 0) && (!unitName.equalsIgnoreCase(weightUnitName))) {
            result = String.valueOf(value / 1000);
        } else {
            result = StringUtils.quantityToString(value / 1000.0);
        }
        return result;
    }

    public boolean isDriverHeader(){
        return (headerMode == SMFPTR_HEADER_MODE_DRIVER)||
                (headerMode == SMFPTR_HEADER_MODE_DRIVER2);
    }
}
