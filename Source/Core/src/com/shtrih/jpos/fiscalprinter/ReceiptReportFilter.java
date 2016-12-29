/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.jpos.fiscalprinter;

import com.shtrih.util.CompositeLogger;

import com.shtrih.fiscalprinter.SMFiscalPrinter;
import com.shtrih.fiscalprinter.command.CloseRecParams;
import com.shtrih.fiscalprinter.command.EndFiscalReceipt;
import com.shtrih.fiscalprinter.command.IPrinterEvents;
import com.shtrih.fiscalprinter.command.LongPrinterStatus;
import com.shtrih.fiscalprinter.command.PrintCashIn;
import com.shtrih.fiscalprinter.command.PrintCashOut;
import com.shtrih.fiscalprinter.command.PrinterCommand;
import com.shtrih.fiscalprinter.command.PrinterConst;
import com.shtrih.fiscalprinter.command.PrinterStatus;
import com.shtrih.util.FileUtils;
import com.shtrih.util.SysUtils;

/**
 * @author V.Kravtsov
 */
public class ReceiptReportFilter implements IPrinterEvents {

    private final SMFiscalPrinter printer;
    private final FptrParameters params;
    private final ReceiptReport report = new ReceiptReport();
    private static CompositeLogger logger = CompositeLogger.getLogger(ReceiptReportFilter.class);

    public ReceiptReportFilter(SMFiscalPrinter printer, FptrParameters params) {
        this.printer = printer;
        this.params = params;
    }

    
    public void printerStatusRead(PrinterStatus status) {
    }

    public String getDayNumberText(int dayNumber) {
        String result = String.valueOf(dayNumber);
        for (int i = result.length(); i < 4; i++) {
            result = "0" + result;
        }
        return result;
    }

    public void saveReport(ReceiptReport report) {
        try {
            XmlReceiptWriter writer = new XmlReceiptWriter();

            String dayNumber = getDayNumberText(report.dayNumber);
            String fileName = params.receiptReportFileName;
            fileName = SysUtils.getFilesPath()
                    + FileUtils.removeExtention(fileName) + "_" + dayNumber
                    + FileUtils.getExtention(fileName);
            writer.save(report, fileName);

        } catch (Exception e) {
            logger.error("saveReport", e);
        }

    }

    @Override
    public void init() {

    }

    @Override
    public void done() {

    }

    public void afterCommand(PrinterCommand command) {
        switch (command.getCode()) {
            case 0x50:
                PrintCashIn cashInCommand = (PrintCashIn) command;
                report.state = 0;
                report.amount = cashInCommand.getAmount();
                report.payments[0] = 0;
                report.payments[1] = 0;
                report.payments[2] = 0;
                report.payments[3] = 0;
                saveReport(report);
                break;

            case 0x51:
                PrintCashOut cashOutCommand = (PrintCashOut) command;
                report.state = 0;
                report.amount = cashOutCommand.getAmount();
                report.payments[0] = 0;
                report.payments[1] = 0;
                report.payments[2] = 0;
                report.payments[3] = 0;
                saveReport(report);
                break;

            case 0x85:
                EndFiscalReceipt endFiscalReceipt = (EndFiscalReceipt) command;
                CloseRecParams params = endFiscalReceipt.getParams();
                long amount = params.getSum1() + params.getSum2()
                        + params.getSum3() + params.getSum4()
                        - endFiscalReceipt.getChange();
                report.state = 0;
                report.amount = amount;
                report.payments[0] = params.getSum1();
                report.payments[1] = params.getSum2();
                report.payments[2] = params.getSum3();
                report.payments[3] = params.getSum4();
                saveReport(report);
        }
    }

    private int readReg(int num) throws Exception {
        return printer.readOperationRegister(num) + 1;
    }

    
    public void beforeCommand(PrinterCommand command) {
        try {
            LongPrinterStatus status;
            switch (command.getCode()) {
                case 0x50:
                    status = printer.readLongStatus();
                    report.dayNumber = printer.readDayNumber() + 1;
                    report.docID = status.getDocumentNumber() + 1;
                    report.date = status.getDate();
                    report.time = status.getTime();
                    report.recType = ReceiptReport.XML_RT_CASH_IN;
                    report.id = readReg(0x9B);
                    break;

                case 0x51:
                    status = printer.readLongStatus();
                    report.dayNumber = printer.readDayNumber() + 1;
                    report.docID = status.getDocumentNumber() + 1;
                    report.date = status.getDate();
                    report.time = status.getTime();
                    report.recType = ReceiptReport.XML_RT_CASH_OUT;
                    report.id = readReg(0x9C);
                    break;

                case 0x85:
                    status = printer.readLongStatus();
                    report.dayNumber = printer.readDayNumber() + 1;
                    report.docID = status.getDocumentNumber() + 1;
                    report.date = status.getDate();
                    report.time = status.getTime();

                    switch (status.getMode()) {
                        case PrinterConst.ECRMODE_RECSELL:
                            report.recType = ReceiptReport.XML_RT_SALE;
                            report.id = readReg(0x94);
                            break;

                        case PrinterConst.ECRMODE_RECBUY:
                            report.recType = ReceiptReport.XML_RT_BUY;
                            report.id = readReg(0x95);
                            break;

                        case PrinterConst.ECRMODE_RECRETSELL:
                            report.recType = ReceiptReport.XML_RT_RETSALE;
                            report.id = readReg(0x96);
                            break;

                        case PrinterConst.ECRMODE_RECRETBUY:
                            report.recType = ReceiptReport.XML_RT_RETBUY;
                            report.id = readReg(0x97);
                            break;

                    }
            }
        } catch (Exception e) {
            logger.error(e);
        }
    }
}
