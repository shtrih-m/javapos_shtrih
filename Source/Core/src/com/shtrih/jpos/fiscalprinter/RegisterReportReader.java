/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.jpos.fiscalprinter;

/**
 *
 * @author V.Kravtsov
 */
import java.util.Vector;

import com.shtrih.fiscalprinter.SMFiscalPrinter;
import com.shtrih.fiscalprinter.command.CashRegister;
import com.shtrih.fiscalprinter.command.FMTotals;
import com.shtrih.fiscalprinter.command.LongPrinterStatus;
import com.shtrih.fiscalprinter.command.OperationRegister;

public class RegisterReportReader {

    private RegisterReportReader() {
    }

    public static void execute(RegisterReport report, SMFiscalPrinter printer)
            throws Exception {
        report.setDayNumber(printer.readDayNumber() + 1);

        int count = 0;
        int result = 0;
        int minNumber = printer.getModel().getMinCashRegister();
        int maxNumber = printer.getModel().getMaxCashRegister();
        Vector cashRegisters = report.getCashRegisters();
        if (minNumber <= maxNumber) {
            for (int i = minNumber; i <= maxNumber; i++) {
                CashRegister register = new CashRegister(i);
                result = printer.readCashRegister(register);
                if (result != 0) {
                    break;
                }
                cashRegisters.add(register);
            }
        }
        // extended cash registers
        minNumber = printer.getModel().getMinCashRegister2();
        maxNumber = printer.getModel().getMaxCashRegister2();
        if (minNumber <= maxNumber) {
            cashRegisters = report.getCashRegisters();
            for (int i = minNumber; i <= maxNumber; i++) {
                CashRegister register = new CashRegister(i);
                result = printer.readCashRegister(register);
                if (result != 0) {
                    break;
                }
                cashRegisters.add(register);
            }
        }

        minNumber = printer.getModel().getMinOperationRegister();
        maxNumber = printer.getModel().getMaxOperationRegister();
        Vector operRegisters = report.getOperRegisters();
        if (minNumber <= maxNumber) {
            for (int i = minNumber; i <= maxNumber; i++) {
                OperationRegister register = new OperationRegister(i);
                result = printer.readOperationRegister(register);
                if (result != 0) {
                    break;
                }
                operRegisters.add(register);
            }
        }

        if (printer.getLongStatus().isFiscalized()) {
            FMTotals totals = printer.readFPTotals(SmFptrConst.FMTOTALS_ALL_FISCALIZATIONS);
            report.setAllFiscalizations(totals);
            totals = printer.readFPTotals(SmFptrConst.FMTOTALS_LAST_FISCALIZATION);
            report.setLastFiscalization(totals);
        }
        report.setCapCommStatus(printer.getCapFiscalStorage());
        if (printer.getCapFiscalStorage()) {
            report.setCommStatus(printer.fsReadCommStatus());
        }
    }

}
