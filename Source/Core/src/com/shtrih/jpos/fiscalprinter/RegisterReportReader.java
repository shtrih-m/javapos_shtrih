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
import com.shtrih.fiscalprinter.command.CashRegisters;
import com.shtrih.fiscalprinter.command.FMTotals;
import com.shtrih.fiscalprinter.command.LongPrinterStatus;
import com.shtrih.fiscalprinter.command.OperationRegister;
import com.shtrih.util.StringUtils;

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
        CashRegisters cashRegisters = report.getCashRegisters();
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
        for (int i = 0; i < 4; i++) {
            CashRegister register = (CashRegister) cashRegisters.get(185 + i);
            long itemsDiscountAmount = getDayTotals(i, cashRegisters)
                    - getDayPayments(i, cashRegisters);
            register.setValue(itemsDiscountAmount);
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
            String serial = printer.fsReadSerial().getSerial().trim();
            if (serial.length() < 16) {
                serial = StringUtils.stringOfChar('0', 16 - serial.length()) + serial;
            }
            report.setFsSerial(serial);
        }
    }

    public static long getDayPayments(int recType, CashRegisters registers) throws Exception {
        long result = 0;
        // Payment types 1..4
        for (int i = 0; i <= 3; i++) {
            CashRegister register = registers.find(193 + recType + i * 4);
            if (register != null) {
                result += register.getValue();
            }
        }
        // Payment types 1..4
        for (int i = 0; i <= 11; i++) {
            CashRegister register = registers.find(4144 + recType + i * 4);
            if (register != null) {
                result += register.getValue();
            }
        }
        return result;
    }

    public static long getDayTotals(int recType, CashRegisters registers) throws Exception {
        long result = 0;
        for (int i = 0; i <= 15; i++) {
            CashRegister register = registers.find(121 + recType + i * 4);
            if (register != null) {
                result += register.getValue();
            }
        }
        return result;
    }

}
