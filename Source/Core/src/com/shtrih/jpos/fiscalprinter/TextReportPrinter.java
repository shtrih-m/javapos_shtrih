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
    }

    public void start() {
        printer.setTextDocumentFilterEnablinessTo(false);
    }

    public void stop() {
        printer.setTextDocumentFilterEnablinessTo(true);
    }

    private void printLines(List<String> lines) throws Exception {
        for (String line : lines) {
            printer.getPrinter().printLine(PrinterConst.SMFP_STATION_REC, line, FontNumber.getNormalFont());
        }
    }

    public void printEJReportDayNumber(int dayNumber) throws Exception {
        List<String> dst = storage.searchZReport(dayNumber);

        if (dst == null) {
            throw new Exception(String.format("Смена № %d не найдена", dayNumber));
        }

        String header = String.format("Контрольная лента Смена № %d", dayNumber);
        dst.add(0, header);
        printLines(dst);
    }

    public void printEJReportDocNumber(int docNumber) throws Exception {
        List<String> dst = storage.getDocument(docNumber);
        if (dst == null) {
            throw new Exception(String.format("Документ № %d не найден", docNumber));
        }

        String header = String.format("Контрольная лента Документ № %d", docNumber);
        dst.add(0, header);
        printLines(dst);
    }

    public void printEJReportDocRange(int N1, int N2) throws Exception {
        if (N1 > N2) {
            throw new Exception(String.format("Номер первого документа больше второго (%d > %d)", N1, N2));
        }

        if (N1 == N2) {
            printEJReportDocNumber(N1);
            return;
        }

        List<String> dst = new ArrayList<String>();
        String header = String.format("Контрольная лента Документ с № %d по № %d", N1, N2);
        dst.add(0, header);

        for (int i = N1; i <= N2; i++) {
            List<String> document = storage.getDocument(i);
            if (document == null)
                continue;

            for (String s : document) {
                dst.add(s);
            }
        }

        printLines(dst);
    }

    public void printEJReportCurrentDay(int dayNumber) throws Exception {
        List<String> dst = storage.getCurrentDayReport();

        if (dst == null) {
            throw new Exception(String.format("Смена № %d не найдена", dayNumber));
        }

        String header = String.format("Контрольная лента Смена № %d", dayNumber);
        dst.add(0, header);
        printLines(dst);
    }
}
