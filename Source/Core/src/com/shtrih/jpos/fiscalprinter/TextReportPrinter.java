package com.shtrih.jpos.fiscalprinter;

import com.shtrih.fiscalprinter.FontNumber;
import com.shtrih.fiscalprinter.command.*;
import com.shtrih.util.SysUtils;

import java.util.ArrayList;
import java.util.List;

public class TextReportPrinter {

    private final FiscalPrinterImpl printer;
    private final TextReportStorage storage;

    public TextReportPrinter(FiscalPrinterImpl printer) {
        this.printer = printer;

        String storageFilePath = SysUtils.getFilesPath() + printer.getParams().textReportFileName;
        this.storage = new TextReportStorage(storageFilePath);
        this.storage.searchForward = printer.getParams().textReportSearchForward;
    }

    public List<String> readEJReportDayNumber(int dayNumber) throws Exception {
        List<String> dst = storage.searchZReport(dayNumber);

        if (dst == null) {
            throw new Exception(String.format("Смена № %d не найдена", dayNumber));
        }

        String header = String.format("Контрольная лента Смена № %d", dayNumber);
        dst.add(0, header);
        return dst;
    }

    public List<String> readEJReportDocNumber(int docNumber) throws Exception {
        List<String> dst = storage.getDocument(docNumber);
        if (dst == null) {
            throw new Exception(String.format("Документ № %d не найден", docNumber));
        }

        String header = String.format("Контрольная лента Документ № %d", docNumber);
        dst.add(0, header);
        return dst;
    }

    public List<String> readEJReportDocRange(int N1, int N2) throws Exception {
        if (N1 > N2) {
            throw new Exception(String.format("Номер первого документа больше второго (%d > %d)", N1, N2));
        }

        if (N1 == N2) {
            return readEJReportDocNumber(N1);

        }

        List<String> dst = new ArrayList<String>();
        String header = String.format("Контрольная лента Документ с № %d по № %d", N1, N2);
        dst.add(0, header);

        for (int i = N1; i <= N2; i++) {
            List<String> document = storage.getDocument(i);
            if (document == null) {
                continue;
            }

            for (String s : document) {
                dst.add(s);
            }
        }
        return dst;
    }

    public List<String> readEJReportCurrentDay(int dayNumber) throws Exception {
        List<String> dst = storage.getCurrentDayReport();

        if (dst == null) {
            throw new Exception(String.format("Смена № %d не найдена", dayNumber));
        }

        String header = String.format("Контрольная лента Смена № %d", dayNumber);
        dst.add(0, header);
        return dst;
    }

    private int readCurrentDayNumber() throws Exception {
        return printer.readLongStatus().getCurrentShiftNumber();
    }

    public void print(int[] params) throws Exception {
        printer.resetPrinter();
        
        List<String> lines = readReport(params);
        boolean filterEnabled = printer.getTextDocumentFilter().getEnabled();
        printer.getTextDocumentFilter().setEnabled(false);
        try {
            for (String line : lines) {
                printer.getPrinter().printLine(PrinterConst.SMFP_STATION_REC,
                        line, FontNumber.getNormalFont());
            }
            printer.printEndFiscal();
        } finally
        {
            printer.getTextDocumentFilter().setEnabled(filterEnabled);
        }
    }
    
    public List<String> readReport(int[] params)throws Exception
    {
        int reportType = params[0];
        switch (reportType) {
            case SmFptrConst.SMFPTR_JRN_REPORT_CURRENT_DAY:
                return readEJReportCurrentDay(readCurrentDayNumber());
                
            case SmFptrConst.SMFPTR_JRN_REPORT_DAY_NUMBER:
                int dayNumber = params[1];

                if (dayNumber == readCurrentDayNumber()) {
                    return readEJReportCurrentDay(dayNumber);
                } else {
                    return readEJReportDayNumber(dayNumber);
                }

            case SmFptrConst.SMFPTR_JRN_REPORT_DOC_NUMBER:
                int docNumber = params[1];
                return readEJReportDocNumber(docNumber);

            case SmFptrConst.SMFPTR_JRN_REPORT_DOC_RANGE:
                int firstDocNumber = params[1];
                int lastDocNumber = params[2];
                return readEJReportDocRange(firstDocNumber, lastDocNumber);
                
            default:
                throw new Exception("Invalid ReportType value");
        }
    }

    
}
