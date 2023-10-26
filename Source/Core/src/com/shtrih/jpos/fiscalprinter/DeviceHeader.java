package com.shtrih.jpos.fiscalprinter;

import com.shtrih.fiscalprinter.FontNumber;
import com.shtrih.fiscalprinter.SMFiscalPrinter;
import com.shtrih.fiscalprinter.command.PrinterConst;
import com.shtrih.fiscalprinter.command.ReadTableInfo;
import com.shtrih.util.CompositeLogger;
import com.shtrih.util.Localizer;
import java.util.List;

import java.util.Vector;

import jpos.JposConst;
import jpos.JposException;

public class DeviceHeader implements PrinterHeader {

    private final SMFiscalPrinter printer;
    private final CompositeLogger logger = CompositeLogger.getLogger(DeviceHeader.class);

    public DeviceHeader(SMFiscalPrinter printer) throws Exception {
        this.printer = printer;
    }

    @Override
    public void initDevice() throws Exception {
        logger.debug("initDevice");

        printer.getParams().setNumHeaderLines(printer.getNumHeaderLines());
        printer.getParams().setNumTrailerLines(printer.getNumTrailerLines());
        printer.updateTableText();
    }

    @Override
    public void setHeaderLine(int number, String text, boolean doubleWidth)
            throws Exception {
        int table = printer.getModel().getHeaderTableNumber();
        int row = printer.getHeaderTableRow();
        printer.writeTable(table, row + number - 1, 1, text);
    }

    @Override
    public void setTrailerLine(int number, String text, boolean doubleWidth)
            throws Exception {
        int table = printer.getModel().getTrailerTableNumber();
        int row = printer.getModel().getTrailerTableRow();
        printer.writeTable(table, row + number - 1, 1, text);
    }

    @Override
    public void beginDocument(String additionalHeader)
            throws Exception {
        printer.printReceiptImage(SmFptrConst.SMFPTR_LOGO_AFTER_HEADER);
        if (additionalHeader.length() > 0) {
            printer.printText(PrinterConst.SMFP_STATION_REC, additionalHeader,
                    printer.getParams().getFont());
        }
    }

    void printTrailer(String additionalTrailer) throws Exception {
        printer.waitForPrinting();
        printer.printReceiptImage(SmFptrConst.SMFPTR_LOGO_BEFORE_TRAILER);
        printer.printReceiptImage(SmFptrConst.SMFPTR_LOGO_AFTER_TRAILER);
        if (additionalTrailer.length() > 0) {
            printer.printText(PrinterConst.SMFP_STATION_REC, additionalTrailer,
                    printer.getParams().getFont());
        }
        printer.printReceiptImage(SmFptrConst.SMFPTR_LOGO_AFTER_ADDTRAILER);
        if (printer.getParams().getNumTrailerLines() >= 0) {
            printRecLine(" ");
            printRecLine(" ");
        }
        printer.waitForPrinting();
    }

    private void printRecLine(String line) throws Exception {
        printer.printLine(PrinterConst.SMFP_STATION_REC, line, FontNumber.getNormalFont());
    }

    void printHeader(String additionalHeader) throws Exception {
        printer.waitForPrinting();
        printer.printReceiptImage(SmFptrConst.SMFPTR_LOGO_AFTER_HEADER);
        if (additionalHeader.length() > 0) {
            printer.printText(PrinterConst.SMFP_STATION_REC, additionalHeader,
                    printer.getParams().getFont());
        }
        printer.waitForPrinting();
    }

    @Override
    public void endNonFiscal(String additionalTrailer)
            throws Exception {
        printTrailer(additionalTrailer);
        printer.printTrailer();
        printer.printDocEnd();
    }

    public void endFiscal(String additionalTrailer)
            throws Exception {
    }

}
