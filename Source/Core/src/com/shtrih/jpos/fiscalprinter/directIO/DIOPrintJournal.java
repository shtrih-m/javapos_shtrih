/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.jpos.fiscalprinter.directIO;

import com.shtrih.jpos.fiscalprinter.FiscalPrinterImpl;
import com.shtrih.jpos.fiscalprinter.SmFptrConst;
import com.shtrih.jpos.fiscalprinter.TextReportPrinter;

public class DIOPrintJournal extends DIOItem {

    public DIOPrintJournal(FiscalPrinterImpl service) {
        super(service);
    }

    public void execute(int[] data, Object object) throws Exception {
        int[] params = (int[]) object;
        TextReportPrinter reportPrinter = new TextReportPrinter(service);
        reportPrinter.print(params);
    }
}
