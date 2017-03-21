/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.jpos.fiscalprinter.directIO;

import jpos.JposConst;
import java.util.Arrays;
import jpos.JposException;

import com.shtrih.util.Localizer;
import com.shtrih.barcode.PrinterBarcode;
import com.shtrih.fiscalprinter.FontNumber;
import com.shtrih.fiscalprinter.PrinterProtocol;
import com.shtrih.jpos.fiscalprinter.SmFptrConst;
import com.shtrih.jpos.fiscalprinter.PrinterImage;
import com.shtrih.jpos.fiscalprinter.ReceiptImage;
import com.shtrih.jpos.fiscalprinter.DioCsvZReport;
import com.shtrih.fiscalprinter.command.PrinterStatus;
import com.shtrih.fiscalprinter.command.PrinterCommand;
import com.shtrih.jpos.fiscalprinter.directIO.DIOExecuteCommandStr2;
import com.shtrih.jpos.fiscalprinter.directIO.DIOGetDriverParameter;
import com.shtrih.jpos.fiscalprinter.directIO.DIOGetReceiptState;
import com.shtrih.jpos.fiscalprinter.directIO.DIOIsReadyFiscal;
import com.shtrih.jpos.fiscalprinter.directIO.DIOIsReadyNonFiscal;
import com.shtrih.jpos.fiscalprinter.directIO.DIOOpenDrawer;
import com.shtrih.jpos.fiscalprinter.directIO.DIOPrintBarcode2;
import com.shtrih.jpos.fiscalprinter.directIO.DIOPrintText;
import com.shtrih.jpos.fiscalprinter.directIO.DIOReadCashReg;
import com.shtrih.jpos.fiscalprinter.directIO.DIOReadCashierName;
import com.shtrih.jpos.fiscalprinter.directIO.DIOReadDayStatus;
import com.shtrih.jpos.fiscalprinter.directIO.DIOReadDrawerState;
import com.shtrih.jpos.fiscalprinter.directIO.DIOReadEJSerial;
import com.shtrih.jpos.fiscalprinter.directIO.DIOReadHeaderLine;
import com.shtrih.jpos.fiscalprinter.directIO.DIOReadLicense;
import com.shtrih.jpos.fiscalprinter.directIO.DIOReadMaxGraphics;
import com.shtrih.jpos.fiscalprinter.directIO.DIOReadOperReg;
import com.shtrih.jpos.fiscalprinter.directIO.DIOReadParameter;
import com.shtrih.jpos.fiscalprinter.directIO.DIOReadPaymentName;
import com.shtrih.jpos.fiscalprinter.directIO.DIOReadPrinterStatus;
import com.shtrih.jpos.fiscalprinter.directIO.DIOReadSerial;
import com.shtrih.jpos.fiscalprinter.directIO.DIOReadTable;
import com.shtrih.jpos.fiscalprinter.directIO.DIOReadTextLength;
import com.shtrih.jpos.fiscalprinter.directIO.DIOReadTrailerLine;
import com.shtrih.jpos.fiscalprinter.directIO.DIOSetDriverParameter;
import com.shtrih.jpos.fiscalprinter.directIO.DIOWaitPrint;
import com.shtrih.jpos.fiscalprinter.directIO.DIOWriteCashierName;
import com.shtrih.jpos.fiscalprinter.directIO.DIOWriteParameter;
import com.shtrih.jpos.fiscalprinter.directIO.DIOWritePaymentName;
import com.shtrih.jpos.fiscalprinter.directIO.DIOWriteTable;
import com.shtrih.jpos.fiscalprinter.directIO.DIOXMLZReport;
import com.shtrih.jpos.fiscalprinter.directIO.DIOReadShortStatus;
import com.shtrih.jpos.fiscalprinter.directIO.DIOReadLongStatus;
import com.shtrih.jpos.fiscalprinter.FiscalPrinterImpl;
import com.shtrih.fiscalprinter.SMFiscalPrinter;
import com.shtrih.fiscalprinter.command.CashRegister;
import com.shtrih.fiscalprinter.command.OperationRegister;
import com.shtrih.jpos.DIOUtils;
import com.shtrih.jpos.fiscalprinter.FptrParameters;

/**
 *
 * @author V.Kravtsov
 */
public class DirectIOHandler2 {

    private final FiscalPrinterImpl service;

    public DirectIOHandler2(FiscalPrinterImpl service) {
        this.service = service;
    }

    public SMFiscalPrinter getPrinter() {
        return service.getPrinter();
    }

    public FptrParameters getParams() {
        return service.getParams();
    }

    public void directIO(int command, int[] data, Object object)
            throws Exception {
        PrinterImage image;
        switch (command) {

            case SmFptrConst.SMFPTR_DIO_COMMAND:
                new DIOExecuteCommand(service).execute(data, object);
                break;

            case SmFptrConst.SMFPTR_DIO_PRINT_BARCODE_OBJECT:
                service.printBarcode((PrinterBarcode) object);
                break;

            case SmFptrConst.SMFPTR_DIO_SET_DEPARTMENT:
                getParams().department = ((int[]) object)[0];
                break;

            case SmFptrConst.SMFPTR_DIO_GET_DEPARTMENT:
                ((int[]) object)[0] = getParams().department;
                break;

            case SmFptrConst.SMFPTR_DIO_STRCOMMAND:
                new DIOExecuteCommandStr2(service).execute(data, object);
                break;

            case SmFptrConst.SMFPTR_DIO_READTABLE:
                new DIOReadTable2(service).execute(data, object);
                break;

            case SmFptrConst.SMFPTR_DIO_WRITETABLE:
                new DIOWriteTable2(service).execute(data, object);
                break;

            case SmFptrConst.SMFPTR_DIO_READ_PAYMENT_NAME:
                new DIOReadPaymentName(service).execute(data, object);
                break;

            case SmFptrConst.SMFPTR_DIO_WRITE_PAYMENT_NAME:
                new DIOWritePaymentName(service).execute(data, object);
                break;

            case SmFptrConst.SMFPTR_DIO_READ_DAY_END:
                PrinterStatus status = getPrinter().readLongPrinterStatus();
                if (status.getPrinterMode().isDayEndRequired()) {
                    data[0] = 1;
                } else {
                    data[0] = 0;
                }
                break;

            case SmFptrConst.SMFPTR_DIO_PRINT_BARCODE:
                new DIOPrintBarcode2(service).execute(data, object);
                break;

            case SmFptrConst.SMFPTR_DIO_LOAD_IMAGE:
                dioLoadImage(data, object);
                break;

            case SmFptrConst.SMFPTR_DIO_PRINT_IMAGE:
                image = service.getPrinterImages().get(data[0]);
                getPrinter().printImage(image);
                getPrinter().waitForPrinting();
                break;

            case SmFptrConst.SMFPTR_DIO_CLEAR_IMAGES:
                service.getPrinterImages().clear();
                service.saveProperties();
                break;

            case SmFptrConst.SMFPTR_DIO_ADD_LOGO:
                dioAddLogo(data, object);
                break;

            case SmFptrConst.SMFPTR_DIO_LOAD_LOGO:
                dioLoadLogo(data, object);
                break;

            // clear logo
            case SmFptrConst.SMFPTR_DIO_CLEAR_LOGO:
                service.getReceiptImages().clear();
                service.saveProperties();
                break;

            case SmFptrConst.SMFPTR_DIO_PRINT_LINE:
                dioPrintLine(data, object);
                break;

            // get driver parameter
            case SmFptrConst.SMFPTR_DIO_GET_DRIVER_PARAMETER:
                dioGetDriverParameter(data, object);
                break;

            case SmFptrConst.SMFPTR_DIO_SET_DRIVER_PARAMETER:
                dioSetDriverParameter(data, object);
                break;

            case SmFptrConst.SMFPTR_DIO_PRINT_TEXT:
                new DIOPrintText(service).execute(data, object);
                break;

            case SmFptrConst.SMFPTR_DIO_WRITE_TABLES:
                service.writeTables((String) object);
                break;

            case SmFptrConst.SMFPTR_DIO_READ_TABLES:
                service.readTables((String) object);
                break;

            case SmFptrConst.SMFPTR_DIO_READ_SERIAL:
                new DIOReadSerial(service).execute(data, object);
                break;

            case SmFptrConst.SMFPTR_DIO_READ_EJ_SERIAL:
                new DIOReadEJSerial(service).execute(data, object);
                break;

            case SmFptrConst.SMFPTR_DIO_OPEN_DRAWER:
                new DIOOpenDrawer(service).execute(data, object);
                break;

            case SmFptrConst.SMFPTR_DIO_READ_DRAWER_STATE:
                new DIOReadDrawerState(service).execute(data, object);
                break;

            case SmFptrConst.SMFPTR_DIO_READ_PRINTER_STATUS:
                dioReadPrinterStatus(data, object);
                break;

            case SmFptrConst.SMFPTR_DIO_READ_CASH_REG:
                dioReadCashReg(data, object);
                break;

            case SmFptrConst.SMFPTR_DIO_READ_OPER_REG:
                dioReadOperReg(data, object);
                break;

            case SmFptrConst.SMFPTR_DIO_COMMAND_OBJECT:
                PrinterCommand printerCommand = (PrinterCommand) object;
                getPrinter().deviceExecute(printerCommand);
                getPrinter().check(printerCommand.getResultCode());
                break;

            case SmFptrConst.SMFPTR_DIO_XML_ZREPORT:
                new DIOXMLZReport(service).execute(data, object);
                break;

            case SmFptrConst.SMFPTR_DIO_CSV_ZREPORT:
                new DioCsvZReport(service).execute(data, object);
                break;

            case SmFptrConst.SMFPTR_DIO_WRITE_DEVICE_PARAMETER:
                new DIOWriteParameter(service).execute(data, object);
                break;

            case SmFptrConst.SMFPTR_DIO_READ_DEVICE_PARAMETER:
                new DIOReadParameter(service).execute(data, object);
                break;

            case SmFptrConst.SMFPTR_DIO_READ_DAY_STATUS:
                new DIOReadDayStatus(service).execute(data, object);
                break;

            case SmFptrConst.SMFPTR_DIO_READ_LICENSE:
                new DIOReadLicense(service).execute(data, object);
                break;

            case SmFptrConst.SMFPTR_DIO_IS_READY_FISCAL:
                new DIOIsReadyFiscal(service).execute(data, object);
                break;

            case SmFptrConst.SMFPTR_DIO_IS_READY_NONFISCAL:
                new DIOIsReadyNonFiscal(service).execute(data, object);
                break;

            case SmFptrConst.SMFPTR_DIO_READ_MAX_GRAPHICS:
                new DIOReadMaxGraphics(service).execute(data, object);
                break;

            case SmFptrConst.SMFPTR_DIO_GET_HEADER_LINE:
                new DIOReadHeaderLine(service).execute(data, object);
                break;

            case SmFptrConst.SMFPTR_DIO_GET_TRAILER_LINE:
                new DIOReadTrailerLine(service).execute(data, object);
                break;

            case SmFptrConst.SMFPTR_DIO_GET_TEXT_LENGTH:
                dioReadTextLength(data, object);
                break;

            case SmFptrConst.SMFPTR_DIO_READ_CASHIER_NAME:
                new DIOReadCashierName(service).execute(data, object);
                break;

            case SmFptrConst.SMFPTR_DIO_WRITE_CASHIER_NAME:
                new DIOWriteCashierName(service).execute(data, object);
                break;

            case SmFptrConst.SMFPTR_DIO_CUT_PAPER:
                new DIOCutPaper(service).execute(data, object);
                break;

            case SmFptrConst.SMFPTR_DIO_WAIT_PRINT:
                new DIOWaitPrint(service).execute(data, object);
                break;

            case SmFptrConst.SMFPTR_DIO_GET_RECEIPT_STATE:
                new DIOGetReceiptState(service).execute(data, object);
                break;

            case SmFptrConst.SMFPTR_DIO_OPEN_DAY:
                new DIOOpenFiscalDay(service).execute(data, object);
                break;

            case SmFptrConst.SMFPTR_DIO_READ_SHORT_STATUS:
                new DIOReadShortStatus(service).execute(data, object);
                break;

            case SmFptrConst.SMFPTR_DIO_READ_LONG_STATUS:
                new DIOReadLongStatus(service).execute(data, object);
                break;

            case SmFptrConst.SMFPTR_DIO_CANCELIO:
                getParams().cancelIO = true;
                break;
                
            case SmFptrConst.SMFPTR_DIO_FS_WRITE_TAG:
                new DIOFSWriteTag(service).execute(data, object);
                break;

            case SmFptrConst.SMFPTR_DIO_FS_WRITE_TLV:
                new DIOFSWriteTLV(service).execute(data, object);
                break;
                
            case SmFptrConst.SMFPTR_DIO_PRINT_DOC_END:
                new DIOPrintDocEnd(service).execute(data, object);
                break;
                
            case SmFptrConst.SMFPTR_DIO_FS_DISABLE_PRINT:
                new DIODisablePrint(service).execute(data, object);
                break;

            case SmFptrConst.SMFPTR_DIO_PRINT_NON_FISCAL:
                new DIOPrintNonFiscal(service).execute(data, object);
                break;
                
            case SmFptrConst.SMFPTR_DIO_FS_WRITE_CUSTOMER_EMAIL:
                new DIOFSWriteCustomerEmail(service).execute(data, object);
                break;
                
            case SmFptrConst.SMFPTR_DIO_FS_WRITE_CUSTOMER_PHONE:
                new DIOFSWriteCustomerPhone(service).execute(data, object);
                break;
                
            case SmFptrConst.SMFPTR_DIO_FS_PRINT_CALC_REPORT:
                new DIOFSPrintCalcReport(service).execute(data, object);
                break;

            case SmFptrConst.SMFPTR_DIO_PRINT_JOURNAL:
                new DIOPrintJournal(service).execute(data, object);
                break;
                
            case SmFptrConst.SMFPTR_DIO_SET_DISCOUNT_AMOUNT:
                new DIOSetDiscountAmount(service).execute(data, object);
                break;
    
            case SmFptrConst.SMFPTR_DIO_READ_FS_PARAMS:
                new DIOReadFSParams(service).execute(data, object);
                break;
                
            case SmFptrConst.SMFPTR_DIO_READ_FS_TICKETS:
                new DIOReadFSTickets(service).execute(data, object);
                break;
                
            case SmFptrConst.SMFPTR_DIO_READ_FS_TICKETS2:
                new DIOReadFSTickets2(service).execute(data, object);
                break;
                
            default:
                throw new JposException(JposConst.JPOS_E_ILLEGAL,
                        Localizer.getString(Localizer.invalidParameterValue)
                        + ", command");
        }
    }

    public void dioAddLogo(int[] data, Object object) throws Exception {
        int[] params = (int[]) object;
        int logoImageIndex = params[0];
        int logoPosition = params[1];
        service.getPrinterImages().checkIndex(logoImageIndex);
        ReceiptImage logo = new ReceiptImage();
        logo.setImageIndex(logoImageIndex);
        logo.setPosition(logoPosition);
        service.getReceiptImages().add(logo);
        service.saveProperties();
    }

    public void dioLoadLogo(int[] data, Object object) throws Exception {
        String fileName = (String) object;
        int logoPosition = data[0];
        int imageIndex = service.loadLogo(fileName, logoPosition);
        data[0] = imageIndex;
    }

    public void dioPrintLine(int[] data, Object object) throws Exception {
        int[] params = (int[]) object;
        byte[] lineData = new byte[service.getModel().getPrintWidth() / 8];
        int lineHeight = params[0];
        int lineType = SmFptrConst.SMFPTR_LINE_TYPE_BLACK;
        if (params.length > 1) {
            lineType = params[1];
        }
        if (lineType == SmFptrConst.SMFPTR_LINE_TYPE_WHITE) {
            Arrays.fill(lineData, (byte) 0x00);
        } else {
            Arrays.fill(lineData, (byte) 0xFF);
        }
        getPrinter().printGraphicLine(lineHeight, lineData);
        getPrinter().waitForPrinting();
    }

    public void dioReadPrinterStatus(int[] data, Object object) throws Exception {
        Object[] params = (Object[]) object;
        PrinterStatus status = service.readPrinterStatus();
        params[0] = status;
    }

    public void dioReadTextLength(int[] data, Object object) throws Exception {
        DIOUtils.checkDataMinLength(data, 1);
        int fontNumber = data[0];
        int textLength = service.getPrinter().getModel()
                .getTextLength(new FontNumber(fontNumber));
        data[0] = textLength;
    }

    public void dioGetDriverParameter(int[] data, Object object) throws Exception {
        DIOUtils.checkDataMinLength(data, 1);
        int paramID = data[0];
        String paramValue = "";
        switch (paramID) {
            case SmFptrConst.SMFPTR_DIO_PARAM_REPORT_DEVICE:
                paramValue = String.valueOf(getParams().reportDevice);
                break;

            case SmFptrConst.SMFPTR_DIO_PARAM_REPORT_TYPE:
                paramValue = String.valueOf(getParams().reportType);
                break;

            case SmFptrConst.SMFPTR_DIO_PARAM_NUMHEADERLINES:
                paramValue = String.valueOf(getParams().numHeaderLines);
                break;

            case SmFptrConst.SMFPTR_DIO_PARAM_NUMTRAILERLINES:
                paramValue = String.valueOf(getParams().numTrailerLines);
                break;

            case SmFptrConst.SMFPTR_DIO_PARAM_POLL_ENABLED:
                paramValue = String.valueOf(getParams().pollEnabled);
                break;

            case SmFptrConst.SMFPTR_DIO_PARAM_CUT_MODE:
                paramValue = String.valueOf(service.getParams().cutMode);
                break;

            case SmFptrConst.SMFPTR_DIO_PARAM_FONT_NUMBER:
                paramValue = String.valueOf(service.getFontNumber());
                break;
                
            case  SmFptrConst.SMFPTR_DIO_PARAM_SYS_PASSWORD:
                paramValue = String.valueOf(service.getPrinter().getSysPassword());
                break;
                
            case  SmFptrConst.SMFPTR_DIO_PARAM_USR_PASSWORD:
                paramValue = String.valueOf(service.getPrinter().getUsrPassword());
                break;
                
            case  SmFptrConst.SMFPTR_DIO_PARAM_TAX_PASSWORD:
                paramValue = String.valueOf(service.getPrinter().getTaxPassword());
                break;
        }
        ((String[]) object)[0] = paramValue;
    }

    public void dioSetDriverParameter(int[] data, Object object) throws Exception {
        DIOUtils.checkDataMinLength(data, 1);
        int paramID = data[0];
        int value = Integer.parseInt(((String[]) object)[0]);
        switch (paramID) {
            case SmFptrConst.SMFPTR_DIO_PARAM_REPORT_DEVICE:
                service.getParams().reportDevice = value;
                break;

            case SmFptrConst.SMFPTR_DIO_PARAM_REPORT_TYPE:
                service.getParams().reportType = value;
                break;

            case SmFptrConst.SMFPTR_DIO_PARAM_NUMHEADERLINES:
                service.setNumHeaderLines(value);
                break;

            case SmFptrConst.SMFPTR_DIO_PARAM_NUMTRAILERLINES:
                service.setNumTrailerLines(value);
                break;

            case SmFptrConst.SMFPTR_DIO_PARAM_POLL_ENABLED:
                service.setPollEnabled(value != 0);
                break;

            case SmFptrConst.SMFPTR_DIO_PARAM_CUT_MODE:
                getParams().cutMode = (value);
                break;

            case SmFptrConst.SMFPTR_DIO_PARAM_FONT_NUMBER:
                service.setFontNumber(value);
                break;
                
            case  SmFptrConst.SMFPTR_DIO_PARAM_SYS_PASSWORD:
                service.getPrinter().setSysPassword(value);
                break;
                
            case  SmFptrConst.SMFPTR_DIO_PARAM_USR_PASSWORD:
                service.getPrinter().setUsrPassword(value);
                break;
                
            case  SmFptrConst.SMFPTR_DIO_PARAM_TAX_PASSWORD:
                service.getPrinter().setTaxPassword(value);
                break;
                
        }
    }

    public void dioReadCashReg(int[] data, Object object) throws Exception {
        DIOUtils.checkDataMinLength(data, 1);
        int number = data[0];
        Object[] params = (Object[]) object;
        CashRegister register = new CashRegister(number);
        long amount = service.printer.readCashRegisterCorrection(number);
        register.setValue(amount);
        params[0] = register;
    }

    public void dioReadOperReg(int[] data, Object object) throws Exception {
        DIOUtils.checkDataMinLength(data, 1);
        int number = data[0];
        Object[] params = (Object[]) object;
        OperationRegister register = new OperationRegister(number);
        service.printer.check(service.printer.readOperationRegister(register));
        params[0] = register;
    }

    public void dioLoadImage(int[] data, Object object) throws Exception 
    {
        String fileName = ((String[]) (object))[0];
        PrinterImage printerImage = new PrinterImage(fileName);
        getPrinter().loadImage(printerImage, true);
        int imageIndex = service.getPrinterImages().getIndex(printerImage);
        data[0] = imageIndex;
    }
}
