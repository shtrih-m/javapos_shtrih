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
import com.shtrih.util.Localizer;
import com.shtrih.fiscalprinter.FontNumber;
import com.shtrih.fiscalprinter.SMFiscalPrinter;
import com.shtrih.fiscalprinter.command.PrinterConst;
import com.shtrih.fiscalprinter.command.ReadTableInfo;
import com.shtrih.util.CompositeLogger;

public class DriverTrailer implements JposConst, PrinterHeader {

    private int count = 0;
    private String additionalTrailer = "";
    private final SMFiscalPrinter printer;
    private final Vector list = new Vector();
    public static CompositeLogger logger = CompositeLogger.getLogger(DriverTrailer.class);

    /**
     * Creates a new instance of PrinterHeader
     */
    public DriverTrailer(SMFiscalPrinter printer) {
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
        HeaderLine headerLine = new HeaderLine();
        headerLine.setText(line);
        list.set(lineNumber - 1, headerLine);
    }

    public void addLine(String text) throws Exception {
        HeaderLine headerLine = new HeaderLine();
        headerLine.setText(text);
        list.add(headerLine);
    }

    public void writeLine(int lineNumber, String text) throws Exception {
        checkNumber(lineNumber);
        setLine(lineNumber, text);
    }

    public void initDevice() throws Exception {
    }

    public void setAdditionalText(String text) {
        this.additionalTrailer = text;
    }

    public void printRecMessages(Vector printItems) throws Exception {
        try {
            while (printItems.size() > 0) {
                PrintItem item = (PrintItem) printItems.get(0);
                item.print(printer);
                printItems.remove(0);
            }
        } catch (Exception e) {
            logger.error(e);
        }

    }

    public void print() throws Exception 
    {
        logger.debug("print");
        printer.waitForPrinting();
        printer.printReceiptImage(SmFptrConst.SMFPTR_LOGO_BEFORE_TRAILER);
        for (int i = 0; i < size(); i++) {
            HeaderLine line = get(i);
            printHeaderLine(line);
        }
        printer.printReceiptImage(SmFptrConst.SMFPTR_LOGO_AFTER_TRAILER);

        if (additionalTrailer.length() > 0) {

            printer.printText(PrinterConst.SMFP_STATION_REC, additionalTrailer,
                    printer.getParams().getFont());
        }
        printer.printReceiptImage(SmFptrConst.SMFPTR_LOGO_AFTER_ADDTRAILER);
    }

    private int printHeaderLine(HeaderLine line)
            throws Exception {
        FontNumber font = printer.getParams().font;
        printer.printLine(PrinterConst.SMFP_STATION_RECJRN, line.getText(),
                font);
        int lineHeight = printer.getModel().getFontHeight(font);
        return lineHeight;
    }

}
