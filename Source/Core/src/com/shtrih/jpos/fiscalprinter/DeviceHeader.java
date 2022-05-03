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
    private final ReceiptLines header;
    private final ReceiptLines trailer;
    private final CompositeLogger logger = CompositeLogger.getLogger(DeviceHeader.class);

    public DeviceHeader(SMFiscalPrinter printer) throws Exception {
        this.printer = printer;
        header = new ReceiptLines(printer.getNumHeaderLines());
        trailer = new ReceiptLines(printer.getNumTrailerLines());
    }

    @Override
    public int getNumHeaderLines() throws Exception {
        return printer.getNumHeaderLines();
    }

    @Override
    public int getNumTrailerLines() throws Exception {
        return printer.getNumTrailerLines();
    }

    // numHeaderLines for device cannot be changed
    @Override
    public void setNumHeaderLines(int numHeaderLines) throws Exception {
    }

    // numTrailerLines for device cannot be changed
    @Override
    public void setNumTrailerLines(int numTrailerLines) throws Exception {
    }

    @Override
    public void initDevice() throws Exception {
        logger.debug("initDevice");

        header.setCount(printer.getNumHeaderLines());
        trailer.setCount(printer.getNumTrailerLines());
        printer.updateTableText();
    }

    @Override
    public ReceiptLine getHeaderLine(int number) throws Exception {
        return header.getLine(number);
    }

    @Override
    public ReceiptLine getTrailerLine(int number) throws Exception {
        return trailer.getLine(number);
    }

    @Override
    public void setHeaderLine(int number, String text, boolean doubleWidth)
            throws Exception {
        header.setLine(number, text, doubleWidth);

        int table = printer.getModel().getHeaderTableNumber();
        int row = printer.getHeaderTableRow();
        printer.writeTable(table, row + number - 1, 1, text);
    }

    @Override
    public void setTrailerLine(int number, String text, boolean doubleWidth)
            throws Exception {
        trailer.setLine(number, text, doubleWidth);

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
        if (getNumTrailerLines() >= 0) {
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

    public ReceiptLines getHeaderLines() {
        return header;
    }

    public ReceiptLines getTrailerLines() {
        return trailer;
    }

}
