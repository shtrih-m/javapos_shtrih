/*
 * SmFptrConst.java
 *
 * Created on 13 Ноябрь 2009 г., 17:57
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.shtrih.jpos.fiscalprinter;

import com.shtrih.fiscalprinter.command.PrinterConst;
import static jpos.FiscalPrinterConst.FPTR_RT_SERVICE;
import static jpos.FiscalPrinterConst.FPTR_RT_SIMPLE_INVOICE;

/**
 * @author V.Kravtsov
 */
public interface SmFptrConst {

    // ///////////////////////////////////////////////////////////////////
    // Report device for printReport, printPeriodicTotalsReport
    // ///////////////////////////////////////////////////////////////////
    /**
     * Report from electronic journal (EJ) *
     */
    public static final int SMFPTR_REPORT_DEVICE_EJ = 0;

    /**
     * Report from fiscal memory (FM) *
     */
    public static final int SMFPTR_REPORT_DEVICE_FM = 1;

    // ///////////////////////////////////////////////////////////////////
    // PrinterBarcode types
    // ///////////////////////////////////////////////////////////////////
    public static final int SMFPTR_BARCODE_UPCA = 0;
    public static final int SMFPTR_BARCODE_UPCE = 1;
    public static final int SMFPTR_BARCODE_EAN13 = 2;
    public static final int SMFPTR_BARCODE_EAN8 = 3;
    public static final int SMFPTR_BARCODE_CODE39 = 4;
    public static final int SMFPTR_BARCODE_ITF = 5;
    public static final int SMFPTR_BARCODE_CODABAR = 6;
    public static final int SMFPTR_BARCODE_CODE93 = 7;
    public static final int SMFPTR_BARCODE_CODE128 = 8;
    public static final int SMFPTR_BARCODE_PDF417 = 10;
    public static final int SMFPTR_BARCODE_GS1_OMNI = 11;
    public static final int SMFPTR_BARCODE_GS1_TRUNC = 12;
    public static final int SMFPTR_BARCODE_GS1_LIMIT = 13;
    public static final int SMFPTR_BARCODE_GS1_EXP = 14;
    public static final int SMFPTR_BARCODE_GS1_STK = 15;
    public static final int SMFPTR_BARCODE_GS1_STK_OMNI = 16;
    public static final int SMFPTR_BARCODE_GS1_EXP_STK = 17;
    public static final int SMFPTR_BARCODE_AZTEC = 18;
    public static final int SMFPTR_BARCODE_DATA_MATRIX = 19;
    public static final int SMFPTR_BARCODE_MAXICODE = 20;
    public static final int SMFPTR_BARCODE_QR_CODE = 21;
    public static final int SMFPTR_BARCODE_RSS_14 = 22;
    public static final int SMFPTR_BARCODE_RSS_EXPANDED = 23;
    public static final int SMFPTR_BARCODE_UPC_EAN_EXTENSION = 24;

    // ///////////////////////////////////////////////////////////////////
    // Barcode text position constants
    // ///////////////////////////////////////////////////////////////////
    /**
     * Not printed *
     */
    public static final int SMFPTR_TEXTPOS_NOTPRINTED = 0;

    /**
     * Above the bar code *
     */
    public static final int SMFPTR_TEXTPOS_ABOVE = 1;

    /**
     * Below the bar code *
     */
    public static final int SMFPTR_TEXTPOS_BELOW = 2;

    /**
     * Both above and below the bar code *
     */
    public static final int SMFPTR_TEXTPOS_BOTH = 3;

    // ///////////////////////////////////////////////////////////////////
    // Barcode printing constants
    // ///////////////////////////////////////////////////////////////////
    /**
     * PrinterBarcode is rendered and printed by device *
     */
    public static final int SMFPTR_PRINTTYPE_DEVICE = 0;

    /**
     * PrinterBarcode is rendered and printed by driver *
     */
    public static final int SMFPTR_PRINTTYPE_DRIVER = 1;

    /**
     * Autoselect *
     */
    public static final int SMFPTR_PRINTTYPE_AUTO = 2;

    // ///////////////////////////////////////////////////////////////////
    // Logo printing constants
    // ///////////////////////////////////////////////////////////////////
    /**
     * Logo printed after receipt header *
     */
    public static final int SMFPTR_LOGO_AFTER_HEADER = 0;

    /**
     * Logo printed before receipt trailer *
     */
    public static final int SMFPTR_LOGO_BEFORE_TRAILER = 1;

    /**
     * Logo printed after receipt trailer *
     */
    public static final int SMFPTR_LOGO_AFTER_TRAILER = 2;

    /**
     * Logo printed after receipt additional trailer *
     */
    public static final int SMFPTR_LOGO_AFTER_ADDTRAILER = 3;

    /**
     * Logo printed before receipt header *
     */
    public static final int SMFPTR_LOGO_BEFORE_HEADER = 4;

    /**
     * Not a logo, just print image *
     */
    public static final int SMFPTR_LOGO_NONE = 100;
    public static final int SMFPTR_LOGO_PRINT = 101;

    // ///////////////////////////////////////////////////////////////////
    // Report type constants
    // ///////////////////////////////////////////////////////////////////
    /**
     * Short report *
     */
    public static final int SMFPTR_REPORT_TYPE_SHORT = 0;

    /**
     * Full report *
     */
    public static final int SMFPTR_REPORT_TYPE_FULL = 1;

    // ///////////////////////////////////////////////////////////////////
    // DirectIO command constants
    // ///////////////////////////////////////////////////////////////////
    /**
     * Execute command object *
     */
    public static final int SMFPTR_DIO_COMMAND = 0x00;

    /**
     * Print barcode object *
     */
    public static final int SMFPTR_DIO_PRINT_BARCODE_OBJECT = 0x01;

    /**
     * Set department *
     */
    public static final int SMFPTR_DIO_SET_DEPARTMENT = 0x02;

    /**
     * Get department *
     */
    public static final int SMFPTR_DIO_GET_DEPARTMENT = 0x03;

    /**
     * Execute string command *
     */
    public static final int SMFPTR_DIO_STRCOMMAND = 0x04;

    /**
     * Read table command *
     */
    public static final int SMFPTR_DIO_READTABLE = 0x05;

    /**
     * Write table command *
     */
    public static final int SMFPTR_DIO_WRITETABLE = 0x06;

    /**
     * Read payment type name *
     */
    public static final int SMFPTR_DIO_READ_PAYMENT_NAME = 0x07;

    /**
     * write payment type name *
     */
    public static final int SMFPTR_DIO_WRITE_PAYMENT_NAME = 0x08;

    /**
     * Read end of day flag *
     */
    public static final int SMFPTR_DIO_READ_DAY_END = 0x09;

    /**
     * Print barcode command *
     */
    public static final int SMFPTR_DIO_PRINT_BARCODE = 0x0A;

    /**
     * Load image from file *
     */
    public static final int SMFPTR_DIO_LOAD_IMAGE = 0x0B;

    /**
     * Print image *
     */
    public static final int SMFPTR_DIO_PRINT_IMAGE = 0x0C;

    /**
     * Clear all images *
     */
    public static final int SMFPTR_DIO_CLEAR_IMAGES = 0x0D;

    /**
     * Set logo *
     */
    public static final int SMFPTR_DIO_ADD_LOGO = 0x0E;

    /**
     * Clear logo *
     */
    public static final int SMFPTR_DIO_CLEAR_LOGO = 0x0F;

    /**
     * Print black line *
     */
    public static final int SMFPTR_DIO_PRINT_LINE = 0x10;

    /**
     * Get driver parameter *
     */
    public static final int SMFPTR_DIO_GET_DRIVER_PARAMETER = 0x11;

    /**
     * Set driver parameter *
     */
    public static final int SMFPTR_DIO_SET_DRIVER_PARAMETER = 0x12;

    /**
     * Print text *
     */
    public static final int SMFPTR_DIO_PRINT_TEXT = 0x13;

    /**
     * Write table values from file *
     */
    public static final int SMFPTR_DIO_WRITE_TABLES = 0x14;

    /**
     * Read table values to file *
     */
    public static final int SMFPTR_DIO_READ_TABLES = 0x15;

    /**
     * Read device serial number *
     */
    public static final int SMFPTR_DIO_READ_SERIAL = 0x16;

    /**
     * Read EJ serial number *
     */
    public static final int SMFPTR_DIO_READ_EJ_SERIAL = 0x17;

    /**
     * Open cash drawer *
     */
    public static final int SMFPTR_DIO_OPEN_DRAWER = 0x18;

    /**
     * Read cash drawer state *
     */
    public static final int SMFPTR_DIO_READ_DRAWER_STATE = 0x19;

    /**
     * Read printer status *
     */
    public static final int SMFPTR_DIO_READ_PRINTER_STATUS = 0x1A;

    /**
     * Read cash register *
     */
    public static final int SMFPTR_DIO_READ_CASH_REG = 0x1B;

    /**
     * Read operation register *
     */
    public static final int SMFPTR_DIO_READ_OPER_REG = 0x1C;

    /* Execute command */
    public static final int SMFPTR_DIO_COMMAND_OBJECT = 0x1D;

    /* Save XML Z report */
    public static final int SMFPTR_DIO_XML_ZREPORT = 0x1E;

    /* Save CSV Z report */
    public static final int SMFPTR_DIO_CSV_ZREPORT = 0x1F;

    /* Write parameter */
    public static final int SMFPTR_DIO_WRITE_DEVICE_PARAMETER = 0x20;

    /* Read parameter */
    public static final int SMFPTR_DIO_READ_DEVICE_PARAMETER = 0x21;

    /**
     * Load logo *
     */
    public static final int SMFPTR_DIO_LOAD_LOGO = 0x22;

    /**
     * Read fiscal day status *
     */
    public static final int SMFPTR_DIO_READ_DAY_STATUS = 0x23;

    /**
     * Read device license number *
     */
    public static final int SMFPTR_DIO_READ_LICENSE = 0x24;

    /**
     * Is printer ready for fiscal documents *
     */
    public static final int SMFPTR_DIO_IS_READY_FISCAL = 0x25;

    /**
     * Is printer ready for non fiscal documents *
     */
    public static final int SMFPTR_DIO_IS_READY_NONFISCAL = 0x26;

    /**
     * Read maximum graphics size *
     */
    public static final int SMFPTR_DIO_READ_MAX_GRAPHICS = 0x27;

    /**
     * Read header line *
     */
    public static final int SMFPTR_DIO_GET_HEADER_LINE = 0x28;

    /**
     * Read trailer line *
     */
    public static final int SMFPTR_DIO_GET_TRAILER_LINE = 0x29;

    /**
     * Read text length *
     */
    public static final int SMFPTR_DIO_GET_TEXT_LENGTH = 0x2A;

    /**
     * Read cashier name *
     */
    public static final int SMFPTR_DIO_READ_CASHIER_NAME = 0x2B;

    /**
     * Write cashier name *
     */
    public static final int SMFPTR_DIO_WRITE_CASHIER_NAME = 0x2C;

    /**
     * Cut paper *
     */
    public static final int SMFPTR_DIO_CUT_PAPER = 0x2D;

    /**
     * Wait for printing *
     */
    public static final int SMFPTR_DIO_WAIT_PRINT = 0x2E;

    /**
     * Get receipt state *
     */
    public static final int SMFPTR_DIO_GET_RECEIPT_STATE = 0x2F;

    /**
     * Short status *
     */
    public static final int SMFPTR_DIO_READ_SHORT_STATUS = 0x30;

    /**
     * Long status *
     */
    public static final int SMFPTR_DIO_READ_LONG_STATUS = 0x31;

    /**
     * Cancel IO *
     */
    public static final int SMFPTR_DIO_CANCELIO = 0x32;

    /**
     * Open fiscal day *
     */
    public static final int SMFPTR_DIO_OPEN_DAY = 0x33;

    /**
     * Write string tag 
     */
    public static final int SMFPTR_DIO_FS_WRITE_TAG = 0x34;
    
    /**
     * Cut and print document end
     */
    public static final int SMFPTR_DIO_PRINT_DOC_END = 0x35;
    
    /**
     * Write TLV structure
     */
    public static final int SMFPTR_DIO_FS_WRITE_TLV = 0x36;
    
    /**
     * Disable receipt printing
     */
    public static final int SMFPTR_DIO_FS_DISABLE_PRINT = 0x37;
    
    /**
     * Print non fiscal document 
     * If receipt is opened - it will be cancelled 
     */
    public static final int SMFPTR_DIO_PRINT_NON_FISCAL = 0x38;
    
    /**
     * Write customer email
     */
    public static final int SMFPTR_DIO_FS_WRITE_CUSTOMER_EMAIL = 0x39;
    
    /**
     * Write customer phone number
     */
    public static final int SMFPTR_DIO_FS_WRITE_CUSTOMER_PHONE = 0x3A;
    
    /**
     * Print calculation report
     */
    public static final int SMFPTR_DIO_FS_PRINT_CALC_REPORT = 0x3B;

    /**
     * Print journal report
     */
    public static final int SMFPTR_DIO_PRINT_JOURNAL = 0x3C;

    /**
     * Set receipt discount amount
     * For receipt discount from 1 to 99 kopeks
     */
    public static final int SMFPTR_DIO_SET_DISCOUNT_AMOUNT = 0x3D;

    /**
     * Read FS parameters
     */
    public static final int SMFPTR_DIO_READ_FS_PARAMS = 0x3E;
    
    /**
     * Read FS tickets
     */
    public static final int SMFPTR_DIO_READ_FS_TICKETS = 0x3F;
    public static final int SMFPTR_DIO_READ_FS_TICKETS2 = 0x40;
    public static final int SMFPTR_DIO_READ_FS_TICKETS3 = 0x41;
    public static final int SMFPTR_DIO_READ_FS_TICKETS4 = 0x42;
    
    public static final int SMFPTR_DIO_PRINT_CORRECTION2 = 0x43;
    
    public static final int SMFPTR_DIO_READ_TOTALS = 0x44;
    
    public static final int SMFPTR_DIO_FS_WRITE_TAG_1162 = 0x45;

    public static final int SMFPTR_DIO_PRINT_RAW_GRAPHICS = 0x46;

    public static final int SMFPTR_DIO_READ_MAX_GRAPHICS_WIDTH = 0x47;

    public static final int SMFPTR_DIO_PRINT_CORRECTION = 0x48;

    public static final int SMFPTR_DIO_FEED_PAPER = 0x49;

    // ///////////////////////////////////////////////////////////////////
    // Parameter constants for directIO commands:
    // FPTR_DIO_GET_DRIVER_PARAMETER,
    // FPTR_DIO_SET_DRIVER_PARAMETER
    // ///////////////////////////////////////////////////////////////////
    /**
     * Report device for printReport, printPeriodicTotalsReport *
     */
    public static final int SMFPTR_DIO_PARAM_REPORT_DEVICE = 0;

    /**
     * Report type for printReport, printPeriodicTotalsReport *
     */
    public static final int SMFPTR_DIO_PARAM_REPORT_TYPE = 1;

    /**
     * Number of header lines *
     */
    public static final int SMFPTR_DIO_PARAM_NUMHEADERLINES = 2;

    /**
     * Number of trailer lines *
     */
    public static final int SMFPTR_DIO_PARAM_NUMTRAILERLINES = 3;

    /**
     * Polling enabled *
     */
    public static final int SMFPTR_DIO_PARAM_POLL_ENABLED = 4;

    /**
     * Cut mode *
     */
    public static final int SMFPTR_DIO_PARAM_CUT_MODE = 5;

    /**
     * Font number *
     */
    public static final int SMFPTR_DIO_PARAM_FONT_NUMBER = 6;

    public static final int SMFPTR_DIO_PARAM_USR_PASSWORD = 7;
    public static final int SMFPTR_DIO_PARAM_SYS_PASSWORD = 8;
    public static final int SMFPTR_DIO_PARAM_TAX_PASSWORD = 9;
    
    public static final int SMFPTR_DIO_PARAM_TAX_VALUE_0   = 10;
    public static final int SMFPTR_DIO_PARAM_TAX_VALUE_1   = 11;
    public static final int SMFPTR_DIO_PARAM_TAX_VALUE_2   = 12;
    public static final int SMFPTR_DIO_PARAM_TAX_VALUE_3   = 13;
    public static final int SMFPTR_DIO_PARAM_TAX_VALUE_4   = 14;
    public static final int SMFPTR_DIO_PARAM_TAX_VALUE_5   = 15;
    public static final int SMFPTR_DIO_PARAM_TAX_SYSTEM    = 16;
    
    // ///////////////////////////////////////////////////////////////////
    // TableMode
    public static final int SMFPTR_TABLE_MODE_AUTO = 0;
    public static final int SMFPTR_TABLE_MODE_DISABLED = 1;

    // ///////////////////////////////////////////////////////////////////
    // CutMode
    public static final int SMFPTR_CUT_MODE_AUTO = 0;
    public static final int SMFPTR_CUT_MODE_DISABLED = 1;

    // ///////////////////////////////////////////////////////////////////
    // LineType constants for SMFPTR_DIO_PRINT_LINE request
    public static final int SMFPTR_LINE_TYPE_BLACK = 0;
    public static final int SMFPTR_LINE_TYPE_WHITE = 1;

    // ///////////////////////////////////////////////////////////////////
    // LogoMode constants
    public static final int SMFPTR_LOGO_MODE_FEED_PAPER = 0;
    public static final int SMFPTR_LOGO_MODE_SPLIT_IMAGE = 1;

    // ///////////////////////////////////////////////////////////////////
    // DayStatus constants
    public static final int SMFPTR_DAY_STATUS_CLOSED = 1;
    public static final int SMFPTR_DAY_STATUS_OPENED = 2;
    public static final int SMFPTR_DAY_STATUS_EXPIRED = 3;
    public static final int SMFPTR_DAY_STATUS_UNKNOWN = 4;

    // ///////////////////////////////////////////////////////////////////
    // SearchMode constants
    public static final int SMFPTR_SEARCH_NONE = 0;
    public static final int SMFPTR_SEARCH_ON_ERROR = 1;

    // ///////////////////////////////////////////////////////////////////
    // SearchMode constants
    public static final int SMFPTR_DAILY_TOTAL_ALL = 0;
    public static final int SMFPTR_DAILY_TOTAL_CASH = 1;
    public static final int SMFPTR_DAILY_TOTAL_PT2 = 2;
    public static final int SMFPTR_DAILY_TOTAL_PT3 = 3;
    public static final int SMFPTR_DAILY_TOTAL_PT4 = 4;

    // ///////////////////////////////////////////////////////////////////
    // SaleReceiptType constants
    public static final int SMFPTR_RECEIPT_NORMAL = 0;
    public static final int SMFPTR_RECEIPT_GLOBUS = 1;

    // ///////////////////////////////////////////////////////////////////////////
    // SeparatorLine constants
    public static final int SMFPTR_SEPARATOR_LINE_NONE = 0;
    public static final int SMFPTR_SEPARATOR_LINE_DASHES = 1;
    public static final int SMFPTR_SEPARATOR_LINE_GRAPHICS = 2;

    // ///////////////////////////////////////////////////////////////////////////
    // Separator type
    public static final int SMFPTR_SEPARATOR_BLACK = 0;
    public static final int SMFPTR_SEPARATOR_WHITE = 1;
    public static final int SMFPTR_SEPARATOR_DOTTED_1 = 2;
    public static final int SMFPTR_SEPARATOR_DOTTED_2 = 3;

    // ///////////////////////////////////////////////////////////////////////////
    // Receipt state
    public static final int RECEIPT_STATE_UNKNOWN = 0;
    public static final int RECEIPT_STATE_OPENED = 1;
    public static final int RECEIPT_STATE_CLOSED = 2;
    public static final int RECEIPT_STATE_CANCELLED = 3;

    // ///////////////////////////////////////////////////////////////////////////
    // Protocol type constants
    public static final int SMFPTR_PROTOCOL_1 = 0;
    public static final int SMFPTR_PROTOCOL_2 = 1;

    // ///////////////////////////////////////////////////////////////////
    // OpenReceiptType constants
    // Open receipt with first operation (printRecItem etc.)
    public static final int SMFPTR_OPEN_RECEIPT_ITEM = 0;
    // Open receipt in beginFiscalReceipt method
    public static final int SMFPTR_OPEN_RECEIPT_BEGIN = 1;

    // ///////////////////////////////////////////////////////////////////
    // PortType constants
    public static final int PORT_TYPE_SERIAL = 0;
    public static final int PORT_TYPE_BLUETOOTH = 1;
    public static final int PORT_TYPE_SOCKET = 2;
    public static final int PORT_TYPE_FROMCLASS = 3;

    public static final int PORT_TYPE_MIN = 0;
    public static final int PORT_TYPE_MAX = 3;

    /////////////////////////////////////////////////////////////////////
    // Header mode constants

    // Header printed before receipt beginning
    public static final int SMFPTR_HEADER_MODE_DRIVER = 0;
    // Header printed by driver
    public static final int SMFPTR_HEADER_MODE_PRINTER = 1;
    // Header printed after receipt beginning
    public static final int SMFPTR_HEADER_MODE_DRIVER2 = 2;
    // Header makes nothing
    public static final int SMFPTR_HEADER_MODE_NULL = 3;

    /////////////////////////////////////////////////////////////////////
    // ReadFMTotals mode constants
    public static final int FMTOTALS_ALL_FISCALIZATIONS = 0;
    public static final int FMTOTALS_LAST_FISCALIZATION = 1;

    /////////////////////////////////////////////////////////////////////
    // Compatibility level constants
    // 
    public static final int SMFPTR_COMPAT_LEVEL_FULL = 0;
    public static final int SMFPTR_COMPAT_LEVEL_NONE = 1;
    
    /////////////////////////////////////////////////////////////////////
    // Receipt number request
    
    public static final int SMFPTR_RN_FP_DOCUMENT_NUMBER    = 0;
    public static final int SMFPTR_RN_FS_DOCUMENT_NUMBER    = 1;
    public static final int SMFPTR_RN_FS_RECEIPT_NUMBER     = 2;

    // ///////////////////////////////////////////////////////////////////
    // Report type constants

    public static final int SMFPTR_JRN_REPORT_CURRENT_DAY = 0;
    public static final int SMFPTR_JRN_REPORT_DAY_NUMBER = 1;
    public static final int SMFPTR_JRN_REPORT_DOC_NUMBER = 2;
    public static final int SMFPTR_JRN_REPORT_DOC_RANGE = 3;
    
    // ///////////////////////////////////////////////////////////////////
    // Receipt type constants
    
    public static final int SMFPTR_RT_SALE      = 100;
    public static final int SMFPTR_RT_BUY       = 101;
    public static final int SMFPTR_RT_RETSALE   = 102;
    public static final int SMFPTR_RT_RETBUY    = 103;
    
}
