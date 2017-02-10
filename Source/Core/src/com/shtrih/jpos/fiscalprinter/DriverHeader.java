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

public class DriverHeader implements JposConst, PrinterHeader {

    private int lineNumber = 0;
    private final SMFiscalPrinter printer;
    private final List<HeaderLine> header = new Vector<HeaderLine>();
    private final List<HeaderLine> trailer = new Vector<HeaderLine>();
    private final CompositeLogger logger = CompositeLogger.getLogger(DriverHeader.class);

    /**
     * Creates a new instance of PrinterHeader
     */
    public DriverHeader(SMFiscalPrinter printer) {
        this.printer = printer;
    }

    public SMFiscalPrinter getPrinter(){
        return printer;
    }
    
    public FptrParameters getParams() {
        return printer.getParams();
    }

    public PrinterModel getModel() throws Exception {
        return printer.getModel();
    }

    @Override
    public HeaderLine getHeaderLine(int number) throws Exception {
        checkHeaderLineNumber(number);
        return header.get(number - 1);
    }

    @Override
    public HeaderLine getTrailerLine(int number) throws Exception {
        checkTrailerLineNumber(number);
        return trailer.get(number - 1);
    }

    @Override
    public void initDevice() throws Exception {
        logger.debug("initDevice");

        setNumHeaderLines(printer.getParams().numHeaderLines);
        setNumTrailerLines(printer.getParams().numTrailerLines);

        ReadTableInfo tableStructure = printer.readTableInfo(PrinterConst.SMFP_TABLE_TEXT);
        int rowCount = tableStructure.getRowCount();
        for (int row = 1; row <= rowCount; row++) {
            int result = printer.writeTable(PrinterConst.SMFP_TABLE_TEXT, row, 1, "");
            if (printer.failed(result)) {
                break;
            }
        }
    }

    @Override
    public int getNumHeaderLines() {
        return getParams().numHeaderLines;
    }

    @Override
    public int getNumTrailerLines() {
        return getParams().numTrailerLines;
    }

    @Override
    public void setNumHeaderLines(int numHeaderLines) throws Exception {
        getParams().numHeaderLines = numHeaderLines;
        for (int i = header.size(); i <= numHeaderLines; i++) {
            header.add(new HeaderLine());
        }
    }

    @Override
    public void setNumTrailerLines(int numTrailerLines) throws Exception {
        getParams().numTrailerLines = numTrailerLines;
        for (int i = trailer.size(); i <= numTrailerLines; i++) {
            trailer.add(new HeaderLine());
        }
    }

    @Override
    public void setHeaderLine(int number, String text, boolean doubleWidth)
            throws Exception {
        checkHeaderLineNumber(number);
        header.set(number - 1, new HeaderLine(text, doubleWidth));
    }

    @Override
    public void setTrailerLine(int number, String text, boolean doubleWidth)
            throws Exception {
        checkTrailerLineNumber(number);
        trailer.set(number - 1, new HeaderLine(text, doubleWidth));
    }

    private void checkHeaderLineNumber(int number) throws Exception {
        if ((number < 1) || (number > getNumHeaderLines())) {
            throw new JposException(JposConst.JPOS_E_ILLEGAL,
                    Localizer.getString(Localizer.InvalidLineNumber));
        }
    }

    private void checkTrailerLineNumber(int number) throws Exception {
        if ((number < 1) || (number > getNumTrailerLines())) {
            throw new JposException(JposConst.JPOS_E_ILLEGAL,
                    Localizer.getString(Localizer.InvalidLineNumber));
        }
    }

    private int printLine(HeaderLine line) throws Exception {
        FontNumber font = printer.getParams().getFont();
        if (line.isDoubleWidth()) {
            font = FontNumber.getDoubleFont();
        }
        printer.printLine(PrinterConst.SMFP_STATION_RECJRN, line.getText(),
                font);
        return getModel().getFontHeight(font);
    }

    @Override
    public void endDocument(String additionalHeader, String additionalTrailer)
            throws Exception {
        printTrailer(additionalTrailer);
        printHeaderBeforeCutter();
        printer.cutPaper();
    }

    @Override
    public void beginDocument(String additionalHeader, String additionalTrailer) throws Exception {
        printHeaderAfterCutter(additionalHeader);
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
        printer.waitForPrinting();
    }

    void printHeaderBeforeCutter() throws Exception {
        printer.waitForPrinting();
        int imageHeight = 0;
        int lineHeight = printer.getLineHeight(new FontNumber(
                PrinterConst.FONT_NUMBER_NORMAL));
        int lineSpacing = printer.getLineSpacing();
        int headerHeight = getModel().getHeaderHeight();
        PrinterImage image = printer
                .getPrinterImage(SmFptrConst.SMFPTR_LOGO_BEFORE_HEADER);
        if (image != null) {
            imageHeight = image.getHeight() + lineSpacing;
        }
        if (imageHeight > headerHeight) {
            if (getParams().logoMode == SmFptrConst.SMFPTR_LOGO_MODE_SPLIT_IMAGE)
            {
                int firstLine = image.getStartPos() + 1;
                printer.printGraphics(firstLine, firstLine + headerHeight);
                printer.waitForPrinting();
            } else {
                printBlankSpace(headerHeight);
            }
        } else {
            lineNumber = (headerHeight - imageHeight) / lineHeight;
            if (lineNumber > 0) {
                printRecLine(" ");
                lineNumber--;
            }
            printer.printReceiptImage(SmFptrConst.SMFPTR_LOGO_BEFORE_HEADER);
            printLines(header, 1, lineNumber);
        }
        printer.waitForPrinting();
    }
    
    void printHeaderAfterCutter(String additionalHeader) throws Exception {
        printer.waitForPrinting();
        int imageHeight = 0;
        int lineHeight = printer.getLineHeight(new FontNumber(PrinterConst.FONT_NUMBER_NORMAL));
        int lineSpacing = printer.getLineSpacing();
        int headerHeight = getModel().getHeaderHeight();
        PrinterImage image = printer.getPrinterImage(SmFptrConst.SMFPTR_LOGO_BEFORE_HEADER);
        if (image != null) {
            imageHeight = image.getHeight() + lineSpacing;
        }
        if (imageHeight > headerHeight) {
            if (getParams().logoMode == SmFptrConst.SMFPTR_LOGO_MODE_SPLIT_IMAGE)
            {
                int firstLine = image.getStartPos() + 1;
                printer.printGraphics(firstLine + headerHeight + 1,
                        image.getEndPos());
            } else {
                printer.printReceiptImage(SmFptrConst.SMFPTR_LOGO_BEFORE_HEADER);
            }
            printLines(header);
        } else {
            printLines(header, lineNumber + 1, header.size());
            printer.printReceiptImage(SmFptrConst.SMFPTR_LOGO_AFTER_HEADER);
        }
        if (additionalHeader.length() > 0) {
            printer.printText(PrinterConst.SMFP_STATION_REC, additionalHeader,
                    printer.getParams().getFont());
        }
        printer.waitForPrinting();
    }

    private void printLines(List<HeaderLine> lines) throws Exception {
        for (HeaderLine line : lines) {
            printLine(line);
        }
    }

    private int printLines(List<HeaderLine> lines, int num1, int num2) throws Exception {
        int result = 0;
        for (int i = num1 - 1; i < num2; i++) {
            result += printLine(lines.get(i));
        }
        return result;
    }

    private void printBlankSpace(int height) throws Exception {
        int lineHeight = printer.getLineHeight(FontNumber.getNormalFont());
        int lineCount = (height + lineHeight - 1) / lineHeight;
        for (int i = 0; i < lineCount; i++) {
            printRecLine(" ");
        }
        printer.waitForPrinting();
    }

    void printSpaceLines(int count) throws Exception {
        for (int i = 0; i < count; i++) {
            printRecLine(" ");
        }
        printer.waitForPrinting();
    }

    private void printTrailer() throws Exception {
        for (int i = 1; i <= getNumTrailerLines(); i++) {
            printLine(getTrailerLine(i));
        }
        printRecLine(" ");
    }

    private void printRecLine(String line) throws Exception {
        printer.printLine(PrinterConst.SMFP_STATION_REC, line, FontNumber.getNormalFont());
    }

}
