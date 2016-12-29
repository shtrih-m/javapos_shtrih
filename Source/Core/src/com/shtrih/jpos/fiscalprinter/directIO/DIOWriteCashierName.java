/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.jpos.fiscalprinter.directIO;

/**
 *
 * @author V.Kravtsov
 */

import com.shtrih.fiscalprinter.SMFiscalPrinter;
import com.shtrih.fiscalprinter.command.PrinterConst;
import com.shtrih.jpos.DIOUtils;
import com.shtrih.jpos.fiscalprinter.FiscalPrinterImpl;

public class DIOWriteCashierName extends DIOItem {

    public DIOWriteCashierName(FiscalPrinterImpl service) {
        super(service);
    }

    public void execute(int[] data, Object object) throws Exception {

        String[] lines = (String[]) object;
        DIOUtils.checkObjectMinLength(lines, 1);
        SMFiscalPrinter printer = getPrinter();
        int operator = printer.readPrinterStatus().getOperator();
        printer.check(printer.writeTable(PrinterConst.SMFP_TABLE_CASHIER,
                operator, 2, lines[0]));
    }
}
