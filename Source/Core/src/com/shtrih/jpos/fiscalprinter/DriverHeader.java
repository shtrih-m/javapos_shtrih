/*
 * PrinterHeader.java
 *
 * Created on March 21 2008, 14:58
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.shtrih.jpos.fiscalprinter;

import java.util.Vector;

import jpos.JposConst;
import jpos.JposException;
import com.shtrih.util.CompositeLogger;
import com.shtrih.util.Localizer;
import com.shtrih.fiscalprinter.FontNumber;
import com.shtrih.fiscalprinter.SMFiscalPrinter;
import com.shtrih.fiscalprinter.command.PrinterConst;
import com.shtrih.fiscalprinter.command.ReadTableInfo;

public class DriverHeader implements JposConst, PrinterHeader {

    private int count = 0;
    private final SMFiscalPrinter printer;
    private final Vector list = new Vector();

    /**
     * Creates a new instance of PrinterHeader
     */
    public DriverHeader(SMFiscalPrinter printer) {
        this.printer = printer;
    }

    public void clear() {
        list.clear();
    }

    public void setCount(int count) {
        this.count = count;
        for (int i = list.size(); i <= count; i++) {
            HeaderLine headerLine = new HeaderLine("");
            list.add(headerLine);
        }
    }

    public int size() {
        return count;
    }

    public HeaderLine get(int index) {
        if (validIndex(index)) {
            return (HeaderLine) list.get(index);
        } else {
            return new HeaderLine(" ");
        }
    }

    public void checkNumber(int number) throws Exception {
        if (!validNumber(number)) {
            throw new JposException(JPOS_E_ILLEGAL,
                    Localizer.getString(Localizer.InvalidLineNumber));
        }
    }

    public boolean validNumber(int number) {
        return ((number >= 1) && (number <= size()));
    }

    public boolean validIndex(int index) {
        return ((index >= 0) && (index < size()));
    }

    public void setLine(int lineNumber, String text)
            throws Exception {
        String line = text;
        HeaderLine headerLine = new HeaderLine(line);
        list.set(lineNumber - 1, headerLine);
    }

    public void addLine(String text) throws Exception {
        HeaderLine headerLine = new HeaderLine(text);
        list.add(headerLine);
    }

    public void writeLine(int lineNumber, String text) throws Exception {
        checkNumber(lineNumber);
        setLine(lineNumber, text);
    }

    private int printHeaderLine(HeaderLine line)
            throws Exception 
    {
        FontNumber font = printer.getParams().font;
        printer.printLine(PrinterConst.SMFP_STATION_RECJRN, line.getText(),
                font);
        int lineHeight = printer.getModel().getFontHeight(font);
        return lineHeight;

    }

    public void initDevice() throws Exception {
        ReadTableInfo command = printer
                .readTableInfo(PrinterConst.SMFP_TABLE_TEXT);
        int rowCount = command.getTable().getRowCount();
        for (int row = 1; row <= rowCount; row++) {
            int result = printer.writeTable(PrinterConst.SMFP_TABLE_TEXT, row,
                    1, "");
            if (printer.failed(result)) {
                break;
            }
        }
    }

    public int printLines() throws Exception {
        int lengthInDots = 0;
        for (int i = 0; i < size(); i++) {
            HeaderLine line = get(i);
            lengthInDots += printHeaderLine(line);
        }
        return lengthInDots;
    }

    public int print(int num1, int num2)
            throws Exception {
        int lineHeight = 0;
        for (int i = num1 - 1; i < num2; i++) {
            lineHeight += printHeaderLine(get(i));
        }
        return lineHeight;
    }

    public void print() throws Exception
    {
        int imageHeight = 0;
        FontNumber font = printer.getParams().font;
        int lineHeight = printer.getLineHeight(font);
        int lineSpacing = printer.getLineSpacing();
        int headerHeight = printer.getModel().getHeaderHeight();
        PrinterImage image = printer.getPrinterImage(SmFptrConst.SMFPTR_LOGO_BEFORE_HEADER);
        if (image != null) {
            imageHeight = image.getHeight() + lineSpacing;
        }
        if (imageHeight > headerHeight) 
        {
            if ((printer.getParams().logoMode == SmFptrConst.SMFPTR_LOGO_MODE_SPLIT_IMAGE)
                    && (printer.getModel().getCapParameter(PrinterConst.SMFP_PARAMID_LINE_SPACING))) {
                int ls = printer.readIntParameter(
                        PrinterConst.SMFP_PARAMID_LINE_SPACING);
                printer.writeParameter(PrinterConst.SMFP_PARAMID_LINE_SPACING, 0);
                int firstLine = image.getStartPos() + 1;
                printer.printGraphics(firstLine, firstLine + headerHeight);
                printer.waitForPrinting();
                printer.cutPaper();
                printer.printGraphics(firstLine + headerHeight + 1, image.getEndPos());
                printer.writeParameter(PrinterConst.SMFP_PARAMID_LINE_SPACING, ls);

            } else {
                printer.printBlankSpace(headerHeight);
                printer.cutPaper();
                printer.printReceiptImage(SmFptrConst.SMFPTR_LOGO_BEFORE_HEADER);
            }
            printLines();
        } else 
        {
            int lineNumber = (headerHeight - imageHeight) / lineHeight;
            int spaceHeight = (headerHeight - imageHeight) % lineHeight;
            if (spaceHeight > 0) {
                printer.printBlankSpace(spaceHeight);
            }
            printer.printReceiptImage(SmFptrConst.SMFPTR_LOGO_BEFORE_HEADER);
            print(1, lineNumber);
            printer.waitForPrinting();
            printer.cutPaper();
            print(lineNumber + 1, size());
            printer.printReceiptImage(SmFptrConst.SMFPTR_LOGO_AFTER_HEADER);
        }
    }

    public void setAdditionalText(String text){
    }
    
    public void printRecMessages(Vector printItems) throws Exception{
    }
    
}
