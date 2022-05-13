/*
 * PrinterHeader.java
 *
 * Created on March 21 2008, 14:58
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.shtrih.jpos.fiscalprinter;

import com.shtrih.fiscalprinter.FontNumber;
import com.shtrih.fiscalprinter.SMFiscalPrinter;
import com.shtrih.fiscalprinter.command.PrinterConst;
import com.shtrih.fiscalprinter.command.ReadTableInfo;
import com.shtrih.fiscalprinter.model.PrinterModel;
import com.shtrih.util.CompositeLogger;
import com.shtrih.util.Localizer;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import jpos.JposConst;
import jpos.JposException;

public class DriverHeader implements PrinterHeader {

    private int lineNumber = 0;
    private final ReceiptLines header;
    private final ReceiptLines trailer;
    private final SMFiscalPrinter printer;
    private final CompositeLogger logger = CompositeLogger.getLogger(DriverHeader.class);

    /**
     * Creates a new instance of PrinterHeader
     */
    public DriverHeader(SMFiscalPrinter printer) throws Exception {
        this.printer = printer;
        
        int numHeaderLines = printer.getParams().numHeaderLines;
        header = new ReceiptLines(numHeaderLines);
        int numTrailerLines = printer.getParams().numTrailerLines;
        trailer = new ReceiptLines(numTrailerLines);
    }

    public SMFiscalPrinter getPrinter() {
        return printer;
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
    public void initDevice() throws Exception {
        logger.debug("initDevice");
        printer.clearTableText();
    }

    @Override
    public int getNumHeaderLines() throws Exception {
        return header.getCount();
    }

    @Override
    public int getNumTrailerLines() {
        return trailer.getCount();
    }

    @Override
    public void setNumHeaderLines(int numHeaderLines) throws Exception {
        header.setCount(numHeaderLines);
    }

    @Override
    public void setNumTrailerLines(int numTrailerLines) throws Exception {
        trailer.setCount(numTrailerLines);
    }

    @Override
    public void setHeaderLine(int number, String text, boolean doubleWidth)
            throws Exception {
        header.setLine(number, text, doubleWidth);
    }

    @Override
    public void setTrailerLine(int number, String text, boolean doubleWidth)
            throws Exception {
        trailer.setLine(number, text, doubleWidth);
    }

    private void printLine(ReceiptLine line) throws Exception {
        FontNumber font = printer.getParams().getFont();
        if (line.isDoubleWidth()) {
            font = FontNumber.getDoubleFont();
        }
        printer.printLine(PrinterConst.SMFP_STATION_RECJRN, line.getText(),
                font);
    }

    public void endDocument(String additionalTrailer)
            throws Exception {
        printTrailer(additionalTrailer);
        if (printer.getCapCutter()) {
            printHeaderBeforeCutter();
            printer.cutPaper();
            printHeaderAfterCutter();
        } else {
            printHeaderNoCutter();
        }
    }

    void printTrailer(String additionalTrailer) throws Exception {
        printer.waitForPrinting();
        printer.printReceiptImage(SmFptrConst.SMFPTR_LOGO_BEFORE_TRAILER);
        printTrailer();
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

    void printHeaderBeforeCutter() throws Exception {
        printer.waitForPrinting();
        int imageHeight = 0;

        int lineHeight = printer.getLineHeight(FontNumber.getNormalFont());
        int headerHeight = printer.getHeaderHeight();
        PrinterImage image = printer
                .getPrinterImage(SmFptrConst.SMFPTR_LOGO_BEFORE_HEADER);
        if (image != null) {
            imageHeight = image.getHeight() + printer.getLineSpacing();
        }
        if (imageHeight > headerHeight) {
            if (printer.getParams().logoMode == SmFptrConst.SMFPTR_LOGO_MODE_SPLIT_IMAGE) {
                int firstLine = image.getStartPos() + 1;
                printer.printGraphics(firstLine, firstLine + headerHeight);
                printer.waitForPrinting();
            } else {
                printBlankSpace(headerHeight);
            }
        } else {
            printer.printReceiptImage(SmFptrConst.SMFPTR_LOGO_BEFORE_HEADER);
            lineNumber = (headerHeight - imageHeight) / lineHeight;
            printLines(header, 1, lineNumber);
        }
        printer.waitForPrinting();
    }

    public void printHeaderNoCutter() throws Exception {
        printer.printReceiptImage(SmFptrConst.SMFPTR_LOGO_BEFORE_HEADER);
        printLines(header);
        printer.waitForPrinting();
        printer.printReceiptImage(SmFptrConst.SMFPTR_LOGO_AFTER_HEADER);
        printer.waitForPrinting();
    }

    public void printHeaderAfterCutter() throws Exception {
        printer.waitForPrinting();
        int imageHeight = 0;
        int lineHeight = printer.getLineHeight(new FontNumber(PrinterConst.FONT_NUMBER_NORMAL));
        int headerHeight = printer.getHeaderHeight();
        PrinterImage image = printer.getPrinterImage(SmFptrConst.SMFPTR_LOGO_BEFORE_HEADER);
        if (image != null) {
            imageHeight = image.getHeight() + printer.getLineSpacing();
        }
        if (imageHeight > headerHeight) {
            if (printer.getParams().logoMode == SmFptrConst.SMFPTR_LOGO_MODE_SPLIT_IMAGE) {
                int firstLine = image.getStartPos() + 1;
                printer.printGraphics(firstLine + headerHeight + 1,
                        image.getEndPos());
            } else {
                printer.printReceiptImage(SmFptrConst.SMFPTR_LOGO_BEFORE_HEADER);
            }
            printLines(header);
        } else {
            lineNumber = (headerHeight - imageHeight) / lineHeight;
            printLines(header, lineNumber + 1, header.getCount());
        }
        printer.printReceiptImage(SmFptrConst.SMFPTR_LOGO_AFTER_HEADER);
        printer.waitForPrinting();
    }

    private void printLines(ReceiptLines lines) throws Exception {
        for (int i = 1; i <= lines.getCount(); i++) {
            printLine(lines.getLine(i));
        }
    }

    private void printLines(ReceiptLines lines, int num1, int num2) throws Exception {
        for (int i = num1; i <= num2; i++) {
            if (i > lines.getCount()) {
                break;
            }
            printLine(lines.getLine(i));
        }
    }

    private void printBlankSpace(int height) throws Exception {
        int lineHeight = printer.getLineHeight(FontNumber.getNormalFont());
        int lineCount = (height + lineHeight - 1) / lineHeight;
        for (int i = 1; i <= lineCount; i++) {
            printRecLine(" ");
        }
        printer.waitForPrinting();
    }

    void printSpaceLines(int count) throws Exception {
        for (int i = 1; i <= count; i++) {
            printRecLine(" ");
        }
        printer.waitForPrinting();
    }

    private void printTrailer() throws Exception {
        for (int i = 1; i <= getNumTrailerLines(); i++) {
            printLine(getTrailerLine(i));
        }
    }

    private void printRecLine(String line) throws Exception {
        printer.printLine(PrinterConst.SMFP_STATION_REC, line, FontNumber.getNormalFont());
    }

    @Override
    public void endFiscal(String additionalTrailer)
            throws Exception {
        logger.debug("endFiscal");
        endDocument(additionalTrailer);
    }

    @Override
    public void endNonFiscal(String additionalTrailer)
            throws Exception {
        logger.debug("endNonFiscal");
        endDocument(additionalTrailer);
    }

    public void printText(String text) throws Exception {
        if (text.length() > 0) {
            printer.printText(PrinterConst.SMFP_STATION_REC, text,
                    printer.getParams().getFont());
        }
        printer.waitForPrinting();
    }

    @Override
    public void beginDocument(String additionalHeader) throws Exception {
        logger.debug("beginDocument");
        printText(additionalHeader);
    }

    public ReceiptLines getHeaderLines() {
        return header;
    }

    public ReceiptLines getTrailerLines() {
        return trailer;
    }

}
