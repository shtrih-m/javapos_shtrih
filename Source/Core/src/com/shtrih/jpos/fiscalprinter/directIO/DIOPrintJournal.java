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
        TextReportPrinter reportPrinter = new TextReportPrinter(service);
        int[] params = (int[]) object;
        int reportType = params[0];

        reportPrinter.start();
        try {
            service.resetPrinter();
            switch (reportType) {
                case SmFptrConst.SMFPTR_JRN_REPORT_CURRENT_DAY:
                    reportPrinter.printEJReportCurrentDay(readDayNumber());
                    break;
                case SmFptrConst.SMFPTR_JRN_REPORT_DAY_NUMBER:
                    int dayNumber = params[1];

                    if (dayNumber == readDayNumber())
                        reportPrinter.printEJReportCurrentDay(dayNumber);
                    else
                        reportPrinter.printEJReportDayNumber(dayNumber);
                    break;

                case SmFptrConst.SMFPTR_JRN_REPORT_DOC_NUMBER:
                    int docNumber = params[1];
                    reportPrinter.printEJReportDocNumber(docNumber);
                    break;

                case SmFptrConst.SMFPTR_JRN_REPORT_DOC_RANGE:
                    int firstDocNumber = params[1];
                    int lastDocNumber = params[2];
                    reportPrinter.printEJReportDocRange(firstDocNumber, lastDocNumber);
                    break;
                default:
                    throw new Exception("Invalid ReportType value");
            }
            service.printDocEnd();
        } finally {
            reportPrinter.stop();
        }
    }

    private int readDayNumber() throws Exception {
        return service.readLongStatus().getCurrentShiftNumber();
    }
}
