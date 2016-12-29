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
import com.shtrih.fiscalprinter.PrinterProtocol;
import com.shtrih.jpos.fiscalprinter.SmFptrConst;
import com.shtrih.jpos.fiscalprinter.PrinterImage;
import com.shtrih.jpos.fiscalprinter.ReceiptImage;
import com.shtrih.jpos.fiscalprinter.DioCsvZReport;
import com.shtrih.fiscalprinter.command.PrinterStatus;
import com.shtrih.fiscalprinter.command.PrinterCommand;
import com.shtrih.jpos.fiscalprinter.directIO.DIOExecuteCommandStr;
import com.shtrih.jpos.fiscalprinter.directIO.DIOGetDriverParameter;
import com.shtrih.jpos.fiscalprinter.directIO.DIOGetReceiptState;
import com.shtrih.jpos.fiscalprinter.directIO.DIOIsReadyFiscal;
import com.shtrih.jpos.fiscalprinter.directIO.DIOIsReadyNonFiscal;
import com.shtrih.jpos.fiscalprinter.directIO.DIOOpenDrawer;
import com.shtrih.jpos.fiscalprinter.directIO.DIOPrintBarcode;
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
import com.shtrih.jpos.fiscalprinter.FptrParameters;

/**
 *
 * @author V.Kravtsov
 */
public class DirectIOHandler {
    
    private final FiscalPrinterImpl service;
    
    public DirectIOHandler(FiscalPrinterImpl service){
        this.service = service;
    }
    
    public SMFiscalPrinter getPrinter(){
        return service.getPrinter();
    }
    
    public PrinterProtocol getDevice(){
        return getPrinter().getDevice();
    }
    
    public FptrParameters getParams(){
        return service.getParams();
    }
    
    public void directIO(int command, int[] data, Object object)
            throws Exception {
        PrinterImage image;
        switch (command) {
            case SmFptrConst.SMFPTR_DIO_COMMAND_OBJECT:
                PrinterCommand printerCommand = (PrinterCommand) object;
                getDevice().send(printerCommand);
                getPrinter().check(printerCommand.getResultCode());
                break;

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
                new DIOExecuteCommandStr(service).execute(data, object);
                break;

            case SmFptrConst.SMFPTR_DIO_READTABLE:
                new DIOReadTable(service).execute(data, object);
                break;

            case SmFptrConst.SMFPTR_DIO_WRITETABLE:
                new DIOWriteTable(service).execute(data, object);
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
                new DIOPrintBarcode(service).execute(data, object);
                break;

            case SmFptrConst.SMFPTR_DIO_LOAD_IMAGE:
                String fileName = ((String[]) (object))[0];
                PrinterImage printerImage = new PrinterImage(fileName);
               getPrinter().loadImage(printerImage, true);
                int imageIndex = service.getPrinterImages().getIndex(printerImage);
                data[0] = imageIndex;
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
                int logoImageIndex = data[0];
                int logoPosition = data[1];
                service.getPrinterImages().checkIndex(logoImageIndex);
                ReceiptImage logo = new ReceiptImage();
                logo.setImageIndex(logoImageIndex);
                logo.setPosition(logoPosition);
                service.getReceiptImages().add(logo);
                service.saveProperties();
                break;

            case SmFptrConst.SMFPTR_DIO_LOAD_LOGO:
                fileName = ((String[]) (object))[0];
                logoPosition = data[0];
                imageIndex = service.loadLogo(fileName, logoPosition);
                data[0] = imageIndex;
                break;

            // clear logo
            case SmFptrConst.SMFPTR_DIO_CLEAR_LOGO:
                service.getReceiptImages().clear();
                service.saveProperties();
                break;

            case SmFptrConst.SMFPTR_DIO_PRINT_LINE:
                byte[] lineData = new byte[service.getModel().getPrintWidth() / 8];
                int lineHeight = data[0];
                int lineType = SmFptrConst.SMFPTR_LINE_TYPE_BLACK;
                if (data.length > 1) {
                    lineType = data[1];
                }
                if (lineType == SmFptrConst.SMFPTR_LINE_TYPE_WHITE) {
                    Arrays.fill(lineData, (byte) 0x00);
                } else {
                    Arrays.fill(lineData, (byte) 0xFF);
                }
                getPrinter().printGraphicLine(lineHeight, lineData);
                getPrinter().waitForPrinting();
                break;

            // get driver parameter
            case SmFptrConst.SMFPTR_DIO_GET_DRIVER_PARAMETER:
                new DIOGetDriverParameter(service).execute(data, object);
                break;

            case SmFptrConst.SMFPTR_DIO_SET_DRIVER_PARAMETER:
                new DIOSetDriverParameter(service).execute(data, object);
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
                new DIOReadPrinterStatus(service).execute(data, object);
                break;

            case SmFptrConst.SMFPTR_DIO_READ_CASH_REG:
                new DIOReadCashReg(service).execute(data, object);
                break;

            case SmFptrConst.SMFPTR_DIO_READ_OPER_REG:
                new DIOReadOperReg(service).execute(data, object);
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
                new DIOReadTextLength(service).execute(data, object);
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
                    
            default:
                throw new JposException(JposConst.JPOS_E_ILLEGAL,
                        Localizer.getString(Localizer.invalidParameterValue)
                        + ", command");
        }
    }
    
}
