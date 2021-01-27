/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shtrih.jpos.fiscalprinter.directIO;

import com.shtrih.jpos.fiscalprinter.FiscalPrinterImpl;
import com.shtrih.jpos.fiscalprinter.SmFptrConst;
import com.shtrih.jpos.fiscalprinter.TextReportPrinter;

public class DIOReadJournal extends DIOItem {

    public DIOReadJournal(FiscalPrinterImpl service) {
        super(service);
    }

    public void execute(int[] data, Object object) throws Exception {
        TextReportPrinter reportPrinter = new TextReportPrinter(service);
        ((Object[])object)[0] = reportPrinter.readReport(data);
    }
}
